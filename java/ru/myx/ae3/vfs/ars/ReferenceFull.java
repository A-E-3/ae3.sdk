package ru.myx.ae3.vfs.ars;

import ru.myx.ae3.vfs.TreeLinkType;

final class ReferenceFull<O extends ArsRecord, R extends ArsReference<O>> implements ArsReference<O> {

	O key;
	
	TreeLinkType mode;
	
	R target;
	
	ReferenceFull(final O key, final TreeLinkType mode, final R target) {

		this.key = key;
		this.mode = mode;
		this.target = target;
	}
	
	@Override
	public O getKey() {

		return this.key;
	}
	
	@Override
	public String getKeyString() {

		return this.key.getKeyString();
	}
	
	@Override
	public long getLastModified() {

		return this.target.getLastModified();
	}
	
	@Override
	public TreeLinkType getLinkageMode() {

		return this.mode;
	}
	
	@Override
	public O getSource() {

		return this.target.getSource();
	}
	
	@Override
	public O getTarget() {

		return this.target.getTarget();
	}
	
	@Override
	public boolean isExist() {

		return null != this.target && this.target.isExist();
	}
}
