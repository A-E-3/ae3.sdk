package ru.myx.ae3.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseHost;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;
import ru.myx.ae3.report.Report;

/** @author myx */
@ReflectionManual
public class CoarseDelayCache implements BaseHost, BaseObjectNoOwnProperties {
	
	private static final BaseObject PROTOTYPE = Reflect.classToBasePrototype(CoarseDelayCache.class);

	private static final ExecProcess ROOT = Exec.createProcess(Exec.getRootProcess(), "CoarseDelayCache cleaner loop");

	private static final Function<CoarseDelayCache, Void> TASK = new Function<CoarseDelayCache, Void>() {

		@Override
		public Void apply(final CoarseDelayCache arg) {

			arg.loop();
			return null;
		}
	};

	private final int interval;

	private final int steps;

	private final BaseFunction onDrop;

	private final Map<BaseObject, BaseObject>[] store;

	private int index;

	private boolean stopped = true;

	private final ExecProcess ctx;
	
	/** @param interval
	 * @param steps
	 * @param onDrop */
	@SuppressWarnings("unchecked")
	@ReflectionExplicit
	public CoarseDelayCache(final int interval, final int steps, final BaseFunction onDrop) {

		this.ctx = Exec.createProcess(CoarseDelayCache.ROOT, "CoarseDelayCache: maintenance context");
		this.interval = interval == 0
			? 20000
			: Math.min(Math.max(interval, 2000), 180000);
		this.steps = steps == 0
			? 2
			: Math.min(Math.max(steps, 1), 16);
		this.onDrop = onDrop;

		this.index = 0;
		this.store = new Map[steps];
		for (int i = 0; i < steps; ++i) {
			this.store[i] = new ConcurrentHashMap<>(128);
		}

	}

	@Override
	public BaseObject basePrototype() {
		
		return CoarseDelayCache.PROTOTYPE;
	}

	@Override
	public CoarseDelayCache baseValue() {
		
		return this;
	}

	/** @param key
	 * @return */
	@ReflectionExplicit
	public BaseObject dropRecord(final BaseObject key) {
		
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		int left = this.steps; //
		for (; left > 0; --left, index = (1 + index) % this.steps) {

			final BaseObject value = this.store[index].remove(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/** @return */
	@ReflectionExplicit
	public boolean isEmpty() {
		
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		int left = this.steps; //
		for (; left > 0; --left) {

			if (!this.store[index++ % this.steps].isEmpty()) {
				return false;
			}
		}
		return true;
	}

	final void loop() {
		
		if (this.stopped == true) {
			return;
		}

		final int index = (this.index - 1 & 0x7FFFFFFF) % this.steps;

		// keep purged
		final Map<BaseObject, BaseObject> stale = this.store[index];
		if (stale.isEmpty()) {
			synchronized (this.store) {
				if (this.isEmpty()) {
					this.stopped = true;
					return;
				}
			}

			// update index
			this.index = index;

			Act.later(this.ctx, CoarseDelayCache.TASK, this, this.interval);
			return;
		}

		// replace with fresh
		this.store[index] = new ConcurrentHashMap<>(128);

		// update index
		this.index = index;

		if (this.onDrop == null) {
			Act.later(this.ctx, CoarseDelayCache.TASK, this, this.interval);
			return;
		}

		BaseObject r;
		for (final BaseObject k : stale.keySet()) {
			if (null != (r = stale.get(k))) {
				try {

					if (BaseObject.TRUE == this.onDrop.callNE2(this.ctx, this, k, r)) {
						this.put(k, r);
					}

				} catch (final Throwable t) {
					Report.exception("CoarseDelayCache", "Unhandled exception while dropping: handler=" + Format.Describe.toEcmaSource(this.onDrop, ""), t);
				}
			}
		}

		Act.later(this.ctx, CoarseDelayCache.TASK, this, this.interval);
	}

	/** @param key
	 * @param value
	 * @return */
	@ReflectionExplicit
	public BaseObject put(final BaseObject key, final BaseObject value) {
		
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		int left = this.steps - 1; //
		final BaseObject previous = this.store[index++].put(key, value);
		for (; left > 0; --left) {

			this.store[index++ % this.steps].remove(key);
		}

		if (this.stopped) {
			synchronized (this.store) {
				if (!this.stopped) {
					return previous;
				}
				this.stopped = false;
			}
			Act.later(this.ctx, CoarseDelayCache.TASK, this, this.interval);
		}

		return previous;
	}

	/** @param key
	 * @return */
	@ReflectionExplicit
	public BaseObject readCheck(final BaseObject key) {
		
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		for (int left = this.steps; left > 0; --left, index = (1 + index) % this.steps) {
			final BaseObject value = this.store[index].get(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/** @param key
	 * @return */
	@ReflectionExplicit
	public BaseObject readUpdate(final BaseObject key) {
		
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		int left = this.steps; //
		for (; left > 0; --left, index = (1 + index) % this.steps) {

			final Map<BaseObject, BaseObject> store = this.store[index];
			final BaseObject value = store.get(key);
			if (value != null) {
				if (left != this.steps) {
					this.store[this.index].put(key, value);
					store.remove(key);
				}
				return value;
			}
		}
		return null;
	}

	/** @param key
	 * @return */
	@ReflectionExplicit
	public BaseObject remove(final BaseObject key) {
		
		return this.dropRecord(key);
	}

	/** @return */
	@ReflectionExplicit
	public int size() {
		
		int size = 0;
		// index updater keeps it in range, the mask is for the case of race
		// update corruption
		int index = this.index & 0x7FFFFFFF;
		int left = this.steps; //
		for (; left > 0; --left) {

			size += this.store[index++ % this.steps].size();
		}
		return size;
	}

	@Override
	public String toString() {
		
		return "[" + this.getClass().getSimpleName() + " (size=" + this.size() + ")]";
	}
}
