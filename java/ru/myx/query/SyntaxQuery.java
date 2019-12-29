/*
 * Created on 24.07.2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

import ru.myx.ae3.help.Convert;

/** @author myx */
public class SyntaxQuery extends Syntax {

	private static final Object T_OPEN = "(";
	
	private static final int ST_WHITESPACE = 0;
	
	private static final int ST_TOKEN = 1;
	
	private static final int ST_TOKEN_CHECK = 2;
	
	private static final int ST_OPERATOR_CHECK = 3;
	
	private static final int ST_ESCAPE = 4;
	
	private static final List<String> filterToTokens(final String filter) {

		final int length = filter.length();
		final List<String> result = new ArrayList<>(2 + length / 8);
		final StringBuilder token = new StringBuilder();
		String last = null;
		int state = SyntaxQuery.ST_WHITESPACE;
		for (int i = 0; i < length; ++i) {
			final char c = filter.charAt(i);
			switch (state) {
				case ST_WHITESPACE : {
					if (c == '\\') {
						state = SyntaxQuery.ST_ESCAPE;
					} else //
					if (c == '.' || c == '/') {
						// ignore
					} else //
					if (c == '!' || c == '-') {
						result.add(last = Syntax.T_NOT);
					} else //
					if (c == '+' || c == '&') {
						result.add(last = Syntax.T_AND);
					} else //
					if (c == '|' || c == '\\') {
						result.add(last = Syntax.T_OR);
					} else //
					if (c == '_' || c == '$' || Character.isLetterOrDigit(c)) {
						token.append(c);
						state = SyntaxQuery.ST_TOKEN;
					} else {
						result.add(String.valueOf(c));
					}
				}
					break;
				case ST_TOKEN : {
					if (c == '\\') {
						state = SyntaxQuery.ST_ESCAPE;
					} else //
					if (c == '_' || c == '-' || c == '.' || c == '/') {
						token.append(c);
						state = SyntaxQuery.ST_TOKEN_CHECK;
					} else //
					if (c == ':' || c == '=' || c == '<' || c == '>' || c == '~') {
						token.append(c);
						state = SyntaxQuery.ST_OPERATOR_CHECK;
					} else //
					if (c == '$' || c == '*' || c == '?' || Character.isLetterOrDigit(c)) {
						token.append(c);
					} else {
						final String text = token.toString();
						token.setLength(0);
						if (last != Syntax.T_OR && last != Syntax.T_AND && last != Syntax.T_NOT) {
							if ("OR".equalsIgnoreCase(text)) {
								result.add(last = Syntax.T_OR);
							} else //
							if ("AND".equalsIgnoreCase(text)) {
								result.add(last = Syntax.T_AND);
							} else {
								result.add(last = text);
							}
						} else //
						if (last == Syntax.T_AND && "NOT".equalsIgnoreCase(text)) {
							result.add(last = Syntax.T_NOT);
						} else {
							result.add(last = text);
						}
						if (c != '!' && c != '-') {
							result.add(String.valueOf(c));
						}
						state = SyntaxQuery.ST_WHITESPACE;
					}
				}
					break;
				case ST_TOKEN_CHECK : {
					if (c == '\\') {
						state = SyntaxQuery.ST_ESCAPE;
					} else //
					if (c == '$' || c == '*' || c == '?' || Character.isLetterOrDigit(c)) {
						token.append(c);
						state = SyntaxQuery.ST_TOKEN;
					} else {
						final String text = token.substring(0, token.length() - 1);
						token.setLength(0);
						if (last != Syntax.T_OR && last != Syntax.T_AND && last != Syntax.T_NOT) {
							if ("OR".equalsIgnoreCase(text)) {
								result.add(last = Syntax.T_OR);
							} else //
							if ("AND".equalsIgnoreCase(text)) {
								result.add(last = Syntax.T_AND);
							} else {
								result.add(last = text);
							}
						} else //
						if (last == Syntax.T_AND && "NOT".equalsIgnoreCase(text)) {
							result.add(last = Syntax.T_NOT);
						} else {
							result.add(last = text);
						}
						result.add(String.valueOf(c));
						state = SyntaxQuery.ST_WHITESPACE;
					}
				}
					break;
				case ST_OPERATOR_CHECK : {
					if (c == '_' || c == '$' || Character.isLetterOrDigit(c)) {
						token.append(c);
						state = SyntaxQuery.ST_TOKEN;
					} else //
					if (c == '\\') {
						state = SyntaxQuery.ST_ESCAPE;
					} else {
						token.setLength(0);
						state = SyntaxQuery.ST_WHITESPACE;
					}
				}
					break;
				case ST_ESCAPE : {
					token.append(c);
					state = SyntaxQuery.ST_TOKEN;
				}
					break;
				default :
			}
		}
		if (state == SyntaxQuery.ST_TOKEN && token.length() > 0) {
			result.add(token.toString());
		}
		if (state == SyntaxQuery.ST_TOKEN_CHECK && token.length() > 0) {
			result.add(token.substring(0, token.length() - 1));
		}
		return result;
	}
	
