package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;

/** @author myx */
public abstract class AbstractNonInteractiveConsole extends AbstractConsole {

	private int newLine = -1;

	/** To deal with string reader task and uncertainty with CRLF sequence.
	 *
	 *
	 * @param ch
	 *            '\r' or '\n'
	 * @return '\r' or '\n' whichever is registered for this session. */
	public int checkRegisterNewLine(final int ch) {

		assert ch == '\r' || ch == '\n' : "ch is none of the valid values: ch=" + ch;
		return this.newLine == -1
			? this.newLine = ch
			: this.newLine;
	}

	@Override
	public final boolean isInteractive() {

		return false;
	}

	@Override
	public Value<Boolean> readBoolean(final String title) {

		return "yes".equals(this.readString(title + "(yes/no)?", null))
			? BaseObject.TRUE
			: BaseObject.FALSE;
	}

	@Override
	public Value<Boolean> readBoolean(final String title, final boolean defaultValue) {

		return "yes".equals(
				this.readString(
						title + "(yes/no)?",
						defaultValue
							? "yes"
							: "no"))
								? BaseObject.TRUE
								: BaseObject.FALSE;
	}

	@Override
	public Value<String> readChoose(final String title, final Collection<Map.Entry<String, Object>> selection, final String defaultValue) {

		final Map<String, String> inputToResult = new TreeMap<>();
		{
			final StringBuilder text = new StringBuilder();
			int i = 1;
			for (final Map.Entry<String, Object> current : selection) {
				text.append(i);
				text.append(".\t");
				text.append(current.getValue());
				text.append('\n');
				inputToResult.put(String.valueOf(i), current.getKey());
				++i;
			}
			this.sendMessage(title + ": ", text.toString());
		}
		for (;;) {
			this.setStateAttention();
			final String result = this.readString("choose", defaultValue).baseValue();
			final String check = inputToResult.get(result);
			if (check != null) {
				return Base.forString(check);
			}
			this.sendError("illegal value!");
		}
	}

	@Override
	public Value<?> readContinue(final String title) {

		if (title != null) {
			this.write(title);
		}
		this.flush();
		return this.readString(" >> hit ENTER", "");
	}

	@Override
	public Value<Number> readInteger(final String title) {

		return Base.forInteger(Integer.parseInt(this.readString(title, null).baseValue()));
	}

	@Override
	public Value<Number> readInteger(final String title, final int defaultValue) {

		return Base.forInteger(Integer.parseInt(this.readString(title, String.valueOf(defaultValue)).baseValue()));
	}

	@Override
	public void sendProgress(final String string) {

		/** Do nothing. Non-interactive console is not interacting with supplier of its input stream
		 * through the output stream. */
	}

	/** @param c
	 * @return */
	protected abstract boolean write(int c);

	/** @param text
	 * @return */
	protected abstract boolean write(String text);
}
