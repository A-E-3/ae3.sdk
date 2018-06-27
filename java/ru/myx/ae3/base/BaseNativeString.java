package ru.myx.ae3.base;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;

/** new Object() or simply {} in javascript will create an instance of this class.
 *
 * Методы isPrimitiveXXX зафиксированы в значение false.
 *
 * @author myx */
@ReflectionDisable
public class BaseNativeString extends BaseNativeAbstract implements BaseStringObject<BaseNativeString> {

	/**
	 *
	 */
	protected final CharSequence stringValue;

	/** @param prototype
	 *            be sure to know what are you doing 8)
	 * @param stringValue */
	public BaseNativeString(final BaseObject prototype, final CharSequence stringValue) {

		super(prototype);
		this.stringValue = stringValue;
	}

	/** @param stringValue */
	public BaseNativeString(final CharSequence stringValue) {

		super(BaseString.PROTOTYPE);
		this.stringValue = stringValue;
	}

	/** overrides default iterator implementation. */
	@Override
	public void baseClear() {

		this.properties = null;
	}

	@Override
	public BaseProperty baseFindProperty(final BasePrimitiveString name) {

		if (this.properties != null) {
			for (final BaseProperty property = this.properties.find(name); property != null;) {
				return property;
			}
		}
		for (final BaseObject object = this.prototype; object != null;) {
			return object.baseFindProperty(name);
		}
		return null;
	}

	@Override
	public BaseProperty baseFindProperty(final BasePrimitiveString name, final BaseObject stop) {

		if (this.properties != null) {
			for (final BaseProperty property = this.properties.find(name); property != null;) {
				return property;
			}
		}
		for (final BaseObject object = this.prototype; object != null && object != stop;) {
			return object.baseFindProperty(name, stop);
		}
		return null;
	}

	@Override
	public BaseProperty baseFindProperty(final String name) {

		if (this.properties != null) {
			for (final BaseProperty property = this.properties.find(name); property != null;) {
				return property;
			}
		}
		for (final BaseObject object = this.prototype; object != null;) {
			return object.baseFindProperty(name);
		}
		return null;
	}

	@Override
	public BaseProperty baseFindProperty(final String name, final BaseObject stop) {

		if (this.properties != null) {
			for (final BaseProperty property = this.properties.find(name); property != null;) {
				return property;
			}
		}
		for (final BaseObject object = this.prototype; object != null && object != stop;) {
			return object.baseFindProperty(name, stop);
		}
		return null;
	}

	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {

		if (index < 0) {
			return defaultValue;
		}
		final CharSequence string = this.stringValue;
		if (index >= string.length()) {
			return defaultValue;
		}
		return Base.forChar(string.charAt(index));
	}

	@Override
	public boolean baseHasKeysOwn() {

		final BaseProperties<?> properties = this.properties;
		return properties != null && properties.hasEnumerableProperties();
	}

	@Override
	@ReflectionHidden
	public String baseToJavaString() {

		return this.stringValue.toString();
	}

	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {

		return Base.forString(this.stringValue);
	}

	@Override
	public BaseNativeString baseValue() {

		return this;
	}

	@Override
	public char charAt(final int index) {

		return this.stringValue.charAt(index);
	}

	@Override
	public void clear() {

		this.baseClear();
	}

	@Override
	public boolean containsKey(final Object key) {

		return Base.hasProperty(this, String.valueOf(key));
	}

