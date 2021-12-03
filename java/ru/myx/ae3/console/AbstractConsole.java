package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.common.Value;

/** @author myx */
public abstract class AbstractConsole implements Console {

	/** does nothing in its abstract implementation */
	@Override
	public void checkUpdateClient() {

		// ignore
	}

	@Override
	public void debug(final String message) {

		this.sendMessage(message);
	}

	@Override
	public void error(final String message) {

		this.setState(ConsoleState.ERROR);
		this.sendMessage(message);
		this.setState(ConsoleState.NORMAL);
	}

	/** ?
	 *
	 * @param message
	 * @return */
	public boolean fail(final String message) {

		this.sendMessage(message);
		return false;
	}

	@Override
	public void info(final String message) {

		this.sendMessage(message);
	}

	@Override
	public void log(final String message) {

		this.sendMessage(message);
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

	/** @param state */
	public abstract void setState(final ConsoleState state);

	@Override
	public void setStateAttention() {

		this.setState(ConsoleState.ATTENTION);
	}

	@Override
	public void setStateError() {

		this.setState(ConsoleState.ERROR);
	}

	@Override
	public void setStateNormal() {

		this.setState(ConsoleState.NORMAL);
	}

	@Override
	public void warn(final String message) {

		this.setState(ConsoleState.ATTENTION);
		this.sendMessage(message);
		this.setState(ConsoleState.NORMAL);
	}
}
