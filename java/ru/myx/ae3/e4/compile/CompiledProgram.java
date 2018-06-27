package ru.myx.ae3.e4.compile;

import ru.myx.ae3.e4.vm.AeVmImpl;
import ru.myx.ae3.e4.vm.AeVmInstruction;

/**
 * 
 * @author myx
 *
 */
public interface CompiledProgram extends Runnable, AeVmInstruction {
	/**
	 * 
	 * @return
	 */
	AeVmImpl getTargetVmImpl();
}
