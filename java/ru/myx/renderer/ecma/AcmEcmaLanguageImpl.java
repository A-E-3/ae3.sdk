package ru.myx.renderer.ecma;

import java.util.HashMap;
import java.util.Map;

import java.util.function.Function;
import ru.myx.ae3.eval.CompileTargetMode;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.report.Report;

/**
 * @author myx
 *
 */
public class AcmEcmaLanguageImpl implements LanguageImpl {

	/**
	 * static instance, constructor is not accessible to disallow creation of
	 * multiple objects.
	 */
	public static final LanguageImpl INSTANCE = new AcmEcmaLanguageImpl();

	private static final byte[] CHARS = new byte[65536];

	private static final Map<String, TokenStatement> KEYWORDS_ANY;

	private static final Map<String, TokenStatement> KEYWORDS_START;

	static {
		for (int i = 65535; i >= 0; --i) {
			final char c = (char) i;
			AcmEcmaLanguageImpl.CHARS[i] = (byte) ((Character.isWhitespace(c)
				? 0x01
				: 0x00)
					+ (Character.isJavaIdentifierStart(c)
						? 0x02
						: 0x00)
					+ (Character.isJavaIdentifierPart(c)
						? 0x04
						: 0x00)
					+ (Character.isDigit(c)
						? 0x08
						: 0x00));
		}
		{
			/**
			 * keywords
			 */
			final Map<String, TokenStatement> all = new HashMap<>(32, 0.25f);
			all.put("break", new TokenStatementBreak(null, 0));
			all.put("case", new TokenStatementCase(null, 0));
			all.put("catch", new TokenStatementCatch(null, 0));
			all.put("class", new TokenStatementClass(null, 0));
			all.put("const", new TokenStatementConst(null, 0));
			all.put("continue", new TokenStatementContinue(null, 0));
			all.put("default", new TokenStatementDefault(null, 0));
			all.put("do", new TokenStatementDo(null, 0));
			all.put("else", new TokenStatementElse(null, 0));
			all.put("finally", new TokenStatementFinally(null, 0));
			all.put("for", new TokenStatementFor(null, 0));
			all.put("function", new TokenStatementFunction(null, 0));
			all.put("if", new TokenStatementIf(null, 0));
			all.put("import", new TokenStatementImport(null, 0));
			all.put("let", new TokenStatementLet(null, 0));
			all.put("return", new TokenStatementReturn(null, 0));
			all.put("switch", new TokenStatementSwitch(null, 0));
			all.put("throw", new TokenStatementThrow(null, 0));
			all.put("try", new TokenStatementTry(null, 0));
			all.put("var", new TokenStatementVar(null, 0));
			all.put("while", new TokenStatementWhile(null, 0));
			all.put("with", new TokenStatementWith(null, 0));
			all.put("$output", new TokenStatementOutput(null, 0));
			KEYWORDS_START = all;
			final Map<String, TokenStatement> any = new HashMap<>(32, 0.25f);
			for (final String key : all.keySet()) {
				final TokenStatement statement = all.get(key);
				if (!statement.isOnlyWhenFirstInStatement()) {
					any.put(key, statement);
				}
			}
			KEYWORDS_ANY = any;
		}
	}

