/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import java.lang.ref.SoftReference;

import ru.myx.ae3.binary.TransferCopier;

/**
 * @author myx
 * 
 * 
 */
public final class ExtraBytes extends BasicExtra {
	private final TransferCopier	copier;
	
	private SoftReference<byte[]>	referenceBytes	= null;
	
	private SoftReference<String>	referenceString	= null;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * @param copier
	 */
	public ExtraBytes(final Object issuer, final String recId, final long recDate, final TransferCopier copier) {
		super( issuer, recId, recDate );
		this.copier = copier;
	}
	
	@Override
	public final Object baseValue() {
		byte[] stored = this.referenceBytes == null
				? null
				: this.referenceBytes.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceBytes == null
						? null
						: this.referenceBytes.get();
				if (stored == null) {
					final byte[] bytes = this.copier.nextDirectArray();
					this.referenceBytes = new SoftReference<>( bytes );
					return bytes;
				}
			}
		}
		return stored;
	}
	
	@Override
	public final Object toBinary() {
		return this.copier;
	}
	
	@Override
	public final String toString() {
		String stored = this.referenceString == null
				? null
				: this.referenceString.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceString == null
						? null
						: this.referenceString.get();
				if (stored == null) {
					final String string = this.copier.toStringUtf8();
					this.referenceString = new SoftReference<>( string );
					return string;
				}
			}
		}
		return stored;
	}
}
