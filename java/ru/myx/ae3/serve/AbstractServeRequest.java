/*
 * Created on 24.03.2006
 */
package ru.myx.ae3.serve;

import java.util.Iterator;
import java.util.function.Function;

import ru.myx.ae3.answer.Reply;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.flow.AbstractMessage;
import ru.myx.ae3.reflect.ReflectionIgnore;

/** @author myx
 * @param <T> */
@ReflectionIgnore
public abstract class AbstractServeRequest<T extends ServeRequestEditable<T>> extends AbstractMessage<T> implements ServeRequestEditable<T> {

	static final Function<ReplyAnswer, Boolean> NUL_RESPONSE_TARGET = new Function<ReplyAnswer, Boolean>() {
		
		@Override
		public Boolean apply(final ReplyAnswer arg) {
			
			return null;
		}
	};

	/** interested in keys only */
	private static final BaseObject KEYS = new BaseNativeObject()//
			.putAppend("sourceAddress", 0)//
			.putAppend("verb", 0)//
			.putAppend("attributes", 0)//
			.putAppend("parameters", 0)//
			.putAppend("target", 0)//
			.putAppend("resourcePrefix", 0)//
			.putAppend("resourceIdentifier", 0)//
			.putAppend("urlBase", 0)//
			.putAppend("url", 0)//
	;
	
	private BaseObject parameters;

	private String[] arguments = null;

	private String targetExact;

	/**
	 *
	 */
	protected String sourceAddress;

	/**
	 *
	 */
	protected String sourceAddressExact;

	/**
	 *
	 */
	protected String targetAddress;

	String url;

	String urlBase;

	/**
	 *
	 */
	protected String sessionID;

	/**
	 *
	 */
	protected String userID;

	Class<?> responseClass = Object.class;

	Function<ReplyAnswer, Boolean> responseTarget = AbstractServeRequest.NUL_RESPONSE_TARGET;

	/**
	 *
	 */
	protected String resourceIdentifier = "/";

	/**
	 *
	 */
	protected String resourcePrefix = "";

	String language;

	Object attachment;

	BaseObject settings;

	/** @param owner
	 * @param verb */
	@SuppressWarnings("javadoc")
	protected AbstractServeRequest() {
		
		//
	}

