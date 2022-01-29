package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Create;

/** @author myx */
public abstract class AbstractCharacterConsole extends AbstractConsole {

	private static final Map<String, String> PROGRESS_UNDEFINED_STATES;

	static {
		PROGRESS_UNDEFINED_STATES = Create.privateMap(16);
		AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.put("", "|");
		AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.put("\\", "|");
		AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.put("|", "/");
		AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.put("/", "-");
		AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.put("-", "\\");
	}

	private int newLine = -1;

	private String progress = null;

	private long lastProgress = -1L;

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

	private final String clearProgress() {

		this.lastProgress = Engine.fastTime();
		final String oldProgress = this.progress;
		if (oldProgress != null) {
			for (int i = oldProgress.length(); i > 0; --i) {
				this.write('\b');
			}
			this.progress = null;
		}
		return oldProgress;
	}

	@Override
	public boolean isInteractive() {

		return true;
	}

	@Override
	public boolean isReadable() {

		return true;
	}

	@Override
	public Value<Boolean> readBoolean(final String title) {

		return "yes".equals(this.readString(title + "(yes/no)?", null).baseValue())
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
							: "no")
						.baseValue())
							? BaseObject.TRUE
							: BaseObject.FALSE;
	}

	@Override
	public Value<String> readChoose(final String title, final Collection<Map.Entry<String, Object>> selection, final String defaultValue) {

		this.clearProgress();
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

		this.clearProgress();
		if (title != null) {
			this.write(title);
		}
		return this.readString(" >> hit ENTER", "");
	}

	@Override
	public Value<Number> readInteger(final String title) {

		return Base.forInteger(Integer.parseInt(this.readString(title, null).baseValue()));
	}

	@Override
	public Value<Number> readInteger(final String title, final int defaultValue) {

		return Base.forInteger(
				Integer.parseInt(
						this.readString(
								title, //
								String.valueOf(defaultValue)).baseValue()));
	}

	@Override
	public final Value<String> readPassword(final String title) {

		this.clearProgress();
		return this.readPasswordImpl(title);
	}

	/** @param title
	 * @return */
	protected abstract Value<String> readPasswordImpl(String title);

	@Override
	public final Value<String> readString(final String title, final String defaultValue) {

		this.clearProgress();
		return this.readStringImpl(title, defaultValue);
	}

	/** @param title
	 * @param defaultValue
	 * @return */
	protected abstract Value<String> readStringImpl(String title, final String defaultValue);

	@Override
	public final void sendMessage(final String message) {

		synchronized (this) {
			this.clearProgress();
			this.sendMessageImpl(
					message == null
						? "null"
						: message);
		}
		this.flush();
	}

	@Override
	public final void sendMessage(final String title, final String text) {

		synchronized (this) {
			this.clearProgress();
			if (text == null) {
				this.sendMessageImpl(
						title == null
							? "null"
							: title);
			} else {
				this.sendMessageImpl(
						title == null
							? "null"
							: title,
						text);
			}
		}
		this.flush();
	}

	/** No need for flush. Called from within synchronized(this)
	 *
	 * @param title
	 *            never NULL
	 * @return */
	protected abstract boolean sendMessageImpl(String title);

	/** No need for flush. Called from within synchronized(this)
	 *
	 * @param title
	 * @param text
	 * @return */
	protected abstract boolean sendMessageImpl(final String title, final String text);

	@Override
	public final void sendProgress(final String string) {

		{
			final long currentTime = Engine.fastTime();
			if (this.lastProgress + 250L >= currentTime) {
				return;
			}
			this.lastProgress = currentTime;
		}
		final String oldProgress = this.clearProgress();
		if (string != null && string.length() > 0) {
			this.write(this.progress = string);
		} else {
			final String checkProgress = AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.get(
					oldProgress == null
						? ""
						: oldProgress);
			final String newProgress = checkProgress == null
				? AbstractCharacterConsole.PROGRESS_UNDEFINED_STATES.get("")
				: checkProgress;
			this.write(this.progress = newProgress);
		}
		this.flush();
	}

	/** @param c
	 * @return */
	protected abstract boolean write(int c);

	/** @param text
	 * @return */
	protected abstract boolean write(String text);
}
