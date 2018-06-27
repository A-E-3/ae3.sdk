package ru.myx.ae3.vfs.signals;

import java.util.Iterator;
import java.util.Map;

import java.util.function.Function;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.exec.ExecNonMaskedException;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSortMatcher;
import ru.myx.ae3.vfs.ars.ArsStorageImpl;

/**
 * @author myx
 * 
 */
public class StorageImplSignals implements ArsStorageImpl<RecordSignals, ReferenceSignals, ArraySignals> {
	
	private final Map<String, Function<Void, Object>>	signals;
	
	final RecordSignals										root;
	
	private final ReferenceSignals							rootReference;
	
	
	/**
	 * @param signals
	 */
	public StorageImplSignals(final Map<String, Function<Void, Object>> signals) {
	
		this.signals = signals;
		this.root = new RecordSignalsRoot( "signals: " + System.identityHashCode( signals ) );
		this.rootReference = new ReferenceSignals( this.root, this.root, this.root );
		assert this.rootReference.getTarget().isContainer() : "Container!";
	}
	
	
	@Override
	public RecordSignals createBinaryTemplate(
			final TransferCopier copier) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public RecordSignals createContainerTemplate() {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public RecordSignals createKeyForString(
			final String key) {
	
		return new RecordSignals( key, this.signals.get( key ) );
	}
	
	
	@Override
	public RecordSignals createPrimitiveTemplate(
			final Guid guid) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public ReferenceSignals createReferenceTemplate(
			final RecordSignals key,
			final TreeLinkType mode,
			final ReferenceSignals target) {
	
		return new ReferenceSignals( this.root, key, target.getTarget() );
	}
	
	
	@Override
	public RecordSignals createTextTemplate(
			final CharSequence text) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public TransactionSignals createTransaction() {
	
		return new TransactionSignals( this );
	}
	
	
	@Override
	public ReferenceSignals doLinkDelete(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode) {
	
		if (object != this.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.root, key );
		this.executeSignal( key.key );
		return reference;
	}
	
	
	@Override
	public ReferenceSignals doLinkMoveRename(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final RecordSignals newObject,
			final RecordSignals newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.root, key );
		this.executeSignal( key.key );
		return reference;
	}
	
	
	@Override
	public ReferenceSignals doLinkRename(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final RecordSignals newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.root, key );
		this.executeSignal( key.key );
		return reference;
	}
	
	
	@Override
	public ReferenceSignals doLinkSet(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.root, key );
		this.executeSignal( key.key );
		return reference;
	}
	
	
	void executeSignal(
			final String key) {
	
		final Function<Void, Object> signal = this.signals.get( key );
		if (signal == null) {
			Report.info( "STORAGE-IMPL-SIGNALS", "signal " + key + " is unknown" );
			return;
		}
		Report.info( "STORAGE-IMPL-SIGNALS", "executinng: " + key + ", signal=" + signal );
		try {
			signal.apply( null );
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Throwable t) {
			throw new RuntimeException( "While executing control signal, key=" + key + ", error=" + t );
		}
		Report.info( "STORAGE-IMPL-SIGNALS", "executed: " + key );
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final RecordSignals object) {
	
		assert object != null : "NULL object";
		return object.getBinaryContent();
	}
	
	
	@Override
	public ReferenceSignals getLink(
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode) {
	
		if (object != this.root) {
			return null;
		}
		return key.handler == null
				? null
				: new ReferenceSignals( this.root, key );
	}
	
	
	@Override
	public ArraySignals getLinks(
			final RecordSignals object,
			final TreeReadType mode) {
	
		final ArraySignals signals = new ArraySignals();
		for (final Map.Entry<String, Function<Void, Object>> entry : this.signals.entrySet()) {
			signals.add( new ReferenceSignals( this.root, new RecordSignals( entry.getKey(), entry.getValue() ) ) );
		}
		return signals;
	}
	
	
	@Override
	public Value<ArraySignals> getLinksRange(
			final RecordSignals object,
			final RecordSignals keyStart,
			final RecordSignals keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		assert object != null : "NULL object";
		final ArraySignals result = new ArraySignals();
		if (limit == 0) {
			for (final Map.Entry<String, Function<Void, Object>> entry : this.signals.entrySet()) {
				if (keyStart != null || keyStop != null) {
					final String key = entry.getKey();
					if (keyStart != null && keyStart.key.compareTo( key ) >= 0) {
						continue;
					}
					if (keyStop != null && keyStop.key.compareTo( key ) < 0) {
						continue;
					}
				}
				result.add( new ReferenceSignals( this.root, new RecordSignals( entry.getKey(), entry.getValue() ) ) );
			}
			return result;
		}
		{
			final String start = keyStart == null
					? null
					: keyStart.key;
			final String stop = keyStop == null
					? null
					: keyStop.key;
			final VfsDefaultLimitSortMatcher<String, Map.Entry<String, Function<Void, Object>>> matcher = new VfsDefaultLimitSortMatcher<>( start,
					stop,
					limit,
					backwards );
			for (final Map.Entry<String, Function<Void, Object>> entry : this.signals.entrySet()) {
				final String key = entry.getKey();
				matcher.put( key, entry );
			}
			final Iterator<Map.Entry<String, Function<Void, Object>>> iterator = backwards
					? matcher.iteratorDescending()
					: matcher.iteratorAscending();
			while (iterator.hasNext()) {
				final Map.Entry<String, Function<Void, Object>> entry = iterator.next();
				result.add( new ReferenceSignals( this.root, new RecordSignals( entry.getKey(), entry.getValue() ) ) );
			}
			return result;
		}
	}
	
	
	@Override
	public ReferenceSignals getRootReference() {
	
		return this.rootReference;
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final RecordSignals object) {
	
		assert object != null : "NULL object";
		return object.getTextContent();
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		return false;
	}
	
	
	@Override
	public boolean isReadOnly() {
	
		return false;
	}
	
	
	@Override
	public void shutdown() throws Exception {
	
		// do nothing
	}
	
	
	@Override
	public String toString() {
	
		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
