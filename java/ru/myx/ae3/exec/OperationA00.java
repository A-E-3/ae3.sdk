/**
 *
 */
package ru.myx.ae3.exec;

/** @author myx */
public interface OperationA00 {
	
	/** @param process
	 * @param constant
	 * @return code */
	ExecStateCode execute(ExecProcess process, int constant);

	/** Returns 0 by default
	 *
	 * @param constant
	 * @return count */
	default int getStackInputCount(final int constant) {
		
		return 0;
	}

	/** @param constant
	 * @param store
	 * @param state
	 * @return */
	default Instruction instruction(final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return new IA00_AXXX_X_NN_NXT(this, constant);
		}
		if (store == ResultHandler.FC_PNN_RET) {
			return new IA00_AXXX_X_NN_RET(this, constant);
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return new IA00_AXXX_X_SN_NXT(this, constant);
		}
		return new IA00_AXXX_X_XX_XXX(this, constant, store);
	}

	/** @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionEditable instructionCreate(final int constant, final ResultHandler store) {
		
		return new InstructionA00E(this, constant, store);
	}

	/** Only for good code dumps. Doesn't affect execution and compilation at all.
	 *
	 * @return false by default */
	default boolean isRelativeAddressInConstant() {
		
		return false;
	}
}
