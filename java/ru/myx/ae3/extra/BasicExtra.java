/*
 * Created on 03.05.2006
 */
package ru.myx.ae3.extra;

/**
 * @author myx
 * 
 */
public abstract class BasicExtra extends AbstractExtra {
	private final Object	issuer;
	
	private final long		recDate;
	
	/**
	 * @param issuer
	 * @param recId
	 * @param recDate
	 * 
	 */
	protected BasicExtra(final Object issuer, final String recId, final long recDate) {
		super( recId );
		this.issuer = issuer;
		this.recDate = recDate;
	}
	
	@Override
	public final long getRecordDate() {
		return this.recDate;
	}
	
	@Override
	public final Object getRecordIssuer() {
		return this.issuer;
	}
	
	@Override
	public String toString() {
		return String.valueOf( this.baseValue() );
	}
}
