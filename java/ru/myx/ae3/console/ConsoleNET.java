/**
 * 
 */
package ru.myx.ae3.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.common.Value;

/**
 * @author myx
 * 
 */
public class ConsoleNET extends AbstractCharacterConsole {
	private static enum AnsiState {
		/**
         * 
         */
		UNDETECTED,
		/**
         * 
         */
		ESCAPE,
		/**
         * 
         */
		DT1,
		/**
         * 
         */
		DT2,
		/**
         * 
         */
		DT3,
		/**
         * 
         */
		DETECTED
	}
	
	private final InputStream	in;
	
	private final OutputStream	out;
	
	private ConsoleState		stateCurrent	= ConsoleState.NORMAL;
	
	private final static byte[]	STATE_NORMAL	= "\033[0;37;40m".getBytes();
	
	private final static byte[]	STATE_ATTENTION	= "\033[0;1;37;40m".getBytes();
	
	private final static byte[]	STATE_ERROR		= "\07\033[0;1;33;40m".getBytes();
	
	private ConsoleState		stateRequested	= ConsoleState.NORMAL;
	
	boolean						ansi			= false;
	
	private static final String	SPEC_DV_HIDDEN	= new String();
	
	/**
	 * @param in
	 * @param out
	 */
	public ConsoleNET(final InputStream in, final OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void close() {
		try {
			this.out.flush();
		} catch (final IOException e) {
			// ignore
		}
		try {
			this.out.close();
		} catch (final IOException e) {
			// ignore
		}
		try {
			this.in.close();
		} catch (final IOException e) {
			// ignore
		}
	}
	
	@Override
	public final void flush() {
		try {
			this.out.flush();
		} catch (final IOException e) {
			// ignore
		}
	}
	
	/**
	 * @throws IOException
	 */
	private void internCheckStatus() throws IOException {
		if (this.ansi && this.stateCurrent != this.stateRequested) {
			if (this.stateRequested == ConsoleState.NORMAL) {
				this.writeIntern( ConsoleNET.STATE_NORMAL );
			}
			if (this.stateRequested == ConsoleState.ATTENTION) {
				this.writeIntern( ConsoleNET.STATE_ATTENTION );
			}
			if (this.stateRequested == ConsoleState.ERROR) {
				this.writeIntern( ConsoleNET.STATE_ERROR );
			}
			this.stateCurrent = this.stateRequested;
		}
	}
	
	/**
	 * @return is ansi
	 */
	public final boolean isAnsi() {
		return this.ansi;
	}
	
	@Override
	public final Value<?> readContinue(final String title) {
		if (title != null) {
			this.write( title + " >> hit ENTER" );
		}
		this.flush();
		return this.readString( null, ConsoleNET.SPEC_DV_HIDDEN );
	}
	
	@Override
	protected final Value<String> readPasswordImpl(final String title) {
		return this.readStringImpl( title, null );
	}
	
	@Override
	protected final Value<String> readStringImpl(final String title, final String defaultValue) {
		if (title != null) {
			this.write( title );
		}
		if (defaultValue == ConsoleNET.SPEC_DV_HIDDEN) {
			//
		} else {
			if (defaultValue != null && defaultValue.length() > 0) {
				this.write( " [" );
				this.write( defaultValue );
				this.write( "]: " );
			} else {
				this.write( ": " );
			}
			this.flush();
		}
		final StringBuilder builder = new StringBuilder();
		AnsiState ansiState = this.ansi
				? AnsiState.DETECTED
				: AnsiState.UNDETECTED;
		for (;;) {
			final int i;
			try {
				i = this.in.read();
			} catch (final IOException e) {
				if (builder.length() == 0) {
					return null;
				}
				break;
			}
			if (i == -1) {
				if (builder.length() == 0) {
					return null;
				}
				break;
			}
			if (i == 3) {
				this.write( "^C\n" );
				return BaseString.EMPTY;
			}
			if (ansiState != AnsiState.DETECTED) {
				if (ansiState == AnsiState.DT3) {
					if (i == 'n') {
						ansiState = AnsiState.DETECTED;
						this.ansi = true;
						continue;
					}
					builder.append( '\033' );
					builder.append( '[' );
					builder.append( '0' );
					ansiState = AnsiState.DETECTED;
					this.ansi = true;
				}
				if (ansiState == AnsiState.DT2) {
					if (i == '0') {
						ansiState = AnsiState.DT3;
						continue;
					}
					builder.append( '\033' );
					builder.append( '[' );
					ansiState = AnsiState.DETECTED;
					this.ansi = true;
				}
				if (ansiState == AnsiState.DT1) {
					if (i == '[') {
						ansiState = AnsiState.DT2;
						continue;
					}
					builder.append( '\033' );
					ansiState = AnsiState.UNDETECTED;
				}
				if (ansiState == AnsiState.UNDETECTED) {
					if (i == 033) {
						ansiState = AnsiState.DT1;
						continue;
					}
				}
			}
			if (i == '\r') {
				continue;
			}
			if (i == '\n') {
				break;
			}
			builder.append( (char) i );
		}
		return Base.forString( builder.toString() );
	}
	
	private final boolean sendMessageIdent(final String message, final int index) {
		boolean ident = true;
		for (final char c : message.toCharArray()) {
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				if (!this.write( '\n' )) {
					return false;
				}
				ident = true;
				continue;
			}
			if (ident) {
				ident = false;
				for (int i = index; i > 0; --i) {
					if (!this.write( '\t' )) {
						return false;
					}
				}
			}
			if (!this.write( Character.toString( c ) )) {
				return false;
			}
		}
		return this.write( '\n' );
	}
	
	@Override
	protected final boolean sendMessageImpl(final String message) {
		return this.write( message ) && this.write( '\n' );
	}
	
	@Override
	protected final boolean sendMessageImpl(final String title, final String body) {
		return this.sendMessageImpl( title ) && this.sendMessageIdent( body, 1 );
	}
	
	@Override
	public void setState(final ConsoleState state) {
		this.stateRequested = state;
	}
	
	@Override
	protected final boolean write(final int i) {
		try {
			this.internCheckStatus();
			this.writeIntern( i );
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
	
	@Override
	protected final boolean write(final String text) {
		try {
			this.internCheckStatus();
			this.writeIntern( text.getBytes() );
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
	
	private final void writeIntern(final byte[] bytes) throws IOException {
		this.out.write( bytes );
	}
	
	private final void writeIntern(final int i) throws IOException {
		this.out.write( i );
	}
	
}
