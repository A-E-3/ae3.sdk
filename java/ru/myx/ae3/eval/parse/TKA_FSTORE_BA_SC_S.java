/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.eval.tokens.TokenAssignment;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 */
public final class TKA_FSTORE_BA_SC_S extends TokenAssignment implements ModifierArgument {
	
	private final BasePrimitiveString argumentA;

	/**
	 * @param argumentA
	 */
	public TKA_FSTORE_BA_SC_S(final BasePrimitiveString argumentA) {
		this.argumentA = argumentA;
	}

	@Override
	public final BaseObject argumentConstantValue() {
		
		return this.argumentA;
	}

	@Override
	public boolean argumentHasSideEffects() {
		
		return false;
	}

	@Override
	public final String argumentNotation() {
		
		return Ecma.toEcmaSourceCompact(this.argumentA);
	}

	@Override
	public final BaseObject argumentRead(final ExecProcess process) {
		
		return this.argumentA;
	}

	/**
	 * @return name
	 */
	public final BasePrimitiveString getBaseName() {
		
		return this.argumentA;
	}

	/**
	 * @return name
	 */
	public final String getName() {
		
		return this.argumentA.baseValue();
	}

	@Override
	public final String getNotation() {
		
		return this.argumentA + " = ";
	}

	@Override
	public final int getOperandCount() {
		
		return 1;
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * check operands
		 */
		assert argumentA != null;
		assert argumentB == null;
		/**
		 * valid store
		 */
		assert store != null;

		assembly.addInstruction(OperationsA2X.XFSTORE_D.instruction(this.argumentA, null, argumentA, 0, store));
	}

	@Override
	public final String toCode() {
		
		return "FSTORE\t2\tCS->S\tCONST('" + this.argumentA + "');";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		return new TKV_FSTORE_BA_VC_S(this.argumentA, argumentA);
	}
}
