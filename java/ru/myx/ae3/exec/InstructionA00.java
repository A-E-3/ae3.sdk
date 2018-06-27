/**
 * 
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.report.Report;

/**
 * @author myx
 * 
 */
abstract class InstructionA00 implements Instruction {
	
	
	/**
	 * 
	 */
	public static final int CNST_MAX = 1 << 19;
	
	/**
	 * 
	 */
	public static final int CNST_MIN = -(1 << 19) - 1;
	
	/**
	 * @return
	 */
	public abstract OperationA00 getOperation();
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {
		
		
		return InstructionA00.CNST_MAX;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {
		
		
		return InstructionA00.CNST_MIN;
	}
	
	@Override
	public int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(this.getConstant());
	}
	
	@Override
	public final int hashCode() {
		
		
		return InstructionA00.hashCode(this.getOperation(), this.getConstant(), this.getStore());
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof InstructionA00)) {
			return false;
		}
		
		final InstructionA00 x = (InstructionA00) o;
		return this.getOperation() == x.getOperation() && this.getConstant() == x.getConstant() && this.getStore() == x.getStore();
	}
	
	@NotNull
	public abstract ResultHandler getStore();
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.getOperation().isRelativeAddressInConstant();
	}
	
	public static final int hashCode(final OperationA00 operation, final int constant, final ResultHandler store) {
		
		
		return operation.hashCode() ^ constant ^ store.hashCode();
	}
	
	private static final ConcurrentHashMap<Instruction, Instruction> ROOTS = new ConcurrentHashMap<>(256);
	
	static Instruction instructionCached(final Instruction temp) {
		
		
		final Instruction known = ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	@Override
	public final String toCode() {
		
		
		final OperationA00 operation = this.getOperation();
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
