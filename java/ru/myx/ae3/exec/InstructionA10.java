/**
 *
 */
package ru.myx.ae3.exec;

import java.util.concurrent.ConcurrentHashMap;

import com.sun.istack.internal.NotNull;

import ru.myx.ae3.report.Report;

/** @author myx */
public abstract class InstructionA10 implements Instruction {

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 14;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 14) - 1;
	
	private static final ConcurrentHashMap<InstructionA10, InstructionA10> ROOTS = new ConcurrentHashMap<>();
	
	final static InstructionA10 instructionCached(final InstructionA10 temp) {

		final InstructionA10 known = InstructionA10.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/** @return */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {

		return InstructionA10.CNST_MAX;
	}
	
	/** @return */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {

		return InstructionA10.CNST_MIN;
	}
	
	/** @return */
	@NotNull
	public abstract ModifierArgument getModifierA();
	
	/** @return */
	public abstract OperationA10 getOperation();
	
	/** @return */
	@NotNull
	public abstract ResultHandler getStore();
	
	@Override
	public final String toCode() {

		final OperationA10 operation = this.getOperation();
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
