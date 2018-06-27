package ru.myx.ae3.console.tty;

abstract class TaskReadStringAbstract<T> extends ConsoleTtyTask<T> {
	
	private final StringBuilder	builder		= new StringBuilder();
	
	private int					position	= 0;
	
	@Override
	ConsoleTtyTask<?> consumeNext(final ConsoleTty console, final int b) {
		switch (b) {
		case 0:
			return this;
		case '\r':
		case '\n':
			if (console.checkRegisterNewLine( b ) != b) {
				return this;
			}
			//$FALL-THROUGH$
			console.internEcho( '\r' );
			console.internEcho( '\n' );
			return this.onDoneRead( console, this.builder.toString() );
		case 127: // backspace somehow %)
			if (this.position > 0) {
				this.builder.deleteCharAt( --this.position );
				if (this.doEcho()) {
					/**
					 * Normal backspace
					 */
					console.internEcho( 8 );
					/**
					 * That's what we get from client
					 */
					// console.echo( 127 );
					/**
					 * Cancel character<br>
					 * Destructive backspace, intended to eliminate ambiguity
					 * about meaning of BS.
					 */
					// console.echo( 148 );
				}
			} else {
				/**
				 * beep
				 */
				console.internEcho( 7 );
			}
			return this;
		default:
			if (b >= 32 && b <= 126) {
				if (this.doEcho()) {
					console.internEcho( b );
				}
				this.builder.insert( this.position++, (char) b );
				return this;
			}
			console.internEcho( 7 );
			System.out.println( ">>>>> readString: next=" + b );
			return this;
		}
	}
	
	@SuppressWarnings("static-method")
	boolean doEcho() {
		return true;
	}
	
	abstract ConsoleTtyTask<?> onDoneRead(final ConsoleTty console, final String string);
	
	@Override
	void onTaskInit(final ConsoleTty console) {
		console.sendStatus( this.builder.toString() );
	}
	
}
