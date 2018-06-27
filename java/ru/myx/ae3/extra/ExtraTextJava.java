/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import java.lang.ref.SoftReference;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * 
 * 
 */
public final class ExtraTextJava extends BasicExtra implements CharSequence {
	
	
	/**
	 * 
	 */
	private static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ExtraTextJava.class));
	
	private final TransferCopier copier;
	
	private SoftReference<String> referenceString = null;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * @param copier
	 */
	public ExtraTextJava(final Object issuer, final String recId, final long recDate, final TransferCopier copier) {
		super(issuer, recId, recDate);
		this.copier = copier;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		return ExtraTextJava.PROTOTYPE;
	}
	
	@Override
	public final BasePrimitiveString baseToString() {
		
		return Base.forString(this.toString());
	}
	
	@Override
	public final Object baseValue() {
		
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
					assert string != null : "NULL string";
					this.referenceString = new SoftReference<>(string);
					return string;
				}
			}
		}
		return stored;
	}
	
	@Override
	public char charAt(final int arg0) {
		
		return this.toString().charAt(arg0);
	}
	
	@Override
	public int length() {
		
		return this.toString().length();
	}
	
	@Override
	public CharSequence subSequence(final int arg0, final int arg1) {
		
		return this.toString().subSequence(arg0, arg1);
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
					assert string != null : "NULL string";
					this.referenceString = new SoftReference<>(string);
					return string;
				}
			}
		}
		return stored;
	}
}
