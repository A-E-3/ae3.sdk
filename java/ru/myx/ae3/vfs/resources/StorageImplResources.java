package ru.myx.ae3.vfs.resources;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSortMatcher;
import ru.myx.ae3.vfs.ars.AbstractArsStorageReadOnly;

/** @author myx */
public final class StorageImplResources //
		extends
			AbstractArsStorageReadOnly<RecordResources, ReferenceResources, ArrayResources> {

	private final Class<?> anchor;

	private final RecordResources root;

	/** @param anchor */
	public StorageImplResources(final Class<?> anchor) {

		this.anchor = anchor;
		this.root = new RecordResources(anchor, "");
	}

	/** @param name
	 *            relative to anchor, will root of the storage
	 * @param anchor */
	public StorageImplResources(final String name, final Class<?> anchor) {

		this.anchor = anchor;
		this.root = new RecordResources(anchor, name);
	}

	@Override
	public RecordResources createKeyForString(final String key) {

		return new RecordResources(this.anchor, key);
	}

	@Override
	public Value<TransferCopier> getBinary(final RecordResources object) {

		assert object != null : "NULL object";
		return object.getBinaryContent();
	}

	@Override
	public Value<ReferenceResources> getLink(final RecordResources object, final RecordResources key, final TreeLinkType mode) {

		final ReferenceResources reference = new ReferenceResources(
				object,
				new RecordResources(
						this.anchor,
						object.key.length() > 0
							? object.key + '/' + key.key
							: key.key));
		return reference;
	}

	@Override
	public ArrayResources getLinks(final RecordResources object, final TreeReadType mode) {

		return this.getLinksRange(object, null, null, 0, false, mode);
	}

	@Override
	public ArrayResources getLinksRange(final RecordResources object,
			final RecordResources keyStart,
			final RecordResources keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {

		int left = limit;
		final ArrayResources result = new ArrayResources();
		for (final String source : new String[]{
				object.key, object.key + "/!file-list.txt", object.key
		}) {
			final InputStream input = this.anchor.getResourceAsStream(source);
			if (input != null) {
				final String text;
				try {
					text = Transfer.createBuffer(input).toString(StandardCharsets.UTF_8);
				} catch (final NullPointerException e) {
					/** this happens with JarURLConnection, folder listings are not supported and
					 * produce unchecked error:
					 * <p>
					 * <code>
						Caused by: java.lang.NullPointerException
							at java.io.FilterInputStream.close(Unknown Source)
							at sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream.close(Unknown Source)
							at ru.myx.ae3.binary.Transfer.createBuffer(Transfer.java:226)
							at ru.myx.ae3.vfs.resources.RecordResources.isContainer(RecordResources.java:100)
							... 25 more
					 * </code> */
					continue;
				}
				for (final StringTokenizer st = new StringTokenizer(text, "\r\n"); st.hasMoreTokens();) {
					final String child = st.nextToken().trim();
					if (child.length() == 0) {
						continue;
					}
					final String key = object.key;
					if (keyStart != null || keyStop != null) {
						if (keyStart != null && (backwards
							? keyStart.key.compareTo(key) <= 0
							: keyStart.key.compareTo(key) >= 0)) {
							continue;
						}
						if (keyStop != null && (backwards
							? keyStop.key.compareTo(key) > 0
							: keyStop.key.compareTo(key) < 0)) {
							continue;
						}
					}
					/** TODO: INVALID: must be sorted */
					if (--left == 0) {
						break;
					}
					result.add(
							new ReferenceResources(
									object,
									new RecordResources(
											this.anchor,
											key.length() > 0
												? object.key + '/' + child
												: child)));
				}
				break;
			}
		}
		if (limit == 0) {
			return result;
		}
		/** TODO: there should be simpler way to sort them!
		 *
		 * no need for keys, already filtered */
		final VfsDefaultLimitSortMatcher<String, ReferenceResources> sorter = new VfsDefaultLimitSortMatcher<>(null, null, limit, backwards);
		for (final ReferenceResources r : result) {
			sorter.put(r.getKeyString(), r);
		}
		result.clear();
		final Iterator<ReferenceResources> iterator = backwards
			? sorter.iteratorDescending()
			: sorter.iteratorAscending();
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		return result;
	}

	@Override
	public ReferenceResources getRootReference() {

		return new ReferenceResources(null, this.root);
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordResources object) {

		assert object != null : "NULL object";
		return Base.forString((CharSequence) object.getBinaryContent().baseValue().toStringUtf8());
	}

	@Override
	public boolean isHistorySupported() {

		return false;
	}

	@Override
	public void shutdown() throws Exception {

		// do nothing
	}

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "(" + this.anchor.getName() + ")]";
	}
}
