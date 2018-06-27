package ru.myx.ae3.e4.vm.macro;

import ru.myx.ae3.e4.vm.AeVmInstruction;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;

interface MacroVmInstruction extends AeVmInstruction {
	class InstructionAbstract implements MacroVmInstruction {
		
		@Override
		public ExecStateCode execCall(
				ExecProcess ctx) throws Exception {
		
			return ctx.vmRaise( "execCall must be overriden, class: " + this.getClass().getSimpleName() );
		}
	}
	
	class InstructionReturnVoid extends InstructionAbstract {
		@Override
		public ExecStateCode execCall(
				ExecProcess ctx) throws Exception {
		
			return ExecStateCode.RETURN;
		}
		
	}
	
	class InstructionReturnConstant extends InstructionAbstract {
		@Override
		public ExecStateCode execCall(
				ExecProcess ctx) throws Exception {
		
			return ExecStateCode.RETURN;
		}
		
	}
	
}
