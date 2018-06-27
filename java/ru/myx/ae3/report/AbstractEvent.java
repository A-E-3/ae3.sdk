package ru.myx.ae3.report;

import ru.myx.ae3.base.BaseHost;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.ReflectionIgnore;

/** @author myx */
@ReflectionIgnore
public abstract class AbstractEvent implements BaseHost, Event, BaseObjectNoOwnProperties {

	/**
	 *
	 */
	// protected long date = Engine.fastTime();
	protected long date = System.currentTimeMillis();

	/**
	 *
	 */
	protected long process = Thread.currentThread().getId();

	@Override
	public long getDate() {

		return this.date;
	}

	@Override
	public abstract String getEventTypeId();

	@Override
	public final long getProcess() {

		return this.process;
	}

	/** returns null. */
	@Override
	public String getSubject() {

		return null;
	}

	@Override
	public String toString() {

		return "EVENT{date=" + Format.Ecma.date(this.getDate()) + ", owner=" + this.getEventTypeId() + ", title=" + this.getTitle() + ", subject=" + this.getSubject() + "}";
	}
}
