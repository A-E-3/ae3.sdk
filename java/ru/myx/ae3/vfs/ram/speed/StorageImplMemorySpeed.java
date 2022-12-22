package ru.myx.ae3.vfs.ram.speed;

import java.util.Iterator;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSorter;
import ru.myx.ae3.vfs.ars.ArsStorage;

/** @author myx */
public final class StorageImplMemorySpeed //
		implements
			ArsStorage<RecordMemory, ReferenceMemory, ArrayMemory> //
{

	static Value<ArrayMemory>
			getLinksRangeImpl(final Map<RecordMemory, ReferenceMemory> source, final RecordMemory keyStart, final RecordMemory keyStop, final int limit, final boolean backwards) {

		final ArrayMemory result = new ArrayMemory();
		if (source == null) {
			return result;
		}
		if (limit == 0) {
			for (final Map.Entry<RecordMemory, ReferenceMemory> reference : source.entrySet()) {
				if (keyStart != null || keyStop != null) {
					final RecordMemory key = reference.getKey();
					if (keyStart != null && (backwards
						? keyStart.primitive.compareTo(key.primitive) <= 0
						: keyStart.primitive.compareTo(key.primitive) >= 0)) {
						continue;
					}
					if (keyStop != null && (backwards
						? keyStop.primitive.compareTo(key.primitive) > 0
						: keyStop.primitive.compareTo(key.primitive) < 0)) {
						continue;
					}
				}
				result.add(reference.getValue());
			}
			return result;
		}
		{
			final VfsDefaultLimitSorter<Guid, ReferenceMemory> sorter = new VfsDefaultLimitSorter<>(limit, backwards);
			for (final Map.Entry<RecordMemory, ReferenceMemory> reference : source.entrySet()) {
				final RecordMemory key = reference.getKey();
				if (keyStart != null && (backwards
					? keyStart.primitive.compareTo(key.primitive) <= 0
					: keyStart.primitive.compareTo(key.primitive) >= 0)) {
					continue;
				}
				if (keyStop != null && (backwards
					? keyStop.primitive.compareTo(key.primitive) > 0
					: keyStop.primitive.compareTo(key.primitive) < 0)) {
					continue;
				}
				sorter.put(key.primitive, reference.getValue());
			}
			final Iterator<ReferenceMemory> iterator = backwards
				? sorter.iteratorDescending()
				: sorter.iteratorAscending();
			while (iterator.hasNext()) {
				result.add(iterator.next());
			}
		}
		return result;
	}

	private final ReferenceMemory rootReference;

	/** @param name */
	public StorageImplMemorySpeed(final String name) {

		final Guid guid = Guid.forString(
				name == null
					? "ramfs-speed-root:" + System.identityHashCode(this)
					: name);
		this.rootReference = new ReferenceMemory(null, new RecordMemory(guid), TreeLinkType.NO_REFERENCE, System.currentTimeMillis(), new RecordMemory());
	}

	@Override
	public RecordMemory createBinaryTemplate(final TransferCopier copier) {

		return new RecordMemory(copier);
	}

	@Override
	public RecordMemory createContainerTemplate() {

		return new RecordMemory();
	}

	@Override
	public RecordMemory createKeyForString(final String key) {

		return new RecordMemory(Guid.forString(key));
	}

	@Override
	public RecordMemory createPrimitiveTemplate(final Guid guid) {

		return new RecordMemory(guid);
	}

	@Override
	public ReferenceMemory createReferenceTemplate(final RecordMemory key, final TreeLinkType mode, final ReferenceMemory original) {

		assert key != null : "Key should not be NULL here";
		assert mode != null : "Mode should not be NULL here";
		assert original != null : "Original should not be NULL here";
		return new ReferenceMemory(original.source, key, mode, original.lastModified, original.target);
	}

	@Override
	public RecordMemory createTextTemplate(final CharSequence text) {

		return new RecordMemoryText(text);
	}

	@Override
	public TransactionMemory createTransaction() {

		return new TransactionMemory(this);
	}

	@Override
	public ReferenceMemory doLinkDelete(final ReferenceMemory template, final RecordMemory object, final RecordMemory key, final TreeLinkType mode) {

		object.collection.remove(key);
		if (template != null) {
			template.target = null;
		}
		return template;
	}

	@Override
	public Value<ReferenceMemory> doLinkMoveRename(final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final RecordMemory newObject,
			final RecordMemory newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {

		final ReferenceMemory reference = new ReferenceMemory(object, newKey, mode, modified, target);
		{
			newObject.collection.put(newKey, reference);
			object.collection.remove(key);
			template.target = null;
		}
		return reference;
	}

	@Override
	public Value<ReferenceMemory> doLinkRename(final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final RecordMemory newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {

		final ReferenceMemory reference = new ReferenceMemory(object, newKey, mode, modified, target);
		{
			object.collection.put(newKey, reference);
			object.collection.remove(key);
			template.target = null;
		}
		return reference;
	}

	@Override
	public ReferenceMemory
			doLinkSet(final ReferenceMemory template, final RecordMemory object, final RecordMemory key, final TreeLinkType mode, final long modified, final RecordMemory target) {

		if (target == null) {
			object.collection.remove(key);
			if (template != null) {
				template.target = null;
			}
			return template;
		}
		final ReferenceMemory reference = new ReferenceMemory(object, key, mode, modified, target);
		object.collection.put(key, reference);
		return reference;
	}

	@Override
	public Value<TransferCopier> getBinary(final RecordMemory object) {

		assert object != null : "NULL object";
		return (TransferCopier) object.binary;
	}

	@Override
	public ReferenceMemory getLink(final RecordMemory object, final RecordMemory key, final TreeLinkType mode) {

		assert object != null : "Object should never be NULL";
		final ReferenceMemory result = object.collection.get(key);
		return result != null
			? result
			: mode != null
				? new ReferenceMemory(object, key, mode, 0L, null)
				: null;
	}

	@Override
	public ArrayMemory getLinks(final RecordMemory object, final TreeReadType mode) {

		final Map<RecordMemory, ReferenceMemory> source = object.collection;
		final ArrayMemory result = new ArrayMemory();
		if (source != null) {
			for (final Map.Entry<RecordMemory, ReferenceMemory> reference : source.entrySet()) {
				result.add(reference.getValue());
			}
		}
		return result;
	}

	@Override
	public Value<ArrayMemory>
			getLinksRange(final RecordMemory object, final RecordMemory keyStart, final RecordMemory keyStop, final int limit, final boolean backwards, final TreeReadType mode) {

		final Map<RecordMemory, ReferenceMemory> source = object.collection;
		return StorageImplMemorySpeed.getLinksRangeImpl(source, keyStart, keyStop, limit, backwards);
	}

	@Override
	public ReferenceMemory getRootReference() {

		return this.rootReference;
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordMemory object) {

		assert object != null : "NULL object";
		return Base.forString((CharSequence) object.binary);
	}

	@Override
	public boolean isHistorySupported() {

		return false;
	}

	@Override
	public boolean isReadOnly() {

		return false;
	}

	@Override
	public void shutdown() throws Exception {

		// do nothing
	}

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "(" + this.getRootReference().getKeyString() + ")]";
	}
}
