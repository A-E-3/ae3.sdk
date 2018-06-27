/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public interface OperationA3X {
	
	/** Return an operation that will definitely produce native result
	 *
	 * @return */
	OperationA3X execNativeResult();
	
	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	OperationA3X execStackResult();
	
	/** @param process
	 * @param argumentA
	 * @param argumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @return code */
	ExecStateCode execute(ExecProcess process, BaseObject argumentA, BaseObject argumentB, BaseObject argumentC, int constant, ResultHandler store);
	
	/** Returns 0 by default
	 *
	 * @param constant
	 * @return count */
	default int getStackInputCount(final int constant) {
		
		return 0;
	}
	
	/** store == NN
	 *
	 * state == NEXT
	 *
	 * @param argumentA
	 * @param constantArgumentB
	 * @param defaultArgumentB
	 * @param argumentC
	 * @param constant
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA,
			final BaseObject constantArgumentB,
			final ModifierArgument defaultArgumentB,
			final ModifierArgument argumentC,
			final int constant) {
		
		return constant == 0
			? new IA3X_AXXX_AOC_0_NN_NXT(this, argumentA, constantArgumentB, defaultArgumentB, argumentC)
			: new IA3X_AXXX_AOC_C_NN_NXT(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant);
	}
	
	/** @param argumentA
	 * @param constantArgumentB
	 * @param defaultArgumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA,
			final BaseObject constantArgumentB,
			final ModifierArgument defaultArgumentB,
			final ModifierArgument argumentC,
			final int constant,
			final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return this.instruction(argumentA, constantArgumentB, defaultArgumentB, argumentC, constant);
		}
		if (store == ResultHandler.FC_PNN_RET) {
			// that way is required, so re-implemented method will be called
			return new IA3X_AXXX_AOC_C_NN_RET(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant);
		}
		return new IA3X_AXXX_AOC_C_XX_XXX(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant, store);
	}
	
	/** @param argumentA
	 * @param argumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument argumentC,
			final int constant,
			final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			{
				final BaseObject constantValue = argumentB.argumentConstantValue();
				if (constantValue != null) {
					return this.instruction(argumentA, constantValue, argumentB, argumentC, constant);
				}
			}
			return new IA3X_AXXX_ABC_C_NN_NXT(this, argumentA, argumentB, argumentC, constant);
		}
		{
			final BaseObject constantValue = argumentB.argumentConstantValue();
			if (constantValue != null) {
				return this.instruction(argumentA, constantValue, argumentB, argumentC, constant, store);
			}
		}
		return new IA3X_AXXX_ABC_C_XX_XXX(this, argumentA, argumentB, argumentC, constant, store);
	}
	
	/** NOT USED ANYMORE
	 *
	 *
	 * @param argumentA
	 * @param argumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	@Deprecated
	default InstructionEditable instructionCreate(final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument argumentC,
			final int constant,
			final ResultHandler store) {
		
		return new InstructionA3XE(this, argumentA, argumentB, argumentC, constant, store);
	}
}
