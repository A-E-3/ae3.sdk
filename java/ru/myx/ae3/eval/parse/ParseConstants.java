/**
 *
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.eval.tokens.TokenAssignment;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenOperator;
import ru.myx.ae3.eval.tokens.TokenSyntax;
import ru.myx.ae3.eval.tokens.TokenValue;

/** @author myx */
public class ParseConstants {

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MADD = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MADD_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MAND = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MAND_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MDIV = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MDIV_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MMOD = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MMOD_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MMUL = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MMUL_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MOR = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MOR_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MSHL = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MSHL_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MSHRS = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MSHRS_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MSHRU = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MSHRU_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MSUB = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MSUB_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_ASSIGN_MXOR = new TKA_WRAP_ASSIGN_LVALUE_O2X(TKO_MXOR_D_BA_SS_S.INSTANCE);

	/**
	 *
	 */
	public static final TokenAssignment TKA_OUTPUT_A_S_S = new TKA_XOUTPUT_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BCVT_A_S_S = new TKO_BCVT_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BEQU_BA_SS_S = new TKO_BEQU_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BIN_A_S_S = new TKO_BIN_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BINSTANCEOF_A_S_S = new TKO_BINSTANCEOF_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BLESS_BA_SS_S = new TKO_BLESS_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BMORE_BA_SS_S = new TKO_BMORE_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BNEQU_BA_SS_S = new TKO_BNEQU_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BNLESS_BA_SS_S = new TKO_BNLESS_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BNMORE_BA_SS_S = new TKO_BNMORE_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BNOT_A_S_S = new TKO_BNOT_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BSEQU_BA_SS_S = new TKO_BSEQU_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_BSNEQU_BA_SS_S = new TKO_BSNEQU_BA_SS_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_DELETE_A_L_S = new TKO_DELETE_A_L_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_MADD_BA_SS = TKO_MADD_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MAND_BA_SS = TKO_MAND_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MDIV_BA_SS = TKO_MDIV_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MMOD_BA_SS = TKO_MMOD_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MMUL_BA_SS = TKO_MMUL_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MPOW_BA_SS = TKO_MPOW_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MNOT_A_S_S = TKO_MNOT_T_A_S_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MOR_BA_SS = TKO_MOR_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MSHL_BA_SS = TKO_MSHL_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MSHRS_BA_SS = TKO_MSHRS_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MSHRU_BA_SS = TKO_MSHRU_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MSUB_BA_S0 = TKO_MSUB_T_BA_S0_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MSUB_BA_SS = TKO_MSUB_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_MXOR_BA_SS = TKO_MXOR_T_BA_SS_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenInstruction TKO_POSTDEC_A_L = TKO_POSTDEC_T_A_L_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenInstruction TKO_POSTINC_A_L = TKO_POSTINC_T_A_L_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenInstruction TKO_PREDEC_A_L_S = new TKO_PREDEC_A_L_S();

	/**
	 *
	 */
	public static final TokenInstruction TKO_PREINC_A_L_S = new TKO_PREINC_A_L_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_AWAIT_A_S_S = new TKO_AWAIT_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_TYPEOF_A_S_S = new TKO_TYPEOF_A_S_S();

	/**
	 *
	 */
	public static final TokenOperator TKO_ZCVTN_A_S = TKO_ZCVTN_T_A_S_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenOperator TKO_ZCVTS_A_S = TKO_ZCVTS_T_A_S_S.INSTANCE;

	/**
	 *
	 */
	public static final TokenSyntax TKS_ASSIGNMENT = new TKS_ASSIGNMENT();

	/** Logical AND operator (&&) */
	public static final TokenSyntax TKS_EAND = new TKS_EAND();

	/** Optional chaining operator (&&) */
	public static final TokenSyntax TKS_EOCO = new TKS_EOCO();

	/** Logical OR operator (||) */
	public static final TokenSyntax TKS_EOR = new TKS_EOR();

	/** Nullish coalescing operator (??) */
	public static final TokenSyntax TKS_ENCO = new TKS_ENCO();

	/**
	 *
	 */
	public static final TokenSyntax TKS_BRACE_CLOSE = new TKS_BRACE_CLOSE();

	/**
	 *
	 */
	public static final TokenSyntax TKS_BRACE_OPEN = new TKS_BRACE_OPEN();

	/**
	 *
	 */
	public static final TokenSyntax TKS_COLON = new TKS_COLON();

	/**
	 *
	 */
	public static final TokenSyntax TKS_COMMA = new TKS_COMMA();

	/**
	 *
	 */
	public static final TokenSyntax TKS_CREATE_CLOSE = new TKS_CREATE_CLOSE();

	/**
	 *
	 */
	public static final TokenSyntax TKS_CREATE_ELEMENT = new TKS_CREATE_ELEMENT();

	/**
	 *
	 */
	public static final TokenSyntax TKS_CREATE_OPEN = new TKS_CREATE_OPEN();

	/**
	 *
	 */
	public static final TokenSyntax TKS_INDEX_CLOSE = new TKS_INDEX_CLOSE();

	/**
	 *
	 */
	public static final TokenSyntax TKS_INDEX_OPEN = new TKS_INDEX_OPEN();

	/**
	 *
	 */
	public static final TokenSyntax TKS_QUESTION_MARK = new TKS_QUESTION_MARK();

	/**
	 *
	 */
	public static final TokenSyntax TKS_QUESTION_MARK_MATCHED = new TKS_QUESTION_MARK();

	/**
	 *
	 */
	public static final TokenSyntax TKS_SEMICOLON = new TKS_SEMICOLON();

	/**
	 *
	 */
	public static final TokenValue TKV_BOOLEAN_FALSE = new TKV_LFALSE();

