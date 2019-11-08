package ru.myx.ae3.exec;

import com.sun.istack.internal.Nullable;

/** @author myx */
public interface InstructionSource extends Instruction {

	/** Returns non-editable (optimised?) instruction.
	 *
	 * @return */
	@Nullable
	Instruction getFinalIfReady();

}
