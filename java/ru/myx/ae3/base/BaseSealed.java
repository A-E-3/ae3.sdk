package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionHidden;

/** @author myx */
public class BaseSealed implements BaseObjectNotWritable, BaseArrayAdvanced<Object>, BaseProperty {

	private final BaseObject wrapped;

	/** @param wrapped */
	public BaseSealed(final BaseObject wrapped) {
		
		this.wrapped = wrapped;
	}

	/** Not always an array */
	@SuppressWarnings("deprecation")
	@Override
	public BaseArray baseArray() {

		return this.wrapped.baseArray() == null
			? null
			: this;
	}

	/** Not always advanced */
	@SuppressWarnings("deprecation")
	@Override
	public BaseArrayAdvanced<?> baseArrayAdvanced() {

		/** called from baseArray() result, expecting to be not null. */
		return this.wrapped.baseArray().baseArrayAdvanced() == null
			? null
			: this;
	}

	@Override
	@ReflectionHidden
	public BaseArrayAdvanced<?> baseArraySlice(final int start) {

		final BaseArray array = this.wrapped.baseArray();
		assert array != null : "Not an array actually!";
		return array.baseArraySlice(start);
	}

	@Override
	@ReflectionHidden
	public BaseArrayAdvanced<?> baseArraySlice(final int start, final int end) {

		final BaseArray array = this.wrapped.baseArray();
		assert array != null : "Not an array actually!";
		return array.baseArraySlice(start, end);
	}

	@Override
	public BaseFunction baseCall() {

		return this.wrapped.baseCall();
	}

	@Override
	public String baseClass() {

		return this.wrapped.baseClass();
	}

	@Override
	public void baseClear() {

		// ignore
	}

	@Override
	public BaseFunction baseConstruct() {

		return this.wrapped.baseConstruct();
	}

	@Override
	public boolean baseContains(final BaseObject value) {

		final BaseArray array = this.wrapped.baseArray();
		assert array != null : "Not an array actually!";
		return array.baseContains(value);
	}

	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {

		return this.wrapped.baseArray().baseGet(index, defaultValue);
	}

	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.wrapped.baseArray().baseArrayAdvanced().baseGetFirst(defaultValue);
	}

	@Override
	public BaseObject baseGetLast(final BaseObject defaultValue) {

		return this.wrapped.baseArray().baseArrayAdvanced().baseGetLast(defaultValue);
	}

	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {

		return this.wrapped.baseGetOwnProperty(name) == null
			? null
			: this;
	}

	@Override
	public BaseProperty baseGetOwnProperty(final String name) {

		return this.wrapped.baseGetOwnProperty(name) == null
			? null
			: this;
	}

	@Override
	public boolean baseHasKeysOwn() {

		return this.wrapped.baseHasKeysOwn();
	}

	@Override
	public Iterator<? extends BaseObject> baseIterator() {

		final BaseArray array = this.wrapped.baseArray();
		assert array != null : "Not an array actually!";
		return array.baseIterator();
	}

	@Override
	public Iterator<String> baseKeysOwn() {

		return this.wrapped.baseKeysOwn();
	}

	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {

		return this.wrapped.baseKeysOwnAll();
	}

	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {

		return this.wrapped.baseKeysOwnPrimitive();
	}

	@Override
	public BaseObject basePrototype() {

		return this.wrapped.basePrototype();
	}

	@Override
	public BasePrimitiveBoolean baseToBoolean() {

		return this.wrapped.baseToBoolean();
	}

	@Override
	public BasePrimitiveNumber baseToInt32() {

		return this.wrapped.baseToInt32();
	}

	@Override
	public BasePrimitiveNumber baseToInteger() {

		return this.wrapped.baseToInteger();
	}

	@Override
	public BasePrimitiveNumber baseToNumber() {

		return this.wrapped.baseToNumber();
	}

	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {

		return this.wrapped.baseToPrimitive(hint);
	}

	@Override
	public BasePrimitiveString baseToString() {

		return this.wrapped.baseToString();
	}

	@Override
	public Object baseValue() {

		return this.wrapped.baseValue();
	}

	@Override
	public boolean equals(final Object o) {

		return this.wrapped.equals(o);
	}

	@Override
	public Object get(final int index) {

		return this.wrapped.baseArray().baseArrayAdvanced().get(index);
	}

	@Override
	public int hashCode() {

		return this.wrapped.hashCode();
	}

	@Override
	public boolean isEmpty() {

		return this.wrapped.baseArray().baseArrayAdvanced().isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {

		final BaseArray array = this.wrapped.baseArray();
		assert array != null : "Not an array actually!";
		final Iterator<? extends BaseObject> iterator = array.baseIterator();
		return new Iterator<Object>() {

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
	public int length() {

		return this.wrapped.baseArray().length();
	}

	@Override
	public short propertyAttributes(final CharSequence name) {

		return (short) (this.wrapped.baseGetOwnProperty(name).propertyAttributes(name) & ~BaseProperty.ATTRS_MASK_WND);
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString key) {

		return this.wrapped.baseGet(key, BaseObject.UNDEFINED);
	}

	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {

		return this.wrapped.baseGet(name, BaseObject.UNDEFINED);
	}

	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {

		return this.wrapped.baseGet(name, BaseObject.UNDEFINED);
	}

	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {

		return this.wrapped.vmPropertyRead(ctx, name, BaseObject.UNDEFINED, store);
	}

	@Override
	public String toString() {

		return this.wrapped.toString();
	}

	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return this.wrapped.baseArray().vmPropertyRead(ctx, index, originalIfKnown, defaultValue, store);
	}
}
