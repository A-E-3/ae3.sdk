package ru.myx.ae3.base;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ControlType;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.util.IteratorSingle;

/** new Array() or simply [] in javascript will create an instance of this class.
 *
 * Методы isPrimitiveXXX зафиксированы в значение false.
 *
 * @author myx */
@ReflectionDisable
public class BaseNativeArray extends BaseNativeAbstract implements BaseProperty, BaseList<Object>, RandomAccess {
	
	private final List<BaseObject> array;
	
	/**
	 *
	 */
	public BaseNativeArray() {

		super(BaseArray.PROTOTYPE);
		this.array = new ArrayList<>();
	}
	
	/** @param element */
	public BaseNativeArray(final BaseObject element) {

		super(BaseArray.PROTOTYPE);
		this.array = new ArrayList<>();
		this.array.add(element);
	}
	
	/** @param elements */
	@SafeVarargs
	public BaseNativeArray(final BaseObject... elements) {

		super(BaseArray.PROTOTYPE);
		this.array = new ArrayList<>(elements.length + 16);
		for (final BaseObject element : elements) {
			this.array.add(element);
		}
	}
	
	/** for tests
	 *
	 * @param prototype
	 * @param adoptProperties */
	BaseNativeArray(final BaseObject prototype, final BaseProperties<?> adoptProperties) {

		super(prototype);
		this.array = new ArrayList<>();
		this.properties = adoptProperties;
	}
	
	/** @param key
	 * @param value */
	public BaseNativeArray(final BasePrimitiveString key, final BaseObject value) {

		super(BaseArray.PROTOTYPE);
		this.properties = BaseProperties.createFirstProperty(key, value, BaseProperty.ATTRS_MASK_WED);
		this.array = new ArrayList<>();
	}
	
	/** special constructor for BaseArray.PROTOTYPE */
	BaseNativeArray(final boolean b) {

		super(BaseObject.PROTOTYPE);
		assert b;
		this.array = Collections.emptyList();
	}
	
	/** @param length
	 *
	 *            This is not ecma array constructor with length argument, this length is just a
	 *            hint, see <code>BaseNativeArray( length, value )</code> for filler constructor */
	public BaseNativeArray(final int length) {

		super(BaseArray.PROTOTYPE);
		this.array = new ArrayList<>(length);
	}
	
	/** @param length
	 * @param value
	 *            - filler value
	 *
	 *            To emulate ecma array constructor with length argument use expression like:
	 *            <code>new BaseNativeArray( x, BaseObject.UNDEFINED )</code> */
	public BaseNativeArray(final int length, final BaseObject value) {

		super(BaseArray.PROTOTYPE);
		this.array = new ArrayList<>(length);
		for (int i = 0; i < length; ++i) {
			this.array.add(value);
		}
	}
	
	private BaseNativeArray(final List<BaseObject> array) {

		super(BaseArray.PROTOTYPE);
		this.array = array;
	}
	
	/** @param key
	 * @param value */
	public BaseNativeArray(final String key, final BaseObject value) {

		super(BaseArray.PROTOTYPE);
		this.properties = BaseProperties.createFirstProperty(key, value, BaseProperty.ATTRS_MASK_WED);
		this.array = new ArrayList<>();
	}
	
	/** @param elements */
	/** <code>
	public BaseNativeArray(final T... elements) {
		super( BaseArray.PROTOTYPE );
		this.array = new ArrayList<BaseObject<? extends T>>( elements.length );
		for (final T element : elements) {
			this.add( element );
		}
	}
	</code> */
	
	@Override
	public void add(final int index, final Object element) {
		
		this.array.add(index, Base.forUnknown(element));
	}
	
	/** @param element */
	/** <code>
	public BaseNativeArray(final T element) {
		super( BaseArray.PROTOTYPE );
		this.array = new ArrayList<BaseObject<? extends T>>();
		this.add( element );
	}
	</code> */
	
	@Override
	public final boolean add(final Object object) {
		
		return this.array.add(Base.forUnknown(object));
	}
	
