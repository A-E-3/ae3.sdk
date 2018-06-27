/*
 * Created on 12.05.2006
 */
package ru.myx.ae3.eval;

/**
 * @author myx
 * 
 */
public enum BalanceType {
	/**
	 * No balance required, call arguments.
	 * 
	 * 1) a = 5, b = 77 will be parsed as two arguments containing assignments
	 */
	ARGUMENT_LIST,
	/**
	 * No balance required, named call arguments.
	 * 
	 * 1) a = 5, b = 77 will be parsed as two named arguments
	 * 
	 * TODO NAMED_ARGUMENTS
	 */
	// NAMED_ARGUMENTS,
	/**
	 * Zero balance required. Statement. Can be optimized keeping in mind that
	 * no results are expected.
	 * 
	 * 1) a.b + 5 could be reduced to a.b cause it can produce side effects in
	 * form of error
	 */
	STATEMENT,
	/**
	 * Zero balance required. Declaration statement.
	 * 
	 * var a = 5, b = 6, c = f();
	 * 
	 * portion between 'var ' and ';'
	 * 
	 */
	DECLARATION,
	/**
	 * Object result required. Expression.
	 */
	EXPRESSION,
	/**
	 * Expression. Can be optimized keeping in mind that only boolean result
	 * expected.
	 * 
	 * 1) [] - is not constant but always true
	 * 
	 * TODO BOOLEAN_EXPRESSION
	 */
	// BOOLEAN_EXPRESSION,
	/**
	 * Array result required.
	 * 
	 * 1) result is an array (instance of BaseNativeArray actually)
	 */
	ARRAY_LITERAL,
	/**
	 * Object result required.
	 * 
	 * 1) result is an object (instance of BaseNativeObject actually)
	 * 
	 * 2) sets of <code>key1 : value1[, key2 : value2...]</code> threated as
	 * property names and values of object to be created.
	 * 
	 * TODO OBJECT_LITERAL
	 */
	// OBJECT_LITERAL,
}
