/**
 *
 */
package ru.myx.ae3.exec;


/** @author myx */
abstract class IA01_AAAA_X_NN_NXT extends InstructionA01 {

	@Override
	public final int getResultCount() {

		return 0;
	}
	
	@Override
	public ResultHandler getStore() {

		return ResultHandler.FA_BNN_NXT;
	}
}
