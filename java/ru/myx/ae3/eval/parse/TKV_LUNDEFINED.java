/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/** @author myx */
final class TKV_LUNDEFINED extends TokenValue {
	
	/** @param value */
	TKV_LUNDEFINED() {
		
		//
	}
	
	@Override
	public final String getNotation() {
		
		return String.valueOf(BaseObject.UNDEFINED);
	}
	
	@Override
	public final String getNotationValue() {
		
		return this.getNotation();
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.UNDEFINED;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		if (store == ResultHandler.FB_BSN_NXT) {
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_SN_NEXT);
			return;
		}
		if (store == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_NEXT);
			return;
		}
		if (store == ResultHandler.FC_PNN_RET) {
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
			return;
		}
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(BaseObject.UNDEFINED, ModifierArguments.AC13UNDEFINED, 0, store));
	}
	
	@Override
	public void toBooleanConditionalSkip(final ProgramAssembly assembly, final boolean compare, final int constant, final ResultHandler store) {
		
		if (!compare) {
			assembly.addInstruction(OperationsA01.XESKIP_P.instruction(constant, store));
		}
	}
	
	@Override
	public InstructionEditable
			toBooleanConditionalSkip(final ProgramAssembly assembly, final int start, final boolean compare, final ResultHandler store) {
		
		if (!compare) {
			final InstructionEditable editable = OperationsA01.XESKIP_P.instructionCreate(0, store);
			assembly.addInstruction(editable);
			return editable;
		}
		return null;
	}
	
	@Override
	public final String toCode() {
		
		return "LOAD\t1\tC  ->S\tCONST(undefined);";
	}
	
	@Override
	public final ModifierArgument toConstantModifier() {
		
		return ModifierArgumentA30IMM.UNDEFINED;
	}
	
	@Override
	public final BaseObject toConstantValue() {
		
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public ModifierArgument toDirectModifier() {
		
		return ModifierArgumentA30IMM.UNDEFINED;
	}
}
