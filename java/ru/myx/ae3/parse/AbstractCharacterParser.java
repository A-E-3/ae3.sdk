package ru.myx.ae3.parse;

import java.io.Reader;

/**
 * Looks like unneeded bloated bull-shit! Do something!
 * 
 * 
 * @author myx
 * 		
 * @param <V>
 */
public abstract class AbstractCharacterParser<V> implements InputParser<V> {
	
	/**
	 * 
	 * @param character
	 * @return NULL when it is OK to proceed, Throwable when the parsing error
	 *         should be raised
	 * @throws Exception
	 */
	protected abstract InputParser<V> onCharacter(char character) throws Exception;
	
	@Override
	public InputParser<V> parseChunk(final Reader reader) throws Exception {
		
		for (int c;;) {
			c = reader.read();
			if (c == -1) {
				return this;
			}
			final InputParser<V> replacement = this.onCharacter((char) c);
			if (replacement == null) {
				return null;
			}
			if (replacement != this) {
				return replacement.parseChunk(reader);
			}
		}
	}
	
	@Override
	public InputParser<V> parseChunk(final String string) throws Exception {
		
		final int length = string.length();
		for (int i = 0; i < length; ++i) {
			final InputParser<V> replacement = this.onCharacter(string.charAt(i));
			if (replacement == null) {
				return null;
			}
			if (replacement != this) {
				return replacement.parseChunk(string.substring(i + 1));
			}
		}
		return this;
	}
	
	@Override
	public InputParser<V> parseChunk(final CharSequence string) throws Exception {
		
		final int length = string.length();
		for (int i = 0; i < length; ++i) {
			final InputParser<V> replacement = this.onCharacter(string.charAt(i));
			if (replacement == null) {
				return null;
			}
			if (replacement != this) {
				return replacement.parseChunk(string.subSequence(i + 1, length));
			}
		}
		return this;
	}
	
	/**
	 * Parser is reset and result object is returned.
	 * 
	 * @return the 'result object' (the meaning and possible values are
	 *         implementation dependent)
	 */
	@Override
	public abstract V parseComplete();
}
