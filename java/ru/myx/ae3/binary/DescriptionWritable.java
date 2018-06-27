/*
 * Created on 21.03.2006
 */
package ru.myx.ae3.binary;

import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;

final class DescriptionWritable extends BaseHostEmpty implements TransferDescription {
	
	private final String name;
	
	final int priority;
	
	private int netByteWriteLimit = -1;
	
	private int netReadByteLimit = -1;
	
	int storageReadByteRateLimit = -1;
	
	int storageWriteByteRateLimit = -1;
	
	private Object object;
	
	DescriptionWritable(final String name, final int priority) {
		this.name = name;
		this.priority = priority;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		return TransferDescription.PROTOTYPE;
	}
	
	@Override
	public Object getAttachment() {
		
		return this.object;
	}
	
	@Override
	public int getNetReadByteRateLimit() {
		
		return this.netReadByteLimit;
	}
	
	@Override
	public int getNetReadByteRateLimitLeft(final long time) {
		
		return 0;
	}
	
	@Override
	public int getNetWriteByteRateLimit() {
		
		return this.netByteWriteLimit;
	}
	
	@Override
	public int getNetWriteByteRateLimitLeft(final long time) {
		
		return 0;
	}
	
	@Override
	public int getPriority() {
		
		return this.priority;
	}
	
	@Override
	public int getStorageReadByteRateLimit() {
		
		return this.storageReadByteRateLimit;
	}
	
	@Override
	public int getStorageWriteByteRateLimit() {
		
		return this.storageWriteByteRateLimit;
	}
	
	@Override
	public boolean isReplaceable(final TransferDescription description) {
		
		if (description == null) {
			return true;
		}
		if (this.getPriority() < description.getPriority()) {
			return false;
		}
		{
			final int limit = this.getNetWriteByteRateLimit();
			if (limit >= 0) {
				final int check = description.getNetWriteByteRateLimit();
				if (check < 0) {
					return false;
				}
				if (check > limit) {
					return false;
				}
			}
		}
		{
			final int limit = this.getNetReadByteRateLimit();
			if (limit >= 0) {
				final int check = description.getNetReadByteRateLimit();
				if (check < 0) {
					return false;
				}
				if (check > limit) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean isWritable() {
		
		return true;
	}
	
	@Override
	public Object setAttachment(final Object attachment) {
		
		try {
			return this.object;
		} finally {
			this.object = attachment;
		}
	}
	
	@Override
	public int setNetReadByteRateLimit(final int netByteReadLimit) {
		
		try {
			return this.netReadByteLimit;
		} finally {
			this.netReadByteLimit = netByteReadLimit;
		}
	}
	
	@Override
	public int setNetWriteByteRateLimit(final int netByteWriteLimit) {
		
		try {
			return this.netByteWriteLimit;
		} finally {
			this.netByteWriteLimit = netByteWriteLimit;
		}
	}
	
	@Override
	public int setStorageReadByteRateLimit(final int storageReadByteRateLimit) {
		
		try {
			return this.storageReadByteRateLimit;
		} finally {
			this.storageReadByteRateLimit = storageReadByteRateLimit;
		}
	}
	
	@Override
	public int setStorageWriteByteRateLimit(final int storageWriteByteRateLimit) {
		
		try {
			return this.storageWriteByteRateLimit;
		} finally {
			this.storageWriteByteRateLimit = storageWriteByteRateLimit;
		}
	}
	
	@Override
	public void statsNetRead(final int bytes) {
		
		// empty
	}
	
	@Override
	public void statsNetWritten(final int bytes) {
		
		// empty
	}
	
	@Override
	public void statsStorageRead(final int bytes) {
		
		// empty
	}
	
	@Override
	public void statsStorageWritten(final int bytes) {
		
		// empty
	}
	
	@Override
	public String toString() {
		
		return "TD{" + this.name + "}";
	}
}
