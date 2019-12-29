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
public abstract class InstructionIA2A implements InstructionIA {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>();

	static final int hashCode(final OperationA2X operation, final int constant, final int modifierA, final int modifierB, final ResultHandler store) {
		
		return operation.getClass().hashCode() ^ operation.hashCode() ^ modifierB ^ modifierA ^ constant ^ store.hashCode();
	}

	final static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionIA2A.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}

	@Override
	public final boolean equals(final Object o) {

		if (o == this) {
			return true;
		}
		
		if (!(o instanceof InstructionIA2A)) {
			return false;
		}
		
		final InstructionIA2A x = (InstructionIA2A) o;
		return this.getOperation() == x.getOperation() && this.getModifierA() == x.getModifierA() && this.getModifierB() == x.getModifierB()
				&& this.getConstant() == x.getConstant() && this.getStore() == x.getStore();
	}

	/** @return */
	public abstract ModifierArgument getModifierA();

	/** @return */
	public abstract ModifierArgument getModifierB();
	
	/** @return */
	@Override
	public abstract OperationA2X getOperation();

	/** @return */
	@Override
	public abstract ResultHandler getStore();

	@Override
	public final int hashCode() {
		
		return InstructionIA2A.hashCode(this.getOperation(), this.getConstant(), this.hashCodeModifierA(), this.hashCodeModifierB(), this.getStore());
	}

	/** @return */
	public int hashCodeModifierA() {
		
		return this.getModifierA().hashCode();
	}

	/** @return */
	public int hashCodeModifierB() {
		
		return this.getModifierB().hashCode();
	}

	@Override
	public final String toCode() {
		
		final OperationA2X operation = this.getOperation();
		final int constant = this.getConstant();

		final StringBuilder result = new StringBuilder(64);

		result.append(Instruction.padOPCODE(operation.toString()));

		result.append("  2");

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
