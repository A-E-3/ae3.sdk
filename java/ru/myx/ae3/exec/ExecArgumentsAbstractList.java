package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;

/** @author myx */
public abstract class ExecArgumentsAbstractList extends ExecArgumentsAbstract {

	@Override
	public abstract BaseObject baseGet(final int i, final BaseObject defaultValue);

	@Override
	public final boolean baseHasKeysOwn() {

		return false;
	}
	@Override
	public final Iterator<String> baseKeysOwn() {

		return BaseObject.ITERATOR_EMPTY;
	}

	@Override
	public final Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {

		return BaseObject.ITERATOR_EMPTY_PRIMITIVE;
	}

	@Override
	public Object get(final int i) {

		return this.baseGet(i, BaseObject.UNDEFINED).baseValue();
	}

	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {

		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
}
