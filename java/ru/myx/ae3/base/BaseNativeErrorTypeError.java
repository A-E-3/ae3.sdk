package ru.myx.ae3.base;

/**
 * @author myx
 * 
 */
public final class BaseNativeErrorTypeError extends BaseNativeError {
	
	
	/**
	 * 
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(BaseError.PROTOTYPE);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246466593516526777L;
	
	/**
	 * 
	 */
	public BaseNativeErrorTypeError() {
		super();
	}
	
	/**
	 * @param message
	 */
	public BaseNativeErrorTypeError(final BasePrimitiveString message) {
		super(message);
	}
	
	/**
	 * @param message
	 */
	public BaseNativeErrorTypeError(final String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public BaseNativeErrorTypeError(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String toString() {
		
		return this.message == null
			? "[error TypeError]"
			: "[error TypeError: " + this.message + "]";
	}
}
