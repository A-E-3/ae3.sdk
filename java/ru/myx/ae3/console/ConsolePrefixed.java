package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map.Entry;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.report.Report;

/**
 * 
 * @author myx
 * 		
 */
public final class ConsolePrefixed implements Console/* , ConsoleOutput */ {
	
	private final String prefix;
	
	private final Console console;
	
	/**
	 * 
	 * @param prefix
	 * @param console
	 */
	public ConsolePrefixed(final String prefix, final Console console) {
		assert prefix != null;
		assert console.isInteractive();
		this.prefix = prefix;
		this.console = console;
	}
	
	@Override
	public void checkUpdateClient() {
		
		this.console.checkUpdateClient();
	}
	
	@Override
	public void close() {
		
		//
	}
	
	@Override
	public void debug(final String message) {
		
		if (Report.MODE_DEBUG) {
			this.console.debug(this.prefix + message);
		}
	}
	
	@Override
	public void error(final String message) {
		
		this.console.error(this.prefix + message);
	}
	
	@Override
	public void flush() {
		
		this.console.flush();
	}
	
	@Override
	public void info(final String message) {
		
		this.console.info(this.prefix + message);
	}
	
	@Override
	public boolean isInteractive() {
		
		return true;
	}
	
	@Override
	public boolean isReadable() {
		
		return this.console.isReadable();
	}
	
	@Override
	public void log(final String message) {
		
		this.console.log(this.prefix + message);
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title) {
		
		return this.console.readBoolean(title);
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title, final boolean defaultValue) {
		
		return this.console.readBoolean(title, defaultValue);
	}
	
	@Override
	public Value<String> readChoose(final String title, final Collection<Entry<String, Object>> selection, final String defaultValue) {
		
		return this.console.readChoose(title, selection, defaultValue);
	}
	
	@Override
	public Value<?> readContinue(final String title) {
		
		return this.console.readContinue(title);
	}
	
	@Override
	public Value<Number> readInteger(final String title) {
		
		return this.console.readInteger(title);
	}
	
	@Override
	public Value<Number> readInteger(final String title, final int defaultValue) {
		
		return this.console.readInteger(title, defaultValue);
	}
	
	@Override
	public Value<String> readPassword(final String title) {
		
		return this.console.readPassword(title);
	}
	
	@Override
	public Value<String> readString(final String title, final String defaultValue) {
		
		return this.console.readString(title, defaultValue);
	}
	
	@Override
	public void sendMessage(final String message) {
		
		this.console.sendMessage(message);
	}
	
	@Override
	public void sendMessage(final String title, final String text) {
		
		this.console.sendMessage(title, text);
	}
	
	@Override
	public void sendProgress(final String string) {
		
		this.console.sendProgress(string);
	}
	
	@Override
	public void setStateAttention() {
		
		this.console.setStateAttention();
	}
	
	@Override
	public void setStateError() {
		
		this.console.setStateError();
	}
	
	@Override
	public void setStateNormal() {
		
		this.console.setStateNormal();
	}
	
	@Override
	public void warn(final String message) {
		
		this.console.warn(this.prefix + message);
	}
	
}
