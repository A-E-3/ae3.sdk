package ru.myx.ae3.flow;

import java.io.File;
import java.nio.charset.Charset;

import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseMessageEditable;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.report.AbstractWrapEvent;

/** Sets attributes and properties of an original message
 *
 * The class in intentionally not marked <b>abstract</b> to ensure all methods are implemented
 *
 * @author myx
 *
 * @param <T>
 * @param <V> */
public abstract class AbstractWrapMessage<T extends AbstractWrapMessage<?, ?>, V extends BaseMessageEditable<? extends V>> extends AbstractWrapEvent<V>
		implements
			BaseMessageEditable<T> {

	/** @param message */
	protected AbstractWrapMessage(final V message) {
		
		super(message);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T addAttribute(final String name, final BaseObject value) {
		
		this.wrapped = this.wrapped.addAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T addAttribute(final String name, final int value) {
		
		this.wrapped = this.wrapped.addAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T addAttribute(final String name, final long value) {
		
		this.wrapped = this.wrapped.addAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T addAttribute(final String name, final Object value) {
		
		this.wrapped = this.wrapped.addAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T addAttribute(final String name, final String value) {
		
		this.wrapped = this.wrapped.addAttribute(name, value);
		return (T) this;
	}
	
	@Override
	public void cancel() {
		
		this.wrapped.cancel();
	}
	
	@Override
	public BaseObject getAttributes() {
		
		return this.wrapped.getAttributes();
	}
	
	@Override
	public File getFile() {
		
		return this.wrapped.getFile();
	}
	
	@Override
	public Object getObject() {
		
		return this.wrapped.getObject();
	}
	
	@Override
	public Class<?> getObjectClass() {
		
		return this.wrapped.getObjectClass();
	}
	
	@Override
	public String getProtocolName() {
		
		return this.wrapped.getProtocolName();
	}
	
	@Override
	public String getProtocolVariant() {
		
		return this.wrapped.getProtocolVariant();
	}
	
	@Override
	public BaseMessage[] getSequence() {
		
		return this.wrapped.getSequence();
	}
	
	@Override
	public String getSourceAddress() {
		
		return this.wrapped.getSourceAddress();
	}
	
	@Override
	public String getSourceAddressExact() {
		
		return this.wrapped.getSourceAddressExact();
	}
	
	@Override
	public String getTarget() {
		
		return this.wrapped.getTarget();
	}
	
	@Override
	public String getTargetAddress() {
		
		return this.wrapped.getTargetAddress();
	}
	
	@Override
	public String getTargetExact() {
		
		return this.wrapped.getTargetExact();
	}
	
	@Override
	public boolean isBinary() {
		
		return this.wrapped.isBinary();
	}
	
	@Override
	public boolean isCharacter() {
		
		return this.wrapped.isCharacter();
	}
	
	@Override
	public boolean isEmpty() {
		
		return this.wrapped.isEmpty();
	}
	
	@Override
	public boolean isFile() {
		
		return this.wrapped.isFile();
	}
	
	@Override
	public boolean isObject() {
		
		return this.wrapped.isObject();
	}
	
	@Override
	public boolean isSequence() {
		
		return this.wrapped.isSequence();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final BaseObject value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final double value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final int value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final long value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final Object value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttribute(final String name, final String value) {
		
		this.wrapped = this.wrapped.setAttribute(name, value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setAttributes(final BaseObject attributes) {
		
		this.wrapped = this.wrapped.setAttributes(attributes);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setContentDisposition(final String disposition) {
		
		this.wrapped = this.wrapped.setContentDisposition(disposition);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setContentID(final String contentName) {
		
		this.wrapped = this.wrapped.setContentID(contentName);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setContentName(final String contentName) {
		
		this.wrapped = this.wrapped.setContentName(contentName);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setContentType(final String contentType) {
		
		this.wrapped = this.wrapped.setContentType(contentType);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setEncoding(final Charset charset) {
		
		this.wrapped = this.wrapped.setEncoding(charset);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setEncoding(final String encoding) {
		
		this.wrapped = this.wrapped.setEncoding(encoding);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setSessionID(final String sessionId) {
		
		this.wrapped = this.wrapped.setSessionID(sessionId);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T setUserID(final String userId) {
		
		this.wrapped = this.wrapped.setUserID(userId);
		return (T) this;
	}
	
	@Override
	public BinaryMessage<?> toBinary() throws FlowOperationException {
		
		return this.wrapped.toBinary();
	}
	
	@Override
	public CharacterMessage<?> toCharacter() throws FlowOperationException {
		
		return this.wrapped.toCharacter();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T useAttributes(final BaseObject attributes) {
		
		this.wrapped = this.wrapped.useAttributes(attributes);
		return (T) this;
	}
	
}
