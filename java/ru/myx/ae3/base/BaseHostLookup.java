package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.control.ControlLookupDynamic;
import ru.myx.ae3.control.ControlLookupStatic;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.produce.Produce;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 *
 * Only 2 abstract methods: baseValue & toString.
 *
 * @author myx
 */
@ReflectionManual
public abstract class BaseHostLookup implements BaseObjectNotWritable, BaseProperty {
	
	/**
	 *
	 */
	public static final BaseObject COMPACT_RELATIVE_DATE_FORMATTER = new BaseHostLookup() {
		
		@Override
		public BaseObject baseGetLookupValue(final BaseObject key) {
			
			final long date = Convert.Any.toLong(key, -1L);
			return Base.forString(Format.Compact.dateRelative(date));
		}

		@Override
		public boolean baseHasKeysOwn() {
			
			return false;
		}

		@Override
		public Iterator<String> baseKeysOwn() {
			
			return BaseObject.ITERATOR_EMPTY;
		}

		@Override
		public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
			
			return BaseObject.ITERATOR_EMPTY_PRIMITIVE;
		}

		@Override
		public String toString() {
			
			return "[Lookup: Relative date formatter]";
		}
	};

	/**
	 *
	 */
	@ReflectionHidden
	public static final BaseObject PROTOTYPE;

	static {
		PROTOTYPE = BaseObject.createObject(BaseObject.PROTOTYPE);
		assert BaseHostLookup.PROTOTYPE.basePrototype() != null : "Need Object.prototype to be already established.";
		/**
		 * Old syntax used in CTRL interfaces is still using it.
		 */
		final BaseObject reflection = Reflect.classToBasePrototype(BaseHostLookup.class);
		final BaseObject lookupGet = reflection.baseGet("baseGetLookupValue", null);
		assert lookupGet != null : "Check baseGetLookupValue method!";
		BaseHostLookup.PROTOTYPE.baseDefine("get", lookupGet, BaseProperty.ATTRS_MASK_NNN);
		BaseHostLookup.PROTOTYPE.baseDefine("containsKey", lookupGet, BaseProperty.ATTRS_MASK_NNN);
	}

	/**
	 * Prototype object
	 */
	protected BaseObject prototype;

	/**
	 *
	 */
	public BaseHostLookup() {

		this.prototype = BaseHostLookup.PROTOTYPE;
	}

	/**
	 * @param prototype
	 */
	public BaseHostLookup(final BaseObject prototype) {

		assert prototype != null : "Prototype is java null";
		this.prototype = prototype == BaseObject.UNDEFINED
			? BaseHostLookup.PROTOTYPE
			: prototype;
	}

	/**
	 * The value of the [[Class]] property is defined by this specification for
	 * every kind of built-in object. The value of the [[Class]] property of a
	 * host object may be any value, even a value used by a built-in object for
	 * its [[Class]] property. The value of a [[Class]] property is used
	 * internally to distinguish different kinds of built-in objects. Note that
	 * this specification does not provide any means for a program to access
	 * that value except through Object.prototype.toString (see 15.2.4.2).
	 *
	 * @return class
	 */
	@Override
	public final String baseClass() {
		
		final String cls = this.getClass().getSimpleName();
		return cls.contains("Lookup")
			? cls
			: "Lookup-" + cls;
	}

	@Override
	public final BaseObject baseGet(final BasePrimitiveString name, final BaseObject defaultValue) {
		
		/**
		 * property goes first - there are some lookups returning values for any
		 * input
		 */
		if (this.prototype != null) {
			for (final BaseProperty property = this.prototype.baseFindProperty(name); property != null;) {
				return property.propertyGet(this, name);
			}
		}
		/**
		 * then lookup
		 */
		{
			final BaseObject value = this.baseGetLookupValue(name);
			if (value != null && value != BaseObject.UNDEFINED) {
				return value;
			}
		}
		return defaultValue;
	}

	@Override
	public final BaseObject baseGet(final String name, final BaseObject defaultValue) {
		
		/**
		 * property goes first - there are some lookups returning values for any
		 * input
		 */
		if (this.prototype != null) {
			for (final BaseProperty property = this.prototype.baseFindProperty(name); property != null;) {
				return property.propertyGet(this, name);
			}
		}
		/**
		 * then lookup
		 */
		{
			final BaseObject value = this.baseGetLookupValue(Base.forString(name));
			if (value != null && value != BaseObject.UNDEFINED) {
				return value;
			}
		}
		return defaultValue;
	}

	/**
	 * Lookup value
	 *
	 * Should return lookup own value for this lookup.
	 *
	 * @param name
	 * @return
	 */
	@ReflectionExplicit
	public abstract BaseObject baseGetLookupValue(final BaseObject name);

	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		/**
		 * check prototype first - cause lookup contains all values
		 */
		if (this.prototype != null) {
			if (Base.hasProperty(this.prototype, name)) {
				/**
				 * NULL - not own
				 */
				return null;
			}
		}
		return this;
	}
	@Override
	public final BaseProperty baseGetOwnProperty(final String name) {
		
		/**
		 * check prototype first - cause lookup contains all values
		 */
		if (this.prototype != null) {
			if (Base.hasProperty(this.prototype, name)) {
				/**
				 * NULL - not own
				 */
				return null;
			}
		}
		return this;
	}

	@Override
	public boolean baseHasKeysOwn() {
		
		return this.baseKeysOwn().hasNext();
	}

	/**
	 *
	 */
	@Override
	public abstract Iterator<String> baseKeysOwn();

	/**
	 * Should return enumerable lookup keys.
	 */
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		return this.baseKeysOwn();
	}

	@Override
	public abstract Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive();

	/**
	 * The value of the [[Prototype]] property must be either an object or null,
	 * and every [[Prototype]] chain must have finite length (that is, starting
	 * from any object, recursively accessing the [[Prototype]] property must
	 * eventually lead to a null value). Whether or not a native object can have
	 * a host object as its [[Prototype]] depends on the implementation.
	 *
	 * @return prototype object or null
	 */
	@Override
	public final BaseObject basePrototype() {
		
		return this.prototype;
	}

	@Override
	public final Object baseValue() {
		
		return this;
	}

	/**
	 * <pre>
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
	 * ECMAScript Language Specification   Edition 3   24-Mar-00
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
	 * String comparison can be forced by: &quot;&quot; + a ==&quot;&quot; +b.
	 * <br>
	 * Numeric comparison can be forced by: a - 0== b- 0. <br>
	 * Boolean comparison can be forced by: !a == !b.
	 * <p>
	 * The equality operators maintain the following invariants:<br>
	 * 1. A != B is equivalent to !(A == B). <br>
	 * 2. A == B is equivalent to B == A, except in the order of evaluation of A
	 * and B.<br>
	 * The equality operator is not always transitive. For example, there might
	 * be two distinct String objects, each representing the same string value;
	 * each String object would be considered equal to the string value by the
	 * == operator, but the two String objects would not be equal to each other.
	 * <p>
	 * Comparison of strings uses a simple equality test on sequences of code
	 * point value values. There is no attempt to use the more complex,
	 * semantically oriented definitions of character or string equality and
	 * collating order defined in the Unicode 2.0 specification. Therefore
	 * strings that are canonically equal according to the Unicode standard
	 * could test as unequal. In effect this algorithm assumes that both strings
	 * are already in normalised form.
	 * <p>
	 *
	 * Type x is this and is an object of any type.
	 */
	@Override
	public boolean equals(final Object o) {
		
		/**
		 * Fits here, booleans, undefined and null are fixed:<br>
		 * 2. If Type(x) is Undefined, return true.<br>
		 * 3. If Type(x) is Null, return true. 12. If Type(x) is Boolean, return
		 * true if x and y are both true or both false. Otherwise, return false.
		 */
		if (o == this) {
			return true;
		}
		/**
		 * instanceof is always false on NULL object
		 */
		if (!(o instanceof BaseObject)) {
			return false;
		}
		if (o == BasePrimitiveNumber.NAN) {
			return false;
		}
		final BaseObject object = (BaseObject) o;
		if (this.baseIsPrimitiveNumber()) {
			assert false : "PrimitiveNumber object should have it's own equals method implementation!";
			/**
			 * 7. If x is the same number value as y, return true.<br>
			 * 8. If x is +0 and y is −0, return true.<br>
			 * 9. If x is −0 and y is +0, return true.<br>
			 * 10. Return false. <br>
			 */
			if (object.baseIsPrimitiveNumber()) {
				return this.baseValue().equals(object.baseValue());
			}
			/**
			 * 16. If Type(x) is Number and Type(y) is String,<br>
			 * return the result of the comparison x == ToNumber(y).
			 */
			if (object.baseIsPrimitiveString()) {
				return this.baseValue().equals(object.baseToNumber().baseValue());
			}
			/**
			 * 20. If Type(x) is either String or Number and Type(y) is Object,
			 * <br>
			 * return the result of the comparison x == ToPrimitive(y).
			 */
			if (!object.baseIsPrimitive()) {
				return this.equals(object.baseToPrimitive(ToPrimitiveHint.NUMBER));
			}
		}
		if (this.baseIsPrimitiveString()) {
			assert false : "PrimitiveString object should have it's own equals method implementation!";
			/**
			 * 11. If Type(x) is String, then return true if x and y are exactly
			 * the same sequence of characters (same length and same characters
			 * in corresponding positions). Otherwise, return false.
			 */
			if (object.baseIsPrimitiveString()) {
				return this.baseValue().equals(object.baseValue());
			}
			/**
			 * 17. If Type(x) is String and Type(y) is Number,<br>
			 * return the result of the comparison ToNumber(x) == y.
			 */
			if (object.baseIsPrimitiveNumber()) {
				return this.baseToNumber().baseValue().equals(object.baseValue());
			}
			/**
			 * 20. If Type(x) is either String or Number and Type(y) is Object,
			 * <br>
			 * return the result of the comparison x == ToPrimitive(y).
			 */
			if (!object.baseIsPrimitive()) {
				return this.equals(object.baseToPrimitive(ToPrimitiveHint.STRING));
			}
		}
		/**
		 * !!!SKIPPED:<br>
		 * 13. Return true if x and y refer to the same object or if they refer
		 * to objects joined to each other (section 13.1.2). Otherwise, return
		 * false.
		 *
		 */
		/**
		 * 14. If x is null and y is undefined, return true.<br>
		 * 15. If x is undefined and y is null, return true. <br>
		 */
		if (o == BaseObject.UNDEFINED || o == BaseObject.NULL) {
			return false;
		}
		/**
		 * 18. If Type(x) is Boolean,<br>
		 * return the result of the comparison ToNumber(x) == y.
		 */
		assert !this.baseIsPrimitiveBoolean() : "PrimitiveBoolean object must have it's own equals method implementation!";
		/**
		 * 19. If Type(y) is Boolean,<br>
		 * return the result of the comparison x == ToNumber(y).
		 */
		if (object.baseIsPrimitiveBoolean()) {
			return this.equals(object.baseToNumber());
		}
		/**
		 * 21. If Type(x) is Object and Type(y) is either String or Number,<br>
		 * return the result of the comparison ToPrimitive(x) == y.
		 */
		if (!this.baseIsPrimitive()) {
			if (object.baseIsPrimitiveString()) {
				return this.baseToPrimitive(ToPrimitiveHint.STRING).equals(object);
			}
			if (object.baseIsPrimitiveNumber()) {
				return this.baseToPrimitive(ToPrimitiveHint.NUMBER).equals(object);
			}
		}
		/**
		 * 22. Return false.
		 */
		return false;
	}

	@Override
	public int hashCode() {
		
		return super.hashCode();
	}

	@Override
	public short propertyAttributes(final CharSequence name) {
		
		return BaseProperty.ATTRS_MASK_NEN;
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString key) {
		
		return this.baseGetLookupValue(key);
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		return this.baseGetLookupValue(Base.forString(name));
	}

	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		return this.baseGetLookupValue(Base.forString(name));
	}

	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> key, final ResultHandler store) {
		
		return store.execReturn(ctx, key instanceof BasePrimitiveString
			? this.propertyGet(instance, (BasePrimitiveString) key)
			: this.propertyGet(instance, key.toString()));
	}

	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final BasePrimitiveString name, final BaseObject defaultValue, final ResultHandler store) {
		
		return store.execReturn(ctx, this.baseGet(name, defaultValue));
	}

	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final String name, final BaseObject defaultValue, final ResultHandler store) {
		
		return store.execReturn(ctx, this.baseGet(name, defaultValue));
	}

	@Override
	public BasePrimitiveString baseToPrimitive(final ToPrimitiveHint hint) {
		
		return this.baseToString();
	}

	@Override
	public BasePrimitiveString baseToString() {
		
		return Base.forString(this.toString());
	}

	@Override
	public abstract String toString();
	
	public static final BaseObject obtainLookup(final BaseObject attributes) {
		
		assert attributes != null : "NULL java value";
		final BaseObject lookupObject = attributes.baseGet("lookup", BaseObject.UNDEFINED);
		assert lookupObject != null : "NULL java value";
		if (lookupObject != BaseObject.UNDEFINED) {
			if (lookupObject instanceof BaseHostLookup) {
				return lookupObject;
			}
			if (lookupObject.baseIsPrimitive()) {
				final String lookupID = lookupObject.baseToJavaString();
				final BaseHostLookup lookup = Produce.object(BaseHostLookup.class, lookupID, attributes, lookupObject);
				if (lookup != null) {
					return lookup;
				}
				throw new IllegalArgumentException("Lookup " + lookupID + " is not accessible!");
			}
			{
				// return Context.getServer().getLookups().baseGet( lookupID );
				final BaseHostLookup lookup = Produce.object(BaseHostLookup.class, "LOOKUP", attributes, lookupObject);
				if (lookup != null) {
					return lookup;
				}
			}
			throw new IllegalArgumentException("Lookup is not accessible!");
		}
		final String staticDefinition = Base.getString(attributes, "static", null);
		if (staticDefinition != null) {
			return new ControlLookupStatic(staticDefinition, Base.getString(attributes, "staticED", "|"), Base.getString(attributes, "staticFD", ","));
		}
		final String dynamicDefinition = Base.getString(attributes, "dynamic", null);
		if (dynamicDefinition == null) {
			throw new IllegalArgumentException("Lookup unavailable!");
		}
		return new ControlLookupDynamic(dynamicDefinition, Base.getString(attributes, "staticED", "|"), Base.getString(attributes, "staticFD", ","));
	}
}
