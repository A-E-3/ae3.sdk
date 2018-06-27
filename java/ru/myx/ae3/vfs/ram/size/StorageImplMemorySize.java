package ru.myx.ae3.vfs.ram.size;

import java.util.Iterator;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSorter;
import ru.myx.ae3.vfs.ars.ArsStorageImpl;

/**
 * @author myx
 * 
 */
public final class StorageImplMemorySize implements ArsStorageImpl<RecordMemory, ReferenceMemory, ArrayMemory> {
	static Value<ArrayMemory> getLinksRangeImpl(
			final Map<RecordMemoryKey, ReferenceMemory> source,
			final RecordMemory keyStart,
			final RecordMemory keyStop,
			final int limit,
			final boolean backwards) {
	
		final ArrayMemory result = new ArrayMemory();
		if (source == null) {
			return result;
		}
		final Guid guidStart = keyStart == null
				? null
				: keyStart.getPrimitiveGuid();
		final Guid guidStop = keyStop == null
				? null
				: keyStop.getPrimitiveGuid();
		if (limit == 0) {
			for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : source.entrySet()) {
				if (guidStart != null || guidStop != null) {
					final RecordMemory key = reference.getKey();
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
				result.add( reference.getValue() );
			}
			return result;
		}
		{
			final VfsDefaultLimitSorter<Guid, ReferenceMemory> sorter = new VfsDefaultLimitSorter<>( limit, backwards );
			for (final Map.Entry<RecordMemoryKey, ReferenceMemory> reference : source.entrySet()) {
				final RecordMemory key = reference.getKey();
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
				sorter.put( guidKey, reference.getValue() );
			}
			final Iterator<ReferenceMemory> iterator = backwards
					? sorter.iteratorDescending()
					: sorter.iteratorAscending();
			while (iterator.hasNext()) {
				result.add( iterator.next() );
			}
			return result;
		}
	}
	
	private final ReferenceMemory	rootReference;
	
	
	/**
	 * 
	 */
	public StorageImplMemorySize() {
	
		this( null );
	}
	
	
	/**
	 * @param name
	 * 
	 */
	public StorageImplMemorySize(final String name) {
	
		final Guid guid = Guid.forString( name == null
				? "ramfs-size-root:" + System.identityHashCode( this )
				: name );
		this.rootReference = new ReferenceMemory( null,
				new RecordMemoryPrimitive( guid ),
				TreeLinkType.NO_REFERENCE,
				System.currentTimeMillis(),
				new RecordMemoryCollection() );
	}
	
	
	@Override
	public RecordMemory createBinaryTemplate(
			final TransferCopier copier) {
	
		return copier.length() == 0
				? RecordMemoryBinaryEmpty.INSTANCE
				: new RecordMemoryBinary( copier );
	}
	
	
	@Override
	public RecordMemory createContainerTemplate() {
	
		return new RecordMemoryCollection();
	}
	
	
	@Override
	public RecordMemory createKeyForString(
			final String key) {
	
		return new RecordMemoryKey( key );
		/**
		 * <code>
		final Guid guid = Guid.forString( key );
		return guid != null
				// and it is isInline() as per .forString(x) spec
				? new RecordMemoryNormal( guid )
				: new RecordMemoryKey( key );
		 * </code>
		 */
	}
	
	
	@Override
	public RecordMemory createPrimitiveTemplate(
			final Guid guid) {
	
		/**
		 * base objects are more compact cause primitives are mostly stored as
		 * no duplicates in memory.
		 */
		return new RecordMemoryBase( guid.getInlineBaseValue() );
		// return new RecordMemoryPrimitive( guid );
	}
	
	
	@Override
	public ReferenceMemory createReferenceTemplate(
			final RecordMemory key,
			final TreeLinkType mode,
			final ReferenceMemory original) {
	
		assert key != null : "Key should not be NULL here";
		assert mode != null : "Mode should not be NULL here";
		assert original != null : "Original should not be NULL here";
		return new ReferenceMemory( original.source, key, mode, original.lastModified, original.target );
	}
	
	
	@Override
	public RecordMemory createTextTemplate(
			final CharSequence text) {
	
		return text.length() == 0
				? RecordMemoryTextEmpty.INSTANCE
				: new RecordMemoryText( text );
	}
	
	
	@Override
	public TransactionMemory createTransaction() {
	
		return new TransactionMemory( this );
	}
	
	
	@Override
	public ReferenceMemory doLinkDelete(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode) {
	
		object.getCollection().remove( key );
		template.target = null;
		return template;
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
		newObject.getCollection().put( (RecordMemoryKey) newKey, reference );
		object.getCollection().remove( key );
		template.target = null;
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
		object.getCollection().put( (RecordMemoryKey) newKey, reference );
		object.getCollection().remove( key );
		template.target = null;
		return reference;
	}
	
	
	@Override
	public ReferenceMemory doLinkSet(
			final ReferenceMemory template,
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode,
			final long modified,
			final RecordMemory target) {
	
		final ReferenceMemory reference = new ReferenceMemory( object, key, mode, modified, target );
		if (target == null) {
			object.getCollection().remove( key );
			template.target = null;
			reference.target = null;
		} else {
			object.getCollection().put( (RecordMemoryKey) key, reference );
		}
		return reference;
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final RecordMemory object) {
	
		assert object != null : "NULL object";
		return object.getBinary();
	}
	
	
	@Override
	public ReferenceMemory getLink(
			final RecordMemory object,
			final RecordMemory key,
			final TreeLinkType mode) {
	
		assert object != null : "Object should never be NULL";
		final ReferenceMemory result = object.getCollection().get( key );
		return result != null
				? result
				: mode != null
						? new ReferenceMemory( object, key, mode, 0L, null )
						: null;
	}
	
	
	@Override
	public ArrayMemory getLinks(
			final RecordMemory object,
			final TreeReadType mode) {
	
		final Map<RecordMemoryKey, ReferenceMemory> source = object.getCollection();
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
	
		final Map<RecordMemoryKey, ReferenceMemory> source = object.getCollection();
		return StorageImplMemorySize.getLinksRangeImpl( source, keyStart, keyStop, limit, backwards );
	}
	
	
	@Override
	public ReferenceMemory getRootReference() {
	
		return this.rootReference;
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final RecordMemory object) {
	
		assert object != null : "NULL object";
		return Base.forString( object.getText() );
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
