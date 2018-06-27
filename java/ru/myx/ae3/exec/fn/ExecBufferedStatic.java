package ru.myx.ae3.exec.fn;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArgumentsLinkedList;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.report.Report;

/*
 * Created on 16.01.2005
 */
/** @author myx */
public final class ExecBufferedStatic extends BaseFunctionAbstract implements FunctionExecutor, ExecCallableBoth.ExecStoreX, Runnable {

	private final ControlFieldset<?> argumentsFieldset;
	
	private final ExecArgumentsLinkedList[] buffers;
	
	private final ProgramPart code;
	
	private volatile int counter = 0;
	
	private final ExecProcess ctx;
	
	private final long delay;
	
	private final BaseObject instance;
	
	private final long period;
	
	private final BaseObject scope;
	
	private final String sourceURI;
	
	private volatile boolean started = false;
	
	private boolean stopped = false;
	
	/** @param sourceURI
	 * @param instance
	 * @param arguments
	 * @param renderer
	 * @param delay
	 * @param period
	 * @param parentContext */
	public ExecBufferedStatic(
			final String sourceURI,
			final BaseObject instance,
			final ControlFieldset<?> arguments,
			final ProgramPart renderer,
			final long delay,
			final long period,
			final ExecProcess parentContext) {
		this.buffers = new ExecArgumentsLinkedList[]{
				new ExecArgumentsLinkedList(), new ExecArgumentsLinkedList(),
				//
		};
		this.argumentsFieldset = arguments;
		this.sourceURI = sourceURI;
		this.instance = instance;
		this.code = renderer;
		this.delay = delay;
		this.period = period;
		
		this.ctx = Exec.createProcess(parentContext, "Buffered method, src=" + sourceURI);
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
					BaseObject.createObject(null)//
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
			final ExecArgumentsLinkedList buffer;
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
			try {
				{
					final BaseObject context = this.ctx.vmScopeDeriveContext(this.scope);
					if (this.argumentsFieldset == null) {
						context.baseDefine("data", buffer, BaseProperty.ATTRS_MASK_WEN);
					}
				}
				
				try {
					this.code.callNEX(this.ctx, this.instance, buffer);
				} catch (final Throwable t) {
					Report.exception("TN_METHOD/BUFFERED", "While running buffered method (" + this.sourceURI + ")", t);
				}
			} catch (final Throwable t) {
				Report.exception("TN_METHOD/BUFFERED", "While preparing buffered method (" + this.sourceURI + ")", t);
			}
			buffer.clear();
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

		return "ExecBufferedStatic [src=" + this.sourceURI + "]";
	}
}
