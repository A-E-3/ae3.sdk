package ru.myx.ae3.console.tty;

class TaskReadBooleanString extends TaskReadStringAbstract<Boolean> {
	private final String	title;
	
	private final Boolean	defaultValue;
	
	TaskReadBooleanString(final String title, final Boolean defaultValue) {
		this.title = title;
		this.defaultValue = defaultValue;
	}
	
	@Override
	ConsoleTtyTask<?> onDoneRead(final ConsoleTty console, final String string) {
		console.setStateNormal();
		if ("yes".equalsIgnoreCase( string )) {
			this.setResult( Boolean.TRUE );
			return null;
		}
		if ("no".equalsIgnoreCase( string )) {
			this.setResult( Boolean.FALSE );
			return null;
		}
		if (string.length() == 0 && this.defaultValue != null) {
			this.setResult( this.defaultValue );
			return null;
		}
		console.setStateError();
		console.sendStatus( "invalid input!\r\n" );
		this.onTaskInit( console );
		return this;
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		console.sendStatus( this.title + (this.defaultValue == null
				? " (yes/no): "
				: "[" + (this.defaultValue.booleanValue()
						? "yes"
						: "no") + "] (yes/no): ") );
		super.onTaskInit( console );
	}
	
}
