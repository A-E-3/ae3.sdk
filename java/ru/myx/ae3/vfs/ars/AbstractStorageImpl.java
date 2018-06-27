package ru.myx.ae3.vfs.ars;

import ru.myx.ae3.vfs.TreeLinkType;

/**
 * 
 * @author myx
 * 
 * @param <O>
 * @param <R>
 * @param <A>
 */
public abstract class AbstractStorageImpl<O extends ArsRecord, R extends ArsReference<O>, A extends ArsArray<R>> implements
		ArsStorageImpl<O, R, A> {
	
	@SuppressWarnings("unchecked")
	@Override
	public final R createReferenceTemplate(
			final O key,
			final TreeLinkType mode,
			final R original) {
	
		return (R) new ReferenceFull<>( key, mode, original );
	}
}
