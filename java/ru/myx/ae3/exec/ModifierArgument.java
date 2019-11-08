/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;

/** @author myx */
public interface ModifierArgument {

	/** @param store
	 * @return */
	static ModifierArgument forStore(final ResultHandler store) {

		switch (store.execDirectTransportType()) {
			case RB :
				return ModifierArguments.AA0RB;
			case RL :
				return ModifierArguments.AA1RL;
			case RD :
				return ModifierArguments.AA2RD;
			case RS :
				return ModifierArguments.AA3RS;
			case RV :
				return ModifierArguments.AC13UNDEFINED;
			case CH :
				return ModifierArguments.AF34AXRR;
			default :
				throw new IllegalArgumentException("Unsupported directType: " + store.execDirectTransportType());
		}
	}

	/** null when argument is not representing frame property access
	 *
	 * @return value */
	default BasePrimitiveString argumentAccessFramePropertyName() {

		return null;
	}

	/** null when argument is not representing 'this' property access
	 *
	 * @return value */
	default BasePrimitiveString argumentAccessThisPropertyName() {

		return null;
	}

	/** null when argument is not representing constant value
	 *
	 * @return value */
	default BaseObject argumentConstantValue() {

		return null;
	}

	/** @param process
	 * @return object */
	default double argumentDouble(final ExecProcess process) {

		return this.argumentRead(process).baseToNumber().doubleValue();
	}

	/** true when it causes side-effects. FV[x] returns 'true'.
	 *
	 * @return */
	boolean argumentHasSideEffects();

	/** @param process
	 * @return object */
	default int argumentInt32(final ExecProcess process) {

		return this.argumentRead(process).baseToJavaInteger();
	}

	/** @param process
	 * @return object */
	default long argumentLong(final ExecProcess process) {

		return this.argumentRead(process).baseToJavaLong();
	}

	/** @return code */
	String argumentNotation();

	/** @param process
	 * @return object */
	BaseObject argumentRead(ExecProcess process);

	/** default implementation returns 0.
	 *
	 * @return int */
	default int argumentStackRead() {

		return 0;
	}

	/** default implementation returns 0.
	 *
	 * @return int */
	default int argumentStackWrite() {

		return 0;
	}

	/** @param process
	 * @return object */
	default CharSequence argumentString(final ExecProcess process) {

		return this.argumentRead(process).baseToJavaString();
	}
}
