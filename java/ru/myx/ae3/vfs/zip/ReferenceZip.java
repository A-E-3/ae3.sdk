package ru.myx.ae3.vfs.zip;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

class ReferenceZip implements ArsReference<RecordZip>, Value<ReferenceZip> {
	RecordZip	source;
	
	RecordZip	key;
	
	RecordZip	target;
	
	/**
	 * Root reference
	 * 
	 * @param target
	 */
	ReferenceZip(final RecordZip target) {
		assert target != null : "Null value";
		this.source = null;
		this.key = null;
		this.target = target;
	}
	
	/**
	 * Root reference
	 * 
	 * @param target
	 */
	ReferenceZip(final RecordZip key, final RecordZip target) {
		assert key != null : "NULL key";
		assert target != null : "Null value";
		this.source = null;
		this.key = key;
		this.target = target;
	}
	
	ReferenceZip(final RecordZip source, final RecordZip key, final RecordZip target) {
		assert source != null : "Null value";
		assert source.isContainer() : "Source is not a container";
		assert key != null : "Null value";
		assert key.key != null : "Key is not key";
		assert target != null : "Null value";
		this.source = source;
		this.key = key;
		this.target = target;
	}
	
	@Override
	public final ReferenceZip baseValue() {
		return this;
	}
	
	@Override
	public RecordZip getKey() {
		return this.key;
	}
	
	@Override
	public String getKeyString() {
		return this.key.key;
	}
	
	@Override
	public long getLastModified() {
		return this.target == null
				? -1L
				: this.target.storage.lastModified;
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return TreeLinkType.PUBLIC_TREE_REFERENCE;
	}
	
	@Override
	public RecordZip getSource() {
		return this.source;
	}
	
	@Override
	public RecordZip getTarget() {
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
		return "ZIPFSREF[" + this.source + ", " + this.target + "]";
	}
}
