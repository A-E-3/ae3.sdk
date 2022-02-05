/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
final class TKV_COBJECT_NEW extends TokenValue {
	
	private final TokenInstruction constructedClassAccess;

	private final TokenInstruction constructorCall;

	TKV_COBJECT_NEW(final TokenInstruction constructedClassAccess, final TokenInstruction constructorCall) {
		assert constructorCall.getOperandCount() == 1 && constructorCall.getResultCount() == 1;
		this.constructedClassAccess = constructedClassAccess;
		this.constructorCall = constructorCall.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {
		
		return "new " + this.constructedClassAccess.getNotation() + "" + this.constructorCall.getNotation() + "";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
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

		final ModifierArgument constructorSourceModifier = this.constructedClassAccess.toDirectModifier();
		if (constructorSourceModifier == ModifierArguments.AA0RB) {
			this.constructedClassAccess.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(OperationsA10.XCOBJECT_N.instruction(constructorSourceModifier, 0, ResultHandler.FB_BSN_NXT));
		this.constructorCall.toAssembly(
				assembly, //
				this.constructorCall.isDirectSupported()
					? ModifierArguments.AA0RB
					: ModifierArguments.AE22PEEK,
				null,
				ResultHandler.FA_BNN_NXT);
		/**
		 * constant=1 means that BOR compares RR === undefined.
		 *
		 * NULL, FALSE, 0, "" are valid results!
		 */
		assembly.addInstruction(OperationsA2X.XEBOR_T.instruction(ModifierArguments.AA0RB, ModifierArguments.AE21POP, 1, store));
	}

	@Override
	public String toCode() {
		
		return "COBJECT_NEW [" + this.constructedClassAccess + ", " + this.constructorCall + "];";
	}
}
