package ru.myx.ae3.act;

final class ThreadGroups {
	static final ThreadGroup	THREAD_GROUP_ROOT;
	
	static final ThreadGroup	THREAD_GROUP_SVC;
	static {
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		while (root.getParent() != null) {
			root = root.getParent();
		}
		THREAD_GROUP_ROOT = root;
		(THREAD_GROUP_SVC = new ThreadGroup( root, "GRP: ACT-SERVICE-GROUP" )).setDaemon( false );
	}
}
