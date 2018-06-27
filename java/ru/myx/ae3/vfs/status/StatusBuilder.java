package ru.myx.ae3.vfs.status;

import java.util.function.Function;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.status.StatusProvider;

interface StatusBuilder extends Function<StatusProvider, TransferCopier> {
	/**
	 * @param arg
	 * @return function result
	 */
	@Override
	TransferCopier apply(final StatusProvider arg);
}
