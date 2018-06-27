/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.report;

class Eventer implements Runnable {
	final Event			event;
	
	final ReportReceiver[]	targets;
	
	final ReportReceiver	parent;
	
	Eventer(final Event event, final ReportReceiver[] targets, final ReportReceiver parent) {
		this.event = event;
		this.targets = targets;
		this.parent = parent;
	}
	
	@Override
	public void run() {
		if (this.parent != null) {
			this.parent.event( this.event );
		}
		{
			final ReportReceiver reciever = ReceiverMultiple.SPY;
			if (reciever != null) {
				reciever.event( this.event );
			}
		}
		for (final ReportReceiver element : this.targets) {
			element.event( this.event );
		}
	}
}
