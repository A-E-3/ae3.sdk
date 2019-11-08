/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface OperationA01 extends VOFmtA00 {

	/** @param constant
	 * @param store
	 * @return */
	default InstructionEditable instructionCreate(final int constant, final ResultHandler store) {
		
		return new InstructionIA0E(this, constant, store);
	}
}
