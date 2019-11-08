/**
 *
 */
package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class IA0_AXXX_X_NN_RET extends InstructionIA0A {

	private final int constant;

	private final VOFmtA0 operation;

	/** @param operation
	 * @param constant
	 * @param store
	 * @param state */
	IA0_AXXX_X_NN_RET(final VOFmtA0 operation, final int constant) {

		this.operation = operation;
		this.constant = constant;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {

		return this.operation.execute(ctx, this.constant, ResultHandler.FC_PNN_RET);
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

		return 0;
	}

	@Override
	public ResultHandler getStore() {

		return ResultHandler.FC_PNN_RET;
	}

	@Override
	public boolean isRelativeAddressInConstant() {

		return this.operation.isRelativeAddressInConstant();
	}

}
