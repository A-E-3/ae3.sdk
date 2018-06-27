package ru.myx.ae3.mime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import ru.myx.ae3.Engine;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 
 */
public class MimeType {
	/**
	 * Recipient type text/plain
	 */
	public static final String					SMT_TEXT_PLAIN				= "text/plain";
	
	/**
	 * Content type text/html
	 */
	public static final String					SMT_TEXT_HTML				= "text/html";
	
	/**
	 * Content type image/gif
	 */
	public static final String					SMT_IMAGE_GIF				= "image/gif";
	
	/**
	 * Content type image/jpeg
	 */
	public static final String					SMT_IMAGE_JPEG				= "image/jpeg";
	
	/**
	 * Content type application/x-unknown-content
	 */
	public static final String					SMT_APPLICATION_UNKNOWN		= "application/x-unknown-content";
	
	/**
	 * Content type multipart/mixed
	 */
	public static final String					SMT_MULTIPART_MIXED			= "multipart/mixed";
	
	/**
	 * Content type multipart/related
	 */
	public static final String					SMT_MULTIPART_RELATED		= "multipart/related";
	
	/**
	 * Content type multipart/signed
	 */
	public static final String					SMT_MULTIPART_SIGNED		= "multipart/signed";
	
	/**
	 * Content type multipart/alternative
	 */
	public static final String					SMT_MULTIPART_ALTERNATIVE	= "multipart/alternative";
	
	/**
	 * Content type multipart/digest
	 */
	public static final String					SMT_MULTIPART_DIGEST		= "multipart/digest";
	
	/**
	 * Content type multipart/parallel
	 */
	public static final String					SMT_MULTIPART_PARALLEL		= "multipart/parallel";
	
	private static final Map<String, MimeType>	EXTENSION_TO_MIME_TYPE		= new HashMap<>();
	
	static final Map<String, MimeType>			CONTENT_TYPE_TO_MIME_TYPE	= new HashMap<>();
	
	static final Map<String, MimeType>			CONTENT_TYPE_TO_GENERIC		= new HashMap<>();
	
	/**
	 * 
	 */
	public static final MimeType				MT_APPLICATION_OCTET_STREAM;
	
	static {
		// main
		
		MT_APPLICATION_OCTET_STREAM = MimeType.registerDefine( "application/octet-stream",
				MimeTypeCompressibility.TRY_COMPRESS,
				true,
				null );
		
		// read
		
		MimeType.readMimeTypes( new File( new File( new File( Engine.PATH_PUBLIC, "resources" ), "data" ),
				"mime-types.txt" ) );
		
		//
		// application
		//
		
		MimeType.registerDefine( "application/java-archive", MimeTypeCompressibility.DEFAULT, "jar" );
		MimeType.registerDefine( "application/zip", MimeTypeCompressibility.DONT_COMPRESS, "zip" );
		MimeType.registerDefine( "application/octet-stream", MimeTypeCompressibility.TRY_COMPRESS, "bin" );
		
		//
		// image
		//
		
		MimeType.registerDefine( "image/gif", MimeTypeCompressibility.TRY_COMPRESS, "gif" );
		MimeType.registerDefine( "image/png", MimeTypeCompressibility.DONT_COMPRESS, "png" );
		MimeType.registerDefine( "image/jpeg", MimeTypeCompressibility.DONT_COMPRESS, "jpg" );
		
		//
		// text
		//
		
		MimeType.registerDefine( "text/plain", MimeTypeCompressibility.SURE_COMPRESS, "txt" );
		MimeType.registerDefine( "text/html", MimeTypeCompressibility.SURE_COMPRESS, "html" );
		MimeType.registerDefine( "text/vnd.sun.j2me.app-descriptor", MimeTypeCompressibility.TRY_COMPRESS, "jad" );
		MimeType.registerDefine( "text/tpl", MimeTypeCompressibility.SURE_COMPRESS, "tpl" );
		MimeType.registerDefine( "text/xml-acm-scheme", MimeTypeCompressibility.SURE_COMPRESS, "scheme" );
		MimeType.registerDefine( "text/javascript", MimeTypeCompressibility.SURE_COMPRESS, "js" );
		MimeType.registerDefine( "text/css", MimeTypeCompressibility.SURE_COMPRESS, "css" );
		MimeType.registerDefine( "text/xml", MimeTypeCompressibility.SURE_COMPRESS, "xml" );
		
		MimeType.registerSuper( "text/javascript", "text/plain" );
		MimeType.registerSuper( "text/xml", "text/plain" );
		MimeType.registerSuper( "text/html", "text/plain" );
	}
	
