package ru.myx.ae3.e4.parse;

/**
 * 
 * @author myx
 *
 */
public interface ProgramSource {
	class ProgramSourceFactory {
		static class ProgramSourceSimpleUnknown implements ProgramSource {
			private final CharSequence	source;
			
			
			ProgramSourceSimpleUnknown(final CharSequence source) {
			
				this.source = source;
			}
			
			
			@Override
			public CharSequence getSourceCode() {
			
				return this.source;
			}
			
			
			@Override
			public ProgramParser getProgramParser() {
			
				// TODO Auto-generated method stub
				return null;
			}
			//
		}
		
		
		public static final ProgramSource createSource(
				final CharSequence source) {
		
			return new ProgramSourceSimpleUnknown( source );
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	CharSequence getSourceCode();
	
	
	/**
	 * 
	 * @return
	 */
	ProgramParser getProgramParser();
}
