/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA01;

/** Special static optimization opportunities (lack of ModifierArgument stuff, small amount of
 * operations, predictable distribution of 'constant' values) are taken.
 *
 * @author myx */
public enum OperationsA01 implements OperationA01 {
	
	/**
	 *
	 */
	XEENTRCTCH_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRCTCH_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? ctx.vmFrameEntryOpCatch(ctx.ri08IP + 1 + constant)
				: ctx.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRCTCH_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRCTRL_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRCTRL_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpCtrl(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRCTRL_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRFULL_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRFULL_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpFull(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRFULL_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRITER_I {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRITER_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpIterCtrl(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRITER_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRITRV_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRITRV_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpIterCtrlNewVars(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRITRV_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRLOOP_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRLOOP_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpCtrlNewVars(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRLOOP_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRNONE_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRNONE_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpNone(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRNONE_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XEENTRVARS_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i > 0; --i) {
				this.cache[i] = new IA01_XEENTRVARS_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return constant > 0
				? process.vmFrameEntryOpNewVars(process.ri08IP + 1 + constant)
				: process.vmRaise("Incorrect frame size!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant > 0 && constant < 256
					? this.cache[constant]
					: new IA01_XEENTRVARS_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XELEAVE_P {
		
		final InstructionA01[] cache = new InstructionA01[256];
		{
			for (int i = 255; i >= 0; --i) {
				this.cache[i] = new IA01_XELEAVE_C_NN_NXT(i);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			return process.ri0BSB == process.ri0ASP
				? process.vmFrameLeave()
				: process.vmRaise("Stack disbalance on frame leave!");
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cache[constant]
					: new IA01_XELEAVE_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
	},
	/**
	 *
	 */
	XESKIP_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XESKIP_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XESKIP_C_NN_NXT(-i - 1);
			}
			this.cachePN[1] = IA01_XESKIP_1_NN_NXT.INSTANCE;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			process.ri08IP += constant;
			/** return NEXT - skip other VLIW command parts */
			return null;
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XESKIP_C_NN_NXT(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRL0_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XESKIPRL0_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XESKIPRL0_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XESKIPRL0_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XESKIPRL0_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra1RL == 0) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XESKIPRL0_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XESKIPRL0_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRL1_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XESKIPRL1_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XESKIPRL1_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XESKIPRL1_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XESKIPRL1_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra1RL != 0) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XESKIPRL1_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XESKIPRL1_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRB0_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XESKIPRB0_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XESKIPRB0_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XESKIPRB0_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XESKIPRB0_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra0RB.baseToBoolean() == BaseObject.FALSE) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XESKIPRB0_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XESKIPRB0_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XNSKIPRB0_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XNSKIPRB0_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XNSKIPRB0_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XNSKIPRB0_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XNSKIPRB0_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra0RB.baseValue() == null) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XNSKIPRB0_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XNSKIPRB0_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRB1_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XESKIPRB1_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XESKIPRB1_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XESKIPRB1_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XESKIPRB1_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra0RB.baseToBoolean() == BaseObject.TRUE) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XESKIPRB1_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XESKIPRB1_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XNSKIPRB1_P {
		
		final InstructionA01[] cacheNN = new InstructionA01[256];
		
		final InstructionA01[] cacheNR = new InstructionA01[256];
		
		final InstructionA01[] cachePN = new InstructionA01[256];
		
		final InstructionA01[] cachePR = new InstructionA01[256];
		
		{
			for (int i = 255; i >= 0; --i) {
				this.cachePN[i] = new IA01_XNSKIPRB1_C_NN_NXT(i);
				this.cacheNN[i] = new IA01_XNSKIPRB1_C_NN_NXT(-i - 1);
				this.cachePR[i] = new IA01_XNSKIPRB1_C_NN_RET(i);
				this.cacheNR[i] = new IA01_XNSKIPRB1_C_NN_RET(-i - 1);
			}
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			if (process.ra0RB.baseValue() != null) {
				process.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return null;
			}
			return store.execReturn(process);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public InstructionIA instruction(final int constant, final ResultHandler store) {
			
			if (store == ResultHandler.FA_BNN_NXT) {
				return constant >= 0 && constant < 256
					? this.cachePN[constant]
					: constant < 0 && constant >= -256
						? this.cacheNN[-constant - 1]
						: new IA01_XNSKIPRB1_C_NN_NXT(constant);
			}
			if (store == ResultHandler.FC_PNN_RET) {
				return constant >= 0 && constant < 256
					? this.cachePR[constant]
					: constant < 0 && constant >= -256
						? this.cacheNR[-constant - 1]
						: new IA01_XNSKIPRB1_C_NN_RET(constant);
			}
			return super.instruction(constant, store);
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XFBTGT_P {
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			process.ri0CBT = process.ri08IP + 1 + constant;
			return null;
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XFCTGT_P {
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			process.ri0DCT = process.ri08IP + 1 + constant;
			return null;
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	/**
	 *
	 */
	XFETGT_P {
		
		@Override
		public final ExecStateCode execute(final ExecProcess process, final int constant, final ResultHandler store) {
			
			assert store instanceof ResultHandlerDirect;
			
			process.ri0EET = process.ri08IP + 1 + constant;
			return null;
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return null;
		}
		
		@Override
		public final boolean isRelativeAddressInConstant() {
			
			return true;
		}
	},
	
	/**
	 *
	 */
	XFCALLA {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {
			
			return ctx.vmCall_FCALLA_FSS(constant, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return constant + 1;
		}
	},

	/**
	 *
	 */
	XRCALLA {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {
			
			return ctx.vmCall_RCALLA_SSS(constant, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return constant + 2;
		}
	},
	
	/**
	 *
	 */
	;
	
	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();
	
	final Instruction instructionCached(//
			final int constant,
			final ResultHandler store) {
		
		return InstructionA01.instructionCached(this.instruction(constant, store));
	}
}
