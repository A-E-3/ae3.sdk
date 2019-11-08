package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VOFmt extends VIFmt {

	/** @param constant
	 * @return */
	default int getStackInputCount(final int constant) {
		
		return 0;
	}
	
	/** Only for good code dumps. Doesn't affect execution and compilation at all.
	 *
	 * @return false by default */
	default boolean isRelativeAddressInConstant() {
		
		return false;
	}
}
