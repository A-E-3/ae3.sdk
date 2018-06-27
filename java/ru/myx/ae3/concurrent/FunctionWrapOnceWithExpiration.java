package ru.myx.ae3.concurrent;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseAbstractException;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableJava;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx */
@ReflectionManual
public final class FunctionWrapOnceWithExpiration extends BaseFunctionAbstract implements ExecCallableJava.NativeE0, ExecCallable.ForStore.UseStore0 {

	/** MAKING : result == null
	 *
	 * FAILED : expiration negative
	 *
	 * RETURN : result != null && expiration is OK */

	private final BaseFunction function;
	private final long expirationMillis;

	private BaseObject result = BaseObject.UNDEFINED;
	private long expires = 0;

	/** @param function
	 * @param expirationMillis */
	@ReflectionExplicit
	public FunctionWrapOnceWithExpiration(final BaseFunction function, final long expirationMillis) {
		
		if (expirationMillis <= 0) {
			throw new IllegalArgumentException("expirationMillis is too small: " + expirationMillis);
		}
		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
		}
		if (function.execArgumentsDeclared() != 0) {
			throw new IllegalArgumentException("Function should have no arguments declared, use '.bind' to provide static arguments!");
		}
		this.function = function;
		this.expirationMillis = expirationMillis;
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

	//

	@Override
	public BaseObject baseConstructPrototype() {

		return this.function.baseConstructPrototype();
	}

	@Override
	public BaseObject callNE0(final ExecProcess ctx, final BaseObject instance) {

		final long checkTime = Engine.fastTime();

		/** fast path */
		{
			final BaseObject result = this.result;
			final long expires = this.expires;
			if (result != null) {
				if (expires > checkTime) {
					return this.result;
				}
				if (-expires > checkTime) {
					throw (BaseAbstractException) result;
				}
			}
		}
		{
			synchronized (this) {
				for (;;) {
					final BaseObject result = this.result;
					if (result == null) {
						try {
							this.wait(1000L);
							continue;
						} catch (final InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
					final long expires = this.expires;
					if (expires > checkTime) {
						return this.result;
					}
					if (-expires > checkTime) {
						throw (BaseAbstractException) result;
					}

					/** result expired, make new */
					this.result = null;
					break;
				}
			}
			/** end of sync */

			try {
				final BaseObject result = this.function.callNE0(ctx, instance);
				synchronized (this) {
					this.expires = Engine.fastTime() + this.expirationMillis;
					this.result = result;
					this.notifyAll();
				}
				return result;
			} catch (final Exception e) {
				final BaseAbstractException result = Base.forThrowable(e);
				synchronized (this) {
					this.expires = -(Engine.fastTime() + this.expirationMillis);
					this.result = result;
					this.notifyAll();
				}
				throw result;
			}
		}
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {

		final long checkTime = Engine.fastTime();

		/** fast path */
		{
			final BaseObject result = this.result;
			final long expires = this.expires;
			if (result != null) {
				if (expires > checkTime) {
					return store.execReturn(ctx, this.result);
				}
				if (-expires > checkTime) {
					ctx.ra0RB = result;
					return ExecStateCode.ERROR;
				}
			}
		}
		{
			synchronized (this) {
				for (;;) {
					final BaseObject result = this.result;
					if (result == null) {
						try {
							this.wait(1000L);
							continue;
						} catch (final InterruptedException e) {
							return ctx.vmRaise(e);
						}
					}
					final long expires = this.expires;
					if (expires > checkTime) {
						return store.execReturn(ctx, this.result);
					}
					if (-expires > checkTime) {
						ctx.ra0RB = result;
						return ExecStateCode.ERROR;
					}

					/** result expired, make new */
					this.result = null;
					break;
				}
			}
			/** end of sync */

			try {
				final BaseObject result = this.function.callNE0(ctx, instance);
				synchronized (this) {
					this.expires = Engine.fastTime() + this.expirationMillis;
					this.result = result;
					this.notifyAll();
				}
				return store.execReturn(ctx, this.result);
			} catch (final Exception e) {
				final BaseObject result = Base.forThrowable(e);
				synchronized (this) {
					this.expires = -(Engine.fastTime() + this.expirationMillis);
					this.result = result;
					this.notifyAll();
				}
				ctx.ra0RB = result;
				return ExecStateCode.ERROR;
			}
		}
	}

	@Override
	public boolean execIsConstant() {

		return true;
	}

	@Override
	public Class<? extends Object> execResultClassJava() {

		final BaseFunction function = this.function;
		return function == null
			? Object.class
			: function.execResultClassJava();
	}

	@Override
	public BaseObject execScope() {

		final BaseFunction function = this.function;
		return function == null
			? ExecProcess.GLOBAL
			: function.execScope();
	}

	@Override
	public String toString() {

		final BaseObject result = this.result;
		final long expires = this.expires;
		final BaseFunction function = this.function;
		if (result != null) {
			if (expires < 0) {
				if (-expires > Engine.fastTime()) {
					return "[function " + this.getClass().getSimpleName() + "(FAILED)]";
				}
				return "[function " + this.getClass().getSimpleName() + "(FAILED/EXPIRED)]";
			}
			if (expires > Engine.fastTime()) {
				return "[function " + this.getClass().getSimpleName() + "(DONE)]";
			}
			return "[function " + this.getClass().getSimpleName() + "(DONE/EXPIRED)]";
		}
		if (expires > 0) {
			return "[function " + this.getClass().getSimpleName() + "(MAKING)]";
		}
		return "[function " + this.getClass().getSimpleName() + '(' + function + ")]";
	}
}
