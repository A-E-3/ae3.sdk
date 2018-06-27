package ru.myx.ae3.vfs.zip;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsRecord;

final class RecordZip implements Value<RecordZip>, ArsRecord, Comparable<RecordZip> {
	String			key;
	
	StorageImplZip	storage;
	
	TransferCopier	content;
	
	RecordZip(final StorageImplZip storage, final String key, final TransferCopier content) {
		this.storage = storage;
		this.key = key;
		this.content = content;
	}
	
	@Override
	public RecordZip baseValue() {
		return this;
	}
	
	@Override
	public int compareTo(final RecordZip o) {
		assert this.key != null : "Can compare keys only!";
		return this.key.compareTo( o.key );
	}
	
	@Override
	public boolean equals(final Object o) {
		return this.key == null
				? super.equals( o )
				: o instanceof RecordZip
						? this.key.equals( ((RecordZip) o).key )
						: false;
	}
	
	@Override
	public long getBinaryContentLength() {
		return this.content == null
				? 0
				: this.content.length();
	}
	
	@Override
	public String getKeyString() {
		return this.key;
	}
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
		return Base.forString( this.key );
	}
	
	@Override
	public Guid getPrimitiveGuid() {
		return Guid.forString( this.key );
	}
	
	@Override
	public Object getPrimitiveValue() {
		return this.key;
	}
	
	@Override
	public int hashCode() {
		return this.key == null
				? super.hashCode()
				: this.key.hashCode();
	}
	
	@Override
	public boolean isBinary() {
		return this.content != null;
	}
	
	@Override
	public boolean isCharacter() {
		return false;
	}
	
	@Override
	public boolean isContainer() {
		return this.storage.children.get( this ) != null;
	}
	
	@Override
	public boolean isPrimitive() {
		return this.key != null;
	}
	
	@Override
	public String toString() {
		return "ZIPFSREC{" + this.key + "}";
	}
}
