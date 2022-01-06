package ru.myx.ae3.vfs.union;

import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.vfs.TreeLinkType;

class RecordUnionReferenceTemplate extends RecordReferenceUnion {

	final RecordReferenceUnion key;

	final TreeLinkType mode;

	final RecordReferenceUnion original;

	/** Container template
	 *
	 * @param original
	 * @param mode
	 * @param key */
	RecordUnionReferenceTemplate(final RecordReferenceUnion key, final TreeLinkType mode, final RecordReferenceUnion original) {

		super();
		this.key = key;
		this.mode = mode;
		this.original = original;
	}

	@Override
	public Value<? extends TransferCopier> getBinaryContent() {

		return this.original.getBinaryContent();
	}

	@Override
	public long getBinaryContentLength() {

		return this.original.getBinaryContentLength();
	}

	@Override
	public RecordReferenceUnion getKey() {

		return this.key;
	}

	@Override
	public String getKeyString() {

		return this.key.getKeyString();
	}

	@Override
	public long getLastModified() {

		return this.original.getLastModified();
	}

	@Override
	public TreeLinkType getLinkageMode() {

		return this.mode;
	}

	@Override
	public Object getPrimitiveValue() {

		return this.original.getPrimitiveValue();
	}

	@Override
	public RecordReferenceUnion getSource() {

		return this.original.getSource();
	}

	@Override
	public RecordReferenceUnion getTarget() {

		return this.original.getTarget();
	}

	@Override
	public boolean isBinary() {

		return this.original.isBinary();
	}

	@Override
	public boolean isContainer() {

		return this.original.isContainer();
	}

	@Override
	public boolean isExist() {

		return this.original.isExist();
	}

	@Override
	public boolean isPrimitive() {

		return this.original.isPrimitive();
	}
}
