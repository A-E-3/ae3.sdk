/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.HashMap;
import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryCollection extends RecordMemory {

	Map<RecordMemoryKey, ReferenceMemory> collection = new HashMap<>(2, 2f);

	// Map<RecordMemoryKey, ReferenceMemory> collection = new TreeMap<>();

	RecordMemoryCollection() {

		//
	}

	@Override
	public int compareTo(final RecordMemory o) {

		if (o == null) {
			return 1;
		}
		{
			final Guid prim = this.getPrimitiveGuid();
			if (prim != null) {
				final Object value = prim.getInlineValue();
				if (value instanceof String) {
					final String second = o.getKeyString();
					if (second != null) {
						return ((String) value).compareTo(second);
					}
				}
				final Guid second = o.getPrimitiveGuid();
				if (second != null) {
					return prim.compareTo(second);
				}
				return -1;
			}
		}
		{
			final String key = this.getKeyString();
			if (key != null) {
				final String second = o.getKeyString();
				if (second != null) {
					return key.compareTo(second);
				}
				return -1;
			}
		}
		return 1;
	}

	@Override
	public boolean equals(final Object object) {

		return this == object;
	}

	@Override
	public TransferCopier getBinary() {

		return null;
	}

	@Override
	public long getBinaryContentLength() {

		return 0;
	}

	@Override
	public Map<RecordMemoryKey, ReferenceMemory> getCollection() {

		return this.collection;
	}

	@Override
	public String getKeyString() {

		return null;
	}

	@Override
	public BaseObject getPrimitiveBaseValue() {

		return BaseObject.UNDEFINED;
	}

	@Override
	public Guid getPrimitiveGuid() {

		return null;
	}

	@Override
	public Object getPrimitiveValue() {

		return null;
	}

	@Override
	public CharSequence getText() {

		return null;
	}

	@Override
	public int hashCode() {

		return System.identityHashCode(this);
	}

	@Override
	public boolean isBinary() {

		return false;
	}

	@Override
	public boolean isCharacter() {

		return false;
	}

	@Override
	public boolean isContainer() {

		return true;
	}

	@Override
	public boolean isPrimitive() {

		return false;
	}

	@Override
	public String toString() {

		return "COLLECTION";
	}
}
