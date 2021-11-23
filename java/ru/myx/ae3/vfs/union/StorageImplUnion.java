package ru.myx.ae3.vfs.union;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.EntryContainer;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.ars.ArsStorageImpl;

/** @author myx */
public class StorageImplUnion implements ArsStorageImpl<RecordReferenceUnion, RecordReferenceUnion, ArrayUnion> {

	private final RecordReferenceUnion rootReference;

	private final Entry[] entries;

	/** first checked first
	 *
	 * changes are written to first
	 *
	 *
	 * @param entries */
	public StorageImplUnion(final Entry[] entries) {

		assert entries != null : "NULL";
		assert entries.length > 1 : "Should have more than one element!";
		this.entries = entries;
		this.rootReference = new RecordReferenceUnion(null, entries);
	}

	@Override
	public RecordReferenceUnion createBinaryTemplate(final TransferCopier copier) {

		return new RecordUnionBinaryTemplate(copier);
	}

	@Override
	public RecordReferenceUnion createContainerTemplate() {

		return new RecordUnionCollectionTemplate();
	}

	@Override
	public RecordReferenceUnion createKeyForString(final String key) {

		return new RecordUnionKey(key);
	}

	@Override
	public RecordReferenceUnion createPrimitiveTemplate(final Guid guid) {

		return new RecordUnionPrimitiveTemplate(guid);
	}

	@Override
	public RecordReferenceUnion createReferenceTemplate(final RecordReferenceUnion key, final TreeLinkType mode, final RecordReferenceUnion original) {

		return new RecordUnionReferenceTemplate(key, mode, original);
	}

	@Override
	public RecordReferenceUnion createTextTemplate(final CharSequence text) {

		return new RecordUnionTextTemplate(text);
	}

