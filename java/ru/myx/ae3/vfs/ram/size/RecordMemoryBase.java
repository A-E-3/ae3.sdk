/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryBase extends RecordMemory {

	final Object value;

	RecordMemoryBase(final BaseObject primitive) {

		assert primitive != null : "NULL primitive";
		final Object value = primitive.baseValue();
		this.value = value == null
			? primitive
			: value;
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

		return this == object || this.value != null && object instanceof RecordMemoryBase && this.value.equals(((RecordMemoryBase) object).value);
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

		return this.value.toString();
	}

	@Override
	public BaseObject getPrimitiveBaseValue() {

		return Base.forUnknown(this.value);
	}

	@Override
	public Guid getPrimitiveGuid() {

		return Guid.forUnknown(this.value);
	}

	@Override
	public Object getPrimitiveValue() {

		return this.value;
	}

	@Override
	public CharSequence getText() {

		return this.value instanceof CharSequence
			? (CharSequence) this.value
			: null;
	}

	@Override
	public int hashCode() {

		return this.value == null
			? System.identityHashCode(this)
			: this.value == null
				? 0
				: this.value.hashCode();
	}

	@Override
	public boolean isBinary() {

		return false;
	}

	@Override
	public boolean isCharacter() {

		return this.value instanceof CharSequence;
	}

	@Override
	public boolean isContainer() {

		return false;
	}

	@Override
	public boolean isPrimitive() {

		return this.value != null;
	}

	@Override
	public String toString() {

		return this.value == null
			? null
			: String.valueOf(this.value);
	}
}
