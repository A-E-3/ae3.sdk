package ru.myx.ae3.serve;

import java.io.UnsupportedEncodingException;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.flow.FlowOperationException;
import ru.myx.ae3.reflect.ReflectionIgnore;

/**
 *
 * @author myx
 *
 * @param <T>
 */
@ReflectionIgnore
public abstract class AbstractCharacterServeRequest<T extends AbstractCharacterServeRequest<T>> extends AbstractServeRequestMutable<T> implements CharacterServeRequest<T> {

	/**
	 *
	 * @param owner
	 * @param verb
	 * @param attributes
	 */
	protected AbstractCharacterServeRequest(final String owner, final String verb, final BaseObject attributes) {

		super(owner, verb, attributes);
	}

	@Override
	public long getCharacterContentLength() {

		return this.getText().length();
	}

	@Override
	public abstract CharSequence getText();

	@Override
	public Value<? extends CharSequence> getTextContent() {

		return Base.forString(this.getText());
	}

	@Override
	public final boolean isCharacter() {

		return true;
	}

	@Override
	public UniversalServeRequest<?> toBinary() throws FlowOperationException {

		try {
			return Request.binaryWrapCharacter(this);
		} catch (final UnsupportedEncodingException e) {
			throw new FlowOperationException("Error converting from character to binary", e);
		}
	}

}
