/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import java.util.Arrays;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKV_CARRAYX_BY_PUSH extends TokenValue {

	private final TokenInstruction[] tokens;

	/** @param tokens */
	public TKV_CARRAYX_BY_PUSH(final TokenInstruction[] tokens) {
		assert TokenValue.assertAllValues(tokens);
		assert tokens.length > 1;
		this.tokens = tokens;
	}

	@Override
	public final String getNotation() {

		return "[" + Arrays.asList(this.tokens) + "]";
	}

	@Override
	public InstructionResult getResultType() {

		return InstructionResult.ARRAY;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		/** flush all values to assembly */
		final int count = this.tokens.length;

		assembly.addInstruction(OperationsA10.XCARRAYX_N.instruction(ModifierArgumentA30IMM.UNDEFINED, count, ResultHandler.FB_BSN_NXT));

		/** have to keep an order, so cannot do backwards */
		for (int i = 0; i < count; ++i) {
			final TokenInstruction expression = this.tokens[i];
			final ModifierArgument directValue = expression.toDirectModifier();
			if (directValue == ModifierArguments.AA0RB) {
				expression.toExecNativeResult().toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(OperationsA3X.XASTORE_N.instruction(
					ModifierArguments.AE22PEEK, //
					ParseConstants.getValue(i).toConstantModifier(),
					directValue,
					0,
					ResultHandler.FA_BNN_NXT));
		}
		/** TODO - other than NEXT conditions could be handled on last element! */
		if (store == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NN_NEXT);
			return;
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return;
		}
		if (store == ResultHandler.FB_BNO_NXT) {
			/** TODO last element */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NO_NEXT);
			return;
		}
		if (store == ResultHandler.FB_BSO_NXT) {
			/** TODO last element */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_SO_NEXT);
			return;
		}
		/** state != StateCode.NEXT */
		if (store == ResultHandler.FC_PNN_RET) {
			/** TODO last element */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NN_RETURN);
			return;
		}
		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(ModifierArguments.AE21POP, 0, store));
		return;
	}

	@Override
	public String toCode() {

		return "ARRAY " + Arrays.asList(this.tokens) + ";";
	}

	@Override
	public final boolean toPreferStackResult() {

		return true;
	}
}
