/**
 *
 */
package ru.myx.ae3.exec;


import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.ecma.Ecma;

/** @author myx */
public final class ModifierArgumentA30IMM implements ModifierArgument {

	/**
	 *
	 */
	public static final ModifierArgument EMPTY_STRING = new ModifierArgumentA30IMM(BaseString.EMPTY);

	/**
	 *
	 */
	public static final ModifierArgument TYPEOF = new ModifierArgumentA30IMM("typeof");

	/**
	 *
	 */
	public static final ModifierArgument TRUE = ModifierArguments.AC15TRUE;

	/**
	 *
	 */
	public static final ModifierArgument NULL = ModifierArguments.AC12NULL;

	/**
	 *
	 */
	public static final ModifierArgument UNDEFINED = ModifierArguments.AC13UNDEFINED;

	/**
	 *
	 */
	public static final ModifierArgument FALSE = ModifierArguments.AC11FALSE;

	/**
	 *
	 */
	public static final ModifierArgument ZERO = ModifierArguments.AC10ZERO;

	/**
	 *
	 */
	public static final ModifierArgument MONE = ModifierArguments.AC16MONE;

	/**
	 *
	 */
	public static final ModifierArgument ONE = ModifierArguments.AC14ONE;

	private final BaseObject value;

	/** @param value */
	public ModifierArgumentA30IMM(final BaseObject value) {

		assert value != null : "NULL java value";
		assert !(value instanceof ModifierArgument) : "Oops!";
		this.value = value;
	}

	/** @param value */
	public ModifierArgumentA30IMM(final Object value) {

		this.value = Base.forUnknown(value);
		assert this.value != null : "NULL java value";
	}

	/** @param value */
	public ModifierArgumentA30IMM(final String value) {

		this.value = Base.forString(value);
	}

	@Override
	public final BaseObject argumentConstantValue() {

		return this.value;
	}

	@Override
	public boolean argumentHasSideEffects() {

		return false;
	}

	@Override
	public final String argumentNotation() {

		return Ecma.toEcmaSourceCompact(this.value);
	}

	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		return this.value;
	}

	@Override
	public final String toString() {

		return Ecma.toEcmaSourceCompact(this.value);
	}
}
