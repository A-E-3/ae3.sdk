/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.help;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.sapi.FormatSAPI;

/** @author myx */
public final class Text {

	private static final Map<Character, String> TRANSLIT_XLAT = new HashMap<>(128);
	static {
		Text.TRANSLIT_XLAT.put(Character.valueOf('а'), "a");
		Text.TRANSLIT_XLAT.put(Character.valueOf('б'), "b");
		Text.TRANSLIT_XLAT.put(Character.valueOf('в'), "v");
		Text.TRANSLIT_XLAT.put(Character.valueOf('г'), "g");
		Text.TRANSLIT_XLAT.put(Character.valueOf('д'), "d");
		Text.TRANSLIT_XLAT.put(Character.valueOf('е'), "e");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ё'), "e");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ж'), "zh");
		Text.TRANSLIT_XLAT.put(Character.valueOf('з'), "z");
		Text.TRANSLIT_XLAT.put(Character.valueOf('и'), "i");
		Text.TRANSLIT_XLAT.put(Character.valueOf('й'), "y");
		Text.TRANSLIT_XLAT.put(Character.valueOf('к'), "k");
		Text.TRANSLIT_XLAT.put(Character.valueOf('л'), "l");
		Text.TRANSLIT_XLAT.put(Character.valueOf('м'), "m");
		Text.TRANSLIT_XLAT.put(Character.valueOf('н'), "n");
		Text.TRANSLIT_XLAT.put(Character.valueOf('о'), "o");
		Text.TRANSLIT_XLAT.put(Character.valueOf('п'), "p");
		Text.TRANSLIT_XLAT.put(Character.valueOf('р'), "r");
		Text.TRANSLIT_XLAT.put(Character.valueOf('с'), "s");
		Text.TRANSLIT_XLAT.put(Character.valueOf('т'), "t");
		Text.TRANSLIT_XLAT.put(Character.valueOf('у'), "u");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ф'), "f");
		Text.TRANSLIT_XLAT.put(Character.valueOf('х'), "h");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ц'), "ts");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ч'), "ch");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ш'), "sh");
		Text.TRANSLIT_XLAT.put(Character.valueOf('щ'), "sch");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ъ'), "'");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ы'), "y");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ь'), "'");
		Text.TRANSLIT_XLAT.put(Character.valueOf('э'), "e");
		Text.TRANSLIT_XLAT.put(Character.valueOf('ю'), "yu");
		Text.TRANSLIT_XLAT.put(Character.valueOf('я'), "ya");
		// Uppercase
		Text.TRANSLIT_XLAT.put(Character.valueOf('А'), "A");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Б'), "B");
		Text.TRANSLIT_XLAT.put(Character.valueOf('В'), "V");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Г'), "G");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Д'), "D");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Е'), "E");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ё'), "E");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ж'), "ZH");
		Text.TRANSLIT_XLAT.put(Character.valueOf('З'), "Z");
		Text.TRANSLIT_XLAT.put(Character.valueOf('И'), "I");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Й'), "Y");
		Text.TRANSLIT_XLAT.put(Character.valueOf('К'), "K");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Л'), "L");
		Text.TRANSLIT_XLAT.put(Character.valueOf('М'), "M");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Н'), "N");
		Text.TRANSLIT_XLAT.put(Character.valueOf('О'), "O");
		Text.TRANSLIT_XLAT.put(Character.valueOf('П'), "P");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Р'), "R");
		Text.TRANSLIT_XLAT.put(Character.valueOf('С'), "S");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Т'), "T");
		Text.TRANSLIT_XLAT.put(Character.valueOf('У'), "U");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ф'), "F");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Х'), "H");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ц'), "TS");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ч'), "CH");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ш'), "SH");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Щ'), "SCH");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ъ'), "'");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ы'), "Y");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ь'), "'");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Э'), "E");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Ю'), "YU");
		Text.TRANSLIT_XLAT.put(Character.valueOf('Я'), "YA");
	}

	/** @param s
	 * @return string */
	public static final String capitalize(final String s) {

		final StringBuilder result = new StringBuilder(s.toLowerCase());
		final int length = s.length();
		boolean capThis = true;
		for (int i = 0; i < length; ++i) {
			final char c = result.charAt(i);
			final boolean letter = Character.isLetterOrDigit(c);
			if (letter) {
				if (capThis) {
					result.setCharAt(i, Character.toUpperCase(c));
					capThis = false;
				}
			} else {
				if (c != '\'') {
					capThis = true;
				}
			}
		}
		return result.toString();
	}

	/** Extracts URL encoded string
	 *
	 * @param string
	 * @param charset
	 * @return string */
	public static final String decodeUri(final Object string, final Charset charset) {

		if (string == null) {
			return null;
		}
		final byte[] bytes = Text.decodeUriAsBytes(string.toString().replace('+', ' '));
		return new String(bytes, charset);
	}

	/** Extracts URL encoded string
	 *
	 * @param string
	 * @param encoding
	 * @return string */
	public static final String decodeUri(final Object string, final String encoding) {

		if (string == null) {
			return null;
		}
		final byte[] bytes = Text.decodeUriAsBytes(string.toString().replace('+', ' '));
		try {
			return new String(bytes, encoding);
		} catch (final Exception e) {
			final String s = new String(bytes);
			System.out.println("Error parsing url encoded String, enc=" + encoding + ", default result=" + s);
			e.printStackTrace();
			return s;
		}
	}

	/** Extracts URL encoded string
	 *
	 * @param string
	 * @return byte array */
	public static final byte[] decodeUriAsBytes(final Object string) {

		if (string == null) {
			return null;
		}
		final byte[] s = string.toString().getBytes(StandardCharsets.UTF_8);
		final int length = s.length;
		final char[] current = new char[2];
		try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(length)) {
			for (int i = 0; i < length; ++i) {
				final int c = s[i] & 0xFF;
				switch (c) {
					case '%' :
						if (i + 2 < length) {
							final int next1 = s[++i] & 0xFF;
							current[0] = (char) next1;
							final int next2 = s[++i] & 0xFF;
							current[1] = (char) next2;
							try {
								bos.write(Integer.parseInt(new String(current), 16));
							} catch (final NumberFormatException e) {
								bos.write('%');
							}
							break;
						}
						//$FALL-THROUGH$
					default :
						bos.write(c);
				}
			}
			return bos.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** @param source
	 * @return string */
	public static final String decodeXmlNodeValue(final Object source) {

		if (source == null) {
			return null;
		}
		final String sourceString = source.toString();
		final int sourceLength = sourceString.length();
		final StringBuilder result = new StringBuilder(sourceLength);
		boolean inEntity = false;
		String entity = "";

		final class CharTokenizer {

			private int index;

			private final int length;

			private final String str;

			CharTokenizer(final String str, final String delim, final boolean returnDelims) {

				assert "&;".equals(delim);
				assert returnDelims;
				this.index = 0;
				this.str = str;
				this.length = sourceLength;
			}

			/** Skips ahead from startPos and returns the index of the next delimiter character
			 * encountered, or maxPosition if no such delimiter is found.
			 *
			 * @param startPos
			 * @return int */
			private final int scanToken(final int startPos) {

				int position = startPos;
				while (position < this.length) {
					final char c = this.str.charAt(position);
					if (c == '&' || c == ';') {
						break;
					}
					position++;
				}
				if (startPos == position) {
					final char c = this.str.charAt(position);
					if (c == '&' || c == ';') {
						position++;
					}
				}
				return position;
			}

			/** Tests if there are more tokens available from this tokenizer's string. If this
			 * method returns <tt>true</tt>, then a subsequent call to <tt>nextToken</tt> with no
			 * argument will successfully return a token.
			 *
			 * @return <code>true</code> if and only if there is at least one token in the string
			 *         after the current position; <code>false</code> otherwise. */
			public final boolean hasMoreTokens() {

				return this.index < this.length;
			}

			/** Returns the next token from this string tokenizer.
			 *
			 * @return the next token from this string tokenizer.
			 * @exception NoSuchElementException
			 *                if there are no more tokens in this tokenizer's string. */
			public final String nextToken() {

				if (this.index >= this.length) {
					throw new NoSuchElementException();
				}
				final int start = this.index;
				this.index = this.scanToken(this.index);
				return this.str.substring(start, this.index);
			}
		}

		for (final CharTokenizer st = new CharTokenizer(sourceString, "&;", true); st.hasMoreTokens();) {
			String current = st.nextToken();
			if (current.length() == 1) {
				final char currentChar = current.charAt(0);
				if (currentChar == '&' && !inEntity) {
					inEntity = true;
					entity = "";
				} else //
				if (currentChar == '&' && inEntity) {
					inEntity = true;
					result.append('&');
					result.append(entity);
					entity = "";
				} else //
				if (currentChar == ';' && inEntity) {
					inEntity = false;
					if (entity.equals("#13")) {
						current = "\n";
					} else //
					if (entity.equals("#10")) {
						current = "\r";
					} else //
					if (entity.equals("#39")) {
						current = "'";
					} else //
					if (entity.equals("lt")) {
						current = "<";
					} else //
					if (entity.equals("gt")) {
						current = ">";
					} else //
					if (entity.equals("amp")) {
						current = "&";
					} else //
					if (entity.equals("quot")) {
						current = "\"";
					} else {
						current = entity;
					}
				}
			}
			if (!inEntity) {
				result.append(current);
			} else {
				entity = current;
			}
		}
		if (inEntity) {
			result.append('&').append(entity);
		}
		return result.toString();
	}

	/** Converts URL to an URL-safe string. Javascript's encodeURI.
	 *
	 * @param string
	 * @param charset
	 * @return string */
	public static final String encodeUri(final Object string, final Charset charset) {

		if (string == null) {
			return null;
		}
		final byte[] bytes = string.toString().getBytes(charset);
		final StringBuilder result = new StringBuilder(bytes.length);
		for (final byte element : bytes) {
			final int b = element & 0xFF;
			switch (b) {
				case ' ' :
				case '"' :
				case '<' :
				case '>' :
				case '%' :
				case '{' :
				case '}' :
				case '|' :
				case '\\' :
				case '^' :
				case '~' :
				case '[' :
				case ']' :
				case '`' :
				case '\r' :
				case '\n' :
					result.append('%');
					if (b < 16) {
						result.append('0');
					}
					result.append(Integer.toHexString(b));
					break;
				default :
					if (b < 32 || b >= 127) {
						result.append('%');
						if (b < 16) {
							result.append('0');
						}
						result.append(Integer.toHexString(b));
					} else {
						result.append((char) b);
					}
			}
		}
		return result.toString();
	}

	/** Converts URL to an URL-safe string. Javascript's encodeURI.
	 *
	 * @param string
	 * @param encoding
	 * @return string */
	public static final String encodeUri(final Object string, final String encoding) {

		if (string == null) {
			return null;
		}
		try {
			final byte[] bytes = string.toString().getBytes(encoding);
			final StringBuilder result = new StringBuilder(bytes.length);
			for (final byte element : bytes) {
				final int b = element & 0xFF;
				switch (b) {
					case ' ' :
					case '"' :
					case '<' :
					case '>' :
					case '%' :
					case '{' :
					case '}' :
					case '|' :
					case '\\' :
					case '^' :
					case '~' :
					case '[' :
					case ']' :
					case '`' :
					case '\r' :
					case '\n' :
						result.append('%');
						if (b < 16) {
							result.append('0');
						}
						result.append(Integer.toHexString(b));
						break;
					default :
						if (b < 32 || b >= 127) {
							result.append('%');
							if (b < 16) {
								result.append('0');
							}
							result.append(Integer.toHexString(b));
						} else {
							result.append((char) b);
						}
				}
			}
			return result.toString();
		} catch (final IOException e) {
			return e.toString();
		}
	}

	/** Converts String object to an URL-safe string. Javascript's encodeURIComponent.
	 *
	 * @param string
	 * @param charset
	 * @return string */
	public static final String encodeUriComponent(final Object string, final Charset charset) {

		if (string == null) {
			return null;
		}
		final byte[] bytes = string.toString().getBytes(charset);
		final StringBuilder result = new StringBuilder(bytes.length * 2);
		for (final byte element : bytes) {
			final int b = element & 0xFF;
			switch (b) {
				// more
				case ';' :
				case '/' :
				case '?' :
				case ':' :
				case '@' :
				case '&' :
				case '=' :
				case '+' :
				case '$' :
				case '#' :
				case ',' :

				case ' ' :
				case '"' :
				case '<' :
				case '>' :
				case '%' :
				case '{' :
				case '}' :
				case '|' :
				case '\\' :
				case '^' :
				case '~' :
				case '[' :
				case ']' :
				case '`' :
				case '\r' :
				case '\n' : {
					result.append('%');
					if (b < 16) {
						result.append('0');
					}
					result.append(Integer.toHexString(b));
					break;
				}
				default :
					if (b < 32 || b >= 127) {
						result.append('%');
						if (b < 16) {
							result.append('0');
						}
						result.append(Integer.toHexString(b));
					} else {
						result.append((char) b);
					}
			}
		}
		return result.toString();
	}

	/** Converts String object to an URL-safe string. Javascript's encodeURIComponent.
	 *
	 * @param string
	 * @param encoding
	 * @return string */
	public static final String encodeUriComponent(final Object string, final String encoding) {

		if (string == null) {
			return null;
		}
		try {
			final byte[] bytes = string.toString().getBytes(encoding);
			final StringBuilder result = new StringBuilder(bytes.length * 2);
			for (final byte element : bytes) {
				final int b = element & 0xFF;
				switch (b) {
					// more
					case ';' :
					case '/' :
					case '?' :
					case ':' :
					case '@' :
					case '&' :
					case '=' :
					case '+' :
					case '$' :
					case '#' :
					case ',' :

					case ' ' :
					case '"' :
					case '<' :
					case '>' :
					case '%' :
					case '{' :
					case '}' :
					case '|' :
					case '\\' :
					case '^' :
					case '~' :
					case '[' :
					case ']' :
					case '`' :
					case '\r' :
					case '\n' : {
						result.append('%');
						if (b < 16) {
							result.append('0');
						}
						result.append(Integer.toHexString(b));
						break;
					}
					default :
						if (b < 32 || b >= 127) {
							result.append('%');
							if (b < 16) {
								result.append('0');
							}
							result.append(Integer.toHexString(b));
						} else {
							result.append((char) b);
						}
				}
			}
			return result.toString();
		} catch (final IOException e) {
			return e.toString();
		}
	}

	/** TODO: remove? (unused, duplicate)
	 *
	 * Converts String object to an XML-safe string
	 *
	 * @param string
	 * @return string */
	public static final String encodeXmlNodeValue(final Object string) {

		return FormatSAPI.xmlNodeValue(string);
	}

	/** @param strs
	 * @param token
	 * @return string */
	public static final String join(final BaseArray strs, final String token) {

		final int length = strs.length();
		if (length == 0) {
			return "";
		}
		final StringBuilder result = new StringBuilder(128);
		for (int i = 0; i < length; ++i) {
			final String object = Base.getString(strs, i, "");
			if (result.length() > 0) {
				result.append(token);
			}
			result.append(object);
		}
		return result.toString();
	}

	/** Please, use standard String.join(delimiter, Iterable<? extends CharSequence) if matches.
	 *
	 * @param strs
	 * @param token
	 * @return string */
	public static final String join(final Iterable<?> strs, final String token) {

		final StringBuilder result = new StringBuilder(128);
		for (final Object object : strs) {
			if (result.length() > 0) {
				result.append(token);
			}
			result.append(String.valueOf(object));
		}
		return result.toString();
	}

	/** @param objects
	 * @param token
	 * @return string */
	public static final String join(final Object[] objects, final String token) {
		
		if (objects == null) {
			return "";
		}
		final StringBuilder result = new StringBuilder(128);
		for (final Object element : objects) {
			if (element != null) {
				if (result.length() > 0) {
					result.append(token);
				}
				result.append(element);
			}
		}
		return result.toString();
	}

	/** @param source
	 * @param maxLength
	 * @return string */
	public static final String limitString(final String source, final int maxLength) {

		if (source.length() > maxLength) {
			final String signature = '_' + Integer.toString(source.length() % 36, 36) + Integer.toString(source.hashCode() % (36 * 36), 36);
			final int partLength = maxLength - signature.length();
			if (partLength < 0) {
				return source.substring(0, maxLength);
			}
			return source.substring(0, partLength) + signature;
		}
		return source;
	}

	/** Cuts a String if string is longer than specified. Appends suffix specified to the end of
	 * string if cut occured.
	 *
	 * @param string
	 *
	 * @param maxLength
	 *            parameter specifies maximum valid string length.
	 * @param cutSuffix
	 * @return string */
	public static final String limitString(final String string, final int maxLength, final String cutSuffix) {

		if (string.length() > maxLength) {
			return string.substring(0, maxLength) + cutSuffix;
		}
		return string;
	}

	/** padds using specified character to fit string into specified length.
	 *
	 * @param string
	 * @param padder
	 * @param length
	 * @return string */
	public static final String padString(final String string, final char padder, final int length) {

		final int diff = length - string.length();
		switch (diff) {
			case 1 : {
				return new StringBuilder(length).append(padder).append(string).toString();
			}
			case 2 : {
				return new StringBuilder(length).append(padder).append(padder).append(string).toString();
			}
			case 3 : {
				return new StringBuilder(length).append(padder).append(padder).append(padder).append(string).toString();
			}
			case 4 : {
				return new StringBuilder(length).append(padder).append(padder).append(padder).append(padder).append(string).toString();
			}
			default : {
				if (diff <= 0) {
					return string;
				}
				if (diff > 5) {
					final StringBuilder result = new StringBuilder(length);
					for (int i = 0; i < diff; ++i) {
						result.append(padder);
					}
					result.append(string);
					return result.toString();
				}
				return new StringBuilder(length).append(padder).append(padder).append(padder).append(padder).append(padder).append(string).toString();
			}
		}
	}

	/** @param source
	 * @return string */
	public static final String stringCode(final CharSequence source) {

		return Integer.toString(source.length(), 36) + '.' + Integer.toString(source.hashCode(), 36);
	}

	/** @param source
	 * @return string */
	public static final String stringCode(final String source) {

		return Integer.toString(source.length(), 36) + '.' + Integer.toString(source.hashCode(), 36);
	}

	/** Converts separated string to a String Array
	 * <p>
	 * Returns maximum lastCount ocurances (Array length <= LastCount)
	 *
	 * @param str
	 * @param divider
	 * @param lastCount
	 * @return string array */
	public static final String[] toStringArray(final String str, final String divider, final int lastCount) {

		final List<String> l = new ArrayList<>();
		final int t_length = divider.length();
		if (t_length == 1) {
			for (final StringTokenizer st = new StringTokenizer(str, divider); st.hasMoreTokens();) {
				l.add(st.nextToken().trim());
			}
		} else {
			int start = 0;
			int end = 0;
			for (;;) {
				start = str.indexOf(divider, end);
				if (start == -1) {
					break;
				}
				l.add(str.substring(end, start).trim());
				end = start + t_length;
			}
			if (end < str.length()) {
				l.add(str.substring(end));
			}
		}
		final int size = l.size();
		return size > lastCount
			? l.subList(size - lastCount, size).toArray(new String[lastCount])
			: l.toArray(new String[size]);
	}

	/** @param message
	 * @return string */
	public static final String transliterate(final String message) {

		final int length = message.length();
		final StringBuilder result = new StringBuilder(length * 2);
		for (int i = 0; i < length; ++i) {
			final char c = message.charAt(i);
			if (c < 16) {
				result.append((int) c);
			} else //
			if (c >= 128) {
				final String s = Text.TRANSLIT_XLAT.get(Character.valueOf(c));
				if (s == null) {
					result.append(c & 0x7F);
				} else {
					result.append(s);
				}
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	/** @param result
	 * @param message
	 * @return string builder */
	public static final StringBuilder transliterate(final StringBuilder result, final String message) {

		final int length = message.length();
		for (int i = 0; i < length; ++i) {
			final char c = message.charAt(i);
			if (c < 16) {
				result.append((int) c);
			} else //
			if (c >= 128) {
				final String s = Text.TRANSLIT_XLAT.get(Character.valueOf(c));
				if (s == null) {
					result.append(c & 0x7F);
				} else {
					result.append(s);
				}
			} else {
				result.append(c);
			}
		}
		return result;
	}

	private Text() {

		// empty
	}
}
