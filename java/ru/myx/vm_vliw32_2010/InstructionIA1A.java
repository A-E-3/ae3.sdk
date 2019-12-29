/**
 *
 */
package ru.myx.vm_vliw32_2010;

import java.util.concurrent.ConcurrentHashMap;


import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.report.Report;

/** @author myx */
public abstract class InstructionIA1A implements InstructionIA {

	private static final ConcurrentHashMap<InstructionIA1A, InstructionIA1A> ROOTS = new ConcurrentHashMap<>();

	static final int hashCode(final VOFmtA1 operation, final int constant, final int modifierA, final ResultHandler store) {

		return operation.getClass().hashCode() ^ operation.hashCode() ^ modifierA ^ constant ^ store.hashCode();
	}

	final static InstructionIA1A instructionCached(final InstructionIA1A temp) {

		final InstructionIA1A known = InstructionIA1A.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}

	@Override
	public final boolean equals(final Object o) {

		if (o == this) {
			return true;
		}

		if (!(o instanceof InstructionIA1A)) {
			return false;
		}

		final InstructionIA1A x = (InstructionIA1A) o;
		return this.getOperation() == x.getOperation() && this.getModifierA() == x.getModifierA() && this.getConstant() == x.getConstant() && this.getStore() == x.getStore();
	}

	/** @return */
	public abstract ModifierArgument getModifierA();

	@Override
	public int getOperandCount() {

		return this.getOperation().getStackInputCount(this.getConstant()) + this.getModifierA().argumentStackRead();
	}

	/** @return */
	@Override
	public abstract VOFmtA1 getOperation();

	/** @return */
	@Override
	public abstract ResultHandler getStore();

	@Override
	public final int hashCode() {

		return InstructionIA1A.hashCode(this.getOperation(), this.getConstant(), this.hashCodeModifierA(), this.getStore());
	}

	/** @return */
	public int hashCodeModifierA() {

		return this.getModifierA().hashCode();
	}

	@Override
	public boolean isRelativeAddressInConstant() {

		return this.getOperation().isRelativeAddressInConstant();
	}

	@Override
	public final String toCode() {

		final VOFmtA1 operation = this.getOperation();
		final int constant = this.getConstant();

		final StringBuilder result = new StringBuilder(64);

		result.append(Instruction.padOPCODE(operation.toString()));

		result.append("  1");

		final int stackInput = operation.getStackInputCount(constant);
		if (stackInput > 0) {
			result.append('>').append(stackInput);
		}

		result.append('\t').append(Instruction.padCONSTANT(constant));

		{
			result.append('\t');
			final int argsIndex = result.length();

			result.append(this.getModifierA().argumentNotation());

			final int argsLength = result.length() - argsIndex;
			if (argsLength < 25) {
				result.append("                         ", argsLength, 25);
			}
		}

		result.append('\t').append(this.getStore());

		if (Report.MODE_ASSERT || Report.MODE_DEBUG) {
			result.append(" \t// ").append(this.getClass().getSimpleName().replace("InstructionA", ""));
		}

		return result.toString();
	}
}
