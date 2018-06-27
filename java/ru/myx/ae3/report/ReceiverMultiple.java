/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.report;

import java.util.HashSet;
import java.util.Set;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.status.StatusInfo;

/**
 * @author myx
 * 
 */
public class ReceiverMultiple extends LogReceiver {
	private int						rcv_register_attempts	= 0;
	
	private int						rcv_registered			= 0;
	
	private int						rcv_unregister_attempts	= 0;
	
	private int						rcv_unregistered		= 0;
	
	private int						rcv_count				= 0;
	
	private LogReceiver[]			children;
	
	private final Set<LogReceiver>	allRecievers			= new HashSet<>();
	
	static ReportReceiver			SPY						= null;
	
	private final ReportReceiver	parent;
	
	/**
	 * @param parent
	 */
	/**
	 * @param parent
	 */
	public ReceiverMultiple(final ReportReceiver parent) {
		this.parent = parent == null
				? LogReceiver.DUMMY
				: parent;
		if (Report.MODE_DEBUG) {
			this.event( "JOS5/SML/ERM", "DEBUG", "Object created, parent=" + this.parent );
		}
	}
	
	@Override
	public LogReceiver[] childRecievers() {
		{
			final LogReceiver[] children = this.children;
			if (children != null) {
				return children;
			}
		}
		synchronized (this) {
			final LogReceiver[] children = this.children;
			return children == null
					? this.children = this.allRecievers.toArray( new LogReceiver[this.allRecievers.size()] )
					: children;
		}
	}
	
	@Override
	protected String[] eventClasses() {
		return null;
	}
	
	@Override
	protected String[] eventTypes() {
		return null;
	}
	
	@Override
	protected void onEvent(final Event event) {
		Act.whenIdle( null, new Eventer( event, this.childRecievers(), this.parent ) );
	}
	
	/**
	 * @param er
	 */
	public void register(final LogReceiver er) {
		synchronized (this) {
			this.rcv_register_attempts++;
			if (Report.MODE_DEBUG) {
				if (!this.allRecievers.contains( er )) {
					this.rcv_registered++;
					this.rcv_count++;
					this.allRecievers.add( er );
					this.event( "JOS5/SML/ERM", "DEBUG", "Registration OK, er=" + er );
				} else {
					this.event( "JOS5/SML/ERM", "DEBUG", "Registration, already registered, er=" + er );
				}
			} else {
				if (!this.allRecievers.contains( er )) {
					this.rcv_registered++;
					this.rcv_count++;
					this.allRecievers.add( er );
				}
			}
			this.children = null;
		}
	}
	
	/**
	 * @param recievers
	 */
	public void register(final LogReceiver[] recievers) {
		synchronized (this) {
			if (Report.MODE_DEBUG) {
				if (recievers != null) {
					this.event( "JOS5/SML/ERM", "DEBUG", "Block registration, count=" + recievers.length );
					for (final LogReceiver element : recievers) {
						this.register( element );
					}
					this.event( "JOS5/SML/ERM", "DEBUG", "Block registration finished" );
				}
			} else {
				if (recievers != null) {
					for (final LogReceiver element : recievers) {
						this.register( element );
					}
				}
			}
		}
	}
	
	@Override
	public void statusFill(final StatusInfo data) {
		super.statusFill( data );
		data.put( "Recievers count", this.rcv_count );
		data.put( "Recievers registered", this.rcv_registered );
		data.put( "Registration attempts", this.rcv_register_attempts );
		data.put( "Recievers unregistered", this.rcv_unregistered );
		data.put( "Unregistration attempts", this.rcv_unregister_attempts );
	}
	
	/**
	 * @param er
	 * @return boolean
	 */
	public boolean unregister(final LogReceiver er) {
		synchronized (this) {
			this.rcv_unregister_attempts++;
			final boolean result = this.allRecievers.remove( er );
			if (Report.MODE_DEBUG) {
				if (result) {
					this.rcv_unregistered++;
					this.rcv_count--;
					this.children = null;
					this.event( "JOS5/SML/ERM", "DEBUG", "Unregistration OK, er=" + er );
				} else {
					this.event( "JOS5/SML/ERM", "DEBUG", "Unregistration failed, not known, er=" + er );
				}
			} else {
				if (result) {
					this.rcv_unregistered++;
					this.rcv_count--;
					this.children = null;
				}
			}
			return result;
		}
	}
}
