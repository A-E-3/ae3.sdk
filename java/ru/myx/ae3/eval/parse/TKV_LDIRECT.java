/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/** @author myx */
final class TKV_LDIRECT extends TokenValue {
	
	/** @param value */
	TKV_LDIRECT() {
		
		//
	}
	
	@Override
	public final String getNotation() {
		
		return "";
	}
	
	@Override
	public final String getNotationValue() {
		
		return this.getNotation();
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		if (store == ResultHandler.FB_BSN_NXT) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_R_SN_NEXT);
			return;
		}
		if (store == ResultHandler.FA_BNN_NXT) {
			/** NO CODE NEEDED **/
			// assembly.addInstruction(Instructions.INSTR_LOAD_1_R_NN_NEXT);
			return;
		}
		if (store == ResultHandler.FC_PNN_RET) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_R_NN_RETURN);
			return;
		}
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(ModifierArguments.AA0RB, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return "LOAD\t1\t" + ModifierArguments.AA0RB + "  ->S\t;";
	}
	
	@Override
	public InstructionEditable toConditionalSkipEditable(final ProgramAssembly assembly, final int start, final TokenInstruction.ConditionType compare, final ResultHandler store) {
		
		final InstructionEditable editable = compare.createEditable(ModifierArguments.AA0RB, store);
		assembly.addInstruction(editable);
		return editable;
	}
	
	@Override
	public void toConditionalSkipSingleton(final ProgramAssembly assembly, final TokenInstruction.ConditionType compare, final int constant, final ResultHandler store) {
		
		assembly.addInstruction(compare.createSingleton(ModifierArguments.AA0RB, constant, store));
	}

	@Override
	public ModifierArgument toDirectModifier() {
		
		return ModifierArguments.AA0RB;
	}
}
