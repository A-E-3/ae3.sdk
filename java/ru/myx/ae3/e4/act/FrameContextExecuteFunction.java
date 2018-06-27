package ru.myx.ae3.e4.act;

import java.util.function.Function;
import ru.myx.ae3.e4.context.FrameContext;

/**
 * 
 * @author myx
 *		
 * @param <A>
 * @param <R>
 */
public class FrameContextExecuteFunction<A, R> implements ActFrameContext {
	
	private final Function<A, R> function;
	
	private final A argument;
	
	/**
	 * 
	 * @param function
	 * @param argument
	 */
	public FrameContextExecuteFunction(final Function<A, R> function, final A argument) {
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
