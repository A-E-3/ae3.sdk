package ru.myx.ae3.vfs.resources;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

class ReferenceResources implements ArsReference<RecordResources>, Value<ReferenceResources> {
	RecordResources	source;
	
	RecordResources	target;
	
	ReferenceResources(final RecordResources source, final RecordResources target) {
		assert target != null : "Null value";
		assert target.key != null : "Null path";
		this.source = source;
		this.target = target;
	}
	
	@Override
	public final ReferenceResources baseValue() {
		return this;
	}
	
	@Override
	public RecordResources getKey() {
		return this.target;
	}
	
	@Override
	public String getKeyString() {
		return this.source == null || this.source.key.length() == 0
				? this.target.key
				: this.target.key.substring( this.source.key.length() + 1 );
	}
	
	@Override
	public long getLastModified() {
		return -1L;
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return TreeLinkType.PUBLIC_TREE_REFERENCE;
	}
	
	@Override
	public RecordResources getSource() {
		return this.source;
	}
	
	@Override
	public RecordResources getTarget() {
		return this.target;
	}
	
	@Override
	public boolean isExist() {
		/**
		 * kind of all of the references exist
		 */
		return true;
	}
	
	@Override
	public String toString() {
		return "RESFSREF[" + this.source + ", " + this.target + "]";
	}
}
