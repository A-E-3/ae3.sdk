package ru.myx.ae3.common;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.BaseFutureAbstract;
import ru.myx.ae3.base.BaseNativeError;
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
public class FutureMergeable<V> extends BaseFutureAbstract<V> {
	private volatile V					result		= null;
	
	private volatile Object				error		= null;
	
	private volatile boolean			loadDone	= false;
	
	private volatile FutureMergeable<V>	loadResult	= null;
	
	private long						hashCode	= 0xF000000000000000L;
	
	
	@Override
	public final Throwable baseError() {
	
		if (this.loadResult == this) {
			if (this.error != null) {
				return FutureValue.throwTaskFailedError( this.error, this );
			}
			return null;
		}
		return this.getReference().baseError();
	}
	
	
	@Override
	public final V baseValue() {
	
		if (this.loadResult == this) {
			if (this.error != null) {
				throw FutureValue.throwTaskFailedError( this.error, this );
			}
			return this.result;
		}
		
		return this.getReference().baseValue();
	}
	
	
	private final int createHashCode() {
	
		final Object o = this.baseValue();
		if (o == null) {
			return 0;
		}
		return (int) (this.hashCode = o.hashCode());
	}
	
	
	@Override
	public final boolean equals(
			final Object anotherObject) {
	
		return anotherObject == this || anotherObject != null && anotherObject.equals( this.baseValue() );
	}
	
	
	final FutureMergeable<V> getReference() {
	
		if (this.loadDone) {
			return this.loadResult;
		}
		synchronized (this) {
			if (!this.loadDone) {
				try {
					/**
					 * I AM NOT PARANOID.
					 *
					 *
					 * Taken from Javadoc for 'wait' method:
					 *
					 * A thread can also wake up without being notified,
					 * interrupted, or timing out, a so-called spurious wakeup.
					 * While this will rarely occur in practice, applications
					 * must guard against it by testing for the condition that
					 * should have caused the thread to be awakened, and
					 * continuing to wait if the condition is not satisfied. In
					 * other words, waits should always occur in loops, like
					 * this one:
					 *
					 * synchronized (obj) { while (<condition does not hold>)
					 * obj.wait(timeout); ... // Perform action appropriate to
					 * condition }
					 *
					 * (For more information on this topic, see Section 3.2.3 in
					 * Doug Lea's
					 * "Concurrent Programming in Java (Second Edition)"
					 * (Addison-Wesley, 2000), or Item 50 in Joshua Bloch's
					 * "Effective Java Programming Language Guide"
					 * (Addison-Wesley, 2001).
					 *
					 */
					for (//
					long left = 60000L, expires = Engine.fastTime() + left; //
					left > 0; //
					left = expires - Engine.fastTime()) {
						//
						this.wait( left );
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
			return this.loadResult;
		}
		if (this.loadResult == null) {
			final WaitTimeoutException timeout = new WaitTimeoutException( "Wait timeout (hash="
					+ System.identityHashCode( this )
					+ ", class="
					+ this.getClass().getSimpleName()
					+ ", result="
					+ this.result
					+ ", error="
					+ this.error
					+ ", loadDone="
					+ this.loadDone
					+ ", loadResult="
					+ this.loadResult
					+ ", hashCode="
					+ this.hashCode
					+ ")!" );
			this.error = new BaseNativeError( "timeout", timeout );
			this.loadResult = this;
			this.loadDone = true;
			return this;
		}
		if (this.loadResult == this) {
			return this;
		}
		this.loadResult = this.loadResult.getReference();
		this.loadDone = true;
		return this.loadResult;
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
	
		return this.loadResult == this
				&& this.loadDone
				&& this.error != null
				|| this.loadResult != null
				&& this.loadResult.isFailed();
	}
	
	
	/**
	 * @param depends
	 */
	protected final void setDuplicateOf(
			final FutureMergeable<V> depends) {
	
		assert depends != null : "Dependency shouldn't be null";
		this.loadResult = depends;
		this.loadDone = true;
	}
	
	
	/**
	 * @param result
	 */
	@ReflectionExplicit
	public final void setError(
			final Object result) {
	
		assert result != null : "Error shouldn't be null";
		this.error = result;
		this.loadResult = this;
		this.loadDone = true;
		/**
		 * TODO: is there a point? Why not just inline?
		 */
		Act.launchNotifyAll( this );
	}
	
	
	/**
	 * @param result
	 */
	@ReflectionExplicit
	public final void setResult(
			final V result) {
	
		this.result = result;
		this.loadResult = this;
		this.loadDone = true;
		/**
		 * TODO: is there a point? Why not just inline?
		 */
		Act.launchNotifyAll( this );
	}
	
	
	@Override
	public String toString() {
	
		return String.valueOf( this.getReference().result );
	}
	
}
