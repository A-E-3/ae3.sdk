package ru.myx.ae3.skinner;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.produce.Produce;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.Storage;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;
import ru.myx.ae3.vfs.zip.StorageImplZip;
import ru.myx.ae3.xml.Xml;
import ru.myx.io.InputStreamNoCloseFilter;

/** @author myx */
public class SkinScanner extends BaseHostEmpty implements Runnable {

	private static final String OWNER = "SKIN-SCANNER";

	private static final SkinScanner SKIN_PRIVATE;

	private static final SkinScanner SKIN_PROTECTED;

	private static final SkinScanner SKIN_PUBLIC;

	static {
		final Entry skinPrivate = Storage.PRIVATE.relative("resources/skin", TreeLinkType.PUBLIC_TREE_REFERENCE);
		final Entry skinProtected = Storage.PROTECTED.relative("resources/skin", TreeLinkType.PUBLIC_TREE_REFERENCE);
		final Entry skinPublic = Storage.PUBLIC.relative("resources/skin", TreeLinkType.PUBLIC_TREE_REFERENCE);

		SKIN_PRIVATE = new SkinScanner(skinPrivate, new TreeMap<String, Skinner>());
		SKIN_PROTECTED = new SkinScanner(skinProtected, new TreeMap<String, Skinner>());
		SKIN_PUBLIC = new SkinScanner(skinPublic, new TreeMap<String, Skinner>());

		SkinScanner.SKIN_PRIVATE.start();
		SkinScanner.SKIN_PROTECTED.start();
		SkinScanner.SKIN_PUBLIC.start();
	}

	/** @param ctx
	 * @param name
	 * @return */
	public static final Skinner getContextSkinner(final ExecProcess ctx, final String name) {

		if (ctx != null) {
			final BaseObject candidate = ctx.baseGet("$skins", BaseObject.UNDEFINED);
			if (candidate instanceof SkinScanner) {
				final Skinner skinner = ((SkinScanner) candidate).getSkinner(name);
				if (skinner != null) {
					return skinner;
				}
			}
		}
		{
			return SkinScanner.getSystemSkinner(name);
		}
	}

	private static BaseObject getSkinSettingsForFile(final Entry file) throws Throwable {

		final Entry settingsFile = file.relative("skin.settings.xml", null);
		if (settingsFile == null || !settingsFile.isExist()) {
			return null;
		}
		return Xml.toBase(
				"skin: " + file.getLocation(),
				settingsFile.toBinary().getBinaryContent().baseValue(),
				null/* Engine.CHARSET_DEFAULT */,
				null,
				null,
				null);
	}

	private static BaseObject getSkinSettingsForZip(final Entry file) throws Throwable {

		final InputStream inputStream = file.toBinary().getBinaryContent().baseValue().nextInputStream();
		try (final ZipInputStream zipFile = new ZipInputStream(inputStream)) {
			try (final InputStream filter = new InputStreamNoCloseFilter(zipFile)) {
				for (ZipEntry entry = zipFile.getNextEntry(); entry != null; entry = zipFile.getNextEntry()) {
					final String entryName = entry.getName().replace('\\', '/');
					if ("skin.settings.xml".equals(entryName)) {
						final TransferCopier copier = Transfer.createBuffer(filter).toBinary();
						return Xml.toBase("skin: " + file.getLocation(), copier, Charset.defaultCharset(), null, null, null);
					}
				}
			}
			return null;
		}
	}

	/** Searches among private (instance), protected (cluster) and public (bundled) skin sets in the
	 * order of appearance.
	 *
	 * @param name
	 * @return */
	public static final Skinner getSystemSkinner(final String name) {

		{
			final Skinner skinner = SkinScanner.SKIN_PRIVATE.getSkinner(name);
			if (skinner != null) {
				return skinner;
			}
		}
		{
			final Skinner skinner = SkinScanner.SKIN_PROTECTED.getSkinner(name);
			if (skinner != null) {
				return skinner;
			}
		}
		{
			final Skinner skinner = SkinScanner.SKIN_PUBLIC.getSkinner(name);
			if (skinner != null) {
				return skinner;
			}
		}
		{
			return null;
		}
	}

	/** @param names
	 *            - can be null
	 * @return */
	public static final Set<String> getSystemSkinnerNames(final Set<String> names) {

		SkinScanner.SKIN_PRIVATE.getSkinnerNames(names);
		SkinScanner.SKIN_PROTECTED.getSkinnerNames(names);
		SkinScanner.SKIN_PUBLIC.getSkinnerNames(names);
		return names;
	}

	private final Entry folder;

	private final Map<String, Skinner> previousSkinners;

	private final Map<String, Skinner> currentSkinners;

	private final Map<String, Skinner> target;

	private boolean started = false;

