package ru.myx.ae3.concurrent;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArgumentsLinkedList;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;
import ru.myx.ae3.report.Report;

/** @author myx */
@ReflectionManual
public final class FunctionWrapBuffered extends BaseFunctionAbstract implements ExecCallableBoth.ExecStoreX, Runnable {
	
	static class BufferedArguments extends ExecArgumentsLinkedList {
		
		private static final long serialVersionUID = -6368502895839729589L;
		
		/** @param callee */
		BufferedArguments(final BaseFunction callee) {
			
			//
		}
		
	}
	
	private final ControlFieldset<?> argumentsFieldset;
	
	private final BufferedArguments[] buffers;
	
	private final BaseFunction function;
	
	private final ExecProcess ctx;
	
	private volatile int counter = 0;
	
	private final long delay;
	
	private final long period;
	
	private volatile int remaining;
	
	private final int capacity;
	
	private final boolean overflowDrop;
	
	private volatile byte started = (byte) 0;
	
	private volatile boolean stopped = false;
	
	/** @param parent
	 * @param function
	 * @param options */
	@ReflectionExplicit
	public FunctionWrapBuffered(final ExecProcess parent, final BaseFunction function, final BaseObject options) {
		
		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
		}
		if (function.execArgumentsDeclared() == 0) {
			throw new IllegalArgumentException("Function should have at least one ('queue') argument declared!");
		}
		this.argumentsFieldset = null;
		this.buffers = new BufferedArguments[]{
				new BufferedArguments(function), new BufferedArguments(function),
				//
		};
		
		this.delay = Base.getInt(options, "delay", 0);
		this.period = Base.getInt(options, "period", 0);
		this.capacity = Base.getInt(options, "buffer", -1);
		this.remaining = this.capacity;
		this.overflowDrop = Base.getString(options, "overflow", "block").equals("drop");
		
		this.ctx = Exec.createProcess(parent, "Buffered method, fn=" + function);
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
	
	/**
	 *
	 */
	@ReflectionExplicit
	public void destroy() {
		
		this.stopped = true;
	}
	
	@Override
	public int execArgumentsAcceptable() {
		
		return this.argumentsFieldset == null
			? this.function.execArgumentsAcceptable()
			: this.argumentsFieldset.size();
	}
	
	@Override
	public final int execArgumentsDeclared() {
		
		return this.argumentsFieldset == null
			? this.function.execArgumentsDeclared()
			: this.argumentsFieldset.size();
	}
	
	@Override
	public int execArgumentsMinimal() {
		
		return this.argumentsFieldset == null
			? this.function.execArgumentsMinimal()
			: 0;
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseArray arguments) {
		
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
		drop : {
			retry : for (;;) {
				synchronized (this.buffers) {
					if (this.stopped) {
						return ctx.vmRaise("Subsystem is stopped!");
					}
					if (this.capacity != -1) {
						if (this.remaining == 0 || --this.remaining == 0) {
							switch (this.started) {
								case 0 :
								case 1 :
									this.buffers[this.counter & 0x01].add(record);
									Act.launch(this.ctx, this);
									this.started = 2;
									break retry;
								default :
									if (this.overflowDrop) {
										break drop;
									}
									try {
										this.buffers.wait(1_000L);
										continue retry;
									} catch (final InterruptedException e) {
										return store.execReturnFalse(ctx);
									}
							}
						}
					}
					this.buffers[this.counter & 0x01].add(record);
					if (this.started == 0) {
						if (this.delay > 0L) {
							Act.later(this.ctx, (Runnable) this, this.delay);
							this.started = 1;
						} else {
							Act.whenIdle(this.ctx, this);
							this.started = 2;
						}
					}
					break retry;
				}
			}
			return store.execReturnTrue(ctx);
		}
		return store.execReturnFalse(ctx);
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
		
		return Void.class;
	}
	
	@Override
	public BaseObject execScope() {
		
		return ExecProcess.GLOBAL;
	}
	
	@Override
	public final void run() {
		
		synchronized (this.buffers) {
			if (this.started > 2) {
				return;
			}
			this.started = 3;
		}
		for (;;) {
			final ExecArgumentsLinkedList buffer;
			synchronized (this.buffers) {
				buffer = this.buffers[this.counter & 0x01];
				this.counter++;
				/** assignment in condition */
				if ((this.remaining = this.capacity) != -1) {
					this.buffers.notifyAll();
				}
				if (buffer.isEmpty()) {
					if (this.period > 0L) {
						if (!this.stopped) {
							Act.later(this.ctx, (Runnable) this, this.period);
							this.started = 1;
							return;
						}
					}
					this.started = 0;
					return;
				}
			}
			try {
				{
					final BaseObject context = this.ctx.vmScopeDeriveContext(this.function.execScope());
					if (this.argumentsFieldset == null) {
						context.baseDefine("data", buffer, BaseProperty.ATTRS_MASK_WEN);
					}
				}
				try {
					this.function.callVE1(this.ctx, BaseObject.UNDEFINED, buffer);
				} catch (final Throwable t) {
					Report.exception("FN-WRAP-BUFFERED", "While running buffered method (fn = " + this.function + ")", t);
				}
			} catch (final Throwable t) {
				Report.exception("FN-WRAP-BUFFERED", "While preparing buffered method (fn = " + this.function + ")", t);
			}
			buffer.clear();
		}
	}
	
	@Override
	public String toString() {
		
		return "[function " + this.getClass().getSimpleName() + '(' + this.function + ")]";
	}
}
