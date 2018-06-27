package ru.myx.ae3.vfs.union;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;

class RecordUnionKey extends RecordReferenceUnion {
	
	final String	key;
	
	/**
	 * Container template
	 */
	RecordUnionKey(final String key) {
		super();
		this.key = key;
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
	public RecordReferenceUnion getKey() {
		return this;
	}
	
	@Override
	public String getKeyString() {
		return this.key;
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
		return false;
	}
	
	@Override
	public boolean isPrimitive() {
		return false;
	}
}
