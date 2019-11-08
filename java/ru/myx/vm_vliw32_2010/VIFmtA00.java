package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA00 extends VIFmt {
	
	/**
	 *
	 */
	public static final int CNST_BITS = 22;
	
	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 21;

	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 21) - 1;
	
	@Override
	default int getArgumentCount() {

		return 0;
	}

	@Override
	default int getConstantMaxValue() {

		return VIFmtA00.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {

		return VIFmtA00.CNST_MIN;
	}
}
