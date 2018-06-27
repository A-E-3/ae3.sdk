package ru.myx.ae3.base;

import java.util.Iterator;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.util.IteratorSingle;

/**
 * @author myx
 *
 */
public class BaseNativeError extends BaseAbstractException implements BaseProperty {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -8406673312851219088L;
	
	/**
	 *
	 */
	protected final BasePrimitiveString message;
	
	private static final BasePrimitiveString PROPERTY_BASE_MESSAGE = Base.forString("message");
	
	BaseNativeError() {
		this.message = null;
	}
	
	/**
	 * @param message
	 */
	public BaseNativeError(final BasePrimitiveString message) {
		super(message == null
			? null
			: message.baseValue());
		this.message = message;
		
		// this.printStackTrace();
	}
	
	/**
	 * @param message
	 */
	public BaseNativeError(final String message) {
		super(message);
		this.message = Base.forString(message);
		
		// this.printStackTrace();
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public BaseNativeError(final String message, final Throwable cause) {
		super(message, cause);
		this.message = Base.forString(message);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		if (name == BaseNativeError.PROPERTY_BASE_MESSAGE) {
			return this;
		}
		return null;
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		if ("message".equals(name)) {
			return this;
		}
		return null;
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		return this.message != null;
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		return this.message == null
			? BaseObject.ITERATOR_EMPTY
			: new IteratorSingle<>("message");
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return this.message == null
			? BaseObject.ITERATOR_EMPTY_PRIMITIVE
			: new IteratorSingle<BasePrimitive<?>>(BaseNativeError.PROPERTY_BASE_MESSAGE);
	}
	
	//
	
	@Override
	public String toString() {
		
		return this.message == null
			? "[error Error]"
			: "[error Error: " + this.message + "]";
	}
	
	@Override
	public BaseNativeError baseValue() {
		
		return this;
	}
	
	@Override
	public short propertyAttributes(final CharSequence name) {
		
		return BaseProperty.ATTRS_MASK_NEN;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {
		
		return this.message;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		return this.message;
	}
	
	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		return this.message;
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		return store.execReturn(ctx, this.message);
	}
	
	@Override
	public CharSequence getMessageContent() {
		
		return this.message;
	}
}