	/** @param parsed
	 * @param replacementConditions
	 * @return string */
	public static String filterToWhere(final List<Object> parsed, final Map<String, Function<OneCondition, OneCondition>> replacementConditions) {

		final StringBuilder buffer = new StringBuilder();
		for (final Object current : parsed) {
			if (current == Syntax.T_AND) {
				buffer.append(" AND ");
			} else //
			if (current == Syntax.T_OR) {
				buffer.append(" OR ");
			} else //
			if (current == Syntax.T_NOT) {
				if (buffer.toString().endsWith(" AND ")) {
					buffer.append("NOT ");
				} else {
					buffer.append(" AND NOT ");
				}
			} else //
			if (current instanceof List<?>) {
				@SuppressWarnings("unchecked")
				final List<Object> list = (List<Object>) current;
				buffer.append('(').append(SyntaxQuery.filterToWhere(list, replacementConditions)).append(')');
			} else //
			if (current instanceof OneCondition) {
				final OneCondition conditionOriginal = (OneCondition) current;
				final Function<OneCondition, OneCondition> replacement = Convert.Any.toAny(Convert.MapEntry.toObject(replacementConditions, conditionOriginal.getField(), null));
				final OneCondition condition;
				if (replacement == null) {
					condition = conditionOriginal;
				} else {
					try {
						condition = replacement.apply(conditionOriginal);
					} catch (final RuntimeException e) {
						throw e;
					} catch (final Exception e) {
						throw new RuntimeException(e);
					}
				}
				buffer.append(condition.getField());
				final String operand = condition.getOperator();
				final String value = condition.getValue().replaceAll("'", "''");
				if (":".equals(operand)) {
					buffer.append(" LIKE '%").append(value.replace('*', '%').replace('?', '_')).append("%'");
				} else //
				if ("=".equals(operand)) {
					if (Math.max(value.indexOf('*'), value.indexOf('?')) >= 0) {
						buffer.append(" LIKE '").append(value.replace('*', '%').replace('?', '_')).append('\'');
					} else {
						buffer.append(operand).append('\'').append(value).append('\'');
					}
				} else {
					buffer.append(operand).append('\'').append(value).append('\'');
				}
			} else {
				throw new IllegalArgumentException("Unknown token (" + current + ") in expression: " + parsed);
			}
		}
		return buffer.toString();
	}
	
	/** @param defaultField
	 * @param defaultCondition
	 * @param replacementFields
	 * @param replacementConditions
	 * @param ignoreCase
	 * @param query
	 * @return string */
	public static String filterToWhere(final String defaultField,
			final String defaultCondition,
			final Map<String, String> replacementFields,
			final Map<String, Function<OneCondition, OneCondition>> replacementConditions,
			final boolean ignoreCase,
			final String query) {

		return query == null
			? null
			: SyntaxQuery.filterToWhere(SyntaxQuery.parseFilter(defaultField, defaultCondition, replacementFields, replacementConditions, ignoreCase, query), null);
	}
	
