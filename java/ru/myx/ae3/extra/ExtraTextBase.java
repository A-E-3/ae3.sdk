/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import java.lang.ref.SoftReference;

import ru.myx.ae3.Engine;
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
public final class ExtraTextBase extends BasicExtra
		implements
			CharSequence /* , BaseString<Object> */ {
	
	
	/**
	 * 
	 */
	private static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ExtraTextBase.class));
	
	private final TransferCopier copier;
	
	private SoftReference<BasePrimitiveString> referenceString = null;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * @param copier
	 */
	public ExtraTextBase(final Object issuer, final String recId, final long recDate, final TransferCopier copier) {
		super(issuer, recId, recDate);
		this.copier = copier;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return ExtraTextBase.PROTOTYPE;
	}
	
	@Override
	public final BasePrimitiveString baseToString() {
		
		
		BasePrimitiveString stored = this.referenceString == null
			? null
			: this.referenceString.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceString == null
					? null
					: this.referenceString.get();
				if (stored == null) {
					final BasePrimitiveString string = Base.forString(this.copier.nextCopy().toString(Engine.CHARSET_UTF8));
					this.referenceString = new SoftReference<>(string);
					return string;
				}
			}
		}
		return stored;
	}
	
	@Override
	public final Object baseValue() {
		
		
		BasePrimitiveString stored = this.referenceString == null
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
					this.referenceString = new SoftReference<>(Base.forString(string));
					return string;
				}
			}
		}
		return stored;
	}
	
	@Override
	public char charAt(final int arg0) {
		
		
		return this.baseToString().charAt(arg0);
	}
	
	@Override
	public int length() {
		
		
		return this.baseToString().length();
	}
	
	@Override
	public CharSequence subSequence(final int arg0, final int arg1) {
		
		
		return this.baseToString().subSequence(arg0, arg1);
	}
	
	@Override
	public final Object toBinary() {
		
		
		return this.copier;
	}
	
	@Override
	public final String baseToJavaString() {
		
		
		return this.baseToString().baseToJavaString();
	}
	
	@Override
	public final String toString() {
		
		
		return this.baseToString().toString();
	}
}
