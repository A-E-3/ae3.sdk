package ru.myx.ae3.eval.collection;

import java.io.File;
import java.nio.charset.Charset;

import java.util.function.Function;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.cache.Cache;
import ru.myx.ae3.cache.CacheL2;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.exec.ProgramPart;

/**
 * @author myx
 *
 */
public final class RenderCollectionFilesystem extends RenderCollectionAbstract {
	
	private static final CacheL2<RenderCacheEntry> cache = Cache.createL2("file_collections", "Prepared Renderers From File Collections");
	
	private final File root;
	
	private final String cacheId;
	
	private final Charset charset;
	
	private Function<String, String> characterSource = null;
	
	private Function<String, TransferBuffer> binarySource = null;
	
	/**
	 * @param charset
	 * @param root
	 * @param language
	 */
	public RenderCollectionFilesystem(final Charset charset, final File root, final LanguageImpl language) {
		super(language);
		this.charset = charset;
		this.root = root.getAbsoluteFile();
		this.cacheId = "RCF:" + String.valueOf(System.identityHashCode(this));
	}
	
	@Override
	protected void finalize() throws Throwable {
		
		RenderCollectionFilesystem.cache.remove(this.cacheId);
		super.finalize();
	}
	
	@Override
	public final Function<String, TransferBuffer> getBinarySource() {
		
		if (this.binarySource == null) {
			synchronized (this) {
				if (this.binarySource == null) {
					final File root = this.root;
					this.binarySource = argument -> {
						
						final File file = new File(root, argument);
						return file.exists()
							? Transfer.createBuffer(file)
							: null;
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
					final File root = this.root;
					final Charset charset = this.charset;
					this.characterSource = argument -> {
						final File file = new File(root, argument);
						return file.exists()
							? Transfer.createBuffer(file).toString(charset)
							: null;
					};
				}
			}
		}
		return this.characterSource;
	}
	
	@Override
	public final ProgramPart prepare(final String name) throws Throwable {
		
		final RenderCacheEntry cached = RenderCollectionFilesystem.cache.get(this.cacheId, name);
		final File file = new File(this.root, name);
		if (file.exists()) {
			final long modified = file.lastModified();
			if (cached != null && cached.modified == modified) {
				return cached.renderer;
			}
			final ProgramPart prepared = Evaluate.compileProgram(this.languageMapper.getLanguage(name), "collection/" + name, this.getCharacterSource(), name);
			RenderCollectionFilesystem.cache.put(this.cacheId, name, new RenderCacheEntry(prepared, modified), 1000L * 60L * 60L);
			return prepared;
		}
		if (cached != null && cached.modified == 0L) {
			return cached.renderer;
		}
		return null;
	}
	
	@Override
	public String toString() {
		
		return "[object " + this.baseClass() + "(" + "identityHashCode:" + System.identityHashCode(this) + ")]";
	}
}
