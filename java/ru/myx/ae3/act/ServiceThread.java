/*
 * Created on 26.03.2006
 */
package ru.myx.ae3.act;

/**
 * @author myx
 * @param <T>
 * 			
 */
public final class ServiceThread<T> extends ContextThread<T> {
	
	static int activeServiceCount = 0;
	
	private final ActService service;
	
	/**
	 * @param ctx
	 * @param service
	 */
	public ServiceThread(final T ctx, final ActService service) {
		super(ctx, ThreadGroups.THREAD_GROUP_SVC, "SERVICE: " + service.toString());
		this.service = service;
		this.setDaemon(true);
	}
	
	@Override
	public void run() {
		
		try {
			ServiceThread.activeServiceCount++;
			try {
				if (!this.service.start()) {
					return;
				}
				for (;;) {
					try {
						if (!this.service.main()) {
							break;
						}
					} catch (final Throwable t) {
						if (!this.service.unhandledException(t)) {
							break;
						}
					}
				}
				this.service.stop();
			} finally {
				ServiceThread.activeServiceCount--;
			}
		} catch (final Exception e) {
			this.getThreadGroup().uncaughtException(this, e);
		}
	}
	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName() + " [ctx=" + this.ctx + ", service=" + this.service + "]";
	}
}
