/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Collections;
import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;
import ru.myx.util.IteratorSingle;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsMap1 extends ExecArgumentsAbstract implements BaseProperty {
	
	private static int COUNT = 0;

	static final void statusFill(final StatusInfo data) {
		
		data.put("ARGS: 'Map1' count", Format.Compact.toDecimal(ExecArgumentsMap1.COUNT));
	}

	private final String key;

	private final BaseObject value;

	/** @param key
	 * @param value */
	@ReflectionHidden
	public ExecArgumentsMap1(final String key, final BaseObject value) {

		assert value != null : "NULL java object!";
		ExecArgumentsMap1.COUNT++;
		this.key = key;
		this.value = value;
	}

	@Override
	@ReflectionHidden
	public boolean baseContains(final BaseObject value) {
		
		return this.value == value || this.value.equals(value);
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {
		
		return i == 0
			? this.value
			: defaultValue;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		return this.value;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetLast(final BaseObject defaultValue) {
		
		return this.value;
	}

	@Override
	@ReflectionHidden
	public final BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		return this.key.compareTo(name.toString()) != 0
			? super.baseGetOwnProperty(name)
			: this;
	}

	@Override
	@ReflectionHidden
	public final BaseProperty baseGetOwnProperty(final String name) {
		
		return this.key.compareTo(name) != 0
			? super.baseGetOwnProperty(name)
			: this;
	}

	@Override
	@ReflectionHidden
	public boolean baseHasKeysOwn() {
		
		return true;
	}

	@Override
	@ReflectionHidden
	public Iterator<? extends BaseObject> baseIterator() {
		
		return new IteratorSingle<>(this.value);
	}

	@Override
	@ReflectionHidden
	public Iterator<String> baseKeysOwn() {
		
		return Collections.singleton(this.key).iterator();
	}

	@Override
	@ReflectionHidden
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return Base.iteratorPrimitiveSafe(this.baseKeysOwn());
	}

	@Override
	@ReflectionHidden
	public final Object get(final int i) {
		
		return this.value.baseValue();
	}

	@Override
	@ReflectionHidden
	public boolean isEmpty() {
		
		return false;
	}

	@Override
	@ReflectionHidden
	public Iterator<Object> iterator() {
		
		return new IteratorSingle<>(this.value.baseValue());
	}

	@Override
	@ReflectionHidden
	public final int length() {
		
		return 1;
	}

	@Override
	@ReflectionHidden
	public short propertyAttributes(final CharSequence name) {
		
		// assert this.key == name.toString() || this.key.compareTo(
		// name.toString() ) == 0 : "key should be equal";
		return BaseProperty.ATTRS_MASK_NNN;
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {
		
		return this.value;
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGet(final BaseObject instance, final String name) {
		
		return this.value;
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		return this.value;
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		return store.execReturn(ctx, this.value);
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		return store.execReturn(ctx, index == 0
			? this.value
			: defaultValue);
	}
}
