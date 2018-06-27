package ru.myx.ae3.e4.logic.ecma;

import ru.myx.ae3.e4.logic.LogicStatement;

/**
 * 
 * @author myx
 * 		
 */
public class LogicLabelledBlock implements LogicStatement {
	
	public final String label;
	
	LogicLabelledBlock(final String label, final LogicStatement statement) {
		this.label = label;
	}
}
