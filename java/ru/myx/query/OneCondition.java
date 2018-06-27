/*
 * Created on 24.07.2004
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.query;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface OneCondition extends Comparable<OneCondition> {
	/**
	 * @return string
	 */
	String getField();
	
	/**
	 * @return string
	 */
	String getOperator();
	
	/**
	 * @return string
	 */
	String getValue();
	
	/**
	 * @return boolean
	 */
	boolean isExact();
	
	@Override
	String toString();
}
