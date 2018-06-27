/**
 * 
 */
package ru.myx.ae3.binary;

import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * 		
 */
public abstract class AbstractTransferDescription extends BaseHostEmpty implements TransferDescription {
	
	@Override
	public BaseObject basePrototype() {
		
		return TransferDescription.PROTOTYPE;
	}
	
	@Override
	public Object getAttachment() {
		
		return null;
	}
	
	@Override
	public int getNetReadByteRateLimit() {
		
		return -1;
	}
	
	@Override
	public int getNetReadByteRateLimitLeft(final long time) {
		
		return 0;
	}
	
	@Override
	public int getNetWriteByteRateLimit() {
		
		return -1;
	}
	
	@Override
	public int getNetWriteByteRateLimitLeft(final long time) {
		
		return 0;
	}
	
	@Override
	public int getPriority() {
		
		return TransferDescription.PC_DEFAULT;
	}
	
	@Override
	public int getStorageReadByteRateLimit() {
		
		return -1;
	}
	
	@Override
	public int getStorageWriteByteRateLimit() {
		
		return -1;
	}
	
	@Override
	public boolean isWritable() {
		
		return false;
	}
	
	@Override
	public Object setAttachment(final Object attachment) {
		
		return null;
	}
	
	@Override
	public int setNetReadByteRateLimit(final int netByteReadLimit) {
		
		return -1;
	}
	
	@Override
	public int setNetWriteByteRateLimit(final int netByteWriteLimit) {
		
		return -1;
	}
	
	@Override
	public int setStorageReadByteRateLimit(final int storageByteReadLimit) {
		
		return -1;
	}
	
	@Override
	public int setStorageWriteByteRateLimit(final int storageByteWriteLimit) {
		
		return -1;
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
		
		return "ABSTRACT: " + super.toString();
	}
}
