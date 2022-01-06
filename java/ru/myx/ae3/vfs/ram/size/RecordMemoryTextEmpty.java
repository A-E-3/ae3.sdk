/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryTextEmpty extends RecordMemory {

	static final RecordMemory INSTANCE = new RecordMemoryTextEmpty();

	private RecordMemoryTextEmpty() {

		//
	}

	@Override
	public int compareTo(final RecordMemory o) {

		final CharSequence other = o.getText();
		return other == null
			? 1
			: other.length() > 0
				? -1
				: 0;
	}

	@Override
	public boolean equals(final Object object) {

		return this == object || object instanceof RecordMemoryText && ((RecordMemoryText) object).text.length() == 0;
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

		return Guid.GUID_STRING_EMPTY.getInlineBaseValue();
	}

	@Override
	public Guid getPrimitiveGuid() {

		/** empty binary text is considered to be an empty string. */
		return Guid.GUID_STRING_EMPTY;
		// return Guid.GUID_TEXT_EMPTY;
	}

	@Override
	public Object getPrimitiveValue() {

		return "";
	}

	@Override
	public CharSequence getText() {

		return "";
	}

	@Override
	public int hashCode() {

		return "".hashCode();
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

		return true;
	}

	@Override
	public String toString() {

		return "";
	}
}
