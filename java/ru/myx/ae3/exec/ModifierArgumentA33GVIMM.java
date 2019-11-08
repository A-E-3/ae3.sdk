/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;

/** @author myx */
public final class ModifierArgumentA33GVIMM implements ModifierArgument {

	private final BasePrimitiveString name;

	/** @param name */
	public ModifierArgumentA33GVIMM(final BasePrimitiveString name) {

		this.name = name;
	}

	/** @param name */
	public ModifierArgumentA33GVIMM(final String name) {

		this.name = Base.forString(name);
	}

	@Override
	public final BasePrimitiveString argumentAccessFramePropertyName() {

		return this.name;
	}

	@Override
	public boolean argumentHasSideEffects() {

		return false;
	}

	@Override
	public final String argumentNotation() {

		return "GV['" + this.name + "']";
	}

	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		return process.ri10GV.baseGet(this.name, BaseObject.UNDEFINED);
	}

	@Override
	public final String toString() {

		return "GV['" + this.name + "']";
	}
}
