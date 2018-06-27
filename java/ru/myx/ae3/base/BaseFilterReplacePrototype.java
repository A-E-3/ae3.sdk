package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * Merges object own properties and a given prototype
 * 
 * @author myx
 * 
 */
public class BaseFilterReplacePrototype implements BaseObject {
	
	
	private final BaseObject object;
	
	private final BaseObject prototype;
	
	/**
	 * 
	 * @param object
	 * @param prototype
	 */
	public BaseFilterReplacePrototype(final BaseObject object, final BaseObject prototype) {
		
		this.object = object;
		this.prototype = prototype;
	}
	
	@Override
	public String baseClass() {
		
		
		return this.object.baseClass();
	}
	
	@Override
	public void baseClear() {
		
		
		this.object.baseClear();
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		return this.object.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		return this.object.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final double value, final short attributes) {
		
		
		return this.object.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final long value, final short attributes) {
		
		
		return this.object.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final String value, final short attributes) {
		
		
		return this.object.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		return this.object.baseDelete(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		return this.object.baseGetOwnProperty(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		return this.object.baseGetOwnProperty(name);
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return this.object.baseHasKeysOwn();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		return this.object.baseIsExtensible();
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return this.object.baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
		return this.object.baseKeysOwnAll();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return this.object.baseKeysOwnPrimitive();
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return this.prototype;
	}
	
	@Override
	public BasePrimitiveBoolean baseToBoolean() {
		
		
		return this.object.baseToBoolean();
	}
	
	@Override
	public BasePrimitiveNumber baseToInt32() {
		
		
		return this.object.baseToInt32();
	}
	
	@Override
	public BasePrimitiveNumber baseToInteger() {
		
		
		return this.object.baseToInt32();
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return this.object.baseToNumber();
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		final BaseObject primitive = this.object.baseToPrimitive(ToPrimitiveHint.STRING);
		assert primitive != null : "baseToPrimitive shouldn't result java NULL, class=" + this.getClass().getName();
		return primitive.baseToString();
	}
	
	@Override
	public BaseFilterReplacePrototype baseValue() {
		
		
		return this;
	}
	
}
