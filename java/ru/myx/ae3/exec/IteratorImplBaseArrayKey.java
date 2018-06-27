package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;

/**
 * @author myx
 * 
 */
final class IteratorImplBaseArrayKey extends BaseHostEmpty implements IteratorImpl {
	
	
	/**
	 * singular instance
	 */
	public static final IteratorImpl INSTANCE = new IteratorImplBaseArrayKey();
	
	private IteratorImplBaseArrayKey() {
		// prevent
	}
	
	@Override
	public final boolean next(final ExecProcess ctx, final BaseObject object, final String name) {
		
		
		final BaseArray array = (BaseArray) object;
		BasePrimitiveNumber baseIndex = (BasePrimitiveNumber) ctx.ri13IV;
		int index = baseIndex.intValue();
		while (index < array.length()) {
			final BaseObject element = array.baseGet(index, null);
			/**
			 * only existing ones
			 */
			if (element == null) {
				++index;
				baseIndex = null;
				continue;
			}
			ctx.contextSetMutableBinding(
					name, //
					baseIndex == null
						? Base.forInteger(index)
						: baseIndex,
					false);
			ctx.ri13IV = Base.forInteger(index + 1);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		
		
		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
