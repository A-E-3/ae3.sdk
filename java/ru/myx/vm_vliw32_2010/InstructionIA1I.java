/**
 *
 */
package ru.myx.vm_vliw32_2010;

/** @author myx */
public abstract class InstructionIA1I extends InstructionIA1A {

	@Override
	public int getOperandCount() {
		
		return this.getOperation().getStackInputCount(this.getConstant());
	}

	@Override
	public final boolean isRelativeAddressInConstant() {
		
		return this.getOperation().isRelativeAddressInConstant();
	}
}
