package ru.myx.geo;

/*
 * Created on 19.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author myx
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GeographyAPI {
	/**
	 * @param address
	 * @return string
	 * @throws Exception
	 */
	public static final String getCountryCodeForAddress(final String address) throws Exception {
		return IpGeography.getCountryCode( address );
	}
	
	/**
	 * @param address
	 * @param def
	 * @return string
	 */
	public static final String getCountryCodeForAddressSafe(final String address, final String def) {
		return IpGeography.getCountryCode( address, def );
	}
	
	/**
	 * @return all codes
	 */
	public static final String[] getCountryCodes() {
		return Geography.getCountryCodes();
	}
	
	/**
	 * @param code
	 * @return string
	 */
	public static final String getCountryNameForCode(final String code) {
		return Geography.getCountryNameForCode( code );
	}
	
	/**
	 * @param code
	 * @param def
	 * @return string
	 */
	public static final String getCountryNameForCodeSafe(final String code, final String def) {
		return Geography.getCountryNameForCode( code, def );
	}
}
