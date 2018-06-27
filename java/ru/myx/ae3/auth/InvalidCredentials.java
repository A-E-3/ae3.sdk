package ru.myx.ae3.auth;

/**
 * @author myx
 * 
 */
public final class InvalidCredentials extends Error {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= -7834146652040618817L;
	
	/**
	 * Stack-less instance
	 */
	public static final InvalidCredentials	INSTANCE			= new InvalidCredentials();
	static {
		InvalidCredentials.INSTANCE.setStackTrace( new StackTraceElement[0] );
	}
	
	/**
	 * 
	 */
	public InvalidCredentials() {
		super( "" );
	}
	
	@Override
	public String toString() {
		return "[error InvalidCredentials]";
	}
	
}
