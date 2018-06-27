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
public abstract class InstructionA2X implements Instruction {
	
	
	/**
	 * 
	 */
	public static final int CNST_MAX = 1 << 9;
	
	/**
	 * 
	 */
	public static final int CNST_MIN = -(1 << 9) - 1;
	
	@Override
	public final int hashCode() {
		
		
		return InstructionA2X.hashCode(this.getOperation(), this.getConstant(), this.hashCodeModifierA(), this.hashCodeModifierB(), this.getStore());
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {
		
		
		return InstructionA2X.CNST_MAX;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {
		
		
		return InstructionA2X.CNST_MIN;
	}
	
	/**
	 * @return
	 */
	public abstract OperationA2X getOperation();
	
	/**
	 * 
	 * @return
	 */
	@NotNull
	public abstract ResultHandler getStore();
	
	static final int hashCode(final OperationA2X operation, final int constant, final int modifierA, final int modifierB, final ResultHandler store) {
		
		
		return operation.getClass().hashCode() ^ operation.hashCode() ^ modifierB ^ modifierA ^ constant ^ store.hashCode();
	}
	
	private static final ConcurrentHashMap<Instruction, Instruction> ROOTS = new ConcurrentHashMap<>();
	
	final static Instruction instructionCached(final Instruction temp) {
		
		
		final Instruction known = InstructionA2X.ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	/**
	 * 
	 * @return
	 */
	@NotNull
	public abstract ModifierArgument getModifierA();
	
	/**
	 * 
	 * @return
	 */
	@NotNull
	public abstract ModifierArgument getModifierB();
	
	int hashCodeModifierA() {
		
		
		return this.getModifierA().hashCode();
	}
	
	int hashCodeModifierB() {
		
		
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
