package ru.myx.ae3.e4.logic.base;

import ru.myx.ae3.e4.context.FrameContext;
import ru.myx.ae3.e4.logic.LogicValue;
import ru.myx.ae3.exec.ExecStateCode;

public interface LogicCall extends LogicValue {
	
	
	ExecStateCode executeCall(FrameContext ctx);
}
