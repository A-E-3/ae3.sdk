package ru.myx.ae3.eval.collection;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;

import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.cache.Cache;
import ru.myx.ae3.cache.CacheL2;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.exec.ProgramPart;

/** @author myx */
public final class RenderCollectionMap extends RenderCollectionAbstract {

	private static final CacheL2<RenderCacheEntry> CACHE = Cache.createL2("map_collections", "Prepared Renderers From Map Collections");

	private final Map<String, TransferCopier> root;

	private final Charset charset;

	private final String cacheId;

	private Function<String, String> characterSource = null;

	private Function<String, TransferBuffer> binarySource = null;

	/** @param charset
	 * @param root
	 * @param renderer */
	public RenderCollectionMap(final Charset charset, final Map<String, TransferCopier> root, final LanguageImpl renderer) {
		
		super(renderer);
		this.charset = charset;
		this.root = root;
		this.cacheId = "RCM:" + String.valueOf(System.identityHashCode(this));
	}

	@Override
	public final Function<String, TransferBuffer> getBinarySource() {

		if (this.binarySource == null) {
			synchronized (this) {
				if (this.binarySource == null) {
					final Map<String, TransferCopier> root = this.root;
					this.binarySource = new Function<>() {

						@Override
						public TransferBuffer apply(final String argument) {

							final TransferCopier copier = root.get(argument);
							return copier == null
								? null
								: copier.nextCopy();
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
					final Map<String, TransferCopier> root = this.root;
					final Charset charset = this.charset;
					this.characterSource = new Function<>() {

						@Override
						public String apply(final String argument) {

							final TransferCopier copier = root.get(argument);
							return copier == null
								? null
								: copier.toString(charset);
						}
					};
				}
			}
		}
		return this.characterSource;
	}

	@Override
	public final ProgramPart prepare(final String name) throws Throwable {

		final RenderCacheEntry cached = RenderCollectionMap.CACHE.get(this.cacheId, name);
		if (cached != null) {
			return cached.renderer;
		}
		final Function<String, String> characterSource = this.getCharacterSource();
		final String source = characterSource.apply(name);
		if (source != null) {
			final ProgramPart prepared = Evaluate.compileProgram(this.languageMapper.getLanguage(name), "collection/" + name, characterSource, name);
			RenderCollectionMap.CACHE.put(this.cacheId, name, new RenderCacheEntry(prepared, 0L), 1000L * 60L * 60L);
			return prepared;
		}
		return null;
	}

	@Override
	public String toString() {

		return "[object " + this.baseClass() + "(" + "identityHashCode:" + System.identityHashCode(this) + ")]";
	}
}
