package ru.myx.ae3.bs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;

/**
 *
 * @author myx
 *
 */
public class FileStoreNameHashLeafs implements BlobStore<Path> {
	
	
	private static final int FOLDERS_NAME_RADIX = 32;
	
	private static final int FOLDERS_COUNT = 1024;
	
	private static final int FOLDERS_MASK = FileStoreNameHashLeafs.FOLDERS_COUNT - 1;
	
	private final Path root;
	
	private final Path[] folders;
	
	private static final String nameForIndex(final int index) {
		
		
		return index < FileStoreNameHashLeafs.FOLDERS_NAME_RADIX
			? "0" + Integer.toString(index, FileStoreNameHashLeafs.FOLDERS_NAME_RADIX)
			: Integer.toString(index, FileStoreNameHashLeafs.FOLDERS_NAME_RADIX);
	}
	
	/**
	 *
	 * @param path
	 */
	public FileStoreNameHashLeafs(final String path) {
		this(Paths.get(path));
	}
	
	/**
	 *
	 * @param path
	 */
	public FileStoreNameHashLeafs(final Path path) {
		this.root = path.normalize();
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Does not exist or not a directory: " + path);
		}
		
		this.folders = new Path[FileStoreNameHashLeafs.FOLDERS_COUNT];
		for (int i = FileStoreNameHashLeafs.FOLDERS_MASK; i >= 0; --i) {
			this.folders[i] = this.root.resolve(FileStoreNameHashLeafs.nameForIndex(i));
		}
	}
	
	/**
	 *
	 * @throws IOException
	 */
	public void checkNormalizeStructure() throws IOException {
		
		
		final Path leafs = this.root.resolve(".leafs");
		if (Files.isRegularFile(leafs)) {
			final List<String> lines = Files.readAllLines(leafs);
			if (lines.size() == 1) {
				if ("1024:32".equals(lines.get(0))) {
					return;
				}
			}
		}
		
		{
			Act.launch(null, () -> {
				System.out.println(this.getClass().getSimpleName() + "(" + this.root + "): normalizing...");

				final int count;

				try {
					count = this.forceNormalizeStructure();
				} catch (final Exception e) {
					System.out.println(this.getClass().getSimpleName() + "(" + this.root + "): normalization failed: " + e.getMessage());
					return;
				}

				try {
					Files.write(leafs, "1024:32".getBytes(Engine.CHARSET_ASCII));
				} catch (final Exception e) {
					System.out.println(this.getClass().getSimpleName() + "(" + this.root + "): normalization failed: " + e.getMessage());
					return;
				}

				System.out.println(this.getClass().getSimpleName() + "(" + this.root + "): normalized, " + count + " files moved.");
			});
		}
	}
	
	/**
	 *
	 * @return
	 * @throws IOException
	 */
	public int forceNormalizeStructure() throws IOException {
		
		
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(this.root)) {
			int count = 0;
			for (final Path entry : listing) {
				final String name = entry.getFileName().getName(0).toString();
				if (name.length() <= 2 || name.charAt(0) == '.') {
					continue;
				}
				if (Files.isRegularFile(entry)) {
					final Path target = this.putPrepare(name);
					Files.move(entry, target);
					++count;
				}
			}
			return count;
		}
	}
	
	/**
	 * Checks that file and it's folder exist
	 *
	 * @param name
	 * @return null or Path to an existing file
	 */
	@Override
	public Path getPrepare(final String name) {
		
		
		final int index = name.hashCode();
		final Path result = this.folders[index & FileStoreNameHashLeafs.FOLDERS_MASK].resolve(name);
		if (Files.isRegularFile(result)) {
			return result;
		}
		return null;
	}
	
	/**
	 * Checks that blob exists
	 *
	 * @param name
	 * @return null or binary
	 */
	@Override
	public TransferCopier getBinary(final String name) {
		
		
		final Path path = this.getPrepare(name);
		if (path == null) {
			return null;
		}
		
		return Transfer.createCopier(path);
	}
	
	/**
	 * Created folder as needed and returns a path for file to be stored
	 *
	 * @param name
	 * @return Path for file to be created
	 * @throws IOException
	 */
	@Override
	public Path putPrepare(final String name) throws IOException {
		
		
		final int index = name.hashCode();
		final Path folder = this.folders[index & FileStoreNameHashLeafs.FOLDERS_MASK];
		Files.createDirectories(folder);
		final Path result = folder.resolve(name);
		return result;
	}
	
	/**
	 * Created folder as needed and returns a path for file to be stored
	 *
	 * @param name
	 * @param binary
	 * @return Path for file to be created
	 * @throws IOException
	 */
	@Override
	public boolean putBinary(final String name, final TransferCopier binary) throws IOException {
		
		
		final Path target = this.putPrepare(name);
		this.writeBinary(target, binary);
		return true;
	}
	
	/**
	 * @param name
	 * @return
	 * @throws IOException
	 */
	@Override
	public boolean drop(final String name) throws IOException {
		
		
		final int index = name.hashCode();
		final Path result = this.folders[index & FileStoreNameHashLeafs.FOLDERS_MASK].resolve(name);
		return Files.deleteIfExists(result);
	}
	
	@Override
	public TransferCopier readBinary(final Path path) throws IOException {
		
		
		return Transfer.createCopier(path);
	}
	
	@Override
	public Path writeBinary(final Path path, final TransferCopier binary) throws IOException {
		
		
		if (binary.length() < Transfer.BUFFER_MAX) {
			Files.write(path, binary.nextDirectArray());
			return path;
		}
		
		final TransferBuffer buffer = binary.nextCopy();
		if (buffer.isDirectAbsolutely()) {
			Files.write(path, buffer.toDirectArray());
			return path;
		}
		
		Transfer.toStream(buffer, Files.newOutputStream(path), true);
		return path;
	}

	@Override
	public String toString() {
		
		
		return "[" + this.getClass().getSimpleName() + "(" + this.root + ")]";
	}
}
