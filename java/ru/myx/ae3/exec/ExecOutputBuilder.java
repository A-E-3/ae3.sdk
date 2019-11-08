/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;

/** @author myx */
public final class ExecOutputBuilder extends BaseFunctionAbstract implements ExecOutputFunction, Describable {

	private final StringBuilder builder;

	/** @param builder */
	public ExecOutputBuilder(final StringBuilder builder) {

		this.builder = builder;
	}

	@Override
	public boolean absorb(final BaseObject object) {

		if (object != null) {
			this.builder.append(object);
		}
		return true;
	}

	@Override
	public final String baseDescribe() {

		return this.getClass().getSimpleName() //
				+ ": len: " + this.builder.length() + ", buffer: " + Format.Describe.toEcmaSource(this.builder.toString(), "");
	}

	@Override
	public BasePrimitiveString baseToString() {

		return Base.forString(this.builder.toString());
	}

	@Override
	public final String toString() {

		return this.builder.toString();
	}
}
