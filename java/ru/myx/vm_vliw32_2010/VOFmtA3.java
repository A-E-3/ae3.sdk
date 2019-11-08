package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface VOFmtA3 extends VOFmt, VIFmtA31 {
	
	/** @param process
	 * @param argumentA
	 * @param argumentB
	 * @param argumentC
	 * @param constant
	 * @param store
	 * @return code */
	ExecStateCode execute(ExecProcess process, BaseObject argumentA, BaseObject argumentB, BaseObject argumentC, int constant, ResultHandler store);
}
