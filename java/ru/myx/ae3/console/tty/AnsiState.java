/**
 * 
 */
package ru.myx.ae3.console.tty;

enum AnsiState {
	/**
     * 
     */
	UNDETECTED,
	/**
     * 
     */
	DETECTED,
	/**
     * 
     */
	ESCAPE,
	/**
     * 
     */
	DT1,
	/**
     * 
     */
	DT2,
	/**
     * 
     */
	DT3,
	/**
     * 
     */
	HIGH_BIT,
}
