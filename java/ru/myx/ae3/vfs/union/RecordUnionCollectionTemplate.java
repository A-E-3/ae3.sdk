package ru.myx.ae3.vfs.union;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;

class RecordUnionCollectionTemplate extends RecordReferenceUnion {
	
	/**
	 * Container template
	 */
	RecordUnionCollectionTemplate() {
		super();
	}
	
	@Override
	public Value<TransferCopier> getBinaryContent() {
		return null;
	}
	
	@Override
	public long getBinaryContentLength() {
		return 0;
	}
	
	@Override
	public Object getPrimitiveValue() {
		return null;
	}
	
	@Override
	public boolean isBinary() {
		return false;
	}
	
	@Override
	public boolean isContainer() {
		return true;
	}
	
	@Override
	public boolean isPrimitive() {
		return false;
	}
}
