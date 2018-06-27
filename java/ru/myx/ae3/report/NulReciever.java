/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.report;

final class NulReciever extends LogReceiver {
	private static final String[]	DUMMY_CLASSES	= new String[0];
	
	private static final String[]	DUMMY_TYPES		= new String[0];
	
	@Override
	protected String[] eventClasses() {
		return NulReciever.DUMMY_CLASSES;
	}
	
	@Override
	protected String[] eventTypes() {
		return NulReciever.DUMMY_TYPES;
	}
	
	@Override
	protected void onEvent(final Event event) {
		System.out.println( "\r\n.\r\n" );
	}
}
