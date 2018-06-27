package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseSealed;

final class ParserArguments extends BaseSealed {
	
	private String[]	array;
	
	private int			size;
	
	ParserArguments() {
		super( BaseObject.PROTOTYPE );
		this.array = new String[4];
	}
	
	void add(final String name) {
		if (this.array.length == this.size) {
			final String[] replacement = new String[this.array.length << 1];
			System.arraycopy( this.array, 0, replacement, 0, this.array.length );
			this.array = replacement;
		}
		this.array[this.size++] = name;
	}
	
	String[] toArray() {
		final String[] result = new String[this.size];
		System.arraycopy( this.array, 0, result, 0, this.size );
		return result;
	}
}
