/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.InstructionIA0A;
import ru.myx.vm_vliw32_2010.OperationA01;

/** @author myx */
abstract class InstructionA01 extends InstructionIA0A {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>(256);

	static final InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionA01.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/** @return operation */
	@Override
	public abstract OperationA01 getOperation();
}
