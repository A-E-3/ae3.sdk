/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface OperationA10 extends VOFmtA10 {
	
	/** Return an operation that will definitely produce native result
	 *
	 * @return */
	OperationA10 execNativeResult();
	
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
		
		return new InstructionIA1E(this, argumentA, constant, store);
	}
	
	/** No side effects and constant result with constant arguments.
	 *
	 * @return */
	boolean isConstantForArguments();
}
