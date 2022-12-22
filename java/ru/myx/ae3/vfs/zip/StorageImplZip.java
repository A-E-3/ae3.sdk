package ru.myx.ae3.vfs.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSortMatcher;
import ru.myx.ae3.vfs.ars.AbstractArsStorageReadOnly;
import ru.myx.io.InputStreamNoCloseFilter;

/**
 * @author myx
 * 
 */
public final class StorageImplZip extends AbstractArsStorageReadOnly<RecordZip, ReferenceZip, ArrayZip> {
	
	String													identity;
	
	long													lastModified;
	
	IdentityHashMap<RecordZip, Map<RecordZip, RecordZip>>	children;
	
	private final RecordZip									root;
	
	private final ReferenceZip								rootReference;
	
	
	/**
	 * @param identity
	 * @param zip
	 * @param lastModified
	 */
	public StorageImplZip(final String identity, final TransferCopier zip, final long lastModified) {
	
		this.lastModified = lastModified;
		this.root = new RecordZip( this, null, null );
		this.rootReference = new ReferenceZip( new RecordZip( this, identity, null ), this.root );
		this.children = new IdentityHashMap<>();
		this.replaceZip( identity, zip, lastModified );
	}
	
	
	@Override
	public RecordZip createKeyForString(
			final String key) {
	
		return new RecordZip( this, key, null );
	}
	
	
	@Override
	public Value<TransferCopier> getBinary(
			final RecordZip object) {
	
		assert object != null : "NULL object";
		return object.content;
	}
	
	
	@Override
	public Value<ReferenceZip> getLink(
			final RecordZip object,
			final RecordZip key,
			final TreeLinkType mode) {
	
		assert object != null : "NULL object";
		assert key != null : "NULL key";
		assert key.key != null : "Not a key!";
		final Map<RecordZip, RecordZip> children = this.children.get( object );
		if (children == null) {
			return null;
		}
		final RecordZip record = children.get( key );
		if (record == null) {
			return null;
		}
		final ReferenceZip reference = new ReferenceZip( object, key, record );
		return reference;
	}
	
	
	@Override
	public ArrayZip getLinks(
			final RecordZip object,
			final TreeReadType mode) {
	
		assert object != null : "NULL object";
		final ArrayZip result = new ArrayZip();
		final Map<RecordZip, RecordZip> children = this.children.get( object );
		if (children != null) {
			for (final Map.Entry<RecordZip, RecordZip> record : children.entrySet()) {
				result.add( new ReferenceZip( object, record.getKey(), record.getValue() ) );
			}
		}
		return result;
	}
	
	
	@Override
	public Value<ArrayZip> getLinksRange(
			final RecordZip object,
			final RecordZip keyStart,
			final RecordZip keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {
	
		assert object != null : "NULL object";
		final ArrayZip result = new ArrayZip();
		final Map<RecordZip, RecordZip> children = this.children.get( object );
		if (children == null) {
			return result;
		}
		if (limit == 0) {
			for (final Map.Entry<RecordZip, RecordZip> entry : children.entrySet()) {
				final RecordZip recordKey = entry.getKey();
				final String key = recordKey.key;
				if (keyStart != null && keyStart.key.compareTo( key ) >= 0) {
					continue;
				}
				if (keyStop != null && keyStop.key.compareTo( key ) < 0) {
					continue;
				}
				result.add( new ReferenceZip( object, recordKey, entry.getValue() ) );
			}
			return result;
		}
		{
			final String start = keyStart == null
					? null
					: keyStart.key;
			final String stop = keyStop == null
					? null
					: keyStop.key;
			final VfsDefaultLimitSortMatcher<String, Map.Entry<RecordZip, RecordZip>> matcher = new VfsDefaultLimitSortMatcher<>( start,
					stop,
					limit,
					backwards );
			for (final Map.Entry<RecordZip, RecordZip> entry : children.entrySet()) {
				final RecordZip recordKey = entry.getKey();
				final String key = recordKey.key;
				matcher.put( key, entry );
			}
			final Iterator<Map.Entry<RecordZip, RecordZip>> iterator = backwards
					? matcher.iteratorDescending()
					: matcher.iteratorAscending();
			while (iterator.hasNext()) {
				final Map.Entry<RecordZip, RecordZip> entry = iterator.next();
				result.add( new ReferenceZip( object, entry.getKey(), entry.getValue() ) );
			}
			return result;
		}
	}
	
	
	@Override
	public ReferenceZip getRootReference() {
	
		return this.rootReference;
	}
	
	
	@Override
	public Value<? extends CharSequence> getText(
			final RecordZip object) {
	
		assert object != null : "NULL object";
		return Base.forString( (CharSequence) object.key );
	}
	
	
	private RecordZip insertRecords(
			final IdentityHashMap<RecordZip, Map<RecordZip, RecordZip>> children,
			final TransferCopier copier,
			final String entryName) {
	
		RecordZip current = this.root;
		for (final StringTokenizer st = new StringTokenizer( entryName, "/" ); st.hasMoreTokens();) {
			final String name = st.nextToken();
			Map<RecordZip, RecordZip> local = children.get( current );
			if (local == null) {
				local = new TreeMap<>();
				children.put( current, local );
			}
			final RecordZip key = this.createKeyForString( name );
			if (st.hasMoreTokens()) {
				/**
				 * folder
				 */
				RecordZip folder = local.get( key );
				if (folder == null) {
					folder = new RecordZip( this, null, null );
					local.put( key, folder );
				}
				current = folder;
			} else {
				/**
				 * file
				 */
				RecordZip binary = local.get( key );
				if (binary == null) {
					binary = new RecordZip( this, null, copier );
					local.put( key, binary );
				}
				current = binary;
			}
		}
		return current;
	}
	
	
	@Override
	public boolean isHistorySupported() {
	
		return false;
	}
	
	
	/**
	 * @param identity
	 * @param zip
	 * @param lastModified
	 */
	public void replaceZip(
			final String identity,
			final TransferCopier zip,
			final long lastModified) {
	
		final IdentityHashMap<RecordZip, Map<RecordZip, RecordZip>> children = new IdentityHashMap<>();
		children.put( this.root, new TreeMap<RecordZip, RecordZip>() );
		
		try (final ZipInputStream zipFile = new ZipInputStream( zip.nextInputStream() )) {
			final InputStream filter = new InputStreamNoCloseFilter( zipFile );
			for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry()) {
				final String entryName = entry.getName().replace( '\\', '/' );
				final TransferCopier copier = Transfer.createBuffer( filter ).toBinary();
				this.insertRecords( children, copier, entryName );
			}
		} catch (final IOException e) {
			Report.exception( "SKIN", "Error registering zip skin interface (" + identity + ")", e );
		}
		
		this.identity = identity;
		this.children = children;
		this.lastModified = lastModified;
	}
	
	
	/**
	 * @param file
	 */
	public void replaceZipOnce(
			final File file) {
	
		final IdentityHashMap<RecordZip, Map<RecordZip, RecordZip>> children = new IdentityHashMap<>();
		children.put( this.root, new TreeMap<RecordZip, RecordZip>() );
		
		try (final ZipFile zipFile = new ZipFile( file )) {
			for (final Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
				final ZipEntry entry = entries.nextElement();
				final String entryName = entry.getName().replace( '\\', '/' );
				final InputStream stream = zipFile.getInputStream( entry );
				final TransferCopier copier = Transfer.createBuffer( stream ).toBinary();
				this.insertRecords( children, copier, entryName );
			}
		} catch (final IOException e) {
			Report.exception( "SKIN", "Error registering zip skin interface (" + file.getAbsolutePath() + ")", e );
		}
		
		this.identity = file.getAbsolutePath();
		this.children = children;
		this.lastModified = file.lastModified();
	}
	
	
	@Override
	public void shutdown() throws Exception {
	
		// do nothing
	}
	
	
	@Override
	public String toString() {
	
		return "ZIPFS{" + this.identity + "}";
	}
}
