package ru.myx.ae3.serve;

import ru.myx.ae3.base.BaseObject;

/**
 *
 * @author myx
 *
 * @param <T>
 * @param <V>
 */
public class AbstractWrapServeRequestWithOwnAttributes<T extends AbstractWrapServeRequestWithOwnAttributes<?, ?>, V extends ServeRequestEditable<? extends V>>
		extends
			AbstractWrapServeRequest<T, V> {
	
	/**
	 *
	 */
	protected BaseObject attributes;
	
	/**
	 *
	 * @param message
	 * @param attributes
	 *            null (to auto-derive parental attributes on demand), parental
	 *            (to share) or new/devired (explicit value)
	 */
	public AbstractWrapServeRequestWithOwnAttributes(final V message, final BaseObject attributes) {
		
		super(message);
		this.attributes = attributes;
	}
	
	@Override
	public BaseObject getAttributes() {
		
		
		{
			final BaseObject attributes = this.attributes;
			if (attributes != null) {
				return attributes;
			}
		}
		{
			final BaseObject attributes = this.wrapped.getAttributes();
			return this.attributes = BaseObject.createObject(attributes == BaseObject.UNDEFINED
				? null
				: attributes);
		}
	}
}
