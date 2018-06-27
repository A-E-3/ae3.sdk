/*
 * Created on 24.03.2006
 */
package ru.myx.ae3.transform;

import java.io.IOException;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.produce.ObjectFactory;

/**
 * @author myx
 * @param <T>
 *
 */
public abstract class TransformMaterializer<T> implements ObjectFactory<Object, T> {
	
	private static final Class<?>[] SOURCES = {
			TransferCopier.class, TransferBuffer.class
	};

	@Override
	public final boolean accepts(final String variant, final BaseObject attributes, final Class<?> source) {
		
		return variant != null && this.canMaterialize(variant, attributes);
	}

	/**
	 * @param contentType
	 * @param attributes
	 * @return boolean
	 */
	@SuppressWarnings("static-method")
	protected boolean canMaterialize(final String contentType, final BaseObject attributes) {
		
		return true;
	}

	/**
	 * @param contentType
	 * @param buffer
	 * @param attributes
	 * @return object
	 * @throws IOException
	 */
	protected abstract T materialize(final String contentType, final TransferBuffer buffer, final BaseObject attributes) throws IOException;

	@Override
	public T produce(final String variant, final BaseObject attributes, final Object source) {
		
		try {
			if (source instanceof TransferCopier) {
				return this.materialize(variant, ((TransferCopier) source).nextCopy(), attributes);
			}
			if (source instanceof TransferBuffer) {
				return this.materialize(variant, (TransferBuffer) source, attributes);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public final Class<?>[] sources() {
		
		return TransformMaterializer.SOURCES;
	}

}
