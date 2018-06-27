package ru.myx.ae3.serve;

import java.util.function.Function;

import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.AbstractWrapMessage;
import ru.myx.ae3.flow.Flow;
import ru.myx.ae3.reflect.ReflectionIgnore;

/** @author myx
 *
 * @param <T>
 * @param <V> */
@ReflectionIgnore
public abstract class AbstractWrapServeRequest<T extends AbstractWrapServeRequest<?, ?>, V extends ServeRequestEditable<? extends V>> extends AbstractWrapMessage<T, V>
		implements
			ServeRequestEditable<T> {

	/** @param request */
	protected AbstractWrapServeRequest(final V request) {
		
		super(request);
	}

	@Override
	public void abort() {

		this.wrapped.abort();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addParameter(final String name, final BaseObject value) {

		this.wrapped = this.wrapped.addParameter(name, value);
		return (T) this;
	}

	@Override
	public void done() {

		this.wrapped.done();
	}

	@Override
	public String[] getArguments() {

		return this.wrapped.getArguments();
	}

	@Override
	public Object getAttachment() {

		return this.wrapped.getAttachment();
	}

	@Override
	public String getLanguage() {

		return this.wrapped.getLanguage();
	}

	@Override
	public BaseObject getParameters() {

		return this.wrapped.getParameters();
	}

	@Override
	public String getParameterString() {

		return this.wrapped.getParameterString();
	}

	@Override
	public String getResourceIdentifier() {

		return this.wrapped.getResourceIdentifier();
	}

	@Override
	public String getResourcePrefix() {

		return this.wrapped.getResourcePrefix();
	}

	@Override
	public Class<?> getResponseClass() {

		return this.wrapped.getResponseClass();
	}

	@Override
	public Function<ReplyAnswer, Boolean> getResponseTarget() {

		return this.wrapped.getResponseTarget();
	}

	@Override
	public String getSessionID() {

		return this.wrapped.getSessionID();
	}

	@Override
	public BaseObject getSettings() {

		return this.wrapped.getSettings();
	}

	@Override
	public boolean getStillActual() {

		return this.wrapped.getStillActual();
	}

	@Override
	public String getUrl() {

		return this.wrapped.getUrl();
	}

	@Override
	public String getUrlBase() {

		return this.wrapped.getUrlBase();
	}

	@Override
	public String getUserID() {

		return this.wrapped.getUserID();
	}

	@Override
	public String getVerb() {

		return this.wrapped.getVerb();
	}

	@Override
	public String getVerbOriginal() {

		return this.wrapped.getVerbOriginal();
	}

	@Override
	public boolean hasArguments() {

		return this.wrapped.hasArguments();
	}

	@Override
	public boolean hasParameters() {

		return this.wrapped.hasParameters();
	}

	@Override
	public Object setAttachment(final Object attachment) {

		return this.wrapped.setAttachment(attachment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setLanguage(final String language) {

		this.wrapped = this.wrapped.setLanguage(language);
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
	public T setParameter(final String name, final BaseObject value) {

		this.wrapped = this.wrapped.setParameter(name, value);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setParameters(final BaseObject parameters) {

		this.wrapped = this.wrapped.setParameters(parameters);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setResourceIdentifier(final String resourceIdentifier) {

		this.wrapped = this.wrapped.setResourceIdentifier(resourceIdentifier);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setResourcePrefix(final String resourcePrefix) {

		this.wrapped = this.wrapped.setResourcePrefix(resourcePrefix);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setResponseClass(final Class<?> responseClass) {

		this.wrapped = this.wrapped.setResponseClass(responseClass);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setResponseTarget(final Function<ReplyAnswer, Boolean> responseTarget) {

		this.wrapped = this.wrapped.setResponseTarget(responseTarget);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSettings(final BaseObject settings) {

		this.wrapped = this.wrapped.setSettings(settings);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSourceAddress(final String sourceAddress) {

		this.wrapped = this.wrapped.setSourceAddress(sourceAddress);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setSourceAddressExact(final String sourceAddressExact) {

		this.wrapped = this.wrapped.setSourceAddressExact(sourceAddressExact);
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
	public T setTargetExact(final String targetExact) {

		this.wrapped = this.wrapped.setTargetExact(targetExact);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setUrl(final String url) {

		this.wrapped = this.wrapped.setUrl(url);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setUrlBase(final String urlBase) {

		this.wrapped = this.wrapped.setUrlBase(urlBase);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T setVerb(final String verb) {

		this.wrapped = this.wrapped.setVerb(verb);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T shiftRequested(final int characters, final boolean shiftBase) {

		this.wrapped = this.wrapped.shiftRequested(characters, shiftBase);
		return (T) this;
	}

	@Override
	public BinaryServeRequest<?> toBinary() throws Flow.FlowOperationException {

		return this.wrapped.toBinary();
	}

	@Override
	public CharacterServeRequest<?> toCharacter() throws Flow.FlowOperationException {

		return this.wrapped.toCharacter();
	}

	@Override
	public ReplyAnswer toSecureChannel() {

		return this.wrapped.toSecureChannel();
	}

	@Override
	public ReplyAnswer toUnSecureChannel() {

		return this.wrapped.toUnSecureChannel();
	}
}
