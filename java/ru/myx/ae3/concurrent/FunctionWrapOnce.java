package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseAbstractException;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableJava;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public final class FunctionWrapOnce extends BaseFunctionAbstract implements ExecCallableJava.NativeE0, ExecCallable.ForStore.UseStore0 {
	
	private static final BaseFunction ST_FAILED = new FunctionWrapOnce(BaseFunction.RETURN_UNDEFINED);
	
	private static final BaseFunction ST_MAKING = new FunctionWrapOnce(BaseFunction.RETURN_UNDEFINED);
	
	private BaseFunction function;
	
	private BaseObject result;
	
	/** @param function */
	@ReflectionExplicit
	public FunctionWrapOnce(final BaseFunction function) {

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
	
	//
	
	@Override
	public BaseObject baseConstructPrototype() {
		
		return this.function.baseConstructPrototype();
	}
	
	@Override
	public BaseObject callNE0(final ExecProcess ctx, final BaseObject instance) {
		
		{
			final BaseFunction function = this.function;
			if (function == null) {
				return this.result;
			}
			if (function == FunctionWrapOnce.ST_FAILED) {
				throw (BaseAbstractException) this.result;
			}
		}
		{
			final BaseFunction function;
			boolean make = false;
			synchronized (this) {
				function = this.function;
				if (function == null) {
					return this.result;
				}
				if (function == FunctionWrapOnce.ST_FAILED) {
					throw (BaseAbstractException) this.result;
				}
				if (function == FunctionWrapOnce.ST_MAKING) {
					try {
						do {
							this.wait(1_000L);
						} while (function == FunctionWrapOnce.ST_MAKING);
					} catch (final InterruptedException e) {
						throw new RuntimeException(e);
					}
				} else {
					this.function = FunctionWrapOnce.ST_MAKING;
					make = true;
				}
			}
			/** end of sync */
			/** so do we have to execute? */
			if (make) {
				try {
					this.result = function.callNE0(ctx, instance);
					this.function = null;
					synchronized (this) {
						this.notifyAll();
					}
					return this.result;
				} catch (final Exception e) {
					final BaseAbstractException result = Base.forThrowable(e);
					this.function = FunctionWrapOnce.ST_FAILED;
					this.result = result;
					synchronized (this) {
						this.notifyAll();
					}
					throw result;
				}
			}
		}
		
		{
			final BaseFunction function = this.function;
			if (function == null) {
				return this.result;
			}
			{
				throw (BaseAbstractException) this.result;
			}
		}
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		{
			final BaseFunction function = this.function;
			if (function == null) {
				return store.execReturn(ctx, this.result);
			}
			if (function == FunctionWrapOnce.ST_FAILED) {
				ctx.ra0RB = this.result;
				return ExecStateCode.ERROR;
			}
		}
		{
			final BaseFunction function;
			boolean make = false;
			synchronized (this) {
				function = this.function;
				if (function == null) {
					return store.execReturn(ctx, this.result);
				}
				if (function == FunctionWrapOnce.ST_FAILED) {
					ctx.ra0RB = this.result;
					return ExecStateCode.ERROR;
				}
				if (function == FunctionWrapOnce.ST_MAKING) {
					try {
						do {
							this.wait(1_000L);
						} while (function == FunctionWrapOnce.ST_MAKING);
					} catch (final InterruptedException e) {
						return ctx.vmRaise(e);
					}
				} else {
					this.function = FunctionWrapOnce.ST_MAKING;
					make = true;
				}
			}
			/** end of sync */
			/** so do we have to execute? */
			if (make) {
				try {
					this.result = function.callNE0(ctx, instance);
					this.function = null;
					synchronized (this) {
						this.notifyAll();
					}
					return store.execReturn(ctx, this.result);
				} catch (final Exception e) {
					final BaseAbstractException result = Base.forThrowable(e);
					this.result = result;
					this.function = FunctionWrapOnce.ST_FAILED;
					synchronized (this) {
						this.notifyAll();
					}
					ctx.ra0RB = result;
					return ExecStateCode.ERROR;
				}
			}
		}
		
		{
			final BaseFunction function = this.function;
			if (function == null) {
				return store.execReturn(ctx, this.result);
			}
			{
				ctx.ra0RB = this.result;
				return ExecStateCode.ERROR;
			}
		}
	}
	
	@Override
	public boolean execIsConstant() {
		
		return true;
	}
	
	@Override
	public Class<? extends Object> execResultClassJava() {
		
		final BaseFunction function = this.function;
		return function == null
			? Object.class
			: function.execResultClassJava();
	}
	
	@Override
	public BaseObject execScope() {
		
		final BaseFunction function = this.function;
		return function == null
			? ExecProcess.GLOBAL
			: function.execScope();
	}
	
	@Override
	public String toString() {
		
		final BaseFunction function = this.function;
		if (function == null) {
			return "[function " + this.getClass().getSimpleName() + "(DONE)]";
		}
		if (function == FunctionWrapOnce.ST_FAILED) {
			return "[function " + this.getClass().getSimpleName() + "(FAILED)]";
		}
		if (function == FunctionWrapOnce.ST_MAKING) {
			return "[function " + this.getClass().getSimpleName() + "(MAKING)]";
		}
		return "[function " + this.getClass().getSimpleName() + '(' + function + ")]";
	}
}
