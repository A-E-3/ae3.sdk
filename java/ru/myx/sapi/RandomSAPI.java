/*
 * Created on 13.12.2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.sapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.reflect.ReflectionHidden;

/** @author myx
 *
 *         Window - Preferences - Java - Code Style - Code Templates */
public class RandomSAPI {
	
	/** base62 digits: case-sensitive, all basic letters and digits */
	@ReflectionHidden
	public static final String MIXED_62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	/** base78 digits: case-sensitive, all basic letters and digits plus some basic keyboard
	 * symbols */
	@ReflectionHidden
	public static final String MIXED_78 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@$^*-_[]{}|.,";
	
	/** base55 digits: case-sensitive easily readable and easily distinguishable */
	@ReflectionHidden
	public static final String MIXED_55_EASY = "BCDFGHKLMNPRSTVZbcdfhkmnprstvzJQWXjwxAEUYaeiouy23456789";
	
	/** base27 digits: case-insensitive easily readable and easily distinguishable */
	@ReflectionHidden
	public static final String MIXED_27_EASY = "bcdfhkmnprtvzjwxaeuy2346789";
	
	/** @param length
	 * @return */
	public static TransferCopier binary(final int length) {

		final byte[] b = new byte[length];
		for (int i = length;;) {
			final int random = Engine.createRandom();
			if (--i < 0) {
				break;
			}
			b[i] = (byte) (random & 0xFF);
			if (--i < 0) {
				break;
			}
			b[i] = (byte) (random >> 8 & 0xFF);
			if (--i < 0) {
				break;
			}
			b[i] = (byte) (random >> 16 & 0xFF);
			if (--i < 0) {
				break;
			}
			b[i] = (byte) (random >> 24 & 0xFF);
		}
		return Transfer.wrapCopier(b);
	}
	
	/** @param format
	 *            D - decimal digit<br>
	 *            H - hexadecimal digit - upper case<br>
	 *            h - hexadecimal digit - lower case<br>
	 *            Z - 36-base digit - upper case<br>
	 *            z - 36-base digit - lower case<br>
	 *            M - 78-base digit - mixed case and ~`!@$^*-_[]{}|.,<br>
	 *            m - 62-base digit - mixed case<br>
	 *            E - 55-base digit - mixed case, easy, non-ambiguous<br>
	 *            \ - next character should be copied<br>
	 *            all other characters just copied
	 *
	 * @return */
	public static String formattedString(final String format) {

		if (format == null) {
			return null;
		}
		final StringBuilder result = new StringBuilder();
		boolean copyNext = false;
		for (int i = 0, left = 0, value = 0; i < format.length(); ++i) {
			final char c = format.charAt(i);
			if (copyNext) {
				result.append(c);
				copyNext = false;
				continue;
			}
			switch (c) {
				case 'D' :
					if (left < 10) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append((value & 0x7FFFFFFF) % 10);
					value /= 10;
					left /= 10;
					continue;
				case 'H' :
				case 'h' :
					if (left < 16) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(
							c == 'H'
								? Integer.toHexString((value & 0x7FFFFFFF) % 16).toUpperCase()
								: Integer.toHexString((value & 0x7FFFFFFF) % 16).toLowerCase());
					value /= 16;
					left /= 16;
					continue;
				case 'Z' :
				case 'z' :
					if (left < 36) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(
							c == 'Z'
								? Integer.toString((value & 0x7FFFFFFF) % 36, 36).toUpperCase()
								: Integer.toString((value & 0x7FFFFFFF) % 36, 36).toLowerCase());
					value /= 36;
					left /= 36;
					continue;
				case 'm' :
					if (left < 62) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(RandomSAPI.MIXED_62.charAt((value & 0x7FFFFFFF) % 62));
					value /= 62;
					left /= 62;
					continue;
				case 'M' :
					if (left < 78) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(RandomSAPI.MIXED_78.charAt((value & 0x7FFFFFFF) % 78));
					value /= 78;
					left /= 78;
					continue;
				case 'E' :
					if (left < 55) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(RandomSAPI.MIXED_55_EASY.charAt((value & 0x7FFFFFFF) % 55));
					value /= 55;
					left /= 55;
					continue;
				case 'e' :
					if (left < 27) {
						value = Engine.createRandom();
						left = Integer.MAX_VALUE;
					}
					result.append(RandomSAPI.MIXED_27_EASY.charAt((value & 0x7FFFFFFF) % 27));
					value /= 27;
					left /= 27;
					continue;
				case '\\' :
					copyNext = true;
					continue;
				default :
					result.append(c);
			}
		}
		return result.toString();
	}
	
