package ru.myx.vm_vliw32_2010;

/** @author myx */
public interface VOFmtA11 extends VOFmtA1, VIFmtA11 {

	/** Return an operation that will definitely produce detachable result
	 *
	 * @return */
	@Override
	default VOFmtA11 execStackResult() {

		return this;
	}
}
