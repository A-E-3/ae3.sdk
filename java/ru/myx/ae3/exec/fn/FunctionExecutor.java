package ru.myx.ae3.exec.fn;

import ru.myx.ae3.exec.ExecCallable;

/*
 * Created on 16.01.2005
 */
/**
 * TODO: crap 8-( replace with more generic interfaces or do without it
 */
public interface FunctionExecutor extends ExecCallable {
	
	
	/**
	 *
	 */
	void start();
	
	/**
	 *
	 */
	void stop();
}
