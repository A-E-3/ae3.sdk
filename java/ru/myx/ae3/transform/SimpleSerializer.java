/*
 * Created on 24.03.2006
 */
package ru.myx.ae3.transform;

import java.io.IOException;

/**
 * @author myx
 * 
 */
public abstract class SimpleSerializer extends AbstractSerializer {
	final Class<?>	inputClass;
	
	final String	resultType;
	
	final String	resultTypeWithAppendix;
	
	/**
	 * @param inputClass
	 * @param resultType
	 */
	public SimpleSerializer(final Class<?> inputClass, final String resultType) {
		this.inputClass = inputClass;
		this.resultType = resultType;
		this.resultTypeWithAppendix = resultType + ';';
	}
	
	@Override
	public boolean canSerialize(final SerializationRequest request) {
		return this.inputClass.isAssignableFrom( request.getObjectClass() )
				&& this.isAcceptable( request.getAcceptTypes() );
	}
	
	final boolean isAcceptable(final String[] types) {
		if (types == null || types.length == 0 || types[0].equals( "*/*" )) {
			return true;
		}
		for (final String type : types) {
			if (type.equals( this.resultType ) || type.startsWith( this.resultTypeWithAppendix )) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public abstract boolean serialize(final SerializationRequest request) throws IOException;
}
