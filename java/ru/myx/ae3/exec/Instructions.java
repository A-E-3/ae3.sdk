package ru.myx.ae3.exec;

/** @author myx */
public class Instructions {
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BCVT_1_R_NN_NEXT = OperationsA10.XBCVT_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BCVT_1_R_SN_NEXT = OperationsA10.XBCVT_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BCVT_1_S_SN_NEXT = OperationsA10.XBCVT_N.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BCVT_1_T_SN_NEXT = OperationsA10.XBCVT_N.instructionCached(ModifierArguments.AB4CT, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BEQU_BA_SS_S = OperationsA2X.XBEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BEQU_BA_SS_V = OperationsA2X.XBEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BLESS_BA_SS_S = OperationsA2X.XBLESS.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BLESS_BA_SS_V = OperationsA2X.XBLESS.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BMORE_BA_SS_S = OperationsA2X.XBMORE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BMORE_BA_SS_V = OperationsA2X.XBMORE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNEQU_BA_SS_S = OperationsA2X.XBNEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNEQU_BA_SS_V = OperationsA2X.XBNEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNLESS_BA_SS_S = OperationsA2X.XBNLESS.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNLESS_BA_SS_V = OperationsA2X.XBNLESS.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNMORE_BA_SS_S = OperationsA2X.XBNMORE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNMORE_BA_SS_V = OperationsA2X.XBNMORE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNOT_1_R_NN_NEXT = OperationsA10.XBNOT_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNOT_1_R_SN_NEXT = OperationsA10.XBNOT_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BNOT_1_S_SN_NEXT = OperationsA10.XBNOT_N.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BOR_EMPTYSTRING_1_R_S = OperationsS2X.VEBOR_D.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.EMPTY_STRING, 0, ResultHandler.FB_BSN_NXT);
	
