package ru.myx.ae3.exec;



/** @author myx */
public interface InstructionEditable extends InstructionPlaceholder {
	
	/** Returns non-editable (optimised?) instruction.
	 *
	 * @return */
	@Override
	Instruction getFinalIfReady();
	
	/** @param constant
	 * @return this */
	InstructionEditable setConstant(int constant);
	
	/** @return */
	Instruction setFinished();
	
}
