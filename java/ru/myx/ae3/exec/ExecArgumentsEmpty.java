/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Collections;
import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseArrayAdvanced;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
@ReflectionDisable
public final class ExecArgumentsEmpty extends ExecArgumentsAbstract {
	
	/** Static instance for NULL callees */
	@ReflectionHidden
	public static final ExecArgumentsEmpty INSTANCE = new ExecArgumentsEmpty();
	
	private static int COUNT = 0;
	
	static final void statusFill(final StatusInfo data) {
		
		data.put("ARGS: 'Empty' count", Format.Compact.toDecimal(ExecArgumentsEmpty.COUNT));
	}
	
	private ExecArgumentsEmpty() {
		ExecArgumentsEmpty.COUNT++;
	}
	
	@Override
	@ReflectionHidden
	public BaseArrayAdvanced<?> baseArraySlice(final int start) {
		
		return BaseObject.createArray(0);
	}
	
	@Override
	@ReflectionHidden
	public BaseArrayAdvanced<?> baseArraySlice(final int start, final int end) {
		
		return BaseObject.createArray(0);
	}
	
	@Override
	@ReflectionHidden
	public boolean baseContains(final BaseObject value) {
		
		return false;
	}
	
	@Override
	@ReflectionHidden
	public final BaseObject baseGet(final int i, final BaseObject defaultValue) {
		
		return defaultValue;
	}
	
	@Override
	@ReflectionHidden
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		return defaultValue;
	}
	
	@Override
	@ReflectionHidden
	public BaseObject baseGetLast(final BaseObject defaultValue) {
		
		return defaultValue;
	}
	
	@Override
	@ReflectionHidden
	public boolean baseHasKeysOwn() {
		
		return false;
	}
	
	@Override
	@ReflectionHidden
	public Iterator<? extends BaseObject> baseIterator() {
		
		return BaseArray.ITERATOR_EMPTY;
	}
	
	@Override
	@ReflectionHidden
	public Iterator<String> baseKeysOwn() {
		
		return BaseObject.ITERATOR_EMPTY;
	}
	
	@Override
	@ReflectionHidden
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return BaseObject.ITERATOR_EMPTY_PRIMITIVE;
	}
	
	@Override
	@ReflectionHidden
	public final Object get(final int i) {
		
		return null;
	}
	
	@Override
	@ReflectionHidden
	public boolean isEmpty() {
		
		return true;
	}
	
	@Override
	@ReflectionHidden
	public Iterator<Object> iterator() {
		
		return Collections.emptyIterator();
	}
	
	@Override
	@ReflectionHidden
	public final int length() {
		
		return 0;
	}
	
	@Override
	public BaseObject[] toArrayBase() {
		
		return Base.ZERO_BASE_OBJECT_ARRAY;
	}
	
	@Override
	@ReflectionHidden
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		return store.execReturn(ctx, defaultValue);
	}
}
