/**
 * 
 */
package ru.myx.ae3.vfs.ram.size;

import java.util.HashMap;
import java.util.Map;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.ars.ArsTransaction;
import ru.myx.ae3.vfs.ars.ArsTransactionNested;

final class TransactionMemory implements ArsTransaction<RecordMemory, ReferenceMemory, ArrayMemory> {
	StorageImplMemorySize										storage;
	
	Map<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>>	changes;
	
	
	TransactionMemory(final StorageImplMemorySize storage) {
	
		this.storage = storage;
	}
	
	
	@Override
	public void cancel() {
	
		this.changes = null;
	}
	
	
	@Override
	public void commit() {
	
		final Map<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>> changes = this.changes;
		if (changes == null) {
			return;
		}
		final long commitDate = System.currentTimeMillis();
		for (final Map.Entry<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>> record : changes.entrySet()) {
			final Map<RecordMemoryKey, ReferenceMemory> collection = record.getKey().getCollection();
			for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : record.getValue().entrySet()) {
				final ReferenceMemory target = reference.getValue();
				if (target == null) {
					collection.remove( reference.getKey() );
				} else {
					if (target.lastModified == Long.MIN_VALUE) {
						target.lastModified = commitDate;
					}
					collection.put( reference.getKey(), target );
				}
			}
		}
		this.changes = null;
	}
	
	
	@Override
	public RecordMemory createBinaryTemplate(
			final TransferCopier copier) {
	
		return this.storage.createBinaryTemplate( copier );
	}
	
	
	@Override
	public RecordMemory createContainerTemplate() {
	
		return this.storage.createContainerTemplate();
	}
	
	
	@Override
	public RecordMemory createKeyForString(
			final String key) {
	
		return this.storage.createKeyForString( key );
	}
	
	
	@Override
	public RecordMemory createPrimitiveTemplate(
			final Guid guid) {
	
		return this.storage.createPrimitiveTemplate( guid );
	}
	
	
	@Override
	public ReferenceMemory createReferenceTemplate(
			final RecordMemory key,
			final TreeLinkType mode,
			final ReferenceMemory original) {
	
		return this.storage.createReferenceTemplate( key, mode, original );
	}
	
	
	@Override
	public RecordMemory createTextTemplate(
			final CharSequence text) {
	
		return this.storage.createTextTemplate( text );
	}
	
	
	@Override
	public ArsTransaction<RecordMemory, ReferenceMemory, ArrayMemory> createTransaction() throws Exception {
	
		return new ArsTransactionNested<>( this );
	}
	
	
	@Override
	public Value<ReferenceMemory> doLinkDelete(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode) {
	
		final ReferenceMemory reference = new ReferenceMemory( object, key, mode, -1L, null );
		this.put( object, (RecordMemoryKey) key, reference );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceMemory> doLinkMoveRename(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final RecordMemory newObject,
			final RecordMemory newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {
	
		final ReferenceMemory reference = new ReferenceMemory( object, newKey, mode, modified, target );
		template.target = null;
		this.put( object, (RecordMemoryKey) key, template );
		this.put( newObject, (RecordMemoryKey) newKey, reference );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceMemory> doLinkRename(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final RecordMemory newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {
	
		final ReferenceMemory reference = new ReferenceMemory( object, newKey, mode, modified, target );
		template.target = null;
		this.put( object, (RecordMemoryKey) key, template );
		this.put( object, (RecordMemoryKey) newKey, reference );
		return reference;
	}
	
	
	@Override
	public Value<ReferenceMemory> doLinkSet(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {
	
		final ReferenceMemory reference = new ReferenceMemory( object, key, mode, modified, target );
		this.put( object, (RecordMemoryKey) key, reference );
		return reference;
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final RecordMemory object) {
	
		return this.storage.getBinary( object );
	}
	
	
	@Override
	public Value<ReferenceMemory> getLink(
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode) {
	
		assert object != null : "Object should never be NULL";
		final Map<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>> changes = this.changes;
		if (changes != null) {
			final Map<RecordMemoryKey, ReferenceMemory> children = changes.get( object );
			if (children != null) {
				final ReferenceMemory record = children.get( key );
				if (record != null) {
					return record;
				}
				if (children.containsKey( key )) {
					return new ReferenceMemory( object, key, mode, 0L, null );
				}
			}
		}
		return this.storage.getLink( object, key, mode );
	}
	
	
	@Override
	public Value<ArrayMemory> getLinks(
			final RecordMemory object,
			final TreeReadType mode) {
	
		final Map<RecordMemoryKey, ReferenceMemory> source;
		final Map<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>> changes = this.changes;
		if (changes == null) {
			source = object.getCollection();
		} else {
			final Map<RecordMemoryKey, ReferenceMemory> children = changes.get( object );
			if (children == null) {
				source = object.getCollection();
			} else {
				source = new HashMap<>( object.getCollection() );
				for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : children.entrySet()) {
					final ReferenceMemory target = reference.getValue();
					if (target == null) {
						source.remove( reference.getKey() );
					} else {
						source.put( reference.getKey(), target );
					}
				}
			}
		}
		final ArrayMemory result = new ArrayMemory();
		if (source != null) {
			for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : source.entrySet()) {
				result.add( reference.getValue() );
			}
		}
		return result;
	}
	
	
	@Override
	public Value<ArrayMemory> getLinksRange(
			final RecordMemory object,
			final RecordMemory keyStart,
			final RecordMemory keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		final Map<RecordMemoryKey, ReferenceMemory> source;
		final Map<RecordMemory, Map<RecordMemoryKey, ReferenceMemory>> changes = this.changes;
		if (changes == null) {
			source = object.getCollection();
		} else {
			final Map<RecordMemoryKey, ReferenceMemory> children = changes.get( object );
			if (children == null) {
				source = object.getCollection();
			} else {
				source = new HashMap<>( object.getCollection() );
				final Guid guidStart = keyStart == null
						? null
						: keyStart.getPrimitiveGuid();
				final Guid guidStop = keyStop == null
						? null
						: keyStop.getPrimitiveGuid();
				for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : children.entrySet()) {
					final ReferenceMemory target = reference.getValue();
					final RecordMemoryKey key = reference.getKey();
					if (target == null) {
						source.remove( key );
					} else {
						if (guidStart != null || guidStop != null) {
							final Guid guidKey = key.getPrimitiveGuid();
							if (guidStart != null && (backwards
									? guidStart.compareTo( guidKey ) < 0
									: guidStart.compareTo( guidKey ) > 0)) {
								continue;
							}
							if (guidStop != null && (backwards
									? guidStop.compareTo( guidKey ) >= 0
									: guidStop.compareTo( guidKey ) <= 0)) {
								continue;
							}
						}
						source.put( reference.getKey(), target );
					}
				}
			}
		}
		return StorageImplMemorySize.getLinksRangeImpl( source, keyStart, keyStop, limit, backwards );
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final RecordMemory object) {
	
		return this.storage.getText( object );
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		return this.storage.isHistorySupported();
	}
	
	
	@Override
	public boolean isReadOnly() {
	
		return this.storage.isReadOnly();
	}
	
	
	void put(
			final RecordMemory object,
			final RecordMemoryKey key,
			final ReferenceMemory value) {
	
		if (this.changes == null) {
			this.changes = new HashMap<>();
		}
		final Map<RecordMemoryKey, ReferenceMemory> changes;
		{
			final Map<RecordMemoryKey, ReferenceMemory> changesFound = this.changes.get( object );
			if (changesFound == null) {
				changes = new HashMap<>();
				this.changes.put( object, changes );
			} else {
				changes = changesFound;
			}
		}
		changes.put( key, value );
	}
}
