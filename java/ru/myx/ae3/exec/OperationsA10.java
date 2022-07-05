/**
 *
 */
package ru.myx.ae3.exec;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseNativeArray;
import ru.myx.ae3.base.BaseNumber;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Format;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA10;

/** @author myx */
public enum OperationsA10 implements OperationA10 {

	/**
	 *
	 */
	XBCVT_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return store.execReturn(ctx, argumentA.baseToBoolean());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.BOOLEAN;
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XBNOT_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return store.execReturnBoolean(ctx, !argumentA.baseToJavaBoolean());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.BOOLEAN;
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XCARRAYX_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return store.execReturn(
					ctx,
					constant == 1
						? new BaseNativeArray(argumentA)
						: new BaseNativeArray(constant, argumentA));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.ARRAY;
		}
		
	},
	/**
	 *
	 */
	XCOBJECT_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA == BaseObject.UNDEFINED) {
				return ctx.vmRaise("Constructor is undefined!");
			}
			if (argumentA.baseIsPrimitive()) {
				return ctx.vmRaise("Primitive class cannot be a constructor!");
			}
			final BaseObject result;
			{
				final BaseFunction constructor = argumentA.baseConstruct();
				if (constructor != null) {
					final BaseObject prototype = constructor.baseConstructPrototype();
					// assert prototype != null : "NULL prototype, constructor class: " +
					// constructor.getClass().getName();
					result = BaseObject.createObject(prototype);
					result.baseDefine(BaseString.STR_CONSTRUCTOR, constructor, BaseProperty.ATTRS_MASK_NNN);
				} else //
				if (argumentA instanceof BaseFunction) {
					final BaseObject prototype = argumentA.baseGet(BaseString.STR_PROTOTYPE, BaseObject.PROTOTYPE);
					// assert prototype != null : "NULL prototype, constructor class: " +
					// constructor.getClass().getName();
					result = BaseObject.createObject(prototype);
					result.baseDefine(BaseString.STR_CONSTRUCTOR, argumentA, BaseProperty.ATTRS_MASK_NNN);
				} else //
				{
					result = BaseObject.createObject(argumentA);
					result.baseDefine(BaseString.STR_CONSTRUCTOR, BaseFunction.RETURN_UNDEFINED, BaseProperty.ATTRS_MASK_NNN);
				}
			}
			
			if (constant > 0) {
				final BaseObject[] stack = ctx.stackRaw();
				final int rASP = ctx.ri0ASP;
				for (int i = constant * 2; i > 0; i -= 2) {
					final BaseObject key = stack[rASP - i - 1];
					final BaseObject value = stack[rASP - i - 0];
					if (key instanceof final CharSequence charSequence) {
						result.baseDefine(charSequence, value, BaseProperty.ATTRS_MASK_WED);
					} else {
						result.baseDefine(key.baseToJavaString(), value, BaseProperty.ATTRS_MASK_WED);
					}
					stack[rASP - i - 1] = null;
					stack[rASP - i - 0] = null;
				}
				ctx.ri0ASP -= constant * 2;
			}
			return store.execReturn(ctx, result);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return constant * 2;
		}
	},
	/**
	 *
	 */
	XCSCOPE_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			assert !argumentA.baseIsPrimitive() : "Must not be a primitive: " + Format.Describe.toEcmaSource(argumentA, "");
			final BaseObject result = ctx.vmScopeCreateMixIn(argumentA);
			if (constant == 0) {
				return store.execReturn(ctx, result);
			}
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant * 2; i > 0; i -= 2) {
				final BaseObject key = stack[rASP - i - 1];
				final BaseObject value = stack[rASP - i - 0];
				if (key instanceof final CharSequence charSequence) {
					result.baseDefine(charSequence, value, BaseProperty.ATTRS_MASK_WED);
				} else {
					result.baseDefine(key.baseToJavaString(), value, BaseProperty.ATTRS_MASK_WED);
				}
				stack[rASP - i - 1] = null;
				stack[rASP - i - 0] = null;
			}
			ctx.ri0ASP -= constant * 2;
			return store.execReturn(ctx, result);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return constant * 2;
		}
	},
	/**
	 *
	 */
	XFCALLS {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final BaseFunction callee = argumentA.baseCall();
			if (callee == null) {
				if (argumentA == BaseObject.UNDEFINED) {
					return ctx.vmRaise("Function is undefined");
				}
				return ctx.vmRaise("Not a function: class=" + argumentA.getClass().getName());
			}
			return ctx.vmCallS(callee, ctx.contextImplicitThisValue(), constant, store);
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
	/** argument is either a local variable name either an array of variable names */
	XFDECLARE_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof CharSequence) {
				ctx.contextCreateMutableBinding(argumentA.baseToString(), BaseObject.UNDEFINED, false);
				return store.execReturnUndefined(ctx);
			}
			final BaseArray array = argumentA.baseArray();
			if (array != null) {
				final int length = array.length();
				for (int i = 0; i < length; ++i) {
					final BaseObject name = array.baseGet(i, null);
					assert name != null;
					ctx.contextCreateMutableBinding(name.baseToString(), BaseObject.UNDEFINED, false);
				}
				return store.execReturnUndefined(ctx);
			}
			return ctx.vmRaise("Invalid argument type: class=" + argumentA.getClass().getName());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.UNDEFINED;
		}
		
	},
	/**
	 *
	 */
	XFDELETE_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return store.execReturnBoolean(ctx, ctx.contextDeleteBinding(argumentA.baseToJavaString()));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.BOOLEAN;
		}
		
	},
	/**
	 *
	 */
	XFLOAD_P {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VFLOAD_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VFLOAD_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return store.execReturn(ctx, argumentA);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public InstructionIA instruction(final BaseObject constantArgumentA, final ModifierArgument originalArgumentA, final int constant, final ResultHandler store) {
			
			assert constant == 0 //
					: this.name() + " constant must be 0";
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return new IA10_XLOAD_P_O_0_NN_NXT(constantArgumentA);
			}
			if (store == ResultHandler.FB_BSN_NXT) {
				return new IA10_XLOAD_P_O_0_SN_NXT(constantArgumentA);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return new IA10_XLOAD_P_O_0_NN_RET(constantArgumentA);
			}
			return super.instruction(constantArgumentA, originalArgumentA, constant, store);
		}
		
		@Override
		public InstructionIA instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {
			
			if (constant == 0) {
				if (store == ResultHandler.FB_BSN_NXT) {
					final BasePrimitiveString framePropertyName = argumentA.argumentAccessFramePropertyName();
					if (framePropertyName != null) {
						return new IA10_XLOAD_P_F_0_SN_NXT(framePropertyName);
					}
				}
				if (store == ResultHandler.FC_PNN_RET) {
					final BasePrimitiveString framePropertyName = argumentA.argumentAccessFramePropertyName();
					if (framePropertyName != null) {
						return new IA10_XLOAD_P_F_0_NN_RET(framePropertyName);
					}
				}
			}
			return super.instruction(argumentA, constant, store);
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XFOTDONE {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			try {
				final BaseFunction result = ctx.execOutputReplace(
						argumentA == BaseObject.NULL
							? null
							: (BaseFunction) argumentA);
				return result == null
					? store.execReturn(ctx, BaseString.EMPTY)
					: store.execReturnString(ctx, result.toString());
			} catch (final ClassCastException e) {
				return ctx.vmRaise("Function object was expected, class: " + argumentA.getClass().getSimpleName());
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.STRING;
		}
	},
	/** add & store then get */
	XFSADDGET_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitiveNumber result;
			if (argumentB instanceof final BasePrimitiveString key) {
				final BasePrimitiveNumber leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			} else //
			if (argumentB instanceof CharSequence) {
				final String key = argumentB.toString();
				final BasePrimitiveNumber leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			} else {
				final BasePrimitiveString key = argumentB.baseToString();
				final BasePrimitiveNumber leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			}
			return store.execReturn(ctx, result);
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
	/** get then add & store */
	XFSGETADD_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitiveNumber leftHand;
			if (argumentB instanceof final BasePrimitiveString key) {
				leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			} else //
			if (argumentB instanceof CharSequence) {
				final String key = argumentB.toString();
				leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			} else {
				final BasePrimitiveString key = argumentB.baseToString();
				leftHand = ctx.contextGetBindingValue(key, false).baseToNumber();
				final BasePrimitiveNumber result = leftHand == BasePrimitiveNumber.NAN
					? leftHand
					: leftHand.baseIsPrimitiveInteger()
						? Base.forLong(leftHand.longValue() + constant)
						: Base.forDouble(leftHand.doubleValue() + constant);
				ctx.contextSetMutableBinding(key, result, false);
			}
			return store.execReturn(ctx, leftHand);
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
	XITRNEXT {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final IteratorImpl impl = ctx.ri11II;
			if (impl == null || !impl.next(ctx, ctx.ri12IA, argumentA.baseToJavaString())) {
				ctx.ri12IA = null;
				ctx.ri11II = null;
				ctx.ri13IV = null;
				return store.execReturnFalse(ctx);
			}
			return store.execReturnTrue(ctx);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.BOOLEAN;
		}
	},
	/** TODO: check - ignores 'store' */
	XITRPREPK {
		
		private final IteratorImpl IMPL_BASE_ARRAY_KEY = IteratorImplBaseArrayKey.INSTANCE;
		
		private final IteratorImpl IMPL_ITERATOR = IteratorImplIterator.INSTANCE;
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA == BaseObject.NULL || argumentA == BaseObject.UNDEFINED) {
				ctx.ri12IA = BaseObject.NULL;
				ctx.ri11II = null;
				ctx.ri13IV = null;
				return null;
			}
			if (argumentA.baseIsPrimitive()) {
				if (argumentA.baseToBoolean() == BaseObject.FALSE) {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
					return null;
				}
				return ctx.vmRaise("Not an iterable! argumentA is a primitive: class=" + argumentA.getClass().getName());
			}
			{
				final BaseArray array = argumentA.baseArray();
				if (array != null) {
					final int length = array.length();
					if (length > 0) {
						ctx.ri12IA = array;
						ctx.ri11II = this.IMPL_BASE_ARRAY_KEY;
						ctx.ri13IV = BasePrimitiveNumber.ZERO;
						/** IntUpSequence <code>
							ctx.r0R0 = Base.forInteger( length );
							ctx.r1R1 = this.IMPL_BASE_ARRAY_KEY;
							// goes like ++index
							ctx.r2R2 = BasePrimitiveNumber.MONE;
						 * </code> */
					} else {
						ctx.ri12IA = BaseObject.NULL;
						ctx.ri11II = null;
						ctx.ri13IV = null;
					}
					return null;
				}
			}
			{
				final Iterator<String> keys = Base.keys(argumentA);
				assert keys != null : "NULL java object (iterator)";
				if (keys.hasNext()) {
					ctx.ri12IA = argumentA;
					ctx.ri11II = this.IMPL_ITERATOR;
					ctx.ri13IV = keys;
				} else {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
				}
				return null;
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
	},
	/** TODO: check - ignores 'store' */
	XITRPREPV {
		
		private final IteratorImpl IMPL_BASE_ARRAY_VALUE = IteratorImplBaseArrayValue.INSTANCE;
		
		private final IteratorImpl IMPL_BASE_ITERATOR = IteratorImplIterator.INSTANCE;
		
		private final IteratorImpl IMPL_BASE_ITERATOR_VALUE = IteratorImplBaseIteratorValue.INSTANCE;
		
		private final BasePrimitiveString ORDER = Base.forString("$ORDER");
		
		@SuppressWarnings("boxing")
		private final Integer ZERO = 0;
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA == BaseObject.NULL || argumentA == BaseObject.UNDEFINED) {
				ctx.ri12IA = BaseObject.NULL;
				ctx.ri11II = null;
				ctx.ri13IV = null;
				return null;
			}
			if (argumentA.baseIsPrimitive()) {
				if (argumentA.baseToBoolean() == BaseObject.FALSE) {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
					return null;
				}
				return ctx.vmRaise("Not an iterable! argumentA is a primitive: class=" + argumentA.getClass().getName());
			}
			{
				final BaseArray array = argumentA.baseArray();
				if (array != null) {
					ctx.ri12IA = array;
					ctx.ri11II = this.IMPL_BASE_ARRAY_VALUE;
					ctx.ri13IV = this.ZERO;
					return null;
				}
			}
			{
				final Object baseValue = argumentA.baseValue();
				if (baseValue instanceof Map<?, ?>) {
					// old ORDER implementation - must be supported here or
					// on
					// ObjectCreation stage
					// Map
					ctx.ri11II = this.IMPL_BASE_ITERATOR_VALUE;
					final Object order = argumentA.baseGet(this.ORDER, BaseObject.UNDEFINED).baseValue();
					if (order != null) {
						if (order instanceof final Object[] array) {
							if (array.length + 1 == ((Map<?, ?>) baseValue).size()) {
								ctx.ri13IV = Arrays.asList((Object[]) order).iterator();
								return null;
							}
						} else //
						if (order instanceof final Collection<?> collection) {
							if (collection.size() + 1 == ((Map<?, ?>) baseValue).size()) {
								ctx.ri13IV = collection.iterator();
								return null;
							}
						}
					}
					/** pass-through */
				} else //
				if (baseValue != null && baseValue != argumentA) {
					if (baseValue instanceof final Iterable<?> iterable) {
						/** only for ITRPREPV - collection values */
						ctx.ri11II = this.IMPL_BASE_ITERATOR;
						ctx.ri13IV = iterable.iterator();
						return null;
					}
					if (baseValue instanceof Iterator) {
						/** only for ITRPREPV - collection values */
						ctx.ri11II = this.IMPL_BASE_ITERATOR;
						ctx.ri13IV = baseValue;
						return null;
					}
					if (baseValue instanceof final BaseObject baseObject) {
						return this.execute(ctx, baseObject, constant, store);
					}
					{
						return this.executeJava(ctx, baseValue, constant, store);
					}
				}
			}
			{
				final Iterator<String> keys = Base.keys(argumentA);
				assert keys != null : "NULL java object (iterator)";
				if (keys.hasNext()) {
					ctx.ri12IA = argumentA;
					ctx.ri11II = this.IMPL_BASE_ITERATOR_VALUE;
					ctx.ri13IV = keys;
				} else {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
				}
				return null;
			}
		}
		
		private final ExecStateCode executeJava(final ExecProcess ctx, final Object argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof final Object[] array) {
				/** only for ITRPREPV - java array values */
				ctx.ri11II = this.IMPL_BASE_ITERATOR;
				ctx.ri13IV = Arrays.asList(array).iterator();
				return null;
			}
			if (argumentA instanceof final Value<?> valueObject) {
				final Object baseValue = valueObject.baseValue();
				if (baseValue != null && baseValue != argumentA) {
					if (baseValue instanceof final BaseObject baseObject) {
						return this.execute(ctx, baseObject, constant, store);
					}
					if (baseValue instanceof final Object[] array) {
						/** only for ITRPREPV - java array values */
						ctx.ri11II = this.IMPL_BASE_ITERATOR;
						ctx.ri13IV = Arrays.asList(array).iterator();
						return null;
					}
					if (baseValue instanceof final Map<?, ?> baseMap) {
						// old ORDER implementation - must be supported here or
						// on
						// ObjectCreation stage
						// Map
						ctx.ri11II = this.IMPL_BASE_ITERATOR_VALUE;
						final Object order = baseMap.get("$ORDER");
						if (order != null) {
							if (order instanceof final Object[] array) {
								if (array.length + 1 == ((Map<?, ?>) baseValue).size()) {
									ctx.ri13IV = Arrays.asList((Object[]) order).iterator();
									return null;
								}
							} else //
							if (order instanceof final Collection<?> collection) {
								if (collection.size() + 1 == ((Map<?, ?>) baseValue).size()) {
									ctx.ri13IV = collection.iterator();
									return null;
								}
							}
						}
						{
							ctx.ri13IV = Base.forUnknown(baseMap.keySet().iterator());
							return null;
						}
					}
					if (baseValue instanceof final Iterable<?> iterable) {
						/** only for ITRPREPV - collection values */
						ctx.ri11II = this.IMPL_BASE_ITERATOR;
						ctx.ri13IV = iterable.iterator();
						return null;
					}
				}
			}
			{
				ctx.ri12IA = BaseObject.NULL;
				ctx.ri11II = null;
				ctx.ri13IV = null;
				return null;
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
	},
	/** macro for: +a */
	ZCVTN_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VCVTN_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VCVTN_D;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof BaseNumber) {
				return store.execReturn(ctx, argumentA);
			}
			{
				final BasePrimitive<?> primitive = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
				return primitive instanceof BaseNumber
					? store.execReturn(ctx, primitive)
					: store.execReturnNumeric(ctx, primitive.doubleValue());
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** macro for: MADD '', a */
	ZCVTS_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VCVTS_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VCVTS_D;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof BaseString) {
				return store.execReturn(ctx, argumentA);
			}
			{
				final BasePrimitive<?> primitive = argumentA.baseToPrimitive(ToPrimitiveHint.STRING);
				return primitive instanceof BaseString
					? store.execReturn(ctx, primitive)
					: store.execReturnString(ctx, primitive.stringValue());
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.STRING;
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** TODO: check - ignores 'store' */
	ZITRPREP7 {
		
		private final IteratorImpl IMPL_BASE_ARRAY_VALUE = IteratorImplBaseArrayValue.INSTANCE;
		
		private final IteratorImpl IMPL_BASE_ITERATOR = IteratorImplIterator.INSTANCE;
		
		private final BasePrimitiveString ORDER = Base.forString("$ORDER");
		
		@SuppressWarnings("boxing")
		private final Integer ZERO = 0;
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			if (argumentA == BaseObject.NULL || argumentA == BaseObject.UNDEFINED) {
				ctx.ri12IA = BaseObject.NULL;
				ctx.ri11II = null;
				ctx.ri13IV = null;
				return null;
			}
			if (argumentA.baseIsPrimitive()) {
				if (argumentA.baseToBoolean() == BaseObject.FALSE) {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
					return null;
				}
				return ctx.vmRaise("Not an iterable! argumentA is a primitive: class=" + argumentA.getClass().getName());
			}
			{
				final BaseArray array = argumentA.baseArray();
				if (array != null) {
					ctx.ri12IA = array;
					ctx.ri11II = this.IMPL_BASE_ARRAY_VALUE;
					ctx.ri13IV = this.ZERO;
					return null;
				}
			}
			{
				final Object baseValue = argumentA.baseValue();
				if (baseValue instanceof final Iterable<?> iterable) {
					/** only for ITRPREPV - collection values */
					ctx.ri11II = this.IMPL_BASE_ITERATOR;
					ctx.ri13IV = iterable.iterator();
					return null;
				}
				if (baseValue instanceof Map<?, ?>) {
					// old ORDER implementation - must be supported here or on
					// ObjectCreation stage
					// Map
					ctx.ri11II = this.IMPL_BASE_ITERATOR;
					final Object order = argumentA.baseGet(this.ORDER, BaseObject.UNDEFINED).baseValue();
					if (order != null) {
						if (order instanceof final Object[] array) {
							if (array.length + 1 == ((Map<?, ?>) baseValue).size()) {
								ctx.ri13IV = Arrays.asList((Object[]) order).iterator();
								return null;
							}
						} else //
						if (order instanceof final Collection<?> collection) {
							if (collection.size() + 1 == ((Map<?, ?>) baseValue).size()) {
								ctx.ri13IV = collection.iterator();
								return null;
							}
						}
					}
				}
			}
			{
				final Iterator<String> keys = Base.keys(argumentA);
				assert keys != null : "NULL java object (iterator), class=" + argumentA.getClass().getName();
				if (keys.hasNext()) {
					ctx.ri12IA = argumentA;
					ctx.ri11II = this.IMPL_BASE_ITERATOR;
					ctx.ri13IV = keys;
				} else {
					ctx.ri12IA = BaseObject.NULL;
					ctx.ri11II = null;
					ctx.ri13IV = null;
				}
				return null;
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return 0;
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
	ZMAND_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VMBAND_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VMBAND_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & constant);
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
	ZMSHL_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VMBSHL_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VMBSHL_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (constant & 0x1F));
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
	ZMSHRS_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VMBSHRS_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VMBSHRS_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (constant & 0x1F));
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
	ZMSHRU_T {
		
		@Override
		public OperationA10 execNativeResult() {
			
			return OperationsS10.VMBSHRU_N;
		}
		
		@Override
		public OperationA10 execStackResult() {
			
			return OperationsS10.VMBSHRU_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (constant & 0x1F));
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
	/** macro for ACALLS RT, a, x */
	ZTCALLS {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
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
			return ctx.vmCallS(callee, argumentA, constant, store);
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
	/** macro for ADELETE RT, a */
	ZTDELETE_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BaseObject argumentA = ctx.rb4CT;
			
			return store.execReturnBoolean(ctx, argumentA.baseDelete(argumentB));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.BOOLEAN;
		}
	},
	/**
	 *
	 */
	ZTLOAD_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {
			
			return ctx.rb4CT.vmPropertyRead(ctx, argumentA, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public boolean isConstantForArguments() {
			
			return false;
		}
	},;
	
	@Override
	public OperationA10 execNativeResult() {
		
		return this;
	}
	
	@Override
	public OperationA10 execStackResult() {
		
		return this;
	}
	
	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();
	
	Instruction instructionCached(//
			final ModifierArgument argumentA,
			final int constant,
			final ResultHandler store) {
		
		return InstructionA10.instructionCached(this.instruction(argumentA, constant, store));
	}
	
	@Override
	public boolean isConstantForArguments() {
		
		return false;
	}
}
