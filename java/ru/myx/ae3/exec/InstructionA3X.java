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
abstract class InstructionA3X implements Instruction {
	
	
	/**
	 * 
	 */
	public static final int CNST_MAX = 1 << 1;
	
	/**
	 * 
	 */
	public static final int CNST_MIN = -(1 << 1) - 1;
	
	@Override
	public final int hashCode() {
		
		
		return InstructionA3X.hashCode(this.getOperation(), this.getConstant(), this.hashCodeModifierA(), this.hashCodeModifierB(), this.hashCodeModifierC(), this.getStore());
	}
	
	@Override
	public final boolean equals(final Object o) {
		
		
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof InstructionA3X)) {
			return false;
		}
		
		final InstructionA3X x = (InstructionA3X) o;
		return this.getOperation() == x.getOperation() && this.getConstant() == x.getConstant() && this.getModifierA() == x.getModifierA()
				&& this.getModifierB() == x.getModifierB() && this.getModifierC() == x.getModifierC() && this.getStore() == x.getStore();
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMaxValue() {
		
		
		return InstructionA3X.CNST_MAX;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("static-method")
	public final int getConstantMinValue() {
		
		
		return InstructionA3X.CNST_MIN;
	}
	
	/**
	 * @return
	 */
	public abstract OperationA3X getOperation();
	
	@NotNull
	public abstract ResultHandler getStore();
	
	public static final int hashCode(final OperationA3X operation,
			final int constant,
			final int modifierA,
			final int modifierB,
			final int modifierC,
			final ResultHandler store) {
		
		
		return operation.getClass().hashCode() ^ operation.hashCode() ^ modifierC ^ modifierB ^ modifierA ^ constant ^ store.hashCode();
	}
	
	private static final ConcurrentHashMap<InstructionA3X, InstructionA3X> ROOTS = new ConcurrentHashMap<>();
	
	final static InstructionA3X instructionCached(final InstructionA3X temp) {
		
		
		final InstructionA3X known = ROOTS.putIfAbsent(temp, temp);
		return known == null
			? temp
			: known;
	}
	
	@NotNull
	public abstract ModifierArgument getModifierA();
	
	@NotNull
	public abstract ModifierArgument getModifierB();
	
	@NotNull
	public abstract ModifierArgument getModifierC();
	
	int hashCodeModifierA() {
		
		
		return this.getModifierA().hashCode();
	}
	
	int hashCodeModifierB() {
		
		
		return this.getModifierB().hashCode();
	}
	
	int hashCodeModifierC() {
		
		
		return this.getModifierC().hashCode();
	}
	
	@Override
	public final String toCode() {
		
		
		final OperationA3X operation = this.getOperation();
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
