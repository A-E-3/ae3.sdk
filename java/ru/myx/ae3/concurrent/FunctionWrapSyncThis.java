package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 *
 * @author myx
 *
 */
@ReflectionManual
public final class FunctionWrapSyncThis extends BaseFunctionAbstract {
	
	
	private final BaseFunction function;

	/**
	 *
	 * @param function
	 */
	@ReflectionExplicit
	public FunctionWrapSyncThis(final BaseFunction function) {
		
		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
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
	public double callDJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callDJ0(instance);
		}
	}

	@Override
	public double callDJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callDJ1(instance, argument);
		}
	}

	@Override
	public double callDJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callDJA(instance, arguments);
		}
	}

	@Override
	public double callDJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callDJ2(instance, argument1, argument2);
		}
	}

	@Override
	public double callDE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callDE0(ctx, instance);
		}
	}

	@Override
	public double callDE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callDE1(ctx, instance, argument);
		}
	}

	@Override
	public double callDEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callDEA(ctx, instance, arguments);
		}
	}

	@Override
	public double callDE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callDE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public double callDEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			return this.function.callDEX(ctx, instance, arguments);
		}
	}

	@Override
	public int callIJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callIJ0(instance);
		}
	}

	@Override
	public int callIJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callIJA(instance, arguments);
		}
	}

	@Override
	public int callIJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callIJ1(instance, argument);
		}
	}

	@Override
	public int callIJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callIJ2(instance, argument1, argument2);
		}
	}

	@Override
	public int callIE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callIE0(ctx, instance);
		}
	}

	@Override
	public int callIEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callIEA(ctx, instance, arguments);
		}
	}

	@Override
	public int callIE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callIE1(ctx, instance, argument);
		}
	}

	@Override
	public int callIE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callIE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public int callIEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			return this.function.callIEX(ctx, instance, arguments);
		}
	}

	@Override
	public long callLJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callLJ0(instance);
		}
	}

	@Override
	public long callLJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callLJ1(instance, argument);
		}
	}

	@Override
	public long callLJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callLJA(instance, arguments);
		}
	}

	@Override
	public long callLJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callLJ2(instance, argument1, argument2);
		}
	}

	@Override
	public long callLE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callLE0(ctx, instance);
		}
	}

	@Override
	public long callLE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callLE1(ctx, instance, argument);
		}
	}

	@Override
	public long callLEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callLEA(ctx, instance, arguments);
		}
	}

	@Override
	public long callLE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callLE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public long callLEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			return this.function.callLEX(ctx, instance, arguments);
		}
	}

	@Override
	public BaseObject callNJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callNJ0(instance);
		}
	}

	@Override
	public BaseObject callNJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callNJ1(instance, argument);
		}
	}

	@Override
	public BaseObject callNJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callNJA(instance, arguments);
		}
	}

	@Override
	public BaseObject callNJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callNJ2(instance, argument1, argument2);
		}
	}

	@Override
	public BaseObject callNE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callNE0(ctx, instance);
		}
	}

	@Override
	public BaseObject callNE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callNE1(ctx, instance, argument);
		}
	}

	@Override
	public BaseObject callNEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callNEA(ctx, instance, arguments);
		}
	}

	@Override
	public BaseObject callNE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callNE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public BaseObject callNEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			return this.function.callNEX(ctx, instance, arguments);
		}
	}

	@Override
	public String callSJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callSJ0(instance);
		}
	}

	@Override
	public String callSJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callSJ1(instance, argument);
		}
	}

	@Override
	public String callSJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callSJA(instance, arguments);
		}
	}

	@Override
	public String callSJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callSJ2(instance, argument1, argument2);
		}
	}

	@Override
	public String callSE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			return this.function.callSE0(ctx, instance);
		}
	}

	@Override
	public String callSE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			return this.function.callSE1(ctx, instance, argument);
		}
	}

	@Override
	public String callSEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			return this.function.callSEA(ctx, instance, arguments);
		}
	}

	@Override
	public String callSE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			return this.function.callSE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public String callSEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			return this.function.callSEX(ctx, instance, arguments);
		}
	}

	@Override
	public void callVJ0(final BaseObject instance) {
		
		
		synchronized (instance) {
			this.function.callVJ0(instance);
		}
	}

	@Override
	public void callVJA(final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			this.function.callVJA(instance, arguments);
		}
	}

	@Override
	public void callVJ1(final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			this.function.callVJ1(instance, argument);
		}
	}

	@Override
	public void callVJ2(final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			this.function.callVJ2(instance, argument1, argument2);
		}
	}

	@Override
	public void callVE0(final ExecProcess ctx, final BaseObject instance) {
		
		
		synchronized (instance) {
			this.function.callVE0(ctx, instance);
		}
	}

	@Override
	public void callVEA(final ExecProcess ctx, final BaseObject instance, final BaseObject... arguments) {
		
		
		synchronized (instance) {
			this.function.callVEA(ctx, instance, arguments);
		}
	}

	@Override
	public void callVE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		
		synchronized (instance) {
			this.function.callVE1(ctx, instance, argument);
		}
	}

	@Override
	public void callVE2(final ExecProcess ctx, final BaseObject instance, final BaseObject argument1, final BaseObject argument2) {
		
		
		synchronized (instance) {
			this.function.callVE2(ctx, instance, argument1, argument2);
		}
	}

	@Override
	public void callVEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {
		
		
		synchronized (instance) {
			this.function.callVEX(ctx, instance, arguments);
		}
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
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		
		synchronized (instance) {
			/**
			 * 'inline' mode - within sync block. maybe will use queue in the
			 * future
			 */
			return this.function.execCallPrepare(ctx, instance, store, true);
		}
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseArray arguments) {
		
		
		synchronized (instance) {
			/**
			 * 'inline' mode - within sync block. maybe will use queue in the
			 * future
			 */
			return this.function.execCallPrepare(ctx, instance, store, true, arguments);
		}
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject argument) {
		
		
		synchronized (instance) {
			/**
			 * 'inline' mode - within sync block. maybe will use queue in the
			 * future
			 */
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
		
		
		synchronized (instance) {
			/**
			 * 'inline' mode - within sync block. maybe will use queue in the
			 * future
			 */
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
