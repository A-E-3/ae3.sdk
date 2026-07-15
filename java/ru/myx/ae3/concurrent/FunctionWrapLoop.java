package ru.myx.ae3.concurrent;

import java.util.Arrays;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableFull;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public class FunctionWrapLoop extends BaseFunctionAbstract implements ExecCallableFull {

	private final BaseFunction[] function;

	private final int mask;

	private int serial = 0;

	/** @param function */
	@ReflectionExplicit
	public FunctionWrapLoop(final BaseFunction... function) {

		if (function == null || function.length == 0) {
			throw new IllegalArgumentException("Function array is NULL or empty!");
		}
		if ((function.length & function.length - 1) != 0) {
			throw new IllegalArgumentException("Function array length must be a power of two, got " + function.length + "!");
		}
		for (final BaseFunction item : function) {
			if (item == null) {
				throw new IllegalArgumentException("Function is NULL!");
			}
			if (item.execArgumentsDeclared() != 0) {
				throw new IllegalArgumentException("Function should have no arguments declared, use '.bind' to provide static arguments!");
			}
		}
		this.function = function;
		this.mask = function.length - 1;
	}

	@Override
	public BaseArray baseArray() {

		return this.function[0].baseArray();
	}

	@Override
	public String baseClass() {

		return this.function[0].baseClass();
	}

	@Override
	public void baseClear() {

		for (final BaseFunction item : this.function) {
			item.baseClear();
		}
	}

	@Override
	public BaseFunction baseConstruct() {

		return this.function[0];
	}

	@Override
	public BaseObject baseConstructPrototype() {

		return this.function[0].baseConstructPrototype();
	}

	@Override
	public int execArgumentsAcceptable() {

		return this.function[0].execArgumentsAcceptable();
	}

	@Override
	public final int execArgumentsDeclared() {

		return this.function[0].execArgumentsDeclared();
	}

	@Override
	public int execArgumentsMinimal() {

		return this.function[0].execArgumentsMinimal();
	}

	@Override
	public ExecStateCode execCallImpl(final ExecProcess context) throws Exception {

		final int serial = --this.serial;
		return this.function[serial & this.mask].execCallPrepare( //
				context,
				context.rb4CT,
				context.riCallResultHandler,
				true,
				context.contextGetArguments()//
		);
	}

	@Override
	public String[] execFormalParameters() {

		return this.function[0].execFormalParameters();
	}

	@Override
	public boolean execHasNamedArguments() {

		return this.function[0].execHasNamedArguments();
	}

	@Override
	public boolean execIsConstant() {

		return this.function[0].execIsConstant();
	}

	@Override
	public Class<? extends Object> execResultClassJava() {

		return this.function[0].execResultClassJava();
	}

	@Override
	public BaseObject execScope() {

		return this.function[0].execScope();
	}

	@Override
	public String toString() {

		return "[function " + this.getClass().getSimpleName() + Arrays.toString(this.function) + "]";
	}
}
