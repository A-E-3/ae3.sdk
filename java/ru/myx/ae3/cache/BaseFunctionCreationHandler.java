package ru.myx.ae3.cache;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public class BaseFunctionCreationHandler extends BaseFunctionAbstract implements ExecCallableBoth.ExecStore2, CreationHandlerObject<Object, BaseObject> {
	
	private final BaseObject thisObject;
	
	private final BaseFunction function;
	
	/** @param thisObject
	 * @param function */
	public BaseFunctionCreationHandler(final BaseObject thisObject, final BaseFunction function) {
		this.thisObject = thisObject;
		this.function = function;
		final ExecCallable itself = function;
		if (itself.execArgumentsMinimal() > 2) {
			throw new IllegalArgumentException("fn with maximum 2 arguments expected: callback(attachment, key), given: " + function);
		}
		if (itself.execArgumentsAcceptable() < 2) {
			throw new IllegalArgumentException("fn with minimum 2 arguments expected: callback(attachment, key), given: " + function);
		}
	}
	
	@Override
	public BaseObject create(final Object attachment, final String key) {
		
		return this.function.callNJ2(this.thisObject, Base.forUnknown(attachment), Base.forString(key));
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx,
			final BaseObject instance,
			final ResultHandler store,
			final boolean inline,
			final BaseObject argument1,
			final BaseObject argument2) {
		
		return this.function.execCallPrepare(ctx, this.thisObject, store, inline, argument1, argument2);
	}
	
	@Override
	public boolean execIsConstant() {
		
		return false;
	}
	
	@Override
	public Class<? extends Object> execResultClassJava() {
		
		return Object.class;
	}
	
	@Override
	public BaseObject execScope() {
		
		return this.function.execScope();
	}
	
	@Override
	public long getTTL() {
		
		return Base.getInt(this.function, "TTL", 15000);
	}
	
	@Override
	public String toString() {
		
		return "[function CreationCallback(" + this.function + ")]";
	}
}
