package ru.myx.ae3.vfs.status;

import java.util.Map;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.status.StatusInfo;
import ru.myx.ae3.status.StatusProvider;
import ru.myx.ae3.vfs.ars.ArsRecord;

class RecordStatus implements ArsRecord, Value<RecordStatus> {

	static final Map<String, StatusBuilder> DATA;
	static {
		DATA = Create.privateMap(8);
		RecordStatus.DATA.put("status.txt", new StatusBuilder() {

			@Override
			public TransferCopier apply(final StatusProvider provider) {

				final StatusInfo status = provider.getStatus();
				return Transfer.createCopier(status.toString().getBytes(Engine.CHARSET_UTF8));
			}
		});
	}

	final String key;

	final StatusProvider provider;

	RecordStatus(final String key, final StatusProvider provider) {

		this.key = key;
		this.provider = provider;
	}

	@Override
	public RecordStatus baseValue() {

		return this;
	}

	Value<TransferCopier> getBinaryContent() {

		/** java feature: conditional cannot cast */
		final StatusBuilder builder = RecordStatus.DATA.get(this.key);
		if (builder != null) {
			return builder.apply(this.provider);
		}
		return Base.forNull();
	}

	@Override
	public long getBinaryContentLength() {

		final StatusBuilder builder = RecordStatus.DATA.get(this.key);
		return builder == null
			? 0
			: builder.apply(this.provider).length();
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

		return Base.forString((CharSequence) this.provider.getStatus().toString());
	}

	@Override
	public boolean isBinary() {

		return RecordStatus.DATA.containsKey(this.key);
	}

	@Override
	public boolean isCharacter() {

		return true;
	}

	@Override
	public boolean isContainer() {

		return !RecordStatus.DATA.containsKey(this.key);
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
