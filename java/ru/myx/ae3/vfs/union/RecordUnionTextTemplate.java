package ru.myx.ae3.vfs.union;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.common.Value;

class RecordUnionTextTemplate extends RecordReferenceUnion {
	CharSequence	text;
	
	
	/**
	 * Container template
	 */
	RecordUnionTextTemplate(final CharSequence text) {
	
		super();
		assert text != null : "NULL value";
		this.text = text;
	}
	
	
	@Override
	public Value<? extends CharSequence> getTextContent() {
	
		return Base.forString( this.text );
	}
	
	
	@Override
	public boolean isContainer() {
	
		return false;
	}
	
	
	@Override
	public boolean isCharacter() {
	
		return true;
	}
}
