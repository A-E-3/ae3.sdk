package ru.myx.ae3.exec;



/** @author myx */
public interface InstructionSource extends Instruction {

	/** Returns non-editable (optimised?) instruction.
	 *
	 * @return */
	Instruction getFinalIfReady();

}
