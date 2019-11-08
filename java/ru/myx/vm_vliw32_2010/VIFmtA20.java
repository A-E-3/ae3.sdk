package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA20 extends VIFmt {

	/**
	 *
	 */
	public static final int CNST_BITS = 8;

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 7;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 7) - 1;
	
	@Override
	default int getArgumentCount() {

		return 2;
	}
	
	@Override
	default int getConstantMaxValue() {
		
		return VIFmtA20.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {
		
		return VIFmtA20.CNST_MIN;
	}
}
