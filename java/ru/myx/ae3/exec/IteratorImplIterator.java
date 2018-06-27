package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
final class IteratorImplIterator extends BaseHostEmpty implements IteratorImpl {
	
	
	/**
	 * singular instance
	 */
	public static final IteratorImpl INSTANCE = new IteratorImplIterator();
	
	private IteratorImplIterator() {
		// prevent
	}
	
	@Override
	public final boolean next(final ExecProcess ctx, final BaseObject object, final String name) {
		
		
		final Iterator<?> iterator = (Iterator<?>) ctx.ri13IV;
		if (iterator.hasNext()) {
			ctx.contextSetMutableBinding(name, Base.forUnknown(iterator.next()), false);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		
		
		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