	private final Set<String> ignored = new TreeSet<>();

	/** @param folder
	 * @param target */
	public SkinScanner(final Entry folder, final Map<String, Skinner> target) {

		this.folder = folder;
		this.currentSkinners = new TreeMap<>();
		this.previousSkinners = new TreeMap<>();
		this.target = target;
	}

	private Skinner createSkinner(final String name, final boolean zip, final Entry file) {

		try {
			final BaseObject settings = zip
				? SkinScanner.getSkinSettingsForZip(file)
				: SkinScanner.getSkinSettingsForFile(file);
			if (settings == null) {
				if (this.ignored.add(name)) {
					Report.info(
							SkinScanner.OWNER,
							"No skin.settings.xml in " + (zip
								? "file "
								: "folder ") + file.getLocation() + ", skin skipped!");
				}
				return null;
			}
			// System.out.println(" >>> >>>> ZZ: " + this + ", name: " + name + ", settings: " +
			// Format.Describe.toEcmaSource(settings, ""));
			final Entry root = zip
				? Storage.createRoot(
						new StorageImplZip(
								file.getLocation(), //
								file.toBinary().getBinaryContent().baseValue(),
								file.getLastModified()))
				: file;
			final Skinner skinner = Produce.object(
					Skinner.class, //
					Base.getString(settings, "type", "HIERARCHY").trim(),
					settings,
					root);
			this.ignored.remove(name);
			Report.info(SkinScanner.OWNER, "skin created '" + name + "' " + //
					(zip
						? "zip skin, file"
						: "folder skin, folder")
					+ "=" + file //
					+ ", instance: " + skinner);
			return skinner;
		} catch (final Throwable t) {
			if (this.ignored.add(name)) {
				Report.exception(SkinScanner.OWNER, "Error creating skin, file=" + file.getLocation(), t);
			}
			return null;
		}
	}

	/**
	 *
	 */
	public void destroy() {

		if (this.started) {
			for (final String remove : this.currentSkinners.keySet()) {
				this.target.put(remove, this.previousSkinners.remove(remove));
				this.currentSkinners.remove(remove);
				Report.info(SkinScanner.OWNER, "Destroying skin scanner - skin '" + remove + "' removed");
			}
			this.started = false;
		}
	}

	/** @param name
	 * @return */
	public Skinner getSkinner(final String name) {

		return this.currentSkinners.get(name);
	}

	/** @param names
	 *            - can be null
	 * @return */
	public Set<String> getSkinnerNames(Set<String> names) {

		if (!this.currentSkinners.isEmpty()) {
			if (names == null) {
				names = new TreeSet<>();
			}
			names.addAll(this.currentSkinners.keySet());
		}
		return names;
	}

	@Override
	public final void run() {

		if (!this.started) {
			return;
		}
		this.scan();
		if (this.started) {
			Act.later(null, this, 15000L + Engine.createRandom(10000));
		}
	}

	private final void scan() {

		final Set<String> left = Create.tempSet(this.currentSkinners.keySet());
		final Set<String> invalid = Create.tempSet();
		for (final Map.Entry<String, Skinner> current : this.currentSkinners.entrySet()) {
			if (!current.getValue().scan()) {
				Report.info(SkinScanner.OWNER, "Skin '" + current.getKey() + "' invalidated in scan, will try to reload!");
				invalid.add(current.getKey());
				left.remove(current.getKey());
			}
		}
		{
			final BaseList<Entry> skinFolders = this.folder.toContainer().getContentCollection(TreeReadType.ITERABLE).baseValue();
			if (skinFolders != null) {
				for (final Entry skin : skinFolders) {
					final String key;
					final boolean zip;
					if (skin.isContainer()) {
						key = skin.getKey();
						zip = false;
					} else //
					if (skin.isBinary() && skin.getKey().toLowerCase().endsWith(".zip")) {
						key = skin.getKey().substring(0, skin.getKey().length() - 4);
						zip = true;
					} else {
						continue;
					}
					final String name = key;
					if (!left.remove(name)) {
						final Skinner skinner = this.createSkinner(name, zip, skin);
						if (skinner != null) {
							this.previousSkinners.put(name, this.target.put(name, skinner));
							this.currentSkinners.put(name, skinner);
							invalid.remove(name);
						}
					}
				}
			}
		}
		left.addAll(invalid);
		for (final String remove : left) {
			this.target.put(remove, this.previousSkinners.remove(remove));
			this.currentSkinners.remove(remove);
			Report.info(SkinScanner.OWNER, "Skin '" + remove + "' removed");
		}
	}

	/**
	 *
	 */
	public void start() {

		if (!this.started) {
			this.started = true;
			this.run();
		}
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + "(" + this.folder.getLocation() + ")";
	}
}
