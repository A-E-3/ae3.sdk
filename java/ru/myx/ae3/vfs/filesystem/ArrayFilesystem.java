package ru.myx.ae3.vfs.filesystem;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

abstract class ArrayFilesystem extends ArrayList<ReferenceFilesystem> implements ArsArray<ReferenceFilesystem>,
		Value<ArrayFilesystem> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500142593281745192L;
	
	ArrayFilesystem() {
		//
	}
	
	@Override
	public ArrayFilesystem baseValue() {
		return this;
	}
}
