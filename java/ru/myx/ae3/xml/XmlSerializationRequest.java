package ru.myx.ae3.xml;

import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.transform.SerializationRequest;

final class XmlSerializationRequest implements SerializationRequest {
	private final StringBuilder		contentTypeBuffer;
	
	private final Object			object;
	
	private final TransferCollector	collector;
	
	XmlSerializationRequest(final StringBuilder contentTypeBuffer,
			final Object object,
			final TransferCollector collector) {
		this.contentTypeBuffer = contentTypeBuffer;
		this.object = object;
		this.collector = collector;
	}
	
	@Override
	public final String[] getAcceptTypes() {
		return null;
	}
	
	@Override
	public final Object getObject() {
		return this.object;
	}
	
	@Override
	public final Class<?> getObjectClass() {
		return this.object.getClass();
	}
	
	@Override
	public final TransferCollector setResultType(final String contentType) {
		this.contentTypeBuffer.append( contentType );
		return this.collector;
	}
}
