package ru.myx.ae3.vfs.resources;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.ars.ArsRecord;
import ru.myx.io.DataInputByteArrayFast;
import ru.myx.io.DataInputByteArrayReusable;
import ru.myx.io.OutputStreamCounter;

final class RecordResources implements Value<RecordResources>, ArsRecord {
	Class<?>		anchor;
	
	TransferCopier	content;
	
	String			key;
	
	URL				url;
	
	RecordResources(final Class<?> anchor, final String key) {
		assert anchor != null : "NULL value";
		this.anchor = anchor;
		this.key = key;
		this.url = key == null || key.length() == 0
				? null
				: anchor.getResource( key );
	}
	
	@Override
	public RecordResources baseValue() {
		return this;
	}
	
	Value<TransferCopier> getBinaryContent() {
		try {
			return this.url == null
					? null
					: this.content == null
							? this.content = Transfer.createCopier( this.url.openStream() )
							: this.content;
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	@Override
	public long getBinaryContentLength() {
		if (this.url == null) {
			return 0;
		}
		if (this.content != null) {
			return this.content.length();
		}
		try {
			/**
			 * Try check for known types of streams not to read contents.
			 */
			@SuppressWarnings("resource")
			final InputStream input = this.url.openStream();
			if (input instanceof ByteArrayInputStream
					|| input instanceof DataInputByteArrayFast
					|| input instanceof DataInputByteArrayReusable) {
				try {
					return input.available();
				} finally {
					input.close();
				}
			}
			final OutputStreamCounter counter = new OutputStreamCounter();
			Transfer.toStream( input, counter, true );
			return counter.getTotal();
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	@Override
	public String getKeyString() {
		return this.key;
	}
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public Guid getPrimitiveGuid() {
		return null;
	}
	
	@Override
	public Object getPrimitiveValue() {
		return null;
	}
	
	@Override
	public boolean isBinary() {
		return this.url != null && !this.isContainer();
	}
	
	@Override
	public boolean isCharacter() {
		return false;
	}
	
	@Override
	public boolean isContainer() {
		if (this.url == null) {
			return true;
		}
		try {
			final InputStream stream = this.url.openStream();
			if (stream == null) {
				return true;
			}
			final TransferBuffer buffer;
			try {
				buffer = Transfer.createBuffer( stream );
			} catch (final NullPointerException e) {
				/**
				 * this happens with JarURLConnection, folder listings are not
				 * supported and produce unchecked error:
				 * <p>
				 * <code>
					Caused by: java.lang.NullPointerException
						at java.io.FilterInputStream.close(Unknown Source)
						at sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream.close(Unknown Source)
						at ru.myx.ae3.binary.Transfer.createBuffer(Transfer.java:226)
						at ru.myx.ae3.vfs.resources.RecordResources.isContainer(RecordResources.java:100)
						... 25 more
				 * </code>
				 */
				final URL url = this.anchor.getResource( this.key + (this.key.endsWith( "/" )
						? "!file-list.txt"
						: "/!file-list.txt") );
				if (url == null) {
					return false;
				}
				try {
					try (final InputStream childStream = url.openStream()) {
						return childStream != null;
					}
				} catch (final FileNotFoundException ee) {
					return false;
				}
			}
			final String check = (buffer.remaining() > 1024
					? buffer.toSubBuffer( 0, 1024 )
					: buffer).toString( Engine.ENCODING_UTF8 );
			final int pos = check.indexOf( '\n' );
			if (pos == -1) {
				return false;
			}
			final String key = check.substring( 0, pos );
			if (key.length() == 0 || key.indexOf( '/' ) != -1) {
				return false;
			}
			final URL url = this.anchor.getResource( this.key + '/' + key );
			if (url == null) {
				return false;
			}
			try {
				try (final InputStream childStream = url.openStream()) {
					return childStream != null;
				}
			} catch (final FileNotFoundException e) {
				return false;
			}
		} catch (final NullPointerException e) {
			throw new RuntimeException( "Null pointer exception: url=" + this.url, e );
		} catch (final IOException e) {
			Report.exception( "RESFS-RECORD",
					"IO exception accessing data while checking for container, will return true",
					e );
			this.url = null;
		}
		return this.url == null;
	}
	
	@Override
	public boolean isPrimitive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "RESFSREC{" + this.key + "}";
	}
}
