package ru.myx.ae3.serve.binary;

import java.util.function.Function;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;
import ru.myx.ae3.serve.AbstractServeRequestMutable;

/**
 * @author myx
 * 
 */
@ReflectionManual
public class ServeRequestCollectReply extends AbstractServeRequestMutable<ServeRequestCollectReply> implements Function<ReplyAnswer, Boolean> {

	private final TransferCollector collector = Transfer.createCollector();

	private TransferCopier result = null;

	private static BaseObject PROTOTYPE = Reflect.classToBasePrototype(ServeRequestCollectReply.class);

	/**
	 *
	 */
	@ReflectionExplicit
	public ServeRequestCollectReply() {
		super("BINARY", "GET", null);
		this.setResponseTarget(this);
	}

	@Override
	public BaseObject basePrototype() {

		return ServeRequestCollectReply.PROTOTYPE;
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {

		assert reply != null : "NULL reply!";
		if (reply.isBinary()) {
			this.result = reply.toBinary().getBinary();
			return Boolean.TRUE;
		}
		if (reply.isCharacter()) {
			this.result = Transfer.createCopierUtf8(reply.toCharacter().getText());
			return Boolean.TRUE;
		}
		if (reply.isFile()) {
			this.result = Transfer.createCopier(reply.getFile());
			return Boolean.TRUE;
		}
		{
			throw new IllegalArgumentException("Unsupported reply type: " + reply + ", class=" + reply.getClass().getSimpleName());
		}
	}

	/**
	 *
	 * @return
	 */
	@ReflectionExplicit
	public TransferCopier getResult() {

		try {
			return this.result;
		} finally {
			this.result = null;
		}
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + ": collector=" + this.collector;
	}

}
