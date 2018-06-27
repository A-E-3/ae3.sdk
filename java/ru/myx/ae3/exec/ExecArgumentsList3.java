/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsList3 extends ExecArgumentsAbstractList {

	private static int COUNT = 0;

	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'List3' count", Format.Compact.toDecimal(ExecArgumentsList3.COUNT));
	}

	private final BaseObject object1;

	private final BaseObject object2;

	private final BaseObject object3;

	/** @param object1
	 * @param object2
	 * @param object3 */
	@ReflectionHidden
	public ExecArgumentsList3(final BaseObject object1, final BaseObject object2, final BaseObject object3) {
		
		assert object1 != null : "NULL java object!";
		assert object2 != null : "NULL java object!";
		assert object3 != null : "NULL java object!";
		ExecArgumentsList3.COUNT++;
		this.object1 = object1;
		this.object2 = object2;
		this.object3 = object3;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		switch (i) {
			case 0 :
				return this.object1;
			case 1 :
				return this.object2;
			case 2 :
				return this.object3;
			default :
				return defaultValue;
		}
	}

	@Override
	@ReflectionHidden
	public BaseObject baseGetFirst(final BaseObject defaultValue) {

		return this.object1;
	}

	@Override
	@ReflectionHidden
	public BaseObject baseGetLast(final BaseObject defaultValue) {

		return this.object3;
	}

	@Override
	@ReflectionHidden
	public final Object get(final int i) {

		switch (i) {
			case 0 :
				return this.object1.baseValue();
			case 1 :
				return this.object2.baseValue();
			case 2 :
				return this.object3.baseValue();
			default :
				return null;
		}
	}

	@Override
	@ReflectionHidden
	public final boolean isEmpty() {

		return false;
	}

	@Override
	@ReflectionHidden
	public final int length() {

		return 3;
	}

	@Override
	public BaseObject[] toArrayBase() {

		return new BaseObject[]{
				this.object1, this.object2, this.object3
		};
	}

	@Override
	@ReflectionHidden
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		switch (index) {
			case 0 :
				return store.execReturn(ctx, this.object1);
			case 1 :
				return store.execReturn(ctx, this.object2);
			case 2 :
				return store.execReturn(ctx, this.object3);
			default :
				return store.execReturn(ctx, defaultValue);
		}
	}
}
