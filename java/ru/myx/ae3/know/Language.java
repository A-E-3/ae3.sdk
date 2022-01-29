/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.know;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseSealed;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.report.Report;

/** @author myx */
public final class Language extends BaseHostMap {

	private final static BaseObject REAL_ALL;

	/**
	 *
	 */
	public final static BaseObject ALL;

	private static final Map<String, Language> LANGUAGES = new HashMap<>();

	private static int ORDINAL = 0;

	/** Default language */
	public static final Language en;

	static {
		// main

		en = new Language(Language.ORDINAL++, "en", "ISO-8859-1", "ISO-8859-1", "ENG", "English", "English", "US", "WIN");

		// maps
		REAL_ALL = new BaseNativeObject(Language.en.name, Language.en);
		ALL = new BaseSealed(Language.REAL_ALL);

		// read

		Language.readLanguages(new File(new File(new File(Engine.PATH_PUBLIC, "resources"), "data"), "languages.txt"));

	}

	/** @return */
	public static Collection<Language> getAllLanguages() {

		return Language.LANGUAGES.values();
	}

	/** 'en' is default
	 *
	 * @param language
	 * @return language */
	public static final Language getLanguage(final String language) {

		return Language.getLanguage(language, Language.en);
	}

	/** @param language
	 * @param defaultLanguage
	 * @return language */
	public static final Language getLanguage(final String language, final Language defaultLanguage) {

		if (language.length() != 2) {
			return defaultLanguage;
		}
		final Language result = Language.LANGUAGES.get(language);
		return result == null
			? defaultLanguage
			: result;
	}

	/** @return */
	public static int ordinalMax() {

		return Language.ORDINAL;
	}

	private static final void readLanguages(final File file) {

		if (!file.exists()) {
			return;
		}
		final String source = Transfer.createBuffer(file).toString(StandardCharsets.UTF_8);
		for (final StringTokenizer lines = new StringTokenizer(source, "\r\n"); lines.hasMoreTokens();) {
			final String line = lines.nextToken().trim();
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}
			final LinkedList<String> tokens = new LinkedList<>();
			boolean quoted = false;
			for (final StringTokenizer columns = new StringTokenizer(line); columns.hasMoreTokens();) {
				final String column = columns.nextToken();
				if (column.length() > 0) {
					if (quoted) {
						if (column.endsWith("\"")) {
							quoted = false;
							tokens.set(tokens.size() - 1, tokens.getLast() + " " + column.substring(0, column.length() - 1));
						} else {
							tokens.set(tokens.size() - 1, tokens.getLast() + " " + column);
						}
					} else {
						if (column.startsWith("\"")) {
							if (column.endsWith("\"")) {
								if (column.length() <= 2) {
									continue;
								}
								tokens.add(column.substring(1, column.length() - 1));
							} else {
								quoted = true;
								tokens.add(column.substring(1));
							}
						} else {
							tokens.add(column);
						}
					}
				}
			}
			if (tokens.isEmpty()) {
				continue;
			}
			final String operation = tokens.removeFirst();
			if ("define".equals(operation)) {
				if (tokens.isEmpty()) {
					continue;
				}
				if (tokens.size() != 8) {
					Report.createReceiver(null)
							.event("LANGUAGE", "WARNING", "Wrong column count for 'define' operation, got " + tokens.size() + " instead of 8, contents: " + tokens);
					continue;
				}
				final String name = tokens.removeFirst();
				final String webEncoding = tokens.removeFirst();
				final String javaEncoding = tokens.removeFirst();
				final String shortName = tokens.removeFirst();
				final String commonName = tokens.removeFirst();
				final String nativeName = tokens.removeFirst();
				final String country = tokens.removeFirst();
				final String variant = tokens.removeFirst();
				final Language registered = Language.LANGUAGES.get(name);
				if (registered == null) {
					final Language language = new Language(Language.ORDINAL++, name, webEncoding, javaEncoding, shortName, commonName, nativeName, country, variant);
					Language.LANGUAGES.put(name, language);
					Language.REAL_ALL.baseDefine(name, language);
				} else {
					registered.webEncoding = webEncoding;
					registered.javaEncoding = javaEncoding;
					registered.shortName = shortName;
					registered.commonName = commonName;
					registered.nativeName = nativeName;
					registered.country = country;
					registered.variant = variant;
					registered.locale = new Locale(name, country, variant);
				}
				continue;
			}
			if ("alias".equals(operation)) {
				if (tokens.isEmpty()) {
					continue;
				}
				final String realType = tokens.removeFirst();
				if (tokens.isEmpty()) {
					continue;
				}
				final String alias = tokens.removeFirst();
				if (Language.LANGUAGES.containsKey(realType)) {
					final Language language = Language.LANGUAGES.get(realType);
					if (!realType.equals(alias)) {
						Language.LANGUAGES.put(alias, language);
						Language.REAL_ALL.baseDefine(alias, language);
					}
				} else {
					Report.createReceiver(".default")
							.event("LANGUAGE", "WARNING", "Main language is unknown while trying to register alias type, main=" + realType + ", alias=" + alias + " - skipping!");
				}
				continue;
			}
		}
	}

	final int ordinal;

	final String name;

	String webEncoding;

	String javaEncoding;

	String shortName;

	String commonName;

	String nativeName;

	String country;

	String variant;

	Locale locale;

	Language(
			final int ordinal,
			final String name,
			final String webEncoding,
			final String javaEncoding,
			final String shortName,
			final String commonName,
			final String nativeName,
			final String country,
			final String variant) {

		this.ordinal = ordinal;
		this.name = name;
		this.webEncoding = webEncoding;
		this.javaEncoding = javaEncoding;
		this.shortName = shortName;
		this.commonName = commonName;
		this.nativeName = nativeName;
		this.country = country;
		this.variant = variant;
		this.locale = new Locale(name, country, variant);
		this.baseDefine("name", Base.forString(name));
		this.baseDefine("webEncoding", Base.forString(webEncoding));
		this.baseDefine("commonEncoding", Base.forString(webEncoding));
		this.baseDefine("countryCode", Base.forString(country));
		this.baseDefine("javaEncoding", Base.forString(javaEncoding));
		this.baseDefine("short", Base.forString(shortName));
		this.baseDefine("commonName", Base.forString(commonName));
		this.baseDefine("nativeName", Base.forString(nativeName));
		this.baseDefine("shortName", Base.forString(shortName));
		this.baseDefine("originalName", Base.forString(nativeName));
	}

	/** @return string */
	public String getCommonEncoding() {

		return this.webEncoding;
	}

	/** @return string */
	public String getCommonName() {

		return this.commonName;
	}

	/** @return */
	public String getCountryCode() {

		return this.country;
	}

	/** @return string */
	public String getJavaEncoding() {

		return this.javaEncoding;
	}

	/** @return locale */
	public Locale getLocale() {

		return this.locale;
	}

	/** @return string */
	public String getName() {

		return this.name;
	}

	/** @return string */
	public String getNativeName() {

		return this.nativeName;
	}

	/** @return string */
	public String getOriginalName() {

		return this.nativeName;
	}

	/** @return string */
	public String getShortName() {

		return this.shortName;
	}

	/** @return */
	public int ordinal() {

		return this.ordinal;
	}

	@Override
	public String toString() {

		return "[object " + this.baseClass() + "(" + this.toStringDetails() + ")]";
	}

	private String toStringDetails() {

		return "name=" + this.name + ", ce=" + this.webEncoding + ", je=" + this.javaEncoding + ", sn=" + this.shortName + ", cn=" + this.commonName + ", on=" + this.nativeName;
	}
}
