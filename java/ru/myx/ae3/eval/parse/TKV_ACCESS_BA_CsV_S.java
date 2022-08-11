/*
 * Created on 09.03.2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA00;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.OperationsS3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.help.Format;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKV_ACCESS_BA_CsV_S extends TokenValue {

	private final TokenInstruction argumentA;

	private final BasePrimitiveString argumentB;

	private int visibility = 0;

	/** @param argumentA
	 * @param argumentB */
	public TKV_ACCESS_BA_CsV_S(final TokenInstruction argumentA, final BasePrimitiveString argumentB) {

		assert argumentA.isStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB;
	}

	@Override
	public final String getNotation() {

		return this.argumentA.getNotationValue() + "[" + Format.Ecma.string(this.argumentB) + "]";
	}

	@Override
	public final String getNotationValue() {

		return this.argumentA.getNotationValue() + "[" + Format.Ecma.string(this.argumentB) + "]";
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public final boolean isAccessReference() {

		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = new TKV_LCONSTS(this.argumentB);
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = false;
		if (directA) {
			this.argumentA.toAssembly(
					assembly,
					null,
					null,
					directB
						? ResultHandler.FB_BSN_NXT
						: ResultHandler.FA_BNN_NXT);
		}
		assembly.addInstruction(
				(this.visibility == 2
					? OperationsS2X.VACCESS_NS
					: OperationsS2X.VACCESS_DS//
				)//
						.instruction(
								modifierA, //
								modifierB,
								0,
								store//
						)//
		);
	}

	@Override
	public final String toCode() {

		return "ACCESS\t0\tVC ->S\t[" + this.argumentA + ", " + this.argumentB + "];";
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

	@Override
	public TokenInstruction toReferenceDelete() {

		return new TKV_DELETE_BA_VV_S(this.argumentA, ParseConstants.getConstantValue(this.argumentB));
	}

	@Override
	public TokenInstruction toReferenceObject() {

		return this.argumentA;
	}

	@Override
	public TokenInstruction toReferenceProperty() {

		return ParseConstants.getConstantValue(this.argumentB);
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {

		assert argumentA == null;
		assert argumentB == null;
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = new TKV_LCONSTS(this.argumentB);
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		if (needRead) {
			assembly.addInstruction(
					(this.visibility == 2
						? OperationsS2X.VACCESS_NS
						: OperationsS2X.VACCESS_DS)//
								.instruction(
										directA
											? ModifierArguments.AE22PEEK
											: modifierA, //
										modifierB,
										0,
										directAllowed
											? ResultHandler.FA_BNN_NXT
											: ResultHandler.FB_BSN_NXT));
			return directAllowed
				? ModifierArguments.AA0RB
				: ModifierArguments.AE21POP;
		}
		return null;
	}

	@Override
	public void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final ResultHandler store) {

		assert argumentA == null;
		assert argumentB == null;
		assert modifierValue != null;
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = new TKV_LCONSTS(this.argumentB);
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		assembly.addInstruction(
				OperationsS3X.VASTORE_NS//
						.instruction(
								directA
									? ModifierArguments.AE21POP
									: modifierA, //
								modifierB,
								modifierValue,
								0,
								store));
	}

	@Override
	public Instruction toReferenceWriteSkipAfterRead(//
			final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ResultHandler store//
	) {

		assert argumentA == null;
		assert argumentB == null;
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		if (modifierA == ModifierArguments.AA0RB) {
			return OperationsA00.XCVOID_P.instruction(
					store.isStackPush()
						? 2
						: 1,
					store);
		}
		return null;
	}
}