	/** @param defaultField
	 * @param defaultOperation
	 * @param replacementFields
	 * @param replacementConditions
	 * @param ignoreCase
	 * @param filter
	 * @return list */
	public static List<Object> parseFilter(final String defaultField,
			final String defaultOperation,
			final Map<String, String> replacementFields,
			final Map<String, Function<OneCondition, OneCondition>> replacementConditions,
			final boolean ignoreCase,
			final String filter) {

		final List<Object> parsed = new ArrayList<>();
		int scopes = 0;
		boolean logicalAnd = true;
		boolean logicalNot = false;
		boolean exact = false;
		final List<String> tokens = SyntaxQuery.filterToTokens(filter);
		for (final String token : tokens) {
			final String field;
			final String operand;
			final String value;
			{
				final String wrd = token;
				if (wrd.length() <= 1) {
					field = null;
					operand = "?";
					value = wrd;
				} else //
				if (wrd.length() < 3) {
					field = defaultField;
					operand = defaultOperation == null
						? "?"
						: defaultOperation;
					value = ignoreCase
						? wrd.toLowerCase()
						: wrd;
				} else {
					final int pos1 = wrd.indexOf(':');
					final int pos2 = wrd.indexOf('=');
					final int pos3 = wrd.indexOf('<');
					final int pos4 = wrd.indexOf('>');
					final int pos5 = wrd.indexOf('~');
					if (pos1 != -1) {
						field = wrd.substring(0, pos1);
						operand = String.valueOf(wrd.charAt(pos1));
						value = ignoreCase
							? wrd.substring(pos1 + 1).toLowerCase()
							: wrd.substring(pos1 + 1);
					} else //
					if (pos2 != -1) {
						field = wrd.substring(0, pos2);
						operand = String.valueOf(wrd.charAt(pos2));
						value = ignoreCase
							? wrd.substring(pos2 + 1).toLowerCase()
							: wrd.substring(pos2 + 1);
					} else //
					if (pos3 != -1) {
						field = wrd.substring(0, pos3);
						operand = String.valueOf(wrd.charAt(pos3));
						value = ignoreCase
							? wrd.substring(pos3 + 1).toLowerCase()
							: wrd.substring(pos3 + 1);
					} else //
					if (pos4 != -1) {
						field = wrd.substring(0, pos4);
						operand = String.valueOf(wrd.charAt(pos4));
						value = ignoreCase
							? wrd.substring(pos4 + 1).toLowerCase()
							: wrd.substring(pos4 + 1);
					} else //
					if (pos5 != -1) {
						field = wrd.substring(0, pos5);
						operand = String.valueOf(wrd.charAt(pos5));
						value = ignoreCase
							? wrd.substring(pos5 + 1).toLowerCase()
							: wrd.substring(pos5 + 1);
					} else {
						field = defaultField;
						operand = defaultOperation == null
							? "?"
							: defaultOperation;
						value = ignoreCase
							? wrd.toLowerCase()
							: wrd;
					}
				}
			}
			if (field != null) {
				if (parsed.size() > 0 && parsed.get(parsed.size() - 1) != SyntaxQuery.T_OPEN) {
					parsed.add(
							logicalAnd
								? Syntax.T_AND
								: Syntax.T_OR);
				}
				if (logicalNot && (parsed.size() == 0 || parsed.get(parsed.size() - 1) != Syntax.T_NOT)) {
					parsed.add(Syntax.T_NOT);
				}
				final OneCondition conditionOriginal = new OneConditionSimple(
						exact
							? true
							: field == defaultField && value.length() > 2
								? false
								: true,
						Convert.MapEntry.toString(replacementFields, field, field),
						operand,
						value);
				final Function<OneCondition, OneCondition> replacement = Convert.Any.toAny(Convert.MapEntry.toObject(replacementConditions, conditionOriginal.getField(), null));
				final OneCondition condition;
				if (replacement == null) {
					condition = conditionOriginal;
				} else {
					try {
						condition = replacement.apply(conditionOriginal);
					} catch (final RuntimeException e) {
						throw e;
					} catch (final Exception e) {
						throw new RuntimeException(e);
					}
				}
				parsed.add(condition);
				logicalAnd = true;
				logicalNot = false;
			} else {
				if (value.equals("\"") || value.equals("'")) {
					exact = !exact;
				} else //
				if (value.equals("|") || value.equals("/")) {
					logicalAnd = false;
				} else //
				if (value.equals("&")) {
					logicalAnd = true;
				} else //
				if (value.equals("!") || value.equals("-")) {
					logicalNot = true;
				} else //
				if (value.equals("(")) {
					++scopes;
					if (parsed.size() > 0 && parsed.get(parsed.size() - 1) != SyntaxQuery.T_OPEN) {
						parsed.add(
								logicalAnd
									? Syntax.T_AND
									: Syntax.T_OR);
					}
					if (logicalNot && parsed.size() > 0 && parsed.get(parsed.size() - 1) != Syntax.T_NOT) {
						parsed.add(Syntax.T_NOT);
					}
					parsed.add(SyntaxQuery.T_OPEN);
					logicalNot = false;
				} else //
				if (value.equals(")")) {
					if (scopes > 0) {
						--scopes;
						final int pos = parsed.lastIndexOf(SyntaxQuery.T_OPEN);
						if (pos != -1) {
							final int size = parsed.size() - pos - 1;
							if (size == 0 || size == 1) {
								parsed.remove(pos);
							} else {
								final List<Object> scope = new ArrayList<>(parsed.size() - pos);
								scope.addAll(parsed.subList(pos + 1, parsed.size()));
								while (parsed.size() > pos) {
									parsed.remove(pos);
								}
								parsed.add(scope);
							}
						}
					}
				}
			}
		}
		while (scopes >= 0) {
			scopes--;
			final int pos = parsed.lastIndexOf(SyntaxQuery.T_OPEN);
			if (pos != -1) {
				final int size = parsed.size() - pos - 1;
				if (size == 0 || size == 1) {
					parsed.remove(pos);
				} else {
					final List<Object> scope = new ArrayList<>(parsed.size() - pos);
					scope.addAll(parsed.subList(pos + 1, parsed.size()));
					while (parsed.size() > pos) {
						parsed.remove(pos);
					}
					parsed.add(scope);
				}
			}
		}
		return parsed;
	}
	
