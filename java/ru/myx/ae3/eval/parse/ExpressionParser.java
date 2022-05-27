/*
 * Created on 29.10.2003
 *
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.BalanceType;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.IAVV_ARGS_CREATEN_X_X_R;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
public final class ExpressionParser {
	
	private static final BasePrimitiveString STRING_REGEXP_CONSTRUCTOR_NAME = Base.forString("RegExp");
	
	private static final byte[] CHARS = new byte[65536];
	
	private static final BaseObject RESERVED_OBJECT = new BaseNativeObject();
	
	private static final Map<String, TokenValue> DEFAULT_VARIABLES = new HashMap<>(256, 0.25f);
	
	private static final int ST_REGEXP_FLAGS = 30;
	
	private static final int ST_REGEXP_START = 29;
	
	private static final int ST_1STRING_FUNCTION_DECL = 28;
	
	private static final int ST_2STRING_FUNCTION_DECL = 27;
	
	private static final int ST_FUNCTION_DECL_BODY = 26;
	
	private static final int ST_FUNCTION_DECL_MORE = 25;
	
	private static final int ST_FUNCTION_DECL_ARGUMENT = 24;
	
	private static final int ST_FUNCTION_DECL_ARGS = 23;
	
	private static final int ST_FUNCTION_DECL_NAME = 22;
	
	private static final int ST_1STRING = 21;
	
	private static final int ST_1STRING_FUNCTION_ROOT = 20;
	
	private static final int ST_1STRING_FUNCTION_NEW = 19;
	
	private static final int ST_1STRING_FUNCTION_ACCS = 18;
	
	private static final int ST_2STRING = 17;
	
	private static final int ST_2STRING_FUNCTION_ROOT = 16;
	
	private static final int ST_2STRING_FUNCTION_NEW = 15;
	
	private static final int ST_2STRING_FUNCTION_ACCS = 14;
	
	private static final int ST_CODE = 13;
	
	private static final int ST_FUNCTION_NEW = 12;
	
	private static final int ST_FUNCTION_ROOT = 11;
	
	private static final int ST_FUNCTION_ACCS = 10;
	
	private static final int ST_IDENTIFIER_CONST = 9;
	
	private static final int ST_IDENTIFIER_NEW = 8;
	
	private static final int ST_IDENTIFIER_NEW_ACCS = 7;
	
	private static final int ST_IDENTIFIER_ROOT = 6;
	
	private static final int ST_IDENTIFIER_ACCS = 5;
	
	private static final int ST_NUMBER = 4;
	
	private static final int ST_NUMBER_WITH_EXP = 3;
	
	private static final int ST_NUMBER_FLOAT = 2;
	
	private static final int ST_NUMBER_HEX = 1;
	
	private static final int ST_NUMBER_OCT = 31;
	
	private static final int ST_NUMBER_BIN = 32;
	
	private static final int ST_WHITESPACE = 0;
	
	private static final TokenValue MA_CONSTRUCTOR = ParseConstants.getConstantValue(Base.forString("constructor"));
	
	private static final Map<String, TokenInstruction> ACCESS_SOURCES = Create.privateMap(512);
	
	private static int EXPR_BUILTIN_HITS = 0;
	
	private static int EXPR_PUBLIC_COUNT = 0;
	
	private static int EXPR_PRIVATE_COUNT = 0;
	
	private static int ERROR_COUNT = 0;
	
	private static int EXCEPTION_COUNT = 0;
	
	static {
		for (int i = 65535; i >= 0; --i) {
			final char c = (char) i;
			ExpressionParser.CHARS[i] = (byte) (0//
					+ (Character.isWhitespace(c)
						? 0x01
						: 0x00) //
					+ (Character.isJavaIdentifierStart(c)
						? 0x02
						: 0x00)
					+ (Character.isJavaIdentifierPart(c)
						? 0x04
						: 0x00) //
					/** OLD <code>
							+ (Character.isDigit( c )
									? 0x08
									: 0x00)//
									</code> */
					+ 0);
		}
	}
	
	static {
		for (final String key : new String[]{ //
				"abstract", //
				"break", //
				"boolean", //
				"byte", //
				"case", //
				"catch", //
				"char", //
				"class", //
				"continue", //
				"const", //
				"debugger", //
				"default", //
				"delete", //
				"do", //
				"double", //
				"else", //
				"enum", //
				"export", //
				"extends", //
				"for", //
				"function", //
				"final", //
				"float", //
				"finally", //
				"goto", //
				"interface", //
				"instanceof", //
				"implements", //
				"import", //
				"int", //
				"if", //
				"in", //
				"long", //
				"new", //
				"native", //
				"package", //
				"private", //
				"protected", //
				"public", //
				"return", //
				"short", //
				"static", //
				"super", //
				"switch", //
				"synchronized", //
				"throws", //
				"this", //
				"throw", //
				"try", //
				"typeof", //
				"transient", //
				"var", //
				"void", //
				"volatile", //
				"while", //
				"with", //
		}) {
			ExpressionParser.DEFAULT_VARIABLES.put(key, new TKV_LCONST(ExpressionParser.RESERVED_OBJECT, key));
		}
		ExpressionParser.DEFAULT_VARIABLES.put("this", ParseConstants.TKV_THIS);
		ExpressionParser.DEFAULT_VARIABLES.put("-1", ParseConstants.TKV_NUMBER_MINUSONE);
		ExpressionParser.DEFAULT_VARIABLES.put("", ParseConstants.TKV_STRING_EMPTY);
		ExpressionParser.DEFAULT_VARIABLES.put("''", ParseConstants.TKV_STRING_EMPTY);
		ExpressionParser.DEFAULT_VARIABLES.put("\"\"", ParseConstants.TKV_STRING_EMPTY);
		ExpressionParser.DEFAULT_VARIABLES.put("0", ParseConstants.getValue(0));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0", ParseConstants.getValue(0));
		ExpressionParser.DEFAULT_VARIABLES.put("0x00", ParseConstants.getValue(0));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0000", ParseConstants.getValue(0));
		ExpressionParser.DEFAULT_VARIABLES.put("+0", ParseConstants.getValue(0));
		ExpressionParser.DEFAULT_VARIABLES.put("1", ParseConstants.getValue(1));
		ExpressionParser.DEFAULT_VARIABLES.put("0x1", ParseConstants.getValue(1));
		ExpressionParser.DEFAULT_VARIABLES.put("0x01", ParseConstants.getValue(1));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0001", ParseConstants.getValue(1));
		ExpressionParser.DEFAULT_VARIABLES.put("+1", ParseConstants.getValue(1));
		ExpressionParser.DEFAULT_VARIABLES.put("2", ParseConstants.getValue(2));
		ExpressionParser.DEFAULT_VARIABLES.put("0x2", ParseConstants.getValue(2));
		ExpressionParser.DEFAULT_VARIABLES.put("0x02", ParseConstants.getValue(2));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0002", ParseConstants.getValue(2));
		ExpressionParser.DEFAULT_VARIABLES.put("+2", ParseConstants.getValue(2));
		ExpressionParser.DEFAULT_VARIABLES.put("3", ParseConstants.getValue(3));
		ExpressionParser.DEFAULT_VARIABLES.put("0x3", ParseConstants.getValue(3));
		ExpressionParser.DEFAULT_VARIABLES.put("0x03", ParseConstants.getValue(3));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0003", ParseConstants.getValue(3));
		ExpressionParser.DEFAULT_VARIABLES.put("4", ParseConstants.getValue(4));
		ExpressionParser.DEFAULT_VARIABLES.put("0x4", ParseConstants.getValue(4));
		ExpressionParser.DEFAULT_VARIABLES.put("0x04", ParseConstants.getValue(4));
		ExpressionParser.DEFAULT_VARIABLES.put("0x0004", ParseConstants.getValue(4));
		ExpressionParser.DEFAULT_VARIABLES.put("5", ParseConstants.getValue(5));
		ExpressionParser.DEFAULT_VARIABLES.put("6", ParseConstants.getValue(6));
		ExpressionParser.DEFAULT_VARIABLES.put("7", ParseConstants.getValue(7));
		ExpressionParser.DEFAULT_VARIABLES.put("8", ParseConstants.getValue(8));
		ExpressionParser.DEFAULT_VARIABLES.put("9", ParseConstants.getValue(9));
		ExpressionParser.DEFAULT_VARIABLES.put("10", ParseConstants.getValue(10));
		ExpressionParser.DEFAULT_VARIABLES.put("11", ParseConstants.getValue(11));
		ExpressionParser.DEFAULT_VARIABLES.put("12", ParseConstants.getValue(12));
		ExpressionParser.DEFAULT_VARIABLES.put("13", ParseConstants.getValue(13));
		ExpressionParser.DEFAULT_VARIABLES.put("14", ParseConstants.getValue(14));
		ExpressionParser.DEFAULT_VARIABLES.put("15", ParseConstants.getValue(15));
		ExpressionParser.DEFAULT_VARIABLES.put("true", ParseConstants.TKV_BOOLEAN_TRUE);
		ExpressionParser.DEFAULT_VARIABLES.put("false", ParseConstants.TKV_BOOLEAN_FALSE);
		ExpressionParser.DEFAULT_VARIABLES.put("NaN", ParseConstants.TKV_NUMBER_NAN);
		ExpressionParser.DEFAULT_VARIABLES.put("+NaN", ParseConstants.TKV_NUMBER_NAN);
		ExpressionParser.DEFAULT_VARIABLES.put("null", ParseConstants.TKV_NULL);
		ExpressionParser.DEFAULT_VARIABLES.put("undefined", ParseConstants.TKV_UNDEFINED);
		ExpressionParser.DEFAULT_VARIABLES.put("Infinity", ParseConstants.TKV_NUMBER_PINFINITY);
		ExpressionParser.DEFAULT_VARIABLES.put("+Infinity", ParseConstants.TKV_NUMBER_PINFINITY);
		ExpressionParser.DEFAULT_VARIABLES.put("-Infinity", ParseConstants.TKV_NUMBER_NINFINITY);
		
		ExpressionParser.DEFAULT_VARIABLES.put("[]", TKV_CARRAY0.INSTANCE);
		ExpressionParser.DEFAULT_VARIABLES.put("{}", TKV_COBJECT_EMPTY.INSTANCE);
	}
	
	private static final void addIdentifierAccess(final List<TokenInstruction> precompiled, final String fieldName) {
		
		TokenInstruction result = ExpressionParser.ACCESS_SOURCES.get(fieldName);
		if (result == null) {
			synchronized (ExpressionParser.ACCESS_SOURCES) {
				result = ExpressionParser.ACCESS_SOURCES.get(fieldName);
				if (result == null) {
					ExpressionParser.ACCESS_SOURCES.put(fieldName, result = new TKO_ACCESS_BA_VS_S(new TKV_LCONSTS(Base.forString(fieldName))));
				}
			}
		}
		precompiled.add(result);
	}
	
	private static final void addIdentifierConst(final ProgramAssembly assembly, final List<TokenInstruction> precompiled, final String token) {
		
		precompiled.add(assembly.constantToken(Integer.parseInt(token)));
	}
	
	private static final void addIdentifierRoot(final List<TokenInstruction> precompiled, final String token, final String expression) {
		
		final TokenValue known = TKV_FLOAD_A_Cs_S.getInstance(token, ExpressionParser.DEFAULT_VARIABLES);
		if (known.toConstantValue() != ExpressionParser.RESERVED_OBJECT) {
			precompiled.add(known);
			return;
		}
		if (Report.MODE_DEBUG) {
			Report.warning("EVALUATE", "Reserved word: " + token + ", expression: " + expression);
		}
		precompiled.add(new TKV_FLOAD_A_Cs_S(Base.forString(token)));
	}
	
	private static final void addNumber(final List<TokenInstruction> precompiled, final String token, final char type) {
		
		try {
			final long number = Long.parseLong(token);
			final int size = precompiled.size();
			if (size > 0 && precompiled.get(size - 1) == ParseConstants.TKO_MSUB_BA_S0) {
				final TokenValue value;
				value = type == 'L'
					? number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number)
					: number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number);
				precompiled.set(size - 1, value);
			} else {
				final TokenValue value;
				value = type == 'L'
					? ParseConstants.getValue(number)
					: ParseConstants.getValue(number);
				precompiled.add(value);
			}
		} catch (final NumberFormatException e) {
			throw new RuntimeException("Not a valid numeric constant: " + token);
		}
	}
	
	private static final void addNumberBin(final List<TokenInstruction> precompiled, final String token, final char type) {
		
		if (token.length() == 0) {
			throw new RuntimeException("Illegal binary constant: 0x" + token);
		}
		try {
			final long number = Long.parseLong(token, 2);
			final int size = precompiled.size();
			if (size > 0 && precompiled.get(size - 1) == ParseConstants.TKO_MSUB_BA_S0) {
				final TokenValue value;
				value = type == 'L'
					? number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number)
					: number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number);
				precompiled.set(size - 1, value);
			} else {
				final TokenValue value;
				value = type == 'L'
					? ParseConstants.getValue(number)
					: ParseConstants.getValue(number);
				precompiled.add(value);
			}
		} catch (final NumberFormatException e) {
			throw new RuntimeException("Not a valid binary integer: " + token);
		}
	}
	
	private static final void addNumberFloat(final List<TokenInstruction> precompiled, final String token, final char type) {
		
		try {
			final int size = precompiled.size();
			if (size > 0 && precompiled.get(size - 1) == ParseConstants.TKO_MSUB_BA_S0) {
				final TokenValue value;
				/** floating (not rounded) numbers are not pre-cached */
				value = type == 'F'
					? new TKV_LCONSTN(Base.forDouble(-Float.parseFloat(token)))
					: new TKV_LCONSTN(Base.forDouble(-Double.parseDouble(token)));
				precompiled.set(size - 1, value);
			} else {
				final TokenValue value;
				/** floating (not rounded) numbers are not pre-cached */
				value = type == 'F'
					? new TKV_LCONSTN(Base.forDouble(Float.parseFloat(token)))
					: new TKV_LCONSTN(Base.forDouble(Double.parseDouble(token)));
				precompiled.add(value);
			}
		} catch (final NumberFormatException e) {
			throw new RuntimeException("Not a valid numeric constant: " + token);
		}
	}
	
	private static final void addNumberHex(final List<TokenInstruction> precompiled, final String token, final char type) {
		
		if (token.length() == 0) {
			throw new RuntimeException("Illegal hexadecimal constant: 0x" + token);
		}
		try {
			final long number = Long.parseLong(token, 16);
			final int size = precompiled.size();
			if (size > 0 && precompiled.get(size - 1) == ParseConstants.TKO_MSUB_BA_S0) {
				final TokenValue value;
				value = type == 'L'
					? number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number)
					: number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number);
				precompiled.set(size - 1, value);
			} else {
				final TokenValue value;
				value = type == 'L'
					? ParseConstants.getValue(number)
					: ParseConstants.getValue(number);
				precompiled.add(value);
			}
		} catch (final NumberFormatException e) {
			throw new RuntimeException("Not a valid hexadecimal integer: " + token);
		}
	}
	
	private static final void addNumberOct(final List<TokenInstruction> precompiled, final String token, final char type) {
		
		if (token.length() == 0) {
			throw new RuntimeException("Illegal octal constant: 0x" + token);
		}
		try {
			final long number = Long.parseLong(token, 8);
			final int size = precompiled.size();
			if (size > 0 && precompiled.get(size - 1) == ParseConstants.TKO_MSUB_BA_S0) {
				final TokenValue value;
				value = type == 'L'
					? number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number)
					: number == 1
						? ParseConstants.TKV_NUMBER_MINUSONE
						: ParseConstants.getValue(-number);
				precompiled.set(size - 1, value);
			} else {
				final TokenValue value;
				value = type == 'L'
					? ParseConstants.getValue(number)
					: ParseConstants.getValue(number);
				precompiled.add(value);
			}
		} catch (final NumberFormatException e) {
			throw new RuntimeException("Not a valid octal integer: " + token);
		}
	}
	
	private static final void addOperator(final ProgramAssembly assembly, final List<TokenInstruction> precompiled, final String token) throws Throwable {
		
		final int length = token.length();
		switch (token.charAt(0)) {
			case '=' : {
				if (length == 1) {
					/** = */
					final int size = precompiled.size();
					if (size == 0) {
						precompiled.add(ParseConstants.TKA_OUTPUT_A_S_S);
						return;
					}
					final TokenInstruction last = precompiled.get(size - 1);
					if (last == ParseConstants.TKV_THIS) {
						throw new RuntimeException("Cannot assign to 'this'!");
					}
					if (last == ParseConstants.TKS_COMMA || last == ParseConstants.TKS_SEMICOLON || last == ParseConstants.TKS_BRACE_OPEN) {
						precompiled.add(ParseConstants.TKA_OUTPUT_A_S_S);
						return;
					}
					if (last instanceof TKV_FLOAD_A_Cs_S) {
						/** used for named arguments parser */
						precompiled.set(size - 1, new TKA_ASSIGN_FSTORE_BA_SC_S(last.toContextPropertyName()));
						return;
					}
					if (last.isAccessReference()) {
						precompiled.add(ParseConstants.TKS_ASSIGNMENT);
						// precompiled.set( size - 1, ((AccessReference)
						// last).toReferenceWrite() );
						return;
					}
					throw new RuntimeException(
							"Invalid '=' operator usage, reference expected to be on the left side, previous token class=" + last.getClass().getName() + ", notation="
									+ last.getNotation() + ", precompiled=" + precompiled);
				}
				/** length > 1 for sure */
				final char second = token.charAt(1);
				if (second == '=') {
					if (length == 2) {
						/** == */
						precompiled.add(ParseConstants.TKO_BEQU_BA_SS_S);
						return;
					}
					/** length > 2 for sure */
					if (token.charAt(2) == '=') {
						/** === */
						precompiled.add(ParseConstants.TKO_BSEQU_BA_SS_S);
						if (length > 3) {
							ExpressionParser.addOperator(assembly, precompiled, token.substring(3));
						}
						return;
					}
					{
						/** == ... */
						precompiled.add(ParseConstants.TKO_BEQU_BA_SS_S);
						ExpressionParser.addOperator(assembly, precompiled, token.substring(2));
						return;
					}
				}
				/** = ... */
				ExpressionParser.addOperator(assembly, precompiled, "=");
				ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				return;
			}
			case '<' : {
				if (length == 1) {
					/** < */
					precompiled.add(ParseConstants.TKO_BLESS_BA_SS_S);
					return;
				}
				final char second = token.charAt(1);
				if (length == 2) {
					if (second == '=') {
						/** <= */
						precompiled.add(ParseConstants.TKO_BNMORE_BA_SS_S);
						return;
					}
					if (second == '<') {
						/** << */
						precompiled.add(ParseConstants.TKO_MSHL_BA_SS);
						return;
					}
					break;
				}
				if (length == 3) {
					if (second == '<' && token.charAt(2) == '=') {
						/** <<= */
						precompiled.add(ParseConstants.TKA_ASSIGN_MSHL);
						return;
					}
				}
				break;
			}
			case '>' : {
				if (length == 1) {
					/** > */
					precompiled.add(ParseConstants.TKO_BMORE_BA_SS_S);
					return;
				}
				final char second = token.charAt(1);
				switch (length) {
					case 2 :
						if (second == '=') {
							/** >= */
							precompiled.add(ParseConstants.TKO_BNLESS_BA_SS_S);
							return;
						}
						if (second == '>') {
							/** >> */
							precompiled.add(ParseConstants.TKO_MSHRS_BA_SS);
							return;
						}
						break;
					case 3 :
						if (second == '>' && token.charAt(2) == '>') {
							/** >>> */
							precompiled.add(ParseConstants.TKO_MSHRU_BA_SS);
							return;
						}
						if (second == '>' && token.charAt(2) == '=') {
							/** >>= */
							precompiled.add(ParseConstants.TKA_ASSIGN_MSHRS);
							return;
						}
						break;
					case 4 :
						if (second == '>' && token.charAt(2) == '>' && token.charAt(3) == '=') {
							/** >>>= */
							precompiled.add(ParseConstants.TKA_ASSIGN_MSHRU);
							return;
						}
						//$FALL-THROUGH$
					default :
						/** length >= 4 */
						if (second == '>') {
							final char third = token.charAt(2);
							if (third == '>') {
								/** length > 4 cause we checked this explicitly in case 4 */
								if (token.charAt(3) == '=') {
									/** >>>= ... */
									precompiled.add(ParseConstants.TKA_ASSIGN_MSHRU);
									ExpressionParser.addOperator(assembly, precompiled, token.substring(4));
									return;
								}
								/** >>> ... */
								precompiled.add(ParseConstants.TKO_MSHRU_BA_SS);
								ExpressionParser.addOperator(assembly, precompiled, token.substring(3));
								return;
							}
							if (third == '=') {
								/** >>= ... */
								precompiled.add(ParseConstants.TKA_ASSIGN_MSHRS);
								ExpressionParser.addOperator(assembly, precompiled, token.substring(3));
								return;
							}
						}
				}
				if (length > 2) {
					if (second == '=') {
						/** >= ... */
						precompiled.add(ParseConstants.TKO_BNLESS_BA_SS_S);
						ExpressionParser.addOperator(assembly, precompiled, token.substring(2));
						return;
					}
					if (second == '>') {
						/** >> ... */
						precompiled.add(ParseConstants.TKO_MSHRS_BA_SS);
						ExpressionParser.addOperator(assembly, precompiled, token.substring(2));
						return;
					}
				}
				/** > ... */
				precompiled.add(ParseConstants.TKO_BMORE_BA_SS_S);
				ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				return;
			}
			case '!' : {
				if (length == 1) {
					/** ! */
					precompiled.add(ParseConstants.TKO_BNOT_A_S_S);
					return;
				}
				/** length > 1 for sure */
				final char second = token.charAt(1);
				switch (length) {
					case 2 :
						if (second == '=') {
							/** != */
							precompiled.add(ParseConstants.TKO_BNEQU_BA_SS_S);
							return;
						}
						if (second == '!') {
							/** !! */
							precompiled.add(ParseConstants.TKO_BCVT_A_S_S);
							return;
						}
						break;
					case 3 :
						if (second == '=' && token.charAt(2) == '=') {
							/** !== */
							precompiled.add(ParseConstants.TKO_BSNEQU_BA_SS_S);
							return;
						}
						/** includes !!X */
						//$FALL-THROUGH$
					default :
						/** length >= 3 */
						if (second == '=') {
							/** length > 3 cause we checked this explicitly in case 3 */
							if (token.charAt(2) == '=') {
								/** !==... */
								precompiled.add(ParseConstants.TKO_BSNEQU_BA_SS_S);
								ExpressionParser.addOperator(assembly, precompiled, token.substring(3));
								return;
							}
							/** !=... */
							precompiled.add(ParseConstants.TKO_BNEQU_BA_SS_S);
							ExpressionParser.addOperator(assembly, precompiled, token.substring(2));
							return;
						}
						if (second == '!') {
							/** !!... */
							precompiled.add(ParseConstants.TKO_BCVT_A_S_S);
							ExpressionParser.addOperator(assembly, precompiled, token.substring(2));
							return;
						}
				}
				{
					/** !... */
					precompiled.add(ParseConstants.TKO_BNOT_A_S_S);
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
					return;
				}
			}
			case '&' :
				if (length == 1) {
					/** & */
					precompiled.add(ParseConstants.TKO_MAND_BA_SS);
					return;
				}
				if (length == 2 && token.charAt(1) == '&') {
					/** && */
					precompiled.add(ParseConstants.TKS_EAND);
					return;
				}
				if (length == 2 && token.charAt(1) == '=') {
					/** &= */
					final int size = precompiled.size();
					if (size > 0) {
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MAND);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
					throw new RuntimeException("Addressable value required!!");
				}
				if (length == 3) {
					if (token.charAt(1) == '&' && token.charAt(2) == '=') {
						/** &&= */
						final int size = precompiled.size();
						if (size == 0) {
							throw new RuntimeException("Addressable value required!!");
						}
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_ELAA_A_S_S);
							return;
						}
						throw new RuntimeException("Invalid " + token + " operator usage, previous token type=" + last.getClass().getName() + "!");
					}
				}
				break;
			case '|' :
				if (length == 1) {
					precompiled.add(ParseConstants.TKO_MOR_BA_SS);
					return;
				}
				if (length == 2 && token.charAt(1) == '|') {
					/** || */
					precompiled.add(ParseConstants.TKS_EOR);
					return;
				}
				if (length == 2 && token.charAt(1) == '=') {
					/** |= */
					final int size = precompiled.size();
					if (size > 0) {
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MOR);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
					throw new RuntimeException("Addressable value required!!");
				}
				if (length == 3) {
					if (token.charAt(1) == '|' && token.charAt(2) == '=') {
						/** ||= */
						final int size = precompiled.size();
						if (size == 0) {
							throw new RuntimeException("Addressable value required!!");
						}
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_ELOA_A_S_S);
							return;
						}
						throw new RuntimeException("Invalid " + token + " operator usage, previous token type=" + last.getClass().getName() + "!");
					}
				}
				break;
			case '^' :
				if (length == 1) {
					/** ^ */
					precompiled.add(ParseConstants.TKO_MXOR_BA_SS);
					return;
				}
				if (length == 2 && token.charAt(1) == '=') {
					/** ^= */
					final int size = precompiled.size();
					if (size > 0) {
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MXOR);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
					throw new RuntimeException("Addressable value required!!");
				}
				break;
			case '~' :
				/** ~ */
				precompiled.add(ParseConstants.TKO_MNOT_A_S_S);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case '+' :
				if (length == 1) {
					/** + */
					final int size = precompiled.size();
					if (size == 0) {
						precompiled.add(ParseConstants.TKO_ZCVTN_A_S);
						return;
					}
					final TokenInstruction left = precompiled.get(size - 1);
					if (left == ParseConstants.TKV_STRING_EMPTY) {
						precompiled.set(size - 1, ParseConstants.TKO_ZCVTS_A_S);
						return;
					}
					precompiled.add(
							left.isParseValueRight()
								? ParseConstants.TKO_MADD_BA_SS
								: ParseConstants.TKO_ZCVTN_A_S);
					return;
				}
				if (length == 2) {
					final char second = token.charAt(1);
					if (second == '+') {
						/** ++ */
						final int size = precompiled.size();
						final TokenInstruction last = size == 0
							? null
							: precompiled.get(size - 1);
						if (last instanceof TKV_FLOAD_A_Cs_S) {
							precompiled.set(size - 1, new TKV_FRAMEPOSTINC(last.toContextPropertyName()));
							return;
						}
						if (last != null && last.isAccessReference()) {
							precompiled.add(ParseConstants.TKO_POSTINC_A_L);
							return;
						}
						precompiled.add(ParseConstants.TKO_PREINC_A_L_S);
						return;
					}
					if (second == '=') {
						/** += */
						final int size = precompiled.size();
						if (size == 0) {
							throw new RuntimeException("Addressable value required!!");
						}
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MADD);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
				}
				break;
			case '-' :
				if (length == 1) {
					/** - */
					final int size = precompiled.size();
					precompiled.add(
							size > 0 && precompiled.get(size - 1).isParseValueRight()
								? ParseConstants.TKO_MSUB_BA_SS
								: ParseConstants.TKO_MSUB_BA_S0);
					return;
				}
				if (length == 2) {
					final char second = token.charAt(1);
					if (second == '-') {
						/** -- */
						final int size = precompiled.size();
						final TokenInstruction last = size == 0
							? null
							: precompiled.get(size - 1);
						if (last instanceof TKV_FLOAD_A_Cs_S) {
							precompiled.set(size - 1, new TKV_FRAMEPOSTDEC(last.toContextPropertyName()));
							return;
						}
						if (last != null && last.isAccessReference()) {
							precompiled.add(ParseConstants.TKO_POSTDEC_A_L);
							return;
							/** <code>
							final AccessReference reference = (AccessReference) last;
							final int start = assembly.size();
							reference.toReferenceRead( start, assembly );
							assembly.addInstruction( start, Instructions.INSTR_MSUB_BA_1S_S );
							reference.toReferenceWrite( start, assembly );
							precompiled.set( size - 1, assembly.toTokenAssignment( start,
									0 + last.getOperandCount(),
									InstructionResult.NUMBER ) );
							return;
							</code> */
						}
						precompiled.add(ParseConstants.TKO_PREDEC_A_L_S);
						return;
					}
					if (second == '=') {
						/** -= */
						final int size = precompiled.size();
						if (size > 0) {
							final TokenInstruction last = precompiled.get(size - 1);
							if (last.isAccessReference()) {
								precompiled.add(ParseConstants.TKA_ASSIGN_MSUB);
								return;
								/** <code>
								final AccessReference reference = (AccessReference) last;
								final int start = assembly.size();
								reference.toReferenceRead( start, assembly );
								assembly.addInstruction( start, ParseConstants.TOKEN_MSUB_BA_SS_S
										.getInstruction( assembly.ctx,
												ModifierArguments.AASP_POP,
												ModifierArguments.AASP_POP,
												null ) );
								reference.toReferenceWrite( start, assembly );
								precompiled.set( size - 1, assembly.toTokenAssignment( start,
										1 + last.getOperandCount(),
										InstructionResult.OBJECT ) );
								return;
								</code> */
							}
							throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
						}
						throw new RuntimeException("Addressable value required!!");
					}
				}
				break;
			case '*' :
				if (length == 1) {
					/** * */
					precompiled.add(ParseConstants.TKO_MMUL_BA_SS);
					return;
				}
				if (length == 2) {
					final char second = token.charAt(1);
					if (second == '*') {
						/** ** */
						precompiled.add(ParseConstants.TKO_MPOW_BA_SS);
						return;
					}
					if (second == '=') {
						/** *= */
						final int size = precompiled.size();
						if (size > 0) {
							final TokenInstruction last = precompiled.get(size - 1);
							if (last.isAccessReference()) {
								precompiled.add(ParseConstants.TKA_ASSIGN_MMUL);
								return;
								/** <code>
								final AccessReference reference = (AccessReference) last;
								final int start = assembly.size();
								reference.toReferenceRead( start, assembly );
								assembly.addInstruction( start, ParseConstants.TOKEN_MMUL_BA_SS_S.getInstruction( null,
										ModifierArguments.AASP_POP,
										ModifierArguments.AASP_POP,
										null ) );
								reference.toReferenceWrite( start, assembly );
								precompiled.set( size - 1, assembly.toTokenAssignment( start,
										1 + last.getOperandCount(),
										InstructionResult.OBJECT ) );
								return;
								</code> */
							}
							throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
						}
						throw new RuntimeException("Addressable value required!!");
					}
				}
				break;
			case '/' :
				if (length == 1) {
					/** / */
					precompiled.add(ParseConstants.TKO_MDIV_BA_SS);
					return;
				}
				if (length == 2 && token.charAt(1) == '=') {
					/** /= */
					final int size = precompiled.size();
					if (size > 0) {
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MDIV);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
					throw new RuntimeException("Addressable value required!!");
				}
				break;
			case '%' :
				if (length == 1) {
					/** % */
					precompiled.add(ParseConstants.TKO_MMOD_BA_SS);
					return;
				}
				if (length == 2 && token.charAt(1) == '=') {
					/** %= */
					final int size = precompiled.size();
					if (size > 0) {
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_MMOD);
							return;
						}
						throw new RuntimeException("Invalid = operator usage, previous token type=" + last.getClass().getName() + "!");
					}
					throw new RuntimeException("Addressable value required!!");
				}
				break;
			case '(' :
				/** ( */
				precompiled.add(ParseConstants.TKS_BRACE_OPEN);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case '[' :
				/** [ */
				precompiled.add(ParseConstants.TKS_INDEX_OPEN);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case '?' :
				if (length == 1) {
					/** ? */
					precompiled.add(ParseConstants.TKS_QUESTION_MARK);
					return;
				}
				if (length == 2) {
					if (token.charAt(1) == '?') {
						/** ?? */
						precompiled.add(ParseConstants.TKS_ENCO);
						return;
					}
					if (token.charAt(1) == '.') {
						/** ?. */
						precompiled.add(ParseConstants.TKS_EOCO);
						return;
					}
				}
				if (length == 3) {
					if (token.charAt(1) == '?' && token.charAt(2) == '=') {
						/** ??= */
						final int size = precompiled.size();
						if (size == 0) {
							throw new RuntimeException("Addressable value required!!");
						}
						final TokenInstruction last = precompiled.get(size - 1);
						if (last.isAccessReference()) {
							precompiled.add(ParseConstants.TKA_ASSIGN_ELNA_A_S_S);
							return;
						}
						throw new RuntimeException("Invalid " + token + " operator usage, previous token type=" + last.getClass().getName() + "!");
					}
				}
				precompiled.add(ParseConstants.TKS_QUESTION_MARK);
				ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				return;
			case ':' :
				/** : */
				precompiled.add(ParseConstants.TKS_COLON);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case ',' :
				/** , */
				precompiled.add(ParseConstants.TKS_COMMA);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case ';' :
				/** ; */
				precompiled.add(ParseConstants.TKS_SEMICOLON);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			case '{' :
				/** { */
				precompiled.add(ParseConstants.TKS_CREATE_OPEN);
				if (length > 1) {
					ExpressionParser.addOperator(assembly, precompiled, token.substring(1));
				}
				return;
			default :
		}
		if (length > 1) {
			ExpressionParser.addOperator(assembly, precompiled, token.substring(0, length - 1));
			ExpressionParser.addOperator(assembly, precompiled, token.substring(length - 1));
		} else {
			throw new RuntimeException(
					"Unknown operator ('" + token + "'" + (token.length() != 1
						? ""
						: ", code=" + Integer.toString(token.charAt(0))) + ") encountered!");
		}
	}
	
	private static void addRegExp(final ProgramAssembly assembly, final List<TokenInstruction> precompiled, final String regexp, final String flags) throws Throwable {
		
		final TokenInstruction constructor = TKV_FLOAD_A_Cs_S.getInstance(ExpressionParser.STRING_REGEXP_CONSTRUCTOR_NAME, ExpressionParser.DEFAULT_VARIABLES);
		final TokenInstruction pattern = ParseConstants.getConstantValue(Base.forString(regexp));
		final List<TokenInstruction> sub = flags.length() == 0
			? Collections.singletonList(pattern)
			: Arrays.asList(new TokenInstruction[]{
					pattern, ParseConstants.TKS_COMMA, ParseConstants.getConstantValue(Base.forString(flags)),
			});
		final TokenInstruction call = ExpressionParser.encodeCallAccessCXE(assembly, constructor, ExpressionParser.MA_CONSTRUCTOR, sub);
		precompiled.add(call);
	}
	
	/** @param assembly
	 * @param precompiled
	 * @return false - stop on error, true - continue
	 * @throws Throwable */
	private static final boolean closeBrace(final ProgramAssembly assembly, final List<TokenInstruction> precompiled) throws Throwable {
		
		final int preparedPosition = precompiled.lastIndexOf(ParseConstants.TKS_BRACE_OPEN);
		if (preparedPosition == -1) {
			precompiled.clear();
			precompiled.add(ParseConstants.TKV_ERROR_UNMATCHED_BRACE_CLOSE);
			return false;
		}
		int precompiledSize = precompiled.size();
		final int subSize = precompiledSize - (preparedPosition + 1);
		final List<TokenInstruction> sub = new ArrayList<>(subSize);
		if (subSize > 0) {
			final List<TokenInstruction> subList = precompiled.subList(preparedPosition + 1, precompiledSize);
			sub.addAll(subList);
		}
		while (precompiledSize > preparedPosition) {
			precompiled.remove(--precompiledSize);
		}
		// System.out.println( ">> >>> precompiled: " + precompiled );
		final TokenInstruction last = precompiledSize == 0
			? null
			: precompiled.get(precompiledSize - 1);
		if (last == null || !last.isParseValueRight()) {
			precompiled.add(assembly.compileExpression(sub, BalanceType.EXPRESSION));
			return true;
		}
		/** DEBUG<code>
			System.out.println( ">>> >>> CLOSEBRACE prevReference="
					+ last.isAccessReference()
					+ ",  prev="
					+ last );
			</code> */
		final ModifierArgument accessObjectModifier;
		final TokenInstruction accessPropertyReference;
		if (last.isAccessReference()) {
			accessPropertyReference = last.toReferenceProperty();
			assert accessPropertyReference != null : "no TKO_ACCESS_BA_Sx token!";
			final TokenInstruction accessObject = last.toReferenceObject();
			if (accessObject == null) {
				/** object is on stack for this token */
				precompiled.set(
						precompiledSize - 1,
						ExpressionParser.encodeCallAccessDXE(
								assembly, //
								accessPropertyReference,
								sub));
				return true;
			}
			{
				final ModifierArgument direct = accessObject.toDirectModifier();
				if (direct == ModifierArguments.AA0RB) {
					precompiled.set(precompiledSize - 1, last.toExecDetachableResult());
					accessObjectModifier = null;
				} else {
					assert direct != ModifierArguments.AB7FV : "Not allowed, used for anonymous functions";
					accessObjectModifier = direct;
					precompiled.remove(precompiledSize - 1);
				}
			}
		} else {
			accessObjectModifier = ModifierArguments.AB7FV;
			if (!last.isStackValue()) {
				precompiled.add(ParseConstants.TKV_ERROR_EXPRESSION_EXPECTED);
				return false;
			}
			accessPropertyReference = last;
			precompiled.remove(precompiledSize - 1);
		}
		precompiled.add(
				ExpressionParser.encodeCallAccessXXE(
						assembly, //
						accessObjectModifier,
						accessPropertyReference,
						sub));
		return true;
	}
	
	/** @param assembly
	 * @param precompiled
	 * @return false - stop on error, true - continue
	 * @throws Exception */
	private static final boolean closeCreate(final ProgramAssembly assembly, final List<TokenInstruction> precompiled) throws Exception {
		
		final int preparedPosition = precompiled.lastIndexOf(ParseConstants.TKS_CREATE_OPEN);
		if (preparedPosition == -1) {
			precompiled.clear();
			precompiled.add(ParseConstants.TKV_ERROR_UNMATCHED_CREATE_CLOSE);
			return false;
		}
		int precompiledSize = precompiled.size();
		final int subSize = precompiledSize - (preparedPosition + 1);
		final List<TokenInstruction> sub = new ArrayList<>(subSize);
		if (subSize > 0) {
			final List<TokenInstruction> subList = precompiled.subList(preparedPosition + 1, precompiledSize);
			sub.addAll(subList);
		}
		while (precompiledSize > preparedPosition) {
			precompiled.remove(--precompiledSize);
		}
		
		final int size = assembly.size();
		if (precompiledSize != 0 && precompiled.get(precompiledSize - 1).isParseValueRight()) {
			final TokenInstruction error = Report.MODE_ASSERT || Report.MODE_DEBUG
				? new TKV_ERROR_A_C_E("'{' is not an operator - it creates value, current tokens: " + precompiled)
				: ParseConstants.TKV_ERROR_COBJECT_IS_NOT_AN_OPERATOR;
			precompiled.clear();
			precompiled.add(error);
			return false;
		}
		
		List<BasePrimitiveString> propertyNames = null;
		List<TokenInstruction> propertyValues = null;
		
		for (int i = 0;;) {
			if (i == subSize) {
				if (propertyNames == null) {
					precompiled.add(TKV_COBJECT_EMPTY.INSTANCE);
					return true;
				}
				assert propertyValues != null;
				precompiled.add(
						new TKV_COBJECT_LITERAL(
								propertyNames.toArray(new BasePrimitiveString[propertyNames.size()]),
								propertyValues.toArray(new TokenInstruction[propertyValues.size()])));
				return true;
			}
			/** property name */
			final String name;
			{
				final TokenInstruction token = sub.get(i++);
				name = token.toCreatePropertyName();
				if (name == null) {
					assembly.truncate(size);
					precompiled.clear();
					precompiled.add(new TKV_ERROR_A_C_E("Property name should be identifier or literal!"));
					return false;
				}
			}
			/** ':' */
			if (i == subSize || sub.get(i++) != ParseConstants.TKS_CREATE_ELEMENT) {
				assembly.truncate(size);
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("':' expected!"));
				return false;
			}
			/** Expression */
			for (final int expressionStart = i;;) {
				final boolean end = i == subSize;
				if (end || sub.get(i++) == ParseConstants.TKS_COMMA) {
					// System.out.println( ">>> >>> CREATE " + sub + ", i=" +
					// i + ", subSize=" + subSize );
					if (i == expressionStart) {
						assembly.truncate(size);
						precompiled.clear();
						precompiled.add(ParseConstants.TKV_ERROR_EXPRESSION_EXPECTED);
						return false;
					}
					
					if (propertyNames == null) {
						propertyNames = new ArrayList<>();
						propertyValues = new ArrayList<>();
					}
					
					assert propertyValues != null;
					propertyNames.add(Base.forString(name));
					propertyValues.add(
							assembly.compileExpression(
									sub.subList(
											expressionStart,
											end
												? i
												: i - 1),
									BalanceType.EXPRESSION).toExecNativeResult());
					break;
				}
			}
		}
	}
	
	/** @param assembly
	 * @param precompiled
	 * @return false - stop on error, true - continue
	 * @throws Exception */
	private static final boolean closeIndex(final ProgramAssembly assembly, final List<TokenInstruction> precompiled) throws Exception {
		
		final int preparedPosition = precompiled.lastIndexOf(ParseConstants.TKS_INDEX_OPEN);
		if (preparedPosition == -1) {
			precompiled.clear();
			precompiled.add(ParseConstants.TKV_ERROR_UNMATCHED_INDEX_CLOSE);
			return false;
		}
		int precompiledSize = precompiled.size();
		final int subSize = precompiledSize - (preparedPosition + 1);
		final List<TokenInstruction> sub = new ArrayList<>(subSize);
		if (subSize > 0) {
			final List<TokenInstruction> subList = precompiled.subList(preparedPosition + 1, precompiledSize);
			sub.addAll(subList);
		}
		while (precompiledSize > preparedPosition) {
			precompiled.remove(--precompiledSize);
		}
		if (precompiledSize != 0 && precompiled.get(precompiledSize - 1).isParseValueRight()) {
			ExpressionParser.EXPR_PRIVATE_COUNT++;
			/** TODO: optimize please, kinda shit, why should I check it here? */
			precompiled.add(new TKO_ACCESS_BA_VS_S(assembly.compileExpression(sub, BalanceType.EXPRESSION)));
			return true;
		}
		precompiled.add(assembly.compileExpression(sub, BalanceType.ARRAY_LITERAL));
		return true;
	}
	
	/** @param assembly
	 * @param precompiled
	 * @return false - stop on error, true - continue
	 * @throws Exception */
	private static final boolean closeTernary(final ProgramAssembly assembly, final List<TokenInstruction> precompiled) throws Exception {
		
		int precompiledSize = precompiled.size();
		if (precompiledSize > 1) {
			final TokenInstruction check = precompiled.get(precompiledSize - 2);
			if (check == ParseConstants.TKS_CREATE_OPEN || check == ParseConstants.TKS_COMMA && precompiled.get(precompiledSize - 1).toCreatePropertyName() != null) {
				precompiled.add(ParseConstants.TKS_CREATE_ELEMENT);
				return true;
			}
		}
		final int preparedPosition = precompiled.lastIndexOf(ParseConstants.TKS_QUESTION_MARK);
		if (preparedPosition == -1) {
			precompiled.clear();
			precompiled.add(ParseConstants.TKV_ERROR_UNMATCHED_COLON);
			return false;
		}
		final int subSize = precompiledSize - (preparedPosition + 1);
		final List<TokenInstruction> sub = new ArrayList<>(subSize);
		if (subSize > 0) {
			final List<TokenInstruction> subList = precompiled.subList(preparedPosition + 1, precompiledSize);
			sub.addAll(subList);
		}
		while (precompiledSize > preparedPosition) {
			precompiled.remove(--precompiledSize);
		}
		// System.out.println( ">> >>> precompiled: " + precompiled + ", sub: "
		// + sub );
		if (precompiledSize != 0 && precompiled.get(precompiledSize - 1).isParseValueRight()) {
			precompiled.add(ParseConstants.TKS_QUESTION_MARK_MATCHED);
			precompiled.add(new TKO_ECHOOSE_CBA_SVS_S(assembly.compileExpression(sub, BalanceType.EXPRESSION)));
			/** OLD<code>
			precompiled.add( ParseConstants.TKS_PREDICATE );
			precompiled.add( assembly.compileExpression( sub, BalanceType.EXPRESSION ) );
			precompiled.add( ParseConstants.TKS_CHOOSE );
			</code> */
			// System.out.println( ">>> >>> precompiled: " + precompiled );
			return true;
		}
		{
			precompiled.clear();
			precompiled.add(ParseConstants.TKV_ERROR_TERNARY_NEEDS_CONDITION);
			return false;
		}
	}
	
	private static final TKV_COBJECT_NEW encodeCallAccessCXE(final ProgramAssembly assembly,
			final TokenInstruction constructedClassAccess,
			final TokenInstruction constructorPropertyName,
			final List<TokenInstruction> callArguments) throws Exception {
		
		assert constructorPropertyName != null;
		assert constructorPropertyName.assertStackValue();
		if (callArguments == null || callArguments.size() == 0) {
			return new TKV_COBJECT_NEW(constructedClassAccess, new TKO_ACALLV_BA_VS_S(constructorPropertyName));
		}
		Map<String, Integer> nameToIndex = null;
		{
			int tokenCount = callArguments.size();
			boolean argumentStart = true;
			for (int i = 0, param = 0; i < tokenCount; ++i) {
				final Object current = callArguments.get(i);
				if (current == ParseConstants.TKS_COMMA) {
					argumentStart = true;
					param++;
				} else //
				if (argumentStart) {
					if (current instanceof TKA_ASSIGN_FSTORE_BA_SC_S) {
						if (nameToIndex == null) {
							nameToIndex = Create.treeMap();
						}
						final String name = ((TKA_ASSIGN_FSTORE_BA_SC_S) current).getName();
						nameToIndex.put(name, Reflect.getInteger(param));
						callArguments.remove(i);
						tokenCount--;
					}
					argumentStart = false;
				}
			}
			if (argumentStart && tokenCount > 0) {
				callArguments.remove(tokenCount - 1);
			}
		}
		ExpressionParser.EXPR_PRIVATE_COUNT++;
		final TokenInstruction arguments = assembly.compileExpression(callArguments, BalanceType.ARGUMENT_LIST);
		final int paramCount = arguments.getResultCount();
		if (nameToIndex == null) {
			if (paramCount == 1) {
				return new TKV_COBJECT_NEW(constructedClassAccess, new TKO_ACALLO_CBA_AVS_S(constructorPropertyName, arguments));
			}
			{
				// paramCount > 1
				return new TKV_COBJECT_NEW(constructedClassAccess, new TKO_ACALLS_CBA_AVS_S(constructorPropertyName, arguments, paramCount));
			}
		}
		{
			/** nameToIndex != null && paramCount > 0 */
			final Instruction carguments = new IAVV_ARGS_CREATEN_X_X_R(paramCount, nameToIndex);
			return new TKV_COBJECT_NEW(constructedClassAccess, new TKO_ACALLM_CBA_AVS_S(constructorPropertyName, arguments, carguments));
		}
	}
	
	/** DIRECT (r7RR) */
	private static final TokenInstruction encodeCallAccessDXE(final ProgramAssembly assembly, final TokenInstruction accessProperty, final List<TokenInstruction> callArguments)
			throws Exception {
		
		assert accessProperty != null;
		assert accessProperty.assertStackValue();
		if (callArguments == null || callArguments.size() == 0) {
			return new TKO_ACALLV_BA_VS_S(accessProperty);
		}
		Map<String, Integer> nameToIndex = null;
		{
			int tokenCount = callArguments.size();
			boolean argumentStart = true;
			for (int i = 0, param = 0; i < tokenCount; ++i) {
				final Object current = callArguments.get(i);
				if (current == ParseConstants.TKS_COMMA) {
					argumentStart = true;
					param++;
				} else //
				if (argumentStart) {
					if (current instanceof TKA_ASSIGN_FSTORE_BA_SC_S) {
						if (nameToIndex == null) {
							nameToIndex = Create.treeMap();
						}
						final String name = ((TKA_ASSIGN_FSTORE_BA_SC_S) current).getName();
						nameToIndex.put(name, Reflect.getInteger(param));
						callArguments.remove(i);
						tokenCount--;
					}
					argumentStart = false;
				}
			}
			if (argumentStart && tokenCount > 0) {
				callArguments.remove(tokenCount - 1);
			}
		}
		ExpressionParser.EXPR_PRIVATE_COUNT++;
		final TokenInstruction arguments = assembly.compileExpression(callArguments, BalanceType.ARGUMENT_LIST);
		final int paramCount = arguments.getResultCount();
		if (nameToIndex == null) {
			if (paramCount == 1) {
				return new TKO_ACALLO_CBA_AVS_S(accessProperty, arguments);
			}
			{
				// paramCount > 1
				return new TKO_ACALLS_CBA_AVS_S(accessProperty, arguments, paramCount);
			}
		}
		{
			/** nameToIndex != null && paramCount > 0 */
			final Instruction carguments = new IAVV_ARGS_CREATEN_X_X_R(paramCount, nameToIndex);
			return new TKO_ACALLM_CBA_AVS_S(accessProperty, arguments, carguments);
		}
	}
	
	private static final TokenInstruction encodeCallAccessXXE(final ProgramAssembly assembly,
			final ModifierArgument accessObjectModifier,
			final TokenInstruction accessProperty,
			final List<TokenInstruction> callArguments) throws Exception {
		
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB && accessObjectModifier != null;
		assert accessProperty != null;
		assert accessProperty.assertStackValue();
		if (callArguments == null || callArguments.size() == 0) {
			return /* modifierA == null ? new TKO_ACALLV_BA_VS_S( argumentB, detachable ) : */accessObjectModifier == ModifierArguments.AB7FV
				? new TKV_FCALLV_A_V_S(accessProperty)
				: accessObjectModifier == ModifierArguments.AB4CT
					? new TKV_ZTCALLV_A_V_S(accessProperty)
					: new TKV_ACALLV_BA_VM_S(accessObjectModifier, accessProperty);
		}
		Map<String, Integer> nameToIndex = null;
		{
			int tokenCount = callArguments.size();
			boolean argumentStart = true;
			for (int i = 0, param = 0; i < tokenCount; ++i) {
				final Object current = callArguments.get(i);
				if (current == ParseConstants.TKS_COMMA) {
					argumentStart = true;
					param++;
				} else //
				if (argumentStart) {
					if (current instanceof TKA_ASSIGN_FSTORE_BA_SC_S) {
						if (nameToIndex == null) {
							nameToIndex = Create.treeMap();
						}
						final String name = ((TKA_ASSIGN_FSTORE_BA_SC_S) current).getName();
						nameToIndex.put(name, Reflect.getInteger(param));
						callArguments.remove(i);
						tokenCount--;
					}
					argumentStart = false;
				}
			}
			if (argumentStart && tokenCount > 0) {
				callArguments.remove(tokenCount - 1);
			}
		}
		ExpressionParser.EXPR_PRIVATE_COUNT++;
		final TokenInstruction arguments = assembly.compileExpression(callArguments, BalanceType.ARGUMENT_LIST);
		final int paramCount = arguments.getResultCount();
		if (nameToIndex == null) {
			if (paramCount == 1) {
				return accessObjectModifier == ModifierArguments.AB7FV
					? new TKV_FCALLO_BA_AV_S(accessProperty, arguments)
					: accessObjectModifier == ModifierArguments.AB4CT
						? new TKV_ZTCALLO_BA_AV_S(accessProperty, arguments)
						: new TKV_ACALLO_CBA_AVM_S(accessObjectModifier, accessProperty, arguments);
			}
			{
				/** paramCount > 1 */
				return accessObjectModifier == ModifierArguments.AB7FV
					? new TKV_FCALLS_BA_AV_S(
							accessProperty, //
							arguments,
							paramCount)
					: accessObjectModifier == ModifierArguments.AB4CT
						? new TKV_ZTCALLS_BA_AV_S(
								accessProperty, //
								arguments,
								paramCount)
						: new TKV_ACALLS_CBA_AVM_S(
								accessObjectModifier, //
								accessProperty,
								arguments,
								paramCount);
			}
		}
		/** nameToIndex != null && paramCount > 0 */
		final Instruction carguments = new IAVV_ARGS_CREATEN_X_X_R(paramCount, nameToIndex);
		return accessObjectModifier == ModifierArguments.AB7FV
			? new TKV_FCALLM_BA_AV_S(accessProperty, arguments, carguments)
			: accessObjectModifier == ModifierArguments.AB4CT
				? new TKV_ZTCALLM_BA_AV_S(accessProperty, arguments, carguments)
				: new TKV_ACALLM_CBA_AVM_S(accessObjectModifier, accessProperty, arguments, carguments);
	}
	
	private static final boolean isIdentifierPart(final char c) {
		
		return (ExpressionParser.CHARS[c] & 0x04) != 0;
	}
	
	private static final boolean isIdentifierStart(final char c) {
		
		return (ExpressionParser.CHARS[c] & 0x02) != 0;
	}
	
	private static final boolean isWhitespace(final char c) {
		
		return (ExpressionParser.CHARS[c] & 0x01) != 0;
	}
	
	/** @param assembly
	 * @param expression
	 * @param balanceType
	 * @return */
	public static TokenInstruction parseExpression(final ProgramAssembly assembly, final String expression, final BalanceType balanceType) {
		
		ExpressionParser.EXPR_PUBLIC_COUNT++;
		final String expr = expression.trim();
		try {
			/** check for simple expressions */
			if (expr.length() < 12) {
				final TokenValue ready = ExpressionParser.DEFAULT_VARIABLES.get(expr);
				if (ready != null) {
					if (balanceType == BalanceType.DECLARATION) {
						throw new IllegalArgumentException("Token is not suitable for daclaration: " + ready);
					}
					if (ready.toConstantValue() != ExpressionParser.RESERVED_OBJECT) {
						ExpressionParser.EXPR_BUILTIN_HITS++;
						if (balanceType == BalanceType.STATEMENT) {
							return null;
						}
						if (balanceType == BalanceType.ARGUMENT_LIST) {
							return ready;
						}
						if (balanceType == BalanceType.EXPRESSION) {
							return ready;
						}
						if (balanceType == BalanceType.ARRAY_LITERAL) {
							return ready.getResultType() == InstructionResult.NEVER
								? ready
								: new TKV_CARRAY1(ready);
						}
						throw new RuntimeException("Unknown balanceType: " + balanceType);
					}
					Report.warning("EVALUATE", "Reserved word (" + ready.getNotation() + ") in expression: " + expr);
					return new TKV_FLOAD_A_Cs_S(Base.forString(expr));
				}
			}
			
			/**
			 *
			 */
			final List<TokenInstruction> precompiled = ExpressionParser.prepare(assembly, expr);
			List<TokenInstruction> subStatements = null;
			{
				final int tokenCount = precompiled.size();
				boolean foundStatements = false;
				for (int index = 0, prevIndex = 0;; ++index) {
					final boolean end = index == tokenCount;
					if (!foundStatements && end) {
						/** Fall through. We've just checked that there is no semicolons in the
						 * expression. */
						return assembly.compileExpression(precompiled, balanceType);
					}
					if (end || precompiled.get(index) == ParseConstants.TKS_SEMICOLON) {
						final List<TokenInstruction> subStatement = prevIndex == index
							? null
							: precompiled.subList(prevIndex, index);
						final TokenInstruction subToken = subStatement == null
							? null
							: assembly.compileExpression(subStatement, BalanceType.STATEMENT);
						if (end) {
							if (subStatements == null) {
								/** we have nothing else added into assembly */
								return subToken;
							}
							if (subToken != null) {
								subStatements.add(subToken);
							}
							
							final int size = subStatements.size();
							switch (size) {
								case 1 :
									return subStatements.get(0);
								case 2 :
									return new TKV_EFLOW2(subStatements.get(0), subStatements.get(1));
								case 3 :
									return new TKV_EFLOW3(subStatements.get(0), subStatements.get(1), subStatements.get(2));
								default :
									return new TKV_EFLOWX(subStatements.toArray(new TokenInstruction[size]));
							}
						}
						/** For statements we need only non-constant ones. */
						if (subToken != null && subToken.toConstantModifier() == null) {
							if (subStatements == null) {
								subStatements = new ArrayList<>();
							}
							subStatements.add(subToken);
						}
						foundStatements = true;
						prevIndex = index + 1;
					}
				}
			}
		} catch (final Throwable t) {
			throw new RuntimeException("Error while compiling: " + expr, t);
		}
	}
	
	private static final List<TokenInstruction> prepare(final ProgramAssembly assembly, final String expressionString) throws Throwable {
		
		final List<TokenInstruction> precompiled = new ArrayList<>(16);
		final StringBuilder current = new StringBuilder(32);
		/** for intermediate collected identifier such as functionName, accessName,
		 * constructorName... */
		String identifier = null;
		final char[] expression = expressionString.toCharArray();
		final int length = expression.length;
		int levelBrace = 0;
		int functionLevel = 0;
		int blockStart = 0;
		int state = ExpressionParser.ST_WHITESPACE;
		main : for (int i = 0; i < length; ++i) {
			/** main */
			switch (state) {
				case ST_WHITESPACE : {
					whitespace : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '\'' : {
								state = ExpressionParser.ST_1STRING;
								continue main;
							}
							case '"' : {
								state = ExpressionParser.ST_2STRING;
								continue main;
							}
							case '.' : {
								state = ExpressionParser.ST_IDENTIFIER_ACCS;
								continue main;
							}
							case '(' : {
								state = ExpressionParser.ST_CODE;
								current.append(c);
								levelBrace++;
								continue main;
							}
							case ')' : {
								current.setLength(0);
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								continue whitespace;
							}
							case '}' : {
								current.setLength(0);
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								continue whitespace;
							}
							case ']' : {
								current.setLength(0);
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								continue whitespace;
							}
							case ':' : {
								current.setLength(0);
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								continue whitespace;
							}
							case '@' : {
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case '/' : {
								final int precompiledSize = precompiled.size();
								if (precompiledSize == 0 || !precompiled.get(precompiledSize - 1).isParseValueRight()) {
									current.setLength(0);
									state = ExpressionParser.ST_REGEXP_START;
									continue main;
								}
								current.append(c);
								state = ExpressionParser.ST_CODE;
								continue main;
							}
							case '0' :
							case '1' :
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' :
							case '8' :
							case '9' : {
								state = ExpressionParser.ST_NUMBER;
								current.append(c);
								continue main;
							}
							// BOM
							// http://www.fileformat.info/info/unicode/char/feff/index.htm
							case 0xFEFF :
								continue whitespace;
							default :
						}
						if (ExpressionParser.isIdentifierStart(c)) {
							state = ExpressionParser.ST_IDENTIFIER_ROOT;
							current.append(c);
							continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							continue whitespace;
						}
						current.append(c);
						state = ExpressionParser.ST_CODE;
						continue main;
					}
					continue main;
				}
				case ST_IDENTIFIER_CONST : {
					identifierConst : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '.') {
							ExpressionParser.addIdentifierConst(assembly, precompiled, current.toString());
							current.setLength(0);
							state = ExpressionParser.ST_IDENTIFIER_ACCS;
							continue main;
						}
						if (c == '(') {
							functionLevel = ++levelBrace;
							identifier = current.toString();
							current.setLength(0);
							state = ExpressionParser.ST_FUNCTION_ROOT;
							break;
						}
						if (ExpressionParser.isWhitespace(c)) {
							ExpressionParser.addIdentifierConst(assembly, precompiled, current.toString());
							current.setLength(0);
							state = ExpressionParser.ST_WHITESPACE;
							break;
						}
						if (ExpressionParser.isIdentifierPart(c)) {
							current.append(c);
							continue identifierConst;
						}
						ExpressionParser.addIdentifierConst(assembly, precompiled, current.toString());
						current.setLength(0);
						switch (c) {
							case ')' :
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case '}' :
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ']' :
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ':' :
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							default :
								current.append(c);
								state = ExpressionParser.ST_CODE;
								continue main;
						}
					}
					continue main;
				}
				case ST_IDENTIFIER_NEW_ACCS :
				case ST_IDENTIFIER_NEW : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '(') {
							functionLevel = ++levelBrace;
							identifier = current.toString();
							current.setLength(0);
							if (state == ExpressionParser.ST_IDENTIFIER_NEW_ACCS) {
								/** push source for next parser stage */
								final List<TokenInstruction> sub = ExpressionParser.prepare(assembly, identifier);
								final TokenInstruction source = assembly.compileExpression(sub, BalanceType.EXPRESSION);
								precompiled.add(source);
								identifier = null;
							}
							state = ExpressionParser.ST_FUNCTION_NEW;
							continue main;
						}
						if (c == '.') {
							current.append(c);
							if (state != ExpressionParser.ST_IDENTIFIER_NEW_ACCS) {
								state = ExpressionParser.ST_IDENTIFIER_NEW_ACCS;
							}
							continue;
						}
						if (ExpressionParser.isWhitespace(c)) {
							if (current.length() == 0) {
								continue;
							}
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("'(' expected (at " + i + "): " + expressionString));
							return precompiled;
						}
						if (ExpressionParser.isIdentifierPart(c)) {
							current.append(c);
							continue;
						}
						if (current.length() == 0 && state == ExpressionParser.ST_IDENTIFIER_NEW) {
							ExpressionParser.addIdentifierRoot(precompiled, "new", expressionString);
						} else {
							ExpressionParser.addIdentifierRoot(precompiled, current.toString(), expressionString);
							current.setLength(0);
						}
						switch (c) {
							case ')' :
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case '}' :
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ']' :
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ':' :
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							default :
						}
						current.append(c);
						state = ExpressionParser.ST_CODE;
						continue main;
					}
					continue main;
				}
				case ST_FUNCTION_DECL_NAME :
				case ST_IDENTIFIER_ROOT : {
					local : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '.') {
							if (state == ExpressionParser.ST_FUNCTION_DECL_NAME) {
								precompiled.clear();
								precompiled.add(new TKV_ERROR_A_C_E("'(' expected (at " + i + "): " + expressionString));
								return precompiled;
							}
							ExpressionParser.addIdentifierRoot(precompiled, current.toString(), expressionString);
							current.setLength(0);
							state = ExpressionParser.ST_IDENTIFIER_ACCS;
							continue main;
						}
						if (c == '(') {
							identifier = current.toString();
							current.setLength(0);
							if (state == ExpressionParser.ST_FUNCTION_DECL_NAME) {
								/** would be used in the end of the process */
								// identifier = null;
								/** this would be a buffer for arguments */
								precompiled.add(ParseConstants.TKV_UNDEFINED);
								state = ExpressionParser.ST_FUNCTION_DECL_ARGS;
								continue main;
							}
							switch (identifier.length()) {
								case 2 :
									if ("in".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_BIN_A_S_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									break;
								case 3 :
									if ("new".equals(identifier)) {
										identifier = null;
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_IDENTIFIER_NEW;
										continue main;
									}
									break;
								case 4 :
									if ("void".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_ZVOID_A_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									break;
								case 5 :
									if ("await".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_AWAIT_A_S_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									break;
								case 6 :
									if ("typeof".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_TYPEOF_A_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									if ("delete".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_DELETE_A_L_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									break;
								case 8 :
									if ("function".equals(identifier)) {
										identifier = null;
										/** this would be a buffer for arguments */
										precompiled.add(ParseConstants.TKV_UNDEFINED);
										state = ExpressionParser.ST_FUNCTION_DECL_ARGS;
										continue main;
									}
									break;
								case 10 :
									if ("instanceof".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_BINSTANCEOF_A_S_S);
										current.append('(');
										levelBrace++;
										state = ExpressionParser.ST_CODE;
										continue main;
									}
									break;
								default :
							}
							functionLevel = ++levelBrace;
							state = ExpressionParser.ST_FUNCTION_ROOT;
							continue main;
						}
						if (ExpressionParser.isIdentifierPart(c)) {
							current.append(c);
							continue local;
						}
						{
							if (ExpressionParser.isWhitespace(c)) {
								char ch = 0;
								for (; i + 1 < length; ++i) {
									ch = expression[i + 1];
									if (ch == '.' || ch == '(') {
										continue local;
									}
									if (ExpressionParser.isWhitespace(ch)) {
										continue;
									}
									break;
								}
							} else {
								/** step back */
								i--;
							}
							identifier = current.toString();
							current.setLength(0);
							switch (identifier.length()) {
								case 2 :
									if ("in".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_BIN_A_S_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									break;
								case 3 :
									if ("new".equals(identifier)) {
										identifier = null;
										state = ExpressionParser.ST_IDENTIFIER_NEW;
										continue main;
									}
									break;
								case 4 :
									if ("void".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_ZVOID_A_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									break;
								case 5 :
									if ("await".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_AWAIT_A_S_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									break;
								case 6 :
									if ("typeof".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_TYPEOF_A_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									if ("delete".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_DELETE_A_L_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									break;
								case 8 :
									if ("function".equals(identifier)) {
										identifier = null;
										state = ExpressionParser.ST_FUNCTION_DECL_NAME;
										continue main;
									}
									break;
								case 10 :
									if ("instanceof".equals(identifier)) {
										identifier = null;
										precompiled.add(ParseConstants.TKO_BINSTANCEOF_A_S_S);
										state = ExpressionParser.ST_WHITESPACE;
										continue main;
									}
									break;
								default :
							}
							state = ExpressionParser.ST_WHITESPACE;
							ExpressionParser.addIdentifierRoot(precompiled, identifier, expressionString);
							identifier = null;
							continue main;
						}
					}
					continue main;
				}
				case ST_IDENTIFIER_ACCS : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '.') {
							ExpressionParser.addIdentifierAccess(precompiled, current.toString());
							current.setLength(0);
							continue;
						}
						if (c == '(') {
							functionLevel = ++levelBrace;
							identifier = current.toString();
							current.setLength(0);
							state = ExpressionParser.ST_FUNCTION_ACCS;
							continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							ExpressionParser.addIdentifierAccess(precompiled, current.toString());
							current.setLength(0);
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						if (ExpressionParser.isIdentifierPart(c)) {
							current.append(c);
							continue;
						}
						ExpressionParser.addIdentifierAccess(precompiled, current.toString());
						current.setLength(0);
						switch (c) {
							case ')' :
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case '}' :
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ']' :
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							case ':' :
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							default :
								current.append(c);
								state = ExpressionParser.ST_CODE;
								continue main;
						}
					}
					continue main;
				}
				case ST_NUMBER : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '0' :
							case '1' :
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' :
							case '8' :
							case '9' : {
								current.append(c);
								continue;
							}
							case '.' : {
								current.append(c);
								state = ExpressionParser.ST_NUMBER_FLOAT;
								continue main;
							}
							case 'x' : {
								if (current.length() != 1 || current.charAt(0) != '0') {
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid hexadecimal constant (at " + i + "): " + expressionString));
									return precompiled;
								}
								current.setLength(0);
								state = ExpressionParser.ST_NUMBER_HEX;
								continue main;
							}
							case 'b' : {
								if (current.length() != 1 || current.charAt(0) != '0') {
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid binary constant (at " + i + "): " + expressionString));
									return precompiled;
								}
								current.setLength(0);
								state = ExpressionParser.ST_NUMBER_BIN;
								continue main;
							}
							case 'o' : {
								if (current.length() != 1 || current.charAt(0) != '0') {
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid octal constant (at " + i + "): " + expressionString));
									return precompiled;
								}
								current.setLength(0);
								state = ExpressionParser.ST_NUMBER_OCT;
								continue main;
							}
							case 'l' :
							case 'L' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'L');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case 'd' :
							case 'D' : {
								ExpressionParser.addNumberFloat(precompiled, current.toString(), 'D');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case 'f' :
							case 'F' : {
								ExpressionParser.addNumberFloat(precompiled, current.toString(), 'F');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '@' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'i');
								current.setLength(0);
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case ')' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '}' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ']' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ':' : {
								ExpressionParser.addNumber(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							default :
						}
						ExpressionParser.addNumber(precompiled, current.toString(), 'i');
						current.setLength(0);
						if (ExpressionParser.isIdentifierStart(c)) {
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("No space between numeric and identifier (at " + i + "): " + expressionString));
							return precompiled;
							// state = ExpressionParser.ST_IDENTIFIER_ROOT;
							// current.append(c);
							// continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						{
							state = ExpressionParser.ST_CODE;
							current.append(c);
							continue main;
						}
					}
					continue main;
				}
				case ST_NUMBER_OCT : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '0' :
							case '1' :
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' : {
								current.append(c);
								continue;
							}
							case '8' :
							case '9' : {
								precompiled.clear();
								precompiled.add(new TKV_ERROR_A_C_E("Unexpected number '" + c + "' (at " + i + "): " + expressionString));
								return precompiled;
							}
							case 'l' :
							case 'L' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'L');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '@' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case ')' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '}' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ']' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ':' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							default :
						}
						ExpressionParser.addNumberOct(precompiled, current.toString(), 'i');
						current.setLength(0);
						if (ExpressionParser.isIdentifierStart(c)) {
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("No space between octal and identifier (at " + i + "): " + expressionString));
							return precompiled;
							// state = ExpressionParser.ST_IDENTIFIER_ROOT;
							// current.append(c);
							// continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						{
							state = ExpressionParser.ST_CODE;
							current.append(c);
							continue main;
						}
					}
					continue main;
				}
				case ST_NUMBER_HEX : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
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
							case 'a' :
							case 'b' :
							case 'c' :
							case 'd' :
							case 'e' :
							case 'f' :
							case 'A' :
							case 'B' :
							case 'C' :
							case 'D' :
							case 'E' :
							case 'F' : {
								current.append(c);
								continue;
							}
							case 'l' :
							case 'L' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'L');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '@' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case ')' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '}' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ']' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ':' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							default :
						}
						ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
						current.setLength(0);
						if (ExpressionParser.isIdentifierStart(c)) {
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("No space between hexadecimal and identifier (at " + i + "): " + expressionString));
							return precompiled;
							// state = ExpressionParser.ST_IDENTIFIER_ROOT;
							// current.append(c);
							// continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						{
							state = ExpressionParser.ST_CODE;
							current.append(c);
							continue main;
						}
					}
					continue main;
				}
				case ST_NUMBER_BIN : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '0' :
							case '1' : {
								current.append(c);
								continue;
							}
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' :
							case '8' :
							case '9' : {
								precompiled.clear();
								precompiled.add(new TKV_ERROR_A_C_E("Unexpected number '" + c + "' (at " + i + "): " + expressionString));
								return precompiled;
							}
							case 'l' :
							case 'L' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'L');
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '@' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case ')' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case '}' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ']' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							case ':' : {
								ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
								current.setLength(0);
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
							default :
						}
						ExpressionParser.addNumberBin(precompiled, current.toString(), 'i');
						current.setLength(0);
						if (ExpressionParser.isIdentifierStart(c)) {
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("No space between binary and identifier (at " + i + "): " + expressionString));
							return precompiled;
							// state = ExpressionParser.ST_IDENTIFIER_ROOT;
							// current.append(c);
							// continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						{
							state = ExpressionParser.ST_CODE;
							current.append(c);
							continue main;
						}
					}
					continue main;
				}
				case ST_NUMBER_WITH_EXP : {
					switch (expression[i]) {
						case 'e' :
						case 'E' :
							precompiled.clear();
							precompiled.add(new TKV_ERROR_A_C_E("Invalid exponential (at " + i + "): " + expressionString));
							return precompiled;
						default :
					}
				}
				//$FALL-THROUGH$
				case ST_NUMBER_FLOAT : {
					final char c = expression[i];
					switch (c) {
						case '0' :
						case '1' :
						case '2' :
						case '3' :
						case '4' :
						case '5' :
						case '6' :
						case '7' :
						case '8' :
						case '9' : {
							current.append(c);
							continue main;
						}
						case 'e' :
						case 'E' : {
							current.append(c);
							state = ExpressionParser.ST_NUMBER_WITH_EXP;
							continue main;
						}
						case 'f' :
						case 'F' : {
							ExpressionParser.addNumberFloat(precompiled, current.toString(), 'F');
							current.setLength(0);
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						case 'd' :
						case 'D' : {
							ExpressionParser.addNumberFloat(precompiled, current.toString(), 'D');
							current.setLength(0);
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						default :
					}
					ExpressionParser.addNumberFloat(precompiled, current.toString(), 'D');
					current.setLength(0);
					if (c == '@') {
						state = ExpressionParser.ST_IDENTIFIER_CONST;
					} else //
					if (ExpressionParser.isIdentifierStart(c) || c == '@') {
						state = ExpressionParser.ST_IDENTIFIER_ROOT;
						current.append(c);
					} else //
					if (ExpressionParser.isWhitespace(c)) {
						state = ExpressionParser.ST_WHITESPACE;
					} else //
					if (c == ')') {
						if (!ExpressionParser.closeBrace(assembly, precompiled)) {
							return precompiled;
						}
						levelBrace--;
						state = ExpressionParser.ST_WHITESPACE;
					} else //
					if (c == '}') {
						if (!ExpressionParser.closeCreate(assembly, precompiled)) {
							return precompiled;
						}
						state = ExpressionParser.ST_WHITESPACE;
					} else //
					if (c == ']') {
						if (!ExpressionParser.closeIndex(assembly, precompiled)) {
							return precompiled;
						}
						state = ExpressionParser.ST_WHITESPACE;
					} else //
					if (c == ':') {
						if (!ExpressionParser.closeTernary(assembly, precompiled)) {
							return precompiled;
						}
						state = ExpressionParser.ST_WHITESPACE;
					} else {
						state = ExpressionParser.ST_CODE;
						current.append(c);
					}
					continue main;
				}
				case ST_CODE : {
					code : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '.' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								state = ExpressionParser.ST_IDENTIFIER_ACCS;
								continue main;
							}
							case '(' : {
								current.append('(');
								levelBrace++;
								continue code;
							}
							case ')' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								if (!ExpressionParser.closeBrace(assembly, precompiled)) {
									return precompiled;
								}
								levelBrace--;
								continue code;
							}
							case '}' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								if (!ExpressionParser.closeCreate(assembly, precompiled)) {
									return precompiled;
								}
								continue code;
							}
							case ']' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								if (!ExpressionParser.closeIndex(assembly, precompiled)) {
									return precompiled;
								}
								continue code;
							}
							case ':' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								if (!ExpressionParser.closeTernary(assembly, precompiled)) {
									return precompiled;
								}
								continue code;
							}
							case '\'' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								state = ExpressionParser.ST_1STRING;
								continue main;
							}
							case '"' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								state = ExpressionParser.ST_2STRING;
								continue main;
							}
							case '@' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								current.setLength(0);
								state = ExpressionParser.ST_IDENTIFIER_CONST;
								continue main;
							}
							case '/' : {
								final int precompiledSize = precompiled.size();
								if (precompiledSize == 0 || !precompiled.get(precompiledSize - 1).isParseValueRight()) {
									if (current.length() > 0) {
										ExpressionParser.addOperator(assembly, precompiled, current.toString());
										current.setLength(0);
									}
									current.setLength(0);
									state = ExpressionParser.ST_REGEXP_START;
									continue main;
								}
								current.append(c);
								continue code;
							}
							case '0' :
							case '1' :
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' :
							case '8' :
							case '9' : {
								if (current.length() > 0) {
									ExpressionParser.addOperator(assembly, precompiled, current.toString());
									current.setLength(0);
								}
								current.append(c);
								state = ExpressionParser.ST_NUMBER;
								continue main;
							}
							default :
						}
						if (ExpressionParser.isIdentifierStart(c)) {
							if (current.length() > 0) {
								ExpressionParser.addOperator(assembly, precompiled, current.toString());
								current.setLength(0);
							}
							current.append(c);
							state = ExpressionParser.ST_IDENTIFIER_ROOT;
							continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							if (current.length() > 0) {
								ExpressionParser.addOperator(assembly, precompiled, current.toString());
								current.setLength(0);
							}
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						{
							current.append(c);
						}
					}
					continue main;
				}
				case ST_FUNCTION_NEW : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '\'') {
							current.append('\'');
							state = ExpressionParser.ST_1STRING_FUNCTION_NEW;
							break;
						}
						if (c == '"') {
							current.append('"');
							state = ExpressionParser.ST_2STRING_FUNCTION_NEW;
							break;
						}
						if (c == '(') {
							current.append('(');
							levelBrace++;
							continue;
						}
						if (c == ')') {
							if (levelBrace-- == functionLevel) {
								final TokenInstruction constructor = identifier != null
									? TKV_FLOAD_A_Cs_S.getInstance(identifier.trim(), ExpressionParser.DEFAULT_VARIABLES)
									: precompiled.remove(precompiled.size() - 1);
								final String parameters = current.toString().trim();
								final List<TokenInstruction> sub = parameters.trim().length() == 0
									? null
									: ExpressionParser.prepare(assembly, parameters);
								{
									final TokenInstruction call = ExpressionParser.encodeCallAccessCXE(assembly, constructor, ExpressionParser.MA_CONSTRUCTOR, sub);
									precompiled.add(call);
								}
								identifier = null;
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								break;
							}
							current.append(')');
							continue;
						}
						current.append(c);
					}
					continue main;
				}
				case ST_FUNCTION_ROOT : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '\'') {
							current.append('\'');
							state = ExpressionParser.ST_1STRING_FUNCTION_ROOT;
							break;
						}
						if (c == '"') {
							current.append('"');
							state = ExpressionParser.ST_2STRING_FUNCTION_ROOT;
							break;
						}
						if (c == '(') {
							current.append('(');
							levelBrace++;
							continue;
						}
						if (c == ')') {
							if (levelBrace-- == functionLevel) {
								assert identifier != null : "function name expected!";
								final String parameters = current.toString().trim();
								final TokenInstruction argumentB = TKV_FLOAD_A_Cs_S.getInstance(identifier.trim(), ExpressionParser.DEFAULT_VARIABLES);
								final List<TokenInstruction> sub = parameters.trim().length() == 0
									? null
									: ExpressionParser.prepare(assembly, parameters);
								precompiled.add( //
										ExpressionParser.encodeCallAccessXXE(
												assembly, //
												ModifierArguments.AB7FV,
												argumentB,
												sub));
								identifier = null;
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								break;
							}
							current.append(')');
							continue;
						}
						current.append(c);
					}
					continue main;
				}
				case ST_FUNCTION_ACCS : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '\'') {
							current.append('\'');
							state = ExpressionParser.ST_1STRING_FUNCTION_ACCS;
							break;
						}
						if (c == '"') {
							current.append('"');
							state = ExpressionParser.ST_2STRING_FUNCTION_ACCS;
							break;
						}
						if (c == '(') {
							current.append('(');
							levelBrace++;
							continue;
						}
						if (c == ')') {
							if (levelBrace-- == functionLevel) {
								final int precompiledSize = precompiled.size();
								final TokenInstruction accessObject = precompiled.get(precompiledSize - 1);
								final ModifierArgument accessObjectModifier = accessObject.toDirectModifier();
								final boolean directAccessObject = accessObjectModifier == ModifierArguments.AA0RB;
								if (!directAccessObject) {
									precompiled.remove(precompiledSize - 1);
								} else {
									precompiled.set(precompiledSize - 1, accessObject.toExecDetachableResult());
								}
								assert identifier != null : "function name expected!";
								final String parameters = current.toString().trim();
								final TokenInstruction accessProperty = ParseConstants.getConstantValue(Base.forString(identifier.trim()));
								final List<TokenInstruction> sub = parameters.trim().length() == 0
									? null
									: ExpressionParser.prepare(assembly, parameters);
								precompiled.add( //
										directAccessObject
											? ExpressionParser.encodeCallAccessDXE(
													assembly, //
													accessProperty,
													sub)
											: ExpressionParser.encodeCallAccessXXE(
													assembly, //
													accessObjectModifier,
													accessProperty,
													sub));
								identifier = null;
								current.setLength(0);
								state = ExpressionParser.ST_WHITESPACE;
								break;
							}
							current.append(')');
							continue;
						}
						current.append(c);
					}
					continue main;
				}
				case ST_1STRING_FUNCTION_ROOT :
				case ST_1STRING_FUNCTION_ACCS :
				case ST_1STRING_FUNCTION_NEW : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '\'' : {
								current.append('\'');
								state = state == ExpressionParser.ST_1STRING_FUNCTION_NEW
									? ExpressionParser.ST_FUNCTION_NEW
									: state == ExpressionParser.ST_1STRING_FUNCTION_ACCS
										? ExpressionParser.ST_FUNCTION_ACCS
										: ExpressionParser.ST_FUNCTION_ROOT;
								continue main;
							}
							case '\\' : {
								current.append('\\');
								final char next = expression[++i];
								current.append(
										next == '\r'
											? expression[++i]
											: next);
								continue;
							}
							default :
								current.append(c);
								continue;
						}
					}
					continue main;
				}
				case ST_1STRING_FUNCTION_DECL : {
					bodyString1 : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '\'' : {
								state = ExpressionParser.ST_FUNCTION_DECL_BODY;
								continue main;
							}
							case '\\' : {
								++i;
								if (expression[i] == '\r') {
									++i;
								}
								continue bodyString1;
							}
							default :
								continue bodyString1;
						}
					}
					continue main;
				}
				case ST_2STRING_FUNCTION_ROOT :
				case ST_2STRING_FUNCTION_ACCS :
				case ST_2STRING_FUNCTION_NEW : {
					for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '"' : {
								current.append('"');
								state = state == ExpressionParser.ST_2STRING_FUNCTION_NEW
									? ExpressionParser.ST_FUNCTION_NEW
									: state == ExpressionParser.ST_2STRING_FUNCTION_ACCS
										? ExpressionParser.ST_FUNCTION_ACCS
										: ExpressionParser.ST_FUNCTION_ROOT;
								continue main;
							}
							case '\\' : {
								current.append('\\');
								final char next = expression[++i];
								current.append(
										next == '\r'
											? expression[++i]
											: next);
								continue;
							}
							default :
								current.append(c);
						}
					}
					continue main;
				}
				case ST_2STRING_FUNCTION_DECL : {
					bodyString2 : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '\"' : {
								state = ExpressionParser.ST_FUNCTION_DECL_BODY;
								continue main;
							}
							case '\\' : {
								++i;
								if (expression[i] == '\r') {
									++i;
								}
								continue bodyString2;
							}
							default :
								continue bodyString2;
						}
					}
					continue main;
				}
				case ST_1STRING : {
					for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '\'') {
							if (current.length() == 0) {
								precompiled.add(ParseConstants.TKV_STRING_EMPTY);
							} else {
								precompiled.add(new TKV_LCONSTS(Base.forString(current.toString())));
								current.setLength(0);
							}
							state = ExpressionParser.ST_WHITESPACE;
							break;
						}
						if (c == '\\') {
							++i;
							if (length <= i) {
								break;
							}
							final char escaped = expression[i];
							switch (escaped) {
								case 'n' :
									current.append('\n');
									break;
								case 'r' :
									current.append('\r');
									break;
								case 't' :
									current.append('\t');
									break;
								case 'b' :
									current.append('\b');
									break;
								case 'f' :
									current.append('\f');
									break;
								case 'x' :
									if (length > i + 2) {
										current.append((char) Integer.parseInt(expressionString.substring(i + 1, i + 3), 16));
										i += 2;
										break;
									}
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid \\x (at " + i + "): " + expressionString));
									return precompiled;
								case 'u' :
									if (length > i + 4) {
										current.append((char) Integer.parseInt(expressionString.substring(i + 1, i + 5), 16));
										i += 4;
										break;
									}
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid \\u (at " + i + "): " + expressionString));
									return precompiled;
								default :
									current.append(escaped);
							}
							continue;
						}
						current.append(c);
					}
					continue main;
				}
				case ST_2STRING : {
					local : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '"') {
							if (current.length() == 0) {
								precompiled.add(ParseConstants.TKV_STRING_EMPTY);
							} else {
								precompiled.add(new TKV_LCONSTS(Base.forString(current.toString())));
								current.setLength(0);
							}
							state = ExpressionParser.ST_WHITESPACE;
							continue main;
						}
						if (c == '\\') {
							++i;
							if (length <= i) {
								continue main;
							}
							final char escaped = expression[i];
							switch (escaped) {
								case 'n' :
									current.append('\n');
									continue local;
								case 'r' :
									current.append('\r');
									continue local;
								case 't' :
									current.append('\t');
									continue local;
								case 'b' :
									current.append('\b');
									continue local;
								case 'f' :
									current.append('\f');
									continue local;
								case 'x' :
									if (length > i + 2) {
										current.append((char) Integer.parseInt(expressionString.substring(i + 1, i + 3), 16));
										i += 2;
										continue local;
									}
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid \\x (at " + i + "): " + expressionString));
									return precompiled;
								case 'u' :
									if (length > i + 4) {
										current.append((char) Integer.parseInt(expressionString.substring(i + 1, i + 5), 16));
										i += 4;
										continue local;
									}
									precompiled.clear();
									precompiled.add(new TKV_ERROR_A_C_E("Invalid \\u (at " + i + "): " + expressionString));
									return precompiled;
								default :
							}
							current.append(escaped);
							continue local;
						}
						current.append(c);
					}
					continue main;
				}
				case ST_FUNCTION_DECL_ARGS : {
					arguments : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == ')') {
							state = ExpressionParser.ST_FUNCTION_DECL_MORE;
							break;
						}
						if (ExpressionParser.isIdentifierStart(c)) {
							final int last = precompiled.size() - 1;
							if (precompiled.get(last) == ParseConstants.TKV_UNDEFINED) {
								/** we'll need real array to collect arguments */
								precompiled.set(last, new TKV_LCONST(new ParserArguments()));
							}
							current.append(c);
							state = ExpressionParser.ST_FUNCTION_DECL_ARGUMENT;
							continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							continue arguments;
						}
						{
							precompiled.clear();
							precompiled.add(
									new TKV_ERROR_A_C_E( //
											"Invalid character for function declaration arguments (at " + i + "): " + expressionString));
							return precompiled;
						}
					}
					continue main;
				}
				case ST_FUNCTION_DECL_ARGUMENT : {
					argument : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == ')') {
							((ParserArguments) precompiled.get(precompiled.size() - 1).toConstantValue()).add(current.toString());
							current.setLength(0);
							state = ExpressionParser.ST_FUNCTION_DECL_MORE;
							continue main;
						}
						if (c == ',') {
							((ParserArguments) precompiled.get(precompiled.size() - 1).toConstantValue()).add(current.toString());
							current.setLength(0);
							state = ExpressionParser.ST_FUNCTION_DECL_ARGS;
							continue main;
						}
						if (ExpressionParser.isIdentifierPart(c)) {
							current.append(c);
							continue argument;
						}
						if (ExpressionParser.isWhitespace(c)) {
							continue argument;
						}
						{
							precompiled.clear();
							precompiled.add(
									new TKV_ERROR_A_C_E( //
											"Invalid character for function declaration arguments (at " + i + "): " + expressionString));
							return precompiled;
						}
					}
					continue main;
				}
				case ST_FUNCTION_DECL_MORE : {
					more : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '{') {
							blockStart = i + 1;
							functionLevel = ++levelBrace;
							state = ExpressionParser.ST_FUNCTION_DECL_BODY;
							continue main;
						}
						if (ExpressionParser.isWhitespace(c)) {
							continue more;
						}
						{
							precompiled.clear();
							precompiled.add(
									new TKV_ERROR_A_C_E( //
											"Code block ('{') start expected (at " + i + "): " + expressionString));
							return precompiled;
						}
					}
					continue main;
				}
				case ST_FUNCTION_DECL_BODY : {
					body : for (; i < length; ++i) {
						final char c = expression[i];
						if (c == '\'') {
							state = ExpressionParser.ST_1STRING_FUNCTION_DECL;
							continue main;
						}
						if (c == '"') {
							state = ExpressionParser.ST_2STRING_FUNCTION_DECL;
							continue main;
						}
						if (c == '{') {
							++levelBrace;
							continue body;
						}
						if (c == '}') {
							if (levelBrace-- == functionLevel) {
								final int last = precompiled.size() - 1;
								final BaseObject constant = precompiled.get(last).toConstantValue();
								final String[] arguments = constant == BaseObject.UNDEFINED
									? null
									: ((ParserArguments) constant).toArray();
								// System.out.println( ">>> >>> CODE: " +
								// expressionString.substring( blockStart, i )
								// );
								precompiled.set(
										last, //
										new TKV_CFUNCTION(
												identifier, //
												arguments,
												expressionString.substring(blockStart, i)));
								identifier = null;
								state = ExpressionParser.ST_WHITESPACE;
								continue main;
							}
						}
					}
					continue main;
				}
				case ST_REGEXP_START : {
					local : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case '/' : {
								identifier = current.toString();
								current.setLength(0);
								/** increment to next index */
								++i;
								/** fall-through there */
								state = ExpressionParser.ST_REGEXP_FLAGS;
								break local;
							}
							case '\\' : {
								current.append('\\');
								final char next = expression[++i];
								current.append(
										next == '\r'
											? expression[++i]
											: next);
								continue;
							}
							default :
						}
						current.append(c);
					}
				}
				//$FALL-THROUGH$
				case ST_REGEXP_FLAGS : {
					local : for (; i < length; ++i) {
						final char c = expression[i];
						switch (c) {
							case 'i' :
							case 'g' :
							case 'm' : {
								if (current.lastIndexOf(String.valueOf(c)) != -1) {
									throw new RuntimeException("RegExp: flag '" + c + "' is already specified");
								}
								current.append(c);
								continue;
							}
							default :
								/** step back */
								i--;
								break local;
						}
					}
					{
						final String flags = current.toString().trim();
						ExpressionParser.addRegExp(assembly, precompiled, identifier, flags);
						identifier = null;
						current.setLength(0);
						state = ExpressionParser.ST_WHITESPACE;
					}
					continue main;
				}
				default :
			}
			assert false : "Invalid state change to: " + state;
		}
		switch (state) {
			case ST_WHITESPACE :
				return precompiled;
			case ST_IDENTIFIER_CONST : {
				if (current.length() > 0) {
					ExpressionParser.addIdentifierConst(assembly, precompiled, current.toString());
					return precompiled;
				}
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated constant: " + expressionString));
				return precompiled;
			}
			case ST_IDENTIFIER_NEW_ACCS :
			case ST_IDENTIFIER_NEW : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Invalid constructor call: " + expressionString));
				return precompiled;
			}
			case ST_IDENTIFIER_ROOT : {
				if (current.length() > 0) {
					ExpressionParser.addIdentifierRoot(precompiled, current.toString(), expressionString);
				}
				return precompiled;
			}
			case ST_IDENTIFIER_ACCS : {
				if (current.length() > 0) {
					ExpressionParser.addIdentifierAccess(precompiled, current.toString());
					return precompiled;
				}
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Invalid access: " + expressionString));
				return precompiled;
			}
			case ST_NUMBER : {
				if (current.length() > 0) {
					ExpressionParser.addNumber(precompiled, current.toString(), 'I');
				}
				return precompiled;
			}
			case ST_NUMBER_HEX : {
				if (current.length() > 0) {
					ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
					return precompiled;
				}
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated hexadecimal number: " + expressionString));
				return precompiled;
			}
			case ST_NUMBER_OCT : {
				if (current.length() > 0) {
					ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
					return precompiled;
				}
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated octal number: " + expressionString));
				return precompiled;
			}
			case ST_NUMBER_BIN : {
				if (current.length() > 0) {
					ExpressionParser.addNumberHex(precompiled, current.toString(), 'i');
					return precompiled;
				}
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated binary number: " + expressionString));
				return precompiled;
			}
			case ST_NUMBER_WITH_EXP :
			case ST_NUMBER_FLOAT : {
				if (current.length() > 0) {
					ExpressionParser.addNumberFloat(precompiled, current.toString(), 'D');
				}
				return precompiled;
			}
			case ST_CODE : {
				if (current.length() > 0) {
					ExpressionParser.addOperator(assembly, precompiled, current.toString());
				}
				return precompiled;
			}
			case ST_FUNCTION_NEW : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated constructor call: " + expressionString));
				return precompiled;
			}
			case ST_FUNCTION_ROOT :
			case ST_FUNCTION_ACCS : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated function call: " + expressionString));
				return precompiled;
			}
			case ST_1STRING :
			case ST_2STRING :
			case ST_1STRING_FUNCTION_NEW :
			case ST_2STRING_FUNCTION_NEW :
			case ST_1STRING_FUNCTION_ROOT :
			case ST_2STRING_FUNCTION_ROOT :
			case ST_1STRING_FUNCTION_ACCS :
			case ST_2STRING_FUNCTION_ACCS :
			case ST_1STRING_FUNCTION_DECL :
			case ST_2STRING_FUNCTION_DECL : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated string constant: " + expressionString));
				return precompiled;
			}
			case ST_FUNCTION_DECL_NAME :
			case ST_FUNCTION_DECL_ARGS :
			case ST_FUNCTION_DECL_ARGUMENT : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated function declaration arguments: " + expressionString));
				return precompiled;
			}
			case ST_FUNCTION_DECL_MORE : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Function declaration body expected: " + expressionString));
				return precompiled;
			}
			case ST_FUNCTION_DECL_BODY : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated function declaration body: " + expressionString));
				return precompiled;
			}
			case ST_REGEXP_START : {
				precompiled.clear();
				precompiled.add(new TKV_ERROR_A_C_E("Unterminated regular expression: " + expressionString));
				return precompiled;
			}
			case ST_REGEXP_FLAGS : {
				final String flags = current.toString().trim();
				ExpressionParser.addRegExp(assembly, precompiled, identifier, flags);
				return precompiled;
			}
			default :
		}
		precompiled.clear();
		precompiled.add(new TKV_ERROR_A_C_E("Unknown parser state(" + state + "): " + expressionString));
		return precompiled;
	}
	
	/** @param data */
	public static final void statusFill(final StatusInfo data) {
		
		data.put("CALC: expessions (public)", Format.Compact.toDecimal(ExpressionParser.EXPR_PUBLIC_COUNT));
		data.put("CALC: builtin hit count", Format.Compact.toDecimal(ExpressionParser.EXPR_BUILTIN_HITS));
		data.put("CALC: expessions (private)", Format.Compact.toDecimal(ExpressionParser.EXPR_PRIVATE_COUNT));
		data.put("CALC: execution error count", Format.Compact.toDecimal(ExpressionParser.ERROR_COUNT));
		data.put("CALC: execution exceptions", Format.Compact.toDecimal(ExpressionParser.EXCEPTION_COUNT));
	}
}
