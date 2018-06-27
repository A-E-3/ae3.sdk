package ru.myx.ae3.vfs.ram.size;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

final class ArrayMemory extends ArrayList<ReferenceMemory> implements ArsArray<ReferenceMemory>, Value<ArrayMemory> {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7225552579760837756L;
	
	ArrayMemory() {
		//
	}
	
	@Override
	public ArrayMemory baseValue() {
		return this;
	}
}
