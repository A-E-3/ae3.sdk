/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.InstructionIA1A;
import ru.myx.vm_vliw32_2010.OperationA10;

/** @author myx */
public abstract class InstructionA10 extends InstructionIA1A {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>();

	final static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionA10.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}

	/** @return */
	@Override
	public abstract OperationA10 getOperation();
}
