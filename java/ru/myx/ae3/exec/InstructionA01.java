/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.report.Report;

/** @author myx */
abstract class InstructionA01 implements Instruction {

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 19;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 19) - 1;
	
	private static final ConcurrentHashMap<Instruction, Instruction> ROOTS = new ConcurrentHashMap<>(256);
	
	public static final int hashCode(final OperationA01 operation, final int constant, final ResultHandler store) {

		return operation.hashCode() ^ constant ^ store.hashCode();
	}
	
	static final Instruction instructionCached(final Instruction temp) {

		final Instruction known = InstructionA01.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	@Override
	public final boolean equals(final Object o) {

		if (o == this) {
			return true;
		}
		
		if (!(o instanceof InstructionA01)) {
			return false;
		}
		
		final InstructionA01 x = (InstructionA01) o;
		return this.getOperation() == x.getOperation() && this.getConstant() == x.getConstant() && this.getStore() == x.getStore();
	}
	
	/** @return */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {

		return InstructionA01.CNST_MAX;
	}
	
	/*
	 *
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {

		return InstructionA01.CNST_MIN;
	}
	
	@Override
	public int getOperandCount() {

		return this.getOperation().getStackInputCount(this.getConstant());
	}
	
	/** @return operation */
	public abstract OperationA01 getOperation();
	
	@NotNull
	public abstract ResultHandler getStore();
	
	@Override
	public final int hashCode() {

		return InstructionA01.hashCode(this.getOperation(), this.getConstant(), this.getStore());
	}
	
	@Override
	public final boolean isRelativeAddressInConstant() {

		return this.getOperation().isRelativeAddressInConstant();
	}
	
	@Override
	public final String toCode() {

		final OperationA01 operation = this.getOperation();
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
