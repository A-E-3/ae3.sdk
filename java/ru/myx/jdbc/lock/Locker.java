package ru.myx.jdbc.lock;

/**
 * @author myx
 * 
 */
public interface Locker {
	/**
	 * @return date
	 */
	public long getLockDate();
	
	/**
	 * @return date
	 */
	public long getLockExpiration();
	
	/**
	 * @return string
	 */
	public String getLockId();
	
	/**
	 * @return boolean
	 */
	public boolean isOwned();
	
	/**
     * 
     */
	public void lockRelease();
	
	/**
     * 
     */
	public void lockUpdate();
}
