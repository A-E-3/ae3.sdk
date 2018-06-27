/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementBlock extends TokenStatementAbstract {
	
	private TokenStatement localsTarget;

	private BaseObject locals;

	private Object statements;

	TokenStatementBlock(final String identity, final int line) {
		super(identity, line);
	}

	@Override
	public final boolean addStatement(final TokenStatement statement) {
		
		if (statement.isEmpty()) {
			/** TODO remove 'if' (with no regression: this one is saying EMPTY in initial state but
			 * the state changes then (when it's too late)) */
			if (!(statement instanceof TokenStatementBlock)) {
				return true;
			}
		}
		if (this.statements == null) {
			this.statements = statement;
			return true;
		}
		if (this.statements instanceof TokenStatement) {
			this.statements = new ListStatements((TokenStatement) this.statements, statement);
			return true;
		}
		((ListStatements) this.statements).add(statement);
		return true;
	}

	@Override
	public final TokenStatement createStatement(final String identity, final int line) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {
		
		if (this.statements == null) {
			return;
		}
		if (this.statements instanceof TokenStatement) {
			((TokenStatement) this.statements).dump(level + 1, buffer);
			return;
		}
		for (final TokenStatement statement : (ListStatements) this.statements) {
			statement.dump(level + 1, buffer);
		}
	}

	@Override
	public final String getKeyword() {
		
		return null;
	}

	@Override
	public boolean isBlockStatement() {
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		
		/** FIXME: should ask after statement filled, otherwise skips all block statements. */
		// return this.statements == null;
		return false;
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
	public boolean isNextStatementFromScratch() {
		
		return true;
	}

	@Override
	public boolean isTotallyComplete() {
		
		return false;
	}

	@Override
	public final TokenStatement lastStatement() {
		
		if (this.statements == null) {
			return null;
		}
		if (this.statements instanceof TokenStatement) {
			return (TokenStatement) this.statements;
		}
		return ((ListStatements) this.statements).getLast();
	}

	@Override
	public final boolean setArguments(final String expression) {
		
		return false;
	}

	@Override
	public final boolean setIdentifier(final String identifier) {
		
		return false;
	}

	@Override
	public final boolean setLocals(final BaseObject locals) {
		
		if (this.localsTarget == null) {
			if (locals == null || !locals.baseHasKeysOwn()) {
				return true;
			}
			if (this.locals == null) {
				this.locals = new BaseNativeObject();
			}
			this.locals.baseDefineImportOwnEnumerable(locals);
			return true;
		}
		return this.localsTarget.setLocals(locals);
	}

	@Override
	public final void setLocalsTarget(final TokenStatement target) {
		
		this.localsTarget = target;
		if (this.locals != null) {
			target.setLocals(this.locals);
			this.locals = null;
		}
	}

	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {
		
		final int size = assembly.size();
		if (this.statements != null) {
			if (this.statements instanceof TokenStatement) {
				((TokenStatement) this.statements).toAssembly(assembly, size);
			} else {
				for (final TokenStatement statement : (ListStatements) this.statements) {
					statement.toAssembly(assembly, size);
				}
			}
		}
		if (assembly.size() == size || this.locals == null) {
			/** block has no actual code or there are no locals for context */
			return;
		}
		final ProgramPart block = assembly.toProgram(size);
		this.addDebug(assembly, "{");
		{
			final BaseObject locals = TokenStatementAbstract.toLocalsObjectOrArray(this.locals);
			final int blockSize = block.length()/* end debug: + 1 */ + (locals != null
				? 1
				: 0);
			assembly.addInstruction(OperationsA01.XEENTRVARS_P.instruction(blockSize, ResultHandler.FA_BNN_NXT));

			if (locals != null) {
				assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(locals, null, 0, ResultHandler.FA_BNN_NXT));
			}

			assembly.addInstruction(block);
			// this.addDebug(assembly, "}");
			assembly.addInstruction(Instructions.INSTR_ELEAVE_0_NN_NEXT);
		}
	}

	final TokenStatement[] toStatements() {
		
		if (this.statements == null) {
			return null;
		}
		if (this.statements instanceof TokenStatement) {
			return new TokenStatement[]{
					(TokenStatement) this.statements
			};
		}
		final ListStatements list = (ListStatements) this.statements;
		return list.toArray(new TokenStatement[list.size()]);
	}
}