	/**
	 *
	 */
	public static final TokenValue TKV_BOOLEAN_TRUE = new TKV_LTRUE();

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_COBJECT_IS_NOT_AN_OPERATOR = new TKV_ERROR_A_C_E("'{' is not an operator - it creates value");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_DECLARATION_EXPECTED = new TKV_ERROR_A_C_E("Declaration expected!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_EXPRESSION_EXPECTED = new TKV_ERROR_A_C_E("Expression expected!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_TERNARY_NEEDS_CONDITION = new TKV_ERROR_A_C_E("'?:' ternary operator and requires conditional expression on left!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_BRACE_CLOSE = new TKV_ERROR_A_C_E("Unmatched ')', no corresponding '(' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_BRACE_OPEN = new TKV_ERROR_A_C_E("Unmatched '(', no corresponding ')' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_COLON = new TKV_ERROR_A_C_E("Unmatched ':', no corresponding '?' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_CREATE_CLOSE = new TKV_ERROR_A_C_E("Unmatched '}', no corresponding '{' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_CREATE_OPEN = new TKV_ERROR_A_C_E("Unmatched '{', no corresponding '}' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_INDEX_CLOSE = new TKV_ERROR_A_C_E("Unmatched ']', no corresponding '[' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_INDEX_OPEN = new TKV_ERROR_A_C_E("Unmatched '[', no corresponding ']' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_ERROR_UNMATCHED_QUESTION_MARK = new TKV_ERROR_A_C_E("Unmatched '?', no corresponding ':' found!");

	/**
	 *
	 */
	public static final TokenValue TKV_NULL = new TKV_LNULL();

	/**
	 *
	 */
	public static final TokenValue TKV_NUMBER_PINFINITY = new TKV_LCONSTN(BasePrimitiveNumber.PINF);

	/**
	 *
	 */
	public static final TokenValue TKV_NUMBER_NINFINITY = new TKV_LCONSTN(BasePrimitiveNumber.NINF);

	/**
	 *
	 */
	public static final TokenValue TKV_NUMBER_MINUSONE = new TKV_LCONSTI(BasePrimitiveNumber.MONE);

	/**
	 *
	 */
	public static final TokenValue TKV_NUMBER_NAN = new TKV_LCONSTN(BasePrimitiveNumber.NAN);

	/**
	 *
	 */
	public static final TokenValue TKV_STRING_EMPTY = new TKV_LCONSTS(BaseString.EMPTY);

	/**
	 *
	 */
	public static final TokenValue TKV_THIS = new TKV_LTHIS();

	/**
	 *
	 */
	public static final TokenValue TKV_UNDEFINED = new TKV_LUNDEFINED();

	private static final TokenValue[] VALUE_CACHE;

	private static final int VALUE_CACHE_SIZE = 32768;

	static {
		VALUE_CACHE = new TokenValue[ParseConstants.VALUE_CACHE_SIZE];
		for (int i = ParseConstants.VALUE_CACHE_SIZE - 1; i >= 0; --i) {
			ParseConstants.VALUE_CACHE[i] = new TKV_LCONSTI(Base.forInteger(i));
		}
	}

	/** @param object
	 * @return */
	public static final TokenValue getConstantValue(final BaseObject object) {

		assert object != null : "NULL java value!";
		if (object.baseIsPrimitive()) {
			if (object == BaseObject.UNDEFINED) {
				return ParseConstants.TKV_UNDEFINED;
			}
			if (object == BaseObject.TRUE) {
				return ParseConstants.TKV_BOOLEAN_TRUE;
			}
			if (object == BaseObject.FALSE) {
				return ParseConstants.TKV_BOOLEAN_FALSE;
			}
			if (object == BaseObject.NULL) {
				return ParseConstants.TKV_NULL;
			}
			if (object.baseIsPrimitiveNumber()) {
				if (object == BasePrimitiveNumber.NAN) {
					return ParseConstants.TKV_NUMBER_NAN;
				}
				if (object == BasePrimitiveNumber.MONE) {
					return ParseConstants.TKV_NUMBER_MINUSONE;
				}
				if (object == BasePrimitiveNumber.PINF) {
					return ParseConstants.TKV_NUMBER_PINFINITY;
				}
				if (object == BasePrimitiveNumber.NINF) {
					return ParseConstants.TKV_NUMBER_NINFINITY;
				}
				final BasePrimitiveNumber number = object.baseToNumber();
				final double value = number.doubleValue();
				if (value == (int) value) {
					final int i = (int) value;
					if (i >= 0 && i < ParseConstants.VALUE_CACHE_SIZE) {
						return ParseConstants.VALUE_CACHE[i];
					}
					return new TKV_LCONSTI(number);
				}
				return new TKV_LCONSTN(number);
			}
			if (object.baseIsPrimitiveString()) {
				if (object == BaseString.EMPTY) {
					return ParseConstants.TKV_STRING_EMPTY;
				}
				return new TKV_LCONSTS(object.baseToString());
			}
		}
		return new TKV_LCONST(object);
	}

	/** CATCH METHOD - object already IS an instance of TokenValue
	 *
	 * @param object */
	@Deprecated
	public static final void getConstantValue(final TokenValue object) {

		//
	}

	/** @param i
	 * @return value */
	public static final TokenValue getValue(final int i) {

		if (i >= 0 && i < ParseConstants.VALUE_CACHE_SIZE) {
			return ParseConstants.VALUE_CACHE[i];
		}
		return i != -1
			? new TKV_LCONSTI(Base.forInteger(i))
			: ParseConstants.TKV_NUMBER_MINUSONE;
	}

	/** @param i
	 * @return value */
	public static final TokenValue getValue(final long i) {

		return i == (int) i
			? ParseConstants.getValue((int) i)
			: new TKV_LCONSTN(Base.forLong(i));
	}
}
