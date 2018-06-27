/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecArgumentsEmpty;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementClass extends TokenStatementAbstract {
	
	
	private static final String EXPECTED = new String();
	
	static final List<String> toLocals(final String statement) {
		
		
		if (statement == null) {
			return Collections.emptyList();
		}
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
	
	private String identifier = null;
	
	private String extendsIdentifier = null;
	
	private TokenStatementBlock token;
	
	TokenStatementClass(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		if (this.token == null && statement instanceof TokenStatementBlock) {
			this.token = (TokenStatementBlock) statement;
			statement.setLocalsTarget(this);
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		return new TokenStatementClass(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("class");
		buffer.append('\t');
		buffer.append(this.identifier);
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "class";
	}
	
	@Override
	public final boolean isIdentifierPossible() {
		
		
		return this.identifier == null || this.extendsIdentifier == null || this.extendsIdentifier == TokenStatementClass.EXPECTED;
	}
	
	@Override
	public final boolean isIdentifierRequired() {
		
		
		return this.identifier == null || this.extendsIdentifier == TokenStatementClass.EXPECTED;
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
	public boolean isTotallyComplete() {
		
		
		return this.token != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		
		
		return false;
	}
	
	@Override
	public final boolean setIdentifier(final String identifier) {
		
		
		if (this.identifier == null) {
			this.identifier = identifier;
			return true;
		}
		if ("extends".equals(identifier)) {
			if (this.extendsIdentifier != null) {
				return false;
			}
			this.extendsIdentifier = TokenStatementClass.EXPECTED;
			return true;
		}
		if (this.extendsIdentifier == TokenStatementClass.EXPECTED) {
			this.extendsIdentifier = identifier;
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean setLocals(final BaseObject locals) {
		
		
		return true;
	}
	
	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		
		this.addDebug(assembly, "class " + this.identifier);
		if (this.identifier == null) {
			assembly.addError("class name is required!");
			return;
		}
		if (this.token == null) {
			assembly.addError("class body is required!");
			return;
		}
		final int size = assembly.size();
		final ProgramPart body;
		{
			this.addDebug(assembly, "class " + this.identifier + "{...}");
			this.token.toAssembly(assembly, size);
			body = assembly.toProgram(size);
		}
		this.parent.setLocals(new BaseNativeObject(this.identifier, BaseObject.FALSE));
		final BaseObject classInstance = new ClassInstance(this.identifier);
		final ExecProcess ctx = assembly.ctx;
		{
			ctx.vmFrameEntryExCall(true, classInstance, body, ExecArgumentsEmpty.INSTANCE, ResultHandler.FA_BNN_NXT);
			ctx.vmScopeCreateSandbox(ctx.ri10GV);
			body.execCallPreparedInilne(ctx);
		}
		assembly.addInstruction(OperationsA2X.XFDECLARE_D.instruction(new ModifierArgumentA30IMM(this.identifier), new ModifierArgumentA30IMM(classInstance), 0, ResultHandler.FA_BNN_NXT));
	}
}