	@Override
	public TransactionUnion createTransaction() throws Exception {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<RecordReferenceUnion>
			doLinkDelete(final RecordReferenceUnion template, final RecordReferenceUnion object, final RecordReferenceUnion key, final TreeLinkType mode) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public Value<RecordReferenceUnion> doLinkMoveRename(final RecordReferenceUnion template,
			final RecordReferenceUnion object,
			final RecordReferenceUnion key,
			final RecordReferenceUnion newObject,
			final RecordReferenceUnion newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordReferenceUnion target) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public Value<RecordReferenceUnion> doLinkRename(final RecordReferenceUnion template,
			final RecordReferenceUnion object,
			final RecordReferenceUnion key,
			final RecordReferenceUnion newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordReferenceUnion target) {

		throw new IllegalAccessError("Read only!");
	}

	@Override
	public Value<RecordReferenceUnion> doLinkSet(final RecordReferenceUnion template,
			final RecordReferenceUnion object,
			final RecordReferenceUnion key,
			final TreeLinkType mode,
			final long modified,
			final RecordReferenceUnion target) {

		assert mode != null : "Mode shouldn't be NULL";
		throw new IllegalAccessError("Read only!");
	}

	@Override
	public Value<? extends TransferCopier> getBinary(final RecordReferenceUnion object) {

		return object.getBinaryContent();
	}

	@Override
	public Value<RecordReferenceUnion> getLink(final RecordReferenceUnion object, final RecordReferenceUnion key, final TreeLinkType mode) {

		/** TODO: optional? asynchronous call */
		final Entry[] sourceEntries = object.entries;
		final int length = sourceEntries.length;
		final Entry[] targetEntries = new Entry[length];
		int targetIndex = 0;
		for (int i = 0; i < length; ++i) {
			final Entry source = sourceEntries[i];
			if (source == null || !source.isExist()) {
				continue;
			}
			final Entry child = source.toContainer().getContentElement(key.getKeyString(), mode).baseValue();
			if (child != null) {
				targetEntries[targetIndex++] = child;
			} else {
				assert mode == null : "Child should not ever be NULL inless mode is NULL";
			}
		}
		if (mode != null || targetIndex > 0) {
			return new RecordReferenceUnion(
					object,
					targetIndex < length
						? Arrays.copyOf(targetEntries, targetIndex)
						: targetEntries);
		}
		return null;
	}

	@Override
	public Value<ArrayUnion> getLinks(final RecordReferenceUnion object, final TreeReadType mode) {

		/** TODO: optional? asynchronous call */
		final ArrayUnion result = new ArrayUnion();
		final Map<String, Entry[]> check = Create.tempMap();
		final Entry[] sourceEntries = object.entries;
		final int length = sourceEntries.length;
		for (int i = 0; i < length; ++i) {
			final Entry entry = sourceEntries[i];
			if (entry == null || !entry.isContainer()) {
				continue;
			}
			final EntryContainer container = entry.toContainer();
			if (container == null) {
				continue;
			}
			final Value<? extends BaseList<Entry>> arrayValue = container.getContentCollection(mode);
			if (arrayValue == null) {
				continue;
			}
			final BaseList<Entry> entries = arrayValue.baseValue();
			if (entries == null) {
				continue;
			}
			for (final Entry item : entries) {
				final String key = item.getKey();
				Entry[] existing = check.get(key);
				if (existing == null) {
					existing = new Entry[length];
					result.add(new RecordReferenceUnion(object, existing));
					check.put(key, existing);
				}
				existing[i] = item;
			}
		}
		return result;
	}

	@Override
	public Value<ArrayUnion> getLinksRange(final RecordReferenceUnion object,
			final RecordReferenceUnion keyStart,
			final RecordReferenceUnion keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {

		final Entry[] sourceEntries = object.entries;
		final int length = sourceEntries.length;
		/** No string needed */
		if (limit == 0) {
			final ArrayUnion result = new ArrayUnion();
			final Map<String, Entry[]> check = Create.tempMap();
			for (int i = 0; i < length; ++i) {
				final Entry entry = sourceEntries[i];
				if (entry == null || !entry.isContainer()) {
					continue;
				}
				final EntryContainer container = entry.toContainer();
				if (container == null) {
					continue;
				}
				final Value<? extends BaseList<Entry>> arrayValue //
						= container.getContentRange(
								keyStart == null
									? null
									: keyStart.getKeyString(), //
								keyStop == null
									? null
									: keyStop.getKeyString(),
								limit,
								backwards,
								mode);
				if (arrayValue == null) {
					continue;
				}
				final BaseList<Entry> entries = arrayValue.baseValue();
				if (entries == null) {
					continue;
				}
				for (final Entry item : entries) {
					final String key = item.getKey();
					Entry[] existing = check.get(key);
					if (existing == null) {
						existing = new Entry[length];
						result.add(new RecordReferenceUnion(object, existing));
						check.put(key, existing);
					}
					existing[i] = item;
				}
			}
			return result;
		}

		{
			final NavigableMap<String, Entry[]> map = new TreeMap<>();
			int left = limit;
			for (int i = 0; i < length; ++i) {
				final Entry entry = sourceEntries[i];
				if (entry == null || !entry.isContainer()) {
					continue;
				}
				final EntryContainer container = entry.toContainer();
				if (container == null) {
					continue;
				}
				final Value<? extends BaseList<Entry>> arrayValue //
						= container.getContentRange(
								keyStart == null
									? null
									: keyStart.getKeyString(), //
								keyStop == null
									? null
									: keyStop.getKeyString(),
								limit,
								backwards,
								mode);
				if (arrayValue == null) {
					continue;
				}
				final BaseList<Entry> entries = arrayValue.baseValue();
				if (entries == null) {
					continue;
				}
				for (final Entry item : entries) {
					final String key = item.getKey();
					Entry[] existing = map.get(key);
					if (existing == null) {
						if (left == 0) {
							if (backwards) {
								final String smallest = map.firstKey();
								if (smallest.compareTo(key) < 0) {
									map.pollFirstEntry();
								} else {
									continue;
								}
							} else {
								final String biggest = map.lastKey();
								if (biggest.compareTo(key) > 0) {
									map.pollLastEntry();
								} else {
									continue;
								}
							}
						} else {
							--left;
						}
						existing = new Entry[length];
						map.put(key, existing);
					}
					existing[i] = item;
				}
			}

			final ArrayUnion result = new ArrayUnion();
			final Iterator<Entry[]> iterator = backwards
				? map.descendingMap().values().iterator()
				: map.values().iterator();
			while (iterator.hasNext()) {
				result.add(new RecordReferenceUnion(object, iterator.next()));
			}
			return result;
		}
	}

	@Override
	public RecordReferenceUnion getRootReference() {

		return this.rootReference;
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordReferenceUnion object) {

		return object.getTextContent();
	}

	@Override
	public boolean isHistorySupported() {

		return false;
	}

	@Override
	public boolean isReadOnly() {

		return true;
	}

	@Override
	public void shutdown() throws Exception {

		// do nothing
	}

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "{" + Arrays.asList(this.entries) + "}]";
	}

}
