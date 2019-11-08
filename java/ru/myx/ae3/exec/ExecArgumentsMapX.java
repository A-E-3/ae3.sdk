/**
 *
 */
package ru.myx.ae3.exec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsMapX extends ExecArgumentsAbstract implements BaseProperty, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6747706660422012389L;

	private static int COUNT = 0;

	/** @param map
	 * @return */
	public static final ExecArguments createArguments(final BaseObject map) {

		if (map == null || map.baseIsPrimitive()) {
			return ExecArgumentsEmpty.INSTANCE;
		}
		if (!map.baseHasKeysOwn()) {
			final BaseArray array = map.baseArray();
			return array != null
				? array instanceof ExecArguments
					? (ExecArguments) array
					: new ExecArgumentsListXWrapped(array)
				: ExecArgumentsEmpty.INSTANCE;
		}

		// assert map.baseKeysOwn().hasNext() //
		// : "cls: " + map.getClass().getSimpleName();

		// assert map.baseKeysOwnAll().hasNext() //
		// : "cls: " + map.getClass().getSimpleName();

		return new ExecArgumentsMapX(map);
	}

	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'MapX' count", Format.Compact.toDecimal(ExecArgumentsMapX.COUNT));
	}

	private BaseObject[] argument = null;

	private final BaseMap map = new BaseNativeObject(null);

	/** @param arguments */
	@ReflectionHidden
	public ExecArgumentsMapX(final BaseObject arguments) {

		assert Base.hasKeys(arguments) : "Expected to have own properties!";
		ExecArgumentsMapX.COUNT++;
		final BaseArray array = arguments.baseArray();
		if (array == null) {
			final List<BaseObject> list = new ArrayList<>();
			for (final Iterator<String> iterator = Base.keys(arguments); iterator.hasNext();) {
				final String key = iterator.next();
				final BaseObject value = arguments.baseGet(key, BaseObject.UNDEFINED);
				list.add(value);
				this.map.baseDefine(key, value, BaseProperty.ATTRS_MASK_NEN);
			}
			this.argument = list.toArray(new BaseObject[list.size()]);
		} else {
			for (final Iterator<String> iterator = Base.keys(arguments); iterator.hasNext();) {
				final String key = iterator.next();
				final BaseObject value = arguments.baseGet(key, BaseObject.UNDEFINED);
				this.map.baseDefine(key, value, BaseProperty.ATTRS_MASK_NEN);
			}
			this.argument = new BaseObject[array.length()];
			for (int i = 0; i < this.argument.length; ++i) {
				final BaseObject value = array.baseGet(i, BaseObject.UNDEFINED);
				this.argument[i] = value;
			}
		}
	}

	/** @param arguments
	 * @param length */
	@ReflectionHidden
	public ExecArgumentsMapX(final ExecArguments arguments, final int length) {

		ExecArgumentsMapX.COUNT++;
		this.argument = new BaseObject[length];
		for (int i = length - 1; i >= 0; --i) {
			this.argument[i] = arguments.baseGet(i, BaseObject.UNDEFINED);
		}
		assert arguments.baseHasKeysOwn() : "It's called MapX, ain't it?";
		for (final Iterator<String> iterator = Base.keys(arguments); iterator.hasNext();) {
			final String key = iterator.next();
			final BaseObject value = arguments.baseGet(key, BaseObject.UNDEFINED);
			this.map.baseDefine(key, value, BaseProperty.ATTRS_MASK_NEN);
		}
	}

	/** @param desc
	 * @param arguments
	 * @param length */
	@ReflectionHidden
	public ExecArgumentsMapX(final NamedToIndexMapper desc, final BaseArray arguments, final int length) {

		ExecArgumentsMapX.COUNT++;
		this.argument = new BaseObject[length];
		for (int i = length - 1; i >= 0; --i) {
			this.argument[i] = arguments.baseGet(i, BaseObject.UNDEFINED);
		}
		final String[] names = desc.names();
		assert names.length > 0 : "It's called MapX, ain't it?";
		for (int i = names.length - 1; i >= 0; --i) {
			final BaseObject value = this.argument[desc.nameIndex(i)];
			this.map.baseDefine(names[i], value, BaseProperty.ATTRS_MASK_NEN);
		}
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		return i < 0 || i >= this.argument.length
			? defaultValue
			: this.argument[i];
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.argument.length == 0
			? defaultValue
			: this.argument[0];
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetLast(final BaseObject defaultValue) {

		final int length = this.argument.length;
		return length == 0
			? defaultValue
			: this.argument[this.argument.length - 1];
	}

	@Override
	@ReflectionHidden
	public final BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {

		return this.map.baseGetOwnProperty(name) == null
			? super.baseGetOwnProperty(name)
			: this;
	}

	@Override
	@ReflectionHidden
	public final BaseProperty baseGetOwnProperty(final String name) {

		return this.map.baseGetOwnProperty(name) == null
			? super.baseGetOwnProperty(name)
			: this;
	}

	@Override
	@ReflectionHidden
	public final boolean baseHasKeysOwn() {

		return true;
	}

	@Override
	@ReflectionHidden
	public final Iterator<BaseObject> baseIterator() {

		final BaseObject[] value = this.argument;
		final int size = value.length;
		return new Iterator<BaseObject>() {

			private int index = 0;

			@Override
			public boolean hasNext() {

				return this.index < size;
			}

			@Override
			public BaseObject next() {

				return value[this.index++];
			}

			@Override
			public void remove() {

				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	@ReflectionHidden
	public final Iterator<String> baseKeysOwn() {

		return this.map.baseKeysOwn();
	}

	@Override
	@ReflectionHidden
	public final Iterator<? extends CharSequence> baseKeysOwnAll() {

		return this.map.baseKeysOwnAll();
	}

	@Override
	@ReflectionHidden
	public final Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {

		return this.map.baseKeysOwnPrimitive();
	}

	@Override
	@ReflectionHidden
	public final Object get(final int i) {

		return this.argument[i].baseValue();
	}

	@Override
	@ReflectionHidden
	public final boolean isEmpty() {

		return false;
	}

	@Override
	@ReflectionHidden
	public final Iterator<Object> iterator() {

		final BaseObject[] value = this.argument;
		final int size = value.length;
		return new Iterator<Object>() {

			private int index = 0;

			@Override
			public boolean hasNext() {

				return this.index < size;
			}

			@Override
			public Object next() {

				return value[this.index++].baseValue();
			}

			@Override
			public void remove() {

				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	@ReflectionHidden
	public final int length() {

		return this.argument.length;
	}

	@Override
	@ReflectionHidden
	public final short propertyAttributes(final CharSequence name) {

		return BaseProperty.ATTRS_MASK_NEN_NNK;
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {

		return this.map.baseGetOwnProperty(name).propertyGet(this.map, name);
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGet(final BaseObject instance, final String name) {

		return this.map.baseGetOwnProperty(name).propertyGet(this.map, name);
	}

	@Override
	@ReflectionHidden
	public final BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {

		return this.map.baseGetOwnProperty(name).propertyGet(this.map, name);
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {

		return this.map.vmPropertyRead(ctx, name.baseToString(), BaseObject.UNDEFINED, store);
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
}
