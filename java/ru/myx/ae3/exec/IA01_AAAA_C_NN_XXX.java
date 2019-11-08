/**
 *
 */
package ru.myx.ae3.exec;

/** @author myx */
abstract class IA01_AAAA_C_NN_XXX extends IA01_AAAA_X_NN_NXT {
	
	protected final int constant;

	/** @param constant
	 * @param store
	 * @param state */
	IA01_AAAA_C_NN_XXX(final int constant) {
		
		this.constant = constant;
	}

	@Override
	public final int getConstant() {
		
		return this.constant;
	}
}
