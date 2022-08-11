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
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_FCALLS_BA_AV_S extends TokenValue implements TokenValue.SyntacticallyFrameAccess {
	
	private final TokenInstruction argumentA;
	
	private final TokenInstruction argument;
	
	private final int constant;
	
	TKV_FCALLS_BA_AV_S(final TokenInstruction argumentA, final TokenInstruction argument, final int constant) {
		
		assert argumentA.assertStackValue();
		assert argument.assertZeroStackOperands();
		assert argument.getResultCount() == constant;
		assert constant > 1 : constant == 0
			? "Use " + TKV_FCALLV_A_V_S.class.getSimpleName() + " then"
			: "Use " + TKV_FCALLO_BA_AV_S.class.getSimpleName() + " then";
		this.argumentA = argumentA;
		this.argument = argument;
		this.constant = constant;
	}
	
	@Override
	public TokenValue getDirectChainingAccessReplacement() {
		
		/** Why do we need this, aint it the same? **/
		if (this.argumentA instanceof final TokenValue access) {
			final BasePrimitiveString contextPropertyName = access.toContextPropertyName();
			if (null != contextPropertyName) {
				return new TKV_ACALLS_CBA_AVV_S(//
						ParseConstants.TKV_DIRECT,
						ParseConstants.getConstantValue(contextPropertyName),
						this.argument,
						this.constant//
				);
			}
		}
		return new TKV_ACALLS_CBA_AVV_S(//
				ParseConstants.TKV_DIRECT,
				this.argumentA,
				this.argument,
				this.constant//
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
		
		/** Anyway in stack, constant is expected to be more than zero */
		this.argument.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		
		final ModifierArgument modifierB = this.argumentA.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.argumentA.toAssembly(
					assembly, //
					null,
					null,
					ResultHandler.FA_BNN_NXT);
		}
		
		assembly.addInstruction(
				OperationsA10.XFCALLS//
						.instruction(modifierB, this.constant, store));
	}
	
	@Override
	public final String toCode() {
		
		return OperationsA10.XFCALLS + "\t0" + this.constant + "\tAV->S;";
	}
}
