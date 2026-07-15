package ru.myx.ae3.vfs.union;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.ars.ArsInterface;
import ru.myx.ae3.vfs.ars.ArsTransaction;

final class TransactionUnion
		implements //
			ArsTransaction<RecordReferenceUnion, RecordReferenceUnion, ArrayUnion>,
			ArsInterface.ReadOnly<RecordReferenceUnion, RecordReferenceUnion, ArrayUnion> //
{

	private final StorageImplUnion storage;

	TransactionUnion(final StorageImplUnion storage) {

		this.storage = storage;
	}

	@Override
	public void cancel() throws Exception {

		// nothing to roll back: stateless read-only view over storage
	}

	@Override
	public void commit() throws Exception {

		// nothing to persist: stateless read-only view over storage
	}

	@Override
	public RecordReferenceUnion createBinaryTemplate(final TransferCopier copier) {

		return this.storage.createBinaryTemplate(copier);
	}

	@Override
	public RecordReferenceUnion createContainerTemplate() {

		return this.storage.createContainerTemplate();
	}

	@Override
	public RecordReferenceUnion createKeyForString(final String key) {

		return this.storage.createKeyForString(key);
	}

	@Override
	public RecordReferenceUnion createPrimitiveTemplate(final Guid guid) {

		return this.storage.createPrimitiveTemplate(guid);
	}

	@Override
	public RecordReferenceUnion createReferenceTemplate(final RecordReferenceUnion key, final TreeLinkType mode, final RecordReferenceUnion original) {

		return this.storage.createReferenceTemplate(key, mode, original);
	}

	@Override
	public RecordReferenceUnion createTextTemplate(final CharSequence text) {

		return this.storage.createTextTemplate(text);
	}

	@Override
	public ArsTransaction<RecordReferenceUnion, RecordReferenceUnion, ArrayUnion> createTransaction() throws Exception {

		return this.storage.createTransaction();
	}

	@Override
	public Value<? extends TransferCopier> getBinary(final RecordReferenceUnion object) {

		return this.storage.getBinary(object);
	}

	@Override
	public Value<RecordReferenceUnion> getLink(final RecordReferenceUnion object, final RecordReferenceUnion key, final TreeLinkType mode) {

		return this.storage.getLink(object, key, mode);
	}

	@Override
	public Value<ArrayUnion> getLinks(final RecordReferenceUnion object, final TreeReadType mode) {

		return this.storage.getLinks(object, mode);
	}

	@Override
	public Value<ArrayUnion> getLinksRange(
			final RecordReferenceUnion object,
			final RecordReferenceUnion keyStart,
			final RecordReferenceUnion keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {

		return this.storage.getLinksRange(object, keyStart, keyStop, limit, backwards, mode);
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordReferenceUnion object) {

		return this.storage.getText(object);
	}

	@Override
	public boolean isHistorySupported() {

		return this.storage.isHistorySupported();
	}
}
