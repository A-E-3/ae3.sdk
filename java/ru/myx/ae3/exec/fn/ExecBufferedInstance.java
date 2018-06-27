package ru.myx.ae3.exec.fn;

import java.util.LinkedList;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArgumentsListXWrapped;
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
public final class ExecBufferedInstance extends BaseFunctionAbstract implements FunctionExecutor, ExecCallableBoth.ExecStoreX, Runnable {
	
	private final ControlFieldset<?> argumentsFieldset;
	
	private final LinkedList<RecordBufferedInstance>[] buffers = Convert.Array.toAny(new LinkedList[]{
			new LinkedList<RecordBufferedInstance>(), new LinkedList<RecordBufferedInstance>()
	});
	
	private final ProgramPart code;
	
	private volatile int counter = 0;
	
	private final ExecProcess ctx;
	
	private final long delay;
	
	private final long period;
	
	private final BaseObject scope;
	
	private final String sourceURI;
	
	private volatile boolean started = false;
	
	private boolean stopped = false;
	
	/** @param sourceURI
	 * @param arguments
	 * @param code
	 * @param delay
	 * @param period
	 * @param parentContext */
	public ExecBufferedInstance(
			final String sourceURI,
			final ControlFieldset<?> arguments,
			final ProgramPart code,
			final long delay,
			final long period,
			final ExecProcess parentContext) {
		this.sourceURI = sourceURI;
		this.argumentsFieldset = arguments;
		this.code = code;
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
		final BaseObject context = this.argumentsFieldset == null
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
				+ Format.Describe.toEcmaSource( context, "" ) );
				 * </code> */
		final RecordBufferedInstance record = new RecordBufferedInstance(instance, context);
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
			final LinkedList<RecordBufferedInstance> buffer;
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
				final int basePointer = this.ctx.ri0BSB;
				final LinkedList<BaseObject> current = new LinkedList<>();
				
				for (BaseObject rb4CT = null;;) {
					final RecordBufferedInstance record;
					if (buffer.isEmpty()) {
						if (current.isEmpty()) {
							break;
						}
						record = null;
					} else {
						record = buffer.removeFirst();
					}
					if (!current.isEmpty() && (record == null || rb4CT != record.object && (rb4CT == null || !rb4CT.equals(record.object)))) {
						final BaseArray array = Base.forArray(current);
						{
							final BaseObject context = this.ctx.vmScopeDeriveContext(this.scope);
							context.baseDefine(this.argumentsFieldset == null
								? "data"
								: "arguments", array, BaseProperty.ATTRS_MASK_WEN);
						}
						try {
							this.code.callNEX(this.ctx, rb4CT, ExecArgumentsListXWrapped.createArguments(array));
						} catch (final Throwable t) {
							Report.exception("TN_METHOD/BUFFERED", "While running buffered method (" + this.sourceURI + ")", t);
						} finally {
							this.ctx.ri0ASP = basePointer;
							this.ctx.ri0BSB = basePointer;
						}
						current.clear();
					}
					if (record != null) {
						rb4CT = record.object;
						current.add(record.map);
					}
				}
			} catch (final Throwable t) {
				Report.exception("TN_METHOD/BUFFERED", "While preparing buffered method (" + this.sourceURI + ")", t);
			} finally {
				this.ctx.rb4CT = null;
				this.ctx.rb7FV = null;
				this.ctx.ri10GV = null;
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
	public final String toString() {
		
		return "ExecBufferedInstance [src=" + this.sourceURI + "]";
	}
}
