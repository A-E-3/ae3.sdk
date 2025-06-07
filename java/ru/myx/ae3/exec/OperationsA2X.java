/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseArrayWritable;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.ecma.compare.ComparatorEcma;
import ru.myx.ae3.help.Format;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA2X;

/** @author myx */
public enum OperationsA2X implements OperationA2X {

	/**
	 *
	 */
	XACALLS {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			assert argumentA != ctx || ctx == ctx.rb4CT : "Use FCALLS then";

			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}

			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);

			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							(argumentA == ctx
								? "Context has no property called "
								: Format.Compact.baseObject(argumentA) + " has no property called ") //
									+ Format.Compact.baseObject(argumentB))//
					;
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToJavaString() + ", class=" + candidate.getClass().getName());
			}

			return ctx.vmCall_Generic_StackArgs(callee, argumentA, constant, store);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** detach-able, any
	 *
	 * always detach-able (LOAD is used for frame access) */
	XACCESS_D {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VACCESS_NA;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, store);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/**
	 *
	 */
	XADELETE {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, argumentA.baseDelete(argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** 11.3.1 Postfix Increment Operator
	 * <p>
	 * The production PostfixExpression : LeftHandSideExpression [no LineTerminator here] ++ is
	 * evaluated as follows:<br>
	 * 1. Evaluate LeftHandSideExpression.<br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Call ToNumber(Result(2)). <br>
	 * 4. Add the value 1 to Result(3), using the same rules as for the + operator (section
	 * 11.6.3).<br>
	 * 5. Call PutValue(Result(1), Result(4)).<br>
	 * 6. Return Result(3).<br>
	 * <p>
	 * From +
	 * <p>
	 * • If either operand is NaN, the result is NaN. */
	/** add & store then get */
	XASADDGET {

		@SuppressWarnings("unchecked")
		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> keyPrimitive = argumentB.baseToPrimitive(null);
			if (keyPrimitive.baseIsPrimitiveInteger()) {
				final BaseArray array = argumentA.baseArray();
				if (array != null) {
					final int key = keyPrimitive.intValue();
					final BasePrimitiveNumber leftHand = array.baseGet(key, BasePrimitiveNumber.NAN).baseToNumber();
					final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
						? leftHand
						: leftHand.baseIsPrimitiveInteger()
							? Base.forLong(leftHand.longValue() + constant)
							: Base.forDouble(leftHand.doubleValue() + constant);
					final BaseArrayWritable<?> writable = array.baseArrayWritable();
					if (writable != null) {
						((BaseArrayWritable<Object>) writable).baseSet(key, result);
					}
					return store.execReturn(ctx, result);
				}
			}
			{
				final BasePrimitiveString key = keyPrimitive.baseToString();
				final BasePrimitiveNumber leftHand = argumentA.baseGet(key, BasePrimitiveNumber.NAN).baseToNumber();
				final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				argumentA.baseDefine(key, result, BaseProperty.ATTRS_MASK_WED);
				return store.execReturn(ctx, result);
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** 11.3.2 Postfix Decrement Operator
	 * <p>
	 * The production PostfixExpression : LeftHandSideExpression [no LineTerminator here] -- is
	 * evaluated as follows:<br>
	 * 1. Evaluate LeftHandSideExpression.<br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Call ToNumber(Result(2)). <br>
	 * 4. Subtract the value 1 from Result(3), using the same rules as for the - operator (section
	 * 11.6.3).<br>
	 * 5. Call PutValue(Result(1), Result(4)).<br>
	 * 6. Return Result(3).<br>
	 * <p>
	 * From -
	 * <p>
	 * • If either operand is NaN, the result is NaN. */
	/** get then add & store */
	XASGETADD {

		@SuppressWarnings("unchecked")
		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> keyPrimitive = argumentB.baseToPrimitive(null);
			if (keyPrimitive.baseIsPrimitiveInteger()) {
				final BaseArray array = argumentA.baseArray();
				if (array != null) {
					final int key = keyPrimitive.intValue();
					final BasePrimitiveNumber leftHand = array.baseGet(key, BasePrimitiveNumber.NAN).baseToNumber();
					final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
						? leftHand
						: leftHand.baseIsPrimitiveInteger()
							? Base.forLong(leftHand.longValue() + constant)
							: Base.forDouble(leftHand.doubleValue() + constant);
					final BaseArrayWritable<?> writable = array.baseArrayWritable();
					if (writable != null) {
						((BaseArrayWritable<Object>) writable).baseSet(key, result);
					}
					return store.execReturn(ctx, leftHand);
				}
			}
			{
				final BasePrimitiveString key = keyPrimitive.baseToString();
				final BasePrimitiveNumber leftHand = argumentA.baseGet(key, BasePrimitiveNumber.NAN).baseToNumber();
				final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				argumentA.baseDefine(key, result, BaseProperty.ATTRS_MASK_WED);
				return store.execReturn(ctx, leftHand);
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/**
	 *
	 */
	XBEQU {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, BaseObject.equalsNative(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.8.7 The in operator
	 *
	 * The production RelationalExpression : RelationalExpression in ShiftExpression is evaluated as
	 * follows:<br>
	 * 1. Let lref be the result of evaluating RelationalExpression. <br>
	 * 2. Let lval be GetValue(lref). <br>
	 * 3. Let rref be the result of evaluating ShiftExpression. <br>
	 * 4. Let rval be GetValue(rref). 5. If Type(rval) is not Object, throw a TypeError exception.
	 * <br>
	 * 6. Return the result of calling the [[HasProperty]] internal method of rval with argument
	 * ToString(lval). */
	XBIN {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> primitiveA = argumentA.baseToPrimitive(null);

			if (primitiveA.baseIsPrimitiveInteger()) {
				final BaseArray array = argumentB.baseArray();
				if (array != null) {
					final int intA = primitiveA.intValue();
					return store.execReturnBoolean(ctx, intA >= 0 && intA < array.length());
				}
			}

			final BasePrimitiveString identity = primitiveA.baseToStringIfReady();
			if (identity != null) {
				return store.execReturnBoolean(ctx, argumentB.baseFindProperty(identity) != null);
			}

			return store.execReturnBoolean(ctx, argumentB.baseFindProperty(primitiveA.stringValue()) != null);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.8.6 The instanceof operator
	 *
	 * The production RelationalExpression: RelationalExpression instanceof ShiftExpression is
	 * evaluated as follows:<br>
	 * 1. Let lref be the result of evaluating RelationalExpression. <br>
	 * 2. Let lval be GetValue(lref). <br>
	 * 3. Let rref be the result of evaluating ShiftExpression. <br>
	 * 4. Let rval be GetValue(rref). <br>
	 * 5. If Type(rval) is not Object, throw a TypeError exception. <br>
	 * 6. If rval does not have a [[HasInstance]] internal method, throw a TypeError exception. <br>
	 * 7. Return the result of calling the [[HasInstance]] internal method of rval with argument
	 * lval. */
	XBINSTOF {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final Object rightHand = argumentB.baseValue();
			/** Can be java class with no constructor, abstract one or even an interface. */
			if (rightHand instanceof final Class<?> classObject) {
				if (classObject.isInstance(argumentA)) {
					return store.execReturnTrue(ctx);
				}
				final Object leftHand = argumentA.baseValue();
				if (leftHand != null && leftHand != argumentA) {
					if (((Class<?>) rightHand).isInstance(argumentA)) {
						return store.execReturnTrue(ctx);
					}
				}
			}

			final BaseFunction function = argumentB.baseConstruct();
			return function == null
				? ctx.vmRaise("TypeError")
				: store.execReturnBoolean(ctx, function.baseHasInstance(argumentA));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XBLESS {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, ComparatorEcma.compareLESS(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XBMORE {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, ComparatorEcma.compareMORE(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XBNEQU {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, !BaseObject.equalsNative(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XBNLESS {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, ComparatorEcma.compareNLESS(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XBNMORE {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturnBoolean(ctx, ComparatorEcma.compareNMORE(argumentA, argumentB));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.9.6 The Strict Equality Comparison Algorithm
	 * <p>
	 * The comparison x === y, where x and y are values, produces true or false. Such a comparison
	 * is performed as follows: <br>
	 * 1. If Type(x) is different from Type(y), return false.<br>
	 * 2. If Type(x) is Undefined, return true. <br>
	 * 3. If Type(x) is Null, return true. <br>
	 * 4. If Type(x) is not Number, go to step 11.<br>
	 * 5. If x is NaN, return false. <br>
	 * 6. If y is NaN, return false. <br>
	 * 7. If x is the same number value as y, return true.<br>
	 * 8. If x is +0 and y is −0, return true. <br>
	 * 9. If x is −0 and y is +0, return true. <br>
	 * 10. Return false. <br>
	 * 11. If Type(x) is String, then return true if x and y are exactly the same sequence of
	 * characters (same length and same characters in corresponding positions); otherwise, return
	 * false.<br>
	 * 12. If Type(x) is Boolean, return true if x and y are both true or both false; otherwise,
	 * return false.<br>
	 * 13. Return true if x and y refer to the same object or if they refer to objects joined to
	 * each other (section 13.1.2). Otherwise, return false. <br>
	 * <p>
	 */
	XBSEQU {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, BaseObject argumentA, BaseObject argumentB, final int constant, final ResultHandler store) {

			loop : for (;;) {
				if (argumentA == BasePrimitiveNumber.NAN || argumentB == BasePrimitiveNumber.NAN) {
					return store.execReturnFalse(ctx);
				}
				if (argumentB == argumentA) {
					return store.execReturnTrue(ctx);
				}
				/** have to do all of this because of non-detachable values */
				if (argumentA instanceof BasePrimitive<?> && argumentB instanceof BasePrimitive<?>) {
					/** Strings are strictly comparable
					 *
					 * No code needed:<code>
					 * </code> */
					if (argumentA.baseIsPrimitiveString() && argumentB.baseIsPrimitiveString()) {
						return store.execReturnBoolean(ctx, argumentA.toString().equals(argumentB.toString()));
					}
					/** Numbers are strictly comparable
					 *
					 * No code needed:<code>
					 * </code> */
					if (argumentA.baseIsPrimitiveNumber() && argumentB.baseIsPrimitiveNumber()) {
						return store.execReturnBoolean(ctx, ((BasePrimitive<?>) argumentA).doubleValue() == ((BasePrimitive<?>) argumentB).doubleValue());
					}
					/** Booleans are strictly comparable
					 *
					 * No code needed:<code>
					if (argumentA.baseIsPrimitiveBoolean() && argumentB.baseIsPrimitiveBoolean()) {
						ctx.r7RR = argumentA.equals( argumentB )
								? BaseObject.TRUE
								: BaseObject.FALSE;
						return null;
					}
					 * </code> */
				}
				if (argumentA instanceof final ExecValueStack<?> execValueStack) {
					// TODO: check is it really needed? .baseValue()?!
					argumentA.baseValue();
					final BaseObject replacement = execValueStack.toNative();
					if (replacement != argumentA) {
						argumentA = replacement;
						continue loop;
					}
				}
				if (argumentB instanceof final ExecValueStack<?> execValueStack) {
					// TODO: check is it really needed? .baseValue()?!
					argumentB.baseValue();
					final BaseObject replacement = execValueStack.toNative();
					if (replacement != argumentB) {
						argumentB = replacement;
						continue loop;
					}
				}
				return store.execReturnFalse(ctx);
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.9.6 The Strict Equality Comparison Algorithm
	 * <p>
	 * The comparison x === y, where x and y are values, produces true or false. Such a comparison
	 * is performed as follows: <br>
	 * 1. If Type(x) is different from Type(y), return false.<br>
	 * 2. If Type(x) is Undefined, return true. <br>
	 * 3. If Type(x) is Null, return true. <br>
	 * 4. If Type(x) is not Number, go to step 11.<br>
	 * 5. If x is NaN, return false. <br>
	 * 6. If y is NaN, return false. <br>
	 * 7. If x is the same number value as y, return true.<br>
	 * 8. If x is +0 and y is −0, return true. <br>
	 * 9. If x is −0 and y is +0, return true. <br>
	 * 10. Return false. <br>
	 * 11. If Type(x) is String, then return true if x and y are exactly the same sequence of
	 * characters (same length and same characters in corresponding positions); otherwise, return
	 * false.<br>
	 * 12. If Type(x) is Boolean, return true if x and y are both true or both false; otherwise,
	 * return false.<br>
	 * 13. Return true if x and y refer to the same object or if they refer to objects joined to
	 * each other (section 13.1.2). Otherwise, return false. <br>
	 * <p>
	 */
	XBSNEQU {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, BaseObject argumentA, BaseObject argumentB, final int constant, final ResultHandler store) {

			loop : for (;;) {
				if (argumentA == BasePrimitiveNumber.NAN || argumentB == BasePrimitiveNumber.NAN) {
					return store.execReturnTrue(ctx);
				}
				if (argumentB == argumentA) {
					return store.execReturnFalse(ctx);
				}
				/** have to do all of this because of non-detachable values */
				if (argumentA instanceof BasePrimitive<?> && argumentB instanceof BasePrimitive<?>) {
					/** Numbers are strictly comparable
					 *
					 * No code needed:<code>
					 * </code> */
					if (argumentA.baseIsPrimitiveString() && argumentB.baseIsPrimitiveString()) {
						return store.execReturnBoolean(ctx, !argumentA.toString().equals(argumentB.toString()));
					}
					/** Strings are strictly comparable
					 *
					 * No code needed:<code>
					 * </code> */
					if (argumentA.baseIsPrimitiveNumber() && argumentB.baseIsPrimitiveNumber()) {
						return store.execReturnBoolean(ctx, ((BasePrimitive<?>) argumentA).doubleValue() != ((BasePrimitive<?>) argumentB).doubleValue());
					}
					/** Booleans are strictly comparable
					 *
					 * No code needed:<code>
					if (argumentA.baseIsPrimitiveBoolean() && argumentB.baseIsPrimitiveBoolean()) {
						ctx.r7RR = argumentA.equals( argumentB )
								? BaseObject.FALSE
								: BaseObject.TRUE;
						return null;
					}
					 * </code> */
				}
				if (argumentA instanceof ExecValueStack) {
					// TODO: check is it really needed? .baseValue()?!
					argumentA.baseValue();
					final BaseObject replacement = ((ExecValueStack<?>) argumentA).toNative();
					if (replacement != argumentA) {
						argumentA = replacement;
						continue loop;
					}
				}
				if (argumentB instanceof ExecValueStack) {
					// TODO: check is it really needed? .baseValue()?!
					argumentB.baseValue();
					final BaseObject replacement = ((ExecValueStack<?>) argumentB).toNative();
					if (replacement != argumentB) {
						argumentB = replacement;
						continue loop;
					}
				}
				return store.execReturnTrue(ctx);
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.BOOLEAN;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** returns argumentB when argumentA is falsish */
	XEBOR_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VEBOR_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VEBOR_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			switch (constant) {
				/** return first argument if it is JS-boolean true, return second argument
				 * otherwise. */
				case 0 :
					return store.execReturn(
							ctx,
							argumentA.baseToJavaBoolean()
								? argumentA
								: argumentB);
				/** return first argument if it is not an UNDEFINED, return second argument
				 * otherwise. */
				case 1 :
					return store.execReturn(
							ctx,
							argumentA != BaseObject.UNDEFINED
								? argumentA
								: argumentB);
				/** return first argument if it has non Java-NULL baseValue(), return second
				 * argument otherwise.
				 *
				 * (NULL and UNDEFINED have Java-NULL baseValue(), for example) */
				case 2 :
					return store.execReturn(
							ctx,
							argumentA.baseValue() != null
								? argumentA
								: argumentB);
				default :
					return ctx.vmRaise("Invalid constant value: " + constant);
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** returns argumentB when argumentA is nullish */
	XENCO_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VENCO_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VENCO_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			return store.execReturn(
					ctx,
					argumentA.baseValue() != null
						? argumentA
						: argumentB);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XFCALLM {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BaseFunction callee = argumentA.baseCall();
			if (callee == null) {
				if (argumentA == BaseObject.UNDEFINED) {
					return ctx.vmRaise("Function is undefined");
				}
				return ctx.vmRaise("Not a function: class=" + argumentA.getClass().getName());
			}

			if (argumentB instanceof BaseArray) {
				return callee.execCallPrepare(ctx, ctx.contextImplicitThisValue(), store, false, (BaseArray) argumentB);
			}

			if (argumentB instanceof NamedToIndexMapper) {

				final NamedToIndexMapper mapper = (NamedToIndexMapper) argumentB;
				return ctx.vmCall_Generic_MapArgs(callee, ctx.contextImplicitThisValue(), mapper, store);
			}

			return ctx.vmRaise(
					"Invalid arguments argument: key=" + argumentA.baseToString() + ", class=" + callee.getClass().getName() + ", argumentsClass: "
							+ argumentB.getClass().getSimpleName());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** SKIP CHECK ACCESS for RCALLA
	 *
	 * get 2 arguments, make 2 stack (thisValue and callee)
	 *
	 * if callee is undefined, do STORE, otherwise return null and IGNORE store **/
	XNCALLPREP {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BaseObject candidateValue = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);
			final Object replacementValue = candidateValue.baseValue();
			/* strict ?? false */
			if (replacementValue == null) {
				return store.execReturnUndefined(ctx);
			}
			/* strict ?? true */
			if (replacementValue == candidateValue) {
				ctx.stackPush(argumentA);
				ctx.stackPush(ctx.ra0RB = candidateValue);
				return null;
			}
			/* implicit reference (References and Promises) */
			if (replacementValue instanceof final BaseObject baseValue) {
				/* reference ?? false */
				if (baseValue.baseValue() == null) {
					return store.execReturnUndefined(ctx);
				}
				/* reference ?? true */
				ctx.stackPush(argumentA);
				ctx.stackPush(ctx.ra0RB = baseValue);
				return null;
			}
			/* failover ?? true */
			ctx.stackPush(argumentA);
			ctx.stackPush(ctx.ra0RB = candidateValue);
			return null;
		}

		@Override
		public InstructionResult getResultType() {

			return null;
		}

		@Override
		public boolean isConstantForArguments() {

			return false;
		}

	},
	/**
	 *
	 */
	XFCALLO {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BaseFunction callee = argumentA.baseCall();
			if (callee == null) {
				if (argumentA == BaseObject.UNDEFINED) {
					return ctx.vmRaise("Function is undefined");
				}
				return ctx.vmRaise("Not a function: class=" + argumentA.getClass().getName());
			}

			return callee.execCallPrepare(ctx, ctx.contextImplicitThisValue(), store, false, argumentB);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/**
	 *
	 */
	XFDECLARE_D {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VFDECLARE_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsA2X.XFDECLARE_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BaseObject value = ExecProcess.vmEnsureDetached(ctx, argumentB);

			ctx.contextCreateMutableBinding(argumentA, value, false);

			return store.execReturn(ctx, value);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** 11.13.1 Simple Assignment ( = ) The production AssignmentExpression : LeftHandSideExpression
	 * = AssignmentExpression is evaluated as follows: <br>
	 * 1. Let lref be the result of evaluating LeftHandSideExpression.<br>
	 * 2. Let rref be the result of evaluating AssignmentExpression.<br>
	 * 3. Let rval be GetValue(rref).<br>
	 * 4. Throw a SyntaxError exception if the following conditions are all true: • Type(lref) is
	 * Reference is true • IsStrictReference(lref) is true • Type(GetBase(lref)) is Enviroment
	 * Record • GetReferencedName(lref) is either "eval" or "arguments"<br>
	 * 5. Call PutValue(lref, rval). 6. Return rval.
	 * <p>
	 * NOTE When an assignment occurs within strict mode code, its LeftHandSide must not evaluate to
	 * an unresolvable reference. If it does a ReferenceError exception is thrown upon assignment.
	 * The LeftHandSide also may not be a reference to a data property with the attribute value
	 * {[[Writable]]:false}, to an accessor property with the attribute value {[[Set]]:undefined},
	 * nor to a non-existent property of an object whose [[Extensible]] internal property has the
	 * value false. In these cases a TypeError exception is thrown. * */
	XFSTORE_D {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VFSTORE_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsA2X.XFSTORE_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BaseObject value = ExecProcess.vmEnsureDetached(ctx, argumentB);

			ctx.contextSetMutableBinding(argumentA, value, false);

			return store.execReturn(ctx, value);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public InstructionIA instruction(final BaseObject constantArgumentA, final ModifierArgument defaultArgumentA, final ModifierArgument argumentB, final int constant) {

			/** Don't really care about constant for this OP */
			// if (constant == 0) {
			return new IA2_VFSTORE_SB_X_NN_NXT(constantArgumentA.baseToString(), argumentB);
			// }

			// return super.instruction(constantArgumentA, defaultArgumentA,
			// argumentB, constant);
		}

		@Override
		public InstructionIA instruction(final BaseObject constantArgumentA,
				final ModifierArgument defaultArgumentA,
				final ModifierArgument argumentB,
				final int constant,
				final ResultHandler store) {

			/** Don't really care about constant for this OP */
			// if (constant == 0) {
			return store == ResultHandler.FA_BNN_NXT
				? new IA2_VFSTORE_SB_X_NN_NXT(constantArgumentA.baseToString(), argumentB)
				: new IA2_VFSTORE_SB_X_XX_XXX(constantArgumentA.baseToString(), argumentB, store);
			// }

			// return super.instruction(constantArgumentA, defaultArgumentA,
			// argumentB, constant, store, state);
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}

	},
	/** 11.6.3 Applying the Additive Operators ( +,-) to Numbers
	 * <p>
	 * The + operator performs addition when applied to two operands of numeric type, producing the
	 * sum of the operands. The - operator performs subtraction, producing the difference of two
	 * numeric operands.
	 * <p>
	 * Addition is a commutative operation, but not always associative.
	 * <p>
	 * The result of an addition is determined using the rules of IEEE 754 double-precision
	 * arithmetic:
	 * <p>
	 * • If either operand is NaN, the result is NaN.
	 * <p>
	 * • The sum of two infinities of opposite sign is NaN.
	 * <p>
	 * • The sum of two infinities of the same sign is the infinity of that sign.
	 * <p>
	 * • The sum of an infinity and a finite value is equal to the infinite operand.
	 * <p>
	 * • The sum of two negative zeros is −0. The sum of two positive zeros, or of two zeros of
	 * opposite sign, is +0.
	 * <p>
	 * • The sum of a zero and a nonzero finite value is equal to the nonzero operand.
	 * <p>
	 * • The sum of two nonzero finite values of the same magnitude and opposite sign is +0.
	 * <p>
	 * • In the remaining cases, where neither an infinity, nor a zero, nor NaN is involved, and the
	 * operands have the same sign or have different magnitudes, the sum is computed and rounded to
	 * the nearest representable value using IEEE 754 round-to-nearest mode. If the magnitude is too
	 * large to represent, the operation overflows and the result is then an infinity of appropriate
	 * sign. The ECMAScript language requires support of gradual underflow as defined by IEEE 754.
	 * <p>
	 * The - operator performs subtraction when applied to two operands of numeric type, producing
	 * the difference of its operands; the left operand is the minuend and the right operand is the
	 * subtrahend. Given numeric operands a and b, it is always the case that a–b produces the same
	 * result as a+(–b).
	 * <p>
	 * <p>
	 *
	 * 11.6.1 The Addition operator ( + )
	 * <p>
	 * The addition operator either performs string concatenation or numeric addition. The
	 * production AdditiveExpression : AdditiveExpression + MultiplicativeExpression is evaluated as
	 * follows: <br>
	 *
	 * 1. Evaluate AdditiveExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate MultiplicativeExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToPrimitive(Result(2)). <br>
	 * 6. Call ToPrimitive(Result(4)). <br>
	 * 7. If Type(Result(5)) is String or Type(Result(6)) is String, go to step 12. (Note that this
	 * step differs from step 3 in the comparison algorithm for the relational operators, by using
	 * or instead of and.) <br>
	 * 8. Call ToNumber(Result(5)). <br>
	 * 9. Call ToNumber(Result(6)). <br>
	 * 10. Apply the addition operation to Result(8) and Result(9). See the note below (section
	 * 11.6.3). <br>
	 * 11. Return Result(10). <br>
	 * 12. Call ToString(Result(5)). <br>
	 * 13. Call ToString(Result(6)). <br>
	 * 14. Concatenate Result(12) followed by Result(13). <br>
	 * 15. Return Result(14).
	 * <p>
	 * NOTE No hint is provided in the calls to ToPrimitive in steps 5 and 6. All native ECMAScript
	 * objects except Date objects handle the absence of a hint as if the hint Number were given;
	 * Date objects handle the absence of a hint as if the hint String were given. Host objects may
	 * handle the absence of a hint in some other manner. */
	XMADD_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMADD_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMADD_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			if (argumentA instanceof CharSequence) {
				return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
			}

			final BasePrimitive<?> additive = argumentA.baseToPrimitive(null);
			if (argumentB instanceof CharSequence) {
				return store.execReturnString(ctx, additive.stringValue() + argumentB.toString());
			}

			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(null);

			return additive instanceof CharSequence || multiplicative instanceof CharSequence
				? store.execReturnString(ctx, additive.stringValue() + multiplicative.stringValue())
				: additive == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, multiplicative)
					: multiplicative == BasePrimitiveNumber.NAN
						? store.execReturn(ctx, additive)
						: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** The production A : A @ B, where @ is one of the bitwise operators in the productions above,
	 * is evaluated as follows:
	 * <p>
	 * 1. Evaluate A. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate B. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToInt32(Result(2)). <br>
	 * 6. Call ToInt32(Result(4)). <br>
	 * 7. Apply the bitwise operator @ to Result(5) and Result(6). The result is a signed 32 bit
	 * integer.<br>
	 * 8. Return Result(7). */
	XMAND_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBAND_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBAND_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & valueB);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** The production A : A @ B, where @ is one of the bitwise operators in the productions above,
	 * is evaluated as follows:
	 * <p>
	 * 1. Evaluate A. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate B. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToInt32(Result(2)). <br>
	 * 6. Call ToInt32(Result(4)). <br>
	 * 7. Apply the bitwise operator @ to Result(5) and Result(6). The result is a signed 32 bit
	 * integer.<br>
	 * 8. Return Result(7). */
	XMBOR_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBOR_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBOR_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA | valueB);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.7.1 The Left Shift Operator ( << )
	 * <p>
	 * Performs a bitwise left shift operation on the left operand by the amount specified by the
	 * right operand.
	 * <p>
	 * The production ShiftExpression : ShiftExpression << AdditiveExpression is evaluated as
	 * follows: <br>
	 * 1. Evaluate ShiftExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate AdditiveExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToInt32(Result(2)). <br>
	 * 6. Call ToUint32(Result(4)). <br>
	 * 7. Mask out all but the least significant 5 bits of Result(6), that is, compute Result(6) &
	 * 0x1F.<br>
	 * 8. Left shift Result(5) by Result(7) bits. The result is a signed 32 bit integer. <br>
	 * 9. Return Result(8). <br>
	 */
	XMBSHL_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBSHL_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBSHL_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (additive & 0x1F));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.7.2 The Signed Right Shift Operator ( >> )
	 * <p>
	 * Performs a sign-filling bitwise right shift operation on the left operand by the amount
	 * specified by the right operand.
	 * <p>
	 * The production ShiftExpression : ShiftExpression >> AdditiveExpression is evaluated as
	 * follows: <br>
	 * 1. Evaluate ShiftExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate AdditiveExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToInt32(Result(2)). <br>
	 * 6. Call ToUint32(Result(4)). <br>
	 * 7. Mask out all but the least significant 5 bits of Result(6), that is, compute Result(6) &
	 * 0x1F.<br>
	 * 8. Perform sign-extending right shift of Result(5) by Result(7) bits. The most significant
	 * bit is propagated. The result is a signed 32 bit integer.<br>
	 * 9. Return Result(8). <br>
	 */
	XMBSHRS_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBSHRS_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBSHRS_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (additive & 0x1F));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.7.3 The Unsigned Right Shift Operator ( >>> )
	 * <p>
	 * Performs a zero-filling bitwise right shift operation on the left operand by the amount
	 * specified by the right operand.
	 * <p>
	 * The production ShiftExpression : ShiftExpression >>> AdditiveExpression is evaluated as
	 * follows: <br>
	 * 1. Evaluate ShiftExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate AdditiveExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToUint32(Result(2)). <br>
	 * 6. Call ToUint32(Result(4)). <br>
	 * 7. Mask out all but the least significant 5 bits of Result(6), that is, compute Result(6) &
	 * 0x1F.<br>
	 * 8. Perform zero-filling right shift of Result(5) by Result(7) bits. Vacated bits are filled
	 * with zero. The result is an unsigned 32 bit integer. <br>
	 * 9. Return Result(8). <br>
	 */
	XMBSHRU_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBSHRU_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBSHRU_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (additive & 0x1F));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** The production A : A @ B, where @ is one of the bitwise operators in the productions above,
	 * is evaluated as follows:
	 * <p>
	 * 1. Evaluate A. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate B. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToInt32(Result(2)). <br>
	 * 6. Call ToInt32(Result(4)). <br>
	 * 7. Apply the bitwise operator @ to Result(5) and Result(6). The result is a signed 32 bit
	 * integer.<br>
	 * 8. Return Result(7). */
	XMBXOR_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMBXOR_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMBXOR_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA ^ valueB);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.INTEGER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.5.2 Applying the / Operator
	 * <p>
	 * The / operator performs division, producing the quotient of its operands. The left operand is
	 * the dividend and the right operand is the divisor. ECMAScript does not perform integer
	 * division. The operands and result of all division operations are double-precision
	 * floating-point numbers. The result of division is determined by the specification of IEEE 754
	 * arithmetic:
	 * <p>
	 * • If either operand is NaN, the result is NaN.
	 * <p>
	 * • The sign of the result is positive if both operands have the same sign, negative if the
	 * operands have different signs.
	 * <p>
	 * • Division of an infinity by an infinity results in NaN.
	 * <p>
	 * • Division of an infinity by a zero results in an infinity. The sign is determined by the
	 * rule already stated above.
	 * <p>
	 * • Division of an infinity by a non-zero finite value results in a signed infinity. The sign
	 * is determined by the rule already stated above.
	 * <p>
	 * • Division of a finite value by an infinity results in zero. The sign is determined by the
	 * rule already stated above.
	 * <p>
	 * • Division of a zero by a zero results in NaN; division of zero by any other finite value
	 * results in zero, with the sign determined by the rule already stated above.
	 * <p>
	 * • Division of a non-zero finite value by a zero results in a signed infinity. The sign is
	 * determined by the rule already stated above.
	 * <p>
	 * • In the remaining cases, where neither an infinity, nor a zero, nor NaN is involved, the
	 * quotient is computed and rounded to the nearest representable value using IEEE 754
	 * round-to-nearest mode. If the magnitude is too large to represent, the operation overflows;
	 * the result is then an infinity of appropriate sign. If the magnitude is too small to
	 * represent, the operation underflows and the result is a zero of the appropriate sign. The
	 * ECMAScript language requires support of gradual underflow as defined by IEEE 754.
	 * <p>
	 * <p>
	 * The production MultiplicativeExpression : MultiplicativeExpression @ UnaryExpression, where @
	 * stands for one of the operators in the above definitions, is evaluated as follows:
	 * <p>
	 * 1. Evaluate MultiplicativeExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate UnaryExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToNumber(Result(2)). <br>
	 * 6. Call ToNumber(Result(4)). <br>
	 * 7. Apply the specified operation (*, /, or %) to Result(5) and Result(6). See the notes below
	 * (sections 11.5.1, 11.5.2, 11.5.3). <br>
	 * 8. Return Result(7). <br>
	 */
	XMDIV_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMDIV_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMDIV_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
			}

			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}

			return store.execReturnNumeric(ctx, dividend.doubleValue() / divisor.doubleValue());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.5.3 Applying the % Operator
	 * <p>
	 * The % operator yields the remainder of its operands from an implied division; the left
	 * operand is the dividend and the right operand is the divisor.
	 * <p>
	 * NOTE In C and C++, the remainder operator accepts only integral operands; in ECMAScript, it
	 * also accepts floating-point operands.
	 * <p>
	 * The result of a floating-point remainder operation as computed by the % operator is not the
	 * same as the “remainder” operation defined by IEEE 754. The IEEE 754 “remainder” operation
	 * computes the remainder from a rounding division, not a truncating division, and so its
	 * behaviour is not analogous to that of the usual integer remainder operator. Instead the
	 * ECMAScript language defines % on floating-point operations to behave in a manner analogous to
	 * that of the Java integer remainder operator; this may be compared with the C library function
	 * fmod.
	 * <p>
	 * The result of a ECMAScript floating-point remainder operation is determined by the rules of
	 * IEEE arithmetic:
	 * <p>
	 * • If either operand is NaN, the result is NaN.
	 * <p>
	 * • The sign of the result equals the sign of the dividend.
	 * <p>
	 * • If the dividend is an infinity, or the divisor is a zero, or both, the result is NaN.
	 * <p>
	 * • If the dividend is finite and the divisor is an infinity, the result equals the dividend.
	 * <p>
	 * • If the dividend is a zero and the divisor is finite, the result is the same as the
	 * dividend.
	 * <p>
	 * • In the remaining cases, where neither an infinity, nor a zero, nor NaN is involved, the
	 * floating-point remainder r from a dividend n and a divisor d is defined by the mathematical
	 * relation r = n − (d * q) where q is an integer that is negative only if n/d is negative and
	 * positive only if n/d is positive, and whose magnitude is as large as possible without
	 * exceeding the magnitude of the true mathematical quotient of n and d.
	 * <p>
	 * <p>
	 * The production MultiplicativeExpression : MultiplicativeExpression @ UnaryExpression, where @
	 * stands for one of the operators in the above definitions, is evaluated as follows:
	 * <p>
	 * 1. Evaluate MultiplicativeExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate UnaryExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToNumber(Result(2)). <br>
	 * 6. Call ToNumber(Result(4)). <br>
	 * 7. Apply the specified operation (*, /, or %) to Result(5) and Result(6). See the notes below
	 * (sections 11.5.1, 11.5.2, 11.5.3). <br>
	 * 8. Return Result(7). <br>
	 */
	XMMOD_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMMOD_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMMOD_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
			}

			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}

			return store.execReturnNumeric(ctx, dividend.doubleValue() % divisor.doubleValue());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.5.1 Applying the * Operator
	 * <p>
	 * The * operator performs multiplication, producing the product of its operands. Multiplication
	 * is commutative. Multiplication is not always associative in ECMAScript, because of finite
	 * precision. The result of a floating-point multiplication is governed by the rules of IEEE 754
	 * double-precision arithmetic:
	 * <p>
	 * • If either operand is NaN, the result is NaN.
	 * <p>
	 * • The sign of the result is positive if both operands have the same sign, negative if the
	 * operands have different signs.
	 * <p>
	 * • Multiplication of an infinity by a zero results in NaN.
	 * <p>
	 * • Multiplication of an infinity by an infinity results in an infinity. The sign is determined
	 * by the rule already stated above.
	 * <p>
	 * • Multiplication of an infinity by a finite non-zero value results in a signed infinity. The
	 * sign is determined by the rule already stated above.
	 * <p>
	 * • In the remaining cases, where neither an infinity or NaN is involved, the product is
	 * computed and rounded to the nearest representable value using IEEE 754 round-to-nearest mode.
	 * If the magnitude is too large to represent, the result is then an infinity of appropriate
	 * sign. If the magnitude is too small to represent, the result is then a zero of appropriate
	 * sign. The ECMAScript language requires support of gradual underflow as defined by IEEE 754.
	 * <p>
	 * <p>
	 * The production MultiplicativeExpression : MultiplicativeExpression @ UnaryExpression, where @
	 * stands for one of the operators in the above definitions, is evaluated as follows:
	 * <p>
	 * 1. Evaluate MultiplicativeExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate UnaryExpression. <br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToNumber(Result(2)). <br>
	 * 6. Call ToNumber(Result(4)). <br>
	 * 7. Apply the specified operation (*, /, or %) to Result(5) and Result(6). See the notes below
	 * (sections 11.5.1, 11.5.2, 11.5.3). <br>
	 * 8. Return Result(7). <br>
	 */
	XMMUL_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMMUL_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMMUL_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}

			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}

			return store.execReturnNumeric(ctx, multiplicative.doubleValue() * unary.doubleValue());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	XMPOW_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMPOW_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMPOW_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}

			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}

			return store.execReturnNumeric(ctx, Math.pow(multiplicative.doubleValue(), unary.doubleValue()));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** 11.6.3 Applying the Additive Operators ( +,-) to Numbers
	 * <p>
	 * The + operator performs addition when applied to two operands of numeric type, producing the
	 * sum of the operands. The - operator performs subtraction, producing the difference of two
	 * numeric operands.
	 * <p>
	 * Addition is a commutative operation, but not always associative.
	 * <p>
	 * The result of an addition is determined using the rules of IEEE 754 double-precision
	 * arithmetic:
	 * <p>
	 * • If either operand is NaN, the result is NaN.
	 * <p>
	 * • The sum of two infinities of opposite sign is NaN.
	 * <p>
	 * • The sum of two infinities of the same sign is the infinity of that sign.
	 * <p>
	 * • The sum of an infinity and a finite value is equal to the infinite operand.
	 * <p>
	 * • The sum of two negative zeros is −0. The sum of two positive zeros, or of two zeros of
	 * opposite sign, is +0.
	 * <p>
	 * • The sum of a zero and a nonzero finite value is equal to the nonzero operand.
	 * <p>
	 * • The sum of two nonzero finite values of the same magnitude and opposite sign is +0.
	 * <p>
	 * • In the remaining cases, where neither an infinity, nor a zero, nor NaN is involved, and the
	 * operands have the same sign or have different magnitudes, the sum is computed and rounded to
	 * the nearest representable value using IEEE 754 round-to-nearest mode. If the magnitude is too
	 * large to represent, the operation overflows and the result is then an infinity of appropriate
	 * sign. The ECMAScript language requires support of gradual underflow as defined by IEEE 754.
	 * <p>
	 * The - operator performs subtraction when applied to two operands of numeric type, producing
	 * the difference of its operands; the left operand is the minuend and the right operand is the
	 * subtrahend. Given numeric operands a and b, it is always the case that a–b produces the same
	 * result as a+(–b).
	 * <p>
	 * <p>
	 *
	 *
	 *
	 * 11.6.2 The Subtraction Operator ( - )
	 * <p>
	 * The production AdditiveExpression : AdditiveExpression - MultiplicativeExpression is
	 * evaluated as follows:
	 * <p>
	 * 1. Evaluate AdditiveExpression. <br>
	 * 2. Call GetValue(Result(1)). <br>
	 * 3. Evaluate MultiplicativeExpression.<br>
	 * 4. Call GetValue(Result(3)). <br>
	 * 5. Call ToNumber(Result(2)). <br>
	 * 6. Call ToNumber(Result(4)). <br>
	 * 7. Apply the subtraction operation to Result(5) and Result(6). See the note below (section
	 * 11.6.3).<br>
	 * 8. Return Result(7).
	 * <p>
	 */
	XMSUB_T {

		@Override
		public OperationA2X execNativeResult() {

			return OperationsS2X.VMSUB_N;
		}

		@Override
		public OperationA2X execStackResult() {

			return OperationsS2X.VMSUB_D;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {

			final BasePrimitive<?> additive = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			// final BasePrimitiveNumber additive = argumentA.baseToNumber();
			if (additive == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, additive);
			}

			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			// final BasePrimitiveNumber multiplicative =
			// argumentB.baseToNumber();
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			return store.execReturnNumeric(ctx, additive.doubleValue() - multiplicative.doubleValue());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public final boolean isConstantForArguments() {

			return true;
		}
	},
	/** macro ACALL RT, a, b */
	ZTCALLM {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseObject argumentA = ctx.rb4CT;

			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}

			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);

			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							(argumentA == ctx
								? "Context has no property called "
								: Format.Compact.baseObject(argumentA) + " has no property called ") //
									+ Format.Compact.baseObject(argumentB));
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToString() + ", class=" + candidate.getClass().getName());
			}

			if (argumentC instanceof final BaseArray baseArray) {
				return callee.execCallPrepare(ctx, argumentA, store, false, baseArray);
			}

			if (argumentC instanceof final NamedToIndexMapper mapper) {
				return ctx.vmCall_Generic_MapArgs(callee, argumentA, mapper, store);
			}
			return ctx.vmRaise(
					"Invalid arguments argument: key=" + argumentB.baseToString() + ", class=" + callee.getClass().getName() + ", argumentsClass: "
							+ argumentC.getClass().getSimpleName());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** macro ACALLO RT, a, b */
	ZTCALLO {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseObject argumentA = ctx.rb4CT;

			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}

			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);

			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							(argumentA == ctx
								? "Context has no property called "
								: Format.Compact.baseObject(argumentA) + " has no property called ") //
									+ Format.Compact.baseObject(argumentB));
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToString() + ", class=" + candidate.getClass().getName());
			}
			return callee.execCallPrepare(ctx, argumentA, store, false, argumentC);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},
	/** macro ASTORE RT, a, b */
	ZTSTORE {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseObject value = ExecProcess.vmEnsureNative(argumentC);
			return ctx.rb4CT.vmPropertyDefine(ctx, argumentB, value, store);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final boolean isConstantForArguments() {

			return false;
		}
	},;

	@Override

	public OperationA2X execDirectResult() {

		return this;
	}

	@Override

	public OperationA2X execNativeResult() {

		return this;
	}

	@Override

	public OperationA2X execStackResult() {

		return this;
	}

	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();

	Instruction instructionCached(//
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final int constant,
			final ResultHandler store) {

		return InstructionA2X.instructionCached(this.instruction(argumentA, argumentB, constant, store));
	}
}
