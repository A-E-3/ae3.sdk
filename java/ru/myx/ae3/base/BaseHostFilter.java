package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * @author myx
 * 
 * @param <P>
 */
public class BaseHostFilter<P extends BaseObject> implements BaseHost {
	
	
	/**
	 * parental instance
	 */
	protected P parent;
	
	/**
	 * Prototype will be set to parent's prototype.
	 * 
	 * @param parent
	 */
	protected BaseHostFilter(final P parent) {
		this.parent = parent;
	}
	
	@Override
	public BaseArray baseArray() {
		
		
		return this.parent.baseArray();
	}
	
	@Override
	public BaseFunction baseCall() {
		
		
		return this.parent.baseCall();
	}
	
	@Override
	public String baseClass() {
		
		
		return this.parent.baseClass();
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		
		return this.parent.baseConstruct();
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		return this.parent.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		return this.parent.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		return this.parent.baseDelete(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		return this.parent.baseGetOwnProperty(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		return this.parent.baseGetOwnProperty(name);
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return this.parent.baseHasKeysOwn();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		return this.parent.baseIsExtensible();
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return this.parent.baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
		return this.parent.baseKeysOwnAll();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return this.parent.baseKeysOwnPrimitive();
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return this.parent.basePrototype();
	}
	
	@Override
	public BasePrimitiveBoolean baseToBoolean() {
		
		
		return this.parent.baseToBoolean();
	}
	
	@Override
	public BasePrimitiveNumber baseToInt32() {
		
		
		return this.parent.baseToInt32();
	}
	
	@Override
	public BasePrimitiveNumber baseToInteger() {
		
		
		return this.parent.baseToInteger();
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return this.parent.baseToNumber();
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return this.parent.baseToString();
	}
	
	@Override
	public boolean equals(final Object o) {
		
		
		return this.parent.equals(o);
	}
	
	@Override
	public int hashCode() {
		
		
		return this.parent.hashCode();
	}
	
	@Override
	public String toString() {
		
		
		return this.parent.toString();
	}
	
}
