package ru.myx.ae3.e4.logic.base;

import ru.myx.ae3.e4.logic.LogicValue;

public class LogicValueOperation2X {
	
	enum Operation2X {
		LA_PLUS {
			
			@Override
			public String toSourceNotation() {
				
				return "+";
			}
		}, //
		LN_ADD {
			
			@Override
			public String toSourceNotation() {
				
				return "+";
			}
		}, //
		LS_CONCAT {
			
			@Override
			public String toSourceNotation() {
				
				return "+";
			}
		}, //
		;
		
		public abstract String toSourceNotation();
		
		public String toString(final LogicValue left, final LogicValue right) {
			
			return left.toString() + " " + this.toSourceNotation() + " " + right.toString();
		}
	}
	
	public LogicValueOperation2X(final LogicValue left, final LogicValue right, final Operation2X operation) {
		//
	}
}
