/*
 * Created on 16.10.2004 Window - Preferences - Java - Code Style - Code
 * Templates
 */
package ru.myx.sapi;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseDate;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseMapEditable;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.ExecThrown;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.help.Html;
import ru.myx.ae3.help.QueryString;
import ru.myx.ae3.help.Text;
import ru.myx.ae3.indexing.ExtractorPlainVariant;
import ru.myx.ae3.reflect.ReflectionHidden;
import ru.myx.plist.Plist;

/** @author myx
 *
 *
 *         TODO: real impl with java types to help.Format (or help.Convert), here special
 *         ExecProcess/BaseObject methods for faster scripting (reflection)
 *
 *         Window - Preferences - Java - Code Style - Code Templates */
public class FormatSAPI {
	
	static final char[] CHARS_BASE58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
	
	/** Formats bytes as base27, more compact and readable than base16/hex
	 *
	 * @param bytes
	 * @return */
	public static final CharSequence binaryAsBase27(final byte[] bytes) {
		
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		BigInteger integer = new BigInteger(1, bytes);
		final BigInteger base = BigInteger.valueOf(27);
		final StringBuilder result = new StringBuilder();
		while (integer.bitLength() > 0) {
			result.append(RandomSAPI.MIXED_27_EASY.charAt(integer.remainder(base).intValueExact()));
			integer = integer.divide(base);
		}
		return result.toString();
	}
	
	/** Formats bytes as base27, more compact and readable than base16/hex
	 *
	 * @param copier
	 * @return */
	public static final CharSequence binaryAsBase27(final TransferCopier copier) {
		
		if (copier == null || copier.length() == 0) {
			return "";
		}
		return FormatSAPI.binaryAsBase27(copier.nextDirectArray());
	}
	
	/** Formats bytes as base36, more compact and less readable than base16/hex
	 *
	 * @param any
	 * @return */
	public static final CharSequence binaryAsBase36(final Object any) {
		
		final BigInteger integer;
		if (any instanceof byte[]) {
			integer = new BigInteger(1, (byte[]) any);
		} else//
		if (any instanceof TransferCopier) {
			integer = new BigInteger(1, ((TransferCopier) any).nextDirectArray());
		} else //
		if (any == null) {
			return "";
		} else {
			throw new IllegalArgumentException("class: " + any.getClass().getName());
		}
		return integer.toString(36);
	}
	
	/** Formats bytes as base58, more compact and less readable than base16/hex
	 *
	 * @param any
	 * @return */
	public static final CharSequence binaryAsBase58(final Object any) {
		
		byte[] bytes;
		if (any instanceof byte[]) {
			bytes = (byte[]) any;
		} else//
		if (any instanceof TransferCopier) {
			bytes = ((TransferCopier) any).nextDirectArray();
		} else //
		if (any == null) {
			return "";
		} else {
			throw new IllegalArgumentException("class: " + any.getClass().getName());
		}
		if (bytes.length == 0) {
			return "";
		}
		
		final char[] buffer = new char[bytes.length * 2];
		
		// Count leading zeros.
		int zeros = 0;
		while (zeros < bytes.length && bytes[zeros] == 0) {
			++zeros;
		}
		bytes = Arrays.copyOf(bytes, bytes.length); // since we modify it in-place
		int outputStart = buffer.length;
		
		for (int inputStart = zeros; inputStart < bytes.length;) {
			int remainder = 0;
			for (int i = inputStart; i < bytes.length; i++) {
				final int digit = bytes[i] & 0xFF;
				final int temp = remainder * 256 + digit;
				bytes[i] = (byte) (temp / 58);
				remainder = temp % 58;
			}
			buffer[--outputStart] = FormatSAPI.CHARS_BASE58[(byte) remainder];
			if (bytes[inputStart] == 0) {
				++inputStart; // optimization - skip leading zeros
			}
		}
		// Preserve exactly as many leading encoded zeros in output as there were leading zeros in
		// input.
		while (outputStart < buffer.length && buffer[outputStart] == '1') {
			++outputStart;
		}
		while (--zeros >= 0) {
			buffer[--outputStart] = '1';
		}
		return new String(buffer, outputStart, buffer.length - outputStart);
	}
	
	/** Formats bytes as base64, useful for formatting md5 checksums, etc.
	 *
	 * @param bytes
	 * @return */
	public static final CharSequence binaryAsBase64(final byte[] bytes) {
		
		return bytes == null
			? ""
			: new String(Base64.getEncoder().withoutPadding().encode(bytes), Engine.CHARSET_ASCII);
	}
	
	/** Formats bytes as base64, useful for formatting md5 checksums, etc.
	 *
	 * @param binary
	 * @return */
	public static final CharSequence binaryAsBase64(final TransferCopier binary) {
		
		return binary == null
			? ""
			: binary.toStringBase64();
	}
	
	/** Formats bytes as hex, useful for formatting md5 checksums, etc.
	 *
	 * @param bytes
	 * @return */
	public static final CharSequence binaryAsHex(final byte[] bytes) {
		
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		final char[] result = new char[bytes.length << 1];
		int target = 0;
		int source = 0;
		for (int i = bytes.length - 1; i >= 0; --i) {
			final byte current = bytes[source++];
			{
				final int hb = (current & 0xF0) >> 4;
				result[target++] = (char) (hb <= 9
					? '0' + hb
					: 'a' + (hb - 10));
			}
			{
				final int hb = (current & 0x0F) >> 0;
				result[target++] = (char) (hb <= 9
					? '0' + hb
					: 'a' + (hb - 10));
			}
		}
		return new String(result, 0, target);
	}
	
