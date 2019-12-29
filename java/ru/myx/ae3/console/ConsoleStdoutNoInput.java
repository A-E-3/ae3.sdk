package ru.myx.ae3.console;

import java.io.PrintStream;

import ru.myx.ae3.report.Report;

/** @author myx */
public class ConsoleStdoutNoInput extends AbstractNoInputConsole {

	/**
	 *
	 */
	public static final ConsoleStdoutNoInput INSTANCE = new ConsoleStdoutNoInput();

	private final static boolean buildMessageIdent(final StringBuilder target, final String message, final int index) {

		boolean ident = true;
		for (final char c : message.toCharArray()) {
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				target.append('\n');
				ident = true;
				continue;
			}
			if (ident) {
				ident = false;
				for (int i = index; i > 0; --i) {
					target.append('\t');
				}
			}
			target.append(Character.toString(c));
		}
		target.append('\n');
		return true;
	}

	// private final PrintWriter out;

	private ConsoleState state = ConsoleState.NORMAL;

	private final PrintStream out;

	/** @param output
	 * @param logger */
	private ConsoleStdoutNoInput() {
		
		this.out = System.out;
		// this.out = new PrintWriter( new FileOutputStream( FileDescriptor.out
		// ), true );
	}

	@Override
	public void checkUpdateClient() {

		// nothing
	}

	@Override
	public void close() {

		// ignore
	}

	@Override
	public void debug(final String message) {

		final String x = "' " + message;
		this.out.println(x);
	}

	@Override
	public void error(final String message) {

		final String x = "! " + message;
		this.out.println(x);
	}

	@Override
	public void flush() {

		this.out.flush();
	}

	@Override
	public void info(final String message) {

		if (!Report.MODE_ASSERT && !Report.MODE_DEBUG) {
			return;
		}
		final String x = "- " + message;
		this.out.println(x);
	}

	@Override
	public final boolean isReadable() {

		return false;
	}

	@Override
	public void log(final String message) {

		final String x = "# " + message;
		this.out.println(x);
	}

	private final String prefixForState() {

		if (this.state == null) {
			return "  ";
		}
		switch (this.state) {
			case ATTENTION :
				return "* ";
			case ERROR :
				return "! ";
			case NORMAL :
				return "= ";
			default :
				return "? ";
		}
	}

	@Override
	public final void sendMessage(final String message) {

		final String pfs = this.prefixForState();
		final String x = pfs + (message == null
			? "null"
			: message);
		this.out.println(x);
		this.flush();
	}

	@Override
	public final void sendMessage(final String title, final String text) {

		final String pfs = this.prefixForState();
		if (text == null) {
			final String x = pfs + (title == null
				? "null"
				: title);
			this.out.println(x);
		} else {
			final StringBuilder builder = new StringBuilder( //
					2 + title.length() + (int) (text.length() * 1.3) //
			);
			builder.append(pfs);
			builder.append(title);
			builder.append("\r\n");
			ConsoleStdoutNoInput.buildMessageIdent(builder, text, 1);
			this.out.println(builder);
		}
		this.flush();
	}

	@Override
	public void setState(final ConsoleState state) {

		this.state = state;
	}

	@Override
	public void setStateAttention() {

		this.state = ConsoleState.ATTENTION;
	}

	@Override
	public void setStateError() {

		this.state = ConsoleState.ERROR;
	}

	@Override
	public void setStateNormal() {

		this.state = ConsoleState.NORMAL;
	}

	@Override
	public void warn(final String message) {

		final String x = "* " + message;
		this.out.println(x);
	}
}
