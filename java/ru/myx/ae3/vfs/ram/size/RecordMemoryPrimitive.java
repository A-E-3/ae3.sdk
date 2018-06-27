/**
 * 
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.know.Guid;

class RecordMemoryPrimitive extends RecordMemory {
	final Guid	primitive;
	
	RecordMemoryPrimitive(final Guid primitive) {
		assert primitive != null : "NULL primitive";
		this.primitive = primitive;
	}
	
	@Override
	public int compareTo(final RecordMemory o) {
		if (o == null) {
			return 1;
		}
		{
			final Guid prim = this.primitive;
			if (prim != null) {
				final Object value = prim.getInlineValue();
				if (value instanceof String) {
					final String second = o.getKeyString();
					if (second != null) {
						return ((String) value).compareTo( second );
					}
				}
				final Guid second = o.getPrimitiveGuid();
				if (second != null) {
					return prim.compareTo( second );
				}
				return -1;
			}
		}
		{
			final String key = this.getKeyString();
			if (key != null) {
				final String second = o.getKeyString();
				if (second != null) {
					return key.compareTo( second );
				}
				return -1;
			}
		}
		return 1;
	}
	
	@Override
	public boolean equals(final Object object) {
		return this == object
				|| this.primitive != null
				&& object instanceof RecordMemoryPrimitive
				&& this.primitive.equals( ((RecordMemoryPrimitive) object).primitive );
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
		return String.valueOf( this.primitive.getInlineValue() );
	}
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
		return this.primitive == null
				? null
				: this.primitive.getInlineBaseValue();
	}
	
	@Override
	public Guid getPrimitiveGuid() {
		return this.primitive;
	}
	
	@Override
	public Object getPrimitiveValue() {
		return this.primitive == null
				? null
				: this.primitive.getInlineValue();
	}
	
	@Override
	public CharSequence getText() {
		final BaseObject value = this.primitive.getInlineBaseValue();
		return value instanceof CharSequence
				? (CharSequence) value
				: null;
	}
	
	@Override
	public int hashCode() {
		return this.primitive == null
				? System.identityHashCode( this )
				: this.primitive == null
						? 0
						: this.primitive.hashCode();
	}
	
	@Override
	public boolean isBinary() {
		return false;
	}
	
	@Override
	public boolean isCharacter() {
		return this.primitive != null && this.primitive.isInlineString();
	}
	
	@Override
	public boolean isContainer() {
		return false;
	}
	
	@Override
	public boolean isPrimitive() {
		return this.primitive != null;
	}
	
	@Override
	public String toString() {
		return this.primitive == null
				? null
				: String.valueOf( this.primitive );
	}
}
