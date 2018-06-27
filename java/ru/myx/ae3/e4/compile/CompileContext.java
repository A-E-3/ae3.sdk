package ru.myx.ae3.e4.compile;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.e4.parse.ParseTokenValue;
import ru.myx.ae3.e4.vm.AeVmImpl;

/**
 * 
 * @author myx
 *
 */
public interface CompileContext {
	/**
	 * Informational
	 * 
	 * @author myx
	 *
	 */
	enum BasicOperations0x {
		ReturnVoid
	}
	
	/**
	 * Informational
	 * 
	 * @author myx
	 *
	 */
	enum BasicOperations1x {
		ReturnValue
	}
	
	
	void addCompiledStatement(
			CompiledProgram statement);
	
	
	/**
	 * 
	 */
	void addReturnVoidStatement();
	
	
	/**
	 * 
	 * @param value
	 */
	void addReturnValueStatement(
			ParseTokenValue value);
	
	
	/**
	 * 
	 * @param value
	 */
	void addReturnConstantStatement(
			BaseObject value);
	
	
	/**
	 * 
	 * @return
	 */
	AeVmImpl getTargetVmImpl();
	
	
	/**
	 * 
	 * @return
	 */
	CompiledProgram toCompiled();
}
