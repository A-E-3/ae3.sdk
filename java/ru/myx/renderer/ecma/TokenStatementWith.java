/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;

final class TokenStatementWith extends TokenStatementAbstract {
	
	
	private String expression;
	
	private BaseMap locals;
	
	private TokenStatement token;
	
	TokenStatementWith(final String identity, final int line) {
		super(identity, line);
	}
	
	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		
		if (this.token == null) {
			this.token = statement;
			statement.setLocalsTarget(this);
			return true;
		}
		return false;
	}
	
	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		
		return new TokenStatementWith(identity, line);
	}
	
	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		
		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("with");
		buffer.append('\t');
		buffer.append('(');
		buffer.append(this.expression);
		buffer.append(')');
		buffer.append('\n');
		if (this.token != null) {
			this.token.dump(level + 1, buffer);
		}
	}
	
	@Override
	public final String getKeyword() {
		
		
		return "with";
	}
	
	@Override
	public final boolean isIdentifierPossible() {
		
		
		return false;
	}
	
	@Override
	public final boolean isIdentifierRequired() {
		
		
		return false;
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
		
		
		return this.token != null && this.expression != null;
	}
	
	@Override
	public final boolean setArguments(final String expression) {
		
		
		if (this.expression == null) {
			this.expression = expression;
			return true;
		}
		return false;
	}
	
	@Override
	public final boolean setIdentifier(final String identifier) {
		
		
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
		
		
		if (this.expression == null) {
			this.addDebug(assembly, "with()");
			assembly.addError("object reference required for with statement!");
			return;
		}
		this.addDebug(assembly, "with(" + this.expression + ")");
		if (this.token == null) {
			assembly.addError("illegal with statement!");
			return;
		}
		final ProgramPart body;
		{
			final int size = assembly.size();
			this.token.toAssembly(assembly, size);
			body = assembly.toProgram(size);
		}
		final InstructionEditable frameEntry = OperationsA01.XEENTRFULL_P.instructionCreate(0, ResultHandler.FA_BNN_NXT);
		assembly.addInstruction(frameEntry);
		final int framePosition = assembly.size();
		// Evaluate.compileToken( assembly, this.expression,
		// ModifierStore.NN, ExecStateCode.NEXT );
		Evaluate.compileExpression(assembly, this.expression, ResultHandler.FA_BNN_NXT);
		assembly.addInstruction(OperationsA10.XCSCOPE_N.instruction(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT));
		if (this.locals != null) {
			final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
			if (locals != null) {
				assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(new ModifierArgumentA30IMM(locals), 0, ResultHandler.FA_BNN_NXT));
			}
		}
		assembly.addInstruction(body);
		frameEntry.setConstant(assembly.getInstructionCount(framePosition)).setFinished();
		assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
	}
}
