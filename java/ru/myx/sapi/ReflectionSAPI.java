/*
 * Created on 17.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sapi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import ru.myx.ae3.reflect.Reflect;

/**
 * @author barachta
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReflectionSAPI {
	
	/**
	 * Example: public final static String name name#CSS public String addr
	 * addr#VIS public final int numb numb#CIi
	 * 
	 * @param field
	 * @return string
	 */
	public static final String describeField(final Field field) {
		return Reflect.describeField( field );
	}
	
	/**
	 * @param o
	 * @return string array
	 */
	public static final String[] describeFields(final Object o) {
		return Reflect.describeFields( o );
	}
	
	/**
	 * @param o
	 * @return string array
	 */
	public static final String[] describeMembers(final Object o) {
		return Reflect.describeMembers( o );
	}
	
	/**
	 * Example: public final static String name(int a, long b, double c)
	 * name#MSS#ild public void addr(Date d, Enumeration e, boolean f)
	 * addr#MIV#OOB public final int numb() numb#MIi#
	 * 
	 * @param method
	 * @return string
	 */
	public static final String describeMethod(final Method method) {
		return Reflect.describeMethod( method );
	}
	
	/**
	 * @param o
	 * @return string array
	 */
	public static final String[] describeMethods(final Object o) {
		return Reflect.describeMethods( o );
	}
	
	/**
	 * @param cls
	 * @return char
	 */
	public static final char getClassLetter(final Class<?> cls) {
		return Reflect.getClassLetter( cls );
	}
}
