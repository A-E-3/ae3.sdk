package ru.myx.ae3.exec.fn;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.reflect.ControlType;

/*
 * Created on 16.01.2005
 */
/** @author myx */
public final class ExecEmpty extends BaseFunctionAbstract implements ExecCallableBoth.NativeJ0 {

	private final BaseObject result;
	
	/** @param result */
	public ExecEmpty(final ControlType<?, ?> result) {
		this.result = result.convertAnyNativeToNative(BaseObject.UNDEFINED);
	}
	
	@Override
	public BaseObject callNJ0(final BaseObject instance) {

		return this.result;
	}
	
	@Override
	public final boolean execHasNamedArguments() {

		return false;
	}
	
	@Override
	public final boolean execIsConstant() {

		return true;
	}
	
	@Override
	public Class<?> execResultClassJava() {

		return this.result.getClass();
	}
	
	@Override
	public BaseObject execScope() {

		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}
}
