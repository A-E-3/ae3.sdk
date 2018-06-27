/**
 * Created on 06.02.2003
 *
 * myx - barachta */
package ru.myx.benchmarking;

import java.util.LinkedList;

import java.util.function.Function;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.report.Report;

/**
 * @author myx
 *
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
final class Queue {
	
	static final String OWNER = "MYX/MICROBENCHMARK/QUEUE";

	private static final Function<Object, Object> OVERHEAD1_TEST = new Function<Object, Object>() {
		
		@Override
		public Object apply(final Object arg) {
			
			return arg;
		}
		
		@Override
		public String toString() {
			
			return "OVERHEAD_TEST #1";
		}
	};

	private static final Function<Benchmark.Result, Object> OVERHEAD_TARGET = new Function<Benchmark.Result, Object>() {
		
		@Override
		public Object apply(final Benchmark.Result arg) {
			
			return null;
		}
		
		@Override
		public String toString() {
			
			return "OVERHEAD_TARGET";
		}
	};

	private static final int WARMUP_LOAD = 1024 * 1024 * 2;

	private static final void warmup() {
		
		final int[] mem_load = new int[Queue.WARMUP_LOAD];
		for (int i = Queue.WARMUP_LOAD - 1; i >= 0; --i) {
			mem_load[i] = i;
		}
	}

	private final LinkedList<Request<? extends Object>> q = new LinkedList<>();

	private boolean first = true;

	private boolean active = false;

	private double bestOverheadIterationTime = Double.POSITIVE_INFINITY;

	/**
	 * to restart tester several times to force it compiled!
	 */
	private int specialExitMask = 7;

	private final void checkStart() {
		
		if (!this.active) {
			Report.event(Queue.OWNER, "INFO", "Launching...");
			this.active = true;
			new Tester(this).start();
			Report.event(Queue.OWNER, "INFO", "Launched.");
		}
	}

	synchronized <T> void enqueue(final ExecProcess ctx, final Function<T, ?> test, final T argument, final Function<Benchmark.Result, Object> target) {
		
		if (this.first) {
			Report.event(Queue.OWNER, "INFO", "Warming up...");
			this.first = false;
			Report.event(Queue.OWNER, "INFO", "Warming up routine call #1/3...");
			Queue.warmup();
			System.gc();
			try {
				Thread.sleep(1000L);
			} catch (final InterruptedException e) {
				throw new RuntimeException("Error while warming up benchmarking engine!", e);
			}
			Compiler.compileClass(Queue.class);
			Compiler.compileClass(Tester.class);
			Compiler.compileClass(Controller.class);
			Report.event(Queue.OWNER, "INFO", "Warming up routine call #2/3...");
			Queue.warmup();
			System.gc();
			try {
				Thread.sleep(1000L);
			} catch (final InterruptedException e) {
				throw new RuntimeException("Error while warming up benchmarking engine!", e);
			}
			Report.event(Queue.OWNER, "INFO", "Warming up routine call #3/3...");
			Queue.warmup();
			System.gc();
			try {
				Thread.sleep(1000L);
			} catch (final InterruptedException e) {
				throw new RuntimeException("Error while warming up benchmarking engine!", e);
			}
			Report.event(Queue.OWNER, "INFO", "Testing overhead...");
			this.q.addFirst(new Request<>("overhead", null, Queue.OVERHEAD1_TEST, null, Queue.OVERHEAD_TARGET));
			this.q.addFirst(new Request<>("overhead", null, Queue.OVERHEAD1_TEST, null, Queue.OVERHEAD_TARGET));
			this.q.addFirst(new Request<>("overhead", null, Queue.OVERHEAD1_TEST, null, Queue.OVERHEAD_TARGET));
			this.q.addFirst(new Request<>("overhead", null, Queue.OVERHEAD1_TEST, null, Queue.OVERHEAD_TARGET));
			Report.event(Queue.OWNER, "INFO", "Warming up, system overhead.");
		}
		this.q.addLast(new Request<>("benchmark(" + System.identityHashCode(target) + ")", ctx, test, argument, target));
		this.checkStart();
	}

	double getBestOverheadIterationTime(final double alternative) {
		
		return this.bestOverheadIterationTime = Math.min(this.bestOverheadIterationTime, alternative);
	}

	synchronized Request<?> nextRequest() {
		
		if (this.q.size() == 0) {
			this.active = false;
			return null;
		}
		if (this.specialExitMask > 0 && (--this.specialExitMask & 0x01) != 0) {
			Report.event(Queue.OWNER, "INFO", "Special tester restart condition - restarting...");
			this.active = false;
			this.checkStart();
			return null;
		}
		return this.q.removeFirst();
	}
}
