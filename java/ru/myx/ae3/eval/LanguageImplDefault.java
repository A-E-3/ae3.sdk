/**
 *
 */
package ru.myx.ae3.eval;

import java.util.function.Function;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;

final class LanguageImplDefault implements LanguageImpl {
	
	@Override
	public void compile(final String identity, final Function<String, String> folder, final String name, final ProgramAssembly assembly, final CompileTargetMode mode)
			throws Evaluate.CompilationException {
		
		if (mode != CompileTargetMode.INLINE) {
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
		}
	}
	
	@Override
	public String[] getAssociatedAliases() {
		
		return new String[]{
				//
				"ACM.UNDEFINED", //
				"UNDEFINED", //
				"DUMMY", //
				"NONE", //
		};
	}
	
	@Override
	public String[] getAssociatedExtensions() {
		
		return null;
	}
	
	@Override
	public String[] getAssociatedMimeTypes() {
		
		return null;
	}
	
	@Override
	public String getKey() {
		
		return "undefined";
	}
}
