/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author myx
 * 		
 */
public final class Html {
	
	private static final Map<String, String> NEW_LINES;
	
	private static final Set<String> OUT_STOP = new TreeSet<>(
			Arrays.asList(new String[]{
					"script", "style"
	}));
	
	private static final Set<String> OUT_START = new TreeSet<>(
			Arrays.asList(new String[]{
					"/script", "/style"
	}));
	
	private static final Set<String> NEED_ATTRIBUTES = new TreeSet<>(
			Arrays.asList(new String[]{
					"img",
	}));
	
	static {
		NEW_LINES = new TreeMap<>();
		Html.NEW_LINES.put("/title", "\r\n\r\n");
		Html.NEW_LINES.put("/h1", "\r\n\r\n");
		Html.NEW_LINES.put("/h2", "\r\n");
		Html.NEW_LINES.put("/h3", "\r\n");
		Html.NEW_LINES.put("/h4", "\r\n");
		Html.NEW_LINES.put("/h5", "\r\n");
		Html.NEW_LINES.put("table", "\r\n");
		Html.NEW_LINES.put("/table", "\r\n");
		Html.NEW_LINES.put("/th", "\t");
		Html.NEW_LINES.put("/td", "\t");
		Html.NEW_LINES.put("/div", "\r\n");
		Html.NEW_LINES.put("p", "\r\n");
		Html.NEW_LINES.put("/p", "\r\n\r\n");
		Html.NEW_LINES.put("p/", "\r\n\r\n");
		Html.NEW_LINES.put("br", "\r\n");
		Html.NEW_LINES.put("br/", "\r\n");
		Html.NEW_LINES.put("tr", "\r\n");
	}
	
