package ru.myx.ae3.vfs.signals;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.ars.ArsTransaction;
import ru.myx.ae3.vfs.ars.ArsTransactionNested;
import ru.myx.util.QueueStackRecord;

final class TransactionSignals implements ArsTransaction<RecordSignals, ReferenceSignals, ArraySignals> {
	private final StorageImplSignals	storage;
	
	private QueueStackRecord<String>	signals;
	
	
	TransactionSignals(final StorageImplSignals storage) {
	
		this.storage = storage;
	}
	
	
	@Override
	public void cancel() {
	
		this.signals = null;
	}
	
	
	@Override
	public void commit() {
	
		if (this.signals != null) {
			for (; !this.signals.isEmpty();) {
				final String key = this.signals.first();
				this.storage.executeSignal( key );
				this.signals.next();
			}
		}
	}
	
	
	@Override
	public RecordSignals createBinaryTemplate(
			final TransferCopier copier) {
	
		return this.storage.createBinaryTemplate( copier );
	}
	
	
	@Override
	public RecordSignals createContainerTemplate() {
	
		return this.storage.createContainerTemplate();
	}
	
	
	@Override
	public RecordSignals createKeyForString(
			final String key) {
	
		return this.storage.createKeyForString( key );
	}
	
	
	@Override
	public RecordSignals createPrimitiveTemplate(
			final Guid guid) {
	
		return this.storage.createPrimitiveTemplate( guid );
	}
	
	
	@Override
	public ReferenceSignals createReferenceTemplate(
			final RecordSignals key,
			final TreeLinkType mode,
			final ReferenceSignals original) {
	
		return this.storage.createReferenceTemplate( key, mode, original );
	}
	
	
	@Override
	public RecordSignals createTextTemplate(
			final CharSequence text) {
	
		return this.storage.createTextTemplate( text );
	}
	
	
	@Override
	public ArsTransaction<RecordSignals, ReferenceSignals, ArraySignals> createTransaction() throws Exception {
	
		return new ArsTransactionNested<>( this );
	}
	
	
	@Override
	public Value<ReferenceSignals> doLinkDelete(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode) {
	
		if (object != this.storage.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.storage.root, key );
		this.put( key.key );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceSignals> doLinkMoveRename(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final RecordSignals newObject,
			final RecordSignals newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.storage.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.storage.root, key );
		this.put( key.key );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceSignals> doLinkRename(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final RecordSignals newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.storage.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.storage.root, key );
		this.put( key.key );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceSignals> doLinkSet(
			final ReferenceSignals template,
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode,
			final long modified,
			final RecordSignals target) {
	
		if (object != this.storage.root) {
			throw new IllegalAccessError( "Signals itself are not collections!" );
		}
		final ReferenceSignals reference = new ReferenceSignals( this.storage.root, key );
		this.put( key.key );
		return reference;
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final RecordSignals object) {
	
		return this.storage.getBinary( object );
	}
	
	
	@Override
	public Value<ReferenceSignals> getLink(
			final RecordSignals object,
			final RecordSignals key,
			final TreeLinkType mode) {
	
		return this.storage.getLink( object, key, mode );
	}
	
	
	@Override
	public Value<ArraySignals> getLinks(
			final RecordSignals object,
			final TreeReadType mode) {
	
		return this.storage.getLinks( object, mode );
	}
	
	
	@Override
	public Value<ArraySignals> getLinksRange(
			final RecordSignals object,
			final RecordSignals keyStart,
			final RecordSignals keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		return this.storage.getLinksRange( object, keyStart, keyStop, limit, backwards, mode );
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final RecordSignals object) {
	
		return this.storage.getText( object );
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		return this.storage.isHistorySupported();
	}
	
	
	@Override
	public boolean isReadOnly() {
	
		return this.storage.isReadOnly();
	}
	
	
	final void put(
			final String key) {
	
		if (this.signals != null) {
			this.signals = new QueueStackRecord<>();
		}
		this.signals.enqueue( key );
	}
}
