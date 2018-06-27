package ru.myx.jdbc.lock;

/**
 * @author myx
 * 
 */
public final class Interest {
	/**
     * 
     */
	public final String	lock;
	
	/**
     * 
     */
	public final int	version;
	
	/**
     * 
     */
	public final Runner	runner;
	
	/**
	 * @param lock
	 * @param runner
	 */
	public Interest(final String lock, final Runner runner) {
		this.lock = lock;
		this.version = runner.getVersion();
		this.runner = runner;
	}
	
	@Override
	public String toString() {
		return "INTEREST{ lock=" + this.lock + ", runner=" + this.runner + " }";
	}
}
