package ru.myx.ae3.e4.parse.java;

import ru.myx.ae3.e4.logic.LogicValue;
import ru.myx.ae3.e4.parse.ProgramSource;

public class LogicJavaSource implements LogicValue<Class<?>> {
	
	
	private String packageName;

	private String unitName;

	private ProgramSource source;

	private Class<?> cls;

	LogicJavaSource(final String packageName, final String unitName, final ProgramSource source) {
		//
	}

	public String toJavaSource() {
		
		
		return String.valueOf(this.source.getSourceCode());
	}

	public Class<?> toJavaClass() {
		
		
		//
	}

	@Override
	public Class<?> toConstantValue() {
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
