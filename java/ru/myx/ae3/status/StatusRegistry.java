/*
 * Created on 22.03.2006
 */
package ru.myx.ae3.status;

import java.util.Map;
import java.util.Observable;

import ru.myx.ae3.help.Create;

/**
 * @author myx
 * 
 */
public class StatusRegistry extends Observable implements StatusFiller, StatusDecomposer {
	/**
     * 
     */
	protected Map<String, StatusProvider>	statusProviders	= Create.treeMap();
	
	/**
	 * 
	 */
	public static final StatusRegistry		ROOT_REGISTRY	= new StatusRegistry( null );
	
	private final String					key;
	
	/**
	 * @param key
	 * 
	 */
	public StatusRegistry(final String key) {
		this.key = key;
	}
	
	/**
	 * @return map
	 */
	public final Map<String, StatusProvider> getStatusProviders() {
		return this.statusProviders;
	}
	
	/**
	 * @param sp
	 */
	public final void register(final StatusProvider sp) {
		this.statusProviders.put( sp.statusName(), sp );
		this.notifyObservers();
	}
	
	@Override
	public StatusProvider[] statusDecomposition() {
		return this.statusProviders.values().toArray( new StatusProvider[this.statusProviders.size()] );
	}
	
	@Override
	public void statusFill(final StatusInfo data) {
		data.put( "Provider count", this.statusProviders.size() );
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.key + ")";
	}
}
