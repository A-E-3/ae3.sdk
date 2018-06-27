package ru.myx.jdbc.lock;

/**
 * @author myx
 * 
 */
public interface LockManager {
	/**
	 * @param interest
	 * @return boolean
	 */
	public boolean addInterest(final Interest interest);
	
	/**
	 * @param guid
	 * @param version
	 * @return locker
	 */
	public Locker createLock(final String guid, final int version);
	
	/**
	 * @param interest
	 * @return boolean
	 */
	public boolean removeInterest(final Interest interest);
	
	/**
	 * @param identity
	 */
	public void start(final String identity);
	
	/**
	 * @param identity
	 */
	public void stop(final String identity);
}
