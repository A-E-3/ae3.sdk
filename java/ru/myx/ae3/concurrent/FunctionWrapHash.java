package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableFull;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;

/**
 *
 * @author myx
 *
 */
public class FunctionWrapHash extends BaseFunctionAbstract implements ExecCallableFull {
	
	
	private final BaseFunction function;
	
	private final BaseObject[] contexts;
	
	private final int mask;
	
	/**
	 *
	 * @param function
	 */
	public FunctionWrapHash(final BaseFunction function) {
		
		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
		}
		if (function.execArgumentsDeclared() != 0) {
			throw new IllegalArgumentException("Function should have no arguments declared, use '.bind' to provide static arguments!");
		}
		this.function = function;
	}
	
	@Override
	public BaseArray baseArray() {
		
		
		return this.function.baseArray();
	}
	
	@Override
	public String baseClass() {
		
		
		return this.function.baseClass();
	}
	
	@Override
	public void baseClear() {
		
		
		this.function.baseClear();
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		
		return this.function;
	}
	
	@Override
	public BaseObject baseConstructPrototype() {
		
		
		return this.function.baseConstructPrototype();
	}
	
	@Override
	public int execArgumentsAcceptable() {
		
		
		return this.function.execArgumentsAcceptable();
	}
	
	@Override
	public final int execArgumentsDeclared() {
		
		
		return this.function.execArgumentsDeclared();
	}
	
	@Override
	public int execArgumentsMinimal() {
		
		
		return this.function.execArgumentsMinimal();
	}
	
	@Override
	public ExecStateCode execCall(final ExecProcess context) throws Exception {
		
		
		final int serial = --this.serial;
		return this.function[context.contextGetArguments().baseGetFirst(BaseObject.UNDEFINED).hashCode() & this.mask].execCallImpl(context);
	}
	
	@Override
	public String[] execFormalParameters() {
		
		
		return this.function.execFormalParameters();
	}
	
	@Override
	public boolean execHasNamedArguments() {
		
		
		return this.function.execHasNamedArguments();
	}
	
	@Override
	public boolean execIsConstant() {
		
		
		return this.function.execIsConstant();
	}
	
	@Override
	public Class<? extends Object> execResultClassJava() {
		
		
		return this.function.execResultClassJava();
	}
	
	@Override
	public BaseObject execScope() {
		
		
		return this.function.execScope();
	}
	
	@Override
	public String toString() {
		
		
		return "[function " + this.getClass().getSimpleName() + '(' + this.function + ")]";
	}
}
