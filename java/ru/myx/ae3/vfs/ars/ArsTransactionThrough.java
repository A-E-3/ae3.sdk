package ru.myx.ae3.vfs.ars;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;

/**
 * 
 * @author myx
 * @param <O>
 * @param <R>
 * @param <A>
 * 
 */
public class ArsTransactionThrough<O extends ArsRecord, R extends ArsReference<O>, A extends ArsArray<R>> implements
		ArsTransaction<O, R, A> {
	
	ArsInterface<O, R, A>	iface;
	
	
	/**
	 * 
	 * @param iface
	 */
	public ArsTransactionThrough(final ArsInterface<O, R, A> iface) {
	
		this.iface = iface;
	}
	
	
	@Override
	public void cancel() throws Exception {
	
		// ignore
	}
	
	
	@Override
	public void commit() throws Exception {
	
		// ignore
	}
	
	
	@Override
	public O createBinaryTemplate(
			final TransferCopier copier) {
	
		return this.iface.createBinaryTemplate( copier );
	}
	
	
	@Override
	public O createContainerTemplate() {
	
		return this.iface.createContainerTemplate();
	}
	
	
	@Override
	public O createKeyForString(
			final String key) {
	
		return this.iface.createKeyForString( key );
	}
	
	
	@Override
	public O createPrimitiveTemplate(
			final Guid guid) {
	
		return this.iface.createPrimitiveTemplate( guid );
	}
	
	
	@Override
	public R createReferenceTemplate(
			final O key,
			final TreeLinkType mode,
			final R original) {
	
		return this.iface.createReferenceTemplate( key, mode, original );
	}
	
	
	@Override
	public O createTextTemplate(
			final CharSequence text) {
	
		return this.iface.createTextTemplate( text );
	}
	
	
	@Override
	public ArsTransaction<O, R, A> createTransaction() throws Exception {
	
		return this.iface.createTransaction();
	}
	
	
	@Override
	public Value<R> doLinkDelete(
			final R template,
			final O object,
			final O key,
			final TreeLinkType mode) {
	
		return this.iface.doLinkDelete( template, object, key, mode );
	}
	
	
	@Override
	public Value<R> doLinkMoveRename(
			final R template,
			final O object,
			final O key,
			final O newObject,
			final O newKey,
			final TreeLinkType mode,
			final long modified,
			final O target) {
	
		return this.iface.doLinkMoveRename( template, object, key, newObject, newKey, mode, modified, target );
	}
	
	
	@Override
	public Value<R> doLinkRename(
			final R template,
			final O object,
			final O key,
			final O newKey,
			final TreeLinkType mode,
			final long modified,
			final O target) {
	
		return this.iface.doLinkRename( template, object, key, newKey, mode, modified, target );
	}
	
	
	@Override
	public Value<R> doLinkSet(
			final R template,
			final O object,
			final O key,
			final TreeLinkType mode,
			final long modified,
			final O target) {
	
		return this.iface.doLinkSet( template, object, key, mode, modified, target );
	}
	
	
	@Override
	public Value<? extends TransferCopier> getBinary(
			final O object) {
	
		return this.iface.getBinary( object );
	}
	
	
	@Override
	public Value<R> getLink(
			final O object,
			final O key,
			final TreeLinkType mode) {
	
		return this.iface.getLink( object, key, mode );
	}
	
	
	@Override
	public Value<A> getLinks(
			final O object,
			final TreeReadType mode) {
	
		return this.iface.getLinks( object, mode );
	}
	
	
	@Override
	public Value<A> getLinksRange(
			final O object,
			final O keyStart,
			final O keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		return this.iface.getLinksRange( object, keyStart, keyStop, limit, backwards, mode );
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final O object) {
	
		return this.iface.getText( object );
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		return this.iface.isHistorySupported();
	}
	
	
	@Override
	public boolean isReadOnly() {
	
		return this.iface.isReadOnly();
	}
	
}
