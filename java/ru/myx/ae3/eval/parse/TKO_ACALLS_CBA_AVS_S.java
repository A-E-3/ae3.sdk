/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA00;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.vm_vliw32_2010.VIFmtA21;

final class TKO_ACALLS_CBA_AVS_S extends TokenOperator {

	private final TokenInstruction accessProperty;

	private final TokenInstruction arguments;

	private final int constant;

	TKO_ACALLS_CBA_AVS_S(final TokenInstruction callPropertyName, final TokenInstruction arguments, final int constant) {

		assert constant > 1 : constant == 0
			? "Use " + TKO_ACALLV_BA_VS_S.class.getSimpleName() + " then"
			: "Use " + TKO_ACALLO_CBA_AVS_S.class.getSimpleName() + " then";
		assert arguments.assertZeroStackOperands();
		assert callPropertyName.assertStackValue();
		this.accessProperty = callPropertyName;
		this.arguments = arguments;
		this.constant = constant;
	}

	@Override
	public final String getNotation() {

		return "[" + this.accessProperty.getNotation() + "]( " + this.arguments.getNotation() + " )";
	}

	@Override
	public final int getOperandCount() {

		return 1;
	}

	@Override
	public final int getPriorityLeft() {

		return 999;
	}

	@Override
	public final int getPriorityRight() {

		return 999;
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public boolean isDirectSupported() {

		return false;
	}

	@Override
	public boolean isParseValueRight() {

		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** argumentA is access source, this.argumentB is access key */
		assert argumentA != null;
		assert argumentB == null;
		
		assert argumentA != ModifierArguments.AA0RB;

		final ModifierArgument accessPropertyModifier = this.accessProperty.toDirectModifier();
		final boolean accessPropertyDirect = accessPropertyModifier == ModifierArguments.AA0RB;

		if (argumentA == ModifierArguments.AE21POP) {
			if (accessPropertyDirect) {
				this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			}
			assembly.addInstruction(
					(this.accessProperty.getResultType() == InstructionResult.STRING
						? OperationsS2X.VACCESS_DS
						: OperationsA2X.XACCESS_D)//
								.instruction(
										ModifierArguments.AE22PEEK, //
										accessPropertyDirect
											? ModifierArguments.AE21POP
											: accessPropertyModifier,
										0,
										ResultHandler.FB_BSN_NXT));
			this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(OperationsA01.XRCALLA.instruction(this.constant, store));
			return;
		}

		if (this.constant > VIFmtA21.CNST_MAX) {
			if (accessPropertyDirect) {
				this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			}
			this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			assembly.addInstruction(OperationsA00.XCARRAY_N.instruction(this.constant, ResultHandler.FA_BNN_NXT));
			assembly.addInstruction(
					OperationsA3X.XACALLM//
							.instruction(
									argumentA, //
									accessPropertyDirect
										? ModifierArguments.AE21POP
										: accessPropertyModifier,
									ModifierArguments.AA0RB,
									0,
									store));
			return;
		}

		if (accessPropertyDirect) {
			this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			this.accessProperty.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			assembly.addInstruction(
					(this.accessProperty.getResultType() == InstructionResult.STRING
						? OperationsS2X.VACALLS_XS
						: OperationsA2X.XACALLS)//
								.instruction(
										argumentA, //
										ModifierArguments.AA0RB,
										this.constant,
										store));
			return;
		}

		this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);

		final ModifierArgument useArgumentA;
		if (argumentA == ModifierArguments.AE22PEEK) {
			/** or it will be shadowed */
			assembly.addInstruction(OperationsA00.XFSP_LOAD_D.instruction(this.constant + 1, ResultHandler.FA_BNN_NXT));
			useArgumentA = ModifierArguments.AA0RB;
		} else {
			useArgumentA = argumentA;
		}

		assembly.addInstruction(
				(this.accessProperty.getResultType() == InstructionResult.STRING
					? OperationsS2X.VACALLS_XS
					: OperationsA2X.XACALLS)//
							.instruction(
									useArgumentA, //
									accessPropertyModifier,
									this.constant,
									store));
		// assert false : "this=" + assembly.toProgram( start ).toCode();
	}

	@Override
	public final String toCode() {

		return (this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t1+" + this.constant + "\tAVS->S;";
	}

	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {

		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			return new TKV_ZTCALLS_BA_AV_S(this.accessProperty, this.arguments, this.constant);
		}
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			return new TKV_FCALLS_BA_AV_S(this.accessProperty, this.arguments, this.constant);
		}
		return new TKV_ACALLS_CBA_AVV_S(argumentA.toExecDetachableResult(), this.accessProperty, this.arguments, this.constant);
	}
}
