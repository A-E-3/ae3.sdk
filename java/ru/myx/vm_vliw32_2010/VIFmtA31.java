package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VIFmtA31 extends VIFmt {

	/**
	 *
	 */
	public static final int CNST_BITS = 10;

	/**
	 *
	 */
	public static final int CNST_MAX = 1 << 9;
	
	/**
	 *
	 */
	public static final int CNST_MIN = -(1 << 9) - 1;

	@Override
	default int getArgumentCount() {
		
		return 3;
	}

	@Override
	default int getConstantMaxValue() {

		return VIFmtA31.CNST_MAX;
	}
	@Override
	default int getConstantMinValue() {

		return VIFmtA31.CNST_MIN;
	}
}
