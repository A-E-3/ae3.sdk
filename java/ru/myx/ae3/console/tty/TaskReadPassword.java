package ru.myx.ae3.console.tty;

class TaskReadPassword extends TaskReadStringAbstract<String> {
	private final String	title;
	
	TaskReadPassword(final String title) {
		this.title = title;
	}
	
	@Override
	boolean doEcho() {
		return false;
	}
	
	@Override
	ConsoleTtyTask<?> onDoneRead(final ConsoleTty console, final String string) {
		console.setStateNormal();
		this.setResult( string );
		return null;
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		console.sendStatus( this.title + ": " );
		super.onTaskInit( console );
	}
}
