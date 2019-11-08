/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;
import ru.myx.util.IteratorSingle;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsList1 extends ExecArgumentsAbstractList {

	private static int COUNT = 0;

	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'List1' count", Format.Compact.toDecimal(ExecArgumentsList1.COUNT));
	}

	private final BaseObject object;

	/** @param object */
	@ReflectionHidden
	public ExecArgumentsList1(final BaseObject object) {

		assert object != null : "NULL java object!";
		ExecArgumentsList1.COUNT++;
		this.object = object;
	}

	@Override
	@ReflectionHidden
	public boolean baseContains(final BaseObject value) {

		return this.object == value || this.object.equals(value);
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		return i == 0
			? this.object
			: defaultValue;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.object;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGetLast(final BaseObject defaultValue) {

		return this.object;
	}

	@Override
	@ReflectionHidden
	public final Iterator<? extends BaseObject> baseIterator() {

		return new IteratorSingle<>(this.object);
	}

	@Override
	@ReflectionHidden
	public final Object get(final int i) {

		return i == 0
			? this.object.baseValue()
			: null;
	}

	@Override
	@ReflectionHidden
	public final boolean isEmpty() {

		return false;
	}

	@Override
	@ReflectionHidden
	public final Iterator<Object> iterator() {

		return new IteratorSingle<>(this.object.baseValue());
	}

	@Override
	@ReflectionHidden
	public final int length() {

		return 1;
	}

	@Override
	public BaseObject[] toArrayBase() {

		return new BaseObject[]{
				this.object
		};
	}

	@Override
	@ReflectionHidden
	public final ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return store.execReturn(
				ctx,
				index == 0
					? this.object
					: defaultValue);
	}
}
