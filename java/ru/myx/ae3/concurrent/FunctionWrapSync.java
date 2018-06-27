package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableJava;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public final class FunctionWrapSync extends BaseFunctionAbstract implements ExecCallableJava.NativeE {
	
	private final BaseFunction function;
	
	private final BaseObject lockObject;
	
	/** @param function
	 * @param lockObject */
	@ReflectionExplicit
	public FunctionWrapSync(final BaseFunction function, final BaseObject lockObject) {

		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
		}
		if (lockObject == null) {
			throw new IllegalArgumentException("LockObject is NULL!");
		}
		this.function = function;
		this.lockObject = lockObject == BaseObject.UNDEFINED
			? null
			: lockObject;
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
	public BaseObject callNE0(final ExecProcess ctx, final BaseObject instance) {
		
		synchronized (this.lockObject) {
			return this.function.callNE0(ctx, instance);
		}
	}
	
	@Override
	public BaseObject callNE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		synchronized (this.lockObject) {
			return this.function.callNE1(ctx, instance, argument);
		}
	}
	
	@Override
	public BaseObject callNE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		synchronized (this.lockObject) {
			return this.function.callNE2(ctx, instance, argument1, argument2);
		}
	}
	
	@Override
	public BaseObject callNEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		synchronized (this.lockObject) {
			return this.function.callNEA(ctx, instance, arguments);
		}
	}
	
	@Override
	public BaseObject callNEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		synchronized (this.lockObject) {
			return this.function.callNEX(ctx, instance, arguments);
		}
	}
	
	@Override
	public final int execArgumentsAcceptable() {
		
		return this.function.execArgumentsAcceptable();
	}
	
	@Override
	public final int execArgumentsDeclared() {
		
		return this.function.execArgumentsDeclared();
	}
	
	@Override
	public final int execArgumentsMinimal() {
		
		return this.function.execArgumentsMinimal();
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		synchronized (this.lockObject) {
			/** 'inline' mode - within sync block. maybe will use queue in the future */
			return this.function.execCallPrepare(ctx, instance, store, true);
		}
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseArray arguments) {
		
		synchronized (this.lockObject) {
			/** 'inline' mode - within sync block. maybe will use queue in the future */
			return this.function.execCallPrepare(ctx, instance, store, true, arguments);
		}
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject argument) {
		
		synchronized (this.lockObject) {
			/** 'inline' mode - within sync block. maybe will use queue in the future */
			return this.function.execCallPrepare(ctx, instance, store, true, argument);
		}
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx,
			final BaseObject instance,
			final ResultHandler store,
			final boolean inline,
			final BaseObject argument1,
			final BaseObject argument2) {
		
		synchronized (this.lockObject) {
			/** 'inline' mode - within sync block. maybe will use queue in the future */
			return this.function.execCallPrepare(ctx, instance, store, true, argument1, argument2);
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
