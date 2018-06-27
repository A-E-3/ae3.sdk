package ru.myx.ae3.common;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseAbstract;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;

/**
 * @author myx
 * 
 * @param <T>
 */
public final class HolderSimple<T> extends BaseAbstract implements Holder<T>, BaseObjectNoOwnProperties {
	
	
	private static final long UNKNOWN_HASH = 0xF000000000000000L;
	
	private T obj;
	
	private long hashCode = HolderSimple.UNKNOWN_HASH;
	
	/**
	 * @param def
	 */
	public HolderSimple(final T def) {
		this.obj = def;
	}
	
	/**
	 * Base.forUnknown( this.baseValue() )
	 */
	@Override
	public BaseObject basePrototype() {
		
		
		return Base.forUnknown(this.obj);
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return Base.forUnknown(this.obj).baseToString();
	}
	
	@Override
	public T baseValue() {
		
		
		return this.obj;
	}
	
	private final int createHashCode() {
		
		
		final Object o = this.baseValue();
		if (o == null) {
			return 0;
		}
		return (int) (this.hashCode = o.hashCode());
	}
	
	@Override
	public final boolean equals(final Object anotherObject) {
		
		
		return anotherObject == this || anotherObject != null && anotherObject.equals(this.obj);
	}
	
	@Override
	public boolean execCanSet() {
		
		
		return true;
	}
	
	@Override
	public boolean execCompareAndSet(final T compare, final T value) {
		
		
		assert value != this : "Resursive value!";
		synchronized (this) {
			if (this.obj == compare) {
				this.obj = value;
				this.hashCode = HolderSimple.UNKNOWN_HASH;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public T execGetAndSet(final T value) {
		
		
		assert value != this : "Resursive value!";
		synchronized (this) {
			try {
				return this.obj;
			} finally {
				this.obj = value;
				this.hashCode = HolderSimple.UNKNOWN_HASH;
			}
		}
	}
	
	@Override
	public void execSet(final T value) {
		
		
		assert value != this : "Resursive value!";
		this.obj = value;
		this.hashCode = HolderSimple.UNKNOWN_HASH;
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.hashCode == HolderSimple.UNKNOWN_HASH
			? this.createHashCode()
			: (int) this.hashCode;
	}
	
	@Override
	public final String toString() {
		
		
		return String.valueOf(this.obj);
	}
}
