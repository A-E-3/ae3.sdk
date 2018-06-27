/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.produce;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveBoolean;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/**
 * @author myx
 * 
 */
public final class BaseProduceReferenceCached implements BaseObject, Reproducible, Describable, Value<Object> {
	
	
	private static final Object NULL_OBJECT = new Object();
	
	private final String factoryIdentity;
	
	private final String restoreParameter;
	
	private Reference<Object> reference = null;
	
	private long hashCode = 0xF000000000000000L;
	
	/**
	 * @param factoryIdentity
	 * @param restoreParameter
	 */
	public BaseProduceReferenceCached(final String factoryIdentity, final String restoreParameter) {
		
		this.factoryIdentity = factoryIdentity;
		this.restoreParameter = restoreParameter;
	}
	
	@Override
	public BaseArray baseArray() {
		
		
		return Base.forUnknown(this.baseValue()).baseArray();
	}
	
	@Override
	public BaseFunction baseCall() {
		
		
		return Base.forUnknown(this.baseValue()).baseCall();
	}
	
	@Override
	public String baseClass() {
		
		
		return "Reference";
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		
		return Base.forUnknown(this.baseValue()).baseConstruct();
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		return Base.forUnknown(this.baseValue()).baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		return Base.forUnknown(this.baseValue()).baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final double value, final short attributes) {
		
		
		return Base.forUnknown(this.baseValue()).baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final long value, final short attributes) {
		
		
		return Base.forUnknown(this.baseValue()).baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final String value, final short attributes) {
		
		
		return Base.forUnknown(this.baseValue()).baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		return Base.forUnknown(this.baseValue()).baseDelete(name);
	}
	
	@Override
	public Object baseDescribe() {
		
		
		return "[object ProduceReference(" + this.factoryIdentity + "/" + this.restoreParameter + ", refValid=" + (this.reference != null && this.reference.get() != null) + ")]";
	}
	
	@Override
	public BaseObject baseGet(final BasePrimitiveString name, final BaseObject defaultValue) {
		
		
		return Base.forUnknown(this.baseValue()).baseGet(name, defaultValue);
	}
	
	@Override
	public BaseObject baseGet(final String name, final BaseObject defaultValue) {
		
		
		return Base.forUnknown(this.baseValue()).baseGet(name, defaultValue);
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final BasePrimitiveString name, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return Base.forUnknown(this.baseValue()).vmPropertyRead(ctx, name, defaultValue, store);
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final String name, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return Base.forUnknown(this.baseValue()).vmPropertyRead(ctx, name, defaultValue, store);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		return Base.forUnknown(this.baseValue()).baseGetOwnProperty(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		return Base.forUnknown(this.baseValue()).baseGetOwnProperty(name);
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return Base.forUnknown(this.baseValue()).baseHasKeysOwn();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		return Base.forUnknown(this.baseValue()).baseIsExtensible();
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return Base.forUnknown(this.baseValue()).baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
		return Base.forUnknown(this.baseValue()).baseKeysOwnAll();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return Base.forUnknown(this.baseValue()).baseKeysOwnPrimitive();
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return Base.forUnknown(this.baseValue()).basePrototype();
	}
	
	@Override
	public BasePrimitiveBoolean baseToBoolean() {
		
		
		return Base.forUnknown(this.baseValue()).baseToBoolean();
	}
	
	@Override
	public BasePrimitiveNumber baseToInt32() {
		
		
		return Base.forUnknown(this.baseValue()).baseToInt32();
	}
	
	@Override
	public BasePrimitiveNumber baseToInteger() {
		
		
		return Base.forUnknown(this.baseValue()).baseToInteger();
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return Base.forUnknown(this.baseValue()).baseToNumber();
	}
	
	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		
		return Base.forUnknown(this.baseValue()).baseToPrimitive(hint);
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return Base.forUnknown(this.baseValue()).baseToString();
	}
	
	@Override
	public final Object baseValue() {
		
		
		Object object = this.reference == null
			? null
			: this.reference.get();
		if (object == null) {
			synchronized (this) {
				object = this.reference == null
					? null
					: this.reference.get();
				if (object == null) {
					object = Produce.object(Object.class, this.factoryIdentity, null, this.restoreParameter);
					this.reference = object == null
						? new WeakReference<>(object = BaseProduceReferenceCached.NULL_OBJECT)
						: new SoftReference<>(object);
				}
			}
		}
		return object != BaseProduceReferenceCached.NULL_OBJECT
			? object
			: null;
	}
	
	@Override
	protected BaseProduceReferenceCached clone() throws CloneNotSupportedException {
		
		
		return this;
	}
	
	private final int createHashCode() {
		
		
		final Object o = this.baseValue();
		return o == null
			? 0
			: (int) (this.hashCode = o.hashCode());
	}
	
	@Override
	public final boolean equals(final Object anotherObject) {
		
		
		if (this == anotherObject) {
			return true;
		}
		if (anotherObject instanceof BaseProduceReferenceCached) {
			final BaseProduceReferenceCached holder = (BaseProduceReferenceCached) anotherObject;
			{
				final String i1 = this.factoryIdentity, i2 = holder.factoryIdentity;
				if (i1 != i2 && !i1.equals(i2)) {
					return false;
				}
			}
			{
				final String p1 = this.restoreParameter, p2 = holder.restoreParameter;
				return p1 == null
					? p2 == null
					: p1 == p2 || p1.equals(p2);
			}
		}
		if (anotherObject instanceof Reproducible) {
			final Reproducible holder = (Reproducible) anotherObject;
			{
				final String i1 = this.factoryIdentity, i2 = holder.restoreFactoryIdentity();
				if (i1 != i2 && !i1.equals(i2)) {
					return false;
				}
			}
			{
				final String p1 = this.restoreParameter, p2 = holder.restoreFactoryParameter();
				return p1 == null
					? p2 == null
					: p1 == p2 || p1.equals(p2);
			}
		}
		return anotherObject != null && anotherObject.equals(this.baseValue());
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.hashCode == 0xF000000000000000L
			? this.createHashCode()
			: (int) this.hashCode;
	}
	
	@Override
	public final String restoreFactoryIdentity() {
		
		
		return this.factoryIdentity;
	}
	
	@Override
	public final String restoreFactoryParameter() {
		
		
		return this.restoreParameter;
	}
	
	@Override
	public final String toString() {
		
		
		return String.valueOf(this.baseValue());
	}
}
