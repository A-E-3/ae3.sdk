/*
 * Created on 10.05.2006
 */
package ru.myx.ae3.access;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
public interface AccessPermission {
	/**
	 * @return key
	 */
	public String getKey();
	
	/**
	 * @return title
	 */
	public BaseObject getTitle();
	
	/**
	 * @return boolean
	 */
	public boolean isForControl();
}
