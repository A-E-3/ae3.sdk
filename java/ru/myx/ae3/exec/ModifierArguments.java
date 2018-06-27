/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;

/**
 * @author myx
 *
 */
public enum ModifierArguments implements ModifierArgument {
	/**
	 * <- ra0RB
	 */
	AA0RB {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "A0RB";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.ra0RB;
		}
		
	},
	/**
	 * <- ra1RL
	 */
	AA1RL {
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return process.ra1RL;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return (int) process.ra1RL;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return process.ra1RL;
		}
		
		@Override
		public String argumentNotation() {
			
			return "A1RL";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return Base.forLong(process.ra1RL);
		}
		
		@Override
		public CharSequence argumentString(final ExecProcess process) {
			
			return Base.toString(process.ra1RL);
		}
		
	},
	/**
	 * <- ra2RD
	 */
	AA2RD {
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return process.ra2RD;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return (int) process.ra2RD;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return (long) process.ra2RD;
		}
		
		@Override
		public String argumentNotation() {
			
			return "A2RD";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return Base.forDouble(process.ra2RD);
		}
		
		@Override
		public CharSequence argumentString(final ExecProcess process) {
			
			return Base.toString(process.ra2RD);
		}
		
	},
	/**
	 * <- ra3RS
	 */
	AA3RS {
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return Base.toDouble(process.ra3RS.toString());
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return Base.toInt32(process.ra3RS.toString());
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return Base.toLong(process.ra3RS.toString());
		}
		
		@Override
		public String argumentNotation() {
			
			return "A3RS";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return Base.forString(process.ra3RS);
		}
		
		@Override
		public CharSequence argumentString(final ExecProcess process) {
			
			return process.ra3RS;
		}
		
	},
	/**
	 * <- rb4CT
	 */
	AB4CT {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "B4CT";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.rb4CT;
		}
		
	},
	/**
	 * <- rb5CC
	 */
	AB5CC {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "B5CC";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.rb5CC;
		}
		
	},
	/**
	 * <- rb6CA
	 */
	AB6CA {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "B6CA";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.rb6CA;
		}
		
	},
	/**
	 * <- rb7FV
	 */
	AB7FV {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "B7FV";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			/**
			 * NOT: return process.r6FV;
			 *
			 * properties of parent contexts must be accessible from result of
			 * this method call, this way we can reach GLOBAL object!
			 */
			return process;
		}
		
	},
	/**
	 * <- 0
	 */
	AC10ZERO {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BasePrimitiveNumber.ZERO;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "0";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BasePrimitiveNumber.ZERO;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return 0;
		}
		
	},
	/**
	 * <- false
	 */
	AC11FALSE {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BaseObject.FALSE;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "false";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BaseObject.FALSE;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return 0;
		}
		
	},
	/**
	 * <- null
	 */
	AC12NULL {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BaseObject.NULL;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "null";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BaseObject.NULL;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return 0;
		}
		
	},
	/**
	 * <- undefined
	 */
	AC13UNDEFINED {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BaseObject.UNDEFINED;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "undefined";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BaseObject.UNDEFINED;
		}
		
		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 0;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return Double.NaN;
		}
		
	},
	/**
	 * <- 1
	 */
	AC14ONE {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BasePrimitiveNumber.ONE;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "1";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BasePrimitiveNumber.ONE;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 1;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 1;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return 1;
		}
		
	},
	/**
	 * <- true
	 */
	AC15TRUE {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BaseObject.TRUE;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "true";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BaseObject.TRUE;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return 1;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return 1;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return 1;
		}
	},
	/**
	 * <- (-1)
	 *
	 */
	AC16MONE {
		
		@Override
		public final BaseObject argumentConstantValue() {
			
			return BasePrimitiveNumber.MONE;
		}
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "-1";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return BasePrimitiveNumber.MONE;
		}

		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return -1;
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return -1;
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return -1;
		}
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AC17RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AE20RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * <- STACK[ -- SP ]
	 *
	 * POP
	 *
	 */
	AE21POP {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public String argumentNotation() {
			
			return "POP";
			// return "POP /*[--SP]*/";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.stackPop();
		}
		
		@Override
		public int argumentStackRead() {
			
			return 1;
		}
		
	},
	/**
	 * <- STACK[ SP - 1 ]
	 *
	 * PEEK
	 *
	 */
	AE22PEEK {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "PEEK";
			// return "PEEK /*[SP-1]*/";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.stackPeek();
		}
		
		@Override
		public int argumentStackRead() {
			
			return 1;
		}
		
		@Override
		public int argumentStackWrite() {
			
			return 1;
		}
	},
	/**
	 * <- STACK[ SP - 2 ]
	 *
	 * PEEK2
	 *
	 */
	AE23PEEK2 {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "PEEK2";
			// return "PEEK2 /*[SP-2]*/";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.stackPeek(1);
		}
		
		@Override
		public int argumentStackRead() {
			
			return 2;
		}
		
		@Override
		public int argumentStackWrite() {
			
			return 2;
		}
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AE24RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AE25RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AE26RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AE27RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * <- [ IP++ ]
	 *
	 * IMMEDIATE
	 *
	 */
	AF30IMM {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return false;
		}
		
		@Override
		public String argumentNotation() {
			
			return "IMMEDIATE";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			throw new UnsupportedOperationException("Use ModifierArgument" + this.toString() + " instance at runtime");
		}
		
	},
	/**
	 * <- RT[ [ IP++ ] ]
	 *
	 * RT IMMEDIATE
	 *
	 */
	AF31RTIMM {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public String argumentNotation() {
			
			return "RT_IMMEDIATE";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			throw new UnsupportedOperationException("Use ModifierArgument" + this.toString() + " instance at runtime");
		}
		
	},
	/**
	 * <- FV[ [ IP++ ] ]
	 *
	 * FV IMMEDIATE
	 *
	 */
	AF32FVIMM {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public String argumentNotation() {
			
			return "FV_IMMEDIATE";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			throw new UnsupportedOperationException("Use ModifierArgument" + this.toString() + " instance at runtime");
		}
		
	},
	/**
	 * <- GV[ [ IP++ ] ]
	 *
	 * FV IMMEDIATE
	 *
	 */
	AF33GVIMM {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public String argumentNotation() {
			
			return "GV_IMMEDIATE";
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			throw new UnsupportedOperationException("Use ModifierArgument" + this.toString() + " instance at runtime");
		}
		
	},
	/**
	 * AxRR - RB/RL/RD/RS based on call frame context
	 *
	 */
	AF34AXRR {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public String argumentNotation() {
			
			return "AXRR";
		}
		
		@Override
		public int argumentInt32(final ExecProcess process) {
			
			return process.riCallResultHandler.execReadInt(process);
		}
		
		@Override
		public long argumentLong(final ExecProcess process) {
			
			return process.riCallResultHandler.execReadLong(process);
		}
		
		@Override
		public double argumentDouble(final ExecProcess process) {
			
			return process.riCallResultHandler.execReadDouble(process);
		}
		
		@Override
		public CharSequence argumentString(final ExecProcess process) {
			
			return process.riCallResultHandler.execReadString(process);
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			return process.riCallResultHandler.execReadNative(process);
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AF35RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AF36RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},
	/**
	 * ?
	 *
	 * RESERVED
	 *
	 */
	AF37RSVD {
		
		@Override
		public boolean argumentHasSideEffects() {
			
			return true;
		}
		
		@Override
		public BaseObject argumentRead(final ExecProcess process) {
			
			assert false : "Reserved, this=" + this;
			return BaseObject.UNDEFINED;
		}
		
	},;
	
	private static final ModifierArguments[] ALL = ModifierArguments.values();
	
	/**
	 *
	 * @param modifier
	 * @return
	 */
	public static final boolean isConstantValue(final ModifierArgument modifier) {
		
		return modifier != null && modifier.argumentConstantValue() != null;
	}
	
	/**
	 * @return
	 */
	public static final int valueCount() {
		
		return ModifierArguments.ALL.length;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public static final ModifierArguments valueForIndex(final int index) {
		
		return ModifierArguments.ALL[index];
	}
	
	/**
	 * @return code
	 */
	@Override
	public String argumentNotation() {
		
		return this.name();
	}
	
	/**
	 * @param process
	 * @return object
	 */
	@Override
	public abstract BaseObject argumentRead(final ExecProcess process);
}
