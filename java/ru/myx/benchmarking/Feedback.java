package ru.myx.benchmarking;

import java.util.function.Function;

final class Feedback implements Runnable {
	private final Function<Benchmark.Result, Object>	target;
	
	final Throwable										error;
	
	final double										bestIterationTimeMean;
	
	final double										bestIterationTimeReal;
	
	Feedback(final Function<Benchmark.Result, Object> target,
			final Throwable error,
			final double bestIterationTimeMean,
			final double bestIterationTimeReal) {
		this.target = target;
		this.error = error;
		this.bestIterationTimeMean = bestIterationTimeMean;
		this.bestIterationTimeReal = bestIterationTimeReal;
	}
	
	@Override
	public void run() {
		try {
			this.target.apply( new Benchmark.Result() {
				@Override
				public Throwable getError() {
					return Feedback.this.error;
				}
				
				@Override
				public double getIterationsPerSecondMean() {
					return 1000.0 / Feedback.this.bestIterationTimeMean;
				}
				
				@Override
				public double getIterationsPerSecondReal() {
					return 1000.0 / Feedback.this.bestIterationTimeReal;
				}
				
				@Override
				public double getIterationTimeMillisMean() {
					return Feedback.this.bestIterationTimeMean;
				}
				
				@Override
				public double getIterationTimeMillisReal() {
					return Feedback.this.bestIterationTimeReal;
				}
			} );
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
}
