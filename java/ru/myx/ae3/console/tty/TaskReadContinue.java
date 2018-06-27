package ru.myx.ae3.console.tty;

class TaskReadContinue extends ConsoleTtyTask<Boolean> {
	private final String	title;
	
	TaskReadContinue(final String title) {
		this.title = title;
	}
	
	@Override
	ConsoleTtyTask<?> consumeNext(final ConsoleTty console, final int b) {
		console.setStateNormal();
		this.setResult( Boolean.TRUE );
		return null;
	}
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.setStateAttention();
		if (this.title != null) {
			console.sendStatus( this.title + " >> hit ENTER" );
		}
	}
	
}
