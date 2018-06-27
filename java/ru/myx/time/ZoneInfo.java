/**
 * 
 */
package ru.myx.time;

import java.util.TimeZone;

/**
 * 
 * @author myx
 *
 */
public class ZoneInfo {
	private final TimeZone	zone;
	
	private final String	masterId;
	
	
	/**
	 * 
	 * @param zone
	 * @param masterId
	 */
	public ZoneInfo(final TimeZone zone, final String masterId) {
	
		this.zone = zone;
		this.masterId = Integer.toString( masterId.hashCode(), 36 );
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final String getId() {
	
		return this.zone.getID();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final String getMasterId() {
	
		return this.masterId;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final String getName() {
	
		return this.zone.getDisplayName();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final long getOffsetMinutes() {
	
		return this.zone.getOffset( System.currentTimeMillis() ) / 60000L;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final long getOffsetMinutesRaw() {
	
		return this.zone.getRawOffset() / 60000L;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public final String getShort() {
	
		return this.zone.getDisplayName( false, TimeZone.SHORT );
	}
}
