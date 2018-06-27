package ru.myx.ae3.l2;

import java.net.URI;
import java.net.URISyntaxException;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.i3.TargetInterface;
import ru.myx.ae3.l2.skin.Skin;
import ru.myx.ae3.skinner.Skinner;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.Storage;

/**
 * @author myx
 *
 * @param <L>
 *            layout
 */
public interface TargetContext<L> {

	/**
	 * @param message
	 * @return true (for assertions)
	 */
	boolean dump(String message);

	/**
	 * @return
	 */
	ExecProcess getContext();

	/**
	 * @return
	 */
	TargetInterface getInterface();

	/**
	 * @return
	 */
	Skin getSkin();

	/**
	 * @return
	 */
	Skinner getSkinner();

	/**
	 * TODO: you need to re-implement it to actually keep binaries accessible
	 *
	 * @param relativeName
	 * @param binary
	 * @return
	 */
	default URI registerBinary(final String relativeName, final TransferCopier binary) {

		try {
			return new URI(relativeName);
		} catch (final URISyntaxException e) {
			throw new RuntimeException();
		}

	}

	/**
	 * @param entry
	 * @return
	 */
	default URI registerEntry(final Entry entry) {

		return this.registerBinary(entry.getLocation(), entry.toBinary().getBinaryContent().baseValue());
	}

	/**
	 * @param resolve
	 * @return
	 */
	default Value<? extends TransferCopier> resolveToBinary(final String resolve) {
		
		if (resolve.startsWith("absolute:")) {
			final String path = resolve.substring("absolute:".length());
			final Entry entry = Storage.getAbsolute(path, null);
			if (entry == null || !entry.isExist()) {
				return Base.forNull();
			}
			if (entry.isFile()) {
				return entry.toBinary().getBinaryContent();
			}
		}
		return Base.forNull();
	}

	/**
	 * @param resolve
	 * @return
	 * @throws Exception
	 */
	default URI resolveToHref(final String resolve) throws Exception {

		if (resolve.startsWith("absolute:")) {
			final String path = resolve.substring("absolute:".length());
			final Entry entry = Storage.getAbsolute(path, null);
			if (entry == null || !entry.isExist()) {
				return null;
			}
			if (entry.isFile()) {
				return new URI("/!/" + path);
				// return new URI( "/$a/" + path );
			}
		}
		return null;
	}

	/**
	 * @param resolve
	 * @param silent
	 * @return
	 */
	default BaseObject resolveToObject(final String resolve) {

		if (resolve.startsWith("absolute:")) {
			final String path = resolve.substring("absolute:".length());
			final Entry entry = Storage.getAbsolute(path, null);
			if (entry == null || !entry.isExist()) {
				return Base.forString("ERROR: doesn't exist: " + resolve);
			}
			if (entry.isBinary()) {
				return LayoutEngine.parseJSLD(entry.toCharacter().getText().toString());
			}
		}
		return Base.forString("ERROR: can't resolve: " + resolve);
	}

	/**
	 *
	 * @param layout
	 * @return
	 */
	BaseObject transformLayout(BaseObject layout);

	/**
	 * @param layout
	 * @return
	 */
	Value<Void> transform(L layout);
}
