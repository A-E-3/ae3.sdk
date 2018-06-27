package ru.myx.ae3.vfs.filesystem;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

class ReferenceFilesystem implements ArsReference<RecordFilesystem>, Value<ReferenceFilesystem> {
	RecordFilesystem	target;
	
	ReferenceFilesystem(final RecordFilesystem target) {
		assert target != null : "Null target";
		this.target = target;
	}
	
	@Override
	public final ReferenceFilesystem baseValue() {
		return this;
	}
	
	@Override
	public RecordFilesystem getKey() {
		return this.target;
	}
	
	@Override
	public String getKeyString() {
		return this.target.getKeyString();
	}
	
	@Override
	public long getLastModified() {
		return this.target.file.lastModified();
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return this.target.file.isHidden()
				? TreeLinkType.HIDDEN_TREE_REFERENCE
				: TreeLinkType.PUBLIC_TREE_REFERENCE;
	}
	
	@Override
	public RecordFilesystem getSource() {
		return new RecordFilesystem( this.target.file.getParentFile() );
	}
	
	@Override
	public RecordFilesystem getTarget() {
		return this.target;
	}
	
	@Override
	public boolean isExist() {
		return this.target.file.exists();
	}
	
	@Override
	public String toString() {
		return "REF[" + this.target + "]";
	}
}
