/**
 * Created on 06.02.2003
 *
 * myx - barachta */
package ru.myx.benchmarking;

/** @author myx
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
final class Controller extends Thread {
	
	private final Tester tester;

	boolean stop = false;

	Controller(final Tester tester) {
		
		super("Microbenchmark controller thread");
		this.tester = tester;
		this.setPriority(Thread.MIN_PRIORITY);
		this.setDaemon(true);
	}

	@Override
	public void run() {
		
		synchronized (this.tester) {
			for (; !this.stop;) {
				try {
					this.tester.wait(200L);
				} catch (final InterruptedException e) {
					return;
				}
				// 500 ms
				if (!this.tester.done && this.tester.started + 500_000_000L < System.nanoTime()) {
					this.tester.done = true;
				}
			}
		}
	}
}
