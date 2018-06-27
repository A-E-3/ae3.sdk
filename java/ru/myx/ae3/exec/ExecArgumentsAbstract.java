package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseHostSealed;
import ru.myx.ae3.base.BaseObject;

/** @author myx */
public abstract class ExecArgumentsAbstract extends BaseHostSealed implements ExecArguments {
	
	/** @param arguments
	 * @return arguments impl */
	static ExecArguments createArgumentsNamedClone(final NamedToIndexMapper desc, final BaseArray arguments) {
		
		if (arguments == null || arguments == ExecArgumentsEmpty.INSTANCE) {
			return ExecArgumentsEmpty.INSTANCE;
		}

		final int size = arguments.length();
		switch (size) {
			case 0 :
				assert false : "C'mon how could there be named arguments without arguments?";
				return ExecArgumentsEmpty.INSTANCE;
			case 1 : {
				final String[] names = desc.names();
				if (names.length == 1) {
					return new ExecArgumentsMap1(
							/** length == 1, so, we cannot miss */
							names[0],
							/** length == 1, so, we cannot miss */
							arguments.baseGetFirst(BaseObject.UNDEFINED));
				}
			}
			//$FALL-THROUGH$
			default :
				return new ExecArgumentsMapX(desc, arguments, size);
		}
	}

	/**
	 */
	protected ExecArgumentsAbstract() {

		//
	}

	@Override
	public String toString() {
		
		return "[object " + this.baseClass() + "(length=" + this.length() + ")]";
	}

}
