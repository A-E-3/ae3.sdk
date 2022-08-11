/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.vm_vliw32_2010.OperationA2X;

final class TKV_FCALLM_BA_AV_S extends TokenValue implements TokenValue.SyntacticallyFrameAccess {
	
	private final TokenInstruction argumentA;
	
	private final TokenInstruction argument;
	
	private final Instruction carguments;
	
	TKV_FCALLM_BA_AV_S(final TokenInstruction argumentA, final TokenInstruction argument, final Instruction carguments) {
		
		/** function itself */
		assert argumentA.assertStackValue();
		/** not really a value, could have more than one result */
		assert argument.assertZeroStackOperands();
		/** CARGUMENTS should consume them all */
		assert argument.getResultCount() == carguments.getOperandCount();
		/**
		 *
		 */
		this.argumentA = argumentA;
		this.argument = argument;
		this.carguments = carguments;
	}
	
	@Override
	public TokenValue getDirectChainingAccessReplacement() {
		
		/** Why do we need this, aint it the same? **/
		if (this.argumentA instanceof final TokenValue access) {
			final BasePrimitiveString contextPropertyName = access.toContextPropertyName();
			if (null != contextPropertyName) {
				return new TKV_ACALLM_CBA_AVV_S(//
						ParseConstants.TKV_DIRECT,
						ParseConstants.getConstantValue(contextPropertyName),
						this.argument,
						this.carguments//
				);
			}
		}
		return new TKV_ACALLM_CBA_AVV_S(//
				ParseConstants.TKV_DIRECT,
				this.argumentA,
				this.argument,
				this.carguments//
		);
	}
	
	@Override
	public final String getNotation() {
		
		return this.argumentA.getNotation() + "( " + this.argument.getNotation() + " )";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands (both operands are already embedded in this token) */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		
		this.argument.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		assembly.addInstruction(this.carguments);
		assembly.addInstruction(
				((OperationA2X) OperationsA2X.XFCALLM).instruction(
						directA
							? ModifierArguments.AE21POP
							: modifierA,
						ModifierArguments.AA0RB,
						0,
						store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA2X.XFCALLM + "\t0\tAV->S;";
	}
}
