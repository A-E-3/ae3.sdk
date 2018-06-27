package ru.myx.ae3.bs;

import ru.myx.ae3.binary.TransferCopier;

/**
 *
 * @author myx
 * @param <Descriptor>
 *
 */
public interface BlobStore<Descriptor extends Object> {
	
	
	/**
	 *
	 * @param name
	 * @return
	 */
	Descriptor getPrepare(final String name);
	
	/**
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	Descriptor putPrepare(final String name) throws Exception;
	
	/**
	 *
	 * @param name
	 * @param binary
	 * @return
	 * @throws Exception
	 */
	default boolean putBinary(final String name, final TransferCopier binary) throws Exception {
		
		final Descriptor d = this.putPrepare(name);
		if (d == null) {
			return false;
		}
		return this.writeBinary(d, binary) != null;
	}
	
	/**
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	boolean drop(final String name) throws Exception;
	
	/**
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	default TransferCopier getBinary(final String name) throws Exception {
		
		
		final Descriptor d = this.getPrepare(name);
		if (d == null) {
			return null;
		}
		return this.readBinary(d);
	}

	/**
	 *
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	TransferCopier readBinary(final Descriptor desc) throws Exception;

	/**
	 *
	 * @param desc
	 * @param binary
	 * @return
	 * @throws Exception
	 */
	Descriptor writeBinary(final Descriptor desc, final TransferCopier binary) throws Exception;
}
