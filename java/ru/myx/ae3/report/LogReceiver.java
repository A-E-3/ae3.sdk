/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.report;

import ru.myx.ae3.status.StatusFiller;
import ru.myx.ae3.status.StatusInfo;

/** @author myx */
public abstract class LogReceiver implements ReportReceiver, StatusFiller {
	
	/**
	 *
	 */
	public static final LogReceiver DUMMY = new NulReciever();

	private boolean allEventClasses = false;

	private boolean allEventTypes = false;

	private String[] eventClasses;

	private String[] eventTypes;

	private volatile int stAttempts = 0;

	private volatile int stAcknowleged = 0;

	/** @return child recievers */
	@SuppressWarnings("static-method")
	public LogReceiver[] childRecievers() {
		
		return null;
	}

	@Override
	public final boolean event(final Event event) {
		
		this.stAttempts++;
		final String eventTypeId = event.getEventTypeId();
		if (this.isEventClass(eventTypeId) && this.isEventType(eventTypeId)) {
			this.stAcknowleged++;
			this.onEvent(event);
		}
		return true;
	}

	/** Owner filter or null if any
	 *
	 * @return */
	protected abstract String[] eventClasses();

	/** Subject filter or null if any
	 *
	 * @return */
	protected abstract String[] eventTypes();

	private boolean isEventClass(final String eventTypeId) {
		
		if (this.eventClasses == null) {
			this.eventClasses = this.eventClasses();
			this.allEventClasses = this.eventClasses == null;
		}
		if (this.allEventClasses) {
			return true;
		}
		final int pos = eventTypeId.indexOf(':');
		final String eventClass = pos == -1
			? eventTypeId
			: eventTypeId.substring(0, pos);
		final int length = eventClass.length();
		for (final String element : this.eventClasses) {
			final int elementLength = element.length();
			if (length < elementLength) {
				continue;
			}
			if (!eventClass.regionMatches(0, element, 0, elementLength)) {
				continue;
			}
			if (length == elementLength) {
				return true;
			}
			if (eventClass.charAt(elementLength) == '/') {
				return true;
			}
		}
		return false;
	}

	private boolean isEventType(final String eventTypeId) {
		
		if (this.eventTypes == null) {
			this.eventTypes = this.eventTypes();
			this.allEventTypes = this.eventTypes == null;
		}
		if (this.allEventTypes) {
			return true;
		}
		final int pos = eventTypeId.lastIndexOf(':');
		final String eventType = pos == -1
			? eventTypeId
			: eventTypeId.substring(pos + 1);
		final int length = eventType.length();
		for (final String element : this.eventTypes) {
			final int elementLength = element.length();
			if (length < elementLength) {
				continue;
			}
			if (!eventType.regionMatches(0, element, 0, elementLength)) {
				continue;
			}
			if (length == elementLength) {
				return true;
			}
			if (eventType.charAt(elementLength) == '-') {
				return true;
			}
		}
		return false;
	}

	/** @param event
	 */
	protected abstract void onEvent(final Event event);

	// //////////////////////////////////////////////////////////////////////////
	// ////////
	// //////////////////////////////////////////////////////////////////////////
	// ////////
	// //////////////////////////////////////////////////////////////////////////
	// ////////
	@Override
	public void statusFill(final StatusInfo data) {
		
		data.put("Events attempted", this.stAttempts);
		data.put("Events acknowleged", this.stAcknowleged);
	}
}
