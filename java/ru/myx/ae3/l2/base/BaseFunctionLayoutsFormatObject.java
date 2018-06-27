package ru.myx.ae3.l2.base;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.ExecCallableBoth;

/** @author myx */
public final class BaseFunctionLayoutsFormatObject extends BaseFunctionAbstract implements ExecCallableBoth.JavaStringJ1 {

	@Override
	public String callSJ1(final BaseObject instance, final BaseObject argument) {

		return Ecma.toEcmaSource(argument, false, 0);
	}

	@Override
	public boolean execHasNamedArguments() {

		return false;
	}

	@Override
	public boolean execIsConstant() {

		return false;
	}

	@Override
	public Class<? extends Object> execResultClassJava() {

		return String.class;
	}

	@Override
	public String toString() {

		return "[Function extra Layouts.formatObject(obj) method]";
	}
}
