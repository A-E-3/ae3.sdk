package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/** @author myx */
public interface InstructionEditable extends InstructionPlaceholder {
	
	/** Returns non-editable (optimised?) instruction.
	 *
	 * @return */
	@Override
	@Nullable
	Instruction getFinalIfReady();
	
	/** @param constant
	 * @return this */
	@NotNull
	InstructionEditable setConstant(int constant);
	
	/** @return */
	@NotNull
	Instruction setFinished();
	
}
