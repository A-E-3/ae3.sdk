package ru.myx.ae3.base;

/**
 * @author myx
 *
 */
public abstract class BaseFunctionWithConstructorAbstract extends BaseFunctionAbstract {
	
	private final BaseFunction constructor;

	/**
	 * @param constructor
	 *
	 */
	protected BaseFunctionWithConstructorAbstract(final BaseFunction constructor) {
		super();
		assert constructor != null;
		this.constructor = constructor;
	}

	@Override
	public final BaseFunction baseConstruct() {
		
		return this.constructor;
	}

	@Override
	public final BaseObject baseConstructPrototype() {
		
		return this.constructor.baseConstructPrototype();
	}
}
