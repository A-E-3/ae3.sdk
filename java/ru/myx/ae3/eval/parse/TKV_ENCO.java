/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.ResultHandlerDirect;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKV_ENCO extends TokenValue {
	
	private TokenInstruction tokenLeft;
	
	private TokenInstruction tokenRight;
	
	/** @param tokenLeft
	 * @param tokenRight */
	public TKV_ENCO(final TokenInstruction tokenLeft, final TokenInstruction tokenRight) {
		
		assert tokenLeft.assertStackValue();
		assert tokenRight.assertStackValue();
		this.tokenLeft = tokenLeft;
		this.tokenRight = tokenRight;
		/** DEBUG <code>
		System.out.println( ">>> >>>   tokenLeft: "
				+ this.tokenLeft.getNotation()
				+ ", tokenRight: "
				+ this.tokenRight.getNotation() );
		</code> */
	}
	
	@Override
	public final String getNotation() {
		
		return "" + this.tokenLeft.getNotation() + " ?? " + this.tokenRight.getNotation() + "";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return this.tokenLeft.getResultType().merge(this.tokenRight.getResultType());
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		final ModifierArgument modifierRight = this.tokenRight.toDirectModifier();
		if (modifierRight != ModifierArguments.AA0RB && !modifierRight.argumentHasSideEffects()) {
			final ModifierArgument modifierLeft = this.tokenLeft.toDirectModifier();
			if (modifierLeft == ModifierArguments.AA0RB) {
				this.tokenLeft.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(OperationsA2X.XENCO_T.instruction(modifierLeft, modifierRight, 0, store));
			return;
		}
		
		if (store == ResultHandler.FA_BNN_NXT || store == ResultHandler.FU_BNN_NXT) {
			final InstructionEditable skip = this.tokenLeft.toConditionalSkipEditable(//
					assembly,
					-1,
					TokenInstruction.ConditionType.NULLISH_NOT,
					store//
			);
			// never skips
			if (skip == null) {
				this.tokenRight.toAssembly(assembly, null, null, store);
				return;
			}
			final int rightStart = assembly.size();
			this.tokenRight.toAssembly(assembly, null, null, store);
			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			return;
		}
		
		if (store instanceof ResultHandlerBasic.ExecutionContinue) {
			final ResultHandlerDirect direct = store.execDirectTransportType().handlerForStoreNext();
			
			this.tokenLeft.toAssembly(assembly, null, null, direct);
			
			final ResultHandlerBasic onlyEffects = ((ResultHandlerBasic.ExecutionContinue) store).replaceEffectsOnly();
			if (onlyEffects != null) {
				assembly.addInstruction(
						direct == ResultHandler.FA_BNN_NXT
							? OperationsA01.XNSKIPRB0_P.instruction(0, onlyEffects)
							: OperationsA11.XNSKIP0A_P.instruction(ModifierArgument.forStore(direct), 0, onlyEffects));
			}
			
			final InstructionEditable skip = direct == ResultHandler.FA_BNN_NXT
				? OperationsA01.XNSKIPRB1_P.instructionCreate(0, ResultHandler.FA_BNN_NXT)
				: OperationsA11.XNSKIP1A_P.instructionCreate(ModifierArgument.forStore(direct), 0, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(skip);
			final int rightStart = assembly.size();
			this.tokenRight.toAssembly(assembly, null, null, store);
			skip.setConstant(assembly.getInstructionCount(rightStart)).setFinished();
			return;
		}
		
		this.tokenLeft.toConditionalSkipSingleton(assembly, TokenInstruction.ConditionType.NULLISH_YES, 0, store);
		this.tokenRight.toAssembly(assembly, null, null, store);
	}
	
	@Override
	public String toCode() {
		
		return "NCO [" + this.tokenLeft + ", " + this.tokenRight + "];";
	}
	
	@Override
	public TokenInstruction toExecDetachableResult() {
		
		this.tokenLeft = this.tokenLeft.toExecDetachableResult();
		this.tokenRight = this.tokenRight.toExecDetachableResult();
		return this;
	}
	
	@Override
	public TokenInstruction toExecNativeResult() {
		
		this.tokenLeft = this.tokenLeft.toExecNativeResult();
		this.tokenRight = this.tokenRight.toExecNativeResult();
		return this;
	}
}
