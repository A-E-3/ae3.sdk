package ru.myx.ae3.exec.fn;

import ru.myx.ae3.base.BaseAbstractException;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ControlType;

/*
 * Created on 16.01.2005
 */
/** @author myx */
public final class ExecAlwaysStatic extends BaseFunctionAbstract implements ExecCallableBoth.ExecStoreX {
	
	private final ControlFieldset<?> argumentsFieldset;

	private final ProgramPart code;

	private final ExecProcess parentContext;

	private final ControlType<?, ?> result;

	private final BaseObject staticInstance;

	/** @param instance
	 * @param result
	 * @param arguments
	 * @param code
	 * @param parentContext */
	public ExecAlwaysStatic(
			final BaseObject instance,
			final ControlType<?, ?> result,
			final ControlFieldset<?> arguments,
			final ProgramPart code,
			final ExecProcess parentContext) {
		this.staticInstance = instance;
		this.argumentsFieldset = arguments;
		this.code = code;
		this.result = result;
		this.parentContext = parentContext;
	}

	@Override
	public final int execArgumentsAcceptable() {
		
		return this.argumentsFieldset == null
			? Integer.MAX_VALUE
			: this.argumentsFieldset.size();
	}

	@Override
	public final int execArgumentsDeclared() {
		
		return this.argumentsFieldset == null
			? 0
			: this.argumentsFieldset.size();
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseArray arguments) {
		
		if (this.code == null) {
			return store.execReturnUndefined(ctx);
		}
		try {
			final BaseObject context = ControlFieldset.argumentsFieldsetContextToLocals(//
					this.argumentsFieldset,
					ctx,
					arguments,
					BaseObject.createObject(this.parentContext.rb7FV)//
			);
			ctx.vmFrameEntryExCall(true, this.staticInstance, this, arguments, ResultHandler.FA_BNN_NXT);
			ctx.rb7FV = ctx.ri10GV = context;
			return store.execReturn(
					ctx, //
					this.result.convertAnyNativeToNative(//
							this.code.execCallPreparedInilne(ctx)//
					)//
			);
		} catch (final BaseAbstractException e) {
			ctx.ra0RB = e;
			return ExecStateCode.ERROR;
		}
	}

	@Override
	public Class<?> execResultClassJava() {
		
		return this.result.getJavaClass();
	}

	@Override
	public BaseObject execScope() {
		
		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}
}
