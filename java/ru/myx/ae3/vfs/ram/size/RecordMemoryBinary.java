/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryBinary extends RecordMemory {

	final TransferCopier binary;

	RecordMemoryBinary(final TransferCopier binary) {

		assert binary != null : "NULL key";
		this.binary = binary;
		/** TODO: create non-inline, non-primitive guid! */
		// this.primitive = Guid.
	}

	@Override
	public int compareTo(final RecordMemory o) {

		final TransferCopier other = o.getBinary();
		return other == null
			? 1
			: this.binary.compareTo(other);
	}

	@Override
	public boolean equals(final Object object) {

		return this == object || object instanceof RecordMemoryBinary && this.binary.equals(((RecordMemoryBinary) object).binary);
	}

	@Override
	public TransferCopier getBinary() {

		return this.binary;
	}

	@Override
	public long getBinaryContentLength() {

		return this.binary.length();
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

		final Guid guid = this.getPrimitiveGuid();
		return guid == null
			? null
			: guid.getInlineBaseValue();
	}

	@Override
	public Guid getPrimitiveGuid() {

		return Guid.forBinaryChecksum(this.binary);
	}

	@Override
	public BaseObject getPrimitiveValue() {

		final Guid guid = this.getPrimitiveGuid();
		return guid == null
			? null
			: guid.getInlineBaseValue();
	}

	@Override
	public CharSequence getText() {

		return null;
	}

	@Override
	public int hashCode() {

		return this.binary.hashCode();
	}

	@Override
	public boolean isBinary() {

		return true;
	}

	@Override
	public boolean isCharacter() {

		return false;
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

		return this.binary.toStringUtf8();
	}
}
