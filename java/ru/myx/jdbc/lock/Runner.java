package ru.myx.jdbc.lock;

/**
 * @author myx
 * 
 */
public interface Runner {
	/**
	 * @return version
	 */
	public int getVersion();
	
	/**
     * 
     */
	public void start();
	
	/**
     * 
     */
	public void stop();
}
