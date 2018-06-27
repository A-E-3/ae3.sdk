package ru.myx.ae3.vfs.zip;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

final class ArrayZip extends ArrayList<ReferenceZip> implements ArsArray<ReferenceZip>, Value<ArrayZip> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500144274281745192L;
	
	ArrayZip() {
		//
	}
	
	@Override
	public ArrayZip baseValue() {
		return this;
	}
}
