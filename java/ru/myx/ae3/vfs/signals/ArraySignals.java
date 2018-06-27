package ru.myx.ae3.vfs.signals;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

final class ArraySignals extends ArrayList<ReferenceSignals> implements ArsArray<ReferenceSignals>, Value<ArraySignals> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5913267375592170609L;
	
	@Override
	public ArraySignals baseValue() {
		return this;
	}
}
