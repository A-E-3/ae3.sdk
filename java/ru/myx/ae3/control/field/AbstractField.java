/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.field;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.AbstractBasic;

/** @author myx
 * @param <F>
 *            Actual field class
 * @param <J>
 *            Java value
 * @param <N>
 *            Native value */
public abstract class AbstractField<F extends AbstractField<?, J, N>, J extends Object, N extends BaseObject> //
		extends
			AbstractBasic<F> //
		implements
			ControlFieldGeneric<F, J, N> {

	@Override
	public abstract F cloneField();

}
