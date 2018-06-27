package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map.Entry;

import ru.myx.ae3.common.Value;

/**
 * @author myx
 * 		
 */
public class ConsoleSimple implements Console {
	
	private final ConsoleInput input;
	
	private final ConsoleOutput output;
	
	private final ConsoleLogger logger;
	
	/**
	 * @param input
	 * @param output
	 * @param logger
	 */
	public ConsoleSimple(final ConsoleInput input, final ConsoleOutput output, final ConsoleLogger logger) {
		this.input = input;
		this.output = output;
		this.logger = logger;
	}
	
	@Override
	public void checkUpdateClient() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void debug(final String message) {
		
		this.logger.debug(message);
	}
	
	@Override
	public void error(final String message) {
		
		this.logger.error(message);
	}
	
	@Override
	public void flush() {
		
		this.output.flush();
	}
	
	@Override
	public void info(final String message) {
		
		this.logger.info(message);
	}
	
	@Override
	public boolean isInteractive() {
		
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isReadable() {
		
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void log(final String message) {
		
		this.logger.log(message);
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title) {
		
		return this.input.readBoolean(title);
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title, final boolean defaultValue) {
		
		return this.input.readBoolean(title, defaultValue);
	}
	
	@Override
	public Value<String> readChoose(final String title, final Collection<Entry<String, Object>> selection, final String defaultValue) {
		
		return this.input.readChoose(title, selection, defaultValue);
	}
	
	@Override
	public Value<?> readContinue(final String title) {
		
		return this.input.readContinue(title);
	}
	
	@Override
	public Value<Number> readInteger(final String title) {
		
		return this.input.readInteger(title);
	}
	
	@Override
	public Value<Number> readInteger(final String title, final int defaultValue) {
		
		return this.input.readInteger(title, defaultValue);
	}
	
	@Override
	public Value<String> readPassword(final String title) {
		
		return this.input.readPassword(title);
	}
	
	@Override
	public Value<String> readString(final String title, final String defaultValue) {
		
		return this.input.readString(title, defaultValue);
	}
	
	@Override
	public void sendMessage(final String message) {
		
		this.output.sendMessage(message);
	}
	
	@Override
	public void sendMessage(final String title, final String text) {
		
		this.output.sendMessage(title, text);
	}
	
	@Override
	public void sendProgress(final String string) {
		
		if (this.input instanceof ConsoleInteractive) {
			((ConsoleInteractive) this.output).sendProgress(string);
		}
	}
	
	@Override
	public void setStateAttention() {
		
		this.output.setStateAttention();
	}
	
	@Override
	public void setStateError() {
		
		this.output.setStateError();
	}
	
	@Override
	public void setStateNormal() {
		
		this.output.setStateNormal();
	}
	
	@Override
	public void warn(final String message) {
		
		this.logger.warn(message);
	}
	
}
