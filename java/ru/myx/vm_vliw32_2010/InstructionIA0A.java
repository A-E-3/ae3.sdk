/**
 *
 */
package ru.myx.vm_vliw32_2010;

import java.util.concurrent.ConcurrentHashMap;


import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.report.Report;

/** @author myx */
public abstract class InstructionIA0A implements InstructionIA {
	
	private static final ConcurrentHashMap<InstructionIA, InstructionIA> ROOTS = new ConcurrentHashMap<>(256);

	/** @param operation
	 * @param constant
	 * @param store
	 * @return */
	public static final int hashCode(final VOFmtA0 operation, final int constant, final ResultHandler store) {
		
		return operation.hashCode() ^ constant ^ store.hashCode();
	}

	static InstructionIA instructionCached(final InstructionIA temp) {
		
		final InstructionIA known = InstructionIA0A.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}

	@Override
	public final boolean equals(final Object o) {
		
		if (o == this) {
			return true;
		}

		if (!(o instanceof InstructionIA0A)) {
			return false;
		}

		final InstructionIA0A x = (InstructionIA0A) o;
		return this.getOperation() == x.getOperation() && this.getConstant() == x.getConstant() && this.getStore() == x.getStore();
	}

	@Override
	public int getOperandCount() {
		
		return this.getOperation().getStackInputCount(this.getConstant());
	}

	/** @return */
	@Override
	public abstract VOFmtA0 getOperation();

	@Override
	public abstract ResultHandler getStore();

	@Override
	public final int hashCode() {
		
		return InstructionIA0A.hashCode(this.getOperation(), this.getConstant(), this.getStore());
	}

	@Override
	public boolean isRelativeAddressInConstant() {
		
		return this.getOperation().isRelativeAddressInConstant();
	}

	@Override
	public final String toCode() {
		
		final VOFmtA0 operation = this.getOperation();
		final int constant = this.getConstant();

		final StringBuilder result = new StringBuilder(64);

		result.append(Instruction.padOPCODE(operation.toString()));

		result.append("  0");

		final int stackInput = operation.getStackInputCount(constant);
		if (stackInput > 0) {
			result.append('>').append(stackInput);
		}

		result.append('\t').append(Instruction.padCONSTANT(constant));

		{
			result.append('\t');
			final int argsIndex = result.length();

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