	@Override
	public boolean addAll(final Collection<?> c) {
		
		return this.array.addAll(new AbstractCollection<BaseObject>() {
			
			@Override
			public Iterator<BaseObject> iterator() {
				
				final Iterator<?> iterator = c.iterator();
				return new Iterator<>() {
					
					@Override
					public boolean hasNext() {
						
						return iterator.hasNext();
					}
					
					@Override
					public BaseObject next() {
						
						return Base.forUnknown(iterator.next());
					}
					
					@Override
					public void remove() {
						
						iterator.remove();
					}
				};
			}
			
			@Override
			public int size() {
				
				return c.size();
			}
		});
	}
	
	@Override
	public boolean addAll(final int index, final Collection<?> c) {
		
		return this.array.addAll(index, new AbstractCollection<BaseObject>() {
			
			@Override
			public Iterator<BaseObject> iterator() {
				
				final Iterator<?> iterator = c.iterator();
				return new Iterator<>() {
					
					@Override
					public boolean hasNext() {
						
						return iterator.hasNext();
					}
					
					@Override
					public BaseObject next() {
						
						return Base.forUnknown(iterator.next());
					}
					
					@Override
					public void remove() {
						
						iterator.remove();
					}
				};
			}
			
			@Override
			public int size() {
				
				return c.size();
			}
		});
	}
	
	@Override
	public BaseArrayAdvanced<?> baseArraySlice(final int start) {
		
		final int length = this.array.size();
		final int startIdx = start >= 0
			? start > length
				? length
				: start
			: Math.max(length + start, 0);
		final BaseNativeArray result = new BaseNativeArray();
		for (int i = startIdx; i < length; ++i) {
			result.array.add(this.array.get(i));
		}
		return result;
	}
	
	@Override
	public BaseArrayAdvanced<?> baseArraySlice(final int start, final int end) {
		
		final int length = this.array.size();
		final int startIdx = start >= 0
			? start > length
				? length
				: start
			: Math.max(length + start, 0);
		final int endIdx = end > 0
			? end > length
				? length
				: end
			: Math.max(length + end, 0);
		final BaseNativeArray result = new BaseNativeArray();
		for (int i = startIdx; i < endIdx; ++i) {
			result.array.add(this.array.get(i));
		}
		return result;
	}
	
	/** The value of the [[Class]] property is defined by this specification for every kind of
	 * built-in object. The value of the [[Class]] property of a host object may be any value, even
	 * a value used by a built-in object for its [[Class]] property. The value of a [[Class]]
	 * property is used internally to distinguish different kinds of built-in objects. Note that
	 * this specification does not provide any means for a program to access that value except
	 * through Object.prototype.toString (see 15.2.4.2).
	 *
	 * @return class */
	@Override
	public String baseClass() {
		
		return "Array";
	}
	
	@Override
	public final boolean baseContains(final BaseObject object) {
		
		return this.array.contains(object);
	}
	
	@Override
	public BaseObject baseDefaultPop() {
		
		final int length = this.array.size();
		return length == 0
			? BaseObject.UNDEFINED
			: this.array.remove(length - 1);
	}
	
	@Override
	public final int baseDefaultPush(final BaseObject object) {
		
		this.array.add(object);
		return this.array.size();
	}
	
	@Override
	public BaseNativeArray baseDefaultReverse() {
		
		Collections.reverse(this.array);
		return this;
	}
	
	@Override
	public BaseObject baseDefaultShift() {
		
		final int length = this.array.size();
		return length == 0
			? BaseObject.UNDEFINED
			: this.array.remove(0);
	}
	
