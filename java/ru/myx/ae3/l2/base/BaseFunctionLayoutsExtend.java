package ru.myx.ae3.l2.base;

import java.util.Iterator;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseMapEditable;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.exec.ExecCallableBoth;

/** @author myx */
public final class BaseFunctionLayoutsExtend extends BaseFunctionAbstract implements ExecCallableBoth.NativeJ2 {

	@Override
	public BaseObject callNJ2(final BaseObject instance, final BaseObject object, final BaseObject props) {

		if (object.baseIsPrimitive()) {
			throw new IllegalArgumentException("TypeError: primitive prototype: " + object);
		}
		
		assert object.baseArray() == null : "Cannot extend array!";

		final BaseMapEditable result = BaseObject.createObject(object);

		if (props == BaseObject.UNDEFINED || props == BaseObject.NULL) {
			return result;
		}
		if (props.baseIsPrimitive()) {
			throw new IllegalArgumentException("TypeError: primitive type, " + props);
		}
		{
			for (final Iterator<String> keys = props.baseKeysOwn(); keys.hasNext();) {
				final String key = keys.next();
				final BaseProperty property = props.baseGetOwnProperty(key);
				result.setOwnProperty(key, property, property.propertyAttributes(key));
			}
			return result;
		}
	}
	
	@Deprecated
	@Override
	public int execArgumentsMinimal() {

		return 1;
	}
	
	@Override
	public boolean execHasNamedArguments() {

		return false;
	}
	
	@Override
	public boolean execIsConstant() {

		return false;
	}
	
	@Override
	public Class<? extends Object> execResultClassJava() {

		return BaseMap.class;
	}
	
	@Override
	public String toString() {

		return "[Function extra Layouts.extend(obj, props) method]";
	}
}
