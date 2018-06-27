package ru.myx.ae3.vfs;

import ru.myx.ae3.know.Guid;

final class BufferTaskUnlink extends BufferTask {
	final Entry	container;
	
	final Guid	key;
	
	BufferTaskUnlink(final Entry container, final Guid key) {
		this.container = container;
		this.key = key;
	}
	
	@Override
	void execute(final VfsInterface iface) {
		iface.doUnlink( this.container, this.key );
	}
	
}
