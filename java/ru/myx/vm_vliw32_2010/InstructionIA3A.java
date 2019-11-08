/**
 *
 */
package ru.myx.vm_vliw32_2010;

import java.util.concurrent.ConcurrentHashMap;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.report.Report;

/** @author myx */
public abstract class InstructionIA3A implements InstructionIA {
	
	private static final ConcurrentHashMap<InstructionIA3A, InstructionIA3A> ROOTS = new ConcurrentHashMap<>();

	/** @param operation
	 * @param constant
	 * @param modifierA
	 * @param modifierB
	 * @param modifierC
	 * @param store
	 * @return */
	public static final int hashCode(final VOFmtA3 operation, final int constant, final int modifierA, final int modifierB, final int modifierC, final ResultHandler store) {
		
		return operation.getClass().hashCode() ^ operation.hashCode() ^ modifierC ^ modifierB ^ modifierA ^ constant ^ store.hashCode();
	}

	final static InstructionIA3A instructionCached(final InstructionIA3A temp) {
		
		final InstructionIA3A known = InstructionIA3A.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}

	@Override
	public final boolean equals(final Object o) {
		
		if (o == this) {
			return true;
		}

		if (!(o instanceof InstructionIA3A)) {
			return false;
		}

		final InstructionIA3A x = (InstructionIA3A) o;
		return this.getOperation() == x.getOperation() && this.getConstant() == x.getConstant() && this.getModifierA() == x.getModifierA()
				&& this.getModifierB() == x.getModifierB() && this.getModifierC() == x.getModifierC() && this.getStore() == x.getStore();
	}

	/** @return */
	@NotNull
	public abstract ModifierArgument getModifierA();

	/** @return */
	@NotNull
	public abstract ModifierArgument getModifierB();

	/** @return */
	@NotNull
	public abstract ModifierArgument getModifierC();

	/** @return */
	@Override
	public abstract VOFmtA3 getOperation();

	@Override
	@NotNull
	public abstract ResultHandler getStore();

	@Override
	public final int hashCode() {
		
		return InstructionIA3A.hashCode(this.getOperation(), this.getConstant(), this.hashCodeModifierA(), this.hashCodeModifierB(), this.hashCodeModifierC(), this.getStore());
	}

	/** @return */
	public int hashCodeModifierA() {
		
		return this.getModifierA().hashCode();
	}

	/** @return */
	public int hashCodeModifierB() {
		
		return this.getModifierB().hashCode();
	}

	/** @return */
	public int hashCodeModifierC() {
		
		return this.getModifierC().hashCode();
	}

	@Override
	public final String toCode() {
		
		final VOFmtA3 operation = this.getOperation();
		final int constant = this.getConstant();

		final StringBuilder result = new StringBuilder(64);

		result.append(Instruction.padOPCODE(operation.toString()));

		result.append("  3");

		final int stackInput = operation.getStackInputCount(constant);
		if (stackInput > 0) {
			result.append('>').append(stackInput);
		}

		result.append('\t').append(Instruction.padCONSTANT(constant));

		{
			result.append('\t');
			final int argsIndex = result.length();

			result.append(this.getModifierA().argumentNotation());
			result.append(", ").append(this.getModifierB().argumentNotation());
			result.append(", ").append(this.getModifierC().argumentNotation());

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
