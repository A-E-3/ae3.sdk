/*
 * Created on 09.10.2004
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.jdbc.lock;

import java.sql.Connection;
import java.util.Enumeration;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Lock {
	/**
	 * @author myx
	 * 
	 */
	public static interface ManagerFactoryImpl {
		/**
		 * @author myx
		 * 
		 */
		public static final class Default implements ManagerFactoryImpl {
			@Override
			public LockManager createManager(
					final Enumeration<Connection> connectionSource,
					final String tableName,
					final String identity) {
				return null;
			}
		}
		
		/**
		 * @param connectionSource
		 * @param tableName
		 * @param identity
		 * @return lock manager
		 */
		public LockManager createManager(
				final Enumeration<Connection> connectionSource,
				final String tableName,
				final String identity);
	}
	
	private static ManagerFactoryImpl	factory		= null;
	
	private static boolean				managerLock	= false;
	
	/**
	 * @param connectionSource
	 * @param tableName
	 * @param identity
	 * @return manager
	 */
	public static LockManager createManager(
			final Enumeration<Connection> connectionSource,
			final String tableName,
			final String identity) {
		if (Lock.factory == null) {
			return null;
		}
		return Lock.factory.createManager( connectionSource, tableName, identity );
	}
	
	/**
	 * @param impl
	 */
	public static final void managerFactoryImpl(final ManagerFactoryImpl impl) {
		if (Lock.managerLock) {
			throw new IllegalStateException( "Locked!" );
		}
		Lock.factory = impl;
	}
	
	/**
     * 
     */
	public static final void managerLock() {
		Lock.managerLock = true;
	}
	
}
