/**
 *
 */
package ru.myx.jdbc.queueing;

import java.util.function.Function;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.common.FutureValue;
import ru.myx.ae3.common.Holder;

/** @author myx
 * @param <T>
 * @param <C> */
public abstract class RequestAttachment<T, C extends RunnerDatabaseRequestor> implements Function<C, T>, Holder<T>, FutureValue<T> {

	private T result;

	private Error error;

	private boolean loadDone = false;

	private RequestAttachment<T, C> loadResult = null;

	private long hashCode = 0xF000000000000000L;

	@Override
	public final T baseValue() {
		
		return this.getReference().result;
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

	@Override
	public final boolean execCanSet() {
		
		return !this.loadDone;
	}

	@Override
	public final boolean execCompareAndSet(final T compare, final T value) {
		
		if (this.result == compare) {
			this.result = value;
			this.loadResult = this;
			this.loadDone = true;
			/** TODO: is there a point? Why not just inline? */
			Act.launchNotifyAll(this);
			return true;
		}
		return false;
	}

	@Override
	public final T execGetAndSet(final T value) {
		
		try {
			return this.result;
		} finally {
			this.result = value;
			this.loadResult = this;
			this.loadDone = true;
			/** TODO: is there a point? Why not just inline? */
			Act.launchNotifyAll(this);
		}
	}

	@Override
	public final void execSet(final T value) {
		
		this.result = value;
		this.loadResult = this;
		this.loadDone = true;
		/** TODO: is there a point? Why not just inline? */
		Act.launchNotifyAll(this);
	}

	/** If key is not NULL - runner will try not to execute same request more than once in one loop.
	 *
	 * @return key for comparsion */
	public abstract String getKey();

	/** @return linkData */
	public final RequestAttachment<T, C> getReference() {
		
		if (this.loadDone) {
			if (this.error != null) {
				throw this.error;
			}
			return this.loadResult;
		}
		synchronized (this) {
			if (this.loadDone) {
				if (this.error != null) {
					throw this.error;
				}
				return this.loadResult;
			}
			if (this.loadResult == null && this.error == null) {
				try {
					/** I AM NOT PARANOID.
					 *
					 *
					 * Taken from Javadoc for 'wait' method:
					 *
					 * A thread can also wake up without being notified, interrupted, or timing out,
					 * a so-called spurious wakeup. While this will rarely occur in practice,
					 * applications must guard against it by testing for the condition that should
					 * have caused the thread to be awakened, and continuing to wait if the
					 * condition is not satisfied. In other words, waits should always occur in
					 * loops, like this one:
					 *
					 * synchronized (obj) { while (<condition does not hold>) obj.wait(timeout); ...
					 * // Perform action appropriate to condition }
					 *
					 * (For more information on this topic, see Section 3.2.3 in Doug Lea's
					 * "Concurrent Programming in Java (Second Edition)" (Addison-Wesley, 2000), or
					 * Item 50 in Joshua Bloch's "Effective Java Programming Language Guide"
					 * (Addison-Wesley, 2001). */
					for (//
							long left = 60000L, expires = Engine.fastTime() + left; //
							left > 0; //
							left = expires - Engine.fastTime()) {
						//
						this.wait(left);
						if (this.loadDone) {
							if (this.error != null) {
								throw this.error;
							}
							return this.loadResult;
						}
						if (this.loadResult != null || this.error != null) {
							break;
						}
					}
				} catch (final InterruptedException e) {
					return null;
				}
			}
		}
		if (this.loadResult == null) {
			throw new DatabaseWaitTimeoutException("Wait timeout (hash=" + System.identityHashCode(this) + ")!", this);
		}
		if (this.loadResult == this) {
			return this;
		}
		this.loadResult = this.loadResult.getReference();
		this.loadDone = true;
		return this.loadResult;
	}

	/** @return */
	protected final T getResult() {
		
		return this.result;
	}

	@Override
	public final int hashCode() {
		
		return this.hashCode == 0xF000000000000000L
			? this.createHashCode()
			: (int) this.hashCode;
	}

	@Override
	public final boolean isDone() {
		
		return this.loadResult == this && this.loadDone || this.loadResult != null && this.loadResult.isDone();
	}

	@Override
	public boolean isFailed() {
		
		return this.loadResult == this && this.loadDone && this.error != null || this.loadResult != null && this.loadResult.isFailed();
	}

	/** @return is this request still valid? */
	public boolean isValid() {
		
		return !this.loadDone;
	}

	/** @param link */
	public final void setDuplicateOf(final RequestAttachment<T, C> link) {
		
		this.loadResult = link;
		this.loadDone = true;
		synchronized (this) {
			this.notifyAll();
		}
	}

	/** @param result */
	protected final void setError(final Error result) {
		
		this.error = result;
		this.loadResult = this;
		this.loadDone = true;
		/** TODO: is there a point? Why not just inline? */
		Act.launchNotifyAll(this);
	}

	/** @param result */
	protected final void setResult(final T result) {
		
		this.result = result;
		this.loadResult = this;
		this.loadDone = true;
		/** TODO: is there a point? Why not just inline? */
		Act.launchNotifyAll(this);
	}

	@Override
	public final String toString() {
		
		return String.valueOf(this.baseValue());
	}
}
