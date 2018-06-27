/*
 * Created on 28.03.2006
 */
package ru.myx.ae3.report;

import ru.myx.ae3.reflect.ReflectionIgnore;

/** @author myx */
@ReflectionIgnore
final class EventDefault extends AbstractEvent {
	
	final String eventTypeId;
	
	final String title;
	
	final String subject;
	
	EventDefault(final String eventTypeId, final String title, final String subject) {

		this.eventTypeId = eventTypeId;
		this.title = title;
		this.subject = subject;
	}
	
	@Override
	public String getEventTypeId() {
		
		return this.eventTypeId;
	}
	
	@Override
	public String getSubject() {
		
		return this.subject;
	}
	
	@Override
	public String getTitle() {
		
		return this.title;
	}
	
}
