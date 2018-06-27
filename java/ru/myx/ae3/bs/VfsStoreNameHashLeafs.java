package ru.myx.ae3.bs;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.EntryContainer;
import ru.myx.ae3.vfs.TreeReadType;

/**
 *
 * @author myx
 *
 */
public class VfsStoreNameHashLeafs implements BlobStore<Entry> {
	
	
	private static final int FOLDERS_NAME_RADIX = 32;

	private static final int FOLDERS_COUNT = 1024;

	private static final int FOLDERS_MASK = VfsStoreNameHashLeafs.FOLDERS_COUNT - 1;

	private final EntryContainer root;

	private final EntryContainer[] folders;

	private static final String nameForIndex(final int index) {
		
		
		return index < VfsStoreNameHashLeafs.FOLDERS_NAME_RADIX
			? "0" + Integer.toString(index, VfsStoreNameHashLeafs.FOLDERS_NAME_RADIX)
			: Integer.toString(index, VfsStoreNameHashLeafs.FOLDERS_NAME_RADIX);
	}

	/**
	 *
	 * @param root
	 */
	public VfsStoreNameHashLeafs(final Entry root) {
		if (!root.isContainer()) {
			throw new IllegalArgumentException("Does not exist or not a directory: " + root.getLocation());
		}
		this.root = root.toContainer();

		this.folders = new EntryContainer[VfsStoreNameHashLeafs.FOLDERS_COUNT];
		for (int i = VfsStoreNameHashLeafs.FOLDERS_MASK; i >= 0; --i) {
			this.folders[i] = this.root.relativeFolder(VfsStoreNameHashLeafs.nameForIndex(i));
		}
	}

	/**
	 *
	 * @throws Exception
	 */
	public void checkNormalizeStructure() throws Exception {
		
		
		final String leafs = this.root.relativeText(".leafs").toString();
		if (!"1024:32".equals(leafs)) {
			Act.launch(null, () -> {
				System.out.println(this.getClass().getSimpleName() + "(" + this.root.getLocation() + "): leafs: " + Format.Ecma.string(leafs) + ", normalizing...");
				final int count;
				try {
					count = VfsStoreNameHashLeafs.this.forceNormalizeStructure();
				} catch (final Exception e) {
					System.out.println(this.getClass().getSimpleName() + "(" + this.root.getLocation() + "): normalization failed: " + e.getMessage());
					return;
				}
				VfsStoreNameHashLeafs.this.root.setContentPublicTreeBinary(".leafs", Transfer.createCopierUtf8("1024:32")).baseValue();
				System.out.println(this.getClass().getSimpleName() + "(" + this.root.getLocation() + "): normalized, " + count + " files moved.");
			});
		}
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public int forceNormalizeStructure() throws Exception {
		
		
		int count = 0;
		for (final Entry entry : this.root.getContentCollection(TreeReadType.ITERABLE).baseValue()) {
			final String name = entry.getKey();
			if (name.length() <= 2 || name.charAt(0) == '.') {
				continue;
			}
			if (entry.isFile()) {
				final Entry target = this.putPrepare(name);
				entry.doMoveRename(target).baseValue();
				++count;
			}
		}
		return count;
	}

	@Override
	public Entry getPrepare(final String name) {
		
		
		final int index = name.hashCode();
		final EntryContainer folder = this.folders[index & VfsStoreNameHashLeafs.FOLDERS_MASK];
		if (!folder.isContainer()) {
			return null;
		}
		final Entry result = folder.relative(name, null);
		return result;
	}

	@Override
	public Entry putPrepare(final String name) throws Exception {
		
		
		final int index = name.hashCode();
		final EntryContainer folder = this.folders[index & VfsStoreNameHashLeafs.FOLDERS_MASK];
		if (!folder.isContainer()) {
			folder.doSetContainer().baseValue();
		}
		final Entry result = folder.relativeFile(name);
		return result;
	}

	@Override
	public boolean putBinary(final String name, final TransferCopier binary) throws Exception {
		
		
		final int index = name.hashCode();
		final EntryContainer folder = this.folders[index & VfsStoreNameHashLeafs.FOLDERS_MASK];
		if (!folder.isContainer()) {
			folder.doSetContainer().baseValue();
		}
		folder.setContentPublicTreeBinary(name, binary).baseValue();
		return true;
	}

	@Override
	public boolean drop(final String name) throws Exception {
		
		
		final int index = name.hashCode();
		final EntryContainer folder = this.folders[index & VfsStoreNameHashLeafs.FOLDERS_MASK];
		if (!folder.isExist()) {
			return false;
		}
		folder.setContentUndefined(name).baseValue();
		return true;
	}

	@Override
	public TransferCopier getBinary(final String name) throws Exception {
		
		
		final int index = name.hashCode();
		final EntryContainer folder = this.folders[index & VfsStoreNameHashLeafs.FOLDERS_MASK];
		if (!folder.isContainer()) {
			return null;
		}
		return folder.relativeBinary(name);
	}

	@Override
	public TransferCopier readBinary(final Entry desc) throws Exception {
		
		
		return desc.isBinary()
			? desc.toBinary().getBinary()
			: null;
	}

	@Override
	public Entry writeBinary(final Entry desc, final TransferCopier binary) throws Exception {
		
		
		desc.doSetBinary(binary).baseValue();
		return desc;
	}

	@Override
	public String toString() {
		
		
		return "[" + this.getClass().getSimpleName() + "(" + this.root + ")]";
	}
}
