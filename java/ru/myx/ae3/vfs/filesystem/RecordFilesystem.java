package ru.myx.ae3.vfs.filesystem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsRecord;

class RecordFilesystem implements Value<RecordFilesystem>, ArsRecord {
	File		file;
	
	RecordType	type;
	
	/**
	 * Container template
	 */
	RecordFilesystem() {
		this.file = null;
	}
	
	RecordFilesystem(final File file) {
		assert file != null : "NULL value";
		this.file = file;
	}
	
	@Override
	public RecordFilesystem baseValue() {
		return this;
	}
	
	Value<TransferCopier> getBinaryContent() {
		return Transfer.createCopier( this.file, 0, this.file.length() );
	}
	
	@Override
	public long getBinaryContentLength() {
		return this.file.length();
	}
	
	@Override
	public String getKeyString() {
		return this.file.getName();
	}
	
	@Override
	public BaseObject getPrimitiveBaseValue() {
		final Guid guid = this.getPrimitiveGuid();
		return guid == null
				? null
				: guid.getInlineBaseValue();
	}
	
	@Override
	public Guid getPrimitiveGuid() {
		if (!this.file.exists() || !this.file.isFile()) {
			return null;
		}
		final long length = this.file.length();
		if (length < 8 || length > 8 + Guid.MAX_LENGTH) {
			return null;
		}
		final byte[] bytes = new byte[(int) length];
		try {
			try (final RandomAccessFile input = new RandomAccessFile( this.file, "r" )) {
				input.readFully( bytes );
			}
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
		if (bytes[0] != '#' && Guid.readGuidByteCount( bytes, 8 ) != length - 8) {
			return null;
		}
		return Guid.readGuid( bytes, 8 );
	}
	
	@Override
	public Object getPrimitiveValue() {
		final Guid guid = this.getPrimitiveGuid();
		return guid == null
				? null
				: guid.getInlineValue();
	}
	
	@Override
	public boolean isBinary() {
		return this.file.isFile();
	}
	
	@Override
	public boolean isCharacter() {
		return false;
	}
	
	@Override
	public boolean isContainer() {
		return this.file.isDirectory();
	}
	
	@Override
	public boolean isPrimitive() {
		if (!this.file.exists() || !this.file.isFile()) {
			return false;
		}
		final long length = this.file.length();
		if (length < 8 || length > 8 + Guid.MAX_LENGTH) {
			return false;
		}
		final byte[] bytes = new byte[(int) length];
		try {
			try (final RandomAccessFile input = new RandomAccessFile( this.file, "r" )) {
				input.readFully( bytes );
			}
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
		return !(bytes[0] != '#' && Guid.readGuidByteCount( bytes, 8 ) != length - 8);
	}
	
	@Override
	public String toString() {
		return "FS(" + this.file + ")";
	}
}