	/**
	 * @param contentType
	 * @param defaultValue
	 * @return boolean
	 */
	public static final boolean compressByContentTypeSure(final String contentType, final boolean defaultValue) {
		if (contentType == null) {
			return defaultValue;
		}
		final MimeType mimeType = MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( contentType );
		if (mimeType == null) {
			final int pos = contentType.indexOf( ';' );
			if (pos != -1) {
				return MimeType.compressByContentTypeSure( contentType.substring( 0, pos ), defaultValue );
			}
			return defaultValue;
		}
		return mimeType.getCompressionSure( defaultValue );
	}
	
	/**
	 * @param contentType
	 * @param defaultValue
	 * @return boolean
	 */
	public static final boolean compressByContentTypeTry(final String contentType, final boolean defaultValue) {
		if (contentType == null) {
			return defaultValue;
		}
		final MimeType mimeType = MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( contentType );
		if (mimeType == null) {
			final int pos = contentType.indexOf( ';' );
			if (pos != -1) {
				return MimeType.compressByContentTypeTry( contentType.substring( 0, pos ), defaultValue );
			}
			return defaultValue;
		}
		return mimeType.getCompressionTry( defaultValue );
	}
	
	/**
	 * @param file
	 * @param defaultValue
	 * @return content-type
	 */
	public static final String forEntry(final Entry file, final String defaultValue) {
		if (file == null) {
			return defaultValue;
		}
		final String name = file.getKey();
		final int dpos = name.lastIndexOf( '.' );
		if (dpos == -1) {
			return defaultValue;
		}
		final String extension = name.substring( dpos + 1 ).toLowerCase();
		final MimeType mimeType = MimeType.EXTENSION_TO_MIME_TYPE.get( extension );
		return mimeType == null
				? defaultValue
				: mimeType.toString();
	}
	
	/**
	 * @param file
	 * @param defaultValue
	 * @return content-type
	 */
	public static final String forFile(final File file, final String defaultValue) {
		if (file == null) {
			return defaultValue;
		}
		final String name = file.getName();
		final int dpos = name.lastIndexOf( '.' );
		if (dpos == -1) {
			return defaultValue;
		}
		final String extension = name.substring( dpos + 1 ).toLowerCase();
		final MimeType mimeType = MimeType.EXTENSION_TO_MIME_TYPE.get( extension );
		return mimeType == null
				? defaultValue
				: mimeType.toString();
	}
	
	/**
	 * @param name
	 * @param defaultValue
	 * @return content-type
	 */
	public static final String forName(final String name, final String defaultValue) {
		if (name == null) {
			return defaultValue;
		}
		final int dpos = name.lastIndexOf( '.' );
		if (dpos == -1) {
			return defaultValue;
		}
		final String extension = name.substring( dpos + 1 ).toLowerCase();
		final MimeType mimeType = MimeType.EXTENSION_TO_MIME_TYPE.get( extension );
		return mimeType == null
				? defaultValue
				: mimeType.toString();
	}
	
	/**
	 * @param contentType
	 * @param compressibility
	 * @param registerUnique
	 * @return
	 */
	public static final MimeType getMimeType(
			final String contentType,
			final MimeTypeCompressibility compressibility,
			final boolean registerUnique) {
		final int pos = contentType.indexOf( ';' );
		return pos == -1
				? MimeType.registerDefine( contentType, compressibility == null
						? MimeTypeCompressibility.DEFAULT
						: compressibility, registerUnique, MimeType.MT_APPLICATION_OCTET_STREAM )
				: MimeType.getMimeType( contentType.substring( 0, pos ), compressibility, registerUnique );
	}
	
