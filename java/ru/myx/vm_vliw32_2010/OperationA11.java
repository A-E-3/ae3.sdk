/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface OperationA11 extends VOFmtA11 {
	
	/** @param argumentA
	 * @param constant
	 * @param store
	 * @return */
	default InstructionEditable instructionCreate(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
		
		return new InstructionIA1E(this, argumentA, constant, store);
	}
}
