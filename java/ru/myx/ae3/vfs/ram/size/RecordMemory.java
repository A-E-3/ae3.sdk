/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsRecord;

abstract class RecordMemory implements Value<RecordMemory>, ArsRecord, Comparable<RecordMemory> {

	RecordMemory() {

		//
	}

	@Override
	public RecordMemory baseValue() {

		return this;
	}

	@Override
	public abstract int compareTo(final RecordMemory o);

	@Override
	public abstract boolean equals(final Object object);

	public abstract TransferCopier getBinary();

	@Override
	public abstract long getBinaryContentLength();

	public abstract Map<RecordMemoryKey, ReferenceMemory> getCollection();

	@Override
	public abstract String getKeyString();

	@Override
	public abstract BaseObject getPrimitiveBaseValue();

	@Override
	public abstract Guid getPrimitiveGuid();

	@Override
	public abstract Object getPrimitiveValue();

	public abstract CharSequence getText();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean isBinary();

	@Override
	public boolean isCharacter() {

		return false;
	}

	@Override
	public abstract boolean isContainer();

	@Override
	public abstract boolean isPrimitive();

	@Override
	public abstract String toString();
}
