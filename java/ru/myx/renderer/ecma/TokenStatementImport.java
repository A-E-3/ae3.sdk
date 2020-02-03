/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;

final class TokenStatementImport extends TokenStatementAbstract {

	private static final ModifierArgument MA_IMPORT = ModifierArgumentFunctionImport.INSTANCE;

	private String className;

	TokenStatementImport(final String identity, final int line) {

		super(identity, line);
	}

	@Override
	public final boolean addStatement(final TokenStatement statement) {

		if (this.className == null && statement instanceof TokenStatementSingle) {
			this.className = ((TokenStatementSingle) statement).toString().trim();
			return true;
		}
		return false;
	}

	@Override
	public final TokenStatement createStatement(final String identity, final int line) {

		return new TokenStatementImport(identity, line);
	}

	@Override
	public final void dump(final int level, final StringBuilder buffer) {

		for (int i = level; i > 0; --i) {
			buffer.append('\t');
		}
		buffer.append("import");
		if (this.className != null) {
			buffer.append(' ').append(this.className);
		}
	}

	@Override
	public final String getKeyword() {

		return "import";
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

		return false;
	}

	@Override
	public final boolean isLabelStatement() {

		return false;
	}

	@Override
	public final boolean isNewLineSemicolon() {

		return this.className == null;
	}

	@Override
	public boolean isTotallyComplete() {

		return this.className != null;
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

		throw new UnsupportedOperationException("No locals allowed here!");
	}

	@Override
	public final void toAssembly(final ProgramAssembly assembly, final int startOffset) throws Exception {

		if (this.className == null) {
			this.addDebug(assembly, "import");
			assembly.addError("No class or type name was specified!");
		} else {
			final String expression = this.className;
			final String identifier = expression.substring(expression.lastIndexOf('.') + 1);
			this.parent.setLocals(new BaseNativeObject(identifier, BaseObject.FALSE));
			this.addDebug(assembly, "import " + expression);
			assembly.addInstruction(
					OperationsA2X.XFCALLO.instruction(
							TokenStatementImport.MA_IMPORT,
							new ModifierArgumentA30IMM(
									/** 'null' callee is allowed here - java call */
									Base.forString(expression)),
							0,
							ResultHandler.FA_BNN_NXT));
		}
	}

	@Override
	public final String toString() {

		return this.className == null
			? "import"
			: "import " + this.className;
	}
}
