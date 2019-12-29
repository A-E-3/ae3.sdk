/**
 *
 */
package ru.myx.vm_vliw32_2010;


import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface InstructionIA extends Instruction {

	/** @return */
	public abstract VOFmt getOperation();

	/** @return */
	public abstract ResultHandler getStore();
	
	@Override
	public abstract String toCode();
	
	/** @return
	 * @throws IllegalArgumentException */
	public default boolean validateFormat() throws IllegalArgumentException {

		final ResultHandler store = this.getStore();
		if (store == null) {
			throw new IllegalArgumentException("Invalid 'store': value=" + store);
		}
		if (store == ResultHandler.FA_BNN_NXT) {
			throw new IllegalArgumentException("Invalid 'store' not BNN_NXT: value=" + store);
		}

		final int constant = this.getConstant();
		final VOFmt operation = this.getOperation();
		if (constant > operation.getConstantMaxValue() || constant < operation.getConstantMinValue()) {
			throw new IllegalArgumentException(
					"Constant out of range, value=" + constant + ", max=" + operation.getConstantMaxValue() + ", min=" + operation.getConstantMinValue());
		}

		return true;
	}
}
