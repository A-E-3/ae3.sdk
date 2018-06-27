package ru.myx.ae3.console.tty;

import ru.myx.ae3.common.Value;

abstract class ConsoleTtyTask<T> implements Value<T> {
	
	private T result = null;
	
	private boolean done = false;
	
	@Override
	public T baseValue() {
		
		if (this.done) {
			return this.result;
		}
		synchronized (this) {
			while (!this.done) {
				try {
					this.wait(5000L);
				} catch (final InterruptedException e) {
					return null;
				}
			}
			return this.result;
		}
	}
	
	abstract ConsoleTtyTask<?> consumeNext(final ConsoleTty console,
			final int b);
			
	abstract void onTaskInit(ConsoleTty console);
	
	void setResult(final T result) {
		
		synchronized (this) {
			this.done = true;
			this.result = result;
			this.notifyAll();
		}
	}
}
