/**
 * Created on 06.02.2003
 * 
 * myx - barachta */
package ru.myx.benchmarking;

import java.util.function.Function;
import ru.myx.ae3.exec.ExecProcess;

/**
 * @author myx
 */
final class Request<T> {
	final String								subject;
	
	final Function<T, ?>						test;
	
	final T										argument;
	
	final Function<Benchmark.Result, Object>	target;
	
	final ExecProcess							context;
	
	Request(final String subject,
			final ExecProcess context,
			final Function<T, ?> test,
			final T argument,
			final Function<Benchmark.Result, Object> target) {
		this.subject = subject;
		this.test = test;
		this.argument = argument;
		this.target = target;
		this.context = context;
	}
	
	@Override
	public String toString() {
		return "benchRequest: subject="
				+ this.subject
				+ ",test="
				+ this.test.getClass().getName()
				+ ", target="
				+ this.target.getClass().getName();
	}
}
