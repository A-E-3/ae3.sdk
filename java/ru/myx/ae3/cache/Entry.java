package ru.myx.ae3.cache;

import ru.myx.ae3.common.Value;

interface Entry<T> extends Value<T> {
	
	/**
	 * @return accessed date
	 */
	public long getAccessed();
	
	/**
	 * @return expiration date
	 */
	public long getExpiration();
	
	/**
	 * @return key
	 */
	public String getKey();
	
	/**
	 * @return true is reference was enqueued for garbage collection
	 */
	public boolean isEnqueued();
	
	/**
	 * @param id
	 * @return boolean
	 */
	public boolean isId(final String id);
	
	/**
	 * @return boolean
	 */
	public boolean isInvalid();
	
	/**
	 * 
	 */
	public void setAccessed();
	
	/**
	 * 
	 */
	public void setInvalid();
}
