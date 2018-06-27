package ru.myx.ae3.common;

import ru.myx.ae3.act.Act;
import java.util.function.Function;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFutureAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 * @author myx
 *
 * @param <V>
 *
 *
 */
@ReflectionManual
public class FutureCallParallel<V> extends BaseFutureAbstract<Boolean> {
	
	static {
		Reflect.classToBasePrototype(FutureCallParallel.class);
	}

	static class Runner implements Function<BaseFunction, Boolean> {
		
		private final FutureCallParallel<?> parent;

		Runner(final FutureCallParallel<?> parent) {

			this.parent = parent;
		}

		@Override
		public Boolean apply(final BaseFunction function) {
			
			try {
				function.callVJ0(BaseObject.UNDEFINED);
				return Boolean.TRUE;
			} catch (final Throwable t) {
				this.parent.error = t;
				return Boolean.FALSE;
			} finally {
				synchronized (this.parent) {
					if (--this.parent.counter == 0) {
						this.parent.loadDone = true;
					}
					this.parent.notifyAll();
				}
			}
		}
	}

	int counter = 0;

	private final Runner runner;

	volatile Object error = null;

	volatile boolean loadDone = false;

	/**
	 * @param process
	 * @param array
	 *
	 */
	@ReflectionExplicit
	public FutureCallParallel(final ExecProcess process, final BaseArray array) {

		final int length = array.length();

		this.counter = length;
		this.runner = new Runner(this);

		int launched = 0, i = 0;

		for (; i < length; i++) {
			final BaseObject object = array.baseGet(i, BaseObject.UNDEFINED);
			if (object == BaseObject.UNDEFINED) {
				synchronized (this) {
					--this.counter;
				}
				continue;
			}
			final BaseFunction function = object.baseCall();
			if (function == null) {
				throw new IllegalArgumentException("Not a function: " + Format.Describe.toDescription(object, ""));
			}

			final ExecProcess ctx = Exec.createProcess(process, "parallel: " + process.contextGetDebug());
			ctx.vmScopeDeriveLocals(process);

			++launched;

			Act.launch(ctx, this.runner, function);
		}

		/**
		 * no need for notifyAll() - object is just being created no one is
		 * listening.
		 */
		if (launched == 0) {
			this.loadDone = true;
		} else {
			synchronized (this) {
				if (!this.loadDone && this.counter == 0) {
					this.loadDone = true;
				}
			}
		}
	}

	@Override
	public final Throwable baseError() {
		
		if (this.loadDone) {
			if (this.error != null) {
				return FutureValue.throwTaskFailedError(this.error, this);
			}
			return null;
		}
		this.getReference();
		if (this.error != null) {
			return FutureValue.throwTaskFailedError(this.error, this);
		}
		return null;
	}

	@Override
	public final Boolean baseValue() {
		
		if (this.loadDone) {
			if (this.error != null) {
				throw FutureValue.throwTaskFailedError(this.error, this);
			}
			return Boolean.TRUE;
		}
		this.getReference();
		if (this.error != null) {
			throw FutureValue.throwTaskFailedError(this.error, this);
		}
		return Boolean.TRUE;
	}

	final FutureCallParallel<V> getReference() {
		
		if (this.loadDone) {
			if (this.error != null) {
				throw FutureValue.throwTaskFailedError(this.error, this);
			}
			return this;
		}
		synchronized (this) {
			if (!this.loadDone) {
				try {
					for (;;) {
						if (this.counter == 0) {
							this.loadDone = true;
							break;
						}
						this.wait(5000L);
						if (this.loadDone) {
							break;
						}
					}
				} catch (final InterruptedException e) {
					return null;
				}
			}
		}
		if (this.loadDone) {
			if (this.error != null) {
				throw FutureValue.throwTaskFailedError(this.error, this);
			}
			return this;
		}
		{
			final WaitTimeoutException timeout = new WaitTimeoutException(
					"Wait timeout (hash=" + System.identityHashCode(this) + ", class=" + this.getClass().getSimpleName() + ", error=" + this.error + ", loadDone=" + this.loadDone
							+ ")!");
			this.error = new Error(timeout);
			this.loadDone = true;
			throw timeout;
		}
	}

	@Override
	public final boolean isDone() {
		
		return this.loadDone;
	}

	@Override
	public boolean isFailed() {
		
		return this.loadDone && this.error != null;
	}

	/**
	 * @param result
	 */
	@ReflectionExplicit
	public void setError(final Object result) {
		
		assert result != null : "Error shouldn't be null";
		this.error = result;
		this.loadDone = true;
		/**
		 * TODO: is there a point? Why not just inline?
		 */
		Act.launchNotifyAll(this);
	}

	@Override
	public String toString() {
		
		return "[future " + this.getClass().getSimpleName() + "(" + (this.loadDone
			? this.error != null
				? "failed"
				: "finished"
			: this.counter + " tasks left") + ")]";
	}

	/**
	 *
	 * @return
	 */
	@ReflectionExplicit
	public final Boolean waitAll() {
		
		return this.baseValue();
	}
}
