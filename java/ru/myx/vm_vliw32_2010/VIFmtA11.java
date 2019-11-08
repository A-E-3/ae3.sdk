package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA11 extends VIFmt {
	
	/**
	 *
	 */
	public static final int CNST_BITS = 19;
	
	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 18;

	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 18) - 1;
	
	@Override
	default int getArgumentCount() {

		return 1;
	}

	@Override
	default int getConstantMaxValue() {

		return VIFmtA11.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {

		return VIFmtA11.CNST_MIN;
	}
}
