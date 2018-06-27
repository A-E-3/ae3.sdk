package ru.myx.ae3.status;

import ru.myx.ae3.Engine;

/*
 * Created on 21.05.2004
 */

/** @author myx */
public class StatusProviderFiller implements StatusProvider {
	
	private final String name;

	private final Object description;

	private final StatusFiller filler;

	/** @param name
	 * @param description
	 * @param filler
	 */
	public StatusProviderFiller(final String name, final Object description, final StatusFiller filler) {
		
		this.name = name == null
			? Engine.createGuid()
			: name;
		this.description = description;
		this.filler = filler;
	}

	@Override
	public StatusProvider[] childProviders() {
		
		if (this.filler instanceof StatusDecomposer) {
			return ((StatusDecomposer) this.filler).statusDecomposition();
		}
		return null;
	}

	@Override
	public String statusDescription() {
		
		return String.valueOf(this.description);
	}

	@Override
	public void statusFill(final StatusInfo status) {
		
		this.filler.statusFill(status);
	}

	@Override
	public String statusName() {
		
		return this.name;
	}
}
