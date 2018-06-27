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
abstract class InstructionA11 implements Instruction {
	
	
	/**
	 * 
	 */
	public static final int CNST_MAX = 1 << 15;
	
	/**
	 * 
	 */
	public static final int CNST_MIN = -(1 << 15) - 1;
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {
		
		
		return InstructionA11.CNST_MAX;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {
		
		
		return InstructionA11.CNST_MIN;
	}
	
	/**
	 * @return
	 */
	public abstract OperationA11 getOperation();
	
	@NotNull
	public abstract ResultHandler getStore();
	
	private static final ConcurrentHashMap<InstructionA11, InstructionA11> ROOTS = new ConcurrentHashMap<>();
	
	final static InstructionA11 instructionCached(final InstructionA11 temp) {
		
		
		final InstructionA11 known = InstructionA11.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	@NotNull
	public abstract ModifierArgument getModifierA();
	
	@Override
	public int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(this.getConstant()) + +this.getModifierA().argumentStackRead();
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {
		
		
		return this.getOperation().isRelativeAddressInConstant();
	}
	
	@Override
	public final String toCode() {
		
		
		final OperationA11 operation = this.getOperation();
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
