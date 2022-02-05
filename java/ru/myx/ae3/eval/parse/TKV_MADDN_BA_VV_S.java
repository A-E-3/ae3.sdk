/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.vm_vliw32_2010.OperationA2X;

final class TKV_MADDN_BA_VV_S extends TokenValue {

	private final TokenInstruction argumentA;
	
	private final TokenInstruction argumentB;
	
	private int visibility;
	
	/** @param argumentA
	 * @param argumentB
	 * @param detachable */
	public TKV_MADDN_BA_VV_S(final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean detachable) {
		
		assert argumentA.isStackValue();
		assert argumentB.isStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB;
		this.visibility = detachable
			? 1
			: 0;
	}
	
	/** @param argumentA
	 * @param argumentB
	 * @param visibility */
	public TKV_MADDN_BA_VV_S(final TokenInstruction argumentA, final TokenInstruction argumentB, final int visibility) {
		
		assert argumentA.isStackValue();
		assert argumentB.isStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB;
		this.visibility = visibility;
	}
	
	@Override
	public final String getNotation() {

		return this.argumentA.getNotation() + "+" + this.argumentB.getNotation();
	}
	
	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.NUMBER;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(
					assembly,
					null,
					null,
					directB
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		if (directB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				((OperationA2X) (this.visibility == 2
					? OperationsS2X.VMADDN_N
					: this.visibility == 1
						? OperationsS2X.VMADDN_D
						: OperationsS2X.VMADDN_T)).instruction(
								directA && directB
									? ModifierArguments.AE21POP
									: modifierA,
								modifierB,
								0,
								store));
	}
	
	@Override
	public final String toCode() {

		return (this.visibility == 2
			? OperationsS2X.VMADDN_N
			: this.visibility == 1
				? OperationsS2X.VMADDN_D
				: OperationsS2X.VMADDN_T) + "(N)\t2\tVV ->S\t[" + this.argumentA + " " + this.argumentB + "];";
	}
	
	@Override
	public TokenInstruction toExecDetachableResult() {

		this.visibility = 1;
		return this;
	}
	
	@Override
	public TokenInstruction toExecNativeResult() {

		this.visibility = 2;
		return this;
	}
}
