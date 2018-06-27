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
public class ArsTransactionBuffered<O extends ArsRecord, R extends ArsReference<O>, A extends ArsArray<R>> implements
		ArsTransaction<O, R, A> {
	
	private ArsInterface<O, R, A>	iface;
	
	
	/**
	 * 
	 * @param iface
	 */
	public ArsTransactionBuffered(final ArsInterface<O, R, A> iface) {
	
		this.iface = iface;
	}
	
	
	@Override
	public void cancel() throws Exception {
	
		this.iface = null;
	}
	
	
	@Override
	public void commit() throws Exception {
	
		this.iface = null;
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
	public ArsTransactionNested<O, R, A> createTransaction() throws Exception {
	
		return new ArsTransactionNested<>( this );
	}
	
	
	@Override
	public Value<R> doLinkDelete(
			final R template,
			final O object,
			final O key,
			final TreeLinkType mode) {
	
		// TODO Auto-generated method stub
		return null;
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
	
		// TODO Auto-generated method stub
		return null;
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
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<R> doLinkSet(
			final R template,
			final O object,
			final O key,
			final TreeLinkType mode,
			final long modified,
			final O target) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final O object) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<R> getLink(
			final O object,
			final O key,
			final TreeLinkType mode) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<A> getLinks(
			final O object,
			final TreeReadType mode) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<A> getLinksRange(
			final ArsRecord object,
			final ArsRecord keyStart,
			final ArsRecord keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final ArsRecord object) {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean isReadOnly() {
	
		// TODO Auto-generated method stub
		return false;
	}
	
}
