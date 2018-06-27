package ru.myx.ae3.answer;

import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.AbstractWrapMessage;
import ru.myx.ae3.flow.Flow;

/**
 *
 * @author myx
 *
 * @param <T>
 * @param <V>
 */
public abstract class AbstractWrapReplyAnswer<T extends AbstractWrapReplyAnswer<?, ?>, V extends ReplyAnswerEditable<? extends V>> extends AbstractWrapMessage<T, V>
		implements
			ReplyAnswerEditable<T> {
	
	/**
	 *
	 * @param message
	 * @param attributes
	 *            null (to auto-derive parental attributes on demand), parental
	 *            (to share) or new/devired (explicit value)
	 */
	AbstractWrapReplyAnswer(final V message) {

		super(message);
	}

	@Override
	public int getCode() {
		
		return this.wrapped.getCode();
	}

	@Override
	public long getExpirationDate() {
		
		return this.wrapped.getExpirationDate();
	}

	@Override
	public BaseObject getFlags() {
		
		return this.wrapped.getFlags();
	}

	@Override
	public BaseMessage getQuery() {
		
		return this.wrapped.getQuery();
	}

	@Override
	public String getSessionID() {
		
		return this.wrapped.getSessionID();
	}

	@Override
	public int getTimeToLiveSeconds() {
		
		return this.wrapped.getTimeToLiveSeconds();
	}

	@Override
	public String getUserID() {
		
		return this.wrapped.getUserID();
	}

	@Override
	public boolean isFinal() {
		
		return this.wrapped.isFinal();
	}

	@Override
	public boolean isPrivate() {
		
		return this.wrapped.isPrivate();
	}

	@Override
	public abstract T nextClone(final BaseMessage query);

	@SuppressWarnings("unchecked")
	@Override
	public T seal() {
		
		this.wrapped = this.wrapped.seal();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setCode(final int code) {
		
		this.wrapped = this.wrapped.setCode(code);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setExpirationDate(final long date) {
		
		this.wrapped = this.wrapped.setExpirationDate(date);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setFinal() {
		
		this.wrapped = this.wrapped.setFinal();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setFlags(final BaseObject flags) {
		
		this.wrapped = this.wrapped.setFlags(flags);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setLastModified(final long date) {
		
		this.wrapped = this.wrapped.setLastModified(date);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setNoCaching() {
		
		this.wrapped = this.wrapped.setNoCaching();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setPrivate() {
		
		this.wrapped = this.wrapped.setPrivate();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSourceAddress(final String address) {
		
		this.wrapped = this.wrapped.setSourceAddress(address);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSourceAddressExact(final String address) {
		
		this.wrapped = this.wrapped.setSourceAddressExact(address);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSubject(final String subject) {
		
		this.wrapped = this.wrapped.setSubject(subject);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setTarget(final String target) {
		
		this.wrapped = this.wrapped.setTarget(target);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setTargetExact(final String target) {
		
		this.wrapped = this.wrapped.setTargetExact(target);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setTimeToLiveSeconds(final int seconds) {
		
		this.wrapped = this.wrapped.setTimeToLiveSeconds(seconds);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setTitle(final String title) {
		
		this.wrapped = this.wrapped.setTitle(title);
		return (T) this;
	}

	@Override
	public BinaryReplyAnswer<?> toBinary() throws Flow.FlowOperationException {
		
		return this.wrapped.toBinary();
	}

	@Override
	public CharacterReplyAnswer<?> toCharacter() throws Flow.FlowOperationException {
		
		return this.wrapped.toCharacter();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T useFlags(final BaseObject flags) {
		
		this.wrapped = this.wrapped.useFlags(flags);
		return (T) this;
	}
}
