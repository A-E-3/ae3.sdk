/*
 * Created on 24.03.2006
 */
package ru.myx.ae3.transform;

import java.io.IOException;

/**
 * @author myx
 * 
 */
public abstract class AbstractSerializer implements TransformSerializer {
	@Override
	public abstract boolean canSerialize(final SerializationRequest request);
	
	@Override
	public TransformSerializer getSerializer(final SerializationRequest request) {
		return this.canSerialize( request )
				? this
				: null;
	}
	
	@Override
	public abstract boolean serialize(final SerializationRequest request) throws IOException;
}
