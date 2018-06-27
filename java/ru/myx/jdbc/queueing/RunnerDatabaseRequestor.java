/**
 * 
 */
package ru.myx.jdbc.queueing;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Map;

import ru.myx.ae3.act.ActService;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.status.StatusFiller;
import ru.myx.ae3.status.StatusInfo;
import ru.myx.util.Counter;
import ru.myx.util.FifoQueueMultithreadEnqueue;
import ru.myx.util.FifoQueueServiceMultithreadSwitching;

/**
 * @author myx
 * 
 */
public class RunnerDatabaseRequestor implements ActService, StatusFiller {
	
	private static final int													ADDED_INIT		= 256;
	
	private RequestAttachment<?, ?>[]											added			= new RequestAttachment[RunnerDatabaseRequestor.ADDED_INIT];
	
	private int																	addedCapacity	= RunnerDatabaseRequestor.ADDED_INIT;
	
	private int																	addedSize		= 0;
	
	private int																	addedSizeMax	= 0;
	
	private Connection															conn;
	
	private final Counter														stsExecution	= new Counter();
	
	private boolean																destroyed		= false;
	
	private final String														name;
	
	private final FifoQueueServiceMultithreadSwitching<RequestAttachment<?, ?>>	queue			= new FifoQueueServiceMultithreadSwitching<>();
	
	private final Enumeration<Connection>										connectionSource;
	
	private int																	stsAdded;
	
	private int																	stsFlushed;
	
	private int																	stsInnerLoops;
	
	private int																	stsLoops;
	
	private int																	stsSkipped;
	
	private int																	stsUnhandled;
	
	/**
	 * @param name
	 * @param server
	 */
	public RunnerDatabaseRequestor(final String name, final Enumeration<Connection> server) {
		this.name = name;
		this.connectionSource = server;
	}
	
	/**
	 * @param record
	 */
	public final void add(final RequestAttachment<?, ?> record) {
		this.stsAdded++;
		if (this.destroyed) {
			throw new IllegalStateException( "Searcher is already destroyed!" );
		}
		try {
			this.queue.offerLast( record );
		} catch (final Throwable t) {
			Report.exception( "PROCESS-SERIALIZER{" + this.name + "}", "add", t );
		}
	}
	
	/**
	 * @return connection
	 */
	public final Connection ctxGetConnection() {
		return this.conn;
	}
	
	/**
	 * @throws Exception
	 */
	protected void loopPrepare() throws Exception {
		// ignore
	}
	
