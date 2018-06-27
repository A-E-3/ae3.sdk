/*
 * Created on 22.04.2006
 */
package ru.myx.ae3.flow;

import java.util.function.Function;
import ru.myx.util.FifoQueueBuffered;

/**
 * TODO: unused really and inaccessible
 *
 *
 * @author myx
 *
 * @param <E>
 */
public class PipeVirtualAbstract<E> implements ObjectSourceAsync<E>, ObjectTarget<E>, Function<E, Boolean> {
	
	private ObjectTarget<E> target;

	private FifoQueueBuffered<E> queue;

	private final Class<? extends E> elementClass;

	/**
	 * @param elementClass
	 */
	public PipeVirtualAbstract(final Class<? extends E> elementClass) {
		this.elementClass = elementClass;
	}

	@Override
	public final boolean absorb(final E object) {
		
		final ObjectTarget<E> target = this.target;
		if (target == null) {
			synchronized (this) {
				if (this.target == null) {
					if (this.queue == null) {
						this.queue = new FifoQueueBuffered<>();
					}
					this.queue.offerLast(object);
				} else {
					this.target.absorb(object);
				}
			}
		} else {
			target.absorb(object);
		}
		return true;
	}

	@Override
	public final Class<? extends E> accepts() {
		
		return this.elementClass;
	}

	@Override
	public final void close() {
		
		// ignore
	}

	@Override
	public final void connectTarget(final ObjectTarget<E> target) throws Exception {
		
		synchronized (this) {
			if (target == null) {
				this.target = null;
				return;
			}
			if (this.target == null) {
				this.target = target;
				if (this.queue != null) {
					while (this.queue.hasNext()) {
						if (!this.target.absorb(this.queue.pollFirst())) {
							break;
						}
					}
					if (!this.queue.hasNext()) {
						this.queue = null;
					}
				}
				return;
			}
		}
		if (this.target != target) {
			throw new IllegalStateException("Target is already connected! Target: " + this.target + ", replacement: " + target);
		}
	}

	@Override
	public final Boolean apply(final E object) {
		
		final ObjectTarget<E> target = this.target;
		if (target == null) {
			synchronized (this) {
				if (this.target == null) {
					if (this.queue == null) {
						this.queue = new FifoQueueBuffered<>();
					}
					this.queue.offerLast(object);
				} else {
					this.target.absorb(object);
				}
			}
		} else {
			target.absorb(object);
		}
		return Boolean.TRUE;
	}

	@Override
	public final boolean isExhausted() {
		
		return false;
	}

	@Override
	public final boolean isReady() {
		
		if (this.queue == null) {
			return false;
		}
		synchronized (this) {
			if (this.queue == null) {
				return false;
			}
			return this.queue.hasNext();
		}
	}

	@Override
	public final E next() {
		
		synchronized (this) {
			if (this.queue == null) {
				return null;
			}
			return this.queue.pollFirst();
		}
	}

	@Override
	public String toString() {
		
		return "[object " + this.getClass().getSimpleName() + "{target:" + this.target + ", elementClass:" + this.elementClass + "}]";
	}
}
