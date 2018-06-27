package ru.myx.ae3.auth;

/**
 * @author myx
 * 
 */
public final class LoginStartContextBean {
	private String	userId;
	
	private String	sessionId;
	
	private String	errorText;
	
	/**
	 * @return the errorText
	 */
	public final String getErrorText() {
		return this.errorText;
	}
	
	/**
	 * @return the sessionId
	 */
	public final String getSessionId() {
		return this.sessionId;
	}
	
	/**
	 * @return the userId
	 */
	public final String getUserId() {
		return this.userId;
	}
	
	/**
	 * @param errorText
	 *            the errorText to set
	 */
	public final void setErrorText(final String errorText) {
		this.errorText = errorText;
	}
	
	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public final void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * @param userId
	 *            the userId to set
	 */
	public final void setUserId(final String userId) {
		this.userId = userId;
	}
}
