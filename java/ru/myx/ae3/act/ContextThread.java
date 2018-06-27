/*
 * Created on 12.12.2005
 */
package ru.myx.ae3.act;

import java.util.function.Function;

/** @author myx
 * @param <T>
 *            context class */
public abstract class ContextThread<T> extends Thread {

	/**
	 *
	 */
	protected T ctx;

	/** @param ctx
	 *            context to use */
	public ContextThread(final T ctx) {
		this.ctx = ctx;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param job
	 * @param arg
	 * @param name */
	public <A, R> ContextThread(final T ctx, final Function<A, R> job, final A arg, final String name) {
		super((Runnable) null, name);
		this.ctx = ctx;
	}

	/** @param ctx
	 *            context to use
	 * @param name */
	public ContextThread(final T ctx, final String name) {
		super(name);
		this.ctx = ctx;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param grp */
	public <A, R> ContextThread(final T ctx, final ThreadGroup grp) {
		super(grp, (Runnable) null);
		this.ctx = ctx;
	}

	/** @param ctx
	 *            context to use
	 * @param grp
	 * @param name */
	public ContextThread(final T ctx, final ThreadGroup grp, final String name) {
		super(grp, name);
		this.ctx = ctx;
	}

	/** @return currently associated context */
	public T getContext() {

		return this.ctx;
	}

	@Override
	public abstract void run();

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " [ctx=" + this.ctx + "]";
	}
}