	/** both arguments detachable - using BOR_T */
	public static final//
	Instruction INSTR_BOR_EMPTYSTRING_1_S_S = OperationsA2X.XEBOR_T.instructionCached(ModifierArguments.AE21POP, ModifierArgumentA30IMM.EMPTY_STRING, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BSEQU_BA_SS_S = OperationsA2X.XBSEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BSEQU_BA_SS_V = OperationsA2X.XBSEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BSNEQU_BA_SS_S = OperationsA2X.XBSNEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_BSNEQU_BA_SS_V = OperationsA2X.XBSNEQU.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CARRAY_0_0_SN_NEXT = OperationsA00.XCARRAY_N.instructionCached(0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CARRAY_0_1_SN_NEXT = OperationsA00.XCARRAY_N.instructionCached(1, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_COBJECT_0_0_SN_NEXT = OperationsA00.XCOBJECT_N.instructionCached(0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CSCOPE_0_0_NN_NEXT = OperationsA00.XCSCOPE_N.instructionCached(0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CSCOPECTX_0_0_NN_NEXT = OperationsA00.XCSCOPECTX_N.instructionCached(0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_D_1_R_SN_NEXT = OperationsS10.VCVTN_D.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_N_1_R_SN_NEXT = OperationsS10.VCVTN_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_D_1_S_SN_NEXT = OperationsS10.VCVTN_D.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_N_1_S_SN_NEXT = OperationsS10.VCVTN_N.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_D_1_R_NN_NEXT = OperationsS10.VCVTN_D.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_N_1_R_NN_NEXT = OperationsS10.VCVTN_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTN_T_1_R_NN_NEXT = OperationsA10.ZCVTN_T.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_D_1_R_SN_NEXT = OperationsS10.VCVTS_D.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_N_1_R_SN_NEXT = OperationsS10.VCVTS_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_D_1_S_SN_NEXT = OperationsS10.VCVTS_D.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_N_1_S_SN_NEXT = OperationsS10.VCVTS_N.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_D_1_R_NN_NEXT = OperationsS10.VCVTS_D.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_N_1_R_NN_NEXT = OperationsS10.VCVTS_N.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_CVTS_T_1_R_NN_NEXT = OperationsA10.ZCVTS_T.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_DELETE_A_SS_S = OperationsA2X.XADELETE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_DELETE_A_SS_V = OperationsA2X.XADELETE.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_EENTRFULL_1_NN_NEXT = OperationsA01.XEENTRFULL_P.instructionCached(1, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_EENTRNONE_1_NN_NEXT = OperationsA01.XEENTRNONE_P.instructionCached(1, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ELEAVE_0_NN_NEXT = OperationsA01.XELEAVE_P.instructionCached(0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ELEAVE_1_NN_NEXT = OperationsA01.XELEAVE_P.instructionCached(1, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ELEAVE_2_NN_NEXT = OperationsA01.XELEAVE_P.instructionCached(2, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ESKIP_1_NN_NEXT = OperationsA01.XESKIP_P.instructionCached(1, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ESKIPRB0_0_NN_BREAK = OperationsA01.XESKIPRB0_P.instructionCached(0, ResultHandler.FB_BNN_BRK);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ESKIPRB0_0_NN_CONTINUE = OperationsA01.XESKIPRB0_P.instructionCached(0, ResultHandler.FB_BNN_CTN);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ESKIPRB1_0_NN_BREAK = OperationsA01.XESKIPRB1_P.instructionCached(0, ResultHandler.FB_BNN_BRK);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ESKIP1_0_NN_CONTINUE = OperationsA01.XESKIPRB1_P.instructionCached(0, ResultHandler.FB_BNN_CTN);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FOTBLDR_0_SN_NEXT = OperationsA00.XFOTBLDR_N.instructionCached(0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FOTDONE_1_S_NN_RETURN = OperationsA10.XFOTDONE.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FC_PNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FOTDONE_1_S_SN_NEXT = OperationsA10.XFOTDONE.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/** To RC */
	public static final//
	Instruction INSTR_FOTDONE_C_1_S_NN_NEXT = OperationsA10.XFOTDONE.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_CNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FOTDONE_B_1_S_NN_NEXT = OperationsA10.XFOTDONE.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FOTNULL_0_SN_NEXT = OperationsA00.XFOTNULL_N.instructionCached(0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_FPULLGV_0_0_NN_NEXT = OperationsA00.XFPULLGV_N.instructionCached(0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREP7_1_R_NN_NEXT = OperationsA10.ZITRPREP7.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREP7_1_S_NN_NEXT = OperationsA10.ZITRPREP7.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREPK_1_R_NN_NEXT = OperationsA10.XITRPREPK.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREPK_1_S_NN_NEXT = OperationsA10.XITRPREPK.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREPV_1_R_NN_NEXT = OperationsA10.XITRPREPV.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_ITRPREPV_1_S_NN_NEXT = OperationsA10.XITRPREPV.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_C_NN_ERROR_NO_MESSAGE = OperationsA10.XFLOAD_P.instructionCached(new ModifierArgumentA30IMM("no message"), 0, ResultHandler.FB_BNN_ERR);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_P_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE22PEEK, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_NN_ERROR = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BNN_ERR);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_NO_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BNO_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_NN_RETURN = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_R_SO_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AA0RB, 0, ResultHandler.FB_BSO_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_NN_ERROR = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BNN_ERR);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_NN_RETURN = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FC_PNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_NO_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BNO_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_S_SO_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AE21POP, 0, ResultHandler.FB_BSO_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_1_T_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArguments.AB4CT, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_FALSE_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.FALSE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_FALSE_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.FALSE, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_TRUE_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.TRUE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_TRUE_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.TRUE, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_UNDEFINED_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.UNDEFINED, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_UNDEFINED_NN_RETURN = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.UNDEFINED, 0, ResultHandler.FC_PNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_UNDEFINED_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.UNDEFINED, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_NULL_NN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.NULL, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_NULL_NN_RETURN = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.NULL, 0, ResultHandler.FC_PNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_LOAD_NULL_SN_NEXT = OperationsA10.XFLOAD_P.instructionCached(ModifierArgumentA30IMM.NULL, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_BA_1S_S = OperationsS2X.VMADD_N.instructionCached(ModifierArguments.AE21POP, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_D_BA_SS_S = OperationsS2X.VMADD_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_N_BA_SS_S = OperationsS2X.VMADD_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADDN_D_BA_1R_V = OperationsS2X.VMADDN_D.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADDN_N_BA_1R_V = OperationsS2X.VMADDN_N.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_D_BA_RS_V = OperationsS2X.VMADD_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADDS_D_BA_RS_V = OperationsS2X.VMADDS_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_N_BA_RS_V = OperationsS2X.VMADD_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_D_BA_SS_V = OperationsS2X.VMADD_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_N_BA_SS_V = OperationsS2X.VMADD_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADDN_T_BA_1R_V = OperationsS2X.VMADDN_T.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_T_BA_RS_V = OperationsA2X.XMADD_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MADD_T_BA_SS_V = OperationsA2X.XMADD_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MAND_D_BA_SS_S = OperationsS2X.VMBAND_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MAND_N_BA_SS_S = OperationsS2X.VMBAND_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MAND_D_BA_SS_V = OperationsS2X.VMBAND_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MAND_N_BA_SS_V = OperationsS2X.VMBAND_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MAND_T_BA_SS_V = OperationsA2X.XMAND_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MDIV_D_BA_SS_S = OperationsS2X.VMDIV_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MDIV_N_BA_SS_S = OperationsS2X.VMDIV_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MDIV_D_BA_SS_V = OperationsS2X.VMDIV_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MDIV_N_BA_SS_V = OperationsS2X.VMDIV_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	*
	*/
	public static final//
	Instruction INSTR_MDIV_T_BA_SS_V = OperationsA2X.XMDIV_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	*
	*/
	public static final//
	Instruction INSTR_MMOD_D_BA_SS_S = OperationsS2X.VMMOD_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	*
	*/
	public static final//
	Instruction INSTR_MMOD_N_BA_SS_S = OperationsS2X.VMMOD_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMOD_D_BA_SS_V = OperationsS2X.VMMOD_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMOD_N_BA_SS_V = OperationsS2X.VMMOD_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	*
	*/
	public static final//
	Instruction INSTR_MMOD_T_BA_SS_V = OperationsA2X.XMMOD_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMUL_D_BA_SS_S = OperationsS2X.VMMUL_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MPOW_D_BA_SS_S = OperationsS2X.VMPOW_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMUL_N_BA_SS_S = OperationsS2X.VMMUL_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MPOW_N_BA_SS_S = OperationsS2X.VMPOW_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMUL_D_BA_SS_V = OperationsS2X.VMMUL_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MPOW_D_BA_SS_V = OperationsS2X.VMPOW_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMUL_N_BA_SS_V = OperationsS2X.VMMUL_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MPOW_N_BA_SS_V = OperationsS2X.VMPOW_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MMUL_T_BA_SS_V = OperationsA2X.XMMUL_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MPOW_T_BA_SS_V = OperationsA2X.XMPOW_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MOR_D_BA_SS_S = OperationsS2X.VMBOR_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MOR_N_BA_SS_S = OperationsS2X.VMBOR_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MOR_D_BA_SS_V = OperationsS2X.VMBOR_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MOR_N_BA_SS_V = OperationsS2X.VMBOR_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MOR_T_BA_SS_V = OperationsA2X.XMBOR_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHL_D_BA_SS_S = OperationsS2X.VMBSHL_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHL_N_BA_SS_S = OperationsS2X.VMBSHL_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHL_D_BA_SS_V = OperationsS2X.VMBSHL_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHL_N_BA_SS_V = OperationsS2X.VMBSHL_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHL_T_BA_SS_V = OperationsA2X.XMBSHL_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRS_D_BA_SS_S = OperationsS2X.VMBSHRS_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRS_N_BA_SS_S = OperationsS2X.VMBSHRS_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRS_D_BA_SS_V = OperationsS2X.VMBSHRS_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRS_N_BA_SS_V = OperationsS2X.VMBSHRS_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRS_T_BA_SS_V = OperationsA2X.XMBSHRS_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRU_D_BA_SS_S = OperationsS2X.VMBSHRU_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRU_N_BA_SS_S = OperationsS2X.VMBSHRU_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRU_D_BA_SS_V = OperationsS2X.VMBSHRU_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRU_N_BA_SS_V = OperationsS2X.VMBSHRU_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSHRU_T_BA_SS_V = OperationsA2X.XMBSHRU_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_R0_S = OperationsS2X.VMSUB_D.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_R0_S = OperationsS2X.VMSUB_N.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AA0RB, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_S0_S = OperationsS2X.VMSUB_D.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_S0_S = OperationsS2X.VMSUB_N.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_SS_S = OperationsS2X.VMSUB_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_SS_S = OperationsS2X.VMSUB_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_1R_V = OperationsS2X.VMSUB_D.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_1R_V = OperationsS2X.VMSUB_N.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_R0_V = OperationsS2X.VMSUB_D.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_R0_V = OperationsS2X.VMSUB_N.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_RS_V = OperationsS2X.VMSUB_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_RS_V = OperationsS2X.VMSUB_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_S0_V = OperationsS2X.VMSUB_D.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_S0_V = OperationsS2X.VMSUB_N.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_D_BA_SS_V = OperationsS2X.VMSUB_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_N_BA_SS_V = OperationsS2X.VMSUB_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_T_BA_1R_V = OperationsA2X.XMSUB_T.instructionCached(ModifierArguments.AA0RB, ModifierArgumentA30IMM.ONE, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_T_BA_R0_V = OperationsA2X.XMSUB_T.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_T_BA_RS_V = OperationsA2X.XMSUB_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AA0RB, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_T_BA_S0_V = OperationsA2X.XMSUB_T.instructionCached(ModifierArgumentA30IMM.ZERO, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MSUB_T_BA_SS_V = OperationsA2X.XMSUB_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MXOR_D_BA_SS_S = OperationsS2X.VMBXOR_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MXOR_N_BA_SS_S = OperationsS2X.VMBXOR_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MXOR_D_BA_SS_V = OperationsS2X.VMBXOR_D.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MXOR_N_BA_SS_V = OperationsS2X.VMBXOR_N.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_MXOR_T_BA_SS_V = OperationsA2X.XMBXOR_T.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FA_BNN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_NOP_0_NN_BREAK = OperationsA00.XCVOID_P.instructionCached(0, ResultHandler.FB_BNN_BRK);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_NOP_0_NN_CONTINUE = OperationsA00.XCVOID_P.instructionCached(0, ResultHandler.FB_BNN_CTN);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_NOP_0_NN_ERROR = OperationsA00.XCVOID_P.instructionCached(0, ResultHandler.FB_BNN_ERR);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_NOP_0_NN_RETURN = OperationsA00.XCVOID_P.instructionCached(0, ResultHandler.FC_PNN_RET);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_SACCESS_CBA_SSF_S = OperationsA3X.XASTORE_N
			.instructionCached(ModifierArguments.AB7FV, ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	/**
	 *
	 */
	public static final//
	Instruction INSTR_SACCESS_CBA_SSS_S = OperationsA3X.XASTORE_N
			.instructionCached(ModifierArguments.AE21POP, ModifierArguments.AE21POP, ModifierArguments.AE21POP, 0, ResultHandler.FB_BSN_NXT);
	
	private Instructions() {
		
		// prevent creation
	}
}
