/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_BIN_A_S_S extends TokenOperator {
	
	@Override
	public final String getNotation() {

		return "in";
	}

	@Override
	public final String getNotationValue() {

		return " in ";
	}

	@Override
	public final int getOperandCount() {

		return 2;
	}

	@Override
	public final int getPriorityLeft() {

		return 260;
	}

	@Override
	public final int getPriorityRight() {

		return 260;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.BOOLEAN;
	}

	@Override
	public final boolean isConstantForArguments() {

		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/**
		 * check operands
		 */
		assert argumentA != null;
		assert argumentB != null;

		/**
		 * valid store
		 */
		assert store != null;

		/**
		 * TODO <code>
		if (state == ExecStateCode.NEXT) {
			if (store == ModifierStore.NN) {
				if (argumentA == ModifierArguments.A07RR) {
					assembly.addInstruction( start, Instructions.INSTR_IN_A_R_V );
					return;
				}
				if (argumentA == ModifierArguments.A10POP) {
					assembly.addInstruction( start, Instructions.INSTR_IN_A_S_V );
					return;
				}
			} else //
			if (store == ModifierStore.SN) {
				if (argumentA == ModifierArguments.A07RR) {
					assembly.addInstruction( start, Instructions.INSTR_IN_A_R_S );
					return;
				}
				if (argumentA == ModifierArguments.A10POP) {
					assembly.addInstruction( start, Instructions.INSTR_IN_A_S_S );
					return;
				}
			}
		}
		 * </code>
		 */
		assembly.addInstruction(OperationsA2X.XBIN.instruction(argumentA, argumentB, 0, store));
	}

	@Override
	public final String toCode() {

		return "BIN\t1\tS  ->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {

		final BaseObject value = argumentA.toConstantValue();
		if (value != null) {
			/**
			 * FIXME: ecmaTypeOf is not the same as BaseObject.baseClass()
			 */
			return ParseConstants.getConstantValue(Base.forString(Ecma.ecmaTypeOf(value)));
		}
		return super.toStackValue(assembly, argumentA, sideEffectsOnly);
	}
}
