/**
 *
 */
package ru.myx.ae3.mime;

/** @author myx */
public enum MimeTypeCompressibility {
	/**
	 */
	DONT_COMPRESS("no") {

		@Override
		public boolean getCompressionSure(final boolean defaultValue) {

			return false;
		}

		@Override
		public boolean getCompressionTry(final boolean defaultValue) {

			return false;
		}
	},

	/**
	 */
	DEFAULT("may") {

		@Override
		public boolean getCompressionSure(final boolean defaultValue) {

			return false;
		}

		@Override
		public boolean getCompressionTry(final boolean defaultValue) {

			return defaultValue;
		}
	},

	/**
	 */
	TRY_COMPRESS("try") {

		@Override
		public boolean getCompressionSure(final boolean defaultValue) {

			return false;
		}

		@Override
		public boolean getCompressionTry(final boolean defaultValue) {

			return true;
		}
	},

	/**
	 */
	SURE_COMPRESS("yes") {

		@Override
		public boolean getCompressionSure(final boolean defaultValue) {

			return true;
		}

		@Override
		public boolean getCompressionTry(final boolean defaultValue) {

			return true;
		}
	},
	//
	;

	/** @param name
	 * @return */
	public static final MimeTypeCompressibility getCompressabilityForName(final String name) {

		if (name == null) {
			return DEFAULT;
		}
		final int length = name.length();
		if (length < 1) {
			return DEFAULT;
		}
		switch (name.charAt(0)) {
			case 'd' : {
				if (length == 7 && "default".equals(name)) {
					return DEFAULT;
				}
				if (length == 4 && "dont".equals(name)) {
					return DONT_COMPRESS;
				}
				return DEFAULT;
			}
			case 'm' : {
				if (length == 3 && "may".equals(name)) {
					return DEFAULT;
				}
				return DEFAULT;
			}
			case 'n' : {
				if (length == 4 && "none".equals(name)) {
					return DONT_COMPRESS;
				}
				if (length == 2 && "no".equals(name)) {
					return DONT_COMPRESS;
				}
				return DEFAULT;
			}
			case 's' : {
				if (length == 4 && "sure".equals(name)) {
					return SURE_COMPRESS;
				}
				return DEFAULT;
			}
			case 't' : {
				if (length == 3 && "try".equals(name)) {
					return TRY_COMPRESS;
				}
				return DEFAULT;
			}
			case 'y' : {
				if (length == 4 && "yes".equals(name)) {
					return SURE_COMPRESS;
				}
				return DEFAULT;
			}
			default :
		}
		return DEFAULT;
	}

	private final String name;

	MimeTypeCompressibility(final String name) {

		this.name = name;
	}

	/** @param defaultValue
	 * @return */
	public abstract boolean getCompressionSure(final boolean defaultValue);

	/** @param defaultValue
	 * @return */
	public abstract boolean getCompressionTry(final boolean defaultValue);

	/** @return null for OTHER */
	public final String getName() {

		return this.name;
	}
}
