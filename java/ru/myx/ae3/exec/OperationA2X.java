/**
 *
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;
import static ru.myx.ae3.exec.ModifierArguments.AE21POP;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public interface OperationA2X {
	
	/** Return an operation that map produce direct/temp result
	 *
	 * @return */
	public OperationA2X execDirectResult();
	
	/** Return an operation that will definitely produce native result
	 *
	 * @return */
	public OperationA2X execNativeResult();
	
	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	public OperationA2X execStackResult();
	
	/** @param process
	 * @param argumentA
	 * @param argumentB
	 * @param constant
	 * @param store
	 * @return code */
	public ExecStateCode execute(ExecProcess process, BaseObject argumentA, BaseObject argumentB, final int constant, ResultHandler store);
	
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
	 * @param constantArgumentA
	 * @param defaultArgumentA
	 * @param argumentB
	 * @param constant
	 * @return */
	default Instruction instruction(final BaseObject constantArgumentA, final ModifierArgument defaultArgumentA, final ModifierArgument argumentB, final int constant) {
		
		return new IA2X_AXXX_AB_C_NN_NXT(this, defaultArgumentA == null
			? new ModifierArgumentA30IMM(constantArgumentA)
			: defaultArgumentA, argumentB, constant);
	}
	
	/** @param constantArgumentA
	 * @param defaultArgumentA
	 * @param argumentB
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default Instruction instruction(final BaseObject constantArgumentA,
			final ModifierArgument defaultArgumentA,
			final ModifierArgument argumentB,
			final int constant,
			final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return this.instruction(constantArgumentA, defaultArgumentA, argumentB, constant);
		}
		
		if (constant == 0) {
			
			if (store == ResultHandler.FC_PNN_RET) {
				return new IA2X_AXXX_OB_0_NN_RET(this, constantArgumentA, argumentB);
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				return new IA2X_AXXX_OB_0_SN_NXT(this, constantArgumentA, argumentB);
			}
			
			return new IA2X_AXXX_AB_0_XX_XXX(this, defaultArgumentA == null
				? new ModifierArgumentA30IMM(constantArgumentA)
				: defaultArgumentA, argumentB, store);
		}
		
		return new IA2X_AXXX_AB_C_XX_XXX(this, defaultArgumentA == null
			? new ModifierArgumentA30IMM(constantArgumentA)
			: defaultArgumentA, argumentB, constant, store);
	}
	
	/** store == NN
	 *
	 * state == NEXT
	 *
	 * @param argumentA
	 * @param constantArgumentB
	 * @param defaultArgumentB
	 * @param constant
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA, final BaseObject constantArgumentB, final ModifierArgument defaultArgumentB, final int constant) {
		
		return constant == 0
			? new IA2X_AXXX_AO_0_NN_NXT(this, argumentA, constantArgumentB)
			: new IA2X_AXXX_AO_C_NN_NXT(this, argumentA, constantArgumentB, constant);
	}
	
	/** @param argumentA
	 * @param constantArgumentB
	 * @param defaultArgumentB
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA,
			final BaseObject constantArgumentB,
			final ModifierArgument defaultArgumentB,
			final int constant,
			final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return this.instruction(argumentA, constantArgumentB, defaultArgumentB, constant);
		}
		
		if (constant == 0) {
			
			if (store == ResultHandler.FC_PNN_RET) {
				return new IA2X_AXXX_AB_0_NN_RET(this, argumentA, defaultArgumentB == null
					? new ModifierArgumentA30IMM(constantArgumentB)
					: defaultArgumentB);
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				return new IA2X_AXXX_AB_0_SN_NXT(this, argumentA, defaultArgumentB == null
					? new ModifierArgumentA30IMM(constantArgumentB)
					: defaultArgumentB);
			}
			return new IA2X_AXXX_AB_0_XX_XXX(this, argumentA, defaultArgumentB == null
				? new ModifierArgumentA30IMM(constantArgumentB)
				: defaultArgumentB, store);
		}
		
		return new IA2X_AXXX_AB_C_XX_XXX(this, argumentA, defaultArgumentB == null
			? new ModifierArgumentA30IMM(constantArgumentB)
			: defaultArgumentB, constant, store);
	}
	
	/** @param argumentA
	 * @param argumentB
	 * @param constant
	 * @param store
	 * @return */
	default Instruction instruction(final ModifierArgument argumentA, final ModifierArgument argumentB, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			if (argumentB == AA0RB) {
				if (argumentA == AE21POP) {
					return new IA2X_AXXX_PR_X_NN_NXT(this, constant);
				}
				return constant == 0
					? new IA2X_AXXX_AR_0_NN_NXT(this, argumentA)
					: new IA2X_AXXX_AR_C_NN_NXT(this, argumentA, constant);
			}
			{
				final BaseObject constantValue = argumentA.argumentConstantValue();
				if (constantValue != null) {
					return this.instruction(constantValue, argumentA, argumentB, constant);
				}
			}
			{
				final BaseObject constantValue = argumentB.argumentConstantValue();
				if (constantValue != null) {
					return this.instruction(argumentA, constantValue, argumentB, constant);
				}
			}
			return new IA2X_AXXX_AB_C_NN_NXT(this, argumentA, argumentB, constant);
		}
		
		{
			final BaseObject constantValue = argumentA.argumentConstantValue();
			if (constantValue != null) {
				return this.instruction(constantValue, argumentA, argumentB, constant, store);
			}
		}
		{
			final BaseObject constantValue = argumentB.argumentConstantValue();
			if (constantValue != null) {
				return this.instruction(argumentA, constantValue, argumentB, constant, store);
			}
		}
		if (constant == 0) {
			if (store == ResultHandler.FC_PNN_RET) {
				return new IA2X_AXXX_AB_0_NN_RET(this, argumentA, argumentB);
			} else//
			if (store == ResultHandler.FB_BSN_NXT) {
				return new IA2X_AXXX_AB_0_SN_NXT(this, argumentA, argumentB);
			}
			
			return new IA2X_AXXX_AB_0_XX_XXX(this, argumentA, argumentB, store);
		}
		return new IA2X_AXXX_AB_C_XX_XXX(this, argumentA, argumentB, constant, store);
	}
	
	/** NOT USED ANYMORE
	 *
	 * @param argumentA
	 * @param argumentB
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	@Deprecated
	default InstructionEditable instructionCreate(final ModifierArgument argumentA, final ModifierArgument argumentB, final int constant, final ResultHandler store) {
		
		return new InstructionA2XE(this, argumentA, argumentB, constant, store);
	}
	
	/** @return is it's constant for constant arguments */
	public boolean isConstantForArguments();
}