	/**
	 * 
	 */
	protected void loopRelease() {
		// ignore
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final boolean main() throws Throwable {
		/**
		 * wait for the first event queued
		 */
		this.queue.switchPlanesWaitReady( 0L );
		
		final Map<String, RequestAttachment<?, ?>> map = Create.treeMap();
		final Connection conn = this.connectionSource.nextElement();
		if (conn == null) {
			Report.error( "PROCESS-SERIALIZER{" + this.name + "}",
					"null connection, sleeping for 2.5 seconds, then retry..." );
			try {
				Thread.sleep( 2500L );
			} catch (final InterruptedException e) {
				return false;
			}
			return true;
		}
		this.stsLoops++;
		this.conn = conn;
		try {
			this.loopPrepare();
			for (int loops = 256; loops > 0; loops--) {
				{
					/**
					 * linger for 2500 milliseconds.
					 */
					final FifoQueueMultithreadEnqueue<RequestAttachment<?, ?>> queue = this.queue
							.switchPlanesWait( 2500L );
					if (queue == null) {
						break;
					}
					for (RequestAttachment<?, ?> target; (target = queue.pollFirst()) != null;) {
						this.stsFlushed++;
						if (this.addedSize == this.addedCapacity) {
							final RequestAttachment<?, ?>[] newAdded = new RequestAttachment[this.addedCapacity <<= 1];
							System.arraycopy( this.added, 0, newAdded, 0, this.addedSize );
							this.added = newAdded;
						}
						this.added[this.addedSize++] = target;
					}
				}
				if (this.addedSize > 0) {
					this.stsInnerLoops++;
					if (this.addedSizeMax < this.addedSize) {
						this.addedSizeMax = this.addedSize;
					}
					try {
						while (this.addedSize > 0) {
							final int index = --this.addedSize;
							@SuppressWarnings("rawtypes")
							final RequestAttachment target = this.added[index];
							if (target != null && target.isValid()) {
								this.added[index] = null;
								final String key = target.getKey();
								@SuppressWarnings("rawtypes")
								final RequestAttachment other = key == null
										? null
										: map.put( key, target );
								if (other != null) {
									target.setDuplicateOf( other );
									this.stsSkipped++;
								} else {
									final long started = System.currentTimeMillis();
									try {
										target.apply( this );
									} catch (final Error e) {
										Report.exception( "PROCESS-SERIALIZER{" + this.name + "}", "error on task: "
												+ target.getClass()
												+ "/"
												+ target.getKey(), e );
										target.setError( e );
									} catch (final Throwable e) {
										Report.exception( "PROCESS-SERIALIZER{" + this.name + "}",
												"throwable on task: " + target.getClass() + "/" + target.getKey(),
												e );
										target.setError( new Error( e ) );
									}
									this.stsExecution.register( System.currentTimeMillis() - started );
								}
							}
						}
					} finally {
						if (map.size() > 0) {
							map.clear();
						}
					}
				}
			}
		} finally {
			try {
				this.loopRelease();
			} catch (final Throwable t) {
				// ignore
			}
			try {
				conn.close();
			} catch (final Throwable t) {
				// ignore
			}
			this.conn = null;
		}
		return !this.destroyed;
	}
	
	@Override
	public final boolean start() {
		this.destroyed = false;
		return true;
	}
	
	@Override
	public void statusFill(final StatusInfo status) {
		status.put( this.name + ", items added", Format.Compact.toDecimal( this.stsAdded ) );
		status.put( this.name + ", duplicates skipped", Format.Compact.toDecimal( this.stsSkipped ) );
		status.put( this.name + ", tasks executed", Format.Compact.toDecimal( this.stsExecution.getCount() ) );
		status.put( this.name + ", average task time", Format.Compact.toPeriod( this.stsExecution.getAverage() ) );
		status.put( this.name + ", maximal task time", Format.Compact.toPeriod( this.stsExecution.getMaximum() ) );
		status.put( this.name + ", total task time", Format.Compact.toPeriod( this.stsExecution.longValue() ) );
		status.put( this.name + ", queue flushed", Format.Compact.toDecimal( this.stsFlushed ) );
		status.put( this.name + ", main loops", Format.Compact.toDecimal( this.stsLoops ) );
		status.put( this.name + ", inner loops", Format.Compact.toDecimal( this.stsInnerLoops ) );
		status.put( this.name + ", exceptions", Format.Compact.toDecimal( this.stsUnhandled ) );
		status.put( this.name + ", active size", Format.Compact.toDecimal( this.addedSize ) );
		status.put( this.name + ", active size max", Format.Compact.toDecimal( this.addedSizeMax ) );
		status.put( this.name + ", active capacity", Format.Compact.toDecimal( this.addedCapacity ) );
	}
	
	@Override
	public final boolean stop() {
		this.destroyed = true;
		this.queue.switchQueueWaitCancel();
		return false;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(name=" + this.name + ")";
	}
	
	@Override
	public final boolean unhandledException(final Throwable t) {
		this.stsUnhandled++;
		Report.exception( "PROCESS-SERIALIZER{" + this.name + "}", "Unhandled exception in a main loop", t );
		synchronized (this) {
			try {
				// sleep
				Thread.sleep( 99L );
				// wait for incoming
				this.wait( 399L );
			} catch (final InterruptedException e) {
				return false;
			}
		}
		return !this.destroyed;
	}
}
