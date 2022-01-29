/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.xml.Xml;

/** @author myx */
public final class ExtraXml extends BasicExtra {

	private final TransferCopier copier;

	private final ExternalHandler handler;

	private final Object attachment;

	private SoftReference<BaseObject> referenceMap = null;

	private SoftReference<String> referenceString = null;

	/** @param issuer
	 * @param recId
	 * @param recDate
	 * @param copier
	 * @param handler
	 * @param attachment */
	public ExtraXml(final Object issuer, final String recId, final long recDate, final TransferCopier copier, final ExternalHandler handler, final Object attachment) {

		super(issuer, recId, recDate);
		this.copier = copier;
		this.handler = handler;
		this.attachment = attachment;
	}

	@Override
	public final Object baseValue() {

		BaseObject stored = this.referenceMap == null
			? null
			: this.referenceMap.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceMap == null
					? null
					: this.referenceMap.get();
				if (stored == null) {
					final String storedString = this.referenceString == null
						? null
						: this.referenceString.get();
					if (storedString != null) {
						stored = Xml.toBase(this.name(), storedString, null, this.handler, this.attachment);
					} else {
						try {
							stored = Xml.toBase(this.name(), this.copier, StandardCharsets.UTF_8, null, this.handler, this.attachment);
						} catch (final RuntimeException e) {
							try {
								this.copier.nextCopy();
								/** ConcurrentModificationException or IllegalArgumentException */
							} catch (final RuntimeException cmeOrIae) {
								External external = null;
								try {
									external = this.handler.getExternal(this.attachment, this.getIdentity());
								} catch (final Throwable t) {
									Report.exception("EXTRA", "Exception while trying to reproduce new reference", this.name(), t);
								}
								if (external == this) {
									throw new IllegalStateException(this.name() + ": is invalid!");
								}
								this.copier.nextCopy();
								stored = Xml.toBase(this.name(), this.copier, StandardCharsets.UTF_8, null, this.handler, this.attachment);
								Report.warning("EXTRA", "Exception while trying to produce an object, recovery completed");
							}
							if (stored == null) {
								throw e;
							}
						}
						if (stored == null) {
							throw new IllegalStateException(this.name() + ": underlying local file is deleted and cannot be reproduced");
						}
					}
					this.referenceMap = new SoftReference<>(stored);
				}
			}
		}
		return stored;
	}

	private final String name() {

		return "extraXml(id=" + this.getIdentity() + ", handler=" + this.handler + ", issuer=" + this.getRecordIssuer() + ")";
	}

	@Override
	public final Object toBinary() {

		return this.copier;
	}

	@Override
	public final String toString() {

		String stored = this.referenceString == null
			? null
			: this.referenceString.get();
		if (stored == null) {
			synchronized (this) {
				stored = this.referenceString == null
					? null
					: this.referenceString.get();
				if (stored == null) {
					final String string = this.copier.toStringUtf8();
					assert string != null : "NULL string";
					this.referenceString = new SoftReference<>(string);
					return string;
				}
			}
		}
		return stored;
	}
}
