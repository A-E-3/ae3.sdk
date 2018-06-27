package ru.myx.ae3.e4.vm.macro;

import java.util.ArrayList;
import java.util.List;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.e4.compile.CompileContext;
import ru.myx.ae3.e4.compile.CompiledProgram;
import ru.myx.ae3.e4.parse.ParseTokenValue;
import ru.myx.ae3.e4.vm.AeVmImpl;
import ru.myx.ae3.e4.vm.AeVmInstruction;

/**
 * 
 * @author myx
 *
 */
class MacroCompileContext implements CompileContext {
	List<AeVmInstruction>	code	= new ArrayList<>();
	
	
	@Override
	public void addReturnVoidStatement() {
	
		this.code.add( new MacroVmInstruction.InstructionReturnVoid() );
		
	}
	
	
	@Override
	public void addReturnValueStatement(
			ParseTokenValue value) {
	
		final BaseObject constant = value.toConstantValue();
		if (constant != null) {
			addReturnConstantStatement( constant );
			return;
		}
		
	}
	
	
	@Override
	public void addCompiledStatement(
			CompiledProgram statement) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void addReturnConstantStatement(
			BaseObject value) {
	
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public AeVmImpl getTargetVmImpl() {
	
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public CompiledProgram toCompiled() {
	
		// TODO Auto-generated method stub
		return null;
	}
	
}