	/** @param list
	 * @return object */
	public static Object fromList(final List<Object> list) {

		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(Engine.createRandom(list.size()));
	}
	
	/** @param list
	 * @return object */
	public static Object fromList(final Object[] list) {

		if (list == null || list.length == 0) {
			return null;
		}
		return list[Engine.createRandom(list.length)];
	}
	
	/** @param limit
	 * @return int */
	public static int integer(final int limit) {

		if (limit < 1) {
			throw new IllegalArgumentException("Limit should be greater than zero, but equals to " + limit + "!");
		}
		return Engine.createRandom(limit);
	}
	
	/** @param random
	 * @return */
	public static String reformat55to27(final String random) {

		final StringBuilder result = new StringBuilder();
		final int length = random.length();
		for (int i = 0, left = 0, value = 0; i < length; i++) {
			value = RandomSAPI.MIXED_55_EASY.indexOf(random.charAt(i));
			assert value >= 0;
			value += left;
			result.append(RandomSAPI.MIXED_27_EASY.charAt((value & 0x7FFFFFFF) % 27));
			left = value / 27;
		}
		return result.toString();
	}
	
	/** @param list
	 * @param limit
	 * @return list */
	public static List<?> subList(final List<Object> list, final int limit) {

		if (list == null) {
			return null;
		}
		if (limit < 1) {
			throw new IllegalArgumentException("Limit should be greater than zero, but equals to " + limit + "!");
		}
		if (limit >= list.size()) {
			final List<Object> result = new ArrayList<>();
			result.addAll(list);
			Collections.shuffle(result);
			return result;
		}
		final List<Object> result = new ArrayList<>();
		result.addAll(list);
		Collections.shuffle(result);
		return result.subList(0, limit);
	}
	
	/** @param map
	 * @param limit
	 * @return map */
	public static BaseObject subMap(final BaseObject map, final int limit) {

		if (map == null) {
			return null;
		}
		if (limit < 1) {
			throw new IllegalArgumentException("Limit should be greater than zero, but equals to " + limit + "!");
		}
		final List<String> result = new ArrayList<>();
		for (final Iterator<String> keys = map.baseKeysOwn(); keys.hasNext();) {
			result.add(keys.next());
		}
		if (limit >= result.size()) {
			return map;
		}
		Collections.shuffle(result);
		final BaseNativeObject resultMap = new BaseNativeObject(map.basePrototype());
		for (final String key : result.subList(0, limit)) {
			resultMap.putAppend(key, map.baseGet(key, BaseObject.UNDEFINED));
		}
		return resultMap;
	}
	
	/** @param map
	 * @param limit
	 * @return map */
	public static Map<String, Object> subMap(final Map<String, Object> map, final int limit) {

		if (map == null) {
			return null;
		}
		if (limit < 1) {
			throw new IllegalArgumentException("Limit should be greater than zero, but equals to " + limit + "!");
		}
		if (limit >= map.size()) {
			return map;
		}
		final List<Map.Entry<String, Object>> result = new ArrayList<>();
		result.addAll(map.entrySet());
		Collections.shuffle(result);
		final BaseMap resultMap = BaseObject.createObject();
		for (final Map.Entry<String, Object> current : result.subList(0, limit)) {
			resultMap.put(current.getKey(), current.getValue());
		}
		return resultMap;
	}
}
