package ru.myx.ae3.help;

/**
 * @author myx
 * 
 */
public final class FileName {
	/**
	 * @param file
	 * @return extension
	 */
	public static final String extension(final java.io.File file) {
		if (file == null) {
			return "unknown";
		}
		return FileName.extensionUpperCase( file.getName() );
	}
	
	/**
	 * @param url
	 * @return extension
	 */
	public static final String extensionExact(final String url) {
		if (url == null || url.length() < 2) {
			return null;
		}
		final int pointPosition = url.lastIndexOf( '.' );
		if (pointPosition == -1) {
			final int slashPosition = Math.max( url.lastIndexOf( '/' ), url.lastIndexOf( '\\' ) );
			return url.substring( slashPosition + 1 );
		}
		return url.substring( pointPosition + 1 );
	}
	
	/**
	 * @param url
	 * @return extension
	 */
	public static final String extensionUpperCase(final String url) {
		if (url == null || url.length() < 2) {
			return null;
		}
		final int pointPosition = url.lastIndexOf( '.' );
		if (pointPosition == -1) {
			final int slashPosition = Math.max( url.lastIndexOf( '/' ), url.lastIndexOf( '\\' ) );
			return url.substring( slashPosition + 1 ).toUpperCase();
		}
		return url.substring( pointPosition + 1 ).toUpperCase();
	}
	
	private FileName() {
		// empty
	}
}
