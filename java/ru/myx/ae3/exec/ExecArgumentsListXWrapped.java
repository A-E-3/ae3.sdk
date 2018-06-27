/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseArrayAdvanced;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsListXWrapped extends ExecArgumentsAbstractList {

	private static int COUNT = 0;

	/** @param list
	 * @return */
	public static final ExecArguments createArguments(final BaseArray list) {

		return list != null && !list.isEmpty()
			? list instanceof ExecArguments
				? (ExecArguments) list
				: new ExecArgumentsListXWrapped(list)
			: ExecArgumentsEmpty.INSTANCE;
	}

	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'ListX' count", Format.Compact.toDecimal(ExecArgumentsListXWrapped.COUNT));
	}

	private final BaseArray arguments;

	/** @param arguments */
	@ReflectionHidden
	public ExecArgumentsListXWrapped(final BaseArray arguments) {

		this.arguments = arguments;
		ExecArgumentsListXWrapped.COUNT++;
	}

	@Override
	@ReflectionHidden
	public final BaseArrayAdvanced<?> baseArraySlice(final int start) {

		return this.arguments.baseArraySlice(start);
	}

	@Override
	@ReflectionHidden
	public final BaseArrayAdvanced<?> baseArraySlice(final int start, final int end) {

		return this.arguments.baseArraySlice(start, end);
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		return this.arguments.baseGet(i, defaultValue);
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.arguments.baseGetFirst(defaultValue);
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetLast(final BaseObject defaultValue) {

		return this.arguments.baseGetLast(defaultValue);
	}

	@Override
	@ReflectionHidden
	public final Iterator<? extends BaseObject> baseIterator() {

		return this.arguments.baseIterator();
	}

	@Override
	@ReflectionHidden
	public final Object get(final int i) {

		return this.arguments.get(i);
	}

	@Override
	@ReflectionHidden
	public final boolean isEmpty() {

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	@ReflectionHidden
	public final Iterator<Object> iterator() {

		return (Iterator<Object>) this.arguments.iterator();
	}

	@Override
	@ReflectionHidden
	public final int length() {

		return this.arguments.length();
	}

	@Override
	public BaseObject[] toArrayBase() {

		return this.arguments.toArrayBase();
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return this.arguments.vmPropertyRead(ctx, index, originalIfKnown, defaultValue, store);
	}
}
