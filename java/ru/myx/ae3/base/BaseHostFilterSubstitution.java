package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * @author myx
 *
 * @param <P>
 */
public abstract class BaseHostFilterSubstitution<P extends BaseObject> implements BaseHostDataSubstitution<P> {
	
	
	@Override
	public BaseArray baseArray() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseArray();
	}
	
	@Override
	public BaseFunction baseCall() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseCall();
	}
	
	@Override
	public String baseClass() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseClass();
	}
	
	@Override
	public void baseClear() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		substitution.baseClear();
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseConstruct();
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDelete(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return substitution.baseFindProperty(name, BaseObject.PROTOTYPE);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return substitution.baseFindProperty(name, BaseObject.PROTOTYPE);
	}
	
	/**
	 * parental instance
	 *
	 * @return
	 */
	@Override
	public abstract P baseGetSubstitution();
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return Base.hasKeys(substitution);
		// return substitution.baseHasKeysOwn();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseIsExtensible();
	}
	
	@Override
	public final Iterator<String> baseKeysOwn() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return Base.keys(substitution);
		// return substitution.baseKeysOwn();
	}
	
	@Override
	public final Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return Base.keysAll(substitution);
		// return substitution.baseKeysOwnAll();
	}
	
	@Override
	public final Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return Base.keysPrimitive(substitution);
		// return substitution.baseKeysOwnPrimitive();
	}
	
	@Override
	public BasePrimitiveBoolean baseToBoolean() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToBoolean();
	}
	
	@Override
	public BasePrimitiveNumber baseToInt32() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToInt32();
	}
	
	@Override
	public BasePrimitiveNumber baseToInteger() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToInteger();
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToNumber();
	}
	
	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToPrimitive(hint);
	}
	
	@Override
	public boolean equals(final Object o) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.equals(o);
	}
	
	@Override
	public Object baseValue() {
		
		
		return this.baseGetSubstitution();
	}
	
	@Override
	public int hashCode() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.hashCode();
	}
	
	@Override
	public String toString() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return "[object " + this.getClass().getName() + ", wrap=" + substitution + "]";
	}
	
}
