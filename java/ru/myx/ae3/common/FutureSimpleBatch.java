package ru.myx.ae3.common;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.BaseFutureAbstract;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;
import ru.myx.util.FifoQueueLinked;

/**
 * @author myx
 *
 * @param <V>
 *
 *
 */
@ReflectionManual
public class FutureSimpleBatch<V> extends BaseFutureAbstract<BaseList<V>> {
	
	
	private volatile Object error = null;
	
	private volatile boolean loadDone = false;
	
	private long hashCode = 0xF000000000000000L;
	
	private final FifoQueueLinked<FutureValue<V>> queue = new FifoQueueLinked<>();
	
	private volatile BaseList<V> result;
	
	/**
	 *
	 */
	@ReflectionExplicit
	public FutureSimpleBatch() {
		
		this.result = BaseObject.createArray();
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
	public final BaseList<V> baseValue() {
		
		
		if (this.loadDone) {
			if (this.error != null) {
				throw FutureValue.throwTaskFailedError(this.error, this);
			}
			return this.result;
		}
		this.getReference();
		if (this.error != null) {
			throw FutureValue.throwTaskFailedError(this.error, this);
		}
		return this.result;
	}
	
	private final int createHashCode() {
		
		
		final Object o = this.baseValue();
		if (o == null) {
			return 0;
		}
		return (int) (this.hashCode = o.hashCode());
	}
	
	@Override
	public final boolean equals(final Object anotherObject) {
		
		
		return anotherObject == this || anotherObject != null && anotherObject.equals(this.baseValue());
	}
	
	final FutureSimpleBatch<V> getReference() {
		
		
		if (this.loadDone) {
			return this;
		}
		synchronized (this) {
			if (!this.loadDone) {
				for (//
				long left = 120000L, expires = Engine.fastTime() + left; //
				left > 0; //
				left = expires - Engine.fastTime()) {
					final FutureValue<V> future = this.queue.pollFirst();
					if (future == null) {
						this.loadDone = true;
						break;
					}
					final V value;
					try {
						value = future.baseValue();
					} catch (final Throwable t) {
						this.error = t;
						this.result.add(null);
						continue;
					}
					this.result.add(value);
					continue;
				}
			}
		}
		if (this.loadDone) {
			return this;
		}
		{
			final WaitTimeoutException timeout = new WaitTimeoutException(
					"Wait timeout (hash=" + System.identityHashCode(this) + ", class=" + this.getClass().getSimpleName() + ", result=" + this.result + ", error=" + this.error
							+ ", loadDone=" + this.loadDone + ", hashCode=" + this.hashCode + ")!");
			this.error = new Error(timeout);
			this.loadDone = true;
			return this;
		}
	}
	
	@Override
	public final int hashCode() {
		
		
		return this.hashCode == 0xF000000000000000L
			? this.createHashCode()
			: (int) this.hashCode;
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
	 * @param ctx
	 * @param futureObject
	 */
	@ReflectionExplicit
	public void push(final ExecProcess ctx, final BaseObject futureObject) {
		
		
		if (futureObject instanceof FutureValue<?>) {
			final FutureValue<V> future = Convert.Any.toAny(futureObject);
			this.push(future);
			return;
		}
		final Object valueObject = futureObject.baseValue();
		if (valueObject == null || valueObject == futureObject) {
			final V object = Convert.Any.toAny(futureObject);
			this.result.add(object);
			return;
		}
		if (valueObject instanceof FutureValue<?>) {
			final FutureValue<V> future = Convert.Any.toAny(valueObject);
			this.push(future);
			return;
		}
		{
			final V object = Convert.Any.toAny(valueObject);
			this.result.add(object);
			return;
		}
	}
	
	/**
	 * @param future
	 */
	@ReflectionExplicit
	public void push(final FutureValue<V> future) {
		
		
		synchronized (future) {
			if (future.isDone()) {
				this.result.add(future.baseValue());
				return;
			}
		}
		synchronized (this) {
			this.queue.offerLast(future);
		}
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
	
	/**
	 * @param result
	 */
	@ReflectionExplicit
	public void setResult(final BaseList<V> result) {
		
		
		this.result = result;
		this.loadDone = true;
		/**
		 * TODO: is there a point? Why not just inline?
		 */
		Act.launchNotifyAll(this);
	}
}
