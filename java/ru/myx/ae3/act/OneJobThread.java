/*
 * Created on 12.12.2005
 */
package ru.myx.ae3.act;

import java.util.function.Function;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Format;

/** @author myx
 * @param <T>
 *            context class */
public class OneJobThread<T> extends ContextThread<T> {

	private static final Function<Object, Object> NULL_JOB = new Function<>() {

		@Override
		public Object apply(final Object arg) {

			throw new RuntimeException("Not setup!");
		}

		@Override
		public String toString() {

			return "NULL_JOB";
		}
	};

	private final Object arg;

	private final Function<Object, Object> job;

	/** @param ctx
	 *            context to use */
	public OneJobThread(final T ctx) {
		super(ctx);
		this.job = OneJobThread.NULL_JOB;
		this.arg = null;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param job
	 * @param arg */
	public <A, R> OneJobThread(final T ctx, final Function<A, R> job, final A arg) {
		super(ctx);
		this.job = Convert.Any.toAny(job);
		this.arg = arg;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param job
	 * @param arg
	 * @param name */
	public <A, R> OneJobThread(final T ctx, final Function<A, R> job, final A arg, final String name) {
		super(ctx, name);
		this.ctx = ctx;
		this.job = Convert.Any.toAny(job);
		this.arg = arg;
	}

	/** @param ctx
	 *            context to use
	 * @param name */
	public OneJobThread(final T ctx, final String name) {
		super(ctx, name);
		this.ctx = ctx;
		this.job = OneJobThread.NULL_JOB;
		this.arg = null;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param grp
	 * @param job
	 * @param arg */
	public <A, R> OneJobThread(final T ctx, final ThreadGroup grp, final Function<A, R> job, final A arg) {
		super(ctx, grp);
		this.ctx = ctx;
		this.job = Convert.Any.toAny(job);
		this.arg = arg;
	}

	/** @param <A>
	 * @param <R>
	 * @param ctx
	 *            context to use
	 * @param grp
	 * @param job
	 * @param arg
	 * @param name */
	public <A, R> OneJobThread(final T ctx, final ThreadGroup grp, final Function<A, R> job, final A arg, final String name) {
		super(ctx, grp, name);
		this.ctx = ctx;
		this.job = Convert.Any.toAny(job);
		this.arg = arg;
	}

	/** @param ctx
	 *            context to use
	 * @param grp
	 * @param name */
	public OneJobThread(final T ctx, final ThreadGroup grp, final String name) {
		super(ctx, grp, name);
		this.job = OneJobThread.NULL_JOB;
		this.arg = null;
	}

	/** @return currently associated context */
	@Override
	public T getContext() {

		return this.ctx;
	}

	@Override
	public void run() {

		try {
			this.job.apply(this.arg);
		} catch (final Exception e) {
			this.getThreadGroup().uncaughtException(this, e);
		}
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " [ctx=" + this.ctx + ", job=" + this.job + ", arg=" + Format.Describe.toEcmaSource(
				new StringBuilder(), //
				Base.forUnknown(this.arg),
				"",
				true,
				0,
				0) + "]";
	}
}
