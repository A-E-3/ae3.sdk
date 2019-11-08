package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA01 extends VIFmt {

	/**
	 *
	 */
	public static final int CNST_BITS = 23;

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 22;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 22) - 1;

	@Override
	default int getArgumentCount() {

		return 0;
	}
	@Override
	default int getConstantMaxValue() {
		
		return VIFmtA01.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {
		
		return VIFmtA01.CNST_MIN;
	}
}
