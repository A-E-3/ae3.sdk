package ru.myx.ae3.exec.fn;

import java.lang.ref.WeakReference;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArgumentsEmpty;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.report.Report;

/*
 * Created on 27.01.2005
 */
/** @author myx */
public final class ExecAuto extends BaseFunctionAbstract implements FunctionExecutor, ExecCallableBoth.ExecStore0 {
	
	final static class ExecAutoRunnable implements Runnable {
		
		private final WeakReference<ExecAuto> callee;

		private final ExecProcess ctx;

		private boolean first = true;

		private final String sourceURI;

		ExecAutoRunnable(final String sourceURI, final ExecProcess ctx, final ExecAuto callee) {
			this.sourceURI = sourceURI;
			this.callee = new WeakReference<>(callee);
			this.ctx = ctx;
		}

		@Override
		public void run() {
			
			final ExecAuto callee = this.callee.get();
			if (callee == null) {
				if (!this.first) {
					Report.debug("TN_METHOD/AUTO", "Auto method cancelled (" + this.sourceURI + ") seems to be replaced or deleted");
				}
				return;
			}

			if (this.first) {
				this.first = false;
			} else //
			if (callee.period == 0L) {
				Report.debug("TN_METHOD/AUTO", "Auto method cancelled (" + this.sourceURI + ") seems to be stopped");
				return;
			}

			/** TODO: make sure code runs in native stepper context */
			try {
				final int basePointer = this.ctx.ri0ASP;
				try {
					this.ctx.vmFrameEntryExCall(true, BaseObject.UNDEFINED, callee, ExecArgumentsEmpty.INSTANCE, ResultHandler.FA_BNN_NXT);
					this.ctx.rb7FV = this.ctx.ri10GV = BaseObject.createObject(this.ctx.getParentProcess().ri10GV);
					callee.code.execCallPreparedInilne(this.ctx);
				} catch (final Throwable t) {
					Report.exception("TN_METHOD/DEFFERED", "While running deffered method (" + this.sourceURI + ")", t);
				} finally {
					this.ctx.ri0ASP = basePointer;
					this.ctx.ri0BSB = basePointer;
				}
			} catch (final Throwable t) {
				Report.exception("TN_METHOD/AUTO", "While running auto method (" + this.sourceURI + ")", t);
			} finally {
				if (callee.period > 0L) {
					Act.later(this.ctx, this, callee.period);
					Report.debug("TN_METHOD/AUTO", "Auto method (" + this.sourceURI + ") rescheduled for " + Format.Compact.toPeriod(callee.period));
				}
			}
		}

		final void start() {
			
			final ExecAuto callee = this.callee.get();
			if (callee == null) {
				throw new NullPointerException(this.sourceURI + ", callee is null on 'start'!");
			}
			this.ctx.vmScopeCreateSandbox(this.ctx.getParentProcess().ri10GV);
			if (callee.delay == 0L) {
				Act.whenIdle(this.ctx, this);
			} else {
				Act.later(this.ctx, this, callee.delay);
			}
			Report.debug("TN_METHOD/AUTO", "Auto method (" + this.sourceURI + ") activated, delay=" + Format.Compact.toPeriod(callee.delay));
		}

		@Override
		public String toString() {
			
			return "[function ExecAutoRunnable(src:" + this.sourceURI + ")]";
		}
	}

	final ProgramPart code;

	private final ExecProcess ctx;

	final long delay;

	long period;

	private ExecAutoRunnable runnable;

	private final String sourceURI;

	/** @param sourceURI
	 * @param code
	 * @param delay
	 * @param period
	 * @param parentContext */
	public ExecAuto(final String sourceURI, final ProgramPart code, final long delay, final long period, final ExecProcess parentContext) {
		this.sourceURI = sourceURI;
		this.ctx = Exec.createProcess(parentContext, "Auto method, src=" + sourceURI);
		this.code = code;
		this.delay = delay;
		this.period = period;
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		return ctx.vmRaise("Automatic methods can not be explicitly called!");
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
	public Class<Void> execResultClassJava() {
		
		return Void.TYPE;
	}

	@Override
	public BaseObject execScope() {
		
		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}

	@Override
	public final void start() {
		
		final ExecAutoRunnable runnable = this.runnable;
		if (runnable == null) {
			this.runnable = new ExecAutoRunnable(this.sourceURI, this.ctx, this);
			this.runnable.start();
		}
	}

	@Override
	public void stop() {
		
		final ExecAutoRunnable runnable = this.runnable;
		if (runnable != null) {
			this.runnable = null;
			this.period = 0L;
		}
	}

	@Override
	public final String toString() {
		
		return "[function ExecAuto, src:" + this.sourceURI + ", delay:" + this.delay + "]";
	}
}
