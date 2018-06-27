/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class TKV_ASSIGN extends TokenValue {

	private final TokenInstruction reference;

	private final TokenInstruction value;

	/**
	 * @param reference
	 * @param value
	 */
	public TKV_ASSIGN(final TokenInstruction reference, final TokenInstruction value) {
		assert reference.assertStackValue();
		assert reference.assertAccessReference();
		assert value.assertStackValue();
		this.reference = reference;
		this.value = value.toExecDetachableResult();
	}

	@Override
	public final String getNotation() {

		return "(" + this.reference.getNotation() + " = " + this.value.getNotation() + ")";
	}

	@Override
	public final InstructionResult getResultType() {

		return this.value.getResultType();
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/**
		 * zero operands (one operand is already embedded in this token)
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final ModifierArgument modifierReference = this.reference.toReferenceReadBeforeWrite(assembly, null, null, false, true);

		assert modifierReference == null;
		// assert modifierReference != ModifierArguments.A21POP;

		final ModifierArgument modifierValue = this.value.toDirectModifier();
		if (modifierValue == ModifierArguments.AA0RB) {
			this.value.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		this.reference.toReferenceWriteAfterRead(assembly, null, null, modifierValue, store);
	}

	@Override
	public String toCode() {

		return "ASSIGN [" + this.reference + " = " + this.value + "];";
	}
}
