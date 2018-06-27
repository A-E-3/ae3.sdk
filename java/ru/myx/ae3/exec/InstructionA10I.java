/**
 * 
 */
package ru.myx.ae3.exec;

/**
 * @author myx
 * 
 */
abstract class InstructionA10I extends InstructionA10 {
	
	
	@Override
	public abstract boolean equals(final Object o);
	
	@Override
	public int getOperandCount() {
		
		
		return this.getOperation().getStackInputCount(this.getConstant());
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public final boolean isRelativeAddressInConstant() {
		
		
		return this.getOperation().isRelativeAddressInConstant();
	}
}
