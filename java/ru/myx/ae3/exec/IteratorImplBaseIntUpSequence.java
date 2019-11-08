package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;

/** @author myx */
final class IteratorImplBaseIntUpSequence extends BaseHostEmpty implements IteratorImpl {

	/** singular instance */
	public static final IteratorImpl INSTANCE = new IteratorImplBaseIntUpSequence();

	private IteratorImplBaseIntUpSequence() {

		// prevent
	}

	@Override
	public final boolean next(final ExecProcess ctx, final BaseObject object, final String name) {

		final BasePrimitiveNumber length = (BasePrimitiveNumber) object;
		final int index = ((BasePrimitiveNumber) ctx.ri13IV).baseToJavaInteger() + 1; // faster???
		if (index < length.intValue()) {
			final BasePrimitiveNumber next = Base.forInteger(index);
			ctx.contextSetMutableBinding(name, next, false);
			ctx.ri13IV = next;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
