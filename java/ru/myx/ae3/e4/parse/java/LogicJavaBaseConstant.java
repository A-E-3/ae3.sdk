package ru.myx.ae3.e4.parse.java;

import ru.myx.ae3.base.BaseObject;

public class LogicJavaBaseConstant implements LogicJavaValue {
	
	private final BaseObject x;
	
	LogicJavaBaseConstant(final BaseObject x) {
		this.x = x;
	}
	
	@Override
	public Object toConstantValue() {
		
		return null;
	}
	
}
