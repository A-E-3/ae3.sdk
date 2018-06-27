package ru.myx.ae3.e4.vm.macro;

import ru.myx.ae3.e4.compile.CompileContext;
import ru.myx.ae3.e4.vm.AeVmImpl;

/**
 * 
 * @author myx
 *
 */
public class VmMacroInterpreter implements AeVmImpl {
	
	@Override
	public CompileContext createCompileContext() {
	
		return new MacroCompileContext();
	}
	//
}
