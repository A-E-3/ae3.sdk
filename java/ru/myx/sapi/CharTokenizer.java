package ru.myx.sapi;

import java.util.NoSuchElementException;

final class CharTokenizer {
	private int				currentPosition;
	
	private final String	delimiters;
	
	private char			maxDelimChar;
	
	private final int		maxPosition;
	
	private final String	str;
	
	CharTokenizer(final String str, final String delim, final boolean returnDelims) {
		this.currentPosition = 0;
		this.str = str;
		this.maxPosition = str.length();
		this.delimiters = delim;
		if (!returnDelims) {
			throw new IllegalArgumentException( "Only true!" );
		}
		this.setMaxDelimChar();
	}
	
	/**
	 * Tests if there are more tokens available from this tokenizer's string. If
	 * this method returns <tt>true</tt>, then a subsequent call to
	 * <tt>nextToken</tt> with no argument will successfully return a token.
	 * 
	 * @return <code>true</code> if and only if there is at least one token in
	 *         the string after the current position; <code>false</code>
	 *         otherwise.
	 */
	public final boolean hasMoreTokens() {
		return this.currentPosition < this.maxPosition;
	}
	
	/**
	 * Returns the next token from this string tokenizer.
	 * 
	 * @return the next token from this string tokenizer.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 */
	public final String nextToken() {
		if (this.currentPosition >= this.maxPosition) {
			throw new NoSuchElementException();
		}
		final int start = this.currentPosition;
		this.currentPosition = this.scanToken( this.currentPosition );
		return this.str.substring( start, this.currentPosition );
	}
	
	/**
	 * Skips ahead from startPos and returns the index of the next delimiter
	 * character encountered, or maxPosition if no such delimiter is found.
	 * 
	 * @param startPos
	 * @return int
	 */
	private final int scanToken(final int startPos) {
		int position = startPos;
		while (position < this.maxPosition) {
			final char c = this.str.charAt( position );
			if (c <= this.maxDelimChar && this.delimiters.indexOf( c ) >= 0) {
				break;
			}
			position++;
		}
		if (startPos == position) {
			final char c = this.str.charAt( position );
			if (c <= this.maxDelimChar && this.delimiters.indexOf( c ) >= 0) {
				position++;
			}
		}
		return position;
	}
	
	/**
	 * Set maxDelimChar to the highest char in the delimiter set.
	 */
	private final void setMaxDelimChar() {
		if (this.delimiters == null) {
			this.maxDelimChar = 0;
			return;
		}
		char m = 0;
		final int length = this.delimiters.length();
		for (int i = 0; i < length; ++i) {
			final char c = this.delimiters.charAt( i );
			if (m < c) {
				m = c;
			}
		}
		this.maxDelimChar = m;
	}
}
