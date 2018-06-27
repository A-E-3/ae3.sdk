/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseHostFilterSubstitution;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveBoolean;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;

/**
 * @author myx
 *
 */
public abstract class AbstractExtra extends BaseHostFilterSubstitution<BaseObject> implements External, Describable {
	
	
	/**
	 *
	 */
	protected final String recId;

	/**
	 * @param recId
	 *
	 */
	protected AbstractExtra(final String recId) {
		this.recId = recId;
	}

	@Override
	public final BaseArray baseArray() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseArray();
	}

	@Override
	public final BaseFunction baseCall() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseCall();
	}

	@Override
	public final String baseClass() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseClass();
	}

	@Override
	public final void baseClear() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		substitution.baseClear();
	}

	@Override
	public final BaseFunction baseConstruct() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseConstruct();
	}

	@Override
	public final boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}

	@Override
	public final boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDefine(name, value, attributes);
	}

	@Override
	public final boolean baseDelete(final String name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseDelete(name);
	}

	@Override
	public final BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		/**
		 * not OWN!
		 */
		return substitution.baseFindProperty(name, BaseObject.PROTOTYPE);
	}

	@Override
	public final BaseProperty baseGetOwnProperty(final String name) {
		
		
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
	public final BaseObject baseGetSubstitution() {
		
		
		return Base.forUnknown(this.baseValue());
	}

	@Override
	public BaseObject basePrototype() {
		
		
		return External.PROTOTYPE;
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
		
		
		return this.baseToNumber().baseToInt32();
	}

	@Override
	public BasePrimitiveNumber baseToInteger() {
		
		
		return this.baseToNumber().baseToInteger();
	}

	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToNumber();
	}

	@Override
	public final BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		
		final BaseObject substitution = this.baseGetSubstitution();
		assert substitution != null : "Should not be NULL, class=" + this.getClass().getName();
		assert substitution != this : "Should not be THIS, class=" + this.getClass().getName();
		return substitution.baseToPrimitive(hint);
	}

	/**
	 * not final - overridden in Text extras
	 */
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return Base.forString(this.toString());
	}

	@Override
	public boolean equals(final Object obj) {
		
		
		if (obj == this) {
			return true;
		}
		if (obj != null && obj instanceof External) {
			final External extra = (External) obj;
			return extra.getRecordIssuer() == this.getRecordIssuer() && (extra.getIdentity() == this.recId || extra.getIdentity().equals(this.recId));
		}
		return false;
	}

	@Override
	public final String getIdentity() {
		
		
		return this.recId;
	}

	@Override
	public int hashCode() {
		
		
		return this.recId.hashCode();
	}

	@Override
	public String toString() {
		
		
		return String.valueOf(this.baseValue());
	}
	
	@Override
	public Object baseDescribe() {
		
		
		final Object base = this.baseValue();
		if (this == base) {
			return "[" + this.getClass().getSimpleName() + " id:" + this.recId + ", circular]";
		}
		return "[" + this.getClass().getSimpleName() + " id:" + this.recId + ", base:" + Format.Describe.toEcmaSource(base, "") + "]";
	}
	
}
