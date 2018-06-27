package ru.myx.ae3.e4.act;

class ActProcessContextSimple implements ActProcessContext {
	
	private final String title;
	
	ActProcessContextSimple(final String title) {
		this.title = title;
	}
	
	@Override
	public CharSequence getProcessTitle() {
		
		return this.title;
	}
	
}
