package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseLinkedList;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.reflect.ReflectionDisable;
import ru.myx.ae3.reflect.ReflectionHidden;

/** Mutable linked list.
 *
 * @author myx */
@ReflectionDisable
public class ExecArgumentsLinkedList extends BaseLinkedList<Object> implements ExecArguments {

	/**
	 *
	 */
	private static final long serialVersionUID = -1294148484657463059L;
	
	/**  */
	@ReflectionHidden
	public ExecArgumentsLinkedList() {
		//
	}
	
	@Override
	@ReflectionHidden
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {

		return ExecArguments.super.baseGetOwnProperty(name);
	}
	
	@Override
	@ReflectionHidden
	public BaseProperty baseGetOwnProperty(final String name) {

		return ExecArguments.super.baseGetOwnProperty(name);
	}
}
