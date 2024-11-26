/**
 *
 */
package ru.myx.ae3.stat;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.ecma.compare.ComparatorEcma;
import ru.myx.ae3.help.Format;
import ru.myx.util.Counter;

/** @author myx
 * @param <T> */
public class UnfairIdentityDetector<T> {
	
	private static final long DELAY = 300_000L;

	private static final int SIZE = 4096;

	private static final int MASK = UnfairIdentityDetector.SIZE - 1;

	private static final int MARK = 384;

	private final Object[] fifoIdentity;

	private final long[] fifoDate;

	private final Map<T, Counter> tree;

	private int head = 0;

	private int tail = 0;

	private int size = 0;

	/** @param comparator */
	public UnfairIdentityDetector(final Comparator<T> comparator) {
		
		this.fifoIdentity = new Object[UnfairIdentityDetector.SIZE];
		this.fifoDate = new long[UnfairIdentityDetector.SIZE];
		if (comparator == null) {
			@SuppressWarnings("unchecked")
			final Comparator<T> comp = (Comparator<T>) ComparatorEcma.INSTANCE;
			this.tree = new TreeMap<>(comp);
		} else {
			this.tree = new TreeMap<>(comparator);
		}
	}

	/** @param identity
	 * @param time
	 * @return true when should be banned */
	public boolean register(final T identity, final long time) {
		
		// clean
		{
			for (int i = this.size; i > 0; --i) {
				final int index = this.tail & UnfairIdentityDetector.MASK;
				final Object check = this.fifoIdentity[index];
				if (check == null) {
					break;
				}
				final long expire = this.fifoDate[index];
				if (expire > time) {
					break;
				}
				final Counter counter = this.tree.get(check);
				if (counter != null) {
					if (counter.intValue() < 2) {
						this.tree.remove(check);
					} else {
						counter.register(-1);
					}
				}
				this.fifoIdentity[index] = null;
				this.tail++;
				this.size--;
			}
		}
		final int weight;
		// register
		{
			final Counter existing = this.tree.get(identity);
			if (existing == null) {
				final Counter created = new Counter(1);
				this.tree.put(identity, created);
				weight = 1;
			} else {
				existing.register(1);
				weight = existing.intValue();
			}
		}
		// clean
		{
			final int index = this.head & UnfairIdentityDetector.MASK;
			this.fifoDate[index] = time + UnfairIdentityDetector.DELAY;
			final Object check = this.fifoIdentity[index];
			if (check == null) {
				this.size++;
				this.head++;
				final int mark = UnfairIdentityDetector.MARK * (UnfairIdentityDetector.SIZE + this.size) / (UnfairIdentityDetector.SIZE * 2);
				return weight > mark;
			}
			final Counter counter = this.tree.get(check);
			if (counter != null) {
				if (counter.intValue() < 2) {
					this.tree.remove(check);
				} else {
					counter.register(-1);
				}
			}
			this.tail++;
			this.head++;
			return weight > UnfairIdentityDetector.MARK;
		}
	}

	@Override
	public String toString() {
		
		return "STAT(DELAY=" + Format.Compact.toPeriod(UnfairIdentityDetector.DELAY) + "SIZE=" + this.size + "/" + UnfairIdentityDetector.SIZE + ", UNIQUE=" + this.tree.size()
				+ ")";
	}
}
