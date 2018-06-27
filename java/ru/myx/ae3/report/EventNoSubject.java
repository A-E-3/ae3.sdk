/*
 * Created on 28.03.2006
 */
package ru.myx.ae3.report;

/**
 * @author myx
 * 		
 */
final class EventNoSubject extends AbstractEvent {
	
	final String eventTypeId;
	
	final String title;
	
	EventNoSubject(final String eventTypeId, final String title) {
		this.eventTypeId = eventTypeId;
		this.title = title;
	}
	
	@Override
	public String getEventTypeId() {
		
		return this.eventTypeId;
	}
	
	@Override
	public String getTitle() {
		
		return this.title;
	}
}
