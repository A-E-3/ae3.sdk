package ru.myx.ae3.vfs.ars;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;

/** @author myx
 *
 * @param <O>
 * @param <R>
 * @param <A> */
public abstract class AbstractArsStorageReadOnly<O extends ArsRecord, R extends ArsReference<O>, A extends ArsArray<R>> //
		extends //
			AbstractArsStorage<O, R, A>
		implements //
			ArsInterface.ReadOnly<O, R, A> //
{

	@Override
	public final O createBinaryTemplate(final TransferCopier copier) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final O createContainerTemplate() {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final O createPrimitiveTemplate(final Guid guid) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final O createTextTemplate(final CharSequence text) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final ArsTransaction<O, R, A> createTransaction() throws Exception {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final Value<R> doLinkDelete(final R template, final O object, final O key, final TreeLinkType mode) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final Value<R>
			doLinkMoveRename(final R template, final O object, final O key, final O newObject, final O newKey, final TreeLinkType mode, final long modified, final O target) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final Value<R> doLinkRename(final R template, final O object, final O key, final O newKey, final TreeLinkType mode, final long modified, final O target) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final Value<R> doLinkSet(final R template, final O object, final O key, final TreeLinkType mode, final long modified, final O target) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public final boolean isReadOnly() {

		return true;
	}
}
