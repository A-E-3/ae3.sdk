package ru.myx.ae3.parse;

public abstract class AbstractClikeCharacterParser<V> extends AbstractCharacterParser<V> {
	
	@Override
	protected final InputParser<V> onCharacter(char character) throws Exception {
		
		// TODO Auto-generated method stub
		return null;
	}
	
	protected abstract void onWhitespace(int index);
	protected abstract void onNewLine(int index);
	protected abstract void onToken(int index, String token);
	protected abstract void onStringLiteral(int index, CharSequence value);
}
