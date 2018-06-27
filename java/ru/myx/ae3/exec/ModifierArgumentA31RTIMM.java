/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;

/** @author myx */
public final class ModifierArgumentA31RTIMM implements ModifierArgument {

	private final BasePrimitiveString name;
	
	/** @param name */
	public ModifierArgumentA31RTIMM(final BasePrimitiveString name) {
		this.name = name;
	}
	
	@Override
	public boolean argumentHasSideEffects() {

		return true;
	}
	
	@Override
	public final String argumentNotation() {

		return "RT['" + this.name + "']";
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		return process.rb4CT.baseGet(this.name, BaseObject.UNDEFINED);
		// return Ecma.ecmaFieldRead( process, process.r4RT, this.name );
	}
	
	@Override
	public final String toString() {

		return "RT['" + this.name + "']";
	}
}
