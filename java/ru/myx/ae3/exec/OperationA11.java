/**
 *
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public interface OperationA11 {
	
	/** @param process
	 * @param argumentA
	 * @param constant
	 * @param store
	 *            TODO
	 * @return code */
	ExecStateCode execute(ExecProcess process, BaseObject argumentA, int constant, ResultHandler store);
	
	/** Returns 0 by default
	 *
	 * @param constant
	 * @return count */
	default int getStackInputCount(final int constant) {
		
		return 0;
	}
	
	/** @param argumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			return argumentA == AA0RB
				? new IA11_AXXX_R_C_NN_NXT(this, constant)
				: new IA11_AXXX_A_C_NN_NXT(this, argumentA, constant);
		}
		return argumentA == AA0RB
			? new IA11_AXXX_R_C_XX_XXX(this, constant, store)
			: new IA11_AXXX_A_C_XX_XXX(this, argumentA, constant, store);
	}
	
	/** @param argumentA
	 * @param constant
	 * @param store
	 * @return */
	default InstructionEditable instructionCreate(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
		
		return new InstructionA11E(this, argumentA, constant, store);
	}
	
	/** Only for good code dumps. Doesn't affect execution and compilation at all.
	 *
	 * @return false by default */
	default boolean isRelativeAddressInConstant() {
		
		return false;
	}
}
