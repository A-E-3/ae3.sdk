/**
 * 
 */
package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map.Entry;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.report.ReportReceiver;

/**
 * @author myx
 * 		
 */
public class ConsoleLoggingWrapper implements Console {
	
	private final String grp;
	
	private final ReportReceiver out;
	
	private final Console console;
	
	boolean ansi = false;
	
	/**
	 * @param group
	 *            - group is a message 'owner' fields (first column in logs).
	 *            supposed to identify a subsystem or task type to which event
	 *            belongs.
	 * @param bus
	 *            - log instance, see @ru.myx.ae3.report.Report
	 * @param console
	 *            - an instance of console that should be wrapped (replacing
	 *            'log', 'info', 'warn' and 'error' methods.
	 */
	public ConsoleLoggingWrapper(final String group, final ReportReceiver bus, final Console console) {
		this.grp = group;
		this.out = bus;
		if (console == null) {
			throw new NullPointerException("Interactive parent must not be NULL!");
		}
		this.console = console;
	}
	
	/**
	 * @param group
	 *            - group is a message 'owner' fields (first column in logs).
	 *            supposed to identify a subsystem or task type to which event
	 *            belongs.
	 * @param bus
	 *            - log name, see @ru.myx.ae3.report.Report
	 * @param console
	 *            - an instance of console that should be wrapped (replacing
	 *            'log', 'info', 'warn' and 'error' methods.
	 */
	public ConsoleLoggingWrapper(final String group, final String bus, final Console console) {
		this.grp = group;
		this.out = Report.createReceiver(bus);
		if (console == null) {
			throw new NullPointerException("Interactive parent must not be NULL!");
		}
		this.console = console;
	}
	
	@Override
	public void checkUpdateClient() {
		
		// TODO Auto-generated method stub
	}
	
	@Override
	public void close() {
		
		this.console.close();
		this.out.event(this.grp, "CLOSE", "console closed.");
	}
	
	@Override
	public void debug(final String message) {
		
		if (Report.MODE_DEBUG) {
			this.out.event(this.grp, "debug", message);
		}
	}
	
	@Override
	public void error(final String message) {
		
		this.out.event(this.grp, "error", message);
	}
	
	@Override
	public final void flush() {
		
		this.console.flush();
	}
	
	@Override
	public void info(final String message) {
		
		this.out.event(this.grp, "info", message);
	}
	
	@Override
	public boolean isInteractive() {
		
		return this.console.isInteractive();
	}
	
	@Override
	public boolean isReadable() {
		
		return this.console.isReadable();
	}
	
	@Override
	public void log(final String message) {
		
		this.out.event(this.grp, "log", message);
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
	public final Value<?> readContinue(final String title) {
		
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
	public final Value<String> readPassword(final String title) {
		
		return this.console.readPassword(title);
	}
	
	@Override
	public final Value<String> readString(final String title, final String defaultValue) {
		
		return this.console.readString(title, defaultValue);
	}
	
	@Override
	public final void sendMessage(final String message) {
		
		this.console.sendMessage(message);
	}
	
	@Override
	public final void sendMessage(final String title, final String body) {
		
		this.console.sendMessage(title, body);
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
		
		this.out.event(this.grp, "warning", message);
	}
	
}
