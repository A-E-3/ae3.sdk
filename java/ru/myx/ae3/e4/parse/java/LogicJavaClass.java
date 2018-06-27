package ru.myx.ae3.e4.parse.java;

import java.lang.reflect.Modifier;

public class LogicJavaClass implements LogicJava {
	
	private int classModifiers = Modifier.PUBLIC;
	private String className;
	private Class<?> classExtends;
	private Class<?>[] classImplements;
	
	private LogicJavaMember[] classMembers;
	
	LogicJavaClass() {
		//
	}
	
	@Override
	public StringBuilder dumpJavaCode(StringBuilder b, String linePrefix) {
		
		if (this.className == null) {
			throw new Error("ClassName!");
		}
		
		if (Modifier.isPublic(this.classModifiers)) {
			b.append("public ");
		}
		if (Modifier.isFinal(this.classModifiers)) {
			b.append("final ");
		}
		if (Modifier.isAbstract(this.classModifiers)) {
			b.append("abstract ");
		}
		
		b.append("class ").append(this.className);
		
		if (this.classExtends != null) {
			b.append(" extends ").append(this.classExtends.getCanonicalName());
		}
		
		if (this.classImplements != null) {
			final int length = this.classImplements.length;
			if (length > 0) {
				b.append(" implements ").append(this.classImplements[0].getCanonicalName());
				for (int i = 1; i < length; i++) {
					b.append(", ").append(this.classImplements[i].getCanonicalName());
				}
			}
		}
		
		b.append("{\n");
		
		final String newPrefix = linePrefix + '\t';
		
		b.append("}\n");
		return b;
	}
}
