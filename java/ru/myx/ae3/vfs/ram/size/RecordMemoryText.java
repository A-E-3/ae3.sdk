/**
 *
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryText extends RecordMemory {

	final CharSequence text;

	RecordMemoryText(final CharSequence text) {

		assert text != null : "NULL key";
		this.text = text;
		/** TODO: create non-inline, non-primitive guid! */
		// this.primitive = Guid.
	}

	@Override
	public int compareTo(final RecordMemory o) {

		final CharSequence other = o.getText();
		return other == null
			? 1
			: this.text.toString().compareTo(other.toString());
	}

	@Override
	public boolean equals(final Object object) {

		return this == object || object instanceof RecordMemoryText && this.text.equals(((RecordMemoryText) object).text);
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

		return Guid.forBinaryTextUtf8(this.text, null, true);
	}

	@Override
	public Object getPrimitiveValue() {

		final Guid guid = this.getPrimitiveGuid();
		return guid == null
			? null
			: guid.getInlineValue();
	}

	@Override
	public CharSequence getText() {

		return this.text;
	}

	@Override
	public int hashCode() {

		return this.text.hashCode();
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

		return this.text.toString();
	}
}
