/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsListX extends ExecArgumentsAbstractList {

	private static int COUNT = 0;

	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'ListX' count", Format.Compact.toDecimal(ExecArgumentsListX.COUNT));
	}

	private final BaseObject[] arguments;

	/** Clones into array
	 *
	 *
	 * @param arguments */
	@ReflectionHidden
	public ExecArgumentsListX(final BaseArray arguments) {

		assert arguments != null : "No arguments given to copy from";
		final int length = arguments.length();
		this.arguments = new BaseObject[length];
		for (int i = length - 1; i >= 0; --i) {
			this.arguments[i] = arguments.baseGet(i, BaseObject.UNDEFINED);
		}
		ExecArgumentsListX.COUNT++;
	}

	/** Clones into array
	 *
	 *
	 * @param arguments
	 * @param length
	 * @param shift */
	@ReflectionHidden
	public ExecArgumentsListX(final BaseArray arguments, final int length, final int shift) {

		assert arguments != null : "No arguments given to copy from";
		this.arguments = new BaseObject[length];
		for (int i = length - 1; i >= 0; --i) {
			this.arguments[i] = arguments.baseGet(i + shift, BaseObject.UNDEFINED);
		}
		ExecArgumentsListX.COUNT++;
	}

	/** Wraps and uses an array
	 *
	 * @param arguments */
	@ReflectionHidden
	public ExecArgumentsListX(final BaseObject[] arguments) {

		assert arguments != null : "No arguments given to copy from";
		this.arguments = arguments;
		ExecArgumentsListX.COUNT++;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		return i < 0 || i >= this.arguments.length
			? defaultValue
			: this.arguments[i];
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.arguments.length == 0
			? defaultValue
			: this.arguments[0];
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetLast(final BaseObject defaultValue) {

		final int length = this.arguments.length;
		return length == 0
			? defaultValue
			: this.arguments[length - 1];
	}

	@Override
	@ReflectionHidden
	public final Iterator<BaseObject> baseIterator() {

		final BaseObject[] value = this.arguments;
		final int size = value.length;
		return new Iterator<>() {

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
	public final Object get(final int i) {

		return this.arguments[i].baseValue();
	}

	@Override
	@ReflectionHidden
	public final boolean isEmpty() {

		return false;
	}

	@Override
	@ReflectionHidden
	public final Iterator<Object> iterator() {

		final BaseObject[] value = this.arguments;
		final int size = value.length;
		return new Iterator<>() {

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

		return this.arguments.length;
	}

	@Override
	public BaseObject[] toArrayBase() {

		return this.arguments;
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return store.execReturn(//
				ctx,
				index < 0 || index >= this.arguments.length
					? defaultValue
					: this.arguments[index]);
	}
}
