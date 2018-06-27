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
public final class ExecArgumentsList2 extends ExecArgumentsAbstractList {

	private static int COUNT = 0;
	
	static final void statusFill(final StatusInfo data) {

		data.put("ARGS: 'List2' count", Format.Compact.toDecimal(ExecArgumentsList2.COUNT));
	}
	
	private final BaseObject object1;
	
	private final BaseObject object2;

	/** @param object1
	 * @param object2 */
	@ReflectionHidden
	public ExecArgumentsList2(final BaseObject object1, final BaseObject object2) {
		
		assert object1 != null : "NULL java object!";
		assert object2 != null : "NULL java object!";
		ExecArgumentsList2.COUNT++;
		this.object1 = object1;
		this.object2 = object2;
	}

	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {

		switch (i) {
			case 0 :
				return this.object1;
			case 1 :
				return this.object2;
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

		return this.object2;
	}
	
	@Override
	@ReflectionHidden
	public final Object get(final int i) {

		switch (i) {
			case 0 :
				return this.object1.baseValue();
			case 1 :
				return this.object2.baseValue();
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

		return 2;
	}
	
	@Override
	public BaseObject[] toArrayBase() {

		return new BaseObject[]{
				this.object1, this.object2
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
			default :
				return store.execReturn(ctx, defaultValue);
		}
	}
}
