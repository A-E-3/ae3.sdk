package ru.myx.ae3.flow;

import ru.myx.ae3.base.BaseMessage;
import ru.myx.util.QueueStackRecord;

/*
 * Created on 02.06.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class SourceBuffered implements ObjectSourceAsync<BaseMessage> {
	
	boolean closed = false;

	private final class FrontDoorTarget implements ObjectTarget<BaseMessage> {
		
		FrontDoorTarget() {
			
			// ignore
		}
		
		@Override
		public final boolean absorb(final BaseMessage object) {
			
			assert object != null : "Message is NULL";
			if (SourceBuffered.this.target != null) {
				return SourceBuffered.this.many && SourceBuffered.this.target.absorb(object);
			}
			synchronized (SourceBuffered.this) {
				if (SourceBuffered.this.queue == null) {
					SourceBuffered.this.queue = new QueueStackRecord<>();
				}
				SourceBuffered.this.queue.enqueue(object);
			}
			return SourceBuffered.this.many;
		}
		
		@Override
		public final Class<? extends BaseMessage> accepts() {
			
			return BaseMessage.class;
		}
		
		@Override
		public void close() {

			SourceBuffered.this.closed = true;
		}
	}

	final boolean many;

	private final ObjectTarget<BaseMessage> frontDoor;

	ObjectTarget<BaseMessage> target;

	QueueStackRecord<BaseMessage> queue;
	
	/**
	 * @param once
	 */
	public SourceBuffered(final boolean once) {
		
		this.many = !once;
		this.frontDoor = new FrontDoorTarget();
	}
	
	@Override
	public final void connectTarget(final ObjectTarget<BaseMessage> target) throws Exception {
		
		synchronized (this) {
			this.target = target;
			if (this.queue != null) {
				for (;;) {
					final BaseMessage message = this.queue.next();
					if (message == null || !target.absorb(message)) {
						break;
					}
				}
				this.queue = null;
			}
		}
	}
	
	/**
	 * @return
	 */
	public final ObjectTarget<BaseMessage> getFrontDoor() {
		
		return this.frontDoor;
	}
	
	@Override
	public final boolean isExhausted() {
		
		return this.closed;
	}
	
	@Override
	public final boolean isReady() {
		
		return this.target == null && this.queue != null && !this.queue.isEmpty();
	}
	
	@Override
	public final synchronized BaseMessage next() {
		
		return this.isReady()
			? this.queue.next()
			: null;
	}
}
