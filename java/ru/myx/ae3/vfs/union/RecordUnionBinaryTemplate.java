package ru.myx.ae3.vfs.union;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;

class RecordUnionBinaryTemplate extends RecordReferenceUnion {
	TransferCopier	binary;
	
	/**
	 * Container template
	 */
	RecordUnionBinaryTemplate(final TransferCopier binary) {
		super();
		assert binary != null : "NULL value";
		this.binary = binary;
	}
	
	@Override
	public Value<TransferCopier> getBinaryContent() {
		return this.binary;
	}
	
	@Override
	public long getBinaryContentLength() {
		return this.binary.length();
	}
	
	@Override
	public boolean isBinary() {
		return true;
	}
	
	@Override
	public boolean isContainer() {
		return false;
	}
}
