/**
 * 
 */
package ru.myx.ae3.report;

/**
 * @author myx
 * 
 */
public abstract class AbstractReciever implements ReportReceiver {
	@Override
	public abstract boolean event(final Event event);
	
	@Override
	public boolean event(final String owner, final String title, final String subject) {
		return this.event( subject == null || subject.length() == 0
				? (Event) new EventNoSubject( owner, title )
				: (Event) new EventDefault( owner, title, subject ) );
	}
}
