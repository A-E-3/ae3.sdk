package ru.myx.ae3.vfs.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.ars.ArsStorage;
import ru.myx.ae3.vfs.ars.ArsTransactionBuffered;

/** @author myx */
public final class StorageImplFilesystem //
		implements
			ArsStorage<RecordFilesystem, ReferenceFilesystem, ArrayFilesystem> //
{

	private static final byte[] HASH_SPACE_GUID_CRLF_ASCII_BYTES = "# GUID\r\n".getBytes(StandardCharsets.US_ASCII);

	private final RecordFilesystem root;

	private final boolean readOnly;

	private final ReferenceFilesystem rootReference;

	/** @param root
	 * @param readOnly */
	public StorageImplFilesystem(final File root, final boolean readOnly) {

		this.root = new RecordFilesystem(root);
		this.rootReference = new ReferenceFilesystem(this.root);
		this.readOnly = readOnly;
	}

	/** @param name
	 * @param root
	 * @param readOnly */
	public StorageImplFilesystem(final String name, final File root, final boolean readOnly) {

		this.root = new RecordFilesystem(root);
		this.rootReference = new ReferenceFull(new RecordFilesystem(new File(name)), TreeLinkType.PUBLIC_TREE_REFERENCE, this.root);
		this.readOnly = readOnly;
	}

	@Override
	public RecordFilesystem createBinaryTemplate(final TransferCopier copier) {

		final RecordFilesystem result = new RecordFilesystemBinaryTemplate(copier);
		result.type = RecordType.BINARY;
		return result;
	}

	@Override
	public RecordFilesystem createContainerTemplate() {

		final RecordFilesystem result = new RecordFilesystem();
		result.type = RecordType.CONTAINER;
		return result;
	}

	@Override
	public RecordFilesystem createKeyForString(final String key) {

		return new RecordFilesystem(new File(key));
	}

	@Override
	public RecordFilesystem createPrimitiveTemplate(final Guid guid) {

		return new RecordFilesystemPrimitiveTemplate(guid);
	}

	@Override
	public ReferenceFilesystem createReferenceTemplate(final RecordFilesystem key, final TreeLinkType mode, final ReferenceFilesystem original) {

		return new ReferenceFull(key, mode, original.target);
	}

	@Override
	public RecordFilesystem createTextTemplate(final CharSequence text) {

		final RecordFilesystem result = new RecordFilesystemBinaryTextTemplate(Transfer.createCopierUtf8(text));
		result.type = RecordType.BINARY;
		return result;
	}

	@Override
	public ArsTransactionBuffered<RecordFilesystem, ReferenceFilesystem, ArrayFilesystem> createTransaction() throws Exception {

		return new ArsTransactionBuffered<>(this);
		// return new TransactionFilesystem();
	}

	@Override
	public Value<ReferenceFilesystem> doLinkDelete(final ReferenceFilesystem template, final RecordFilesystem object, final RecordFilesystem key, final TreeLinkType mode) {
		
		if (template != null) {
			if (template.target.file.exists()) {
				template.target.file.delete();
			}
			return template;
		}
		if (object != null && key != null) {
			new File(object.file, key.getKeyString()).delete();
		}
		return Base.forNull();
	}

	@Override
	public Value<ReferenceFilesystem> doLinkMoveRename(final ReferenceFilesystem template,
			final RecordFilesystem object,
			final RecordFilesystem key,
			final RecordFilesystem newObject,
			final RecordFilesystem newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordFilesystem target) {

		assert mode != null : "Mode shouldn't be NULL";

		assert key.file.getName().equals(template.target.file.getName()) //
				: "keys differ: " + key.file.getName() + " and " + template.target.file.getName();

		final File prev = new File(object.file, key.file.getName());
		final File file = new File(newObject.file, newKey.file.getName());
		if (!prev.renameTo(file)) {
			throw new RuntimeException("failed to rename " + prev + " to " + file + " !");
		}
		template.target = new RecordFilesystem(file);
		return template;
	}

	@Override
	public Value<ReferenceFilesystem> doLinkRename(final ReferenceFilesystem template,
			final RecordFilesystem object,
			final RecordFilesystem key,
			final RecordFilesystem newKey,
			final TreeLinkType mode,
			final long modified,
			final RecordFilesystem target) {

		assert mode != null : "Mode shouldn't be NULL";

		assert key.file.getName().equals(template.target.file.getName()) //
				: "keys differ: " + key.file.getName() + " and " + template.target.file.getName();

		final File prev = new File(object.file, key.file.getName());
		final File file = new File(object.file, newKey.file.getName());
		if (!prev.renameTo(file)) {
			throw new RuntimeException("failed to rename " + prev + " to " + file + " !");
		}
		template.target = new RecordFilesystem(file);
		return template;
	}

	@Override
	public ReferenceFilesystem doLinkSet(final ReferenceFilesystem templateOrNull,
			final RecordFilesystem object,
			final RecordFilesystem key,
			final TreeLinkType mode,
			final long modified,
			final RecordFilesystem target) {

		assert object != null : "Source object shouldn't be NULL";
		assert key != null : "Key object shouldn't be NULL";
		assert mode != null : "Mode shouldn't be NULL";

		final ReferenceFilesystem template;
		final File file;

		if (templateOrNull == null) {
			file = new File(object.file, key.file.getName());
			template = new ReferenceFull(
					key, //
					mode,
					new RecordFilesystem(file));
		} else {
			template = templateOrNull;
			file = template.target.file;
		}

		if (target == null) {
			try {
				Files.deleteIfExists(file.toPath());
			} catch (final IOException e) {
				Report.exception("VFS-FS", "While trying to delete", e);
			}
			return template;
		}

		if (target == template.target && template.target.file != null && template.target.file.exists()) {
			// nothing to do
		} else {
			if (target instanceof RecordFilesystemBinaryTemplate) {
				final File folder = file.getParentFile();
				final File work = file.exists()
					? new File(folder, ".$" + file.getName() + ".temp-out")
					: file;
				folder.mkdirs();
				try {
					Transfer.toStream(((RecordFilesystemBinaryTemplate) target).binary.nextCopy(), new FileOutputStream(work), true);
				} catch (final FileNotFoundException e) {
					throw new RuntimeException(e);
				} catch (final IOException e) {
					throw new RuntimeException("io error after " + Format.Compact.toBytes(work.length()) + "bytes written", e);
				}
				if (file != work) {
					final File temp = new File(file.getParentFile(), ".$" + file.getName() + ".temp-bak");
					temp.delete();
					file.renameTo(temp);
					work.renameTo(file);
					temp.delete();
				}
				template.target = new RecordFilesystem(file);
			} else //
			if (target instanceof RecordFilesystemPrimitiveTemplate) {
				final File work = file.exists()
					? new File(file.getParentFile(), ".$" + file.getName() + ".temp-out")
					: file;
				work.getParentFile().mkdirs();
				try {
					try (final FileOutputStream output = new FileOutputStream(work)) {
						output.write(StorageImplFilesystem.HASH_SPACE_GUID_CRLF_ASCII_BYTES);
						output.write(((RecordFilesystemPrimitiveTemplate) target).guid.toBytes());
					}
				} catch (final FileNotFoundException e) {
					throw new RuntimeException(e);
				} catch (final IOException e) {
					throw new RuntimeException("io error after " + Format.Compact.toBytes(work.length()) + "bytes written", e);
				}
				if (file != work) {
					final File temp = new File(file.getParentFile(), ".$" + file.getName() + ".temp-bak");
					temp.delete();
					file.renameTo(temp);
					work.renameTo(file);
					temp.delete();
				}
				template.target = new RecordFilesystem(file);
			} else {
				if (target.type == RecordType.CONTAINER) {
					file.mkdirs();
				}
				target.file = file;
				template.target = target;
			}
		}
		if (file.lastModified() / 1000 != modified / 1000) {
			template.target.file.setLastModified(modified);
		}
		return template;
	}

	@Override
	public Value<TransferCopier> getBinary(final RecordFilesystem object) {

		assert object != null : "NULL object";
		return object.getBinaryContent();
	}

	@Override
	public Value<ReferenceFilesystem> getLink(final RecordFilesystem object, final RecordFilesystem key, final TreeLinkType mode) {

		final File file = new File(object.file, key.file.getName());
		if (mode == null && !file.exists()) {
			return null;
		}
		final ReferenceFilesystem reference = new ReferenceFilesystem(new RecordFilesystem(file));
		return reference;
	}

	@Override
	public ArrayFilesystem getLinks(final RecordFilesystem object, final TreeReadType mode) {

		final ArrayFilesystemAll result = new ArrayFilesystemAll();
		object.file.listFiles(result);
		return result;
	}

	@Override
	public ArrayFilesystem getLinksRange(final RecordFilesystem object,
			final RecordFilesystem keyStart,
			final RecordFilesystem keyStop,
			final int limit,
			final boolean backwards,
			final TreeReadType mode) {

		final String keyStartString = keyStart == null
			? null
			: keyStart.file.getName();
		final String keyStopString = keyStop == null
			? null
			: keyStop.file.getName();
		if (limit == 0) {
			final ArrayFilesystemRange result = new ArrayFilesystemRange(keyStartString, keyStopString, limit);
			object.file.listFiles(result);
			return result;
		}
		{
			final VfsFilenameLimitSortMatcher matcher = new VfsFilenameLimitSortMatcher(keyStartString, keyStopString, limit, backwards);
			object.file.listFiles(matcher);
			final Iterator<String> iterator = backwards
				? matcher.iteratorDescending()
				: matcher.iteratorAscending();
			final ArrayFilesystemAll result = new ArrayFilesystemAll();
			while (iterator.hasNext()) {
				final File file = new File(object.file, iterator.next());
				final ReferenceFilesystem reference = new ReferenceFilesystem(new RecordFilesystem(file));
				result.add(reference);
			}
			return result;
		}
	}

	@Override
	public ReferenceFilesystem getRootReference() {

		return this.rootReference;
	}

	@Override
	public Value<? extends CharSequence> getText(final RecordFilesystem object) {

		assert object != null : "NULL object";
		return Base.forString((CharSequence) object.getBinaryContent().baseValue().toStringUtf8());
	}

	@Override
	public boolean isHistorySupported() {

		return false;
	}

	@Override
	public boolean isReadOnly() {

		return this.readOnly;
	}

	@Override
	public void shutdown() throws Exception {

		// do nothing
	}

	@Override
	public String toString() {

		return "[object " + this.getClass().getSimpleName() + "(" + this.root.file.getAbsolutePath() + ")]";
	}
}
