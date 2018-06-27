/*
 * Created on 10.05.2006
 */
package ru.myx.ae3.access;

import java.util.Collections;
import java.util.Set;

import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 
 */
public interface AccessPermissions {
	/**
	 * 
	 */
	public static final Set<String>	PERMISSIONS_ALL		= new AccessPermissionsAll();
	
	/**
	 * 
	 */
	public static final Set<String>	PERMISSIONS_NONE	= Collections.emptySet();
	
	/**
	 * @param permission
	 * @return same or new object
	 */
	public AccessPermissions addPermission(final AccessPermission permission);
	
	/**
	 * @param key
	 * @param title
	 * @return same or new object
	 */
	public AccessPermissions addPermission(final String key, final BaseObject title);
	
	/**
	 * @param key
	 * @param title
	 * @param forControl
	 * @return same or new object
	 */
	public AccessPermissions addPermission(final String key, final BaseObject title, final boolean forControl);
	
	/**
	 * @param preset
	 * @return same or new object
	 */
	public AccessPermissions addPreset(final AccessPreset preset);
	
	/**
	 * @param permissions
	 * @param title
	 * @return same or new object
	 */
	public AccessPermissions addPreset(final String[] permissions, final BaseObject title);
	
	/**
	 * @return permissions
	 */
	public AccessPermission[] getAllPermissions();
	
	/**
	 * @return presets
	 */
	public AccessPreset[] getPresets();
}
