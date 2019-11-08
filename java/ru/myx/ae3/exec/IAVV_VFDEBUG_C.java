/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.exec;

import java.util.ArrayList;
import java.util.List;

import ru.myx.vm_vliw32_2010.OperationA00;

/** @author myx */
public final class IAVV_VFDEBUG_C implements Instruction {
	
	private Object message;

	/** @param message */
	public IAVV_VFDEBUG_C(final String message) {
		
		this.message = message;
	}

	/** @param message */
	public void addDebug(final String message) {
		
		if (message == null) {
			return;
		}
		if (this.message == null) {
			this.message = message;
			return;
		}
		if (this.message instanceof String) {
			final List<String> collection = new ArrayList<>();
			collection.add((String) this.message);
			collection.add(message);
			this.message = message;
			return;
		}
		{
			assert this.message instanceof List<?>;
			@SuppressWarnings("unchecked")
			final List<String> collection = (List<String>) this.message;
			collection.add(message);
			this.message = collection;
		}
	}

	@Override
	public boolean equals(final Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final IAVV_VFDEBUG_C other = (IAVV_VFDEBUG_C) obj;
		if (this.message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!this.message.equals(other.message)) {
			return false;
		}
		return true;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {
		
		return ctx.contextExecFDEBUG(this.message);
	}

	/** @param other */
	public void fillDebug(final IAVV_VFDEBUG_C other) {
		
		if (this.message == null) {
			return;
		}
		if (this.message instanceof String) {
			other.addDebug((String) this.message);
			return;
		}
		{
			assert this.message instanceof List<?>;
			@SuppressWarnings("unchecked")
			final List<String> collection = (List<String>) this.message;
			for (final String current : collection) {
				other.addDebug(current);
			}
		}
	}

	/** @return debug */
	public Object getDebug() {
		
		return this.message;
	}

	@Override
	public final int getOperandCount() {
		
		return 0;
	}

	/** Nominal
	 *
	 * @return */
	@SuppressWarnings("static-method")
	public OperationA00 getOperation() {
		
		return OperationsA00.XFDEBUG_P;
	}

	@Override
	public final int getResultCount() {
		
		return 0;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.message == null
			? 0
			: this.message.hashCode());
		return result;
	}

	@Override
	public final String toCode() {
		
		return Instruction.padOPCODE("FDEBUG") + "\t'" + this.message + "';";
	}
}
