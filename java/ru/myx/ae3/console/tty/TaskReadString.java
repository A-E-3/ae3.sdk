package ru.myx.ae3.console.tty;

class TaskReadString extends TaskReadStringAbstract<String> {
	private final String	title;
	
	private final String	defaultValue;
	
	TaskReadString(final String title, final String defaultValue) {
		this.title = title;
		this.defaultValue = defaultValue;
	}
	
	@Override
	ConsoleTtyTask<?> onDoneRead(final ConsoleTty console, final String string) {
		console.setStateNormal();
		this.setResult( this.defaultValue != null && string != null && string.length() == 0
				? this.defaultValue
				: string );
		return null;
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		console.sendStatus( this.title + (this.defaultValue == null || this.defaultValue.length() == 0
				? ": "
				: "[" + this.defaultValue + "]: ") );
		super.onTaskInit( console );
	}
	
}