	@Override
	public void abort() {

		// empty
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T addParameter(final String name, final BaseObject value) {

		if (value == null) {
			throw new NullPointerException("Parameter value cannot be null, name=" + name);
		}
		if (this.parameters == null) {
			this.parameters = new BaseNativeObject();
			this.parameters.baseDefine(name, value);
		} else {
			final BaseObject o = this.parameters.baseGet(name, BaseObject.UNDEFINED);
			assert o != null : "NULL java value!";
			if (o == BaseObject.UNDEFINED) {
				this.parameters.baseDefine(name, value);
			} else {
				if (o instanceof MultipleList) {
					final MultipleList list = (MultipleList) o;
					if (!list.contains(value)) {
						list.add(value);
					}
				} else {
					if (!o.equals(value)) {
						final MultipleList list = new MultipleList();
						list.add(o);
						list.add(value);
						this.parameters.baseDefine(name, list);
					}
				}
			}
		}
		return (T) this;
	}

	@Override
	public void baseClear() {

		if (this.parameters != null) {
			this.parameters.baseClear();
		}
	}

	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {

		if (value == null) {
			throw new NullPointerException("Parameter value cannot be null, name=" + name);
		}
		if (this.parameters.baseIsPrimitive()) {
			this.parameters = new BaseNativeObject();
		}
		return this.parameters.baseDefine(name, value, attributes);
	}

	@Override
	public boolean baseDelete(final String name) {

		if (this.parameters != null) {
			return this.parameters.baseDelete(name);
		}
		return false;
	}

	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {

		if (this.parameters != null) {
			/** ???? is it really gonna work? instance object will be kinda fucked isn't it? */
			return this.parameters.baseGetOwnProperty(name);
		}
		return null;
	}

	@Override
	public BaseProperty baseGetOwnProperty(final String name) {

		if (this.parameters != null) {
			/** ???? is it really gonna work? instance object will be kinda fucked isn't it? */
			return this.parameters.baseGetOwnProperty(name);
		}
		return null;
	}

	@Override
	public boolean baseHasKeysOwn() {

		return true;
	}

	@Override
	public boolean baseIsExtensible() {

		return true;
	}

	@Override
	public Iterator<String> baseKeysOwn() {

		return AbstractServeRequest.KEYS.baseKeysOwn();
	}

	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {

		return AbstractServeRequest.KEYS.baseKeysOwnPrimitive();
	}

	@Override
	public void done() {

		// empty
	}

	@Override
	public final String[] getArguments() {

		return this.arguments;
	}

	@Override
	public Object getAttachment() {

		return this.attachment;
	}

	@Override
	public abstract String getEventTypeId();

	@Override
	public final String getLanguage() {

		return this.language;
	}

	@Override
	public final BaseObject getParameters() {

		return this.parameters == null
			? this.parameters = new BaseNativeObject()
			: this.parameters;
	}

	@Override
	public String getParameterString() {

		return null;
	}

	@Override
	public final String getResourceIdentifier() {

		return this.resourceIdentifier;
	}

	@Override
	public final String getResourcePrefix() {

		return this.resourcePrefix;
	}

	/** Returns Object.class by default. */
	@Override
	public final Class<?> getResponseClass() {

		return this.responseClass;
	}

	@Override
	public final Function<ReplyAnswer, Boolean> getResponseTarget() {

		return this.responseTarget;
	}

	/** Tries to parse an attribute "Session-Id". */
	@Override
	public String getSessionID() {

		return this.sessionID != null
			? this.sessionID
			: (this.sessionID = Base.getString(this.getAttributes(), "Session-Id", null));
	}

	@Override
	public BaseObject getSettings() {

		return this.settings == null
			? this.settings = BaseObject.UNDEFINED
			: this.settings;
	}

	@Override
	public final String getSourceAddress() {

		return this.sourceAddress == null
			? this.sourceAddressExact
			: this.sourceAddress;
	}

	@Override
	public final String getSourceAddressExact() {

		return this.sourceAddressExact == null
			? this.sourceAddress
			: this.sourceAddressExact;
	}

	@Override
	public boolean getStillActual() {

		return true;
	}

	/** Returns getTargetExact() by default. */
	@Override
	public String getTarget() {

		return this.getTargetExact();
	}

	@Override
	public final String getTargetAddress() {

		return this.targetAddress;
	}

	/** Returns null by default. */
	@Override
	public final String getTargetExact() {

		return this.targetExact;
	}

	/** Returns same result as Ask.getDefaultTitleForVerb(getVerb()) by default. */
	@Override
	public String getTitle() {

		return Request.getDefaultTitleForVerb(this.getVerb());
	}

	/** Returns null by default. */
	@Override
	public final String getUrl() {

		return this.url;
	}

	/** Returns null by default. */
	@Override
	public final String getUrlBase() {

		return this.urlBase;
	}

	/** Tries to parse an attribute "User-Id". */
	@Override
	public String getUserID() {

		return this.userID != null
			? this.userID
			: (this.userID = Base.getString(this.getAttributes(), "User-Id", null));
	}

	@Override
	public abstract String getVerb();

	@Override
	public final String getVerbOriginal() {

		return this.getVerb();
	}

	@Override
	public final boolean hasArguments() {

		return this.arguments != null && this.arguments.length > 0;
	}

	@Override
	public final boolean hasParameters() {

		return this.parameters != null && this.parameters.baseHasKeysOwn();
	}

	@Override
	public Object setAttachment(final Object attachment) {

		try {
			return this.attachment;
		} finally {
			this.attachment = attachment;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setLanguage(final String language) {

		this.language = language;
		return (T) this;
	}

	@Override
	public final T setLastModified(final long date) {

		return this.setAttribute("Last-Modified", date <= 0L
			? null
			: Base.forDateMillis(date));
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setParameter(final String name, final BaseObject value) {

		if (value == null) {
			throw new NullPointerException("Parameter value cannot be null, name=" + name);
		}
		if (this.parameters.baseIsPrimitive()) {
			this.parameters = new BaseNativeObject();
		}
		this.parameters.baseDefine(name, value);
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setParameters(final BaseObject parameters) {

		if (parameters != null) {
			if (this.parameters == null || this.parameters.baseIsPrimitive()) {
				this.parameters = new BaseNativeObject();
			}
			this.parameters.baseDefineImportAllEnumerable(parameters);
		}
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setResourceIdentifier(final String resourceIdentifier) {

		/** actually QueryHandler does that intentionally, sorry */
		// assert resourceIdentifier.startsWith( "/" ) :
		// "Paths should start with '/', identifier=" + resourceIdentifier;
		this.resourceIdentifier = resourceIdentifier;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setResourcePrefix(final String resourcePrefix) {

		this.resourcePrefix = resourcePrefix;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setResponseClass(final Class<?> responseClass) {

		this.responseClass = responseClass;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setResponseTarget(final Function<ReplyAnswer, Boolean> responseTarget) {

		this.responseTarget = responseTarget;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T setSettings(final BaseObject settings) {

		this.settings = settings;
		return (T) this;
	}

	/** @return same request */
	@Override
	@SuppressWarnings("unchecked")
	public final T setSourceAddress(final String sourceAddress) {

		this.sourceAddress = sourceAddress;
		return (T) this;
	}

	/** @param sourceAddressExact
	 * @return same request */
	@Override
	@SuppressWarnings("unchecked")
	public final T setSourceAddressExact(final String sourceAddressExact) {

		this.sourceAddressExact = sourceAddressExact;
		return (T) this;
	}

	/** Does setTargetExact() by default. */
	@Override
	public T setTarget(final String target) {

		return this.setTargetExact(target);
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setTargetExact(final String targetExact) {

		this.targetExact = targetExact;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setUrl(final String url) {

		this.url = url;
		return (T) this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setUrlBase(final String urlBase) {

		this.urlBase = urlBase;
		return (T) this;
	}

	@Override
	public final T setVerb(final String verb) {

		final T editable = this.toEditable();
		assert editable != null && editable != this;
		return editable.setVerb(verb);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T shiftRequested(final int characters, final boolean shiftBase) {

		final String identifier = this.getResourceIdentifier();
		final String cutRequest = identifier.substring(0, characters);
		if (shiftBase) {
			this.setTargetExact(this.getTargetExact() + cutRequest);
			this.setUrlBase(this.getUrlBase() + cutRequest);
		} else {
			this.setResourcePrefix(this.getResourcePrefix() + cutRequest);
		}
		this.setResourceIdentifier(identifier.substring(characters));
		return (T) this;
	}

	/** Returns a message with DENIED code and a text about an error switching to secure mode. */
	@Override
	public ReplyAnswer toSecureChannel() {

		return Reply.string("AR:" + this.getEventTypeId(), this, "Access cannot be granted. Secure interface is unavailable but required!").setCode(Reply.CD_DENIED);
	}

	@Override
	public String toString() {

		return "[request " + this.getClass().getSimpleName() + "(" + this.getVerb() + ", " + this.getResourcePrefix() + this.getResourceIdentifier() + ")]";
	}

	/** Returns a message with DENIED code and a text about an error switching to insecure mode. */
	@Override
	public ReplyAnswer toUnSecureChannel() {

		return Reply.string("AR:" + this.getEventTypeId(), this, "Access cannot be granted. UnSecure interface is unavailable but required!").setCode(Reply.CD_DENIED);
	}

	/** @param arguments
	 * @return */
	protected final ServeRequest useArguments(final String[] arguments) {

		this.arguments = arguments;
		return this;
	}

	/** NULL value is explicitly allowed
	 *
	 * @param parameters
	 * @return */
	protected final ServeRequest useParameters(final BaseObject parameters) {

		this.parameters = parameters;
		return this;
	}
}
