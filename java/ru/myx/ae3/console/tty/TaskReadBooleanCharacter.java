package ru.myx.ae3.console.tty;

class TaskReadBooleanCharacter extends ConsoleTtyTask<Boolean> {
	private final String	title;
	
	private final Boolean	defaultValue;
	
	TaskReadBooleanCharacter(final String title, final Boolean defaultValue) {
		this.title = title;
		this.defaultValue = defaultValue;
	}
	
	@Override
	ConsoleTtyTask<?> consumeNext(final ConsoleTty console, final int b) {
		switch (b) {
		case 'y':
		case 'Y':
			console.internEcho( b );
			console.setStateNormal();
			this.setResult( Boolean.TRUE );
			return null;
		case 'n':
		case 'N':
			console.internEcho( b );
			console.setStateNormal();
			this.setResult( Boolean.FALSE );
			return null;
		case '\n':
			if (this.defaultValue != null) {
				console.internEcho( this.defaultValue.booleanValue()
						? 'y'
						: 'n' );
				console.setStateNormal();
				this.setResult( this.defaultValue );
				return null;
			}
			//$FALL-THROUGH$
		default:
			return this;
		}
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		console.sendStatus( this.title + (this.defaultValue == null
				? " (y/n): "
				: "[" + (this.defaultValue.booleanValue()
						? "y"
						: "n") + "] (y/n): ") );
	}
	
}
