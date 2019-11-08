/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA0_AXXX_X_XX_XXX extends InstructionIA0A {

	private final int constant;
	
	private final VOFmtA0 operation;
	
	private final ResultHandler store;
	
	/** @param operation
	 * @param constant
	 * @param store */
	IA0_AXXX_X_XX_XXX(final VOFmtA0 operation, final int constant, final ResultHandler store) {

		this.operation = operation;
		this.constant = constant;
		this.store = store;
	}
	
	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {

		return this.operation.execute(process, this.constant, this.store);
	}
	
	/** @return constant */
	@Override
	public final int getConstant() {

		return this.constant;
	}
	
	@Override
	public final int getOperandCount() {

		return this.operation.getStackInputCount(this.constant);
	}
	
	@Override
	public final VOFmtA0 getOperation() {

		return this.operation;
	}
	
	@Override
	public final int getResultCount() {

		return this.store.isStackPush()
			? 1
			: 0;
	}
	
	@Override
	public ResultHandler getStore() {

		return this.store;
	}
	
	@Override
	public boolean isRelativeAddressInConstant() {

		return this.operation.isRelativeAddressInConstant();
	}
	
}
