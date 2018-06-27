package ru.myx.ae3.vfs.status;

import ru.myx.ae3.Engine;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

class ReferenceStatus implements ArsReference<RecordStatus>, Value<ReferenceStatus> {
	
	private final RecordStatus	key;
	
	private final RecordStatus	parent;
	
	private final RecordStatus	target;
	
	ReferenceStatus(final RecordStatus parent, final RecordStatus key) {
		this.parent = parent;
		this.key = key;
		this.target = key;
	}
	
	ReferenceStatus(final RecordStatus parent, final RecordStatus key, final RecordStatus target) {
		this.parent = parent;
		this.key = key;
		this.target = target;
	}
	
	@Override
	public ReferenceStatus baseValue() {
		return this;
	}
	
	@Override
	public RecordStatus getKey() {
		return this.key;
	}
	
	@Override
	public String getKeyString() {
		return this.key.key;
	}
	
	@Override
	public long getLastModified() {
		return Engine.fastTime();
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return TreeLinkType.PUBLIC_TREE_REFERENCE;
	}
	
	@Override
	public RecordStatus getSource() {
		return this.parent;
	}
	
	@Override
	public RecordStatus getTarget() {
		return this.target;
	}
	
	@Override
	public boolean isExist() {
		return true;
	}
	
	@Override
	public String toString() {
		return "REFSTATUS{key=" + this.key + "}";
	}
}