	/** @param target
	 * @param replacementOrder
	 * @param replacementFields
	 * @param order
	 * @return list */
	public static List<Object> parseOrder(final List<Object> target, final Map<?, ?> replacementOrder, final Map<?, ?> replacementFields, final String order) {

		final List<Object> result = target == null
			? new ArrayList<>()
			: target;
		for (final StringTokenizer st = new StringTokenizer(order, ","); st.hasMoreTokens();) {
			final String element = st.nextToken();
			final int elementLength = element.length();
			if (elementLength == 0) {
				continue;
			}
			final OneSort replacement = (OneSort) Convert.MapEntry.toObject(replacementOrder, element, null);
			if (replacement == null) {
				final String fieldName;
				final boolean descending = element.endsWith("-");
				final boolean cut = descending | element.endsWith("+");
				final boolean textual;
				final boolean numeric;
				if (element.charAt(0) == '#') {
					fieldName = cut
						? element.substring(1, elementLength - 1)
						: element.substring(1);
					textual = false;
					numeric = true;
				} else //
				if (element.charAt(0) == '^') {
					fieldName = cut
						? element.substring(1, elementLength - 1)
						: element.substring(1);
					textual = true;
					numeric = false;
				} else {
					fieldName = cut
						? element.substring(0, elementLength - 1)
						: element;
					textual = false;
					numeric = false;
				}
				final String realName = Convert.MapEntry.toString(replacementFields, fieldName, fieldName);
				result.add(new OneSort.Simple(realName, textual, numeric, descending));
			} else {
				result.add(replacement);
			}
		}
		return result;
	}
}
