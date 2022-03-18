/*
 * Created on 09.03.2004 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA00;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsA3X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.OperationsS3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/** @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments */
public final class TKO_ACCESS_BA_VS_S extends TokenOperator {
	
	private final TokenInstruction accessPropertyName;
	
	private int visibility = 0;
	
	/** @param accessPropertyName */
	public TKO_ACCESS_BA_VS_S(final TokenInstruction accessPropertyName) {
		
		assert accessPropertyName.isStackValue();
		this.accessPropertyName = accessPropertyName;
	}
	
	@Override
	public final String getNotation() {
		
		return "[" + this.accessPropertyName.getNotation() + "]";
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
	public final boolean isAccessReference() {
		
		return true;
	}
	
	@Override
	public boolean isDirectSupported() {
		
		return this.accessPropertyName.toDirectModifier() != ModifierArguments.AA0RB;
	}
	
	@Override
	public boolean isParseValueRight() {
		
		return true;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** check operands */
		assert argumentA != null;
		assert argumentB == null;
		
		/** valid store */
		assert store != null;
		
		final ModifierArgument modifierB = this.accessPropertyName.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			this.accessPropertyName.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
		final InstructionResult argumentType = this.accessPropertyName.getResultType();
		assembly.addInstruction(
				(argumentType == InstructionResult.INTEGER
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
									.instruction(argumentA, modifierB, 0, store));
	}
	
	@Override
	public final String toCode() {
		
		return "ACCESS\t2\tSV ->S\t[" + this.accessPropertyName + "];";
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
		
		return new TKO_DELETE_BA_VS_S(this.accessPropertyName);
	}
	
	@Override
	public TokenInstruction toReferenceObject() {
		
		return null;
	}
	
	@Override
	public TokenInstruction toReferenceProperty() {
		
		return this.accessPropertyName;
	}
	
	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {
		
		assert argumentA != null;
		assert argumentB == null;
		assert argumentA != ModifierArguments.AA0RB;
		final ModifierArgument modifierB = this.accessPropertyName.toDirectModifier();
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		if (directB) {
			this.accessPropertyName.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		}
		if (needRead) {
			final InstructionResult argumentType = this.accessPropertyName.getResultType();
			assembly.addInstruction(
					(argumentType == InstructionResult.INTEGER
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
												argumentA == ModifierArguments.AE21POP
													? directB
														? ModifierArguments.AE23PEEK2
														: ModifierArguments.AE22PEEK
													: argumentA, //
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
		
		assert argumentA != null;
		assert argumentB == null;
		assert argumentA != ModifierArguments.AA0RB;
		assert modifierValue != null;
		final ModifierArgument modifierB = this.accessPropertyName.toDirectModifier();
		assembly.addInstruction(
				(this.accessPropertyName.getResultType() == InstructionResult.STRING
					? OperationsS3X.VASTORE_NS
					: OperationsA3X.XASTORE_N)//
							.instruction(
									argumentA, //
									modifierB == ModifierArguments.AA0RB
										? ModifierArguments.AE21POP
										: modifierB,
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
		
		assert argumentA != null;
		assert argumentB == null;
		assert argumentA != ModifierArguments.AA0RB;
		final ModifierArgument modifierB = this.accessPropertyName.toDirectModifier();
		if (modifierB == ModifierArguments.AA0RB) {
			return OperationsA00.XCVOID_P.instruction(
					store.isStackPush()
						? 2
						: 1,
					store);
		}
		return null;
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) {
		
		if (argumentA.toDirectModifier() == ModifierArguments.AB4CT) {
			final BaseObject value = this.accessPropertyName.toConstantValue();
			/** no fate %) <code>
			if (value == null) {
				return new TKV_ZTLOAD_A_V( this.argumentB );
			}
			</code> */
			if (value != null) {
				final BasePrimitive<?> primitiveValue = value.baseToPrimitive(null);
				return primitiveValue.baseIsPrimitiveInteger()
					? new TKV_ZTLOAD_A_Ci(primitiveValue.baseToInt32())
					: new TKV_ZTLOAD_A_Cs(primitiveValue.baseToString());
			}
		} else //
		if (argumentA.toDirectModifier() == ModifierArguments.AB7FV) {
			final BaseObject value = this.accessPropertyName.toConstantValue();
			if (value != null) {
				return new TKV_FLOAD_A_Cs_S(value.baseToString());
			}
		}
		return new TKV_ACCESS_BA_VV_S(argumentA, this.accessPropertyName);
	}
}
