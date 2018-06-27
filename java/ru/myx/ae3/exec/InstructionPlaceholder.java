package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * @author myx
 *
 */
public interface InstructionPlaceholder extends Instruction {
	
	
	/**
	 * @param instruction
	 * @return this
	 */
	@NotNull
	public InstructionPlaceholder setInstruction(final Instruction instruction);
	
	/**
	 * Returns non-editable (optimised?) instruction.
	 *
	 * @return or NULL
	 */
	@Nullable
	public Instruction getFinalIfReady();
	
}
