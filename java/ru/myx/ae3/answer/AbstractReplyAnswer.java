/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.answer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.AbstractMessage;

/** @author myx
 * @param <T> */
public abstract class AbstractReplyAnswer<T extends ReplyAnswerEditable<T>> extends AbstractMessage<T> implements ReplyAnswerEditable<T> {
	
	/**
	 *
	 */
	protected BaseMessage query;

	/** @param query */
	protected AbstractReplyAnswer(final BaseMessage query) {

		this.query = query;
	}

	@Override
	public abstract String getEventTypeId();

	@Override
	public long getExpirationDate() {
		
		return Base.getLong(this.getAttributes(), "Expires", 0);
	}

	@Override
	public final BaseObject getFlags() {
		
		return null;
	}

	/** returns getQuery().getProtocolName(). */
	@Override
	public String getProtocolName() {
		
		return this.getQuery().getProtocolName();
	}

	/** returns getQuery().getProtocolVariant(). */
	@Override
	public String getProtocolVariant() {
		
		return this.getQuery().getProtocolVariant();
	}

	@Override
	public BaseMessage getQuery() {
		
		return this.query;
	}

	@Override
	public final String getSessionID() {
		
		return Base.getString(this.getAttributes(), "X-Session-Id", null);
	}

	@Override
	public final String getSourceAddress() {
		
		return this.getSourceAddressExact();
	}

	@Override
	public final String getSourceAddressExact() {
		
		return Base.getString(this.getAttributes(), "Source", null);
	}

	@Override
	public final String getSubject() {
		
		return Base.getString(this.getAttributes(), "Subject", null);
	}

	@Override
	public final String getTarget() {
		
		return this.getTargetExact();
	}

	@Override
	public String getTargetAddress() {
		
		return this.query.getSourceAddressExact();
	}

	@Override
	public final String getTargetExact() {
		
		return Base.getString(this.getAttributes(), "Host", null);
	}

	/** throws UnsupportedOperationException. */
	@Override
	public abstract ReplyAnswer nextClone(final BaseMessage query);

	@Override
	public T seal() {
		
		assert false;
		return null;
	}

	@Override
	public T setCode(final int code) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setCode(code);
	}

	@Override
	public final T setExpirationDate(final long date) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setExpirationDate(date);
	}

	@Override
	public T setFinal() {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setFinal();
	}

	@Override
	public final T setFlags(final BaseObject flags) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setFlags(flags);
	}

	@Override
	public final T setLastModified(final long date) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setLastModified(date);
	}

	@Override
	public final T setNoCaching() {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setNoCaching();
	}

	@Override
	public final T setPrivate() {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setPrivate();
	}

	@Override
	public final T setSourceAddress(final String address) {
		
		return this.setSourceAddressExact(address);
	}

	@Override
	public final T setSourceAddressExact(final String address) {
		
		return this.setAttribute("Source", address);
	}

	@Override
	public final T setSubject(final String subject) {
		
		return this.setAttribute("Subject", subject);
	}

	@Override
	public final T setTarget(final String address) {
		
		return this.setTargetExact(address);
	}

	@Override
	public final T setTargetExact(final String address) {
		
		return this.setAttribute("Host", address);
	}

	@Override
	public final T setTimeToLiveSeconds(final int seconds) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setTimeToLiveSeconds(seconds);
	}

	@Override
	public final T setTitle(final String title) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setTitle(title);
	}

	@Override
	public String toString() {
		
		return "[reply " + this.getClass().getSimpleName() + "(code=" + this.getCode() + ", " + this.getTitle() + ")]";
	}

	@Override
	public final T useFlags(final BaseObject flags) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.useFlags(flags);
	}
}