	private static final void readMimeTypes(final File file) {
		if (!file.exists()) {
			return;
		}
		final String source = Transfer.createBuffer( file ).toString();
		for (final StringTokenizer lines = new StringTokenizer( source, "\r\n" ); lines.hasMoreTokens();) {
			final String line = lines.nextToken().trim();
			if (line.length() == 0 || line.charAt( 0 ) == '#') {
				continue;
			}
			final LinkedList<String> tokens = new LinkedList<>();
			for (final StringTokenizer columns = new StringTokenizer( line ); columns.hasMoreTokens();) {
				final String column = columns.nextToken();
				if (column.length() > 0) {
					tokens.add( column );
				}
			}
			if (tokens.isEmpty()) {
				continue;
			}
			final String operation = tokens.removeFirst();
			if ("define".equals( operation )) {
				if (tokens.isEmpty()) {
					continue;
				}
				final String realType = tokens.removeFirst();
				final MimeTypeCompressibility compress = tokens.isEmpty()
						? MimeTypeCompressibility.DEFAULT
						: MimeTypeCompressibility.getCompressabilityForName( tokens.removeFirst() );
				final MimeType mimeType = MimeType.registerDefine( realType, compress, true, null );
				while (!tokens.isEmpty()) {
					final String extension = tokens.removeFirst();
					if (extension.length() < 2 || extension.charAt( 0 ) != '.') {
						continue;
					}
					mimeType.addExtension( extension );
					final String cleanExtension = extension.substring( 1 );
					if (!MimeType.EXTENSION_TO_MIME_TYPE.containsKey( cleanExtension )) {
						MimeType.EXTENSION_TO_MIME_TYPE.put( cleanExtension, mimeType );
					}
				}
				continue;
			}
			if ("alias".equals( operation )) {
				if (tokens.isEmpty()) {
					continue;
				}
				final String realType = tokens.removeFirst();
				if (tokens.isEmpty()) {
					continue;
				}
				final String aliasType = tokens.removeFirst();
				MimeType.registerAlias( realType, aliasType );
				continue;
			}
			if ("super".equals( operation )) {
				if (tokens.isEmpty()) {
					continue;
				}
				final String realType = tokens.removeFirst();
				if (tokens.isEmpty()) {
					continue;
				}
				final String superType = tokens.removeFirst();
				MimeType.registerSuper( realType, superType );
				continue;
			}
		}
	}
	
	private static final void registerAlias(final String realType, final String type) {
		final MimeType mimeType;
		if (MimeType.CONTENT_TYPE_TO_MIME_TYPE.containsKey( realType )) {
			mimeType = MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( realType );
		} else {
			final int pos = type.indexOf( '/' );
			if (pos == -1) {
				Report.warning( "MIME", "Invalid mime alias, main=" + realType + ", alias=" + type + ", skipping!" );
				return;
			}
			Report.warning( "MIME", "Main mime type is unknown while trying to register alias type, main="
					+ realType
					+ ", alias="
					+ type
					+ ", will use default compressibility!" );
			mimeType = new MimeType( type.substring( 0, pos ),
					type.substring( pos + 1 ),
					MimeTypeCompressibility.DEFAULT );
			MimeType.CONTENT_TYPE_TO_MIME_TYPE.put( realType, mimeType );
		}
		if (!realType.equals( type )) {
			MimeType.CONTENT_TYPE_TO_MIME_TYPE.put( type, mimeType );
		}
	}
	
	private static final MimeType registerDefine(
			final String type,
			final MimeTypeCompressibility compression,
			final boolean registerUnique,
			final MimeType defaultValue) {
		if (MimeType.CONTENT_TYPE_TO_MIME_TYPE.containsKey( type )) {
			return MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( type );
		}
		final int pos = type.indexOf( '/' );
		if (pos == -1) {
			return defaultValue;
		}
		final MimeType mimeType = new MimeType( type.substring( 0, pos ), type.substring( pos + 1 ), compression );
		if (registerUnique) {
			synchronized (MimeType.class) {
				if (MimeType.CONTENT_TYPE_TO_MIME_TYPE.containsKey( type )) {
					return MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( type );
				}
				MimeType.CONTENT_TYPE_TO_MIME_TYPE.put( type, mimeType );
			}
		}
		return mimeType;
	}
	
	private static final void registerDefine(
			final String type,
			final MimeTypeCompressibility compression,
			final String extension) {
		final MimeType mimeType = MimeType.registerDefine( type, compression, true, null );
		mimeType.addExtension( '.' + extension );
		if (!MimeType.EXTENSION_TO_MIME_TYPE.containsKey( extension )) {
			MimeType.EXTENSION_TO_MIME_TYPE.put( extension, mimeType );
		}
	}
	
