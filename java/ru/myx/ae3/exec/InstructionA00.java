/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.InstructionIA0A;
import ru.myx.vm_vliw32_2010.OperationA00;

/** @author myx */
abstract class InstructionA00 extends InstructionIA0A {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>(256);
	
	static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionA00.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/** @return */
	@Override
	public abstract OperationA00 getOperation();
}
