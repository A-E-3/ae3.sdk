package ru.myx.ae3.e4.act;

import ru.myx.ae3.e4.context.FrameContext;

/**
 * 
 * @author myx
 *		
 */
public class FrameContextRunRunnable implements ActFrameContext {
	
	private final Runnable run;
	
	/**
	 * 
	 * @param run
	 */
	public FrameContextRunRunnable(final Runnable run) {
		this.run = run;
	}
	
	@Override
	public FrameContext executeStep() {
		
		this.run.run();
		return null;
	}
	
}