	/** Formats binary value as hex, useful for formatting md5 checksums, etc.
	 *
	 * @param binary
	 * @return */
	public static final CharSequence binaryAsHex(final TransferCopier binary) {
		
		if (binary == null) {
			return "";
		}
		final long lengthLong = binary.length();
		if (lengthLong > Integer.MAX_VALUE / 2) {
			throw new UnsupportedOperationException("binary is too long (" + lengthLong + ") to be represented in HEX (Base16)");
		}
		final int length = (int) lengthLong;
		if (length == 0) {
			return "";
		}
		final TransferBuffer buffer = binary.nextCopy();
		final char[] result = new char[length << 1];
		int target = 0;
		for (int i = length - 1; i >= 0; --i) {
			final int current = buffer.next();
			{
				final int hb = (current & 0xF0) >> 4;
				result[target++] = (char) (hb <= 9
					? '0' + hb
					: 'a' + (hb - 10));
			}
			{
				final int hb = (current & 0x0F) >> 0;
				result[target++] = (char) (hb <= 9
					? '0' + hb
					: 'a' + (hb - 10));
			}
		}
		return new String(result, 0, target);
	}
	
	/** @param bytes
	 * @param offset
	 * @return */
	public static final CharSequence binaryAsInetAddress4(final byte[] bytes, final int offset) {
		
		int o = offset;
		return (bytes[o++] & 0xFF) + "." + (bytes[o++] & 0xFF) + "." + (bytes[o++] & 0xFF) + "." + (bytes[o++] & 0xFF);
	}
	
	/** @param bytes
	 * @param offset
	 * @return */
	public static final CharSequence binaryAsInetAddress6(final byte[] bytes, final int offset) {
		
		int o = offset;
		return "[" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ ":" + //
				Integer.toHexString(((bytes[o++] & 0xFF) << 8) + ((bytes[o++] & 0xFF) << 0)) //
				+ "]";
	}
	
	/** @param size
	 * @return */
	public static final String bytesCompact(final double size) {
		
		return Format.Compact.toBytes(size);
	}
	
	/** @param size
	 * @return */
	public static final String bytesExact(final long size) {
		
		return Format.Exact.toBytes(size);
	}
	
	/** @param size
	 * @return */
	public static final String bytesRound(final long size) {
		
		return Format.Round.toBytes(size);
	}
	