	@Override
	public final BaseArray baseDefaultSplice(final int start, final int count, final BaseObject[] values) {
		
		/** 3. Let lenVal be the result of calling the [[Get]] internal method of O with argument
		 * "length".<br>
		 * 4. Let len be ToUint32(lenVal). */
		final int length = this.array.size();
		/** 5. Let relativeStart be ToInteger(start).<br>
		 * 6. If relativeStart is negative, let actualStart be max((len + relativeStart),0); else
		 * let actualStart be min(relativeStart, len). */
		final int actualStart = start < 0
			? Math.max(length + start, 0)
			: Math.min(start, length);
		/** 7. Let actualDeleteCount be min(max(ToInteger(deleteCount),0), len – actualStart). */
		final int deleteCount = Math.min(Math.max(count, 0), length - start);
		final BaseArray result = this.baseArraySlice(actualStart, deleteCount);
		final int insertCount = values.length;
		if (deleteCount > insertCount) {
			for (int i = deleteCount - insertCount; i > 0; --i) {
				this.array.remove(start);
			}
			for (int i = 0; i < insertCount; ++i) {
				this.array.set(start + i, values[i]);
			}
		} else //
		if (deleteCount < insertCount) {
			this.array.addAll(start, Arrays.asList(values).subList(insertCount - deleteCount, insertCount));
			for (int i = 0; i < insertCount - deleteCount; ++i) {
				this.array.set(start + i, values[i]);
			}
		} else {
			for (int i = 0; i < insertCount; ++i) {
				this.array.set(start + i, values[i]);
			}
		}
		return result;
	}
	
	@Override
	public final int baseDefaultUnshift(final BaseObject object) {
		
		this.array.add(0, object);
		return this.array.size();
	}
	
