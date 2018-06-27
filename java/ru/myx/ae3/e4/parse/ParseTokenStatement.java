package ru.myx.ae3.e4.parse;

/**
 * 
 * Statement extends value. From the abstract point of view, it is a value that
 * returns 'undefined'. But when it is important (syntactically) the identify
 * the 'statement' values as one that is not an 'expression'
 * 
 * @author myx
 *
 */
public interface ParseTokenStatement extends ParseTokenValue {
	//
}
