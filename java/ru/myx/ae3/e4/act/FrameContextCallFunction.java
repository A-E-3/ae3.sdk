package ru.myx.ae3.e4.act;

import java.util.function.Function;
import ru.myx.ae3.e4.context.FrameContext;
import ru.myx.ae3.exec.ExecProcess;

/**
 * 
 * @author myx
 * 		
 * @param <A>
 * @param <R>
 */
public class FrameContextCallFunction<A, R> implements ActFrameContext {
	
	private final ExecProcess process;
	
	private final Function<A, R> function;
	
	private final A argument;
	
	/**
	 * 
	 * @param process
	 * @param function
	 * @param argument
	 */
	public FrameContextCallFunction(final ExecProcess process, final Function<A, R> function, final A argument) {
		this.process = process;
		this.function = function;
		this.argument = argument;
	}
	
	@Override
	public FrameContext executeStep() {
		
		try {
			this.function.apply(this.argument);
			return null;
		} catch (Exception e) {
			return FrameControlSymbolContext.createErrorWithThrowable(e);
		}
	}
	
}
