package ru.myx.ae3.common;

import ru.myx.ae3.base.BaseFutureTransferCopier;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 * @author myx
 * 		
 */
@ReflectionManual
public class FutureSimpleTransferCopier extends FutureSimpleUnknown<TransferCopier> implements BaseFutureTransferCopier {
	
	static {
		Reflect.classToBasePrototype(FutureSimpleTransferCopier.class);
	}
	
	/**
	 * 
	 */
	@ReflectionExplicit
	public FutureSimpleTransferCopier() {
		//
	}
	
}
