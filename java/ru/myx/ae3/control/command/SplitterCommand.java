/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.command;

/**
 * TODO: move to sys?
 *
 * @author myx
 *
 */
public final class SplitterCommand extends SimpleCommand {
	
	
	/**
	 *
	 */
	public SplitterCommand() {
		this.key = "";
	}
	
	@Override
	public final String commandDescription() {
		
		
		return "divider";
	}
	
	@Override
	public String commandPermission() {
		
		
		return "";
	}
	
	@Override
	public final String getIcon() {
		
		
		return null;
	}
	
	@Override
	public final String getTitle() {
		
		
		return "";
	}
}
