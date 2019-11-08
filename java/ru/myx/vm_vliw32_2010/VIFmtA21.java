package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA21 extends VIFmt {

	/**
	 *
	 */
	public static final int CNST_BITS = 12;

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 11;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 11) - 1;
	
	@Override
	default int getArgumentCount() {

		return 2;
	}
	
	@Override
	default int getConstantMaxValue() {
		
		return VIFmtA21.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {
		
		return VIFmtA21.CNST_MIN;
	}
}
