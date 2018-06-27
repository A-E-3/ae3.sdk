package ru.myx.jdbc.queueing;

import ru.myx.ae3.common.WaitTimeoutException;

/**
 * @author myx
 * 
 */
public class DatabaseWaitTimeoutException extends WaitTimeoutException {
	
	private final RequestAttachment<?, ? extends RunnerDatabaseRequestor>	source;
	
	/**
	 * 
	 */
	private static final long												serialVersionUID	= 2760643191410059284L;
	
	/**
	 * @param message
	 * @param source
	 */
	public DatabaseWaitTimeoutException(final String message,//
			final RequestAttachment<?, ? extends RunnerDatabaseRequestor> source) {
		super( message );
		this.source = source;
	}
	
	/**
	 * @param message
	 * @param source
	 * @param cause
	 */
	public DatabaseWaitTimeoutException(final String message,
			final RequestAttachment<?, ? extends RunnerDatabaseRequestor> source,
			final Throwable cause) {
		super( message, cause );
		this.source = source;
	}
	
	/**
	 * @return
	 */
	public final RequestAttachment<?, ? extends RunnerDatabaseRequestor> getRequestAttachment() {
		return this.source;
	}
}
