package ru.myx.ae3.e4.parse.java;

import ru.myx.ae3.e4.logic.LogicValue;

/**
 * 
 * @author myx
 *		
 */
public class LogicJavaFunctionRoot implements LogicValue<Class<?>>, LogicJava {
	
	private String packageName;
	private Class<?>[] imports;
	
	private LogicJava body;
	
	LogicJavaFunctionRoot(final String packageName, final Class<?>[] imports) {
		this.packageName = packageName;
		this.imports = imports;
	}
	
	@Override
	public StringBuilder dumpJavaCode(StringBuilder b, String linePrefix) {
		
		b.append('\n');
		if (this.packageName != null) {
			b.append(linePrefix).append("package ").append(this.packageName).append(';').append('\n');
		}
		if (this.imports != null) {
			b.append('\n');
			for (final Class<?> cls : this.imports) {
				b.append(linePrefix).append("import ").append(cls.getCanonicalName()).append(';').append('\n');
			}
		}
		if (this.body != null) {
			b.append('\n');
			this.body.dumpJavaCode(b, linePrefix);
		}
		return b;
	}
	
	/**
	 * Must evaluate and return the class object for this unit
	 */
	@Override
	public Class<?> toConstantValue() {
		
		// TODO Auto-generated method stub
		return null;
	}
}