	@Override
	public boolean containsValue(final Object value) {

		for (final Iterator<String> iterator = Base.keys(this); iterator.hasNext();) {
			final String key = iterator.next();
			final BaseObject x = this.baseGet(key, BaseObject.UNDEFINED);
			if (x == value || x.baseValue() == value || x.baseValue() == null && value == null || x.baseValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {

		return new UtilMapEntrySet(this);
	}

	/** <pre>
	 * 11.9.1 The Equals Operator ( == )
	 * The production EqualityExpression : EqualityExpression == RelationalExpression is evaluated as follows:
	 * 1. Evaluate EqualityExpression.
	 * 2. Call GetValue(Result(1)).
	 * 3. Evaluate RelationalExpression.
	 * 4. Call GetValue(Result(3)).
	 * 5. Perform the comparison Result(4) == Result(2). (Section 11.9.3.)
	 * 6. Return Result(5).
	 * </pre>
	 *
	 * <pre>
	 * 11.9.3 The Abstract Equality Comparison Algorithm
	 * The comparison x == y, where x and y are values, produces true or false. Such a comparison is performed as
	 * follows:
	 * 1. If Type(x) is different from Type(y), go to step 14.
	 * 2. If Type(x) is Undefined, return true.
	 * 3. If Type(x) is Null, return true.
	 * 4. If Type(x) is not Number, go to step 11.
	 * 5. If x is NaN, return false.
	 * 6. If y is NaN, return false.
	 * 7. If x is the same number value as y, return true.
	 * 8. If x is +0 and y is −0, return true.
	 * 9. If x is −0 and y is +0, return true.
	 * 10. Return false.
	 * 11. If Type(x) is String, then return true if x and y are exactly the same sequence of characters (same length and
	 * same characters in corresponding positions). Otherwise, return false.
	 * 12. If Type(x) is Boolean, return true if x and y are both true or both false. Otherwise, return false.
	 * 13. Return true if x and y refer to the same object or if they refer to objects joined to each other (section 13.1.2).
	 * Otherwise, return false.
	 * 14. If x is null and y is undefined, return true.
	 * 15. If x is undefined and y is null, return true.
	 * 16. If Type(x) is Number and Type(y) is String,
	 * return the result of the comparison x == ToNumber(y).
	 * 17. If Type(x) is String and Type(y) is Number,
	 * return the result of the comparison ToNumber(x) == y.
	 * 18. If Type(x) is Boolean, return the result of the comparison ToNumber(x) == y.
	 * 19. If Type(y) is Boolean, return the result of the comparison x == ToNumber(y).
	 * 20. If Type(x) is either String or Number and Type(y) is Object,
	 * return the result of the comparison x == ToPrimitive(y).
	 * 21. If Type(x) is Object and Type(y) is either String or Number,
	 * return the result of the comparison ToPrimitive(x) == y.
	 * 22. Return false.
	 * </pre>
	 *
	 * NOTE Given the above definition of equality:<br>
	 * String comparison can be forced by: &quot;&quot; + a ==&quot;&quot; +b. <br>
	 * Numeric comparison can be forced by: a - 0== b- 0. <br>
	 * Boolean comparison can be forced by: !a == !b.
	 * <p>
	 * The equality operators maintain the following invariants:<br>
	 * 1. A != B is equivalent to !(A == B). <br>
	 * 2. A == B is equivalent to B == A, except in the order of evaluation of A and B.<br>
	 * The equality operator is not always transitive. For example, there might be two distinct
	 * String objects, each representing the same string value; each String object would be
	 * considered equal to the string value by the == operator, but the two String objects would not
	 * be equal to each other.
	 * <p>
	 * Comparison of strings uses a simple equality test on sequences of code point value values.
	 * There is no attempt to use the more complex, semantically oriented definitions of character
	 * or string equality and collating order defined in the Unicode 2.0 specification. Therefore
	 * strings that are canonically equal according to the Unicode standard could test as unequal.
	 * In effect this algorithm assumes that both strings are already in normalised form.
	 * <p>
	 *
	 * Type x is this and is an object of any type. */
	@Override
	public boolean equals(final Object o) {

		/** Fits here, booleans, undefined and null are fixed:<br>
		 * 2. If Type(x) is Undefined, return true.<br>
		 * 3. If Type(x) is Null, return true. 12. If Type(x) is Boolean, return true if x and y are
		 * both true or both false. Otherwise, return false. */
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		/** instanceof is always false on NULL object */
		if (!(o instanceof BaseObject)) {
			return false;
		}
		/** 5. If x is NaN, return false. <br>
		 * 6. If y is NaN, return false. <br>
		*/
		if (o == BasePrimitiveNumber.NAN) {
			return false;
		}
		/** 14. If x is null and y is undefined, return true.<br>
		 * 15. If x is undefined and y is null, return true. <br>
		*/
		if (o == BaseObject.UNDEFINED || o == BaseObject.NULL) {
			return false;
		}
		final BaseObject object = (BaseObject) o;
		assert !this.baseIsPrimitive() : "BaseNativeObject is NOT primitive!";
		{
			if (object.baseIsPrimitive()) {
				/** 18. If Type(x) is Boolean, return the result of the comparison ToNumber(x) ==
				 * y.<br>
				 * 19. If Type(y) is Boolean, return the result of the comparison x == ToNumber(y).
				 * <br>
				*/
				if (object.baseIsPrimitiveBoolean()) {
					return this.baseToNumber().equals(object);
				}
				/** 20. If Type(x) is either String or Number and Type(y) is Object, return the
				 * result of the comparison x == ToPrimitive(y).<br>
				 * 21. If Type(x) is Object and Type(y) is either String or Number, return the
				 * result of the comparison ToPrimitive(x) == y.<br>
				*/
				if (object.baseIsPrimitiveString()) {
					return this.baseToPrimitive(ToPrimitiveHint.STRING).equals(object);
				}
				if (object.baseIsPrimitiveNumber()) {
					return this.baseToPrimitive(ToPrimitiveHint.NUMBER).equals(object);
				}
				/**
				 *
				 */
				return this.baseToPrimitive(null).equals(object);
			}
		}

		/** !!! NON-STANDARD: check properties recursively! */
		if (object instanceof BaseMap && this.prototype == object.basePrototype()) {
			for (final Iterator<? extends CharSequence> iterator = this.baseKeysOwnAll(); iterator.hasNext();) {
				final CharSequence key = iterator.next();
				final BaseObject original = this.baseGet(key, BaseObject.UNDEFINED);
				assert original != null : "NULL java value";
				final BaseObject effective = object.baseGet(key, BaseObject.UNDEFINED);
				assert effective != null : "NULL java value";
				if (original != effective && !original.equals(effective)) {
					return false;
				}
			}
			for (final Iterator<? extends CharSequence> iterator = object.baseKeysOwnAll(); iterator.hasNext();) {
				final CharSequence key = iterator.next();
				final BaseObject original = this.baseGet(key, BaseObject.UNDEFINED);
				assert original != null : "NULL java value";
				final BaseObject effective = object.baseGet(key, BaseObject.UNDEFINED);
				assert effective != null : "NULL java value";
				if (original != effective && !original.equals(effective)) {
					return false;
				}
			}
			return true;
		}
		/** 22. Return false. */
		return false;
	}

	@Override
	public String get(final int index) {

		if (index < 0) {
			return null;
		}
		final CharSequence string = this.stringValue;
		if (index >= string.length()) {
			return null;
		}
		return String.valueOf(string.charAt(index));
	}

	@Override
	public Object get(final Object key) {

		return this.baseGet(String.valueOf(key), BaseObject.UNDEFINED).baseValue();
	}

	@Override
	public int hashCode() {

		return System.identityHashCode(this);
	}

	/** calculated hashCode - same hash for same value but change in time (which is not compatible
	 * with HashMap's)
	 *
	 * @return */
	public int hashCodeForValue() {

		int hashCode = this.stringValue.hashCode();
		if (this.properties != null) {
			for (final Iterator<? extends CharSequence> iterator = this.properties.iteratorAll(); iterator != null && iterator.hasNext();) {
				final CharSequence key = iterator.next();
				{
					hashCode >>>= 11;
					hashCode ^= key.hashCode();
				}
				{
					hashCode >>>= 11;
					final BaseProperty property = this.properties.find(key);
					hashCode ^= (key instanceof BasePrimitiveString
						? property.propertyGet(this, (BasePrimitiveString) key)
						: property.propertyGet(this, key.toString())).hashCode();
				}
			}
		}
		if (this.prototype != null && this.prototype != BaseObject.PROTOTYPE) {
			hashCode >>>= 11;
			hashCode ^= this.prototype.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean isEmpty() {

		return !Base.hasKeys(this);
	}

	@Override
	public Set<String> keySet() {

		return new UtilMapKeySet(this);
	}

	@Override
	public int length() {

		return this.stringValue.length();
	}

	@Override
	public Object put(final String key, final Object value) {

		final String name = String.valueOf(key);
		try {
			return this.baseGet(name, BaseObject.UNDEFINED).baseValue();
		} finally {
			this.baseDefine(name, Base.forUnknown(value));
		}
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> t) {

		for (final Map.Entry<? extends String, ? extends Object> entry : t.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Object remove(final Object key) {

		final String name = String.valueOf(key);
		try {
			return this.baseGet(name, BaseObject.UNDEFINED).baseValue();
		} finally {
			this.baseDelete(name);
		}
	}

	@Override
	public int size() {

		int count = 0;
		for (final Iterator<String> keys = this.baseKeysOwn(); keys.hasNext(); keys.next()) {
			count++;
		}
		return count;
	}

	@Override
	public CharSequence subSequence(final int startIndex, final int endIndex) {

		return Base.forString(this.stringValue.subSequence(startIndex, endIndex));
	}

	@Override
	public String toString() {

		return this.stringValue.toString();
	}

	@Override
	public Collection<Object> values() {

		return new UtilMapValues(this);
	}

	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}

}
