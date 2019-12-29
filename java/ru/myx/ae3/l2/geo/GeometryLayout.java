package ru.myx.ae3.l2.geo;

/** @author myx
 *
 *         Geometry has predefined set of layouts. Any GeometryRenderer must implement all of
 *         defined layouts here for its particular media type. */
public enum GeometryLayout {
	/** Attachment layout has following attributes:<br>
	 * - cssClass */
	ATTACHMENT {

		final String[] contentPropertyNames = new String[]{
				"attachment", "content"
		};

		@Override
		public String[] getContentPropertyNames() {

			return this.contentPropertyNames;
		}
	},
	/** Container layout has following attributes:<br>
	 * - cssClass */
	CONTAINER {

		final String[] contentPropertyNames = new String[]{
				"content"
		};

		@Override
		public String[] getContentPropertyNames() {

			return this.contentPropertyNames;
		}
	},
	/** Grid layout has following attributes:<br>
	 * - cssClass - width, integer, number of columns - elements, array of cells */
	GRID {

		@Override
		public String[] getContentPropertyNames() {

			return null;
		}
	},
	/**
	 *
	 */
	SEQUENCE {

		@Override
		public String[] getContentPropertyNames() {

			return null;
		}

	},
	/**
	 *
	 */
	STRING {

		@Override
		public String[] getContentPropertyNames() {

			return null;
		}

	},;

	/** @param layout
	 * @return */
	public static final GeometryLayout parse(final String layout) {

		assert layout != null;
		assert layout.length() > 0;
		switch (layout.charAt(0)) {
			case 's' :
				if (layout.length() == 6 && 't' == layout.charAt(1) && "string".equals(layout)) {
					return STRING;
				}
				if (layout.length() == 8 && 'e' == layout.charAt(1) && "sequence".equals(layout)) {
					return SEQUENCE;
				}
				break;
			case 'a' :
				if (layout.length() == 10 && 't' == layout.charAt(1) && "attachment".equals(layout)) {
					return ATTACHMENT;
				}
				break;
			case 'g' :
				if (layout.length() == 4 && 'r' == layout.charAt(1) && "grid".equals(layout)) {
					return GRID;
				}
				break;
			case 'c' :
				if (layout.length() == 9 && 'o' == layout.charAt(1) && "container".equals(layout)) {
					return CONTAINER;
				}
				break;
			default :
		}
		return null;
	}

	/** @return */
	public abstract String[] getContentPropertyNames();
}
