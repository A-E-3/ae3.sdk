package ru.myx.ae3.vfs.union;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.ars.ArsRecord;
import ru.myx.ae3.vfs.ars.ArsReference;

/**
 * implements both, record and reference at the same time
 * 
 * @author myx
 * 
 */
class RecordReferenceUnion implements Value<RecordReferenceUnion>, ArsRecord, ArsReference<RecordReferenceUnion> {
	
	/**
	 * source record
	 */
	RecordReferenceUnion	source;
	
	/**
	 * current entries, all non-null
	 */
	Entry[]					entries;
	
	
	/**
	 * For templates
	 */
	RecordReferenceUnion() {
	
		this.entries = null;
	}
	
	
	/**
	 * For normal (including root) records
	 * 
	 * @param entries
	 */
	RecordReferenceUnion(final RecordReferenceUnion source, final Entry[] entries) {
	
		assert entries != null : "NULL value";
		assert entries.length > 0 : "empty array!";
		this.source = source;
		this.entries = entries;
	}
	
	
	@Override
	public RecordReferenceUnion baseValue() {
	
		return this;
	}
	
	
	Value<? extends TransferCopier> getBinaryContent() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toBinary().getBinaryContent();
			}
		}
		return null;
	}
	
	
	@Override
	public long getBinaryContentLength() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toBinary().getBinaryContentLength();
			}
		}
		return 0;
	}
	
	
	@Override
	public RecordReferenceUnion getKey() {
	
		return this;
	}
	
	
	/**
	 * overridden in actual 'key' class so this implementation should never be
	 * called in real life
	 */
	@Override
	public String getKeyString() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			return entry.getKey();
		}
		return null;
	}
	
	
	@Override
	public long getLastModified() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.getLastModified();
			}
		}
		return -1L;
	}
	
	
	@Override
	public TreeLinkType getLinkageMode() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.getMode();
			}
		}
		return null;
	}
	
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toPrimitive().getPrimitiveValue( null );
			}
		}
		return null;
	}
	
	
	@Override
	public Guid getPrimitiveGuid() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toPrimitive().getPrimitiveGuid();
			}
		}
		return null;
	}
	
	
	@Override
	public Object getPrimitiveValue() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toPrimitive().getPrimitiveValue();
			}
		}
		return null;
	}
	
	
	@Override
	public RecordReferenceUnion getSource() {
	
		return this.source;
	}
	
	
	@Override
	public RecordReferenceUnion getTarget() {
	
		return this;
	}
	
	
	Value<? extends CharSequence> getTextContent() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.toCharacter().getTextContent();
			}
		}
		return BaseString.EMPTY;
	}
	
	
	@Override
	public boolean isBinary() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.isBinary();
			}
		}
		return false;
	}
	
	
	@Override
	public boolean isCharacter() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.isCharacter();
			}
		}
		return false;
	}
	
	
	@Override
	public boolean isContainer() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.isContainer();
			}
		}
		return false;
	}
	
	
	@Override
	public boolean isExist() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public boolean isPrimitive() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return entry.isPrimitive();
			}
		}
		return false;
	}
	
	
	@Override
	public String toString() {
	
		for (final Entry entry : this.entries) {
			if (entry == null) {
				continue;
			}
			if (entry.isExist()) {
				return "UNIONFS-RECREF{" + entry.toString() + "}";
			}
		}
		return "UNIONFS-RECREF{" + this.entries[0] + "}";
	}
}
