/**
 *
 */
package ru.myx.renderer.ecma;

import java.io.Serializable;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;

/** @author myx */
final class ClassInstance extends BaseNativeObject implements Serializable {

	private static class DummyConstructorClass extends BaseFunctionAbstract implements ExecCallableBoth.NativeJ0 {
		
		@Override
		public BaseObject callNJ0(final BaseObject instance) {

			return BaseObject.UNDEFINED;
		}
		
		@Override
		public boolean execIsConstant() {

			return false;
		}
		
		@Override
		public Class<? extends Object> execResultClassJava() {

			return Object.class;
		}
		
		@Override
		public BaseObject execScope() {

			/** executes in real current scope */
			return ExecProcess.GLOBAL;
		}
	}
	
	/**
	 *
	 */
	private static final long serialVersionUID = -4386748758093068857L;
	
	private static final ExecCallable DUMMY_CONSTRUCTOR = new DummyConstructorClass();
	
	private final String name;
	
	ClassInstance(final String name) {

		this.name = name;
		this.put(name, ClassInstance.DUMMY_CONSTRUCTOR);
	}
	
	@Override
	public final String toString() {

		return "class " + this.name;
	}
	
}
