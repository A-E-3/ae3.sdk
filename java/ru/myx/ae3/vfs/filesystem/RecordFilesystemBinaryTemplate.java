package ru.myx.ae3.vfs.filesystem;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;

class RecordFilesystemBinaryTemplate extends RecordFilesystem {
	TransferCopier	binary;
	
	/**
	 * Container template
	 */
	RecordFilesystemBinaryTemplate(final TransferCopier binary) {
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