	private static final String formatCodeHighlight(final char[] code, final int highlightLine) {

		final StringBuilder builder = new StringBuilder();
		int line = 1, position = 0;
		for (final char c : code) {
			if (c == '\n') {
				if (line - 2 <= highlightLine && line + 2 >= highlightLine) {
					final String string = builder.substring(position);
					builder.setLength(position);
					builder.append('\t');
					builder.append(line == highlightLine
						? '>'
						: ' ');
					builder.append(line);
					builder.append(line == highlightLine
						? '>'
						: '|');
					builder.append(' ');
					builder.append(string);
					builder.append('\n');
					position = builder.length();
					if (line - 1 > highlightLine) {
						break;
					}
				} else {
					builder.setLength(position);
				}
				++line;
			} else //
			if (line + 2 >= highlightLine) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	private static final String formatCodeHighlight(//
			final char[] code, final int line, @SuppressWarnings("unused") final int column) {

		return AcmEcmaLanguageImpl.formatCodeHighlight(code, line);
	}

	private static final boolean isIdentifierPart(final char c) {

		return (AcmEcmaLanguageImpl.CHARS[c] & 0x04) != 0;
	}

	private static final boolean isIdentifierStart(final char c) {

		return (AcmEcmaLanguageImpl.CHARS[c] & 0x02) != 0;
	}

	private static final boolean isWhitespace(final char c) {

		return (AcmEcmaLanguageImpl.CHARS[c] & 0x01) != 0;
	}

	private AcmEcmaLanguageImpl() {
		// empty
	}

	@Override
	public final void compile(final String identity, final Function<String, String> folder, final String name, final ProgramAssembly assembly, final CompileTargetMode mode)
			throws Evaluate.CompilationException {

		final String source = folder.apply(name);
		if (source == null) {
			assembly.addError("No source (null source) for key: " + name + " in " + folder);
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
			return;
		}
		final StringBuilder buffer = new StringBuilder();
		final StringBuilder string = new StringBuilder();
		final StringBuilder unicode = new StringBuilder(8);
		ParserState state2 = ParserState.WHITESPACE;
		ParserState state1 = ParserState.WHITESPACE;
		ParserState state = ParserState.WHITESPACE;
		boolean valueHasJustClosed = false;
		int braceLevel = 0;
		int argsLevel = 1;
		int tokenStartPosition = 0;
		int line = 1;
		int column = 0;
		final TokenStatement statementRoot;
		switch (mode) {
			case BLOCK :
				statementRoot = new TokenStatementRootScript(identity, line);
				break;
			case INLINE :
				statementRoot = new TokenStatementRootInline(identity, line);
				break;
			case STANDALONE :
				statementRoot = new TokenStatementRootOutput(identity, line);
				break;
			default :
				throw new IllegalArgumentException("mode is unsupported: " + mode);
		}
		TokenStatement statementCurrent = statementRoot;
		final AcmEcmaParserStack statementStack = new AcmEcmaParserStack();
		final char[] code = source.toCharArray();
		main : for (int i = 0; i < code.length; ++i) {
			if (state == ParserState.WHITESPACE) {
				local : for (; i < code.length;) {
					final char c = code[i];
					switch (c) {
						case '\r' : {
							++i;
							continue local;
						}
						case '\n' : {
							if (statementCurrent.isNewLineSemicolon() && buffer.length() == 0) {
								if (statementStack.isEmpty()) {
									throw new IllegalStateException(
											"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
													+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								statementCurrent = statementStack.pop();
							}
							++line;
							column = 0;
							++i;
							if (buffer.length() > 0) {
								buffer.append('\n');
							}
							continue local;
						}
						/**
						 * faster than next check
						 */
						case ' ' :
						case '\t' : {
							++i;
							++column;
							continue local;
						}
						default :
					}
					if (AcmEcmaLanguageImpl.isWhitespace(c)) {
						++i;
						++column;
						continue local;
					}
					if (buffer.length() > 0) {
						buffer.append(' ');
					}
					if (AcmEcmaLanguageImpl.isIdentifierStart(c)) {
						tokenStartPosition = buffer.length();
						state = ParserState.IDENTIFIER;
						column++;
						buffer.append(c);
						++i;
						for (; i < code.length; ++i) {
							final char c2 = code[i];
							if (!AcmEcmaLanguageImpl.isIdentifierPart(c2)) {
								/**
								 * repeat;
								 */
								--i;
								continue main;
							}
							++column;
							buffer.append(c2);
						}
						continue main;
					}
					{
						if (valueHasJustClosed) {
							valueHasJustClosed = false;
						}
						state = ParserState.CODE;
						/**
						 * need repeat
						 */
						--i;
						continue main;
					}
				}
				/**
				 * to re-check length (cannot just fall through)
				 */
				continue main;
			}
			{
				final char c = code[i];
				if (c == '\r') {
					continue;
				}
				++column;
				if (c == '\n') {
					if (state == ParserState.STRU1 || state == ParserState.STRU2 || state == ParserState.STRU3 || state == ParserState.STRU4) {
						throw new IllegalStateException(
								"Unterminated unicode symbol in string constant: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
										+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
					}
					if (state == ParserState.STR1 || state == ParserState.STR2) {
						throw new IllegalStateException(
								"Unterminated string constant: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
										+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
					}
					if (state == ParserState.COMMENT_SINGLE) {
						state = state1;
						state1 = state2;
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.COMMENT_MULTILINE_END_SUSPECT) {
						state = ParserState.COMMENT_MULTILINE;
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.STR_ESCAPE) {
						state = state1;
						state1 = state2;
						line++;
						column = 0;
						continue;
					}
					if (state == ParserState.OUTPUT_SUSPECT) {
						state = state1;
						state1 = state2;
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.COMMENT_MULTILINE) {
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.ARGUMENTS_DECLARATION) {
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.OUTPUT) {
						string.append(c);
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.OUTPUT_CLOSE_SUSPECT) {
						state = ParserState.OUTPUT;
						string.append('<').append(c);
						++line;
						column = 0;
						continue;
					}
					if (state == ParserState.CODE || state == ParserState.IDENTIFIER) {
						if (statementCurrent.isNewLineSemicolon() && buffer.length() == 0) {
							if (statementStack.isEmpty()) {
								throw new IllegalStateException(
										"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
												+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
							}
							statementCurrent = statementStack.pop();
						}
						++line;
						column = 0;
						if (state == ParserState.CODE) {
							state = ParserState.WHITESPACE;
							continue;
						}
						/**
						 * have to continue otherwise, for identifier lookup
						 */
					} else {
						throw new IllegalStateException(
								state + " while new line: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
										+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
					}
				}
				if (state == ParserState.OUTPUT_SUSPECT) {
					if (c == '>') {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						if (buffer.length() > 0) {
							final TokenStatement statement = new TokenStatementSingle(buffer.toString(), identity, line);
							buffer.setLength(0);
							for (;;) {
								if (statementCurrent.addStatement(statement)) {
									statement.setParent(statementCurrent);
									break;
								}
								if (statementStack.isEmpty()) {
									throw new IllegalStateException(
											"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
													+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								statementCurrent = statementStack.pop();
							}
						}
						state = ParserState.OUTPUT;
						continue main;
					}
					buffer.append('%');
					state = state1;
					state1 = state2;
				}

				if (state == ParserState.IDENTIFIER) {
					if (!AcmEcmaLanguageImpl.isIdentifierPart(c)) {
						final String pattern = buffer.substring(tokenStartPosition);
						final TokenStatement discovered;
						final boolean suspectSemicolon = valueHasJustClosed;
						if (suspectSemicolon) {
							discovered = AcmEcmaLanguageImpl.KEYWORDS_START.get(pattern);
							valueHasJustClosed = false;
						} else {
							discovered = tokenStartPosition == 0 && statementCurrent.isNextStatementFromScratch()
								? AcmEcmaLanguageImpl.KEYWORDS_START.get(pattern)
								: AcmEcmaLanguageImpl.KEYWORDS_ANY.get(pattern);
						}
						if (discovered == null) {
							if (tokenStartPosition == 0 && statementCurrent.isIdentifierPossible() && statementCurrent.setIdentifier(pattern)) {
								buffer.setLength(0);
							}
							state = ParserState.CODE;
						} else {
							if (braceLevel > 0) {
								/**
								 * DEBUG <code>
								System.out.println( ">>> >> "
									+ braceLevel
									+ " "
									+ discovered
									+ ", state1="
									+ state1
									+ ", state2="
									+ state2
									+ ", buffer="
									+ buffer );
								</code>
								 */
							} else {
								if (tokenStartPosition > 0) {
									final TokenStatement statement = new TokenStatementSingle(buffer.substring(0, tokenStartPosition), identity, line);
									for (;;) {
										if (statementCurrent.addStatement(statement)) {
											statement.setParent(statementCurrent);
											break;
										}
										if (statementStack.isEmpty()) {
											throw new IllegalStateException(
													"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
															+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
										}
										statementCurrent = statementStack.pop();
									}
								}
								if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
									throw new IllegalStateException(
											"Keyword '" + discovered.getKeyword() + "' cannot be used as an identitier: file=" + identity + ", line=" + line + ", column=" + column
													+ ", buffer=" + buffer + ", code:\r\n" + AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								if (argsLevel > 1) {
									throw new IllegalStateException(
											"Keyword '" + discovered.getKeyword() + "' cannot be used in arguments: file=" + identity + ", args=" + argsLevel + ", line=" + line
													+ ", column=" + column + ", buffer=" + buffer + ", code:\r\n" + AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								final TokenStatement statement = discovered.createStatement(identity, line);
								for (;;) {
									if (statementCurrent.addStatement(statement)) {
										statement.setParent(statementCurrent);
										break;
									}
									if (statementStack.isEmpty()) {
										throw new IllegalStateException(
												"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
														+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
									}
									statementCurrent = statementStack.pop();
								}
								statementStack.push(statementCurrent);
								statementCurrent = statement;
								buffer.setLength(0);
								state = ParserState.CODE;
							}
						}
					}
				}
				if (state == ParserState.IDENTIFIER || state == ParserState.CODE) {
					if (c == ';') {
						if (braceLevel > 0) {
							/**
							 * DEBUG <code>
							System.out.println( ">>> >> "
								+ braceLevel
								+ " "
								+ "';'"
								+ ", state1="
								+ state1
								+ ", state2="
								+ state2
								+ ", buffer="
								+ buffer );
							</code>
							 */
							buffer.append(';');
							continue;
						}

						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						final TokenStatement statement;
						if (buffer.length() > 0) {
							statement = new TokenStatementSingle(buffer.toString(), identity, line);
							buffer.setLength(0);
						} else {
							statement = new TokenStatementEmpty(identity, line);
						}
						for (;;) {
							if (statementCurrent.addStatement(statement)) {
								statement.setParent(statementCurrent);
								break;
							}
							if (statementStack.isEmpty()) {
								throw new IllegalStateException(
										"Unexpected statement: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
												+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
							}
							statementCurrent = statementStack.pop();
						}
						while (statementCurrent.isTotallyComplete()) {
							statementCurrent = statementStack.pop();
						}
						continue main;
					}
					if (c == '\'') {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						state2 = state1;
						state1 = state;
						state = ParserState.STR1;
						continue;
					}
					if (c == '"') {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						state2 = state1;
						state1 = state;
						state = ParserState.STR2;
						continue;
					}
					if (c == '/') {
						state2 = state1;
						state1 = state;
						state = ParserState.COMMENT_START_SUSPECT;
						continue;
					}
					if (c == ':' && statementCurrent.isLabelStatement()) {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						if (buffer.length() > 0) {
							final String expression = buffer.toString();
							buffer.setLength(0);
							if (!statementCurrent.setArguments(expression)) {
								throw new IllegalStateException(
										"Unexpected label: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + expression + ", code:\r\n"
												+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
							}
						}
						final TokenStatement statement = new TokenStatementEmpty(identity, line);
						for (;;) {
							if (statementCurrent.addStatement(statement)) {
								statement.setParent(statementCurrent);
								break;
							}
							if (statementStack.isEmpty()) {
								throw new IllegalStateException(
										"Unexpected statement: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
												+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
							}
							statementCurrent = statementStack.pop();
						}
						continue;
					}
					if (c == '%') {
						state2 = state1;
						state1 = state;
						state = ParserState.OUTPUT_SUSPECT;
						continue;
					}
					if (c == '{') {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						if (buffer.length() > 0) {
							/**
							 * DEBUG <code>
							System.out.println( ">>> >> { " + statementCurrent + " buffer=" + buffer );
							* </code>
							 */
							buffer.append(c);
							braceLevel++;
							continue;
						}
						while (statementCurrent.isTotallyComplete()) {
							statementCurrent = statementStack.pop();
						}
						if (!statementCurrent.isKeywordExpectStatement()) {
							/**
							 * <code>
							System.out.println( "========= { "
								+ statementCurrent
								+ ", statement="
								+ statementCurrent.isStatementStart()
								+ ", line="
								+ statementCurrent.getLine()
								+ ", stack="
								+ statementStack );
								</code>
							 */
							buffer.append(c);
							braceLevel++;
							continue;
						}
						if (buffer.length() > 0) {
							final TokenStatement statement = new TokenStatementSingle(buffer.toString(), identity, line);
							buffer.setLength(0);
							for (;;) {
								if (statementCurrent.addStatement(statement)) {
									statement.setParent(statementCurrent);
									break;
								}
								if (statementStack.isEmpty()) {
									throw new IllegalStateException(
											"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
													+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								statementCurrent = statementStack.pop();
							}
						}
						final TokenStatement statement = new TokenStatementBlock(identity, line);
						for (;;) {
							if (statementCurrent.addStatement(statement)) {
								statement.setParent(statementCurrent);
								break;
							}
							if (statementStack.isEmpty()) {
								throw new IllegalStateException(
										"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
												+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
							}
							statementCurrent = statementStack.pop();
						}
						statementStack.push(statementCurrent);
						statementCurrent = statement;
						continue;
					}
					if (c == '}') {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						if (braceLevel > 0) {
							/** <code>System.out.println( "========= } " + statementCurrent + " buffer=" + buffer );</code> */
							buffer.append(c);
							if (--braceLevel == 0) {
								valueHasJustClosed = true;
							}
							continue main;
						}
						while (!statementCurrent.isBlockStatement()) {
							statementCurrent = statementStack.pop();
						}
						if (buffer.length() > 0) {
							final TokenStatement statement = new TokenStatementSingle(buffer.toString(), identity, line);
							buffer.setLength(0);
							for (;;) {
								if (statementCurrent.addStatement(statement)) {
									statement.setParent(statementCurrent);
									break;
								}
								if (statementStack.isEmpty()) {
									throw new IllegalStateException(
											"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
													+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								statementCurrent = statementStack.pop();
							}
						}
						if (statementCurrent == statementRoot || statementStack.isEmpty()) {
							throw new IllegalStateException(
									"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						statementCurrent = statementStack.pop();
						valueHasJustClosed = true;
						state = ParserState.WHITESPACE;
						continue main;
					}
					if (c == '(' && buffer.length() == 0) {
						if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
							throw new IllegalStateException(
									"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", keyword=" + statementCurrent.getKeyword() + ", buffer="
											+ buffer + ", code:\r\n" + AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
						}
						state = ParserState.ARGUMENTS_DECLARATION;
						argsLevel = 1;
						continue;
					}
					if (AcmEcmaLanguageImpl.isWhitespace(c)) {
						state = ParserState.WHITESPACE;
						continue;
					}
					if (state == ParserState.CODE && AcmEcmaLanguageImpl.isIdentifierStart(c)) {
						tokenStartPosition = buffer.length();
						state = ParserState.IDENTIFIER;
						buffer.append(c);
						++i;
						for (; i < code.length; ++i) {
							final char c2 = code[i];
							if (!AcmEcmaLanguageImpl.isIdentifierPart(c2)) {
								/**
								 * repeat;
								 */
								--i;
								break;
							}
							++column;
							buffer.append(c2);
						}
						continue;
					}
					buffer.append(c);
					continue;
				}
				if (state == ParserState.ARGUMENTS_DECLARATION) {
					if (c == '\'') {
						state2 = state1;
						state1 = state;
						state = ParserState.STR1;
						continue;
					}
					if (c == '"') {
						state2 = state1;
						state1 = state;
						state = ParserState.STR2;
						continue;
					}
					if (c == '/') {
						state2 = state1;
						state1 = state;
						state = ParserState.COMMENT_START_SUSPECT;
						continue;
					}
					if (c == '(') {
						++argsLevel;
						buffer.append(c);
						continue;
					}
					if (c == ')') {
						if (--argsLevel == 0) {
							final String expression = buffer.toString();
							buffer.setLength(0);
							if (!statementCurrent.setArguments(expression)) {
								buffer.append('(').append(expression).append(')');
							}
							while (statementCurrent.isTotallyComplete()) {
								statementCurrent = statementStack.pop();
							}
							valueHasJustClosed = true;
							state = ParserState.WHITESPACE;
							continue;
						}
					}
					if (c == ' ') {
						buffer.append(' ');
						continue;
					}
					buffer.append(c);
					continue;
				}
				if (state == ParserState.OUTPUT) {
					if (c == '<') {
						state = ParserState.OUTPUT_CLOSE_SUSPECT;
						continue;
					}
					string.append(c);
					continue;
				}
				if (state == ParserState.OUTPUT_CLOSE_SUSPECT) {
					if (c == '<') {
						string.append(c);
						continue;
					}
					if (c == '%') {
						final String constant = string.toString();
						string.setLength(0);
						try {
							{
								final TokenStatement last = statementCurrent.lastStatement();
								/**
								 * null is not an instance
								 */
								if (last instanceof TokenStatementOut) {
									((TokenStatementOut) last).concatenate(constant);
									continue;
								}
							}
							final TokenStatement statement = new TokenStatementOut(constant, identity, line);
							for (;;) {
								if (statementCurrent.addStatement(statement)) {
									statement.setParent(statementCurrent);
									break;
								}
								if (statementStack.isEmpty()) {
									throw new IllegalStateException(
											"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
													+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
								}
								statementCurrent = statementStack.pop();
							}
							continue;
						} finally {
							state = state1;
							state1 = state2;
						}
					}
					state = ParserState.OUTPUT;
					string.append('<').append(c);
					continue;
				}
			}
			/**
			 * less common
			 */
			local : for (; i < code.length; ++i) {
				final char c = code[i];
				if (c == '\r') {
					/**
					 * ignore
					 */
					continue local;
				}
				if (c == '\n') {
					/**
					 * have to repeat
					 */
					--i;
					/**
					 * we'll handle it in main loop
					 */
					continue main;
				}
				if (state == ParserState.STRU1 || state == ParserState.STRU2 || state == ParserState.STRU3 || state == ParserState.STRU4) {
					/**
					 * collect code
					 */
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
						case 'A' :
						case 'a' :
						case 'B' :
						case 'b' :
						case 'C' :
						case 'c' :
						case 'D' :
						case 'd' :
						case 'E' :
						case 'e' :
						case 'F' :
						case 'f' :
							unicode.append(c);
							break;
						default :
							throw new IllegalArgumentException(
									"Illegal unicode character: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
											+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
					}
					/**
					 * handle the state
					 */
					if (state == ParserState.STRU4) {
						state = state1;
						state1 = state2;
						string.append((char) Integer.parseInt(unicode.toString(), 16));
						unicode.setLength(0);
						/**
						 * no jump to the main loop, STRU states are for STRx,
						 * both of which are handled in the same loop.
						 */
						continue local;
					}
					if (state == ParserState.STRU3) {
						state = ParserState.STRU4;
						continue local;
					}
					if (state == ParserState.STRU2) {
						state = ParserState.STRU3;
						continue local;
					}
					if (state == ParserState.STRU1) {
						state = ParserState.STRU2;
						continue local;
					}
					throw new IllegalStateException(
							"Internal parser error: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
									+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
				} else //
				if (state == ParserState.STR_ESCAPE) {
					switch (c) {
						case 'u' :
							state = ParserState.STRU1;
							continue local;
						case 'n' :
							string.append('\n');
							break;
						case 'r' :
							string.append('\r');
							break;
						case 't' :
							string.append('\t');
							break;
						default :
							string.append(c);
					}
					state = state1;
					state1 = state2;
					/**
					 * no jump to the main loop, cause STRE possible only for
					 * STRx, both of them handed in the same local loop
					 */
					continue local;
				}
				if (state == ParserState.STR1) {
					if (c == '\'') {
						final String constant = string.toString();
						final int position = assembly.constantRegister(constant);
						buffer.append(' ').append('@').append(position).append(' ');
						string.setLength(0);
						state = state1;
						state1 = state2;
						/**
						 * jump to the main loop
						 */
						continue main;
					}
					if (c == '\\') {
						state2 = state1;
						state1 = state;
						state = ParserState.STR_ESCAPE;
						continue local;
					}
					string.append(c);
					continue local;
				}
				if (state == ParserState.STR2) {
					if (c == '"') {
						final String constant = string.toString();
						final int position = assembly.constantRegister(constant);
						buffer.append(' ').append('@').append(position).append(' ');
						string.setLength(0);
						state = state1;
						state1 = state2;
						/**
						 * jump to the main loop
						 */
						continue main;
					}
					if (c == '\\') {
						state2 = state1;
						state1 = state;
						state = ParserState.STR_ESCAPE;
						continue local;
					}
					string.append(c);
					continue local;
				}
				if (state == ParserState.COMMENT_START_SUSPECT) {
					if (c == '/') {
						state = ParserState.COMMENT_SINGLE;
						continue local;
					}
					if (c == '*') {
						state = ParserState.COMMENT_MULTILINE;
						continue local;
					}
					{
						buffer.append('/');
						state = state1;
						state1 = state2;
						/**
						 * have to repeat (was not a comment)
						 */
						--i;
						/**
						 * jump to the main loop
						 */
						continue main;
					}
				}
				if (state == ParserState.COMMENT_SINGLE) {
					continue local;
				}
				if (state == ParserState.COMMENT_MULTILINE) {
					if (c == '*') {
						state = ParserState.COMMENT_MULTILINE_END_SUSPECT;
					}
					continue local;
				}
				if (state == ParserState.COMMENT_MULTILINE_END_SUSPECT) {
					if (c == '/') {
						state = state1;
						state1 = state2;
						/**
						 * jump to the main loop
						 */
						continue main;
					}
					if (c != '*') {
						state = ParserState.COMMENT_MULTILINE;
					}
					continue local;
				}
				{
					throw new IllegalStateException("Invalid state: " + state);
				}
			}
			/**
			 * local loop ended - new iteration of main loop
			 */
		}
		if (tokenStartPosition == 0 && statementCurrent.isIdentifierRequired()) {
			throw new IllegalStateException(
					"Identifier expected: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (buffer.length() > 0) {
			final TokenStatement statement = new TokenStatementSingle(buffer.toString(), identity, line);
			buffer.setLength(0);
			for (;;) {
				if (statementCurrent.addStatement(statement)) {
					statement.setParent(statementCurrent);
					break;
				}
				if (statementStack.isEmpty()) {
					throw new IllegalStateException(
							"Unexpected block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
									+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
				}
				statementCurrent = statementStack.pop();
			}
		}
		if (state == ParserState.STRU1 || state == ParserState.STRU2 || state == ParserState.STRU3 || state == ParserState.STRU4) {
			throw new IllegalStateException(
					"Unterminated unicode symbol in string constant: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (state == ParserState.STR1 || state == ParserState.STR2 || state == ParserState.STR_ESCAPE) {
			throw new IllegalStateException(
					"Unterminated string constant: file=" + identity + ", line=" + line + ", column=" + column + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (state == ParserState.ARGUMENTS_DECLARATION) {
			throw new IllegalStateException(
					"Unterminated arguments: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (state == ParserState.COMMENT_MULTILINE_END_SUSPECT) {
			throw new IllegalStateException(
					"Unterminated multiline comment: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (state == ParserState.OUTPUT_SUSPECT) {
			throw new IllegalStateException(
					"Unterminated statement: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		if (state == ParserState.OUTPUT || state == ParserState.OUTPUT_CLOSE_SUSPECT) {
			throw new IllegalStateException(
					"Unterminated output block: file=" + identity + ", line=" + line + ", column=" + column + ", buffer=" + buffer + ", code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, line, column));
		}
		while (!statementStack.isEmpty() && !(statementStack.peek() instanceof TokenStatementBlock)) {
			statementCurrent = statementStack.pop();
		}
		if (!statementStack.isEmpty() && statementStack.peek() != statementRoot) {
			final TokenStatement statement = statementStack.peek();
			throw new IllegalStateException(
					"Block is not closed: file=" + identity + ", line=" + statement.getLine() + ", stack=[" + statementStack + "], code:\r\n"
							+ AcmEcmaLanguageImpl.formatCodeHighlight(code, statement.getLine()));
		}
		if (Report.MODE_DEVEL) {
			assembly.addDebug("ECMA RENDERER, identity=" + identity + ", mode=" + mode + ", lines=" + line + ", chars=" + source.length());
		}
		
		try {
			statementRoot.toAssembly(assembly, assembly.size());
		} catch (final Exception e) {
			throw new Evaluate.CompilationException(this.getClass().getSimpleName(), e);
		}
		
		if (mode == CompileTargetMode.STANDALONE) {
			final Object errors = assembly.getErrors();
			if (errors != null) {
				Report.exception("ECMA", "Errors while compiling unit, name=" + identity, new Error(errors.toString()));
			}
		}
	}

	@Override
	public String[] getAssociatedAliases() {

		return new String[]{
				//
				"ACM.ECMA", //
				"ECMA", //
				"ACM.JSCRIPT", //
				"JSCRIPT", //
				"ACM.JAVASCRIPT", //
				"JAVASCRIPT", //
				"ACM.ECMA262", //
				"ECMA262",//
		};
	}

	@Override
	public String[] getAssociatedExtensions() {

		return new String[]{
				//
				".js", //
				".jslt",//
		};
	}

	@Override
	public String[] getAssociatedMimeTypes() {

		return new String[]{
				//
				"text/javascript", //
				"application/javascript", //
				"application/x-ae3-js-layout-transform", //
				"application/x-ae3-js-class", //
		};
	}

	@Override
	public String getKey() {

		return "ECMA-262";
	}
}
