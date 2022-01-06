/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryKey extends RecordMemory {

	private final String key;

	RecordMemoryKey(final String key) {

		assert key != null : "NULL key";
		this.key = key;
		/** TODO: create non-inline, non-primitive guid! */
		// this.primitive = Guid.
	}

	@Override
	public int compareTo(final RecordMemory o) {

		if (o == null) {
			return 1;
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
		return 1;
	}

	@Override
	public boolean equals(final Object object) {

		return this == object || object instanceof RecordMemoryKey && this.key.equals(((RecordMemoryKey) object).key);
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

		return null;
	}

	@Override
	public String getKeyString() {

		return this.key;
	}

	@Override
	public BaseObject getPrimitiveBaseValue() {

		return Base.forString(this.key);
	}

	@Override
	public Guid getPrimitiveGuid() {

		return Guid.forString(this.key);
	}

	@Override
	public String getPrimitiveValue() {

		return this.key;
	}

	@Override
	public CharSequence getText() {

		return this.key;
	}

	@Override
	public int hashCode() {

		return this.key.hashCode();
	}

	@Override
	public boolean isBinary() {

		return false;
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

		return Guid.forString(this.key) != null;
	}

	@Override
	public String toString() {

		return this.key;
	}
}
