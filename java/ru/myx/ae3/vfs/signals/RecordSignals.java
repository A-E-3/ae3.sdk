package ru.myx.ae3.vfs.signals;

import java.util.function.Function;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.ars.ArsRecord;

class RecordSignals implements ArsRecord, Value<RecordSignals> {

	final String key;

	final Function<Void, Object> handler;

	RecordSignals(final String key, final Function<Void, Object> handler) {

		this.key = key;
		this.handler = handler;
	}

	@Override
	public RecordSignals baseValue() {

		return this;
	}

	Value<TransferCopier> getBinaryContent() {

		/** java feature: conditional cannot cast */
		if (this.handler == null) {
			return Base.forNull();
		}
		return Transfer.createCopier((this.handler.toString() + "\r\nDelete or modify this file to execute.").getBytes(Engine.CHARSET_UTF8));
	}

	@Override
	public long getBinaryContentLength() {

		return this.handler == null
			? 0
			: (this.handler.toString() + "\r\nDelete or modify this file to execute.").getBytes(Engine.CHARSET_UTF8).length;
	}

	@Override
	public String getKeyString() {

		return this.key;
	}

	@Override
	public BaseObject getPrimitiveBaseValue() {

		return Base.forString(this.key);
	}

	@Override
	public Guid getPrimitiveGuid() {

		return Guid.forString(this.key);
	}

	@Override
	public Object getPrimitiveValue() {

		return this.key;
	}

	Value<? extends CharSequence> getTextContent() {

		/** java feature: conditional cannot cast */
		if (this.handler == null) {
			return Base.forNull();
		}
		return Base.forString((CharSequence) (this.handler.toString() + "\r\nDelete or modify this file to execute."));
	}

	@Override
	public boolean isBinary() {

		return this.handler != null;
	}

	@Override
	public boolean isCharacter() {

		return this.handler != null;
	}

	@Override
	public boolean isContainer() {

		return false;
	}

	@Override
	public boolean isPrimitive() {

		return this.key != null;
	}

	@Override
	public String toString() {

		return this.key;
	}
}
