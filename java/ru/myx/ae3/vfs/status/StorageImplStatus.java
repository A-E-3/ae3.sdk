package ru.myx.ae3.vfs.status;

import java.util.Iterator;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.status.StatusFiller;
import ru.myx.ae3.status.StatusProvider;
import ru.myx.ae3.status.StatusProviderFiller;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.VfsDefaultLimitSortMatcher;
import ru.myx.ae3.vfs.ars.AbstractStorageImplReadOnly;

/** @author myx */
public class StorageImplStatus extends AbstractStorageImplReadOnly<RecordStatus, ReferenceStatus, ArrayStatus> {

	private final StatusProvider provider;

	private final RecordStatus root;

	private final ReferenceStatus rootReference;

	/** @param provider
	 */
	public StorageImplStatus(final StatusFiller provider) {

		assert provider != null : "provider is NULL";
		this.provider = provider instanceof StatusProvider
			? (StatusProvider) provider
			: new StatusProviderFiller(String.valueOf(System.identityHashCode(provider)), provider, provider)//
		;
		this.root = new RecordStatus("status: " + this.provider.statusName(), this.provider);
		this.rootReference = new ReferenceStatus(null, this.root, this.root);
		assert this.rootReference.getTarget().isContainer() : "Container!";
	}

	@Override
	public RecordStatus createKeyForString(final String key) {

		return new RecordStatus(key, null);
	}

	@Override
	public Value<TransferCopier> getBinary(final RecordStatus object) {

		assert object != null : "NULL object";
		return object.getBinaryContent();
	}

	@Override
	public ReferenceStatus getLink(final RecordStatus object, final RecordStatus key, final TreeLinkType mode) {

		assert object != null : "object is NULL";
		assert object.provider != null : "object's provider is NULL";
		assert key != null : "key is NULL";
		assert key.key != null : "key's key is NULL";
		if (RecordStatus.DATA.containsKey(key.key)) {
			return new ReferenceStatus(object, key, new RecordStatus(key.key, object.provider));
		}
		{
			final StatusProvider[] providers = object.provider.childProviders();
			if (providers != null) {
				for (final StatusProvider provider : providers) {
					if (key.key.equals(provider.statusName())) {
						return new ReferenceStatus(object, key, new RecordStatus(key.key, provider));
					}
				}
			}
		}
		return null;
	}

	@Override
	public ArrayStatus getLinks(final RecordStatus object, final TreeReadType mode) {

		assert object != null : "object is NULL";
		final ArrayStatus signals = new ArrayStatus();
		if (object.provider == null) {
			return signals;
		}
		if (RecordStatus.DATA.containsKey(object.key)) {
			return signals;
		}

		for (final String key : RecordStatus.DATA.keySet()) {
			signals.add(new ReferenceStatus(object, new RecordStatus(key, object.provider)));
		}
		final StatusProvider[] providers = object.provider.childProviders();
		if (providers != null) {
			for (final StatusProvider provider : providers) {
				signals.add(new ReferenceStatus(object, new RecordStatus(provider.statusName(), provider)));
			}
		}
		return signals;
	}

	@Override
	public Value<ArrayStatus>
			getLinksRange(final RecordStatus object, final RecordStatus keyStart, final RecordStatus keyStop, final int limit, final boolean backwards, final TreeReadType mode) {

		final ArrayStatus signals = new ArrayStatus();
		assert object != null : "object is NULL";
		if (object.provider == null) {
			return signals;
		}
		if (RecordStatus.DATA.containsKey(object.key)) {
			return signals;
		}

		if (limit == 0) {
			for (final String key : RecordStatus.DATA.keySet()) {
				signals.add(new ReferenceStatus(object, new RecordStatus(key, object.provider)));
			}
			final StatusProvider[] providers = object.provider.childProviders();
			if (providers != null) {
				for (final StatusProvider provider : providers) {
					final String key = provider.statusName();
					signals.add(new ReferenceStatus(object, new RecordStatus(key, provider)));
				}
			}
			return signals;
		}
		{
			final String start = keyStart == null
				? null
				: keyStart.key;
			final String stop = keyStop == null
				? null
				: keyStop.key;
			final VfsDefaultLimitSortMatcher<String, RecordStatus> matcher = new VfsDefaultLimitSortMatcher<>(start, stop, limit, backwards);
			for (final String key : RecordStatus.DATA.keySet()) {
				matcher.put(key, new RecordStatus(key, object.provider));
			}
			final StatusProvider[] providers = object.provider.childProviders();
			if (providers != null) {
				for (final StatusProvider provider : providers) {
					final String key = provider.statusName();
					matcher.put(key, new RecordStatus(key, object.provider));
				}
			}
			final Iterator<RecordStatus> iterator = backwards
				? matcher.iteratorDescending()
				: matcher.iteratorAscending();
			while (iterator.hasNext()) {
				signals.add(new ReferenceStatus(object, iterator.next()));
			}
			return signals;
		}
	}

	@Override
	public ReferenceStatus getRootReference() {

		return this.rootReference;
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordStatus object) {

		assert object != null : "NULL object";
		return object.getTextContent();
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

		return "[object " + this.getClass().getSimpleName() + "(" + this.provider.statusName() + ": " + this.provider.statusDescription() + ")]";
	}
}
