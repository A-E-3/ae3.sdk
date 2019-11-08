package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA30 extends VIFmt {
	
	/**
	 *
	 */
	public static final int CNST_BITS = 6;
	
	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 5;

	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 5) - 1;
	
	@Override
	default int getArgumentCount() {

		return 3;
	}
	
	@Override
	default int getConstantMaxValue() {
		
		return VIFmtA30.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {
		
		return VIFmtA30.CNST_MIN;
	}
}
