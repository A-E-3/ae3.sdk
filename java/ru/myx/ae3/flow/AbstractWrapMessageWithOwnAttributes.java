package ru.myx.ae3.flow;

import ru.myx.ae3.base.BaseMessageEditable;
import ru.myx.ae3.base.BaseObject;

/**
 *
 * @author myx
 *
 * @param <T>
 * @param <V>
 */
public abstract class AbstractWrapMessageWithOwnAttributes<T extends AbstractWrapMessageWithOwnAttributes<?, ?>, V extends BaseMessageEditable<? extends V>>
		extends
			AbstractWrapMessage<T, V> {
	
	
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
	public AbstractWrapMessageWithOwnAttributes(final V message, final BaseObject attributes) {
		
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
