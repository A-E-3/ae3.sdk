/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA0_AXXX_X_SN_NXT extends InstructionIA0A {
	
	private final int constant;

	private final VOFmtA0 operation;

	/** @param operation
	 * @param constant
	 * @param store
	 * @param state */
	IA0_AXXX_X_SN_NXT(final VOFmtA0 operation, final int constant) {
		
		this.operation = operation;
		this.constant = constant;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess process) throws Exception {
		
		return this.operation.execute(process, this.constant, ResultHandler.FB_BSN_NXT);
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
		
		return 1;
	}

	@Override
	public ResultHandler getStore() {
		
		return ResultHandler.FB_BSN_NXT;
	}

	@Override
	public boolean isRelativeAddressInConstant() {
		
		return this.operation.isRelativeAddressInConstant();
	}

}
