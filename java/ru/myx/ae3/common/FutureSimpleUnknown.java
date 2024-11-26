package ru.myx.ae3.common;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.BaseFuture;
import ru.myx.ae3.base.BaseFutureAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx
 *
 * @param <V> */
@ReflectionManual
public class FutureSimpleUnknown<V> extends BaseFutureAbstract<V> implements BaseFuture.Completable<V> {

	static BaseObject PROTOTYPE = Reflect.classToBasePrototype(FutureSimpleUnknown.class);

	private volatile V result = null;

	private volatile Object error = null;

	private volatile boolean loadDone = false;

	private long hashCode = 0xF000000000000000L;
	
	/**
	 *
	 */
	@ReflectionExplicit
	public FutureSimpleUnknown() {

		//
	}
	
	private final int createHashCode() {

		final Object o = this.baseValue();
		if (o == null) {
			return 0;
		}
		return (int) (this.hashCode = o.hashCode());
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
	public BaseObject baseFutureType() {
		
		return FutureSimpleUnknown.PROTOTYPE;
	}

	@Override
	public final V baseValue() {

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

	@Override
	public final boolean equals(final Object anotherObject) {

		return anotherObject == this || anotherObject != null && anotherObject.equals(this.baseValue());
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

	@Override
	public void setError(final Object error) {

		// System.out.println(">>> >>> setError: thisClass: " +
		// this.getClass().getName() + ", dump: " +
		// Format.Describe.toEcmaSource(error, ""));

		assert error != null : "Error shouldn't be null";
		this.error = error;
		this.loadDone = true;
		/** TODO: is there a point? Why not just inline? */
		Act.launchNotifyAll(this);
	}

	@Override
	public void setResult(final V result) {

		// System.out.println(">>> >>> setResult: thisClass: " +
		// this.getClass().getName() + ", dump: " +
		// Format.Describe.toEcmaSource(result, ""));

		this.result = result;
		this.loadDone = true;
		/** TODO: is there a point? Why not just inline? */
		Act.launchNotifyAll(this);
	}

	final FutureSimpleUnknown<V> getReference() {

		if (this.loadDone) {
			return this;
		}
		synchronized (this) {
			if (!this.loadDone) {
				/** Normally should be:
				 *
				 * <pre>
				 * try {
				 * 	this.wait(60_000L);
				 * } catch (final InterruptedException e) {
				 * 	return null;
				 * }
				 * </pre>
				 */
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
							long left = 60_000L, expires = Engine.fastTime() + left; //
							left > 0; //
							left = expires - Engine.fastTime()) {
						//
						this.wait(left);
						if (this.loadDone) {
							return this;
						}
					}
				} catch (final InterruptedException e) {
					this.error = e;
					this.loadDone = true;
					return null;
				}
			}
		}
		if (this.loadDone) {
			return this;
		}
		{
			final WaitTimeoutException timeout = new WaitTimeoutException(//
					"Wait timeout (hash=" + System.identityHashCode(this) //
							+ ", class=" + this.getClass().getSimpleName() //
							+ ", result=" + this.result //
							+ ", error=" + this.error + ", loadDone=true, hashCode=" + this.hashCode + ")!");
			this.error = new Error(timeout);
			this.loadDone = true;
			return this;
		}
	}
}
