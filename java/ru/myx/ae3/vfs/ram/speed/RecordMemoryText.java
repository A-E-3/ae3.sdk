/**
 * 
 */
package ru.myx.ae3.vfs.ram.speed;


class RecordMemoryText extends RecordMemory {
	
	RecordMemoryText(final CharSequence binary) {
		super( binary );
	}
	
	@Override
	public boolean isCharacter() {
		return true;
	}
	
	@Override
	public String toString() {
		return "BINARY-TEXT";
	}
}
