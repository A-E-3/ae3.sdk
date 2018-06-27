/**
 * 
 */
package ru.myx.ae3.exec;

import com.sun.istack.internal.NotNull;

/**
 * @author myx
 * 
 */
abstract class IA2X_AAAA_XX_X_NN_NXT extends InstructionA2XI {
	
	
	@Override
	public final boolean isStackPush() {
		
		
		return false;
	}
	
	@Override
	@NotNull
	public final ResultHandler getStore() {
		
		
		return ResultHandler.FA_BNN_NXT;
	}
}
