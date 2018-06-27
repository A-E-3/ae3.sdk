/*
 * Created on 09.03.2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationA3X;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.OperationsS3X;
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
public final class TKV_ACCESS_BA_VV_S extends TokenValue {

	private final TokenInstruction argumentA;
	
	private final TokenInstruction argumentB;
	
	private int visibility = 0;
	
	/**
	 * @param argumentA
	 * @param argumentB
	 */
	public TKV_ACCESS_BA_VV_S(final TokenInstruction argumentA, final TokenInstruction argumentB) {
		assert argumentA.isStackValue();
		assert argumentB.isStackValue();
		this.argumentA = argumentA;
		this.argumentB = argumentB;
	}
	
	@Override
	public final String getNotation() {

		return this.argumentA.getNotationValue() + "[" + this.argumentB.getNotation() + "]";
	}
	
	@Override
	public final String getNotationValue() {

		return this.argumentA.getNotationValue() + "[" + this.argumentB.getNotation() + "]";
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

		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;
		
		/**
		 * valid store
		 */
		assert store != null;
		
		final ModifierArgument modifierA = this.argumentA.toDirectModifier();
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(assembly, null, null, directB
				? ResultHandler.FB_BSN_NXT
				: ResultHandler.FA_BNN_NXT);
		}
		if (directB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		final InstructionResult argumentType = this.argumentB.getResultType();
		assembly.addInstruction((argumentType == InstructionResult.INTEGER
			? this.visibility == 0
				? OperationsS2X.VACCESS_TI
				: OperationsS2X.VACCESS_DI
			: argumentType == InstructionResult.STRING
				? this.visibility == 2
					? OperationsS2X.VACCESS_NS
					: OperationsS2X.VACCESS_DS
				: this.visibility == 2
					? OperationsS2X.VACCESS_NA
					: OperationsA2X.XACCESS_D)//
							.instruction(
									directA && directB
										? ModifierArguments.AE21POP
										: modifierA, //
									modifierB,
									0,
									store));
	}
	
	@Override
	public final String toCode() {

		return "ACCESS\t0\tVV ->S\t[" + this.argumentA + ", " + this.argumentB + "];";
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

		return new TKV_DELETE_BA_VV_S(this.argumentA, this.argumentB);
	}
	
	@Override
	public TokenInstruction toReferenceObject() {

		return this.argumentA;
	}
	
	@Override
	public TokenInstruction toReferenceProperty() {

		return this.argumentB;
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
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directA) {
			this.argumentA.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		if (directB) {
			this.argumentB.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		if (needRead) {
			final InstructionResult argumentType = this.argumentB.getResultType();
			assembly.addInstruction((argumentType == InstructionResult.INTEGER
				? this.visibility == 0
					? OperationsS2X.VACCESS_TI
					: OperationsS2X.VACCESS_DI
				: argumentType == InstructionResult.STRING
					? this.visibility == 2
						? OperationsS2X.VACCESS_NS
						: OperationsS2X.VACCESS_DS
					: this.visibility == 2
						? OperationsS2X.VACCESS_NA
						: OperationsA2X.XACCESS_D)//
								.instruction(
										directA
											? directB
												? ModifierArguments.AE23PEEK2
												: ModifierArguments.AE22PEEK
											: modifierA, //
										directB
											? ModifierArguments.AE22PEEK
											: modifierB,
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
		final ModifierArgument modifierB = this.argumentB.toDirectModifier();
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		final OperationA3X operation;
		switch (this.argumentB.getResultType()) {
			case STRING :
				operation = OperationsS3X.VASTORE_NS;
				break;
			case INTEGER :
				operation = OperationsS3X.VASTORE_NI;
				break;
			default :
				operation = OperationsA3X.XASTORE_N;
		}
		assembly.addInstruction(operation//
				.instruction(
						directA
							? ModifierArguments.AE21POP
							: modifierA, //
						directB
							? ModifierArguments.AE21POP
							: modifierB,
						modifierValue,
						0,
						store));
	}
}
