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
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/**
 * @author myx
 *
 */
public final class TKV_FDECLARE_A_Cs_S extends TokenValue implements ModifierArgument {
	
	private final BasePrimitiveString argumentB;

	/**
	 * @param name
	 */
	public TKV_FDECLARE_A_Cs_S(final BasePrimitiveString name) {
		this.argumentB = name;
	}

	@Override
	public final BaseObject argumentConstantValue() {
		
		return this.argumentB;
	}

	@Override
	public boolean argumentHasSideEffects() {
		
		return false;
	}

	@Override
	public final String argumentNotation() {
		
		return Ecma.toEcmaSourceCompact(this.argumentB);
	}

	@Override
	public final BaseObject argumentRead(final ExecProcess process) {
		
		return this.argumentB;
	}

	@Override
	public final String getNotation() {
		
		return this.argumentB.baseValue();
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.UNDEFINED;
	}

	@Override
	public final boolean isAccessReference() {
		
		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		if (store == ResultHandler.FA_BNN_NXT) {
			assembly.addTokenInstruction(this);
			return;
		}

		assembly.addInstruction(OperationsA10.XFDECLARE_N.instruction(this.argumentB, this, 0, store));
	}

	@Override
	public ExecStateCode execCall(final ExecProcess ctx) {
		
		ctx.contextCreateMutableBinding(//
				this.argumentB,
				// assignment!
				ctx.ra0RB = BaseObject.UNDEFINED,
				false//
		);
		return null;
	}

	@Override
	public final String toCode() {
		
		return "FDECLARE\t1\tC ->S\tCONST('" + this.argumentB + "');";
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {
		
		if (!needRead) {
			return null;
		}
		assembly.addError("Declaration cannot be read!");
		return ModifierArguments.AA0RB;
	}

	@Override
	public void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final ResultHandler store) {
		
		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;
		/**
		 * valid store
		 */
		assert store != null;

		assembly.addInstruction(OperationsS2X.VFDECLARE_N.instruction(
				this, //
				modifierValue,
				0,
				store));
	}

}
