/**
 * 
 */
package ru.myx.ae3.vfs.ram.size;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsReference;

final class ReferenceMemory implements Value<ReferenceMemory>, ArsReference<RecordMemory> {
	RecordMemory	source;
	
	RecordMemory	key;
	
	long			lastModified;
	
	TreeLinkType	mode;
	
	RecordMemory	target;
	
	ReferenceMemory(final RecordMemory source,
			final RecordMemory key,
			final TreeLinkType mode,
			final long lastModified,
			final RecordMemory target) {
		this.source = source;
		this.key = key;
		this.mode = mode;
		this.lastModified = lastModified;
		this.target = target;
	}
	
	@Override
	public ReferenceMemory baseValue() {
		return this;
	}
	
	@Override
	public boolean equals(final Object object) {
		return object == this || object instanceof ReferenceMemory && this.key.equals( ((ReferenceMemory) object).key );
	}
	
	@Override
	public RecordMemory getKey() {
		return this.key;
	}
	
	@Override
	public String getKeyString() {
		return this.key.getKeyString();
	}
	
	@Override
	public long getLastModified() {
		return this.lastModified;
	}
	
	@Override
	public TreeLinkType getLinkageMode() {
		return this.mode;
	}
	
	@Override
	public RecordMemory getSource() {
		return this.source;
	}
	
	@Override
	public RecordMemory getTarget() {
		return this.target;
	}
	
	@Override
	public int hashCode() {
		return this.key.hashCode();
	}
	
	@Override
	public boolean isExist() {
		return this.target != null;
	}
	
	@Override
	public String toString() {
		return "REFERENCE[" + this.source + ", " + this.key + ", " + this.target + "]";
	}
}
