/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import java.util.ArrayList;
import java.util.List;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.exec.IAVV_ARGS_SETUP;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementFunction extends TokenStatementAbstract {
	
	
	static final List<String> toLocals(final String statement) {
		
		
		final List<String> variables = new ArrayList<>();
		{
			final StringBuilder buffer = new StringBuilder();
			int levelBrace = 0;
			boolean isQuote = false;
			boolean isApos = false;
			boolean nextSymbol = false;
			boolean skipping = false;
			for (final char c : statement.toCharArray()) {
				if (!skipping) {
					if (Character.isWhitespace(c)) {
						if (buffer.length() > 0) {
							variables.add(buffer.toString());
							buffer.setLength(0);
							skipping = true;
						}
						continue;
					}
					if (!Character.isJavaIdentifierPart(c)) {
						if (buffer.length() > 0) {
							variables.add(buffer.toString());
							buffer.setLength(0);
						}
						skipping = true;
					}
				}
				switch (c) {
					case '[' :
					case '(' :
						if (!isQuote && !isApos) {
							levelBrace++;
						}
						nextSymbol = false;
						break;
					case ']' :
					case ')' :
						if (!isQuote && !isApos) {
							levelBrace--;
						}
						nextSymbol = false;
						break;
					case '"' :
						if (!nextSymbol) {
							if (!isApos) {
								isQuote = !isQuote;
							}
						}
						nextSymbol = false;
						break;
					case '\'' :
						if (!nextSymbol) {
							if (!isQuote) {
								isApos = !isApos;
							}
						}
						nextSymbol = false;
						break;
					case '\\' :
						if (isQuote || isApos) {
							nextSymbol = true;
						}
						break;
					case ',' :
						if (!isQuote && !isApos && levelBrace == 0) {
							if (!skipping && buffer.length() > 0) {
								variables.add(buffer.toString());
								buffer.setLength(0);
							}
							skipping = false;
							continue;
						}
						break;
					default :
						nextSymbol = false;
				}
				if (!skipping) {
					buffer.append(c);
				}
			}
			if (!skipping && buffer.length() > 0) {
				variables.add(buffer.toString());
			}
		}
		return variables;
	}
	
	private String identifier;
	
	private String arguments;
	
	private TokenStatementBlock token;
	
	private BaseObject locals;
	
	TokenStatementFunction(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		if (this.token == null && statement instanceof TokenStatementBlock) {
			this.token = (TokenStatementBlock) statement;
			this.token.setLocalsTarget(this);
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		return new TokenStatementFunction(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("function");
		buffer.append('\t');
		buffer.append(this.identifier);
		buffer.append('(');
		buffer.append(this.arguments);
		buffer.append(')');
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "function";
	}
	
	@Override
	public final boolean isIdentifierPossible() {
		
		
		return this.identifier == null && this.arguments == null;
	}
	
	@Override
	public final boolean isIdentifierRequired() {
		
		
		return this.identifier == null && this.arguments == null;
	}
	
	@Override
	public final boolean isKeywordExpectStatement() {
		
		
		return true;
	}
	
	@Override
	public final boolean isLabelStatement() {
		
		
		return false;
	}
	
	@Override
	public boolean isNextStatementFromScratch() {
		
		
		return true;
	}
	
	@Override
	public boolean isOnlyWhenFirstInStatement() {
		
		
		return true;
	}
	
	@Override
	public boolean isTotallyComplete() {
		
		
		return this.token != null && this.arguments != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		
		
		if (this.arguments == null) {
			this.arguments = expression;
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean setIdentifier(final String identifier) {
		
		
		if (this.identifier == null) {
			this.identifier = identifier;
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean setLocals(final BaseObject locals) {
		
		
		if (locals == null || !locals.baseHasKeysOwn()) {
			return true;
		}
		if (this.locals == null) {
			this.locals = new BaseNativeObject();
		}
		this.locals.baseDefineImportOwnEnumerable(locals);
		return true;
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		
		this.addDebug(assembly, "function " + this.identifier + '(' + this.arguments + ')');
		if (this.identifier == null) {
			assembly.addError("function name is required!");
			return;
		}
		if (this.arguments == null) {
			assembly.addError("argument list is required!");
			return;
		}
		if (this.token == null) {
			assembly.addError("function body is required!");
			return;
		}
		this.parent.setLocals(new BaseNativeObject(this.identifier, BaseObject.FALSE));
		final List<String> arguments = TokenStatementFunction.toLocals(this.arguments);
		final int argumentCount = arguments.size();
		final ModifierArgument argumentA = argumentCount == 0
			? ParseConstants.TKV_UNDEFINED.toDirectModifier()
			: new ModifierArgumentA30IMM(arguments.toArray(new String[argumentCount]));
		final InstructionEditable function = OperationsA11.XCFUNCTION_N.instructionCreate(argumentA, 0, ResultHandler.FA_BNN_NXT);
		assembly.addInstruction(function);
		final int bodyStart = assembly.size();
		this.token.toAssembly(assembly, assembly.size());
		final int functionSize = assembly.getInstructionCount(bodyStart);
		if (functionSize == 0) {
			/**
			 * Doesn't matter arguments and anything - there's no function body
			 */
			assembly.truncate(bodyStart);
			/**
			 * still need return undefined
			 */
			function.setConstant(1).setFinished();
		} else {
			if (argumentCount == 0 && this.locals == null) {
				function.setConstant(functionSize + 1 /* extra return */).setFinished();
			} else {
				final ProgramPart body = assembly.toProgram(bodyStart);
				/**
				 * There's no need for EENTRY/ELEAVE, cause vmCall does them
				 * already.
				 */
				/**
				 * Not needed<br>
				 * <code>
				final InstructionA01 entry = new InstructionA01( OperationsA01.EENTRY, //
						0,
						ModifierStore.NN,
						ExecStateCode.NEXT );
				assembly.addInstruction( -1, entry );
				 * </code>
				 */
				/**
				 * calleeName is null, cause function statement declares itself
				 * in common scope (however this could be overwritten by another
				 * function declaration)
				 */
				assembly.addInstruction(argumentCount == 0
					? Instructions.INSTR_CSCOPE_0_0_NN_NEXT
					: new IAVV_ARGS_SETUP(null, arguments.toArray(new String[argumentCount])));
				
				final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
				if (locals != null) {
					assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
				}
				
				assembly.addInstruction(body);
				/**
				 * Not needed<br>
				 * <code>
				entry.setConstant( functionSize + 1 );
				assembly.addInstruction( -1, Instructions.INSTR_ELEAVE_0_NN_NEXT );
				function.setConstant( functionSize + 1 + 2 + 1 );
				 * </code>
				 */
				function.setConstant(functionSize + 1 /* scope */ + 1 /* return */ + (locals == null
					? 0
					: 1)).setFinished();
			}
		}
		assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
		assembly.addInstruction(OperationsA2X.XFDECLARE_D.instruction(new ModifierArgumentA30IMM(this.identifier), ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT));
	}
}
