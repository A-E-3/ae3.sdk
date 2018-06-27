package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableFull;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 *
 * @author myx
 *
 */
@ReflectionManual
public final class FunctionWrapDeferredSyncThis extends BaseFunctionAbstract implements ExecCallableFull {
	
	
	private final BaseFunction function;

	/**
	 *
	 * @param function
	 */
	@ReflectionExplicit
	public FunctionWrapDeferredSyncThis(final BaseFunction function) {
		
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
	public ExecStateCode execCallImpl(final ExecProcess ctx) throws Exception {
		
		
		final BaseObject instance = ctx.rb4CT;
		synchronized (instance) {
			return this.function.execCallPrepare(ctx, instance, ctx.riCallResultHandler, true, ctx.contextGetArguments());
		}
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
