/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface OperationA3X extends VOFmtA3 {
	
	/** Return an operation that will definitely produce native result
	 *
	 * @return */
	OperationA3X execNativeResult();
	
	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	OperationA3X execStackResult();
	
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
	default InstructionIA instruction(final ModifierArgument argumentA,
			final BaseObject constantArgumentB,
			final ModifierArgument defaultArgumentB,
			final ModifierArgument argumentC,
			final int constant) {
		
		return constant == 0
			? new IA3_AXXX_AOC_0_NN_NXT(this, argumentA, constantArgumentB, defaultArgumentB, argumentC)
			: new IA3_AXXX_AOC_C_NN_NXT(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant);
	}
	
	/** @param argumentA
	 * @param constantArgumentB
	 * @param defaultArgumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @return */
	default InstructionIA instruction(final ModifierArgument argumentA,
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
			return new IA3_AXXX_AOC_C_NN_RET(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant);
		}
		return new IA3_AXXX_AOC_C_XX_XXX(this, argumentA, constantArgumentB, defaultArgumentB, argumentC, constant, store);
	}
	
	/** @param argumentA
	 * @param argumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionIA
			instruction(final ModifierArgument argumentA, final ModifierArgument argumentB, final ModifierArgument argumentC, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			{
				final BaseObject constantValue = argumentB.argumentConstantValue();
				if (constantValue != null) {
					return this.instruction(argumentA, constantValue, argumentB, argumentC, constant);
				}
			}
			return new IA3_AXXX_ABC_C_NN_NXT(this, argumentA, argumentB, argumentC, constant);
		}
		{
			final BaseObject constantValue = argumentB.argumentConstantValue();
			if (constantValue != null) {
				return this.instruction(argumentA, constantValue, argumentB, argumentC, constant, store);
			}
		}
		return new IA3_AXXX_ABC_C_XX_XXX(this, argumentA, argumentB, argumentC, constant, store);
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
	default InstructionEditable
			instructionCreate(final ModifierArgument argumentA, final ModifierArgument argumentB, final ModifierArgument argumentC, final int constant, final ResultHandler store) {
		
		return new InstructionIA3E(this, argumentA, argumentB, argumentC, constant, store);
	}
}