	private static final void registerSuper(final String realType, final String superType) {
		if (realType.equals( superType )) {
			Report.error( "MIME", "super type is same as real type: " + realType );
			return;
		}
		final MimeType mimeType;
		if (MimeType.CONTENT_TYPE_TO_MIME_TYPE.containsKey( superType )) {
			mimeType = MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( superType );
		} else {
			final int pos = superType.indexOf( '/' );
			if (pos == -1) {
				return;
			}
			Report.warning( "MIME", "Supertype mime type is unknown while trying to register super type, main="
					+ realType
					+ ", super="
					+ superType
					+ ", will use default compressibility!" );
			mimeType = new MimeType( superType.substring( 0, pos ),
					superType.substring( pos + 1 ),
					MimeTypeCompressibility.DEFAULT );
			MimeType.CONTENT_TYPE_TO_MIME_TYPE.put( realType, mimeType );
		}
		MimeType.CONTENT_TYPE_TO_GENERIC.put( realType, mimeType );
	}
	
	/**
	 * @param contentType
	 * @param defaultValue
	 * @return extension
	 */
	public static final String toExtension(final String contentType, final String defaultValue) {
		if (contentType == null) {
			return defaultValue;
		}
		final MimeType mimeType = MimeType.CONTENT_TYPE_TO_MIME_TYPE.get( contentType );
		if (mimeType == null) {
			final int pos = contentType.indexOf( ';' );
			if (pos != -1) {
				return MimeType.toExtension( contentType, defaultValue );
			}
			return defaultValue;
		}
		final String result = mimeType.getExtensionDefault();
		return result == null
				? defaultValue
				: result;
	}
	
	private final MimeMediaType				mediaType;
	
	private final String					mimeMediaTypeName;
	
	private final String					mimeSubTypeName;
	
	private final String					contentTypeName;
	
	private final MimeTypeCompressibility	compression;
	
	private String							extensionDefault;
	
	private List<String>					extensionList;
	
	MimeType(final String mimeMediaTypeName, final String mimeSubTypeName, final MimeTypeCompressibility compression) {
		this.mediaType = MimeMediaType.getMediaTypeForName( mimeMediaTypeName );
		// single instance string
		this.mimeMediaTypeName = this.mediaType == MimeMediaType.OTHER
				? mimeMediaTypeName
				: this.mediaType.getName();
		this.mimeSubTypeName = mimeSubTypeName;
		this.contentTypeName = mimeMediaTypeName + '/' + mimeSubTypeName;
		this.compression = compression == MimeTypeCompressibility.DEFAULT
				? this.mediaType.getCompressibility()
				: compression;
	}
	
	final void addExtension(final String extension) {
		if (this.extensionList == null) {
			this.extensionDefault = extension;
			this.extensionList = new ArrayList<>();
		} else {
			if (this.extensionList.contains( extension )) {
				return;
			}
		}
		this.extensionList.add( extension );
	}
	
	/**
	 * @return
	 */
	public final MimeTypeCompressibility getCompressibility() {
		return this.compression;
	}
	
	/**
	 * @param defaultValue
	 * @return
	 */
	public boolean getCompressionSure(final boolean defaultValue) {
		return this.compression.getCompressionSure( defaultValue );
	}
	
	/**
	 * @param defaultValue
	 * @return
	 */
	public boolean getCompressionTry(final boolean defaultValue) {
		return this.compression.getCompressionTry( defaultValue );
	}
	
	/**
	 * 
	 * @return
	 */
	public String getContentType() {
		return this.contentTypeName;
	}
	
	/**
	 * @return
	 */
	public String getExtensionDefault() {
		return this.extensionDefault;
	}
	
	/**
	 * @return
	 */
	public String[] getExtensionList() {
		return this.extensionList == null
				? null
				: this.extensionList.toArray( new String[this.extensionList.size()] );
	}
	
	/**
	 * @return
	 */
	public String getMediaTypeName() {
		return this.mimeMediaTypeName;
	}
	
	/**
	 * @return
	 */
	public String getSubTypeName() {
		return this.mimeSubTypeName;
	}
	
	/**
	 * @return
	 */
	public MimeType getSuperType() {
		if (this == MimeType.MT_APPLICATION_OCTET_STREAM) {
			return null;
		}
		final MimeType superType = MimeType.CONTENT_TYPE_TO_GENERIC.get( this.contentTypeName );
		return superType == null
				? MimeType.MT_APPLICATION_OCTET_STREAM
				: superType;
	}
	
	/**
	 * Same as this.getContentType();
	 */
	@Override
	public String toString() {
		return this.contentTypeName;
	}
	
}
