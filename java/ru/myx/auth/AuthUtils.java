package ru.myx.auth;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Base64;
import ru.myx.ae3.know.WhirlpoolDigest;
import ru.myx.ae3.serve.ServeRequest;

/**
 * 
 * @author myx
 * 
 */
public class AuthUtils {
	
	/**
	 * 
	 * @param hash
	 * @param login
	 * @param password
	 * @return null as 'false', same hash string as 'true' and new hash string
	 *         if hash is to be upgraded.
	 */
	public static final String checkPasswordHash(final String hash, final String login, final String password) {
		if (hash == null || hash.length() < 2 || hash.charAt( 1 ) != ';') {
			throw new IllegalArgumentException( "Invalid hash: " + hash );
		}
		final String lowerCaseLogin = login.toLowerCase();
		switch (hash.charAt( 0 )) {
		/**
		 * obsolete version
		 */
		case '0': {
			final int h = password.length() == 0
					? 0
					: lowerCaseLogin.hashCode() ^ password.hashCode();
			final String compare = "0;" + h;
			if (!compare.equals( hash )) {
				return null;
			}
			return AuthUtils.hashPassword( lowerCaseLogin, password );
		}
		/**
		 * current version
		 */
		case '1': {
			final String compare = AuthUtils.hashPassword( lowerCaseLogin, password );
			return compare.equals( hash )
					? hash
					: null;
		}
		default: {
			throw new IllegalArgumentException( "Invalid hash: " + hash );
		}
		}
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public static final String hashPassword(final String login, final String password) {
		final WhirlpoolDigest digest = new WhirlpoolDigest();
		final byte[] bytes = digest.digest( (login + '-' + password).getBytes( Engine.CHARSET_UTF8 ) );
		final byte[] hash = new byte[32];
		for (int i = bytes.length - 1; i >= 0; --i) {
			hash[i % 32] ^= bytes[i];
		}
		final StringBuilder result = new StringBuilder( 64 );
		result.append( "1;" );
		result.append( Base64.encode( hash, false ) );
		return result.toString();
	}
	
	/**
	 * 
	 * @param query
	 * @return null or {login:login, password:password}
	 */
	public static final BaseObject squeezeCredentials(final ServeRequest query) {
		{
			final BaseObject result = AuthUtils.squeezeCredentialsFromRequestParameters( query );
			if (result != null) {
				return result;
			}
		}
		{
			final BaseObject result = AuthUtils.squeezeCredentialsFromRequestHeaders( query );
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param query
	 * @return null or {login:login, password:password}
	 */
	public static final BaseObject squeezeCredentialsFromRequestHeaders(final ServeRequest query) {
		final String authorization = Base.getString( query.getAttributes(), "Authorization", "" ).trim();
		if (authorization.length() < 6) {
			return null;
		}
		if (!authorization.regionMatches( true, 0, "Basic", 0, 5 )) {
			return null;
		}
		final String auth;
		{
			final byte[] bytes = Base64.decode( authorization.substring( 6 ), true );
			if (bytes == null || bytes.length == 0) {
				return null;
			}
			auth = new String( bytes, Engine.CHARSET_UTF8 );
		}
		final int pos = auth.indexOf( ':' );
		if (pos == -1) {
			return null;
		}
		final String login = auth.substring( 0, pos ).trim().toLowerCase();
		if (login.length() == 0) {
			return null;
		}
		final String password = auth.substring( pos + 1 );
		/**
		 * cleanup
		 */
		query.setAttribute( "Authorization", "true " + login );
		/**
		 * 
		 */
		return new BaseNativeObject().putAppend( "login", login ).putAppend( "password", password );
	}
	
	/**
	 * 
	 * @param query
	 * @return null or {login:login, password:password}
	 */
	public static final BaseObject squeezeCredentialsFromRequestParameters(final ServeRequest query) {
		final BaseObject parameters = query.getParameters();
		assert parameters != null : "NULL java value";
		if (parameters.baseIsPrimitive()) {
			return null;
		}
		final String login = Base.getString( parameters, "login", "" ).toLowerCase();
		if (login.length() == 0) {
			return null;
		}
		final String password = Base.getString( parameters, "password", "" );
		if (password.length() == 0) {
			return null;
		}
		/**
		 * cleanup
		 */
		parameters.baseDelete( "password" );
		/**
		 * 
		 */
		return new BaseNativeObject().putAppend( "login", login ).putAppend( "password", password );
	}
}
