/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
abstract class IA2X_AAAA_AB_C_NN_NXT extends IA2X_AAAA_XX_X_NN_NXT {
	
	
	protected final ModifierArgument modifierA;
	
	protected final ModifierArgument modifierB;
	
	protected final int constant;
	
	/**
	 * @param modifierA
	 * @param modifierB
	 */
	IA2X_AAAA_AB_C_NN_NXT(final ModifierArgument modifierA, final ModifierArgument modifierB, final int constant) {
		this.modifierA = modifierA;
		this.modifierB = modifierB;
		this.constant = constant;
	}
	
	@Override
	public final int getConstant() {
		
		
		return this.constant;
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(this.constant) + this.modifierA.argumentStackRead() + this.modifierB.argumentStackRead();
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierA() {
		
		
		return this.modifierA;
	}
	
	@Override
	@NotNull
	public ModifierArgument getModifierB() {
		
		
		return this.modifierB;
	}
}
