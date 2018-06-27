package ru.myx.ae3.e4.parse;

import ru.myx.ae3.base.BaseObject;

/**
 * 
 * @author myx
 *
 */
public interface ParseContext {
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	ParseTokenValue constantRegister(
			BaseObject value);
	
	
	/**
	 * 'const', 'let' (and when 'ae3-ecma-nonstrict' 'var'
	 * 
	 * @param name
	 * @return
	 */
	ParseTokenValue declareBlockLocalVariable(
			CharSequence name);
	
	
	/**
	 * 'const', 'let' (and when 'ae3-ecma-nonstrict' 'var'
	 * 
	 * @param name
	 * @return
	 */
	ParseTokenValue declareBlockLocalConstant(
			CharSequence name);
	
	
	/**
	 * 'var'
	 * 
	 * @param name
	 * @return
	 */
	ParseTokenValue declareFrameLocal(
			CharSequence name);
	
	
	/**
	 * 
	 * @return
	 */
	ParseTokenValue toTokenValue();
	
	
	/**
	 * 
	 * @return
	 */
	ParseTokenStatement toTokenStatement();
	
}
