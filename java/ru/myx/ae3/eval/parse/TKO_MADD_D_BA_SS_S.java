/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_MADD_D_BA_SS_S extends TokenOperator {
	
	public static final TokenOperator INSTANCE = new TKO_MADD_D_BA_SS_S();

	private TKO_MADD_D_BA_SS_S() {
		//
	}

	@Override
	public final String getNotation() {
		
		return "+";
	}

	@Override
	public final int getOperandCount() {
		
		return 2;
	}

	@Override
	public final int getPriorityLeft() {
		
		return 300;
	}

	@Override
	public final int getPriorityRight() {
		
		return 300;
	}

	@Override
	public final InstructionResult getResultType() {
		
		/**
		 * STRING or NUMBER
		 */
		return InstructionResult.OBJECT;
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

		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentA == ModifierArguments.AE21POP) {
				if (argumentA == argumentB) {
					assembly.addInstruction(Instructions.INSTR_MADD_D_BA_SS_V);
					return;
				}
				if (argumentB == ModifierArguments.AA0RB) {
					assembly.addInstruction(Instructions.INSTR_MADD_D_BA_RS_V);
					return;
				}
			}
		} else //
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == argumentB && argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(Instructions.INSTR_MADD_D_BA_SS_S);
				return;
			}
		}

		assembly.addInstruction(OperationsS2X.VMADD_D.instruction(argumentA, argumentB, 0, store));
	}

	@Override
	public final String toCode() {
		
		return OperationsS2X.VMADD_D + "\t2\tSS ->S;";
	}

	@Override
	public final TokenInstruction toExecNativeResult() {
		
		return TKO_MADD_N_BA_SS_S.INSTANCE;
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly) {
		
		final BaseObject constantA = argumentA.toConstantValue();
		if (constantA == BaseString.EMPTY) {
			final BaseObject constantB = argumentB.toConstantValue();
			if (constantB != null) {
				return constantB.baseIsPrimitiveString()
					? argumentB.toExecDetachableResult()
					: ParseConstants.getConstantValue(constantB.baseToString());
			}
			return argumentB.getResultType() == InstructionResult.STRING
				? argumentB.toExecDetachableResult()
				: new TKV_ZCVTS(argumentB, 1);
		}
		final BaseObject constantB = argumentB.toConstantValue();
		if (constantB == BaseString.EMPTY) {
			if (constantA != null) {
				return constantA.baseIsPrimitiveString()
					? argumentA.toExecDetachableResult()
					: ParseConstants.getConstantValue(constantA.baseToString());
			}
			return argumentA.getResultType() == InstructionResult.STRING
				? argumentA.toExecDetachableResult()
				: new TKV_ZCVTS(argumentA, 1);
		}
		if (constantA != null) {
			if (sideEffectsOnly && constantA.baseIsPrimitive()) {
				/**
				 * known to be not a constant already
				 */
				return argumentB.toExecDetachableResult();
			}
			if (constantB != null) {
				return super.toStackValue(assembly, argumentA, argumentB, sideEffectsOnly);
			}
		}
		switch (argumentA.getResultType()) {
			case STRING :
				return new TKV_MADDS_BA_VV_S(argumentA, argumentB, 1);
			case BOOLEAN :
			case INTEGER :
			case NUMBER :
				switch (argumentB.getResultType()) {
					case STRING :
						return new TKV_MADDS_BA_VV_S(argumentA, argumentB, 1);
					case BOOLEAN :
					case INTEGER :
					case NUMBER :
						return new TKV_MADDN_BA_VV_S(argumentA, argumentB, 1);
					default :
						return super.toStackValue(assembly, argumentA, argumentB, sideEffectsOnly);
				}
			default :
				switch (argumentB.getResultType()) {
					case STRING :
						return new TKV_MADDS_BA_VV_S(argumentA, argumentB, 1);
					default :
						return super.toStackValue(assembly, argumentA, argumentB, sideEffectsOnly);
				}
		}
	}
}