	/** @param items
	 * @param relaxed
	 * @return */
	public static final String csvFragment(final BaseList<?> items, final boolean relaxed) {
		
		if (items == null || items.isEmpty()) {
			return "";
		}
		final StringBuilder builder = new StringBuilder();
		for (final Iterator<? extends BaseObject> i = items.baseIterator(); i.hasNext();) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			final BaseObject o = i.next();
			if (relaxed) {
				if (o instanceof Date) {
					builder.append(FormatSAPI.dateISO(o));
					continue;
				}
				if (o instanceof Number) {
					final Number n = (Number) o;
					final long l = n.longValue();
					final double d = n.doubleValue();
					if (l == d) {
						builder.append(Base.toString(l));
					} else {
						builder.append(Base.toString(d));
					}
					continue;
				}
			}
			FormatSAPI.csvStringFragmentImpl(
					String.valueOf(o), //
					builder.append('\"')//
			).append('\"');
			continue;
		}
		return builder.toString();
	}
	
	/** @param string
	 * @return string */
	public static final String csvString(final String string) {
		
		return string == null
			? "\"\""
			: FormatSAPI.csvStringFragmentImpl(
					string, //
					new StringBuilder(2 + (int) (string.length() * 1.1))//
							.append('\"')//
			)//
					.append('\"')//
					.toString();
	}
	
	/** @param string
	 * @return string */
	public static final String csvStringFragment(final String string) {
		
		return string == null
			? ""
			: FormatSAPI.csvStringFragmentImpl(
					string, //
					new StringBuilder((int) (string.length() * 1.1)))//
					.toString();
	}
	
	private static final StringBuilder csvStringFragmentImpl(final String string, final StringBuilder builder) {
		
		if (string == null) {
			return builder;
		}
		final int length = string.length();
		for (int i = 0; i < length; ++i) {
			final char c = string.charAt(i);
			if (c == '\"') {
				builder.append(c).append(c);
			} else {
				builder.append(c);
			}
		}
		return builder;
	}
	
	/** @param object
	 * @return string */
	public static final String date(final Object object) {
		
		return FormatSAPI.date(object, null);
	}
	
	/** @param object
	 * @param format
	 * @return string */
	public static final String date(final Object object, final String format) {
		
		final SimpleDateFormat formatter = new SimpleDateFormat(
				format == null
					? "yyyy-MM-dd HH:mm:ss"
					: format);
		if (object instanceof Date) {
			return formatter.format((Date) object);
		}
		return formatter.format(new Date(Convert.Any.toLong(object, 0L)));
	}
	
	/** like: 2012-09-19T05:50:38.161Z
	 *
	 * @param object
	 * @return string */
	public static final String dateISO(final Object object) {
		
		if (object instanceof Date) {
			return Format.Ecma.date((Date) object);
		}
		return Format.Ecma.date(Convert.Any.toLong(object, 0L));
	}
	
	/** @param object
	 * @param format
	 * @return string */
	public static final String dateUTC(final Object object, final String format) {
		
		final SimpleDateFormat formatter = new SimpleDateFormat(
				format == null
					? "yyyy-MM-dd HH:mm:ss"
					: format);
		formatter.setTimeZone(Engine.TIMEZONE_GMT);
		if (object instanceof Date) {
			return formatter.format((Date) object);
		}
		return formatter.format(new Date(Convert.Any.toLong(object, 0L)));
	}
	
	/** @param number
	 * @return */
	public static final String decimalCompact(final double number) {
		
		return Format.Compact.toDecimal(number);
	}
	
	/** @param number
	 * @return */
	public static final String decimalRounded(final double number) {
		
		return Format.Round.toDecimal(number);
	}
	
	/** @param o
	 * @return string */
	public static final String enhanceHtml(final String o) {
		
		return o == null
			? null
			: Html.enhanceHtml(o, null);
	}
	
	/** @param o
	 * @param hrefAttributes
	 * @return string */
	public static final String enhanceHtml(final String o, final String hrefAttributes) {
		
		return o == null
			? null
			: Html.enhanceHtml(o, hrefAttributes);
	}
	
	/** Converts Base16 representation intor Base27
	 *
	 * @param hex
	 * @return */
	public static final CharSequence hexAsBase27(final CharSequence hex) {
		
		if (hex == null) {
			return "";
		}
		// System.err.println( ">>> >>>> hexAsBinary: hex=" + hex );
		final int hexLength = hex.length();
		final int binaryLength = hexLength / 2;
		final byte[] bytes = new byte[binaryLength];
		for (int i = binaryLength, j = hexLength; j > 0 && i > 0;) {
			final char l = hex.charAt(--j);
			final char h = hex.charAt(--j);
			final int L = 0x0F & (l >= 'a'
				? l - 'a' + 10
				: l >= 'A'
					? l - 'A' + 10
					: l - '0');
			final int H = 0x0F & (h >= 'a'
				? h - 'a' + 10
				: h >= 'A'
					? h - 'A' + 10
					: h - '0');
			bytes[--i] = (byte) ((H << 4) + L);
		}
		return FormatSAPI.binaryAsBase27(bytes);
	}
	
	/** Converts Base16 representation into Base36
	 *
	 * @param hex
	 * @return */
	public static final String hexAsBase36(final CharSequence hex) {
		
		if (hex == null) {
			return "";
		}
		// System.err.println( ">>> >>>> hexAsBinary: hex=" + hex );
		final int hexLength = hex.length();
		final int binaryLength = hexLength / 2;
		final byte[] bytes = new byte[binaryLength];
		for (int i = binaryLength, j = hexLength; j > 0 && i > 0;) {
			final char l = hex.charAt(--j);
			final char h = hex.charAt(--j);
			final int L = 0x0F & (l >= 'a'
				? l - 'a' + 10
				: l >= 'A'
					? l - 'A' + 10
					: l - '0');
			final int H = 0x0F & (h >= 'a'
				? h - 'a' + 10
				: h >= 'A'
					? h - 'A' + 10
					: h - '0');
			bytes[--i] = (byte) ((H << 4) + L);
		}
		return new BigInteger(1, bytes).toString(36);
	}
	
	/** Formats bytes as base64, useful for formatting md5 checksums, etc.
	 *
	 * @param hex
	 * @return */
	public static final CharSequence hexAsBase64(final CharSequence hex) {
		
		if (hex == null) {
			return null;
		}
		final int hexLength = hex.length();
		final int binaryLength = hexLength / 2;
		final byte[] bytes = new byte[binaryLength];
		for (int i = binaryLength, j = hexLength; j > 0 && i > 0;) {
			final char l = hex.charAt(--j);
			final char h = hex.charAt(--j);
			final int L = 0x0F & (l >= 'a'
				? l - 'a' + 10
				: l >= 'A'
					? l - 'A' + 10
					: l - '0');
			final int H = 0x0F & (h >= 'a'
				? h - 'a' + 10
				: h >= 'A'
					? h - 'A' + 10
					: h - '0');
			bytes[--i] = (byte) ((H << 4) + L);
		}
		return new String(Base64.getEncoder().withoutPadding().encode(bytes), Engine.CHARSET_ASCII);
	}
	
	/** Formats binary value as hex, useful for formatting md5 checksums, etc.
	 *
	 * @param hex
	 * @return */
	public static final TransferCopier hexAsBinary(final CharSequence hex) {
		
		if (hex == null) {
			return null;
		}
		// System.err.println( ">>> >>>> hexAsBinary: hex=" + hex );
		final int hexLength = hex.length();
		final int binaryLength = hexLength / 2;
		final byte[] bytes = new byte[binaryLength];
		for (int i = binaryLength, j = hexLength; j > 0 && i > 0;) {
			final char l = hex.charAt(--j);
			final char h = hex.charAt(--j);
			final int L = 0x0F & (l >= 'a'
				? l - 'a' + 10
				: l >= 'A'
					? l - 'A' + 10
					: l - '0');
			final int H = 0x0F & (h >= 'a'
				? h - 'a' + 10
				: h >= 'A'
					? h - 'A' + 10
					: h - '0');
			bytes[--i] = (byte) ((H << 4) + L);
		}
		return Transfer.wrapCopier(bytes);
	}
	
	/** @param o
	 * @return string */
	public static final String htmlAsPlainText(final String o) {
		
		return o == null
			? null
			: Html.cleanHtml(o);
	}
	
	/** @param s
	 * @return */
	public static final boolean isValidDnsHostName(final CharSequence s) {
		
		final int l = s.length();
		if (l < 1) {
			return false;
		}
		if (l > 63) {
			return false;
		}
		char c;
		int i = 0;
		boolean start = true;
		for (; i < l; ++i) {
			c = s.charAt(i);
			if (start) {
				if (!FormatSAPI.isValidDnsLabelStart(c)) {
					return false;
				}
				start = false;
				continue;
			}
			if (c == '.') {
				start = true;
				continue;
			}
			if (!FormatSAPI.isValidDnsLabelPart(c)) {
				return false;
			}
		}
		return true;
	}
	
	/** @param s
	 * @return */
	public static final boolean isValidDnsLabel(final CharSequence s) {
		
		final int l = s.length();
		if (l < 1) {
			return false;
		}
		if (l > 63) {
			return false;
		}
		char c = s.charAt(0);
		if (!FormatSAPI.isValidDnsLabelStart(c)) {
			return false;
		}
		int i = 1;
		for (; i < l; ++i) {
			c = s.charAt(i);
			if (!FormatSAPI.isValidDnsLabelPart(c)) {
				return false;
			}
		}
		return true;
	}
	
	/** @param c
	 * @return */
	public static final boolean isValidDnsLabelPart(final char c) {
		
		if (c == '.') {
			return false;
		}
		if (c <= 32 || c > 127) {
			return false;
		}
		if (Character.toLowerCase(c) != c || !(Character.isLetterOrDigit(c) || c == '-')) {
			return false;
		}
		return true;
	}
	
	/** @param c
	 * @return */
	public static final boolean isValidDnsLabelStart(final char c) {
		
		if (c == '.') {
			return false;
		}
		if (c <= 32 || c > 127) {
			return false;
		}
		if (Character.toLowerCase(c) != c || !Character.isLetterOrDigit(c)) {
			return false;
		}
		return true;
	}
	
	/** @param s
	 * @return */
	public static final boolean isValidXmlCharacterData(final CharSequence s) {
		
		return Format.Xml.isValidCharacterData(s);
	}
	
	/** @param s
	 * @return */
	public static final boolean isValidXmlName(final CharSequence s) {
		
		return Format.Xml.isValidName(s);
	}
	
	/** @param s
	 * @return */
	public static final boolean isValidXmlValue(final CharSequence s) {
		
		return Format.Xml.isValidAttributeValue(s);
	}
	
	/** @param object
	 * @return */
	public static final String jsDescribe(final BaseObject object) {
		
		return Format.Describe.toEcmaSource(object, "");
	}
	
	/** Creates special object that acts like a string object, but when serialized to JS (using
	 * Format.jsObject, for example) is not escaped like string should but in-lined as an
	 * expression.
	 *
	 * @param string
	 * @return */
	public static final BaseString<?> jsInlineExpressionString(final CharSequence string) {
		
		return new InlineEvalExpressionImpl(string);
	}
	
	/** @param object
	 * @return */
	public static final String jsObject(final BaseObject object) {
		
		return Ecma.toEcmaSourceCompact(object);
	}
	
	/** @param object
	 * @return */
	public static final String jsObjectReadable(final BaseObject object) {
		
		return Ecma.toEcmaSource(object, true, 0);
	}
	
	/** @param string
	 * @return string */
	public static final String jsString(final CharSequence string) {
		
		return Format.Ecma.string(string);
	}
	
	/** @param string
	 * @return string */
	public static final String jsStringFragment(final CharSequence string) {
		
		if (string == null) {
			return "";
		}
		final int length = string.length();
		final StringBuilder builder = new StringBuilder((int) (length * 1.1));
		char prev = 0;
		for (int i = 0; i < length; ++i) {
			final char c = string.charAt(i);
			switch (c) {
				case 0 :
					builder.append("\\x00");
					continue;
				case 0xFDD0 :
					builder.append("\\uFDD0");
					continue;
				case 0xFDEF :
					builder.append("\\uFDEF");
					continue;
				case 0xFFFE :
					builder.append("\\uFFFE");
					continue;
				case 0xFFFF :
					builder.append("\\uFFFF");
					continue;
				case '\t' :
					builder.append('\\').append('t');
					continue;
				case '\n' :
					builder.append('\\').append('n');
					continue;
				case '\r' :
					builder.append('\\').append('r');
					continue;
				case '\b' :
					builder.append('\\').append('b');
					continue;
				case '\f' :
					builder.append('\\').append('f');
					continue;
				case '\'' :
				case '"' :
				case '\\' :
					builder.append('\\').append(c);
					continue;
				/** have escape </script> like </scr\x69pt> */
				case '<' :
					prev = c;
					builder.append(c);
					continue;
				case '/' :
					if (prev == '<') {
						prev = c;
						builder.append(c);
						continue;
					}
					break;
				case 'S' :
				case 's' :
					if (prev == '/') {
						prev = c;
						builder.append(c);
						continue;
					}
					break;
				case 'C' :
				case 'c' :
					if (prev == 's') {
						prev = c;
						builder.append(c);
						continue;
					}
					break;
				case 'R' :
				case 'r' :
					if (prev == 'c') {
						prev = c;
						builder.append(c);
						continue;
					}
					break;
				case 'I' :
				case 'i' :
					if (prev == 'r' && length - i >= 3 && ('p' == string.charAt(i + 1) || 'P' == string.charAt(i + 1))
							&& ('t' == string.charAt(i + 2) || 'T' == string.charAt(i + 2)) && '>' == string.charAt(i + 3)) {
						builder.append('\\');
						builder.append('x');
						builder.append(
								c == 'i'
									? '6' // i
									: '4'); // I
						builder.append('9');
						prev = 0;
						continue;
					}
					//$FALL-THROUGH$
				default :
			}
			prev = 0;
			if (c < 32) {
				if (c < 16) {
					builder.append("\\x0").append(Integer.toString(c, 16));
					continue;
				}
				builder.append("\\x").append(Integer.toString(c, 16));
				continue;
			}
			builder.append(c);
		}
		return builder.toString();
	}
	
	/** @param number
	 * @return */
	public static final String periodCompact(final double number) {
		
		return Format.Compact.toPeriod(number);
	}
	
	/** @param o
	 * @return string */
	public static final String plainTextAsHtml(final String o) {
		
		return o == null
			? null
			: Html.fromPlainText(o, null);
	}
	
	/** @param o
	 * @param hrefAttributes
	 * @return string */
	public static final String plainTextAsHtml(final String o, final String hrefAttributes) {
		
		return o == null
			? null
			: Html.fromPlainText(o, hrefAttributes);
	}
	
	/** @param object
	 * @return */
	public static final String plainTextDescribe(final BaseObject object) {
		
		try {
			if (object instanceof Throwable) {
				return FormatSAPI.plainTextDescribe((Throwable) object);
			}
			return object.toString();
		} catch (final Throwable e) {
			return "[ERROR: " + e.getClass().getName() + " " + e.getMessage() + "]";
		}
	}
	
	/** @param object
	 * @return */
	public static final String plainTextDescribe(final Object object) {
		
		try {
			if (object instanceof Throwable) {
				return FormatSAPI.plainTextDescribe((Throwable) object);
			}
			return object.toString();
		} catch (final Throwable e) {
			return "[ERROR: " + e.getClass().getName() + " " + e.getMessage() + "]";
		}
	}
	
	/** @param t
	 * @return */
	public static final String plainTextDescribe(final Throwable t) {
		
		try {
			return "[" + t.getClass().getName() + " " + FormatSAPI.jsString(t.getMessage()) + "]";
		} catch (final Throwable e) {
			return "[ERROR: " + e.getClass().getName() + " " + e.getMessage() + "]";
		}
	}
	
	/** @param object
	 * @return */
	public static final String plistObject(final BaseObject object) {
		
		return Plist.toPlistSource(object);
	}
	
	/** @param object
	 * @return */
	public static final String queryStringParameters(final BaseObject object) {
		
		return QueryString.toQueryString(object, Engine.CHARSET_UTF8);
	}
	
	/** @param base
	 * @param change
	 * @return */
	public static final String queryStringParameters(final BaseObject base, final BaseObject change) {
		
		if (change == null || change == BaseObject.UNDEFINED) {
			return QueryString.toQueryString(base, Engine.CHARSET_UTF8);
		}
		final BaseMapEditable result = BaseObject.createObject(base);
		result.baseDefineImportAllEnumerable(change);
		return QueryString.toQueryString(result, Engine.CHARSET_UTF8);
	}
	
	/** @param csvLine
	 * @return list */
	public static final BaseList<?> splitCsvLine(final String csvLine) {
		
		return csvLine == null
			? null
			: FormatSAPI.splitCsvLine(csvLine, ',');
	}
	
	/** @param csvLine
	 * @param splitter
	 * @return list */
	public static final BaseList<?> splitCsvLine(final String csvLine, final char splitter) {
		
		if (csvLine == null) {
			return null;
		}
		final BaseList<CharSequence> result = BaseList.create();
		final StringBuilder buffer = new StringBuilder();
		final String splitterString = Character.toString(splitter);
		boolean inString = false;
		boolean lastQuote = false;
		for (final CharTokenizer st = new CharTokenizer(csvLine, "\"" + splitter, true); st.hasMoreTokens();) {
			final String current = st.nextToken();
			if (current.equals(splitterString) && !inString) {
				result.baseDefaultPush(Base.forString(buffer.toString().trim()));
				buffer.setLength(0);
				lastQuote = false;
			} else if (current.equals("\"")) {
				if (!inString) {
					if (lastQuote) {
						buffer.append('"');
					}
					lastQuote = false;
					inString = true;
				} else {
					lastQuote = true;
					inString = false;
				}
			} else {
				lastQuote = false;
				buffer.append(current);
			}
		}
		result.baseDefaultPush(Base.forString(buffer.toString().trim()));
		return result;
	}
	
	/** @param csvLine
	 * @param splitter
	 * @return list */
	@ReflectionHidden
	public static final BaseList<?> splitCsvLine(final String csvLine, final String splitter) {
		
		return FormatSAPI.splitCsvLine(csvLine, splitter.charAt(0));
	}
	
	/** @param o
	 * @return list */
	public static final BaseList<?> splitLines(final Object o) {
		
		if (o == null) {
			return null;
		}
		final BaseList<CharSequence> result = BaseList.create();
		final StringBuilder buffer = new StringBuilder();
		boolean outString = true;
		for (final CharTokenizer st = new CharTokenizer(o.toString(), "\r\n\"", true); st.hasMoreTokens();) {
			final String current = st.nextToken();
			if (current.equals("\r")) {
				continue;
			}
			if (outString && current.equals("\n")) {
				final String line = buffer.toString().trim();
				if (line.length() > 0) {
					result.baseDefaultPush(Base.forString(line));
				}
				buffer.setLength(0);
			} else if (current.equals("\"")) {
				buffer.append('"');
				outString = !outString;
			} else {
				buffer.append(current);
			}
		}
		final String least = buffer.toString().trim();
		if (least.length() > 0) {
			result.baseDefaultPush(Base.forString(least));
		}
		return result;
	}
	
	/** @param o
	 * @return list */
	public static final BaseList<?> splitLinesIgnoreQuotes(final Object o) {
		
		if (o == null) {
			return null;
		}
		final BaseList<CharSequence> result = BaseList.create();
		final StringBuilder buffer = new StringBuilder();
		for (final CharTokenizer st = new CharTokenizer(o.toString(), "\r\n", true); st.hasMoreTokens();) {
			final String current = st.nextToken();
			if (current.equals("\r")) {
				continue;
			}
			if (current.equals("\n")) {
				final String line = buffer.toString().trim();
				if (line.length() > 0) {
					result.baseDefaultPush(Base.forString(line));
				}
				buffer.setLength(0);
			} else {
				buffer.append(current);
			}
		}
		final String least = buffer.toString().trim();
		if (least.length() > 0) {
			result.baseDefaultPush(Base.forString(least));
		}
		return result;
	}
	
	/** @param format
	 * @param args
	 * @return */
	public static final String sprintf(final String format, final Object... args) {
		
		return String.format(format, args);
	}
	
	/** @param string
	 * @return string */
	public static final String sqlString(final String string) {
		
		return string == null
			? "''"
			: FormatSAPI.sqlStringFragmentImpl(
					string, //
					new StringBuilder(2 + (int) (string.length() * 1.1))//
							.append('\'')//
			)//
					.append('\'')//
					.toString();
	}
	
	/** @param string
	 * @return string */
	public static final String sqlStringFragment(final String string) {
		
		return string == null
			? ""
			: FormatSAPI.sqlStringFragmentImpl(
					string, //
					new StringBuilder((int) (string.length() * 1.1)))//
					.toString();
	}
	
	private static final StringBuilder sqlStringFragmentImpl(final String string, final StringBuilder builder) {
		
		if (string == null) {
			return builder;
		}
		final int length = string.length();
		for (int i = 0; i < length; ++i) {
			final char c = string.charAt(i);
			if (c == '\'') {
				builder.append(c).append(c);
			} else {
				builder.append(c);
			}
		}
		return builder;
	}
	
	/** @param text
	 * @return */
	public static final byte[] stringAsAsciiBytes(final String text) {
		
		return text == null
			? new byte[0]
			: text.getBytes(Engine.CHARSET_ASCII);
	}
	
	/** Formats bytes as base64, useful for formatting md5 checksums, etc.
	 *
	 * @param text
	 * @return */
	public static final CharSequence stringAsUtf8Base64(final String text) {
		
		return text == null
			? ""
			: new String(Base64.getEncoder().withoutPadding().encode(text.getBytes(Engine.CHARSET_UTF8)), Engine.CHARSET_ASCII);
	}
	
	/** @param text
	 * @return */
	public static final byte[] stringAsUtf8Bytes(final String text) {
		
		return text == null
			? new byte[0]
			: text.getBytes(Engine.CHARSET_UTF8);
	}
	
	/** Formats bytes as base64, useful for formatting md5 checksums, etc.
	 *
	 * @param base64
	 * @return */
	public static final CharSequence stringFromUtf8Base64(final String base64) {
		
		return base64 == null
			? ""
			: new String(Base64.getDecoder().decode(base64.getBytes(Engine.CHARSET_ASCII)), Engine.CHARSET_UTF8);
	}
	
	/** @param o
	 * @return string */
	public static final String throwableAsPlainText(final Throwable o) {
		
		if (o instanceof ExecThrown) {
			return Base.forUnknown(((ExecThrown) o).getThrownValue()).baseToJavaString();
		}
		return Format.Throwable.toText(o).replaceAll("ru\\.myx\\.ae3", "ae3");
	}
	
	/** Parses some text forms of dates into date object or returns undefined.
	 *
	 * @param anyDate
	 * @return */
	public static final BaseDate toDate(final BaseObject anyDate) {
		
		if (anyDate instanceof BaseDate) {
			return (BaseDate) anyDate;
		}
		if (anyDate == null || anyDate == BaseObject.UNDEFINED || anyDate == BaseObject.NULL) {
			return null;
		}
		if (anyDate instanceof Number) {
			return new BaseDate(((Number) anyDate).longValue());
		}
		final long date = Format.Ecma.dateParse(anyDate.toString());
		if (date == -1) {
			return null;
		}
		return new BaseDate(date);
	}
	
	/** @param text
	 * @return */
	public static final long toPeriod(final String text) {
		
		return Convert.Any.toPeriod(text, -1);
	}
	
	/** @param source
	 * @param delimiter
	 * @return string */
	public static final String uniqueWords(final String source, final String delimiter) {
		
		if (source == null) {
			return "";
		}
		final Set<String> words = new TreeSet<>();
		ExtractorPlainVariant.extractContent(words, source);
		return Text.join(words, delimiter);
	}
	
	/** @param object
	 * @return string */
	public static final String utcDate(final Object object) {
		
		return FormatSAPI.utcDate(object, null);
	}
	
	/** @param object
	 * @param format
	 * @return string */
	public static final String utcDate(final Object object, final String format) {
		
		final SimpleDateFormat formatter = new SimpleDateFormat(
				format == null
					? "yyyy-MM-dd HH:mm:ss"
					: format);
		formatter.setTimeZone(Engine.TIMEZONE_GMT);
		if (object instanceof Date) {
			return formatter.format((Date) object);
		}
		return formatter.format(new Date(Convert.Any.toLong(object, 0L)));
	}
	
	/** Will ignore names which are not valid XML attribute names, will escape values properly (you
	 * don't need to replace '&amp;' to '&amp;amp;' and such when passing your strings to this
	 * method...).
	 *
	 * @param name
	 * @param value
	 * @return empty string or a space character with valid XML attribute notation concatenated */
	public static final String xmlAttribute(final String name, final String value) {
		
		if (name == null || value == null) {
			return "";
		}
		try {
			return Format.Xml.xmlAttributeImpl(name, value, new StringBuilder()).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** The part of XML attribute text.
	 *
	 * Example: <code>
	 * 	&lt;div title="title: &lt;%= Format.xmlAttributeFragment( this.title ) %>" >
	 * 		&lt;%= {
	 * 			layout	: "link",
	 * 			title	: this.title,
	 * 			href	: "/!/skin/skin-acmcms-info/jdoc-ae3.sdk/ru/myx/sapi/FormatSAPI.html"
	 * 		} %>
	 * 	&lt;/div>
	 * </code>
	 *
	 * @param string
	 * @return */
	public static final String xmlAttributeFragment(final String string) {
		
		if (string == null) {
			return "";
		}
		try {
			return Format.Xml.xmlAttributeFragmentImpl(
					string, //
					new StringBuilder((int) (string.length() * 1.2))) //
					.toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Makes a string with ' name="value"' parts for every non-Object.prototype's mapping.
	 *
	 * Values are escaped according to XML specification rules. Illegal attribute names are silently
	 * skipped.
	 *
	 * @param object
	 * @return */
	public static final String xmlAttributes(final BaseObject object) {
		
		try {
			return FormatSAPI.xmlAttributesImpl(object, new StringBuilder(64)).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final StringBuilder xmlAttributesImpl(final BaseObject object, final StringBuilder builder) throws IOException {
		
		for (final Iterator<String> iterator = Base.keys(object, BaseObject.PROTOTYPE); iterator.hasNext();) {
			final String key = iterator.next();
			Format.Xml.xmlAttributeImpl(key, object.baseGet(key, null), builder);
		}
		return builder;
	}
	
	/** The while XML attribute including quotes and text.
	 *
	 * The Date instances are formatted as Format.Ecma.date( x )
	 *
	 * Other objects are formatted as x.toString()
	 *
	 * Example: <code>
	 * 	&lt;div title=&lt;%= Format.xmlAttributeValue( 'Title: ' + this.title ) %> >
	 * 		&lt;%= {
	 * 			layout	: "link",
	 * 			title	: this.title,
	 * 			href	: "/!/skin/skin-acmcms-info/jdoc-ae3.sdk/ru/myx/sapi/FormatSAPI.html"
	 * 		} %>
	 * 	&lt;/div>
	 * </code>
	 *
	 * @param string
	 * @return */
	public static final String xmlAttributeValue(final Object string) {
		
		if (string == null) {
			return "\"\"";
		}
		if (string instanceof Date) {
			return FormatSAPI.xmlAttributeValue(Format.Ecma.date((Date) string));
		}
		final String s = string.toString();
		if (s == null) {
			return "\"\"";
		}
		final int length = s.length();
		if (length == 0) {
			return "\"\"";
		}
		final StringBuilder target = new StringBuilder((int) (2 + length * 1.2));
		target.append('"');
		try {
			Format.Xml.xmlAttributeFragmentImpl(s, target);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		target.append('"');
		return target.toString();
	}
	
	/** Makes an xml element with given name, attributes and child elements. This version support
	 * child elements as well as attributes:
	 * <ul>
	 * <li>for a value to get into attributes it must be a primitive value;</li>
	 * <li>for a value to get into child elements it must be a non-primitive value.</li>
	 * </ul>
	 *
	 * Supported extras (the value of an attribute or nested value):
	 * <ul>
	 * <li>{ layout : 'xml-fragment', content : uncheckedXmlFragment }</li>
	 * <li>{ layout : 'xml', content : uncheckedXml }</li>
	 * <li>{ layout : 'final', type : 'text/xml', content : uncheckedXml }</li>
	 * </ul>
	 *
	 * @param name
	 * @param attributes
	 * @return */
	public static final String xmlElement(final String name, final BaseObject attributes) {
		
		assert Format.Xml.isValidName(name) : "invalid element name: " + name;
		try {
			return FormatSAPI.xmlElementImpl(name, attributes, new StringBuilder(64)).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Makes an xml element with given name and attributes.
	 *
	 * @param name
	 * @param attributes
	 * @param nodeValue
	 * @return */
	public static final String xmlElement(final String name, final BaseObject attributes, final BaseObject nodeValue) {
		
		final StringBuilder builder = new StringBuilder(64);
		assert Format.Xml.isValidName(name) : "invalid element name: " + name;
		try {
			builder.append('<').append(name);
			FormatSAPI.xmlAttributesImpl(attributes, builder);
			if (nodeValue == null || nodeValue.baseValue() == null) {
				return builder.append("/>").toString();
			}
			builder.append('>');
			Format.Xml.xmlNodeValueImpl(nodeValue, builder);
			builder.append("</").append(name).append('>');
			return builder.toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Makes an xml element with given name, attribute and body. For methods like
	 * String.prototype.anchor...
	 *
	 * @param name
	 * @param attributeName
	 * @param attributeValue
	 * @param nodeValue
	 * @return */
	public static final String xmlElement(final String name, final String attributeName, final BaseObject attributeValue, final BaseObject nodeValue) {
		
		final StringBuilder builder = new StringBuilder(128);
		assert Format.Xml.isValidName(name) : "invalid element name: " + name;
		try {
			builder.append('<').append(name);
			Format.Xml.xmlAttributeImpl(attributeName, attributeValue, builder);
			if (nodeValue == null || nodeValue.baseValue() == null) {
				return builder.append("/>").toString();
			}
			builder.append('>');
			Format.Xml.xmlNodeValueImpl(nodeValue, builder);
			builder.append("</").append(name).append('>');
			return builder.toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("javadoc")
	@Deprecated
	public static final String xmlElementAttributes(final BaseObject object) {
		
		try {
			return FormatSAPI.xmlAttributesImpl(object, new StringBuilder()).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final <T extends Appendable> T xmlElementImpl(final String name, final BaseObject attributes, final T result) throws IOException {
		
		if (!Format.Xml.isValidName(name)) {
			return result;
		}
		result.append('<').append(name);
		StringBuilder body = null;
		mapProperties : for (final Iterator<String> iterator = Base.keys(attributes, BaseObject.PROTOTYPE); iterator.hasNext();) {
			final String key = iterator.next();
			final BaseObject value = attributes.baseGet(key, null);
			if (value == null || value == BaseObject.UNDEFINED || value == BaseObject.NULL) {
				continue mapProperties;
			}
			if (value instanceof CharSequence || value.baseIsPrimitive()) {
				Format.Xml.xmlAttributeImpl(key, value, result);
				continue mapProperties;
			}
			if (value instanceof BaseDate) {
				Format.Xml.xmlAttributeImpl(key, ((BaseDate) value).toISOString(), result);
				continue mapProperties;
			}
			{
				final Object base = value.baseValue();
				if (base != value) {
					if (base == null && base == BaseObject.UNDEFINED || base == BaseObject.NULL) {
						continue mapProperties;
					}
					if (base instanceof CharSequence || base instanceof BaseObject && ((BaseObject) base).baseIsPrimitive()) {
						Format.Xml.xmlAttributeImpl(key, base, result);
						continue mapProperties;
					}
					if (base instanceof BaseDate) {
						Format.Xml.xmlAttributeImpl(key, ((BaseDate) base).toISOString(), result);
						continue mapProperties;
					}
				}
			}
			if (body == null) {
				body = new StringBuilder(64);
			}
			{
				final BaseArray array = value.baseArray();
				if (array != null) {
					final int length = array.length();
					arrayElements : for (int i = 0; i < length; ++i) {
						final BaseObject element = array.baseGet(i, null);
						if (element == null || element == BaseObject.UNDEFINED || element == BaseObject.NULL) {
							continue arrayElements;
						}
						if (!Format.Xml.isValidName(key)) {
							continue arrayElements;
						}
						if (element instanceof CharSequence || element.baseIsPrimitive()) {
							body.append('<').append(key).append('>');
							Format.Xml.xmlNodeValueImpl(element, body);
							body.append("</").append(key).append('>');
							continue arrayElements;
						}
						if (element instanceof BaseDate) {
							body.append('<').append(key).append('>');
							Format.Xml.xmlNodeValueImpl(((BaseDate) element).toISOString(), body);
							body.append("</").append(key).append('>');
							continue arrayElements;
						}
						{
							final Object base = element.baseValue();
							if (base != element) {
								if (base == null && base == BaseObject.UNDEFINED || base == BaseObject.NULL) {
									continue arrayElements;
								}
								if (base instanceof CharSequence || base instanceof BaseObject && ((BaseObject) base).baseIsPrimitive()) {
									body.append('<').append(key).append('>');
									Format.Xml.xmlNodeValueImpl(base, body);
									body.append("</").append(key).append('>');
									continue arrayElements;
								}
								if (base instanceof BaseDate) {
									body.append('<').append(key).append('>');
									Format.Xml.xmlNodeValueImpl(((BaseDate) base).toISOString(), body);
									body.append("</").append(key).append('>');
									continue arrayElements;
								}
							}
						}
						FormatSAPI.xmlElementImpl(key, element, body);
					}
					continue mapProperties;
				}
			}
			{
				final String layout = Base.getString(value, "layout", null);
				if (layout != null) {
					if ("xml".equals(layout) || "xml-fragment".equals(layout) || "final".equals(layout) && "text/xml".equals(Base.getString(value, "type", null))) {
						body.append('<').append(key).append('>');
						
						{
							/** NOTE: unchecked XML fragment */
							body.append(value.baseGet("content", BaseString.EMPTY));
						}
						
						body.append("</").append(key).append('>');
						continue mapProperties;
					}
				}
			}
			{
				FormatSAPI.xmlElementImpl(key, value, body);
			}
		}
		if (body == null || body.length() == 0) {
			result.append("/>");
			return result;
		}
		result.append('>').append(body).append("</").append(name).append('>');
		return result;
	}
	
	/** Executes xmlElement zero or more times to build an XML fragment string. No element is
	 * produced if attributes equal to 'undefined'. When attributes is an array, xmlElement will be
	 * called for each item, unless that item is 'undefined' or primitive. For primitive values of
	 * an attribute the element with text content is produced.
	 *
	 * @param name
	 * @param attributes
	 * @return */
	public static final String xmlElements(final String name, final BaseObject attributes) {
		
		assert Format.Xml.isValidName(name) : "invalid element name: " + name;
		if (attributes == null || attributes == BaseObject.UNDEFINED) {
			return "";
		}
		if (attributes instanceof CharSequence || attributes.baseIsPrimitive()) {
			final StringBuilder result = new StringBuilder(64);
			result.append('<').append(name).append('>');
			try {
				Format.Xml.xmlNodeValueImpl(attributes, result);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			result.append("</").append(name).append('>');
			return result.toString();
		}
		{
			final BaseArray array = attributes.baseArray();
			if (array != null) {
				final int count = array.length();
				if (count == 0) {
					return "";
				}
				final StringBuilder result = new StringBuilder(128);
				try {
					for (int i = 0; i < count; ++i) {
						final BaseObject item = array.baseGet(i, BaseObject.UNDEFINED);
						if (item == null || item == BaseObject.UNDEFINED) {
							continue;
						}
						if (item instanceof CharSequence || item.baseIsPrimitive()) {
							result.append('<').append(name).append('>');
							Format.Xml.xmlNodeValueImpl(item, result);
							result.append("</").append(name).append('>');
							continue;
						}
						FormatSAPI.xmlElementImpl(name, item, result);
					}
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
				return result.toString();
			}
		}
		return FormatSAPI.xmlElement(name, attributes);
	}
	
	/** nodeValue - element's body.
	 *
	 * Example: <code>
	 * 	&lt;textarea>
	 * 		&lt;%= Format.xmlNodeValue( this.text ) %>
	 * 	&lt;/textarea>
	 * </code>
	 *
	 *
	 * @param o
	 * @return string */
	public static final String xmlNodeValue(final Object o) {
		
		if (o == null) {
			return "";
		}
		final String s = o.toString();
		final int length = s.length();
		final StringBuilder result = new StringBuilder((int) (length * 1.2));
		try {
			return Format.Xml.xmlNodeValueImpl(s, result).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** @param o
	 * @return string */
	public static final String xmlTextAsHtml(final String o) {
		
		return o == null
			? null
			: Html.fromXmlText(o, null);
	}
	
	/** @param o
	 * @param hrefAttributes
	 * @return string */
	public static final String xmlTextAsHtml(final String o, final String hrefAttributes) {
		
		return o == null
			? null
			: Html.fromXmlText(o, hrefAttributes);
	}
}
