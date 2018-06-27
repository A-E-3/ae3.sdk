/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.answer;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.AbstractMessageMutable;
import ru.myx.ae3.reflect.ReflectionIgnore;

/** @author myx
 * @param <T> */
@ReflectionIgnore
public abstract class AbstractReplyAnswerMutable<T extends ReplyAnswerEditable<T>> extends AbstractMessageMutable<T> implements ReplyAnswerEditable<T> {
	
	/**
	 *
	 */
	protected int code;

	BaseObject flags = null;

	/**
	 *
	 */
	protected byte isFinal = -1;

	/**
	 *
	 */
	protected byte isPrivate = -1;

	/**
	 *
	 */
	protected String eventTypeId;

	/**
	 *
	 */
	protected BaseMessage query;

	/** @param eventTypeId
	 * @param query
	 * @param attributes */
	protected AbstractReplyAnswerMutable(final String eventTypeId, final BaseMessage query, final BaseObject attributes) {

		super(attributes);
		this.eventTypeId = eventTypeId;
		this.query = query;
		this.code = Reply.CD_OK;
	}

	/** @param owner
	 * @param query
	 * @param code
	 * @param attributes */
	protected AbstractReplyAnswerMutable(final String owner, final BaseMessage query, final int code, final BaseObject attributes) {

		super(attributes);
		this.eventTypeId = owner;
		this.query = query;
		this.code = code;
	}

	@Override
	public int getCode() {
		
		return this.code;
	}

	@Override
	public String getEventTypeId() {
		
		return this.eventTypeId;
	}

	@Override
	public long getExpirationDate() {
		
		return Base.getLong(this.getAttributes(), "Expires", 0);
	}

	@Override
	public final BaseObject getFlags() {
		
		return this.flags;
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

	@Override
	public boolean isFinal() {
		
		return this.isFinal == -1
			/** getAttributes is a final method and we know it is using 'this.attributes'. */
			? (this.isFinal = Base.getBoolean(this.getAttributes(), "Final", false)
				? (byte) 1
				: (byte) 0) != 0
			: this.isFinal != 0;
	}

	@Override
	public final boolean isPrivate() {
		
		return this.isPrivate == -1
			/** getAttributes is a final method and we know it is using 'this.attributes'. */
			? (this.isPrivate = Base.getBoolean(this.getAttributes(), "Private", false)
				? (byte) 1
				: (byte) 0) != 0
			: this.isPrivate != 0;
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
	@SuppressWarnings("unchecked")
	public T setCode(final int code) {
		
		this.code = code;
		return (T) this;
	}

	@Override
	public final T setExpirationDate(final long date) {
		
		if (date <= 0) {
			return this.setNoCaching();
		}
		final int secs = (int) ((date - Engine.fastTime()) / 1000);
		if (secs >= 0 && secs < 60 * 60) {
			return this.setAttribute("maxage", secs).addAttribute("Cache-Control", "max-age=" + secs + "; s-maxage=" + secs).setAttribute("Expires", Base.forDateMillis(date));
		}
		return this.setAttribute("Expires", Base.forDateMillis(date));
	}

	@Override
	public T setFinal() {
		
		this.isFinal = 1;
		return this.setAttribute("Final", "true");
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setFlags(final BaseObject flags) {
		
		if (flags != null) {
			if (this.flags == null) {
				this.flags = new BaseNativeObject();
			}
			this.flags.baseDefineImportAllEnumerable(flags);
		}
		return (T) this;
	}

	@Override
	public final T setLastModified(final long date) {
		
		return this.setAttribute("Last-Modified", date <= 0L
			? BaseObject.NULL
			: Base.forDateMillis(date));
	}

	@Override
	public final T setNoCaching() {
		
		return this//
				.setAttribute("Cache-Control", "no-cache, max-age=0")//
				// "private, no-cache, max-age=0, s-maxage=0, must-revalidate"
				.setAttribute("Pragma", "no-cache")//
				.setAttribute("Expires", Base.forDateMillis(31536000000L))//
				.setAttribute("maxage", 0)//
				.setLastModified(0L);
	}

	@Override
	public final T setPrivate() {
		
		this.isPrivate = 1;
		return this.setAttribute("Private", "true");
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
		
		return seconds <= 0
			? this.setNoCaching()
			: this.setAttribute("maxage", seconds).addAttribute("Cache-Control", "max-age=" + seconds + "; s-maxage=" + seconds)
					.setAttribute("Expires", Base.forDateMillis(Engine.fastTime() + seconds * 1000L));
	}

	@Override
	public final T setTitle(final String title) {
		
		return this.setAttribute("Title", title);
	}

	@Override
	public String toString() {
		
		return "[reply " + this.getClass().getSimpleName() + "(code=" + this.getCode() + ", " + this.getTitle() + ")]";
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T useFlags(final BaseObject flags) {
		
		this.flags = flags;
		return (T) this;
	}
}
