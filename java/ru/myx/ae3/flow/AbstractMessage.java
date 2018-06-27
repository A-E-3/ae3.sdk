package ru.myx.ae3.flow;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseMessageEditable;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.common.BodyAccessBinary;
import ru.myx.ae3.common.BodyAccessCharacter;
import ru.myx.ae3.reflect.ReflectionIgnore;
import ru.myx.ae3.report.AbstractEvent;

/** @author barachta
 *
 * myx - barachta 
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go
 *         to Window>Preferences>Java>Code Generation.
 * @param <T> */
@ReflectionIgnore
public abstract class AbstractMessage<T extends BaseMessageEditable<T>> extends AbstractEvent implements BaseMessageEditable<T> {
	
	/** interested in keys only */
	private static final BaseObject KEYS = new BaseNativeObject()//
			.putAppend("protocolName", 0)//
			.putAppend("protocolVariant", 0)//
			.putAppend("target", 0)//
			.putAppend("attributes", 0)//
	;

	/**
	 *
	 */
	protected AbstractMessage() {
		
		//
	}
	
	@Override
	public T addAttribute(final String name, final BaseObject value) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.addAttribute(name, value);
	}
	
	@Override
	public T addAttribute(final String name, final int value) {
		
		return this.addAttribute(name, Base.forInteger(value));
	}
	
	@Override
	public T addAttribute(final String name, final long value) {
		
		return this.addAttribute(name, Base.forLong(value));
	}
	
	@Override
	public T addAttribute(final String name, final Object value) {
		
		return this.addAttribute(name, Base.forUnknown(value));
	}
	
	@Override
	public T addAttribute(final String name, final String value) {
		
		return this.addAttribute(name, Base.forString(value));
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		return true;
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		return AbstractMessage.KEYS.baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return AbstractMessage.KEYS.baseKeysOwnPrimitive();
	}
	
	@Override
	public void cancel() {
		
		// default implementation is empty
	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BaseMessage) /* this.getClass() != obj.getClass() */) {
			return false;
		}
		final BaseMessage other = (BaseMessage) obj;
		
		if (this.isBinary()) {
			if (!other.isBinary()) {
				return false;
			}
			final BodyAccessBinary thisBinary = (BodyAccessBinary) this;
			final BodyAccessBinary otherBinary = (BodyAccessBinary) other;
			if (thisBinary.getBinaryContentLength() != otherBinary.getBinaryContentLength()) {
				return false;
			}
			if (!MessageDigest.isEqual(thisBinary.getBinaryMessageDigest().digest(), otherBinary.getBinaryMessageDigest().digest())) {
				return false;
			}
		} else //
		if (this.isCharacter()) {
			if (!other.isCharacter()) {
				return false;
			}
			final BodyAccessCharacter thisCharacter = (BodyAccessCharacter) this;
			final BodyAccessCharacter otherCharacter = (BodyAccessCharacter) other;
			if (thisCharacter.getCharacterContentLength() != otherCharacter.getCharacterContentLength()) {
				return false;
			}
			if (!thisCharacter.getText().equals(otherCharacter.getText())) {
				return false;
			}
		}
		
		final BaseObject thisAttributes = this.getAttributes();
		final BaseObject otherAttributes = other.getAttributes();
		return thisAttributes == null
			? otherAttributes == null
			: thisAttributes.equals(otherAttributes);
	}
	
	/** returns null */
	@Override
	public String getProtocolName() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getProtocolVariant() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getSourceAddress() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getSourceAddressExact() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getTarget() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getTargetAddress() {
		
		return null;
	}
	
	/** returns null */
	@Override
	public String getTargetExact() {
		
		return null;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = this.isBinary()
			? ((BodyAccessBinary) this).getBinaryMessageDigest().hashCode()
			: this.getObject().hashCode();
		final BaseObject attributes = this.getAttributes();
		result = prime * result + (attributes == null
			? 0
			: attributes.hashCode());
		return result;
	}
	
	@Override
	public T setAttribute(final String name, final BaseObject value) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setAttribute(name, value);
	}
	
	@Override
	public T setAttribute(final String name, final double value) {
		
		return this.setAttribute(name, Base.forDouble(value));
	}
	
	@Override
	public T setAttribute(final String name, final int value) {
		
		return this.setAttribute(name, Base.forInteger(value));
	}
	
	@Override
	public T setAttribute(final String name, final long value) {
		
		return this.setAttribute(name, Base.forLong(value));
	}
	
	@Override
	public T setAttribute(final String name, final Object value) {
		
		return this.setAttribute(name, Base.forUnknown(value));
	}
	
	@Override
	public T setAttribute(final String name, final String value) {
		
		return this.setAttribute(name, Base.forString(value));
	}
	
	@Override
	public T setAttributes(final BaseObject attributes) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setAttributes(attributes);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setContentDisposition(final String disposition) {
		
		{
			final String contentName = Base.getString(this.getAttributes(), "Content-Name", "").trim();
			if (contentName.length() > 0) {
				return this.setAttribute("Content-Disposition", disposition + "; filename=\"" + contentName + "\"");
			}
		}
		if (this.isFile()) {
			final String contentName = this.getFile().getName();
			return this.setAttribute("Content-Disposition", disposition + "; filename=\"" + contentName + "\"");
		}
		return (T) this;
	}
	
	@Override
	public final T setContentID(final String contentName) {
		
		return this.setAttribute("Content-Id", contentName);
	}
	
	@Override
	public final T setContentName(final String contentName) {
		
		return this.setAttribute("Content-Name", contentName);
	}
	
	@Override
	public final T setContentType(final String contentType) {
		
		return this.setAttribute("Content-Type", contentType);
	}
	
	@Override
	public final T setEncoding(final Charset charset) {
		
		return this.setAttribute("Character-Encoding", charset.name());
	}
	
	@Override
	public final T setEncoding(final String encoding) {
		
		return this.setAttribute("Character-Encoding", encoding);
	}
	
	@Override
	public final T setSessionID(final String sessionId) {
		
		return this.setAttribute("X-Session-Id", sessionId);
	}
	
	@Override
	public final T setUserID(final String userId) {
		
		return this.setAttribute("X-User-Id", userId);
	}
	
	/** @return */
	public abstract T toEditable();
	
	@Override
	public String toString() {
		
		return "[object " + this.getClass().getSimpleName() + "]";
	}
	
	@Override
	public T useAttributes(final BaseObject attributes) {
		
		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.useAttributes(attributes);
	}
}
