/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_ASSIGN1 extends TokenValue {
	
	private final TokenInstruction reference;

	private final TokenInstruction operation;

	private final TokenInstruction value;

	/**
	 * @param operand
	 * @param operation
	 * @param value
	 */
	public TKV_ASSIGN1(final TokenInstruction operand, final TokenInstruction operation, final TokenInstruction value) {
		assert operand.assertStackValue();
		assert operation.getTokenType() == TokenType.ASSIGNMENT;
		assert operation.getOperandCount() == 2 : "NO WRAP_IMPL_ASSIGNMENT NEEDED THEN";
		assert operation.getResultCount() == 1;
		assert value.assertStackValue();
		this.reference = operand;
		this.operation = operation.toExecDetachableResult();
		this.value = value;
	}

	@Override
	public final String getNotation() {
		
		return "(" + this.reference.getNotation() + " " + this.operation.getNotation() + " " + this.value.getNotation() + " /* ASSIGN1C, class="
				+ this.operation.getClass().getName() + " */)";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return this.value.getResultType();
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands (two operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;
		/**
		 * valid store
		 */
		assert store != null;

		final boolean directSupport = this.operation.isDirectSupported();
		final ModifierArgument modifierValue = this.value.toDirectModifier();
		final boolean directValue = modifierValue == ModifierArguments.AA0RB;
		final ModifierArgument modifierReferenced = this.reference.toReferenceReadBeforeWrite(assembly, argumentA, argumentB, true, directSupport && !directValue);
		if (directValue) {
			this.value.toAssembly(assembly, null, null, directSupport
				? ResultHandler.FA_BNN_NXT
				: ResultHandler.FB_BSN_NXT);
		}
		this.operation.toAssembly(
				assembly, //
				modifierReferenced,
				directValue && !directSupport
					? ModifierArguments.AE21POP
					: modifierValue,
				ResultHandler.FA_BNN_NXT);
		this.reference.toReferenceWriteAfterRead(assembly, argumentA, argumentB, ModifierArguments.AA0RB, store);
	}

	@Override
	public String toCode() {
		
		return "ASSIGN1C [" + this.value + ", " + this.operation + "];";
	}
}
