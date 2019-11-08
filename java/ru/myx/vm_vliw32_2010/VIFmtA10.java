package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA10 extends VIFmt {
	
	/**
	 *
	 */
	public static final int CNST_BITS = 17;
	
	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 16;

	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 16) - 1;
	
	@Override
	default int getArgumentCount() {

		return 1;
	}

	@Override
	default int getConstantMaxValue() {

		return VIFmtA10.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {

		return VIFmtA10.CNST_MIN;
	}
}
