/**
 * 
 */
package ru.myx.ae3.console;

import ru.myx.ae3.report.Report;
import ru.myx.ae3.report.ReportReceiver;

/**
 * @author myx
 * 		
 */
public class ConsoleLoggerReport implements ConsoleLogger {
	
	private final String grp;
	
	private final ReportReceiver out;
	
	boolean ansi = false;
	
	/**
	 * @param group
	 *            - group is a message 'owner' fields (first column in logs).
	 *            supposed to identify a subsystem or task type to which event
	 *            belongs.
	 * @param bus
	 *            - log instance, see @ru.myx.ae3.report.Report
	 */
	public ConsoleLoggerReport(final String group, final ReportReceiver bus) {
		this.grp = group;
		this.out = bus;
	}
	
	/**
	 * @param group
	 *            - group is a message 'owner' fields (first column in logs).
	 *            supposed to identify a subsystem or task type to which event
	 *            belongs.
	 * @param bus
	 *            - log name, see @ru.myx.ae3.report.Report
	 */
	public ConsoleLoggerReport(final String group, final String bus) {
		this.grp = group;
		this.out = Report.createReceiver(bus);
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
	public void info(final String message) {
		
		this.out.event(this.grp, "info", message);
	}
	
	@Override
	public void log(final String message) {
		
		this.out.event(this.grp, "log", message);
	}
	
	@Override
	public void warn(final String message) {
		
		this.out.event(this.grp, "warning", message);
	}
	
}
