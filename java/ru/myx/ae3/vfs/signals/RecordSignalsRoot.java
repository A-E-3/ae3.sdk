package ru.myx.ae3.vfs.signals;

class RecordSignalsRoot extends RecordSignals {
	
	RecordSignalsRoot(final String key) {
		super( key, null );
	}
	
	@Override
	public RecordSignalsRoot baseValue() {
		return this;
	}
	
	@Override
	public boolean isBinary() {
		return false;
	}
	
	@Override
	public boolean isContainer() {
		return true;
	}
	
	@Override
	public String toString() {
		return "SIGNAL-ROOT";
	}
}
