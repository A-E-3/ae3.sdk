/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.extra;

import ru.myx.ae3.binary.TransferCopier;

/**
 * @author myx
 * 
 */
public interface ExternalHandler {
	
	/**
	 * @param issuer
	 * @return true when given issuer is served by this handler
	 */
	boolean checkIssuer(Object issuer);
	
	/**
	 * @param attachment
	 * @param identifier
	 * @return object
	 * @throws Exception
	 */
	External getExternal(Object attachment, String identifier) throws Exception;
	
	/**
	 * @param attachment
	 * @param identifier
	 * @return boolean
	 * @throws Exception
	 */
	boolean hasExternal(Object attachment, String identifier) throws Exception;
	
	/**
	 * @param attachment
	 * @param key
	 * @param type
	 * @param copier
	 * @return identifier
	 * @throws Exception
	 */
	String putExternal(Object attachment, String key, String type, TransferCopier copier) throws Exception;
}
