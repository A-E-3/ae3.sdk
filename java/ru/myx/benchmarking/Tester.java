/**
 * Created on 06.02.2003
 * 
 * myx - barachta */
package ru.myx.benchmarking;

import ru.myx.ae3.act.Act;
import java.util.function.Function;
import ru.myx.ae3.act.ContextThread;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.report.Report;

/**
 * @author myx
 * 		
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
final class Tester extends ContextThread<ExecProcess> {
	
	private final Queue q;
	
	volatile boolean done = true;
	
	long started = 0;
	
	Tester(final Queue queue) {
		super(Exec.getRootProcess(), "Microbenchmark tester thread");
		this.setPriority(Thread.MAX_PRIORITY);
		this.setDaemon(false);
		this.q = queue;
	}
	
	@Override
	public void run() {
		
		try {
			final Controller controller = new Controller(this);
			controller.start();
			try {
				for (;;) {
					@SuppressWarnings("unchecked")
					final Request<Object> request = (Request<Object>) this.q.nextRequest();
					if (request == null) {
						return;
					}
					if (request.context != null) {
						this.ctx = request.context;
					}
					final Function<Object, ?> test = request.test;
					final Object argument = request.argument;
					Benchmark.LOG.event(Benchmark.OWNER, "INFO", "starting test=" + test + ".");
					// Compiler.enable();
					// Compiler.compileClass(test.getClass());
					double bestIterationTime = Double.POSITIVE_INFINITY;
					// Compiler.disable();
					try {
						for (int loops = 6; loops > 0; loops--) {
							if (Report.MODE_DEBUG) {
								Benchmark.LOG.event(Benchmark.OWNER, "DEBUG", "prepare, [" + loops + " iterations left].");
							}
							System.gc();
							synchronized (this) {
								try {
									this.wait(loops * 400L);
								} catch (final InterruptedException e) {
									return;
								}
							}
							if (Report.MODE_DEBUG) {
								Benchmark.LOG.event(Benchmark.OWNER, "DEBUG", "running, [" + loops + " iterations left].");
							}
							int counter = 0;
							this.started = System.nanoTime();
							for (this.done = false; !this.done; counter++) {
								test.apply(argument);
							}
							final long time = System.nanoTime() - this.started;
							final double iterationTime = time / 1000000.0 / counter;
							if (bestIterationTime > iterationTime) {
								bestIterationTime = iterationTime;
							}
							final double bestOverheadIterationTime = this.q.getBestOverheadIterationTime(bestIterationTime);
							Benchmark.LOG.event(
									Benchmark.OWNER,
									"INFO",
									"checkpoint, speed=" + Format.Compact.toDecimal(1000.0 / iterationTime) + "/s, time=" + Format.Compact.toPeriod(iterationTime) + ", corrected="
											+ Format.Compact.toPeriod(iterationTime - bestOverheadIterationTime));
							if (counter < 2 && loops > 2) {
								loops = 2;
								Benchmark.LOG.event(Benchmark.OWNER, "INFO", "test is too long, reducing loop count!");
							}
						}
					} catch (final Exception e) {
						Benchmark.LOG.event(Benchmark.OWNER, "ERROR", "done, best result=" + Format.Throwable.toText(e) + ".");
						Act.launch(request.context, new Feedback(request.target, e, Double.NaN, Double.NaN));
						continue;
					}
					final double bestOverheadIterationTime = this.q.getBestOverheadIterationTime(bestIterationTime);
					Benchmark.LOG.event(
							Benchmark.OWNER,
							"INFO",
							"done, best result=" + Format.Compact.toDecimal(1000.0 / bestIterationTime) + " loops per second, best time= "
									+ Format.Compact.toPeriod(bestIterationTime) + ", corrected=" + Format.Compact.toPeriod(bestIterationTime - bestOverheadIterationTime) + ".");
					Act.launch(request.context, new Feedback(request.target, null, bestIterationTime - bestOverheadIterationTime, bestIterationTime));
				}
			} finally {
				// Compiler.enable();
				controller.stop = true;
			}
		} catch (final Exception e) {
			this.getThreadGroup().uncaughtException(this, e);
		}
	}
}
