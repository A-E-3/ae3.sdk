package ru.myx.ae3.vfs.status;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

final class ArrayStatus extends ArrayList<ReferenceStatus> implements ArsArray<ReferenceStatus>, Value<ArrayStatus> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5913267375592170609L;
	
	@Override
	public ArrayStatus baseValue() {
		return this;
	}
}
