/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.InstructionIA2A;
import ru.myx.vm_vliw32_2010.OperationA2X;

/** @author myx */
public abstract class InstructionA2X extends InstructionIA2A {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>();
	
	final static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionA2X.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/** @return */
	@Override
	public abstract OperationA2X getOperation();
}
