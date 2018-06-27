package ru.myx.ae3.exec.fn;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecArgumentsEmpty;
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
public final class ExecOnceStatic extends BaseFunctionAbstract implements ExecCallableBoth.ExecStore0 {
	
	private boolean doing = false;

	private boolean error = false;

	private final BaseObject instance;

	private final ExecProcess parentContext;

	private final ProgramPart renderer;

	private final ControlType<?, ?> result;

	private BaseObject value = null;

	/** @param instance
	 * @param result
	 * @param renderer
	 * @param parentContext */
	public ExecOnceStatic(final BaseObject instance, final ControlType<?, ?> result, final ProgramPart renderer, final ExecProcess parentContext) {
		this.instance = instance;
		this.result = result;
		this.renderer = renderer;
		this.parentContext = parentContext;
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		BaseObject result = this.value;
		if (result == null) {
			boolean make = false;
			synchronized (this) {
				/** need to re-check after 'sync' */
				result = this.value;
				/** still null? */
				if (result == null) {
					/** already doing it, or really have to start now? */
					if (this.doing) {
						do {
							try {
								this.wait(1000L);
							} catch (final InterruptedException e) {
								return ctx.vmRaise(e);
							}
						} while (this.doing);
						result = this.value;
					} else {
						this.doing = true;
						make = true;
					}
				}
			}
			/** end of sync */
			/** so do we have to execute? */
			if (make) {
				/** empty arguments */
				ctx.vmFrameEntryExCall(true, this.instance, this, ExecArgumentsEmpty.INSTANCE, ResultHandler.FA_BNN_NXT);
				ctx.vmScopeDeriveContext(this.parentContext);
				try {
					result = this.renderer.execCallPreparedInilne(ctx);
					assert result != null : "NULL java value";
				} catch (final Exception e) {
					result = Base.forThrowable(e);
					this.error = true;
				}
				this.value = result;
				this.doing = false;
				synchronized (this) {
					this.notifyAll();
				}
			}
		}
		if (this.error) {
			ctx.ra0RB = result;
			return ExecStateCode.ERROR;
		}
		return store.execReturn(ctx, this.result.convertAnyNativeToNative(result));
	}

	@Override
	public final boolean execHasNamedArguments() {
		
		return false;
	}

	@Override
	public final boolean execIsConstant() {
		
		return false;
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
