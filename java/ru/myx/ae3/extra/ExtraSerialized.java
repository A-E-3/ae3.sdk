/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.SoftReference;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.transform.Transform;

/**
 * @author myx
 * 
 */
public final class ExtraSerialized extends BasicExtra {
	private final String			recType;
	
	private final TransferCopier	copier;
	
	private SoftReference<Object>	referenceObject	= null;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * @param recType
	 * @param copier
	 */
	public ExtraSerialized(final Object issuer,
			final String recId,
			final long recDate,
			final String recType,
			final TransferCopier copier) {
		super( issuer, recId, recDate );
		this.recType = recType;
		this.copier = copier;
	}
	
	@Override
	public final Object baseValue() {
		Object stored = this.referenceObject == null
				? null
				: this.referenceObject.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceObject == null
						? null
						: this.referenceObject.get();
				if (stored == null) {
					final Object object = Transform.materialize( Object.class,
							this.recType,
							new BaseNativeObject( "Content-Type", this.recType ),
							this.copier.nextCopy() );
					if (object == null || object == BaseObject.UNDEFINED) {
						return null;
					}
					if (object instanceof TransferBuffer) {
						return object;
					}
					if (object instanceof InputStream) {
						return object;
					}
					if (object instanceof Reader) {
						return object;
					}
					this.referenceObject = new SoftReference<>( object );
					return object;
				}
			}
		}
		return stored;
	}
	
	@Override
	public final Object toBinary() {
		return this.baseValue();
	}
	
	@Override
	public final String toString() {
		return String.valueOf( this.baseValue() );
	}
}
