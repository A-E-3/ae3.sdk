package ru.myx.ae3.vfs.filesystem;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;

class RecordFilesystemBinaryTextTemplate extends RecordFilesystemBinaryTemplate {
	TransferCopier	binary;
	
	/**
	 * Container template
	 */
	RecordFilesystemBinaryTextTemplate(final TransferCopier binary) {
		super( binary );
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
	public boolean isCharacter() {
		return true;
	}
}
