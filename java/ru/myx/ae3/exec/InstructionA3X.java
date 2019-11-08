/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.InstructionIA3A;
import ru.myx.vm_vliw32_2010.OperationA3X;

/** @author myx */
abstract class InstructionA3X extends InstructionIA3A {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>();

	final static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionA3X.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/** @return */
	@Override
	public abstract OperationA3X getOperation();

}
