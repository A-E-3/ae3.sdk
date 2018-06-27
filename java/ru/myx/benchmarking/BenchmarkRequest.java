package ru.myx.benchmarking;

import ru.myx.ae3.act.Act;
import java.util.function.Function;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;

/**
 * Simple test implementation
 */
public final class BenchmarkRequest implements Function<Benchmark.Result, Object>, Runnable {
	
	private int iterations = 3;

	private double maxs = 0;

	private double maxt = 0;

	private final ExecProcess ctx;

	private final Function<Object, ?> test;

	private final Object argument;

	private final String title;

	/**
	 * @param <T>
	 * @param ctx
	 * @param title
	 * @param test
	 * @param argument
	 */
	@SuppressWarnings("unchecked")
	public <T> BenchmarkRequest(final ExecProcess ctx, final String title, final Function<T, ?> test, final T argument) {
		this.ctx = ctx;
		this.title = title;
		this.test = (Function<Object, ?>) test;
		this.argument = argument;
	}

	/**
	 * @param ctx
	 * @param title
	 * @param test
	 */
	@SuppressWarnings("unchecked")
	public BenchmarkRequest(final ExecProcess ctx, final String title, final Runnable test) {
		this.ctx = ctx;
		this.title = title;
		this.test = (Function<Object, ?>) (Function<?, ?>) Act.FUNCTION_RUN_RUNNABLE;
		this.argument = test;
	}

	/**
	 * @param test
	 */
	@SuppressWarnings("unchecked")
	public BenchmarkRequest(final Runnable test) {
		this.ctx = Exec.currentProcess();
		this.title = test.toString();
		this.test = (Function<Object, ?>) (Function<?, ?>) Act.FUNCTION_RUN_RUNNABLE;
		this.argument = test;
	}

	@Override
	public final Object apply(final Benchmark.Result result) {
		
		{
			final Throwable error = result.getError();
			if (error != null) {
				error.printStackTrace(System.err);
				return null;
			}
		}
		if (result.getIterationsPerSecondMean() > this.maxs) {
			this.maxs = result.getIterationsPerSecondMean();
			this.maxt = result.getIterationTimeMillisMean();
		}
		System.out
				.println("Testing[" + this.title + "]: " + --this.iterations + " iterations left, speed=" + Format.Compact.toDecimal(result.getIterationsPerSecondMean()) + "...");
		if (this.iterations <= 0) {
			System.out.println("Result[" + this.title + "]: speed=" + Format.Compact.toDecimal(this.maxs) + "/s, time=" + Format.Compact.toPeriod(this.maxt));
			synchronized (this) {
				this.notify();
			}
		}
		return null;
	}

	@Override
	public void run() {
		
		for (int i = this.iterations; i > 0; --i) {
			ru.myx.benchmarking.Benchmark.enqueue(this.ctx, this.test, this.argument, this);
		}
		synchronized (this) {
			try {
				this.wait(0L);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
