/**
 *
 */
package ru.myx.ae3.console;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;

/** @author myx */
public class ConsoleStdio extends AbstractCharacterConsole {
	
	private final static byte[] STATE_ATTENTION = "\033[0;1;37;40m".getBytes();

	private final static byte[] STATE_ERROR = "\07\033[0;1;33;40m".getBytes();

	private final static byte[] STATE_NORMAL = "\033[0;37;40m".getBytes();

	/**
	 *
	 */
	public static final Console INSTANCE;

	static {
		if (System.console() == null) {
			INSTANCE = new ConsoleNET(new FileInputStream(FileDescriptor.in), new FileOutputStream(FileDescriptor.err));
		} else {
			INSTANCE = new ConsoleStdio();
		}
	}

	private final java.io.Console console;

	private ConsoleState stateCurrent = null;

	private ConsoleState stateRequested = ConsoleState.NORMAL;

	boolean ansi = false;

	private ConsoleStdio() {
		
		this.console = System.console();
		this.ansi = true;
	}

	private final boolean sendMessageIdent(final String message, final int index) {
		
		boolean ident = true;
		for (final char c : message.toCharArray()) {
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				if (!this.write('\n')) {
					return false;
				}
				ident = true;
				continue;
			}
			if (ident) {
				ident = false;
				for (int i = index; i > 0; --i) {
					if (!this.write('\t')) {
						return false;
					}
				}
			}
			if (!this.write(Character.toString(c))) {
				return false;
			}
		}
		return this.write('\n');
	}

	private final void writeIntern(final byte[] bytes) {
		
		for (final byte b : bytes) {
			this.writeIntern(b & 0xFF);
		}
	}

	private final void writeIntern(final int i) {
		
		this.console.writer().write(i);
	}

	@Override
	public void close() {
		
		this.console.flush();
	}

	@Override
	public final void flush() {
		
		this.console.flush();
	}

	/** @return is ansi */
	public final boolean isAnsi() {
		
		return this.ansi;
	}

	@Override
	public final Value<?> readContinue(final String title) {
		
		if (title != null) {
			this.write(title + " >> hit ENTER");
		}
		this.flush();
		this.console.readLine();
		return BaseObject.UNDEFINED;
	}

	@Override
	public void setState(final ConsoleState state) {
		
		this.stateRequested = state;
	}

	@Override
	protected final Value<String> readPasswordImpl(final String title) {
		
		this.write(title);
		this.write(": ");
		this.flush();
		final char[] password = this.console.readPassword();
		return Base.forString(new String(password));
	}

	@Override
	protected final Value<String> readStringImpl(final String title, final String defaultValue) {
		
		this.write(title);
		if (defaultValue != null && defaultValue.length() > 0) {
			this.write(" [");
			this.write(defaultValue);
			this.write("]: ");
		} else {
			this.write(": ");
		}
		this.flush();
		return Base.forString(this.console.readLine());
	}

	@Override
	protected final boolean sendMessageImpl(final String message) {
		
		return this.write(message) && this.write('\n');
	}

	@Override
	protected final boolean sendMessageImpl(final String title, final String body) {
		
		return this.sendMessageImpl(title) && this.sendMessageIdent(body, 1);
	}

	@Override
	protected final boolean write(final int i) {
		
		if (this.ansi && this.stateCurrent != this.stateRequested) {
			if (this.stateRequested == ConsoleState.NORMAL) {
				this.writeIntern(ConsoleStdio.STATE_NORMAL);
			}
			if (this.stateRequested == ConsoleState.ATTENTION) {
				this.writeIntern(ConsoleStdio.STATE_ATTENTION);
			}
			if (this.stateRequested == ConsoleState.ERROR) {
				this.writeIntern(ConsoleStdio.STATE_ERROR);
			}
			this.stateCurrent = this.stateRequested;
		}
		this.writeIntern(i);
		return true;
	}

	@Override
	protected final boolean write(final String text) {
		
		if (this.ansi && this.stateCurrent != this.stateRequested) {
			if (this.stateRequested == ConsoleState.NORMAL) {
				this.writeIntern(ConsoleStdio.STATE_NORMAL);
			}
			if (this.stateRequested == ConsoleState.ATTENTION) {
				this.writeIntern(ConsoleStdio.STATE_ATTENTION);
			}
			if (this.stateRequested == ConsoleState.ERROR) {
				this.writeIntern(ConsoleStdio.STATE_ERROR);
			}
			this.stateCurrent = this.stateRequested;
		}
		this.console.format("%s", text);
		return true;
	}

}
