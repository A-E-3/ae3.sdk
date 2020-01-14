package ru.myx.ae3.serve;

import java.util.function.Function;

import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** @author myx
 *
 *         It just collects the reply which you can then access by .getResult() method. */
@ReflectionManual
public class SimpleServeRequest extends AbstractServeRequestMutable<SimpleServeRequest> implements Function<ReplyAnswer, Boolean> {

	private static BaseObject PROTOTYPE = Reflect.classToBasePrototype(SimpleServeRequest.class);

	private final TransferCollector collector = Transfer.createCollector();

	private ReplyAnswer result = null;

	/**
	 *
	 */
	@ReflectionExplicit
	public SimpleServeRequest() {

		super("SIMPLE-SRV-RQ", "GET", null);
		this.setResponseTarget(this);
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {

		assert reply != null : "NULL reply!";
		this.result = reply;
		return Boolean.TRUE;
	}

	@Override
	public BaseObject basePrototype() {

		return SimpleServeRequest.PROTOTYPE;
	}

	/** @return */
	@ReflectionExplicit
	public ReplyAnswer getResult() {

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
