package ru.myx.ae3.e4.parse;

import ru.myx.ae3.base.BaseObject;

/**
 * 
 * @author myx
 *
 */
public interface ParseTokenValue extends ParseToken {
	
	/**
	 * If this token represents constant value, returns that value, otherwise
	 * returns NULL.
	 * 
	 * Have none or constant arguments already and doesn't produce side-effects.
	 * 
	 * @return NULL by default
	 */
	public BaseObject toConstantValue();
}
