package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface VOFmtA2 extends VOFmt, VIFmtA21 {
	
	/** @param process
	 * @param argumentA
	 * @param argumentB
	 * @param constant
	 * @param store
	 * @return code */
	public ExecStateCode execute(ExecProcess process, BaseObject argumentA, BaseObject argumentB, final int constant, ResultHandler store);
}
