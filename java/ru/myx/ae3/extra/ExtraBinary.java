/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import ru.myx.ae3.binary.TransferCopier;

/**
 * @author myx
 * 
 * 
 */
public final class ExtraBinary extends BasicExtra {
	private final TransferCopier	copier;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * @param copier
	 */
	public ExtraBinary(final Object issuer, final String recId, final long recDate, final TransferCopier copier) {
		super( issuer, recId, recDate );
		this.copier = copier;
	}
	
	@Override
	public final Object baseValue() {
		return this.copier;
	}
	
	@Override
	public final Object toBinary() {
		return this.copier;
	}
	
	@Override
	public final String toString() {
		return this.copier.toStringUtf8();
	}
}
