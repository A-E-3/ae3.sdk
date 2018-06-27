/**
 * 
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryBinaryEmpty extends RecordMemory {
	static final RecordMemory	INSTANCE	= new RecordMemoryBinaryEmpty();
	
	
	private RecordMemoryBinaryEmpty() {
	
		//
	}
	
	
	@Override
	public int compareTo(
			final RecordMemory o) {
	
		final TransferCopier other = o.getBinary();
		return other == null
				? 1
				: other.length() > 0
						? -1
						: 0;
	}
	
	
	@Override
	public boolean equals(
			final Object object) {
	
		return this == object || object instanceof RecordMemoryBinary && ((RecordMemoryBinary) object).binary.length() == 0;
	}
	
	
	@Override
	public TransferCopier getBinary() {
	
		return TransferCopier.NUL_COPIER;
	}
	
	
	@Override
	public long getBinaryContentLength() {
	
		return 0;
	}
	
	
	@Override
	public Map<RecordMemoryKey, ReferenceMemory> getCollection() {
	
		return null;
	}
	
	
	@Override
	public String getKeyString() {
	
		return null;
	}
	
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
	
		return TransferCopier.NUL_COPIER;
	}
	
	
	@Override
	public Guid getPrimitiveGuid() {
	
		return Guid.GUID_BYTES_EMPTY;
	}
	
	
	@Override
	public Object getPrimitiveValue() {
	
		return TransferCopier.NUL_COPIER;
	}
	
	
	@Override
	public CharSequence getText() {
	
		return null;
	}
	
	
	@Override
	public int hashCode() {
	
		return TransferCopier.NUL_COPIER.hashCode();
	}
	
	
	@Override
	public boolean isBinary() {
	
		return true;
	}
	
	
	@Override
	public boolean isCharacter() {
	
		return true;
	}
	
	
	@Override
	public boolean isContainer() {
	
		return false;
	}
	
	
	@Override
	public boolean isPrimitive() {
	
		return false;
	}
	
	
	@Override
	public String toString() {
	
		return "";
	}
}
