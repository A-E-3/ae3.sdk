package ru.myx.ae3.e4.tokenize;

public interface TokenizeContext {
	
	void addComment();
	
	
	void addWhitespace();
	
	
	void addOperators();
	
	
	void addConstant();
	
	
	void addIdentifier();
	
}
