package ru.myx.ae3.answer;

import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 *
 * @param <T>
 * @param <V>
 */
public class AbstractWrapReplyAnswerWithOwnAttributes<T extends AbstractWrapReplyAnswerWithOwnAttributes<?, ?>, V extends ReplyAnswerEditable<? extends V>>
		extends
			AbstractWrapReplyAnswer<T, V> {
	
	
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
	AbstractWrapReplyAnswerWithOwnAttributes(final V message, final BaseObject attributes) {
		
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
	
	@SuppressWarnings("unchecked")
	@Override
	public T nextClone(final BaseMessage query) {
		
		
		return (T) new AbstractWrapReplyAnswerWithOwnAttributes<T, V>((V) this.wrapped.nextClone(query), null);
	}
}
