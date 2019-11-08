package ru.myx.vm_vliw32_2010;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface VOFmtA1 extends VOFmt {
	
	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	VOFmtA1 execStackResult();

	/** @param process
	 * @param argumentA
	 * @param constant
	 * @param store
	 * @return */
	ExecStateCode execute(ExecProcess process, BaseObject argumentA, int constant, ResultHandler store);

	/** @param constantArgumentA
	 * @param defaultArgumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionIA/* 1A */ instruction(final BaseObject constantArgumentA, final ModifierArgument defaultArgumentA, final int constant, final ResultHandler store) {
		
		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return constant == 0
				? new IA1_AXXX_O_0_NN_NXT(this, constantArgumentA)
				: new IA1_AXXX_O_C_NN_NXT(this, constantArgumentA, constant);
		}
		if (store == ResultHandler.FC_PNN_RET) {
			return new IA1_AXXX_O_C_NN_RET(this, constantArgumentA, constant);
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return new IA1_AXXX_O_C_SN_NXT(this, constantArgumentA, constant);
		}
		return new IA1_AXXX_O_C_XX_XXX(this, constantArgumentA, constant, store);
	}

	/** @param argumentA
	 * @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionIA/* 1A */ instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {

		if (store == ResultHandler.FA_BNN_NXT) {
			if (constant == 0) {
				if (argumentA == AA0RB) {
					return new IA1_AXXX_R_0_NN_NXT(this);
				}
				{
					final BaseObject constantValue = argumentA.argumentConstantValue();
					if (constantValue != null) {
						return new IA1_AXXX_O_0_NN_NXT(this, constantValue);
					}
				}
				return new IA1_AXXX_A_0_NN_NXT(this, argumentA);
			}

			if (argumentA == AA0RB) {
				return new IA1_AXXX_R_C_NN_NXT(this, constant);
			}
			{
				final BaseObject constantValue = argumentA.argumentConstantValue();
				if (constantValue != null) {
					return new IA1_AXXX_O_C_NN_NXT(this, constantValue, constant);
				}
			}
			return new IA1_AXXX_A_C_NN_NXT(this, argumentA, constant);
		}
		if (argumentA == AA0RB) {
			return new IA1_AXXX_R_C_XX_XXX(this, constant, store);
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
			return new IA1_AXXX_A_C_SN_NXT(this, argumentA, constant);
		}

		return new IA1_AXXX_A_C_XX_XXX(this, argumentA, constant, store);
	}
}
