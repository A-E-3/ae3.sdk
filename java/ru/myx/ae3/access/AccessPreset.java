/*
 * Created on 10.05.2006
 */
package ru.myx.ae3.access;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
public interface AccessPreset {
	/**
	 * @return permissions
	 */
	public String[] getPermissions();
	
	/**
	 * @return title
	 */
	public BaseObject getTitle();
}
