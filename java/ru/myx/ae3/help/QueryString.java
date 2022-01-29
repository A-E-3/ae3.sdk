/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.help;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;

/** @author myx */
public class QueryString {

	/** Charset is UTF-8
	 *
	 * @param queryString
	 * @return */
	public static final BaseMap parse(final String queryString) {

		return QueryString.parseQueryString(queryString, StandardCharsets.UTF_8);
	}

	/** New object. Writable.
	 *
	 * @param queryString
	 * @param charset
	 * @return map */
	public static final BaseMap parseQueryString(final String queryString, final Charset charset) {

		final BaseMap map = BaseObject.createObject();
		if (queryString == null) {
			return map;
		}
		for (final StringTokenizer st = new StringTokenizer(queryString, "&", false); st.hasMoreTokens();) {
			final String next = st.nextToken();
			final int pos = next.indexOf('=');
			if (pos == -1) {
				continue;
			}
			final String name = next.substring(0, pos);
			final String value = Text.decodeUri(next.substring(pos + 1), charset);
			final BaseObject o = map.baseGet(name, BaseObject.UNDEFINED);
			if (o == BaseObject.UNDEFINED) {
				map.baseDefine(name, value);
			} else {
				assert o != null : "NULL java object";
				final MultipleList list;
				if (o instanceof MultipleList) {
					list = (MultipleList) o;
				} else {
					list = new MultipleList();
					list.add(o);
					map.baseDefine(name, list);
				}
				list.add(value);
			}
		}
		return map;
	}

	/** @param queryString
	 * @param encoding
	 * @return map */
	public static final BaseMap parseQueryString(final String queryString, final String encoding) {

		return QueryString.parseQueryString(queryString, Charset.forName(encoding));
	}

	/** Charset is UTF-8
	 *
	 * @param params
	 * @return */
	public static final String stringify(final BaseObject params) {

		return QueryString.toQueryString(params, StandardCharsets.UTF_8);
	}

	/** @param params
	 * @param charset
	 * @return string */
	public static final String toQueryString(final BaseObject params, final Charset charset) {

		boolean first = true;
		final StringBuilder result = new StringBuilder(64);
		for (final Iterator<String> keys = Base.keys(params, BaseObject.PROTOTYPE); keys.hasNext();) {
			final String key = keys.next();
			final BaseObject value = params.baseGet(key, BaseObject.UNDEFINED);
			if (value == BaseObject.UNDEFINED) {
				continue;
			}
			if (value instanceof CharSequence || value.baseArray() == null) {
				if (!first) {
					result.append('&');
				} else {
					first = false;
				}
				result.append(key).append('=').append(Text.encodeUriComponent(value, charset));
			} else {
				final BaseArray array = value.baseArray();
				for (int i = 0; i < array.length(); i++) {
					if (!first) {
						result.append('&');
					} else {
						first = false;
					}
					result.append(key).append('=').append(Text.encodeUriComponent(array.baseGet(i, BaseObject.UNDEFINED), charset));
				}
			}
		}
		return result.toString();
	}

	/** @param params
	 * @param encoding
	 * @return string */
	public static final String toQueryString(final BaseObject params, final String encoding) {

		return QueryString.toQueryString(params, Charset.forName(encoding));
	}

	private QueryString() {

		// empty
	}
}
