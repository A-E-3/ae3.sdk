package ru.myx.ae3.exec;



/** @author myx */
public interface InstructionPlaceholder extends Instruction {

	/** Returns non-editable (optimised?) instruction.
	 *
	 * @return or NULL */
	public Instruction getFinalIfReady();

	/** @param instruction
	 * @return this */
	public InstructionPlaceholder setInstruction(final Instruction instruction);

}
