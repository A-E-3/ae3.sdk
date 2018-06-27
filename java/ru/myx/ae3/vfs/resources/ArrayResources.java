package ru.myx.ae3.vfs.resources;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsArray;

final class ArrayResources extends ArrayList<ReferenceResources> implements ArsArray<ReferenceResources>,
		Value<ArrayResources> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500145693281745192L;
	
	ArrayResources() {
		//
	}
	
	@Override
	public ArrayResources baseValue() {
		return this;
	}
}
