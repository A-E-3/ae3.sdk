/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_AWAIT_A_S_S extends TokenOperator {
	
	private static final ModifierArgument MODIFIER_FN_AWAIT = ModifierArgumentFunctionAwait.INSTANCE;
	
	private static final Instruction INSTR_AWAIT_A_R_S;
	
	private static final Instruction INSTR_AWAIT_A_R_V;
	
	private static final Instruction INSTR_AWAIT_A_S_S;
	
	private static final Instruction INSTR_AWAIT_A_S_V;
	
	static {
		INSTR_AWAIT_A_R_S = //
				OperationsA2X.XFCALLO.instruction(TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
		
		INSTR_AWAIT_A_R_V = //
				OperationsA2X.XFCALLO.instruction(TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
		
		INSTR_AWAIT_A_S_S = //
				OperationsA2X.XFCALLO.instruction(TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
		
		INSTR_AWAIT_A_S_V = //
				OperationsA2X.XFCALLO.instruction(TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
		
	}
	
	@Override
	public final String getNotation() {
		
		return "await";
	}
	
	@Override
	public final String getNotationValue() {
		
		return "await ";
	}
	
	@Override
	public final int getOperandCount() {
		
		return 1;
	}
	
	@Override
	public final int getPriorityLeft() {
		
		return +10000;
	}
	
	@Override
	public final int getPriorityRight() {
		
		return 500;
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public final boolean isConstantForArguments() {
		
		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** check operands */
		assert argumentA != null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(TKO_AWAIT_A_S_S.INSTR_AWAIT_A_R_V);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_AWAIT_A_S_S.INSTR_AWAIT_A_S_V);
				return;
			}
			assembly.addInstruction(
					OperationsA2X.XFCALLO.instruction(
							TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, //
							argumentA,
							0,
							store));
			return;
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			if (argumentA == ModifierArguments.AA0RB) {
				assembly.addInstruction(TKO_AWAIT_A_S_S.INSTR_AWAIT_A_R_S);
				return;
			}
			if (argumentA == ModifierArguments.AE21POP) {
				assembly.addInstruction(TKO_AWAIT_A_S_S.INSTR_AWAIT_A_S_S);
				return;
			}
		}
		
		assembly.addInstruction(OperationsA2X.XFCALLO.instruction(TKO_AWAIT_A_S_S.MODIFIER_FN_AWAIT, argumentA, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return "AWAIT\t1\tS  ->S;";
	}
}
