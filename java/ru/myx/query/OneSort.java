/*
 * Created on 24.07.2004
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.query;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface OneSort {
	/**
	 * @author myx
	 * 
	 */
	static final class Simple implements OneSort {
		private final String	field;
		
		private final boolean	textual;
		
		private final boolean	numeric;
		
		private final boolean	descending;
		
		/**
		 * @param field
		 * @param textual
		 * @param numeric
		 * @param descending
		 */
		Simple(final String field, final boolean textual, final boolean numeric, final boolean descending) {
			this.field = field;
			this.textual = textual;
			this.numeric = numeric;
			this.descending = descending;
		}
		
		@Override
		public String getField() {
			return this.field;
		}
		
		@Override
		public boolean isDescending() {
			return this.descending;
		}
		
		@Override
		public boolean isNumeric() {
			return this.numeric;
		}
		
		@Override
		public boolean isTextual() {
			return this.textual;
		}
		
		@Override
		public String toString() {
			return this.field + (this.isNumeric()
					? " NUMERIC"
					: this.isTextual()
							? " TEXTUAL"
							: "") + (this.isDescending()
					? " DESCENDING"
					: " ASCENDING");
		}
	}
	
	/**
	 * @return string
	 */
	String getField();
	
	/**
	 * @return boolean
	 */
	boolean isDescending();
	
	/**
	 * @return boolean
	 */
	boolean isNumeric();
	
	/**
	 * @return boolean
	 */
	boolean isTextual();
}
