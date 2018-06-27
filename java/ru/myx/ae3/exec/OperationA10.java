/**
 *
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public interface OperationA10 {
	
	/** Return an operation that will definitely produce native result
	 *
	 * @return */
	OperationA10 execNativeResult();
	
	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	OperationA10 execStackResult();
	
	/** @param process
	 * @param argumentA
	 * @param constant
	 * @param store
	 *            TODO
	 * @return code */
	ExecStateCode execute(final ExecProcess process, final BaseObject argumentA, final int constant, ResultHandler store);
	
	/** Returns 0 by default
	 *
	 * @param constant
	 * @return count */
	default int getStackInputCount(final int constant) {
		
		return 0;
	}
	
	/** @param constantArgumentA
	 * @param defaultArgumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionA10 instruction(final BaseObject constantArgumentA, final ModifierArgument defaultArgumentA, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return constant == 0
				? new IA10_AXXX_O_0_NN_NXT(this, constantArgumentA)
				: new IA10_AXXX_O_C_NN_NXT(this, constantArgumentA, constant);
		}
		if (store == ResultHandler.FC_PNN_RET) {
			return new IA10_AXXX_O_C_NN_RET(this, constantArgumentA, constant);
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return new IA10_AXXX_O_C_SN_NXT(this, constantArgumentA, constant);
		}
		return new IA10_AXXX_O_C_XX_XXX(this, constantArgumentA, constant, store);
	}
	
	/** @param argumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionA10 instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			if (constant == 0) {
				if (argumentA == AA0RB) {
					return new IA10_AXXX_R_0_NN_NXT(this);
				}
				{
					final BaseObject constantValue = argumentA.argumentConstantValue();
					if (constantValue != null) {
						return new IA10_AXXX_O_0_NN_NXT(this, constantValue);
					}
				}
				return new IA10_AXXX_A_0_NN_NXT(this, argumentA);
			}
			
			if (argumentA == AA0RB) {
				return new IA10_AXXX_R_C_NN_NXT(this, constant);
			}
			{
				final BaseObject constantValue = argumentA.argumentConstantValue();
				if (constantValue != null) {
					return new IA10_AXXX_O_C_NN_NXT(this, constantValue, constant);
				}
			}
			return new IA10_AXXX_A_C_NN_NXT(this, argumentA, constant);
		}
		if (argumentA == AA0RB) {
			return new IA10_AXXX_R_C_XX_XXX(this, constant, store);
		}
		{
			final BaseObject constantValue = argumentA.argumentConstantValue();
			if (constantValue != null) {
				return this.instruction(constantValue, argumentA, constant, store);
			}
		}
		/** <code>
		if (store == NN) {
			return new IA10_AXXX_A_C_NN_XXX(this, argumentA, constant, state);
		}
		</code> */
		if (store == ResultHandler.FB_BSN_NXT) {
			return new IA10_AXXX_A_C_SN_NXT(this, argumentA, constant);
		}
		
		return new IA10_AXXX_A_C_XX_XXX(this, argumentA, constant, store);
	}
	
	/** NOT USED ANYMORE
	 *
	 *
	 * @param argumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	@Deprecated
	default InstructionEditable instructionCreate(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
		
		return new InstructionA10E(this, argumentA, constant, store);
	}
	
	/** No side effects and constant result with constant arguments.
	 *
	 * @return */
	boolean isConstantForArguments();
	
	/** Only for good code dumps. Doesn't affect execution and compilation at all.
	 *
	 * @return false by default */
	default boolean isRelativeAddressInConstant() {
		
		return false;
	}
}
