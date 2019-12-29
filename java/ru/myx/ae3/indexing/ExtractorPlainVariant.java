/*
 * Created on 08.02.2006
 */
package ru.myx.ae3.indexing;

import java.util.Collection;

/** @author myx */
public class ExtractorPlainVariant {

	private static final int ST_WHITESPACE = 0;

	private static final int ST_CHARS = 1;

	private static final int ST_COMPOUND_START = 2;

	private static final int ST_COMPOUND = 3;

	private static final int ST_COMPOUND_CONTINUE = 4;

	private static final int ST_CAPI_COMPOUND = 5;

	private static final int ST_CHARS_UPPER = 6;

	private static final int ST_COMPOUND_UPPER_START = 7;

	private static final int ST_COMPOUND_UPPER = 8;

	private static final int ST_COMPOUND_UPPER_CONTINUE = 9;

	/** @param result
	 * @param text
	 * @return collection */
	public static final Collection<String> extractContent(final Collection<String> result, final String text) {

		final int length = text.length();
		final StringBuilder token = new StringBuilder();
		final StringBuilder compound = new StringBuilder();
		int state = ExtractorPlainVariant.ST_WHITESPACE;
		for (int i = 0; i < length; ++i) {
			final char current = text.charAt(i);
			switch (state) {
				case ST_WHITESPACE : {
					if (current == '-' || current == '_' || current == '/' || current == '.') {
						// ignore
					} else //
					if (current == '$' || Character.isLetterOrDigit(current)) {
						token.append(Character.toLowerCase(current));
						state = ExtractorPlainVariant.ST_CHARS_UPPER;
					} else {
						// ignore
					}
				}
					break;
				case ST_CHARS : {
					if (current == '-' || current == '_' || current == '/' || current == '.' || current == '*' || current == '?') {
						result.add(token.toString());
						compound.append(token).append(current);
						token.setLength(0);
						state = ExtractorPlainVariant.ST_COMPOUND_START;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
					} else //
					if (Character.isLetter(current)) {
						if (Character.isUpperCase(current)) {
							result.add(token.toString());
							final char charachter = Character.toLowerCase(current);
							compound.append(token).append(charachter);
							token.setLength(0);
							token.append(charachter);
							state = ExtractorPlainVariant.ST_CAPI_COMPOUND;
						} else {
							token.append(current);
						}
					} else {
						result.add(token.toString());
						token.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND_START : {
					if (current == '?') {
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND;
					} else //
					if (current == '$') {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						token.append(character);
						compound.append(character);
						state = character == current
							? ExtractorPlainVariant.ST_COMPOUND
							: ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else {
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND : {
					if (current == '-' || current == '_' || current == '/' || current == '.' || current == '*' || current == '?') {
						compound.append(current);
						if (token.length() > 0) {
							result.add(token.toString());
							token.setLength(0);
						}
						state = ExtractorPlainVariant.ST_COMPOUND_CONTINUE;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						if (current != character) {
							compound.append(character);
							if (token.length() > 0) {
								result.add(token.toString());
								token.setLength(0);
							}
							token.append(character);
						} else {
							token.append(current);
							compound.append(current);
						}
					} else {
						result.add(token.toString());
						result.add(compound.toString());
						token.setLength(0);
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND_CONTINUE : {
					if (current == '?') {
						compound.append(current);
					} else //
					if (current == '$') {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND;
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						token.append(character);
						compound.append(character);
						state = current == character
							? ExtractorPlainVariant.ST_COMPOUND
							: ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else {
						result.add(compound.substring(0, compound.length() - 1));
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_CAPI_COMPOUND : {
					if (current == '-' || current == '_' || current == '/' || current == '.') {
						if (token.length() > 0) {
							result.add(token.toString());
							token.setLength(0);
						}
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_CONTINUE;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						if (current != character) {
							compound.append(character);
							if (token.length() > 1) {
								result.add(token.toString());
								token.setLength(0);
							}
							token.append(character);
						} else {
							token.append(current);
							compound.append(current);
						}
					} else {
						result.add(token.toString());
						token.setLength(0);
						result.add(compound.toString());
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_CHARS_UPPER : {
					if (current == '-' || current == '_' || current == '/' || current == '.' || current == '*' || current == '?') {
						result.add(token.toString());
						compound.append(token).append(current);
						token.setLength(0);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER_START;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
					} else //
					if (Character.isLetter(current)) {
						if (Character.isUpperCase(current)) {
							token.append(Character.toLowerCase(current));
						} else {
							token.append(current);
							state = ExtractorPlainVariant.ST_CHARS;
						}
					} else {
						result.add(token.toString());
						token.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND_UPPER_START : {
					if (current == '?') {
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						token.append(character);
						compound.append(character);
						state = current == character
							? ExtractorPlainVariant.ST_COMPOUND
							: ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else {
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND_UPPER : {
					if (current == '-' || current == '_' || current == '/' || current == '.' || current == '*' || current == '?') {
						compound.append(current);
						if (token.length() > 0) {
							result.add(token.toString());
							token.setLength(0);
						}
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER_CONTINUE;
					} else //
					if (current == '$' || Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						if (character != current) {
							token.append(character);
							compound.append(character);
						} else {
							token.append(current);
							compound.append(current);
							state = ExtractorPlainVariant.ST_COMPOUND;
						}
					} else {
						result.add(token.toString());
						result.add(compound.toString());
						token.setLength(0);
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				case ST_COMPOUND_UPPER_CONTINUE : {
					if (current == '?') {
						compound.append(current);
					} else //
					if (current == '$') {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isDigit(current)) {
						token.append(current);
						compound.append(current);
						state = ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else //
					if (Character.isLetter(current)) {
						final char character = Character.toLowerCase(current);
						token.append(character);
						compound.append(character);
						state = current == character
							? ExtractorPlainVariant.ST_COMPOUND
							: ExtractorPlainVariant.ST_COMPOUND_UPPER;
					} else {
						result.add(compound.substring(0, compound.length() - 1));
						compound.setLength(0);
						state = ExtractorPlainVariant.ST_WHITESPACE;
					}
				}
					break;
				default :
			}
		}
		if ((state == ExtractorPlainVariant.ST_CHARS || state == ExtractorPlainVariant.ST_CHARS_UPPER || state == ExtractorPlainVariant.ST_COMPOUND
				|| state == ExtractorPlainVariant.ST_COMPOUND_UPPER) && token.length() > 0) {
			result.add(token.toString());
		}
		if ((state == ExtractorPlainVariant.ST_COMPOUND || state == ExtractorPlainVariant.ST_COMPOUND_UPPER || state == ExtractorPlainVariant.ST_CAPI_COMPOUND)
				&& compound.length() > 0) {
			result.add(compound.toString());
		}
		if ((state == ExtractorPlainVariant.ST_COMPOUND_CONTINUE || state == ExtractorPlainVariant.ST_COMPOUND_UPPER_CONTINUE) && compound.length() > 0) {
			result.add(compound.substring(0, compound.length() - 1));
		}
		return result;
	}
}
