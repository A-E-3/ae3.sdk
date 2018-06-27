package ru.myx.ae3.e4.parse;

/**
 * 
 * @author myx
 *
 */
public interface ProgramSourceSlice {
	/**
	 * 
	 * @return
	 */
	ProgramSource getParentSource();
	
	
	/**
	 * 
	 * @return
	 */
	SourceCodeSlice getParentSourceSlice();
	
	
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
