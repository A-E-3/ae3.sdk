package ru.myx.ae3.console.tty;

class TaskReadNumber extends TaskReadStringAbstract<Number> {
	private final String	title;
	
	private final Number	defaultValue;
	
	TaskReadNumber(final String title, final Number defaultValue) {
		this.title = title;
		this.defaultValue = defaultValue;
	}
	
	@Override
	ConsoleTtyTask<?> onDoneRead(final ConsoleTty console, final String string) {
		console.setStateNormal();
		this.setResult( Double.valueOf( Double.parseDouble( string ) ) );
		return null;
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		console.sendStatus( this.title + (this.defaultValue == null
				? ": "
				: "[" + this.defaultValue + "]: ") );
		super.onTaskInit( console );
	}
	
}