	@Override
	public final int baseDefaultUnshift(final BaseObject[] objects) {
		
		this.array.addAll(0, Arrays.asList(objects));
		return this.array.size();
	}
	
	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {
		
		return index >= 0 && index < this.array.size()
			? this.array.get(index)
			: defaultValue;
	}
	
	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		return this.array.size() > 0
			? this.array.get(0)
			: defaultValue;
	}
	
	@Override
	public BaseObject baseGetLast(final BaseObject defaultValue) {
		
		final int length = this.array.size();
		return length == 0
			? defaultValue
			: this.array.get(length - 1);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		if (name == BaseString.STR_LENGTH) {
			return this;
		}
		{
			final BaseProperties<?> properties = this.properties;
			if (properties != null) {
				final BaseProperty found = properties.find(name);
				if (found != null) {
					return found;
				}
			}
		}
		{
			final double dbl = name.doubleValue();
			final int int32 = (int) dbl;
			if (int32 == dbl) {
				return BaseArrayNumericStringProperty.INSTANCE;
			}
		}
		return null;
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		if (BasePrimitiveString.PROPERTY_JAVA_LENGTH.equals(name)) {
			return this;
		}
		{
			final BaseProperties<?> properties = this.properties;
			if (properties != null) {
				final BaseProperty found = properties.find(name);
				if (found != null) {
					return found;
				}
			}
		}
		{
			final double dbl = Base.toDouble(name);
			final int int32 = (int) dbl;
			if (int32 == dbl) {
				return BaseArrayNumericStringProperty.INSTANCE;
			}
		}
		return null;
	}
	
	@Override
	public final boolean baseHasKeysOwn() {
		
		final BaseProperties<?> properties = this.properties;
		return properties != null && properties.hasEnumerableProperties();
	}
	
	@Override
	public int baseInsert(final int index, final BaseObject object) {
		
		this.array.add(index, object);
		return this.array.size();
	}
	
	@Override
	public Iterator<BaseObject> baseIterator() {
		
		return this.array.iterator();
	}
	
	/** Never returns NULL
	 *
	 * @return */
	@Override
	public Iterator<String> baseKeysOwn() {
		
		if (this.array.isEmpty()) {
			return super.baseKeysOwn();
		}
		if (this.properties == null) {
			return new IteratorBaseArrayKey(this);
		}
		final Iterator<String> iterator = this.properties.iteratorEnumerableAsString();
		assert iterator != null : "NULL iterator: use BaseObject.ITERATOR_EMPTY";
		return iterator == BaseObject.ITERATOR_EMPTY
			? new IteratorBaseArrayKey(this)
			: new IteratorSequenceString(new IteratorBaseArrayKey(this), iterator);
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		if (this.array.isEmpty()) {
			return this.properties == null
				? new IteratorSingle<>(BasePrimitiveString.PROPERTY_JAVA_LENGTH)
				: new IteratorSequenceString(this.properties.iteratorAllAsString(), new IteratorSingle<>(BasePrimitiveString.PROPERTY_JAVA_LENGTH));
		}
		if (this.properties == null) {
			return new IteratorSequenceString(new IteratorBaseArrayKey(this), new IteratorSingle<>(BasePrimitiveString.PROPERTY_JAVA_LENGTH));
		}
		final Iterator<String> iterator = this.properties.iteratorAllAsString();
		assert iterator != null : "NULL iterator: use BaseObject.ITERATOR_EMPTY";
		return iterator == BaseObject.ITERATOR_EMPTY
			? new IteratorSequenceString(new IteratorSequenceString(new IteratorBaseArrayKey(this), iterator), new IteratorSingle<>(BasePrimitiveString.PROPERTY_JAVA_LENGTH))
			: new IteratorSequenceString(new IteratorBaseArrayKey(this), iterator);
	}
	
	/** Never returns NULL
	 *
	 * @return */
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		if (this.array.isEmpty()) {
			return super.baseKeysOwnPrimitive();
		}
		if (this.properties == null) {
			return new IteratorBaseArrayKeyPrimitive(this);
		}
		final Iterator<BasePrimitiveString> iterator = this.properties.iteratorEnumerableAsPrimitive();
		assert iterator != null : "NULL iterator: use BaseObject.ITERATOR_EMPTY_PRIMITIVE";
		return iterator == BaseProperties.ITERATOR_EMPTY_PRIMITIVE_STRING
			? new IteratorBaseArrayKeyPrimitive(this)
			: new IteratorSequenceAnyPrimitive(new IteratorBaseArrayKeyPrimitive(this), iterator);
	}
	
	@Override
	public BaseObject baseRemove(final int index) {
		
		return this.array.remove(index);
	}
	
	/** @param object */
	@Override
	public final boolean baseSet(final int index, final BaseObject object) {
		
		if (index < 0) {
			return false;
		}
		assert object != null : "NULL java object!";
		int length = this.array.size();
		if (index < length) {
			this.array.set(index, object);
			return true;
		}
		for (; length < index; length++) {
			this.array.add(BaseObject.UNDEFINED);
		}
		this.array.add(index, object);
		return true;
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		final int length = this.size();
		if (length == 0) {
			return BaseString.EMPTY;
		}
		final StringBuilder builder = new StringBuilder(length * 10);
		for (int i = 0; i < length; ++i) {
			final BaseObject element = this.array.get(i);
			assert element != null : "NULL java value!";
			if (i != 0) {
				builder.append(',');
			}
			if (element == BaseObject.UNDEFINED || element == BaseObject.NULL) {
				// ignore
			} else {
				builder.append(element.baseToJavaString());
			}
		}
		return Base.forString(builder.toString());
	}
	
	@Override
	public final BaseNativeArray baseValue() {
		
		return this;
	}
	
	@Override
	public void clear() {
		
		this.array.clear();
	}
	
	@Override
	public boolean contains(final Object o) {
		
		return this.array.contains(o) || this.array.contains(Base.forUnknown(o));
	}
	
	@Override
	public boolean containsAll(final Collection<?> c) {
		
		final Iterator<?> e = c.iterator();
		while (e.hasNext()) {
			if (!this.contains(e.next())) {
				return false;
			}
		}
		return true;
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
		/** instanceof is always false on NULL object */
		if (!(o instanceof BaseObject)) {
			return false;
		}
		if (o == BasePrimitiveNumber.NAN) {
			return false;
		}
		final BaseObject object = (BaseObject) o;
		assert !this.baseIsPrimitive() : "BaseNativeObject is NOT primitive!";
		/** 14. If x is null and y is undefined, return true.<br>
		 * 15. If x is undefined and y is null, return true. <br>
		*/
		if (o == BaseObject.UNDEFINED || o == BaseObject.NULL) {
			return false;
		}
		/** 21. If Type(x) is Object and Type(y) is either String or Number,<br>
		 * return the result of the comparison ToPrimitive(x) == y. */
		{
			if (object.baseIsPrimitiveString()) {
				return this.baseToPrimitive(ToPrimitiveHint.STRING).equals(object);
			}
			if (object.baseIsPrimitiveNumber()) {
				return this.baseToPrimitive(ToPrimitiveHint.NUMBER).equals(object);
			}
		}
		
		/** !!! NON-STANDARD: check arrays and properties! */
		if (o instanceof BaseNativeArray && this.prototype == object.basePrototype()) {
			final BaseNativeArray a = (BaseNativeArray) o;
			final int length = this.length();
			if (a.length() != length) {
				return false;
			}
			for (int i = 0; i < length; ++i) {
				final BaseObject original = this.baseGet(i, BaseObject.UNDEFINED);
				assert original != null : "NULL java value";
				final BaseObject effective = a.baseGet(i, BaseObject.UNDEFINED);
				assert effective != null : "NULL java value";
				if (original != effective && !original.equals(effective)) {
					return false;
				}
			}
			if (this.properties == null) {
				return a.properties == null;
			}
			for (final Iterator<? extends CharSequence> iterator = this.properties.iteratorAll(); iterator.hasNext();) {
				final CharSequence key = iterator.next();
				final BaseObject original = this.baseGet(key, BaseObject.UNDEFINED);
				assert original != null : "NULL java value";
				final BaseObject effective = object.baseGet(key, BaseObject.UNDEFINED);
				assert effective != null : "NULL java value";
				if (original != effective && !original.equals(effective)) {
					return false;
				}
			}
			for (final Iterator<? extends CharSequence> iterator = a.properties.iteratorAll(); iterator.hasNext();) {
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
	public Object get(final int index) {
		
		return this.array.get(index).baseValue();
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
		
		int hashCode = 0;
		if (this.array != null) {
			final int length = this.array.size();
			for (int i = 0; i < length; ++i) {
				hashCode >>>= 11;
				final Object value = this.array.get(i);
				hashCode ^= value == null
					? 0
					: value.hashCode();
			}
		}
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
		if (this.prototype != null && this.prototype != BaseObject.PROTOTYPE && this.prototype != BaseArray.PROTOTYPE) {
			hashCode >>>= 11;
			hashCode ^= this.prototype.hashCode();
		}
		return hashCode;
	}
	
	@Override
	public int indexOf(final Object o) {
		
		return this.array.indexOf(o);
	}
	
	@Override
	public boolean isEmpty() {
		
		return this.array.isEmpty();
	}
	
	@Override
	public Iterator<Object> iterator() {
		
		final Iterator<BaseObject> iterator = this.baseIterator();
		return new Iterator<>() {
			
			@Override
			public boolean hasNext() {
				
				return iterator.hasNext();
			}
			
			@Override
			public Object next() {
				
				return iterator.next().baseValue();
			}
			
			@Override
			public void remove() {
				
				iterator.remove();
			}
		};
	}
	
	@Override
	public int lastIndexOf(final Object o) {
		
		return this.array.lastIndexOf(Base.forUnknown(o));
	}
	
	@Override
	public int length() {
		
		return this.array.size();
	}
	
	@Override
	public ListIterator<Object> listIterator() {
		
		return this.listIterator(0);
	}
	
	@Override
	public ListIterator<Object> listIterator(final int index) {
		
		final ListIterator<BaseObject> iterator = this.array.listIterator(index);
		return new ListIterator<>() {
			
			@Override
			public void add(final Object e) {
				
				iterator.add(Base.forUnknown(e));
			}
			
			@Override
			public boolean hasNext() {
				
				return iterator.hasNext();
			}
			
			@Override
			public boolean hasPrevious() {
				
				return iterator.hasPrevious();
			}
			
			@Override
			public Object next() {
				
				return iterator.next().baseValue();
			}
			
			@Override
			public int nextIndex() {
				
				return iterator.nextIndex();
			}
			
			@Override
			public Object previous() {
				
				return iterator.previous().baseValue();
			}
			
			@Override
			public int previousIndex() {
				
				return iterator.previousIndex();
			}
			
			@Override
			public void remove() {
				
				iterator.remove();
			}
			
			@Override
			public void set(final Object e) {
				
				iterator.set(Base.forUnknown(e));
			}
		};
	}
	
	@Override
	public short propertyAttributes(final CharSequence name) {
		
		assert BasePrimitiveString.PROPERTY_JAVA_LENGTH.equals(name) : "Expected to be equal to 'length', but: " + name;
		return BaseProperty.ATTRS_MASK_NNN_NPN;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {
		
		return Base.forInteger(this.size());
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		return Base.forInteger(this.size());
	}
	
	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		return Base.forInteger(this.size());
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		return store.execReturnNumeric(ctx, this.size());
	}
	
	/** Funny method returns same object. For use in in-line initializations.
	 * <p>
	 * Will return NULL if put fails, but this possible only when prototype object explicitly
	 * prohibits it or property was previously set as read-only.
	 *
	 * @param value
	 * @return */
	public final BaseNativeArray putAppend(final BaseObject value) {
		
		return this.array.add(value)
			? this
			: null;
	}
	
	/** Funny method returns same object. For use in in-line initializations.
	 * <p>
	 * Will return NULL if put fails, but this possible only when prototype object explicitly
	 * prohibits it or property was previously set as read-only.
	 *
	 * @param key
	 * @param value
	 * @return */
	public final BaseNativeArray putAppend(final String key, final BaseObject value) {
		
		return this.baseDefine(key, value, BaseProperty.ATTRS_MASK_WED)
			? this
			: null;
	}
	
	@Override
	public Object remove(final int index) {
		
		return this.array.remove(index).baseValue();
	}
	
	@Override
	public boolean remove(final Object o) {
		
		return this.array.remove(o);
	}
	
	@Override
	public boolean removeAll(final Collection<?> c) {
		
		return this.array.removeAll(c);
	}
	
	@Override
	public boolean retainAll(final Collection<?> c) {
		
		return this.array.retainAll(c);
	}
	
	/** @param object */
	@Override
	public final Object set(final int index, final Object object) {
		
		return this.array.set(index, Base.forUnknown(object)).baseValue();
	}
	
	@Override
	public int size() {
		
		return this.array.size();
	}
	
	@Override
	public BaseNativeArray subList(final int fromIndex, final int toIndex) {
		
		return new BaseNativeArray(this.array.subList(fromIndex, toIndex));
	}
	
	@Override
	public Object[] toArray() {
		
		return this.array.toArray();
	}
	
	@Override
	public <E> E[] toArray(final E[] a) {
		
		final int length = a.length;
		if (length == 0) {
			return a;
		}
		final int size = this.array.size();
		if (size == 0) {
			return a;
		}
		final Class<?> elementClass = a.getClass().getComponentType();
		final ControlType<?, ?> convertor = Reflect.getConverter(elementClass);
		
		@SuppressWarnings("unchecked")
		final E[] result = length < size
			? (E[]) Array.newInstance(elementClass, size)
			: a;
		
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			final E converted = (E) convertor.convertAnyNativeToJava(this.array.get(i));
			result[i] = converted;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		
		final int length = this.size();
		if (length == 0) {
			return "";
		}
		final StringBuilder builder = new StringBuilder(length * 10);
		for (int i = 0; i < length; ++i) {
			final BaseObject element = this.array.get(i);
			assert element != null : "NULL java value!";
			if (i != 0) {
				builder.append(',');
			}
			if (element == BaseObject.UNDEFINED || element == BaseObject.NULL) {
				// ignore
			} else {
				builder.append(element.baseToJavaString());
			}
		}
		return builder.toString();
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		return store.execReturn(ctx, index >= 0 && index < this.array.size()
			? this.array.get(index)
			: defaultValue);
	}
}
