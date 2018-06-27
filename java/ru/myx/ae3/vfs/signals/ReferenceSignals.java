package ru.myx.ae3.vfs.signals;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

class ReferenceSignals implements ArsReference<RecordSignals>, Value<ReferenceSignals> {
	
	private final RecordSignals	key;
	
	private final RecordSignals	root;
	
	private final RecordSignals	target;
	
	ReferenceSignals(final RecordSignals root, final RecordSignals key) {
		this.root = root;
		this.key = key;
		this.target = key;
	}
	
	ReferenceSignals(final RecordSignals root, final RecordSignals key, final RecordSignals target) {
		this.root = root;
		this.key = key;
		this.target = target;
	}
	
	@Override
	public ReferenceSignals baseValue() {
		return this;
	}
	
	@Override
	public RecordSignals getKey() {
		return this.key;
	}
	
	@Override
	public String getKeyString() {
		return this.key.key;
	}
	
	@Override
	public long getLastModified() {
		return 0;
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return TreeLinkType.PUBLIC_TREE_REFERENCE;
	}
	
	@Override
	public RecordSignals getSource() {
		return this.root;
	}
	
	@Override
	public RecordSignals getTarget() {
		return this.target;
	}
	
	@Override
	public boolean isExist() {
		return true;
	}
	
	@Override
	public String toString() {
		return "REFSIGNALS{key=" + this.key + "}";
	}
}
