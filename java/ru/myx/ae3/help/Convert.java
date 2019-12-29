/**
 * Created on 10.11.2002
 *
 * myx - barachta */
package ru.myx.ae3.help;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;

/** @author myx
 *
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
public class Convert {
	
	/** Fast - all methods are implemented without any code reuse. */
	public static class Any {
		
		private static final long parseInt(final String s, final int radix) {
			
			int result = 0;
			boolean negative = false;
			int i = 0;
			final int max = s.length();
			int limit;
			int multmin;
			int digit;

			if (max > 0) {
				if (s.charAt(0) == '-') {
					negative = true;
					limit = Integer.MIN_VALUE;
					++i;
				} else {
					limit = -Integer.MAX_VALUE;
				}
				multmin = limit / radix;
				if (i < max) {
					digit = Character.digit(s.charAt(i++), radix);
					if (digit < 0) {
						return Long.MIN_VALUE;
					}
					result = -digit;
				}
				while (i < max) {
					// Accumulating negatively avoids surprises near MAX_VALUE
					digit = Character.digit(s.charAt(i++), radix);
					if (digit < 0) {
						return Long.MIN_VALUE;
					}
					if (result < multmin) {
						return Long.MIN_VALUE;
					}
					result *= radix;
					if (result < limit + digit) {
						return Long.MIN_VALUE;
					}
					result -= digit;
				}
			} else {
				return Long.MIN_VALUE;
			}
			if (negative) {
				if (i > 1) {
					return result;
				} /* Only got "-" */
				return Long.MIN_VALUE;
			}
			return -result;
		}

		private static final double parseLong(final String s, final int radix) throws NumberFormatException {
			
			long result = 0;
			boolean negative = false;
			int i = 0;
			final int max = s.length();
			long limit;
			long multmin;
			int digit;

			if (max > 0) {
				if (s.charAt(0) == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
					++i;
				} else {
					limit = -Long.MAX_VALUE;
				}
				multmin = limit / radix;
				if (i < max) {
					digit = Character.digit(s.charAt(i++), radix);
					if (digit < 0) {
						return Double.NEGATIVE_INFINITY;
					}
					result = -digit;
				}
				while (i < max) {
					// Accumulating negatively avoids surprises near MAX_VALUE
					digit = Character.digit(s.charAt(i++), radix);
					if (digit < 0) {
						return Double.NEGATIVE_INFINITY;
					}
					if (result < multmin) {
						return Double.NEGATIVE_INFINITY;
					}
					result *= radix;
					if (result < limit + digit) {
						return Double.NEGATIVE_INFINITY;
					}
					result -= digit;
				}
			} else {
				return Double.NEGATIVE_INFINITY;
			}
			if (negative) {
				if (i > 1) {
					return result;
				}
				return Double.NEGATIVE_INFINITY;
			}
			return -result;
		}

		/** @param <T>
		 * @param any
		 * @return object */
		@SuppressWarnings("unchecked")
		public final static <T> T toAny(final Object any) {
			
			return (T) any;
		}

		/** CATCH METHOD
		 *
		 * @param any
		 * @param defaultValue
		 * @return boolean */
		public static final boolean toBoolean(final BaseObject any, final boolean defaultValue) {
			
			if (any == null || any == BaseObject.UNDEFINED || any == BaseObject.NULL) {
				return defaultValue;
			}
			return Convert.Any.toInt(
					any,
					defaultValue
						? 1
						: 0) != 0;
		}

		/** = Convert.Any.toInt( any, defaultValue ? 1 : 0) != 0
		 *
		 * @param any
		 * @param defaultValue
		 * @return boolean */
		public static final boolean toBoolean(final Object any, final boolean defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			return Convert.Any.toInt(
					any,
					defaultValue
						? 1
						: 0) != 0;
		}

		/** Safely converts any object to byte. Following steps are performed while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???g???m???k???' where g means multiplied by 1024 in power of
		 * 3, m means multiplied by 1024 in power of 2 and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return byte */
		public static final byte toByte(final Object any, final byte defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).byteValue();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			try {
				return Byte.parseByte(stringual);
			} catch (final NumberFormatException t) {
				// ignore
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				try {
					return Byte.parseByte(lowered.substring(2), 16);
				} catch (final NumberFormatException t) {
					return defaultValue;
				}
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && "yes".equals(lowered)) {
				return 1;
			}
			if (length == 2 && "no".equals(lowered)) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				byte value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to char. Following steps are performed while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Character </b>- return value of character</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for an instance of <b>String </b> with a single character in it</li>
		 * </ul>
		 *
		 * @param any
		 * @param def
		 * @return char */
		public static final char toChar(final Object any, final char def) {
			
			if (any == null) {
				return def;
			}
			if (any instanceof Character) {
				return ((Character) any).charValue();
			}
			if (any instanceof Number) {
				return (char) ((Number) any).intValue();
			}
			final String s = Any.toString(any, null);
			if (s == null || s.length() == 0) {
				return def;
			}
			if (s.length() == 1) {
				return s.charAt(0);
			}
			return def;
		}

		/** Safely converts any object to double. Following steps are performed while converting:
		 * <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???t???g???m???k???' where t means multiplied by 1024 in power
		 * of 4, g means multiplied by 1024 in power of 3, m means multiplied by 1024 in power of 2
		 * and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return float */
		public static final double toDouble(final Object any, final double defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).doubleValue();
			}
			if (any instanceof Date) {
				return ((Date) any).getTime();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			try {
				return Double.parseDouble(stringual.replace(',', '.'));
			} catch (final NumberFormatException t) {
				// ignore
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final double val = Any.parseLong(lowered.substring(2), 16);
				if (val == Double.NEGATIVE_INFINITY) {
					return defaultValue;
				}
				return (long) val;
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && lowered.equals("yes")) {
				return 1;
			}
			if (length == 2 && lowered.equals("no")) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				long value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 't' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_TERA;
							buffer.setLength(0);
							break;
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to float. Following steps are performed while converting:
		 * <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???t???g???m???k???' where t means multiplied by 1024 in power
		 * of 4, g means multiplied by 1024 in power of 3, m means multiplied by 1024 in power of 2
		 * and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final Object any, final float defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).floatValue();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			try {
				return Float.parseFloat(stringual.replace(',', '.'));
			} catch (final NumberFormatException t) {
				// ignore
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final double val = Any.parseLong(lowered.substring(2), 16);
				if (val == Double.NEGATIVE_INFINITY) {
					return defaultValue;
				}
				return (long) val;
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && lowered.equals("yes")) {
				return 1;
			}
			if (length == 2 && lowered.equals("no")) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				long value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 't' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_TERA;
							buffer.setLength(0);
							break;
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to int. Following steps are performed while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???g???m???k???' where g means multiplied by 1024 in power of
		 * 3, m means multiplied by 1024 in power of 2 and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final Object any, final int defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).intValue();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			{
				final long val = Any.parseInt(stringual, 10);
				if (val != Long.MIN_VALUE) {
					return (int) val;
				}
			}
			final String lowered = stringual.trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final long val = Any.parseInt(lowered.substring(2), 16);
				if (val != Long.MIN_VALUE) {
					return (int) val;
				}
				return defaultValue;
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && "yes".equals(lowered)) {
				return 1;
			}
			if (length == 2 && "no".equals(lowered)) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				int value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
						case '\t' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to int. Following steps are performed while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a case-insencetive value of one of variants passed as a parameter</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???g???m???k???' where g means multiplied by 1024 in power of
		 * 3, m means multiplied by 1024 in power of 2 and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param variants
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final Object any, final String[] variants, final int defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).intValue();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			for (int i = variants.length - 1; i >= 0; --i) {
				if (stringual.equalsIgnoreCase(variants[i])) {
					return i;
				}
			}
			{
				final long val = Any.parseInt(stringual, 10);
				if (val != Long.MIN_VALUE) {
					return (int) val;
				}
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final long val = Any.parseInt(lowered.substring(2), 16);
				if (val != Long.MIN_VALUE) {
					return (int) val;
				}
				return defaultValue;
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && lowered.equals("yes")) {
				return 1;
			}
			if (length == 2 && lowered.equals("no")) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				int value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to long. Following steps are performed while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for an instance of <b>Date </b>- return value of java.util.Date</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???t???g???m???k???' where t means multiplied by 1024 in power
		 * of 4, g means multiplied by 1024 in power of 3, m means multiplied by 1024 in power of 2
		 * and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return long */
		public static final long toLong(final Object any, final long defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).longValue();
			}
			if (any instanceof Date) {
				return ((Date) any).getTime();
			}
			if (any instanceof Value<?>) {
				final Object base = ((Value<?>) any).baseValue();
				if (base != null && base != any) {
					if (base instanceof Number) {
						return ((Number) base).longValue();
					}
					if (base instanceof Date) {
						return ((Date) base).getTime();
					}
				}
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			{
				final double val = Any.parseLong(stringual, 10);
				if (val != Double.NEGATIVE_INFINITY) {
					return (long) val;
				}
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final double val = Any.parseLong(lowered.substring(2), 16);
				if (val == Double.NEGATIVE_INFINITY) {
					return defaultValue;
				}
				return (long) val;
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && lowered.equals("yes")) {
				return 1;
			}
			if (length == 2 && lowered.equals("no")) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				long value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 't' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_TERA;
							buffer.setLength(0);
							break;
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** @param <T>
		 * @param any
		 * @param defaultValue
		 * @return object */
		public static final <T> T toObject(final T any, final T defaultValue) {
			
			return any == null
				? defaultValue
				: any;
		}

		/** Safely converts any object to period in milliseconds. Following steps are performed
		 * while converting: <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Try to parse in form '???w???d???h???m???s???' where w means multiplied by weeks, d -
		 * by days, h - by hours, m - by minutes and s - by seconds</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return long */
		public static final long toPeriod(final Object any, final long defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).longValue();
			}
			if (any instanceof Date) {
				return Math.abs(((Date) any).getTime() - Engine.fastTime());
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			{
				final double val = Any.parseLong(stringual, 10);
				if (val != Double.NEGATIVE_INFINITY) {
					return (long) val;
				}
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				final double val = Any.parseLong(lowered.substring(2), 16);
				if (val == Double.NEGATIVE_INFINITY) {
					return defaultValue;
				}
				return (long) val;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				long value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 'w' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_WEEKS;
							buffer.setLength(0);
							break;
						case 'd' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_DAYS;
							buffer.setLength(0);
							break;
						case 'h' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_HOURS;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MINUTES;
							buffer.setLength(0);
							break;
						case 's' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_SECONDS;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to short. Following steps are performed while converting:
		 * <br>
		 * <ul>
		 * <li>Check for NULL value - return default</li>
		 * <li>Check for an instance of <b>Number </b>- return value of number</li>
		 * <li>Check for a decimal stringual representation</li>
		 * <li>Check for a hexadecimal representation, starting with '0x' sequence</li>
		 * <li>Check for a boolean <b>true/false </b> values - return 1/0 respectively</li>
		 * <li>Check for a boolean <b>yes/no </b> values - return 1/0 respectively</li>
		 * <li>Try to parse in form '???g???m???k???' where g means multiplied by 1024 in power of
		 * 3, m means multiplied by 1024 in power of 2 and k means multiplied by 1024</li>
		 * </ul>
		 *
		 * @param any
		 * @param defaultValue
		 * @return short */
		public static final short toShort(final Object any, final short defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Number) {
				return ((Number) any).shortValue();
			}
			final String stringual = Any.toString(any);
			if (stringual == null || stringual.length() == 0) {
				return defaultValue;
			}
			try {
				return Short.parseShort(stringual);
			} catch (final NumberFormatException t) {
				// ignore
			}
			final String lowered = stringual.replace('\t', ' ').trim().toLowerCase();
			final int length = lowered.length();
			if (length > 2 && lowered.charAt(0) == '0' && lowered.charAt(1) == 'x') {
				try {
					return Short.parseShort(lowered.substring(2), 16);
				} catch (final NumberFormatException t) {
					return defaultValue;
				}
			}
			if (length == 4 && "true".equals(lowered)) {
				return 1;
			}
			if (length == 5 && "false".equals(lowered)) {
				return 0;
			}
			if (length == 3 && lowered.equals("yes")) {
				return 1;
			}
			if (length == 2 && lowered.equals("no")) {
				return 0;
			}
			try {
				final StringBuilder buffer = new StringBuilder(16);
				short value = 0;
				for (int i = 0; i < length; ++i) {
					final char current = lowered.charAt(i);
					switch (current) {
						case 'g' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_GIGA;
							buffer.setLength(0);
							break;
						case 'm' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_MEGA;
							buffer.setLength(0);
							break;
						case 'k' :
							value += Double.parseDouble(buffer.toString()) * Convert.MUL_KILO;
							buffer.setLength(0);
							break;
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' :
						case '.' :
							buffer.append(current);
							break;
						case ' ' :
							break;
						default :
							return defaultValue;
					}
				}
				if (buffer.length() > 0) {
					value += Double.parseDouble(buffer.toString());
				}
				return value;
			} catch (final NumberFormatException e) {
				// ignore
			}
			return defaultValue;
		}

		/** Safely converts any object to String without any NullPointerException errors. Returns
		 * null when source is null, creates a string when source is an array of chars or bytes.
		 * calls any.toString() at least.
		 *
		 * @param any
		 * @return string */
		private static final String toString(final Object any) {
			
			if (any == null) {
				return null;
			}
			final Class<?> cls = any.getClass();
			if (cls == String.class) {
				return (String) any;
			}
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			return any.toString();
		}

		/** Safely converts any object to String without any NullPointerException and null results
		 * (when defaultValue is not null). Default value returned when any is null or when
		 * any.toString() equals to null. Creates a string when source is an array.
		 *
		 * @param any
		 * @param defaultValue
		 * @return string */
		public static final String toString(final Object any, final String defaultValue) {
			
			if (any == null) {
				return defaultValue;
			}
			final Class<?> cls = any.getClass();
			if (cls == String.class) {
				return (String) any;
			}
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			final String tmp = any.toString();
			return tmp == null
				? defaultValue
				: tmp;
		}

		/** CATCH METHOD
		 *
		 * @param string */
		public static final void toString(final String string) {
			
			//
		}

		/** CATCH METHOD
		 *
		 * @param string
		 * @param defaultValue */
		public static final void toString(final String string, final String defaultValue) {
			
			//
		}

		private Any() {

			// empty
		}
	}

	/** @author myx */
	public static class Array {
		
		/** @param <T>
		 * @param array
		 * @return array */
		@SuppressWarnings("unchecked")
		public final static <T> T[] toAny(final Object[] array) {
			
			return (T[]) array;
		}

		/** @param array
		 * @param def
		 * @return byte array */
		public static final byte[] toBytes(final Object[] array, final byte def) {
			
			final byte[] result = new byte[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toByte(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return byte array */
		public static final byte[] toBytes(final Object[] array, final byte[] def) {
			
			final byte[] result = new byte[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toByte(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return char array */
		public static final char[] toChars(final Object[] array, final char def) {
			
			final char[] result = new char[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toChar(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return char array */
		public static final char[] toChars(final Object[] array, final char[] def) {
			
			final char[] result = new char[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toChar(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return double array */
		public static final double[] toDoubles(final Object[] array, final double def) {
			
			final double[] result = new double[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toDouble(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return double array */
		public static final double[] toDoubles(final Object[] array, final double[] def) {
			
			final double[] result = new double[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toDouble(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return float array */
		public static final float[] toFloats(final Object[] array, final float def) {
			
			final float[] result = new float[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toFloat(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return fload array */
		public static final float[] toFloats(final Object[] array, final float[] def) {
			
			final float[] result = new float[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toFloat(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return int array */
		public static final int[] toInts(final Object[] array, final int def) {
			
			final int[] result = new int[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toInt(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return int array */
		public static final int[] toInts(final Object[] array, final int[] def) {
			
			final int[] result = new int[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toInt(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return long array */
		public static final long[] toLongs(final Object[] array, final long def) {
			
			final long[] result = new long[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toLong(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return long array */
		public static final long[] toLongs(final Object[] array, final long[] def) {
			
			final long[] result = new long[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toLong(array[i], def[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final boolean[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = array[i]
					? Boolean.TRUE
					: Boolean.FALSE;
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final byte[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Byte.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final char[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Character.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final double[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Double.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final float[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Float.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final int[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Integer.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final long[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Long.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @return array */
		public static final Object[] toObjects(final short[] array) {
			
			final Object[] result = new Object[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Short.valueOf(array[i]);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return short array */
		public static final short[] toShorts(final Object[] array, final short def) {
			
			final short[] result = new short[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toShort(array[i], def);
			}
			return result;
		}

		/** @param array
		 * @param def
		 * @return short array */
		public static final short[] toShorts(final Object[] array, final short[] def) {
			
			final short[] result = new short[array.length];
			for (int i = array.length - 1; i >= 0; --i) {
				result[i] = Any.toShort(array[i], def[i]);
			}
			return result;
		}

		private Array() {

			// empty
		}
	}

	/** @author myx */
	public static class ListEntry {
		
		private static final String NOT_A_STRING = "<not a string>";

		/** Tries to get booleans from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return boolean array */
		public static final boolean[] allToBoolean(final java.util.List<?> list, final int[] key, final boolean defaultValue) {
			
			final int count = key.length;
			final boolean[] result = new boolean[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toBoolean(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return boolean array */
		public static final boolean[] allToBooleans(final java.util.List<?> list, final int[] key, final boolean[] def) {
			
			final int count = key.length;
			final boolean[] result = new boolean[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toBoolean(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get bytes from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return byte array */
		public static final byte[] allToByte(final java.util.List<?> list, final int[] key, final byte defaultValue) {
			
			final int count = key.length;
			final byte[] result = new byte[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toByte(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get bytes from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return byte array */
		public static final byte[] allToBytes(final java.util.List<?> list, final int[] key, final byte[] def) {
			
			final int count = key.length;
			final byte[] result = new byte[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toByte(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get chars from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return char array */
		public static final char[] allToChar(final java.util.List<?> list, final int[] key, final char defaultValue) {
			
			final int count = key.length;
			final char[] result = new char[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toChar(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get chars from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return char array */
		public static final char[] allToChars(final java.util.List<?> list, final int[] key, final char[] def) {
			
			final int count = key.length;
			final char[] result = new char[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toChar(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get doubles from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return double array */
		public static final double[] allToDouble(final java.util.List<?> list, final int[] key, final double defaultValue) {
			
			final int count = key.length;
			final double[] result = new double[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toDouble(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get doubles from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return double array */
		public static final double[] allToDoubles(final java.util.List<?> list, final int[] key, final double[] def) {
			
			final int count = key.length;
			final double[] result = new double[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toDouble(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get floats from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return float array */
		public static final float[] allToFloat(final java.util.List<?> list, final int[] key, final float defaultValue) {
			
			final int count = key.length;
			final float[] result = new float[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toFloat(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get floats from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return float array */
		public static final float[] allToFloats(final java.util.List<?> list, final int[] key, final float[] def) {
			
			final int count = key.length;
			final float[] result = new float[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toFloat(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return int array */
		public static final int[] allToInt(final java.util.List<?> list, final int[] key, final int defaultValue) {
			
			final int count = key.length;
			final int[] result = new int[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toInt(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return int array */
		public static final int[] allToInts(final java.util.List<?> list, final int[] key, final int[] def) {
			
			final int count = key.length;
			final int[] result = new int[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toInt(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get longs from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return long array */
		public static final long[] allToLong(final java.util.List<?> list, final int[] key, final long defaultValue) {
			
			final int count = key.length;
			final long[] result = new long[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toLong(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get longs from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return long array */
		public static final long[] allToLongs(final java.util.List<?> list, final int[] key, final long[] def) {
			
			final int count = key.length;
			final long[] result = new long[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toLong(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get an objects from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return object array */
		public static final Object[] allToObject(final java.util.List<?> list, final int[] key, final Object defaultValue) {
			
			final int count = key.length;
			final Object[] result = new Object[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toObject(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get an objects from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return array */
		public static final Object[] allToObjects(final java.util.List<?> list, final int[] key, final Object[] def) {
			
			final int count = key.length;
			final Object[] result = new Object[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toObject(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get shorts from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return short array */
		public static final short[] allToShort(final java.util.List<?> list, final int[] key, final short defaultValue) {
			
			final int count = key.length;
			final short[] result = new short[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toShort(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get shorts from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return short array */
		public static final short[] allToShorts(final java.util.List<?> list, final int[] key, final short[] def) {
			
			final int count = key.length;
			final short[] result = new short[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toShort(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get strings from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return string srray */
		public static final String[] allToString(final java.util.List<?> list, final int[] key, final String defaultValue) {
			
			final int count = key.length;
			final String[] result = new String[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toString(list, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get strings from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param list
		 * @param key
		 * @param def
		 * @return string array */
		public static final String[] allToStrings(final java.util.List<?> list, final int[] key, final String[] def) {
			
			final int count = key.length;
			final String[] result = new String[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = ListEntry.toString(list, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get a boolean value from map for any key specified in array, if map is null or
		 * no object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return boolean */
		public static final boolean anyToBoolean(final java.util.List<?> list, final int[] key, final boolean defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(
							result,
							defaultValue
								? 1
								: 0) != 0;
				}
			}
			return defaultValue;
		}

		/** Tries to get a byte from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte anyToByte(final java.util.List<?> list, final int[] key, final byte defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final short s = Any.toShort(result, Short.MAX_VALUE);
				if (s != Short.MAX_VALUE) {
					return Any.toByte(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a char from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return char */
		public static final char anyToChar(final java.util.List<?> list, final int[] key, final char defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final String s = Any.toString(result, ListEntry.NOT_A_STRING);
				if (s == ListEntry.NOT_A_STRING) {
					continue;
				}
				final int j = Any.toInt(result, Integer.MAX_VALUE);
				if (j != Integer.MAX_VALUE) {
					return (char) j;
				}
				if (s.length() == 1) {
					return s.charAt(0);
				}
			}
			return defaultValue;
		}

		/** Tries to get a double from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return double */
		public static final double anyToDouble(final java.util.List<?> list, final int[] key, final double defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final double d = Any.toDouble(result, Double.NaN);
				if (Double.NaN != d) {
					return d;
				}
			}
			return defaultValue;
		}

		/** Tries to get a float from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float anyToFloat(final java.util.List<?> list, final int[] key, final float defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return f;
				}
			}
			return defaultValue;
		}

		/** Tries to get an int from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int anyToInt(final java.util.List<?> list, final int[] key, final int defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a long from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long anyToLong(final java.util.List<?> list, final int[] key, final long defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return Any.toLong(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get an object from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object anyToObject(final java.util.List<?> list, final int[] key, final Object defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result != null) {
					return result;
				}
			}
			return defaultValue;
		}

		/** Tries to get a short from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short anyToShort(final java.util.List<?> list, final int[] key, final short defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final int j = Any.toInt(result, Integer.MAX_VALUE);
				if (j != Integer.MAX_VALUE) {
					return Any.toShort(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a string from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String anyToString(final java.util.List<?> list, final int[] key, final String defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			for (final int element : key) {
				if (list.size() <= element) {
					continue;
				}
				final Object result = list.get(element);
				if (result == null) {
					continue;
				}
				final String s = Any.toString(result, ListEntry.NOT_A_STRING);
				if (s != ListEntry.NOT_A_STRING) {
					return s;
				}
			}
			return defaultValue;
		}

		/** Tries to get a boolean value from list, if list is null or object is null returns
		 * defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return boolean */
		public static final boolean toBoolean(final BaseArray list, final int key, final boolean defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toInt(
						result,
						defaultValue
							? 1
							: 0) != 0;
		}

		/** Tries to get a boolean value from list, if list is null or object is null returns
		 * defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return boolean */
		public static final boolean toBoolean(final java.util.List<?> list, final int key, final boolean defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toInt(
						result,
						defaultValue
							? 1
							: 0) != 0;
		}

		/** Tries to get a byte from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte toByte(final java.util.List<?> list, final int key, final byte defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toByte(result, defaultValue);
		}

		/** Tries to get a char from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return char */
		public static final char toChar(final java.util.List<?> list, final int key, final char defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toChar(result, defaultValue);
		}

		/** @param <T>
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return collection */
		public static final <T> Collection<T> toCollection(final BaseArray list, final int key, final Collection<T> defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Object[]) {
				@SuppressWarnings("unchecked")
				final T[] array = (T[]) any;
				return Arrays.asList(array);
			}
			if (any instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				final Collection<T> result = (Collection<T>) any;
				return result;
			}
			{
				@SuppressWarnings("unchecked")
				final T object = (T) any;
				return Collections.singleton(object);
			}
		}

		/** @param <T>
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return collection */
		public static final <T> Collection<T> toCollection(final java.util.List<?> list, final int key, final Collection<T> defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Object[]) {
				@SuppressWarnings("unchecked")
				final T[] array = (T[]) any;
				return Arrays.asList(array);
			}
			if (any instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				final Collection<T> collection = (Collection<T>) any;
				return collection;
			}
			{
				@SuppressWarnings("unchecked")
				final T object = (T) any;
				return Collections.singleton(object);
			}
		}

		/** Tries to get a double from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return double */
		public static final double toDouble(final BaseArray list, final int key, final double defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toDouble(result, defaultValue);
		}

		/** Tries to get a double from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return double */
		public static final double toDouble(final java.util.List<?> list, final int key, final double defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toDouble(result, defaultValue);
		}

		/** Tries to get a float from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final BaseArray list, final int key, final float defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toFloat(result, defaultValue);
		}

		/** Tries to get a float from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final java.util.List<?> list, final int key, final float defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toFloat(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final BaseArray list, final int key, final int defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final java.util.List<?> list, final int key, final int defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 * case insencetive, 0-indexed.
		 *
		 * @param list
		 * @param key
		 * @param variants
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final java.util.List<?> list, final int key, final String[] variants, final int defaultValue) {
			
			if (list == null) {
				return defaultValue;
			}
			final Object result = list.get(key);
			if (result == null) {
				return defaultValue;
			}
			final String value = result.toString();
			for (int i = variants.length - 1; i >= 0; --i) {
				if (value.equalsIgnoreCase(variants[i])) {
					return i;
				}
			}
			return Any.toInt(result, defaultValue);
		}

		/** Tries to get a long from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toLong(final BaseArray list, final int key, final long defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toLong(result, defaultValue);
		}

		/** Tries to get a long from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toLong(final java.util.List<?> list, final int key, final long defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toLong(result, defaultValue);
		}

		/** Tries to get an object from list, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object toObject(final BaseArray list, final int key, final Object defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			return any == null
				? defaultValue
				: any;
		}

		/** Tries to get an object from list, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object toObject(final java.util.List<?> list, final int key, final Object defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			return any == null
				? defaultValue
				: any;
		}

		/** Tries to get a period from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toPeriod(final java.util.List<?> list, final int key, final long defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toPeriod(result, defaultValue);
		}

		/** Tries to get a short from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short toShort(final java.util.List<?> list, final int key, final short defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object result = list.get(key);
			return result == null
				? defaultValue
				: Any.toShort(result, defaultValue);
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final BaseArray list, final int key, final String defaultValue) {
			
			if (list == null || list.length() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			final String tmp = any == null
				? null
				: any.getClass() == char[].class
					? new String((char[]) any)
					: any.getClass() == byte[].class
						? new String((byte[]) any)
						: any.toString();
			return tmp == null
				? defaultValue
				: tmp;
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param list
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final java.util.List<?> list, final int key, final String defaultValue) {
			
			if (list == null || list.size() <= key) {
				return defaultValue;
			}
			final Object any = list.get(key);
			final String tmp = any == null
				? null
				: any.getClass() == char[].class
					? new String((char[]) any)
					: any.getClass() == byte[].class
						? new String((byte[]) any)
						: any.toString();
			return tmp == null
				? defaultValue
				: tmp;
		}

		private ListEntry() {

			// empty
		}
	}

	/** @author myx */
	public static class MapEntry {
		
		private static final String NOT_A_STRING = "<not a string>";

		/** Tries to get booleans from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return boolean array */
		public static final boolean[] allToBoolean(final Map<?, ?> map, final Object[] key, final boolean defaultValue) {
			
			final int count = key.length;
			final boolean[] result = new boolean[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toBoolean(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return boolean array */
		public static final boolean[] allToBooleans(final Map<?, ?> map, final Object[] key, final boolean[] def) {
			
			final int count = key.length;
			final boolean[] result = new boolean[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toBoolean(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get bytes from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return byte array */
		public static final byte[] allToByte(final Map<?, ?> map, final Object[] key, final byte defaultValue) {
			
			final int count = key.length;
			final byte[] result = new byte[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toByte(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get bytes from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return byte array */
		public static final byte[] allToBytes(final Map<?, ?> map, final Object[] key, final byte[] def) {
			
			final int count = key.length;
			final byte[] result = new byte[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toByte(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get chars from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return char array */
		public static final char[] allToChar(final Map<?, ?> map, final Object[] key, final char defaultValue) {
			
			final int count = key.length;
			final char[] result = new char[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toChar(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get chars from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return char array */
		public static final char[] allToChars(final Map<?, ?> map, final Object[] key, final char[] def) {
			
			final int count = key.length;
			final char[] result = new char[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toChar(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get doubles from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return double array */
		public static final double[] allToDouble(final Map<?, ?> map, final Object[] key, final double defaultValue) {
			
			final int count = key.length;
			final double[] result = new double[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toDouble(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get doubles from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return double array */
		public static final double[] allToDoubles(final Map<?, ?> map, final Object[] key, final double[] def) {
			
			final int count = key.length;
			final double[] result = new double[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toDouble(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get floats from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return float array */
		public static final float[] allToFloat(final Map<?, ?> map, final Object[] key, final float defaultValue) {
			
			final int count = key.length;
			final float[] result = new float[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toFloat(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get floats from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return float array */
		public static final float[] allToFloats(final Map<?, ?> map, final Object[] key, final float[] def) {
			
			final int count = key.length;
			final float[] result = new float[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toFloat(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int array */
		public static final int[] allToInt(final Map<?, ?> map, final Object[] key, final int defaultValue) {
			
			final int count = key.length;
			final int[] result = new int[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toInt(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get ints from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return int array */
		public static final int[] allToInts(final Map<?, ?> map, final Object[] key, final int[] def) {
			
			final int count = key.length;
			final int[] result = new int[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toInt(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get longs from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long array */
		public static final long[] allToLong(final Map<?, ?> map, final Object[] key, final long defaultValue) {
			
			final int count = key.length;
			final long[] result = new long[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toLong(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get longs from map for all keys specified in array, if map is null or no object
		 * found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return long array */
		public static final long[] allToLongs(final Map<?, ?> map, final Object[] key, final long[] def) {
			
			final int count = key.length;
			final long[] result = new long[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toLong(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get an objects from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return array */
		public static final Object[] allToObject(final Map<?, ?> map, final Object[] key, final Object defaultValue) {
			
			final int count = key.length;
			final Object[] result = new Object[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toObject(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get an objects from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return array */
		public static final Object[] allToObjects(final Map<?, ?> map, final Object[] key, final Object[] def) {
			
			final int count = key.length;
			final Object[] result = new Object[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toObject(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get shorts from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return short array */
		public static final short[] allToShort(final Map<?, ?> map, final Object[] key, final short defaultValue) {
			
			final int count = key.length;
			final short[] result = new short[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toShort(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get shorts from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return string array */
		public static final short[] allToShorts(final Map<?, ?> map, final Object[] key, final short[] def) {
			
			final int count = key.length;
			final short[] result = new short[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toShort(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get strings from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string array */
		public static final String[] allToString(final Map<?, ?> map, final Object[] key, final String defaultValue) {
			
			final int count = key.length;
			final String[] result = new String[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toString(map, key[i], defaultValue);
			}
			return result;
		}

		/** Tries to get strings from map for all keys specified in array, if map is null or no
		 * object found - defaultValue is stored in its place of result array.
		 *
		 * @param map
		 * @param key
		 * @param def
		 * @return string array */
		public static final String[] allToStrings(final Map<?, ?> map, final Object[] key, final String[] def) {
			
			final int count = key.length;
			final String[] result = new String[count];
			for (int i = count - 1; i >= 0; --i) {
				result[i] = Convert.MapEntry.toString(map, key[i], def[i]);
			}
			return result;
		}

		/** Tries to get a boolean value from map for any key specified in array, if map is null or
		 * no object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return boolean */
		public static final boolean anyToBoolean(final Map<?, ?> map, final Object[] key, final boolean defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(
							result,
							defaultValue
								? 1
								: 0) != 0;
				}
			}
			return defaultValue;
		}

		/** Tries to get a byte from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte anyToByte(final Map<?, ?> map, final Object[] key, final byte defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final short s = Any.toShort(result, Short.MAX_VALUE);
				if (s != Short.MAX_VALUE) {
					return Any.toByte(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a char from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return char */
		public static final char anyToChar(final Map<?, ?> map, final Object[] key, final char defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final String s = Any.toString(result, MapEntry.NOT_A_STRING);
				if (s == MapEntry.NOT_A_STRING) {
					continue;
				}
				final int j = Any.toInt(result, Integer.MAX_VALUE);
				if (j != Integer.MAX_VALUE) {
					return (char) j;
				}
				if (s.length() == 1) {
					return s.charAt(0);
				}
			}
			return defaultValue;
		}

		/** Tries to get a double from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return double */
		public static final double anyToDouble(final Map<?, ?> map, final Object[] key, final double defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final double d = Any.toDouble(result, Double.NaN);
				if (Double.NaN != d) {
					return d;
				}
			}
			return defaultValue;
		}

		/** Tries to get a float from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float anyToFloat(final Map<?, ?> map, final Object[] key, final float defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return f;
				}
			}
			return defaultValue;
		}

		/** Tries to get an int from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int anyToInt(final BaseMap map, final String[] key, final int defaultValue) {
			
			if (map == null || map.baseIsPrimitive()) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java value";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get an int from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int anyToInt(final BaseObject map, final String[] key, final int defaultValue) {
			
			if (map == null || map.baseIsPrimitive()) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java value";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get an int from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int anyToInt(final Map<?, ?> map, final Object[] key, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final long l = Any.toLong(result, Long.MAX_VALUE);
				if (l != Long.MAX_VALUE) {
					return Any.toInt(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a long from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long anyToLong(final BaseMap map, final String[] key, final long defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java value";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return Any.toLong(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a long from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long anyToLong(final BaseObject map, final String[] key, final long defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java value";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return Any.toLong(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a long from map for any key specified in array, if map is null or no object
		 * found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long anyToLong(final Map<?, ?> map, final Object[] key, final long defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final float f = Any.toFloat(result, Float.NaN);
				if (Float.NaN != f) {
					return Any.toLong(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get an object from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object anyToObject(final Map<?, ?> map, final Object[] key, final Object defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result != null) {
					return result;
				}
			}
			return defaultValue;
		}

		/** Tries to get a short from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short anyToShort(final Map<?, ?> map, final Object[] key, final short defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final int j = Any.toInt(result, Integer.MAX_VALUE);
				if (j != Integer.MAX_VALUE) {
					return Any.toShort(result, defaultValue);
				}
			}
			return defaultValue;
		}

		/** Tries to get a string from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String anyToString(final BaseMap map, final String[] key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java object";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final String s = Any.toString(result, MapEntry.NOT_A_STRING);
				if (s != MapEntry.NOT_A_STRING) {
					return s;
				}
			}
			return defaultValue;
		}

		/** Tries to get a string from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String anyToString(final BaseObject map, final String[] key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final String element : key) {
				final BaseObject result = map.baseGet(element, BaseObject.UNDEFINED);
				assert result != null : "NULL java object";
				if (result == BaseObject.UNDEFINED) {
					continue;
				}
				final String s = Any.toString(result, MapEntry.NOT_A_STRING);
				if (s != MapEntry.NOT_A_STRING) {
					return s;
				}
			}
			return defaultValue;
		}

		/** Tries to get a string from map for any key specified in array, if map is null or no
		 * object found - returns defaultValue. Keys are searched from first to last.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String anyToString(final Map<?, ?> map, final Object[] key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			for (final Object element : key) {
				final Object result = map.get(element);
				if (result == null) {
					continue;
				}
				final String s = Any.toString(result, MapEntry.NOT_A_STRING);
				if (s != MapEntry.NOT_A_STRING) {
					return s;
				}
			}
			return defaultValue;
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static boolean toBoolean(final BaseMap object, final String key, final boolean defaultValue) {
			
			return Convert.MapEntry.toInt(
					object,
					key,
					defaultValue
						? 1
						: 0) != 0;
			/** THIS WOULD BE WRONG: 'false' will become true */
			// return object.baseGetBoolean( key, defaultValue );
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static boolean toBoolean(final BaseObject object, final String key, final boolean defaultValue) {
			
			return Convert.MapEntry.toInt(
					object,
					key,
					defaultValue
						? 1
						: 0) != 0;
			/** THIS WOULD BE WRONG: 'false' will become true */
			// return object.baseGetBoolean( key, defaultValue );
		}

		/** Tries to get a boolean value from map, if map is null or object is null returns
		 * defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return boolean */
		public static final boolean toBoolean(final Map<?, ?> map, final Object key, final boolean defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toInt(
						result,
						defaultValue
							? 1
							: 0) != 0;
		}

		/** Tries to get a byte from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte toByte(final BaseMap map, final String key, final byte defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toByte(result, defaultValue);
		}

		/** Tries to get a byte from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte toByte(final BaseObject map, final String key, final byte defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toByte(result, defaultValue);
		}

		/** Tries to get a byte from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return byte */
		public static final byte toByte(final Map<?, ?> map, final Object key, final byte defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toByte(result, defaultValue);
		}

		/** Tries to get a char from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return char */
		public static final char toChar(final Map<?, ?> map, final Object key, final char defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toChar(result, defaultValue);
		}

		/** @param map
		 * @param key
		 * @return */
		public static final BaseArray toCollection(final BaseMap map, final String key) {
			
			return MapEntry.toCollection(map, key, (BaseArray) null);
		}

		/** @param map
		 * @param key
		 * @param defaultValue
		 * @return */
		public static final BaseArray toCollection(final BaseMap map, final String key, final BaseArray defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject object = map.baseGet(key, BaseObject.UNDEFINED);
			if (object == BaseObject.UNDEFINED) {
				return defaultValue;
			}
			/** our primitive strings do implement BaseArray, so we have to avoid */
			if (object.baseIsPrimitive()) {
				return new BaseNativeArray(object);
			}
			final BaseArray array = object.baseArray();
			return array == null
				? new BaseNativeArray(object)
				: array;
		}

		/** @param map
		 * @param key
		 * @param defaultValue
		 * @return */
		public static final BaseArray toCollection(final BaseObject map, final String key, final BaseArray defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject object = map.baseGet(key, BaseObject.UNDEFINED);
			assert object != null : "NULL java object";
			if (object == BaseObject.UNDEFINED) {
				return defaultValue;
			}
			/** our primitive strings do implement BaseArray, so we have to avoid */
			if (object.baseIsPrimitive()) {
				return new BaseNativeArray(object);
			}
			final BaseArray array = object.baseArray();
			if (array != null) {
				return array;
			}
			if (object instanceof Iterable<?>) {
				final BaseList<Object> created = BaseObject.createArray();
				for (final Object value : (Iterable<?>) object) {
					created.add(value);
				}
				return created;
			}
			if (object.baseValue() instanceof Iterable<?>) {
				final BaseList<Object> created = BaseObject.createArray();
				for (final Object value : (Iterable<?>) object.baseValue()) {
					created.add(value);
				}
				return created;
			}
			{
				return new BaseNativeArray(object);
			}
		}

		/** Tries to get a collection from map, if map is null or object is null returns
		 * defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @param <T>
		 * @return collection */
		public static final <T> Collection<T> toCollection(final Map<?, ?> map, final Object key, final Collection<T> defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = map.get(key);
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Object[]) {
				@SuppressWarnings("unchecked")
				final T[] array = (T[]) any;
				return Arrays.asList(array);
			}
			if (any instanceof Collection<?>) {
				@SuppressWarnings("unchecked")
				final Collection<T> collection = (Collection<T>) any;
				return collection;
			}
			{
				@SuppressWarnings("unchecked")
				final T object = (T) any;
				return Collections.singleton(object);
			}
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static double toDouble(final BaseMap object, final String key, final double defaultValue) {
			
			if (object == null) {
				return defaultValue;
			}
			final BaseObject result = object.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toDouble(result, defaultValue);
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static double toDouble(final BaseObject object, final String key, final double defaultValue) {
			
			if (object == null) {
				return defaultValue;
			}
			final BaseObject result = object.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toDouble(result, defaultValue);
		}

		/** Tries to get a double from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return double */
		public static final double toDouble(final Map<?, ?> map, final Object key, final double defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toDouble(result, defaultValue);
		}

		/** Tries to get a float from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final BaseMap map, final String key, final float defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toFloat(result, defaultValue);
		}

		/** Tries to get a float from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final BaseObject map, final String key, final float defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toFloat(result, defaultValue);
		}

		/** Tries to get a float from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return float */
		public static final float toFloat(final Map<?, ?> map, final Object key, final float defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toFloat(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final BaseMap map, final String key, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = Base.getJava(map, key, null);
			return result == null
				? defaultValue
				: Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final BaseObject map, final String key, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = Base.getJava(map, key, null);
			return result == null
				? defaultValue
				: Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 * case insencetive, 0-indexed.
		 *
		 * @param map
		 * @param key
		 * @param variants
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final BaseObject map, final String key, final String[] variants, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			assert result != null : "NULL java value";
			if (result == BaseObject.UNDEFINED) {
				return defaultValue;
			}
			final String value = result.toString();
			for (int i = variants.length - 1; i >= 0; --i) {
				if (value.equalsIgnoreCase(variants[i])) {
					return i;
				}
			}
			return Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final Map<?, ?> map, final Object key, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toInt(result, defaultValue);
		}

		/** Tries to get an int from map, if map is null or object is null returns defaultValue.
		 * case insencetive, 0-indexed.
		 *
		 * @param map
		 * @param key
		 * @param variants
		 * @param defaultValue
		 * @return int */
		public static final int toInt(final Map<?, ?> map, final String key, final String[] variants, final int defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			if (result == null) {
				return defaultValue;
			}
			final String value = result.toString();
			for (int i = variants.length - 1; i >= 0; --i) {
				if (value.equalsIgnoreCase(variants[i])) {
					return i;
				}
			}
			return Any.toInt(result, defaultValue);
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static long toLong(final BaseMap object, final String key, final long defaultValue) {
			
			if (object == null) {
				return defaultValue;
			}
			final Object result = object.get(key);
			return result == null
				? defaultValue
				: Any.toLong(result, defaultValue);
		}

		/** @param object
		 * @param key
		 * @param defaultValue
		 * @return */
		public static long toLong(final BaseObject object, final String key, final long defaultValue) {
			
			if (object == null) {
				return defaultValue;
			}
			final BaseObject result = object.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toLong(result, defaultValue);
		}

		/** Tries to get a long from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toLong(final Map<?, ?> map, final Object key, final long defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toLong(result, defaultValue);
		}

		/** Tries to get a map from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @param <K>
		 * @param <V>
		 * @return map */
		public static final <K, V> Map<K, V> toMap(final Map<?, ?> map, final Object key, final Map<K, V> defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = map.get(key);
			if (any == null) {
				return defaultValue;
			}
			if (any instanceof Map<?, ?>) {
				return Convert.Any.toAny(any);
			}
			return defaultValue;
		}

		/** Tries to get an object from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object toObject(final BaseMap map, final String key, final Object defaultValue) {
			
			return map == null
				? defaultValue
				: Base.getJava(map, key, defaultValue);
		}

		/** Tries to get an object from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object toObject(final BaseObject map, final String key, final Object defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = map.baseGet(key, BaseObject.UNDEFINED).baseValue();
			return any == null
				? defaultValue
				: any;
		}

		/** Tries to get an object from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return object */
		public static final Object toObject(final Map<?, ?> map, final Object key, final Object defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = map.get(key);
			return any == null
				? defaultValue
				: any;
		}

		/** Tries to get a period from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toPeriod(final BaseMap map, final String key, final long defaultValue) {
			
			if (map == null || map.baseIsPrimitive()) {
				return defaultValue;
			}
			final Object result = map.baseGet(key, BaseObject.UNDEFINED).baseValue();
			return result == null
				? defaultValue
				: Any.toPeriod(result, defaultValue);
		}

		/** Tries to get a period from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toPeriod(final BaseObject map, final String key, final long defaultValue) {
			
			if (map == null || map.baseIsPrimitive()) {
				return defaultValue;
			}
			final Object result = map.baseGet(key, BaseObject.UNDEFINED).baseValue();
			return result == null
				? defaultValue
				: Any.toPeriod(result, defaultValue);
		}

		/** Tries to get a period from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return long */
		public static final long toPeriod(final Map<?, ?> map, final Object key, final long defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toPeriod(result, defaultValue);
		}

		/** Tries to get a short from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short toShort(final BaseMap map, final String key, final short defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toShort(result, defaultValue);
		}

		/** Tries to get a short from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short toShort(final BaseObject map, final String key, final short defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final BaseObject result = map.baseGet(key, BaseObject.UNDEFINED);
			return result == BaseObject.UNDEFINED
				? defaultValue
				: Any.toShort(result, defaultValue);
		}

		/** Tries to get a short from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return short */
		public static final short toShort(final Map<?, ?> map, final Object key, final short defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object result = map.get(key);
			return result == null
				? defaultValue
				: Any.toShort(result, defaultValue);
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final BaseArray map, final String key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = Base.getJava(map, key, null);
			if (any == null) {
				return defaultValue;
			}
			final Class<?> cls = any.getClass();
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			final String result = any.toString();
			return result == null
				? defaultValue
				: result;
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final BaseMap map, final String key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = Base.getJava(map, key, null);
			if (any == null) {
				return defaultValue;
			}
			final Class<?> cls = any.getClass();
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			final String result = any.toString();
			return result == null
				? defaultValue
				: result;
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final BaseObject map, final String key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = Base.getJava(map, key, null);
			if (any == null) {
				return defaultValue;
			}
			final Class<?> cls = any.getClass();
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			final String result = any.toString();
			return result == null
				? defaultValue
				: result;
		}

		/** Tries to get a string from map, if map is null or object is null returns defaultValue.
		 *
		 * @param map
		 * @param key
		 * @param defaultValue
		 * @return string */
		public static final String toString(final Map<?, ?> map, final Object key, final String defaultValue) {
			
			if (map == null) {
				return defaultValue;
			}
			final Object any = map.get(key);
			if (any == null) {
				return defaultValue;
			}
			final Class<?> cls = any.getClass();
			if (cls == char[].class) {
				return new String((char[]) any);
			}
			if (cls == byte[].class) {
				return new String((byte[]) any);
			}
			final String result = any.toString();
			return result == null
				? defaultValue
				: result;
		}

		private MapEntry() {

			// empty
		}
	}

	/**
	 *
	 */
	public static final long MUL_SECONDS = 1000L;

	/**
	 *
	 */
	public static final long MUL_MINUTES = Convert.MUL_SECONDS * 60L;

	/**
	 *
	 */
	public static final long MUL_HOURS = Convert.MUL_MINUTES * 60L;

	/**
	 *
	 */
	public static final long MUL_DAYS = Convert.MUL_HOURS * 24L;

	/**
	 *
	 */
	public static final long MUL_WEEKS = Convert.MUL_DAYS * 7L;

	/**
	 *
	 */
	public static final long MUL_KILO = 1024L;

	/**
	 *
	 */
	public static final long MUL_MEGA = Convert.MUL_KILO * 1024L;

	/**
	 *
	 */
	public static final long MUL_GIGA = Convert.MUL_MEGA * 1024L;

	/**
	 *
	 */
	public static final long MUL_TERA = Convert.MUL_GIGA * 1024L;

	private Convert() {

		// empty
	}
}
