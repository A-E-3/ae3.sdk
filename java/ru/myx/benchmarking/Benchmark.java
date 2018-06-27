/**
 * Created on 06.02.2003
 * 
 * myx - barachta */
package ru.myx.benchmarking;

import ru.myx.ae3.act.Act;
import java.util.function.Function;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.report.ReportReceiver;

/**
 * @author myx
 * 
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public final class Benchmark {
	/**
	 * @author myx
	 * 
	 */
	public static interface Result {
		/**
		 * @return
		 */
		Throwable getError();
		
		/**
		 * @return double
		 */
		double getIterationsPerSecondMean();
		
		/**
		 * @return double
		 */
		double getIterationsPerSecondReal();
		
		/**
		 * @return double
		 */
		double getIterationTimeMillisMean();
		
		/**
		 * @return double
		 */
		double getIterationTimeMillisReal();
	}
	
	static final String			OWNER	= "MYX/MICROBENCHMARK";
	
	static final ReportReceiver	LOG		= Report.createReceiver( null );
	
	private static final Queue	QUEUE	= new Queue();
	
	/**
	 * 
	 */
	public static final String	USER_ID	= " BENCHMARK ";
	
	static {
		Benchmark.LOG.event( Benchmark.OWNER, "INFO", "Benchmarking engine is being initialized." );
	}
	
	/**
	 * Results will be passed to as a Benchmark. Result instance for a
	 * target.execute method.
	 * 
	 * @param <T>
	 * 
	 * @param ctx
	 * @param test
	 * @param argument
	 * @param target
	 */
	public static final <T> void enqueue(
			final ExecProcess ctx,
			final Function<T, ?> test,
			final T argument,
			final Function<Benchmark.Result, Object> target) {
		Benchmark.LOG.event( Benchmark.OWNER, "INFO", "Enqueuing benchmark: test=" + test + ", target=" + target + "." );
		Benchmark.QUEUE.enqueue( ctx, test, argument, target );
	}
	
	/**
	 * Results will be passed to as a Benchmark. Result instance for a
	 * target.execute method.
	 * 
	 * @param ctx
	 * @param test
	 * @param target
	 */
	public static final void enqueue(
			final ExecProcess ctx,
			final Runnable test,
			final Function<Benchmark.Result, Object> target) {
		Benchmark.LOG.event( Benchmark.OWNER, "INFO", "Enqueuing benchmark: test=" + test + ", target=" + target + "." );
		Benchmark.QUEUE.enqueue( ctx, Act.FUNCTION_RUN_RUNNABLE, test, target );
	}
	
	private Benchmark() {
		// empty
	}
}
