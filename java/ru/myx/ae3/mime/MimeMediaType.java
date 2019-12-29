/**
 *
 */
package ru.myx.ae3.mime;

/** @author myx */
public enum MimeMediaType {
	/**
	 */
	APPLICATION("application", MimeTypeCompressibility.DEFAULT),

	/**
	 */
	AUDIO("audio", MimeTypeCompressibility.DONT_COMPRESS),

	/**
	 */
	EXAMPLE("example", MimeTypeCompressibility.TRY_COMPRESS),

	/**
	 */
	IMAGE("image", MimeTypeCompressibility.DONT_COMPRESS),

	/**
	 */
	MESSAGE("message", MimeTypeCompressibility.TRY_COMPRESS),

	/**
	 */
	MODEL("model", MimeTypeCompressibility.TRY_COMPRESS),

	/**
	 */
	MULTIPART("multipart", MimeTypeCompressibility.TRY_COMPRESS),

	/**
	 */
	TEXT("text", MimeTypeCompressibility.SURE_COMPRESS),

	/**
	 */
	VIDEO("video", MimeTypeCompressibility.DONT_COMPRESS),

	/**
	 */
	VND("vnd", MimeTypeCompressibility.TRY_COMPRESS),

	/**
	 */
	OTHER(null, MimeTypeCompressibility.DEFAULT),
	//
	;

	/** @param name
	 * @return */
	public static final MimeMediaType getMediaTypeForName(final String name) {

		if (name == null) {
			return OTHER;
		}
		final int length = name.length();
		if (length < 2) {
			return OTHER;
		}
		switch (name.charAt(0)) {
			case 'a' : {
				if (length == 11 && "application".equals(name)) {
					return APPLICATION;
				}
				if (length == 5 && "audio".equals(name)) {
					return AUDIO;
				}
				return OTHER;
			}
			case 'e' : {
				if (length == 7 && "example".equals(name)) {
					return EXAMPLE;
				}
				return OTHER;
			}
			case 'i' : {
				if (length == 5 && "image".equals(name)) {
					return IMAGE;
				}
				return OTHER;
			}
			case 'm' : {
				if (length == 7 && "message".equals(name)) {
					return MESSAGE;
				}
				if (length == 5 && "model".equals(name)) {
					return MODEL;
				}
				if (length == 9 && "multipart".equals(name)) {
					return MULTIPART;
				}
				return OTHER;
			}
			case 't' : {
				if (length == 4 && "text".equals(name)) {
					return TEXT;
				}
				return OTHER;
			}
			case 'v' : {
				if (length == 5 && "video".equals(name)) {
					return VIDEO;
				}
				if (length == 3 && "vnd".equals(name)) {
					return VND;
				}
				return OTHER;
			}
			default :
		}
		return OTHER;
	}

	private final String name;

	private final MimeTypeCompressibility compression;

	MimeMediaType(final String name, final MimeTypeCompressibility compression) {

		this.name = name;
		this.compression = compression;
	}

	/** @return */
	public final MimeTypeCompressibility getCompressibility() {

		return this.compression;
	}

	/** @return null for OTHER */
	public final String getName() {

		return this.name;
	}
}