	/**
	 * @param original
	 *            html
	 * @return plain text
	 */
	public static final String cleanHtml(final String original) {
		
		int levelTag = 0;
		int levelEnt = 0;
		boolean whitespace = true;
		boolean tagAttributes = false;
		boolean tagWhitespace = false;
		boolean needAttributes = false;
		List<String> waitClose = null;
		final int length = original.length();
		final StringBuilder buffer = new StringBuilder(256);
		final StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			final char c = original.charAt(i);
			if (c == '<') {
				if (buffer.length() > 0 && levelEnt > 0 && waitClose == null) {
					result.append('&').append(buffer);
				}
				levelEnt = 0;
				levelTag++;
				buffer.setLength(0);
				tagWhitespace = true;
				tagAttributes = false;
				continue;
			}
			if (c == '>') {
				if (levelTag > 0) {
					levelTag--;
					final String source = buffer.toString();
					final String putNewLine = Html.NEW_LINES.get(source);
					if (putNewLine != null) {
						result.append(putNewLine);
						whitespace = true;
					} else {
						if (Html.OUT_STOP.contains(source)) {
							if (waitClose == null) {
								waitClose = new ArrayList<>();
							}
							waitClose.add(source);
						} else {
							if (Html.OUT_START.contains(source)) {
								if (waitClose != null) {
									waitClose.remove(source.substring(1));
									if (waitClose.isEmpty()) {
										waitClose = null;
									}
								}
							} else {
								if (source.regionMatches(true, 0, "IMG ", 0,
										4)) {
									final int altStart = source
											.indexOf(" alt=\"");
									if (altStart == -1) {
										result.append("[IMG]");
									} else {
										final int altStop = source.indexOf('"',
												altStart + 6);
										if (altStop == -1) {
											result.append("[IMG]");
										} else {
											if (altStart + 6 < altStop) {
												result.append('<');
												result.append(buffer.substring(
														altStart + 6, altStop));
												result.append('>');
												whitespace = false;
											}
										}
									}
								}
							}
						}
					}
					buffer.setLength(0);
				}
				continue;
			}
			if (levelTag == 0) {
				if (waitClose != null) {
					continue;
				}
				if (levelEnt > 0) {
					if (c == ';') {
						levelEnt = 0;
						final String entity = buffer.toString().toLowerCase();
						buffer.setLength(0);
						whitespace = false;
						if ("nbsp".equals(entity)) {
							result.append(' ');
							continue;
						}
						if ("lt".equals(entity)) {
							result.append('<');
							continue;
						}
						if ("gt".equals(entity)) {
							result.append('>');
							continue;
						}
						if ("amp".equals(entity)) {
							result.append('&');
							continue;
						}
						if ("copy".equals(entity)) {
							result.append("(c)");
							continue;
						}
						if ("hellip".equals(entity)) {
							result.append("...");
							continue;
						}
						result.append('&').append(entity).append(';');
						continue;
					}
					if (buffer.length() > 6) {
						result.append('&').append(buffer);
						buffer.setLength(0);
						levelEnt = 0;
						continue;
					}
					buffer.append(c);
					continue;
				}
				if (c == '&') {
					levelEnt = 1;
					buffer.setLength(0);
					continue;
				}
				if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
					if (whitespace) {
						continue;
					}
					whitespace = true;
					result.append(' ');
					continue;
				}
				whitespace = false;
				result.append(c);
				continue;
			}
			if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				if (tagWhitespace) {
					continue;
				}
				if (!tagAttributes) {
					tagAttributes = true;
					needAttributes = Html.NEED_ATTRIBUTES
							.contains(buffer.toString().toLowerCase());
				}
			} else {
				tagWhitespace = false;
			}
			if (!tagAttributes || needAttributes) {
				buffer.append(c);
			}
		}
		if (buffer.length() > 0 && levelEnt > 0 && waitClose == null) {
			result.append('&').append(buffer);
		}
		return result.toString();
	}
	
	/**
	 * Converts any occurrence of unwrapped mail@adderess.es to a &lt;a
	 * href="mailto: links
	 * 
	 * @param original
	 *            plain text
	 * @param hrefAttributes
	 * @return html-enchanced
	 */
	public static final String enhanceHtml(final String original,
			final String hrefAttributes) {
			
		final StringBuilder result = new StringBuilder(original);
		{
			int lastFound = result.length();
			for (;;) {
				final int pos = result.lastIndexOf("@", lastFound);
				if (pos == -1) {
					break;
				}
				int left = pos;
				int right = pos;
				for (left--; left >= 0; left--) {
					final char current = result.charAt(left);
					if (current != '.' && current != '-'
							&& !Character.isJavaIdentifierPart(current)) {
						break;
					}
				}
				left++;
				{
					final int length = result.length();
					for (right++; right < length; right++) {
						final char current = result.charAt(right);
						if (current != '.' && current != '-'
								&& !Character.isJavaIdentifierPart(current)) {
							break;
						}
					}
				}
				final boolean mailto;
				if (left > 7 && result.substring(left - 7, left)
						.equalsIgnoreCase("mailto:")) {
					left -= 7;
					mailto = true;
				} else {
					mailto = false;
				}
				lastFound = left - 1;
				if (result.charAt(right - 1) == '.') {
					right--;
				}
				final String address = result.substring(left, right);
				if (address.indexOf('.', pos - left + 1) == -1) {
					continue;
				}
				{
					final int leftAdd = left > 0
							&& (result.charAt(left - 1) == '"'
									|| result.charAt(left - 1) == '\'')
											? -1
											: 0;
					if (left > 5 - leftAdd && result
							.substring(left - 5 + leftAdd, left + leftAdd)
							.equalsIgnoreCase("href=")) {
						continue;
					}
				}
				if (right + 3 < result.length() && result
						.substring(right, right + 3).equalsIgnoreCase("</a")) {
					continue;
				}
				final String prefix = (mailto
						? "<a href=\""
						: "<a href=\"mailto:") //
						+ address //
						+ (hrefAttributes == null
								? "\">"
								: "\" " + hrefAttributes + '>');
				final String suffix = "</a>";
				result.insert(right, suffix);
				result.insert(left, prefix);
			}
		}
		{
			int lastFound = result.length();
			main : for (;;) {
				final int pos = result.lastIndexOf("://", lastFound);
				if (pos == -1) {
					break;
				}
				/**
				 * length can be changed only after these loops
				 */
				final int length = result.length();
				int left = pos - 1;
				local : for (; left >= 0; left--) {
					final char c = result.charAt(left);
					if (Character.isWhitespace(c)
							|| !Character.isJavaIdentifierPart(c) && c != '#'
									&& c != '/' && c != '%' && c != '+'
									&& c != '-' && c != '_' && c != '.'
									&& c != '?' && c != '&' && c != '='
									&& c != '-') {
						break local;
					}
				}
				left++;
				int right = pos + 7;
				local : for (;; right++) {
					if (right >= length) {
						if (right == length) {
							break local;
						}
						break main;
					}
					final char c = result.charAt(right);
					// if(Character.isWhitespace(c)
					// || (!Character.isJavaIdentifierPart(c)
					// && c != '#'
					// && c != '/'
					// && c != '%'
					// && c != '+'
					// && c != '-'
					// && c != '_'
					// && c != '.'
					// && c != '|'
					// && c != ';'
					// && c != '?'
					// && c != ','
					// && c != '&'
					// && c != '=' && c != ':')){
					// break;
					// }
					if (Character.isWhitespace(c) || c == '<' || c == '>'
							|| c == '"') {
						break local;
					}
				}
				lastFound = left - 1;
				/**
				 * extra dot in the end of the name
				 */
				if (result.charAt(right - 1) == '.') {
					right--;
				}
				{
					final int leftAdd = left > 0
							&& (result.charAt(left - 1) == '"'
									|| result.charAt(left - 1) == '\'')
											? -1
											: 0;
					if (left > 5 - leftAdd && result
							.substring(left - 5 + leftAdd, left + leftAdd)
							.equalsIgnoreCase("href=")) {
						continue main;
					}
					if (left > 4 - leftAdd && result
							.substring(left - 4 + leftAdd, left + leftAdd)
							.equalsIgnoreCase("src=")) {
						continue main;
					}
				}
				if (right + 3 < length && result.substring(right, right + 3)
						.equalsIgnoreCase("</a")) {
					continue main;
				}
				final String address = result.substring(left, right);
				final String prefix = "<a href=\"";
				final String suffix = (hrefAttributes == null
						? "\">"
						: "\" " + hrefAttributes + '>')
						+ Text.limitString(address, 75, "...") + "</a>";
				result.insert(right, suffix);
				result.insert(left, prefix);
				lastFound = left;
			}
		}
		return result.toString();
	}
	
	/**
	 * @param text
	 * @param hrefAttributes
	 * @return
	 */
	public static final String fromPlainText(final String text,
			final String hrefAttributes) {
			
		return Html.enhanceHtml(text//
				.trim()//
				.replace("\r", "")//
				.replace("<br>", "\n")//
				.replace("<br/>", "\n")//
				.replace("<p>", "\n\n")//
				.replace("</p>", "")//
				.replace("&#13;", "\n")//
				.replace(" \n", "\n")//
				.replace("\n ", "\n")//
				.replace("\t\n", "\n")//
				.replace("\n\t", "\n")//
				.replace("\n\n\n", "\n\n")//
				.replace("  ", "&nbsp;")//
				.replace("\n\n", "<p>")//
				.replace("\n", "<br>")//
				, hrefAttributes);
	}
	
	/**
	 * @param text
	 * @param hrefAttributes
	 * @return
	 */
	public static final String fromXmlText(final String text,
			final String hrefAttributes) {
			
		return Html.enhanceHtml(text//
				.trim()//
				.replace("\r", "")//
				.replace("<br>", "\n")//
				.replace("<p>", "\n\n")//
				.replace("</p>", "")//
				.replace("&#13;", "\n")//
				.replaceAll("[ \\t\\r]+\n", "\n")//
				.replaceAll("\n[ \\t\\r]+", "\n")//
				.replace("\n\n\n", "\n\n")//
				.replace("  ", "&nbsp;")//
				.replace("\n\n", "<p>")//
				, hrefAttributes);
	}
	
	private Html() {
		// empty
	}
}
