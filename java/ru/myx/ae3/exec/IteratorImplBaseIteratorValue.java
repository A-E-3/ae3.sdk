package ru.myx.ae3.exec;

import java.util.Iterator;

import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 */
final class IteratorImplBaseIteratorValue extends BaseHostEmpty implements IteratorImpl {
	
	
	/**
	 * singular instance
	 */
	public static final IteratorImpl INSTANCE = new IteratorImplBaseIteratorValue();
	
	private IteratorImplBaseIteratorValue() {
		// prevent
	}
	
	@Override
	public final boolean next(final ExecProcess process, final BaseObject object, final String name) {
		
		
		assert object != null : "NULL java value!";
		final Iterator<?> iterator = (Iterator<?>) process.ri13IV;
		if (iterator.hasNext()) {
			final String key = (String) iterator.next();
			process.contextSetMutableBinding(name, object.baseGet(key, BaseObject.UNDEFINED), false);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		
		
		return "[object " + this.getClass().getSimpleName() + "]";
	}
}
