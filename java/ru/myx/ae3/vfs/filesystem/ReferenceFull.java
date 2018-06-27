package ru.myx.ae3.vfs.filesystem;

import ru.myx.ae3.vfs.TreeLinkType;

final class ReferenceFull extends ReferenceFilesystem {
	RecordFilesystem	key;
	
	TreeLinkType		mode;
	
	ReferenceFull(final RecordFilesystem key, final TreeLinkType mode, final RecordFilesystem target) {
		super( target );
		this.key = key;
		this.mode = mode;
	}
	
	@Override
	public RecordFilesystem getKey() {
		return this.key;
	}
	
	@Override
	public String getKeyString() {
		return this.key.getKeyString();
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return this.mode;
	}
	
	@Override
	public RecordFilesystem getTarget() {
		return this.target;
	}
	
	@Override
	public boolean isExist() {
		return this.target.file.exists();
	}
}
