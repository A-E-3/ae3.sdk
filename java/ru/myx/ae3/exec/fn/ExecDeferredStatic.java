package ru.myx.ae3.exec.fn;

import java.util.LinkedList;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArgumentsEmpty;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.report.Report;

/*
 * Created on 16.01.2005
 */
/** @author myx */
public final class ExecDeferredStatic extends BaseFunctionAbstract implements FunctionExecutor, ExecCallableBoth.ExecStoreX, Runnable {
	
	private final ControlFieldset<?> argumentsFieldset;

	private final LinkedList<BaseObject>[] buffers = Convert.Array.toAny(new LinkedList[]{
			new LinkedList<BaseObject>(), new LinkedList<BaseObject>()
	});

	private volatile int counter = 0;

	private final ExecProcess ctx;

	private final long delay;

	private final long period;

	private final ProgramPart renderer;

	private final BaseObject scope;

	private final String sourceURI;

	private volatile boolean started = false;

	private final BaseObject staticInstance;

	private boolean stopped = false;

	/** @param sourceURI
	 * @param instance
	 * @param arguments
	 * @param renderer
	 * @param delay
	 * @param period
	 * @param parentContext */
	public ExecDeferredStatic(
			final String sourceURI,
			final BaseObject instance,
			final ControlFieldset<?> arguments,
			final ProgramPart renderer,
			final long delay,
			final long period,
			final ExecProcess parentContext) {
		this.sourceURI = sourceURI;
		this.staticInstance = instance;
		this.argumentsFieldset = arguments;
		this.renderer = renderer;
		this.delay = delay;
		this.period = period;

		this.ctx = Exec.createProcess(parentContext, "Deffered method, src=" + sourceURI);
		this.scope = Base.seal(parentContext);
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
		
		if (this.stopped) {
			throw new IllegalStateException(this + " was already stopped!");
		}
		// final BaseObject object = ctx.r4RT;
		final BaseObject record = this.argumentsFieldset == null
			? ExecProcess.vmEnsureArgumentsDetached(ctx, arguments)
			: ControlFieldset.argumentsFieldsetContextToLocals(//
					this.argumentsFieldset,
					ctx,
					arguments,
					BaseObject.createObject(this.scope)//
			);
		/** debug<code>
				System.err.println( ">>> >>> "
						+ this.getClass().getSimpleName()
						+ ": enqueued: "
						+ Format.Describe.toEcmaSource( record, "" ) );
						 * </code> */
		synchronized (this.buffers) {
			this.buffers[this.counter & 0x01].add(record);
			if (!this.started) {
				if (this.delay > 0L) {
					Act.later(this.ctx, (Runnable) this, this.delay);
				} else {
					Act.whenIdle(this.ctx, this);
				}
				this.started = true;
			}
		}
		return store.execReturnUndefined(ctx);
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
	public final void run() {
		
		for (;;) {
			final LinkedList<BaseObject> buffer;
			synchronized (this.buffers) {
				buffer = this.buffers[this.counter & 0x01];
				this.counter++;
				if (buffer.isEmpty()) {
					if (this.period > 0L) {
						if (!this.stopped) {
							Act.later(this.ctx, (Runnable) this, this.period);
							return;
						}
					}
					this.started = false;
					return;
				}
			}
			final int basePointer = this.ctx.ri0BSB;
			for (; !buffer.isEmpty();) {
				try {
					final BaseObject map = buffer.removeFirst();
					this.ctx.vmFrameEntryExCall(true, this.staticInstance, this, ExecArgumentsEmpty.INSTANCE, ResultHandler.FA_BNN_NXT);
					this.ctx.rb7FV = this.ctx.ri10GV = this.argumentsFieldset == null
						? BaseObject.createObject(map)
						: map;
					try {
						this.renderer.execCallPreparedInilne(this.ctx);
					} catch (final Throwable t) {
						Report.exception("TN_METHOD/DEFFERED", "While running deffered method (" + this.sourceURI + ")", t);
					} finally {
						this.ctx.ri0ASP = basePointer;
						this.ctx.ri0BSB = basePointer;
					}
				} catch (final Throwable t) {
					Report.exception("TN_METHOD/DEFFERED", "While preparing deffered method (" + this.sourceURI + ")", t);
				}
			}
		}
	}

	@Override
	public final void start() {
		
		// ignore
	}

	@Override
	public final void stop() {
		
		synchronized (this.buffers) {
			this.stopped = true;
		}
	}

	@Override
	public String toString() {
		
		return "ExecDeferredStatic [src=" + this.sourceURI + "]";
	}

}
