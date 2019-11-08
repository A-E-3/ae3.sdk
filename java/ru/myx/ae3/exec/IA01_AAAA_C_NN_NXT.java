/**
 *
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/** @author myx */
abstract class IA01_AAAA_C_NN_NXT extends IA01_AAAA_X_NN_NXT {

	protected final int constant;
	
	/** @param constant
	 * @param store
	 * @param state */
	IA01_AAAA_C_NN_NXT(final int constant) {

		this.constant = constant;
	}
	
	@Override
	public final int getConstant() {

		return this.constant;
	}
	
	@Override
	@NotNull
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
