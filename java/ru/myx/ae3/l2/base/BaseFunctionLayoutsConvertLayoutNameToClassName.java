package ru.myx.ae3.l2.base;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.l2.LayoutEngine;

/** @author myx */
public final class BaseFunctionLayoutsConvertLayoutNameToClassName extends BaseFunctionAbstract implements ExecCallableBoth.JavaStringJ1 {

	@Override
	public String callSJ1(final BaseObject instance, final BaseObject name) {

		return LayoutEngine.convertLayoutNameToClassName(name.baseToJavaString());
	}

	@Override
	public boolean execIsConstant() {

		return true;
	}

	@Override
	public Class<? extends Object> execResultClassJava() {

		return String.class;
	}

	@Override
	public String toString() {

		return "[Function extra Layouts.convertLayoutNameToClassName(name) method]";
	}
}
