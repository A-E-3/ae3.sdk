package ru.myx.ae3.console;

import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;

/** @author myx */
public class ConsoleCollector extends AbstractNoInputConsole {

	private BaseList<Object> events = null;

	private ConsoleState state = ConsoleState.NORMAL;

	private boolean hasNonDefaultOutput = false;

	@Override
	public void close() {
		
		// ignore
	}

	@Override
	public boolean fail(final String message) {
		
		final ConsoleState state = this.state;
		try {
			this.state = ConsoleState.ERROR;
			this.sendMessage(message);
			return false;
		} finally {
			this.state = state;
		}
	}

	@Override
	public void flush() {
		
		// ignore
	}

	/** @return */
	public BaseList<?> getCollected() {
		
		return this.events;
	}

	/** @return */
	public boolean getHasNonDefaultOutput() {
		
		return this.hasNonDefaultOutput;
	}

	@Override
	public void log(final String message) {
		
		final ConsoleState state = this.state;
		try {
			this.state = ConsoleState.NORMAL;
			this.sendMessage(message);
		} finally {
			this.state = state;
		}
	}

	@Override
	public void sendMessage(final String message) {
		
		if (this.events == null) {
			this.events = BaseObject.createArray();
		}
		if (this.state != ConsoleState.NORMAL) {
			this.hasNonDefaultOutput = true;
		}
		this.events.baseDefaultPush(
				BaseObject.createObject()//
						.putAppend("state", this.state.name())//
						.putAppend("text", message)//
		);
	}

	@Override
	public void sendMessage(final String title, final String text) {
		
		this.sendMessage(title + ": " + text);
	}

	@Override
	public void setState(final ConsoleState state) {
		
		this.state = state;
	}

	@Override
	public void warn(final String message) {
		
		final ConsoleState state = this.state;
		try {
			this.state = ConsoleState.ATTENTION;
			this.sendMessage(message);
		} finally {
			this.state = state;
		}
	}

}
