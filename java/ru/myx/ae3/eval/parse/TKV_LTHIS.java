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

final class TKV_LTHIS extends TokenValue {

	@Override
	public final String getNotation() {

		return "this";
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
			assembly.addInstruction(Instructions.INSTR_LOAD_1_T_SN_NEXT);
			return;
		}

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(ModifierArguments.AB4CT, 0, store));
	}

	@Override
	public final String toCode() {

		return "LOAD\t1\tT  ->S;";
	}

	@Override
	public InstructionEditable toConditionalSkipEditable(final ProgramAssembly assembly, final int start, final TokenInstruction.ConditionType compare, final ResultHandler store) {

		final InstructionEditable editable = compare.createEditable(ModifierArguments.AB4CT, store);
		assembly.addInstruction(editable);
		return editable;
	}

	@Override
	public void toConditionalSkipSingleton(final ProgramAssembly assembly, final TokenInstruction.ConditionType compare, final int constant, final ResultHandler store) {

		assembly.addInstruction(compare.createSingleton(ModifierArguments.AB4CT, constant, store));
	}

	@Override
	public ModifierArgument toDirectModifier() {

		return ModifierArguments.AB4CT;
	}
}
