package ru.myx.ae3.vfs.filesystem;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;

class RecordFilesystemPrimitiveTemplate extends RecordFilesystem {

	Guid guid;

	/** Container template */
	RecordFilesystemPrimitiveTemplate(final Guid guid) {

		super();
		assert guid != null : "NULL value";
		assert guid.isInline() : "inline value expected as a primitive!";
		this.guid = guid;
	}

	@Override
	public Value<TransferCopier> getBinaryContent() {

		return null;
	}

	@Override
	public long getBinaryContentLength() {

		return 0;
	}

	@Override
	public Object getPrimitiveValue() {

		return this.guid.getInlineValue();
	}

	@Override
	public boolean isBinary() {

		return false;
	}

	@Override
	public boolean isContainer() {

		return false;
	}

	@Override
	public boolean isPrimitive() {

		return true;
	}
}
