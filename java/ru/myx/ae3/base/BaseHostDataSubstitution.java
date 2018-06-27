package ru.myx.ae3.base;

import java.util.Iterator;

/**
 * @author myx
 *
 * @param <P>
 */
public interface BaseHostDataSubstitution<P extends BaseObject> extends BaseHost {
	
	
	/**
	 * same as of parental class. <code>
	 *
	 * @Override default String baseClass(){ return "Object"; } </code>
	 */
	// default String baseClass(){
	
	@Override
	default void baseClear() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		substitution.baseClear();
	}
	
	@Override
	default boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}
	
	@Override
	default boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}
	
	@Override
	default boolean baseDelete(final String name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * OWN 8-)
		 */
		return substitution.baseDelete(name);
	}
	
	@Override
	default BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return substitution.baseFindProperty(name, BaseObject.PROTOTYPE);
	}
	
	@Override
	default BaseProperty baseGetOwnProperty(final String name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return substitution.baseFindProperty(name, BaseObject.PROTOTYPE);
	}

	/**
	 *
	 * @return
	 */
	abstract P baseGetSubstitution();
	
	@Override
	default boolean baseHasKeysOwn() {
		
		
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
	default boolean baseIsExtensible() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseIsExtensible();
	}
	
	@Override
	default Iterator<String> baseKeysOwn() {
		
		
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
	default Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
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
	default Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return Base.keysPrimitive(substitution);
		// return substitution.baseKeysOwnPrimitive();
	}
}
