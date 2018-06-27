/*
 * Created on 16.10.2004
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.sapi;

import ru.myx.ae3.mime.MimeType;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FileSAPI {
	/**
	 * @param name
	 * @return string
	 */
	public static final String clearSlashes(final String name) {
		if (name == null) {
			return null;
		}
		return name.replace( '/', '_' ).replace( '\\', '_' );
	}
	
	/**
	 * @param url
	 * @return string
	 */
	public static final String getContentTypeForExtension(final String url) {
		return MimeType.forName( "file." + url, "-unknown-" );
	}
	
	/**
	 * @param url
	 * @return string
	 */
	public static final String getContentTypeForName(final String url) {
		return MimeType.forName( url, "-unknown-" );
	}
	
	/**
	 * @param url
	 * @return string
	 */
	public static final String getFileExtension(final String url) {
		if (url == null || url.length() < 2) {
			return null;
		}
		final int pointPosition = url.lastIndexOf( '.' );
		if (pointPosition == -1) {
			return url;
		}
		return url.substring( pointPosition + 1 );
	}
	
	/**
	 * @param url
	 * @return string
	 */
	public static final String getFileName(final String url) {
		if (url == null || url.length() < 2) {
			return null;
		}
		final int slashPosition = url.replace( '\\', '/' ).lastIndexOf( '/', url.length() - 2 );
		if (slashPosition == -1) {
			return url;
		}
		return url.substring( slashPosition + 1 );
	}
	
	/**
	 * @param url
	 * @return string
	 */
	public static final String getFileTitle(final String url) {
		if (url == null || url.length() < 2) {
			return null;
		}
		final int slashPosition = url.replace( '\\', '/' ).lastIndexOf( '/', url.length() - 2 );
		if (slashPosition == -1) {
			final int pointPosition = url.lastIndexOf( '.' );
			if (pointPosition == -1) {
				return url;
			}
			return url.substring( 0, pointPosition );
		}
		final int pointPosition = url.lastIndexOf( '.' );
		if (pointPosition == -1 || pointPosition < slashPosition) {
			return url.substring( slashPosition + 1 );
		}
		return url.substring( slashPosition + 1, pointPosition );
	}
	
	/**
	 * @param name
	 * @return string
	 */
	public static final String niceNameDecode(final String name) {
		if (name == null) {
			return null;
		}
		return name.replace( "_-_-_-_", "+" ).replace( "_-_-_", "%" ).replace( "_-_", "#" ).replace( '_', ' ' );
	}
	
	/**
	 * @param name
	 * @return string
	 */
	public static final String niceNameEncode(final String name) {
		if (name == null) {
			return null;
		}
		return name.replace( ' ', '_' ).replace( "#", "_-_" ).replace( "%", "_-_-_" ).replace( "+", "_-_-_" );
	}
	
	// %><%RETURN: data.name.toLowerCase().replace('
	// ','_').replace(':','_').replace('"',"'").replace('`',"'") %><%
	/**
	 * @param name
	 * @return string
	 */
	public static final String niceNameNotation(final String name) {
		if (name == null) {
			return null;
		}
		return name.toLowerCase().replace( '/', '_' ).replace( ' ', '_' ).replace( ':', '_' ).replace( '+', '_' )
				.replace( '&', '_' ).replace( '?', '_' ).replace( '"', '\'' ).replace( '`', '\'' );
	}
}
