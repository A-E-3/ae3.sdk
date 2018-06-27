package ru.myx.ae3.e4.vm;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;

/**
 * 
 * @author myx
 *
 */
public interface AeVmInstruction extends VmInstruction<ExecStateCode, ExecProcess> {
	/**
	 * null - instruction finished normally.
	 * 
	 * @param ctx
	 * @return state code
	 * @throws Exception
	 */
	@Override
	ExecStateCode execCall(
			ExecProcess ctx) throws Exception;
	
}
