package ru.myx.vm_vliw32_2010;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
public interface VOFmtA0 extends VOFmt {

	/** @param process
	 * @param constant
	 * @param store
	 * @return code */
	ExecStateCode execute(ExecProcess process, int constant, ResultHandler store);
	
	/** @param constant
	 * @param store
	 * @param state
	 * @return */
	default InstructionIA instruction(final int constant, final ResultHandler store) {

		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return new IA0_AXXX_X_NN_NXT(this, constant);
		}
		if (store == ResultHandler.FC_PNN_RET) {
			return new IA0_AXXX_X_NN_RET(this, constant);
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return new IA0_AXXX_X_SN_NXT(this, constant);
		}
		return new IA0_AXXX_X_XX_XXX(this, constant, store);
	}
}
