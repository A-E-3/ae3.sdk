package ru.myx.ae3.vfs.union;

import java.util.ArrayList;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.ars.ArsRefArray;

final class ArrayUnion extends ArrayList<RecordReferenceUnion> implements ArsRefArray<RecordReferenceUnion>,
		Value<ArrayUnion> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7500145693281745192L;
	
	ArrayUnion() {
		//
	}
	
	@Override
	public ArrayUnion baseValue() {
		return this;
	}
}
