/**
 * 
 */
package ru.myx.ae3.vfs.ram.speed;

import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsRecord;

class RecordMemory implements Value<RecordMemory>, ArsRecord, Comparable<RecordMemory> {
	Object								binary;
	
	Guid								primitive;
	
	Map<RecordMemory, ReferenceMemory>	collection;
	
	RecordMemory() {
		this.collection = new TreeMap<>();
		this.primitive = null;
	}
	
	RecordMemory(final CharSequence text) {
		this.binary = text;
		this.primitive = null;
	}
	
	RecordMemory(final Guid primitive) {
		this.primitive = primitive;
	}
	
	RecordMemory(final TransferCopier binary) {
		this.binary = binary;
		this.primitive = null;
	}
	
	@Override
	public RecordMemory baseValue() {
		return this;
	}
	
	@Override
	public int compareTo(final RecordMemory o) {
		return this.primitive.compareTo( o.primitive );
	}
	
	@Override
	public boolean equals(final Object object) {
		return this == object
				|| this.primitive != null
				&& object instanceof RecordMemory
				&& this.primitive.equals( ((RecordMemory) object).primitive );
	}
	
	@Override
	public long getBinaryContentLength() {
		return ((TransferCopier) this.binary).length();
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
	public int hashCode() {
		return this.primitive == null
				? System.identityHashCode( this )
				: this.primitive == null
						? 0
						: this.primitive.hashCode();
	}
	
	@Override
	public boolean isBinary() {
		return this.binary instanceof TransferCopier;
	}
	
	@Override
	public boolean isCharacter() {
		return this.primitive != null && this.primitive.isInlineString() || this.binary instanceof CharSequence;
	}
	
	@Override
	public boolean isContainer() {
		return this.collection != null;
	}
	
	@Override
	public boolean isPrimitive() {
		return this.primitive != null;
	}
	
	@Override
	public String toString() {
		return this.primitive == null
				? this.collection == null
						? this.binary == null
								? null
								: this.binary instanceof CharSequence
										? "TEXT"
										: "BINARY"
						: "COLLECTION"
				: String.valueOf( this.primitive );
	}
}
