package ru.myx.ae3.eval.collection;

import java.nio.charset.Charset;

import java.util.function.Function;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.cache.Cache;
import ru.myx.ae3.cache.CacheL2;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 
 */
public final class RenderCollectionVfs extends RenderCollectionAbstract {

	private static final CacheL2<RenderCacheEntry> cache = Cache.createL2("vfs_collections", "Prepared Renderers From File Collections");

	private final Entry root;

	private final String cacheId;

	private final Charset charset;

	private final String extensionSuffix;

	private Function<String, String> characterSource = null;

	private Function<String, TransferBuffer> binarySource = null;

	/**
	 * @param charset
	 * @param root
	 * @param renderer
	 * @param extensionSuffix
	 */
	public RenderCollectionVfs(final Charset charset, final Entry root, final LanguageImpl renderer, final String extensionSuffix) {
		super(renderer);
		this.root = root;
		this.charset = charset;
		this.extensionSuffix = extensionSuffix == null || extensionSuffix.length() == 0
			? null
			: extensionSuffix;
		this.cacheId = "RCV:" + String.valueOf(System.identityHashCode(this));
	}

	@Override
	protected void finalize() throws Throwable {

		RenderCollectionVfs.cache.remove(this.cacheId);
		super.finalize();
	}

	@Override
	public final Function<String, TransferBuffer> getBinarySource() {

		if (this.binarySource == null) {
			synchronized (this) {
				if (this.binarySource == null) {
					final Entry root = this.root;
					// final String extensionSuffix = this.extensionSuffix;
					this.binarySource = new Function<String, TransferBuffer>() {

						@Override
						public TransferBuffer apply(final String argument) {

							final Entry file = root.relative(
									argument,
									// extensionSuffix == null
									// ? argument
									// : argument + extensionSuffix,
									null);
							return file != null && file.isExist() && file.isBinary()
								? file.toBinary().getBinaryContent().baseValue().nextCopy()
								: null;
						}

						@Override
						public String toString() {

							return "binary(" + RenderCollectionVfs.this.toString() + ")";
						}
					};
				}
			}
		}
		return this.binarySource;
	}

	@Override
	public final Function<String, String> getCharacterSource() {

		if (this.characterSource == null) {
			synchronized (this) {
				if (this.characterSource == null) {
					final Entry root = this.root;
					// final String extensionSuffix = this.extensionSuffix;
					final Charset charset = this.charset;
					this.characterSource = new Function<String, String>() {

						@Override
						public String apply(final String argument) throws Transfer.TransferOperationException {

							final Entry file = root.relative(
									argument,
									// extensionSuffix == null
									// ? argument
									// : argument + extensionSuffix,
									null);
							return file != null && file.isExist() && file.isBinary()
								? file.toBinary().getBinaryContent().baseValue().toString(charset)
								: null;
						}

						@Override
						public String toString() {

							return "character(" + RenderCollectionVfs.this.toString() + ")";
						}
					};
				}
			}
		}
		return this.characterSource;
	}

	@Override
	public final ProgramPart prepare(final String argument) throws Throwable {

		final String extensionSuffix = this.extensionSuffix;
		final String name = extensionSuffix == null
			? argument
			: argument + extensionSuffix;
		final RenderCacheEntry cached = RenderCollectionVfs.cache.get(this.cacheId, name);
		final Entry file = this.root.relative(name, null);
		if (file != null && file.isExist() && file.isBinary()) {
			final long modified = file.getLastModified();
			if (cached != null && cached.modified == modified) {
				return cached.renderer;
			}
			final ProgramPart prepared = Evaluate.compileProgram(
					this.languageMapper.getLanguage(name), //
					this + "/" + name,
					this.getCharacterSource(),
					name);
			RenderCollectionVfs.cache.put(
					this.cacheId, //
					name,
					new RenderCacheEntry(prepared, modified),
					1000L * 60L * 60L);
			return prepared;
		}
		if (cached != null && cached.modified == 0L) {
			return cached.renderer;
		}
		return null;
	}

	@Override
	public String toString() {

		return "[object " + this.baseClass() + "(" + "identityHashCode:" + System.identityHashCode(this) + ", location:" + this.root.getLocation() + ")]";
	}
}
