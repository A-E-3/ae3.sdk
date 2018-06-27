/**
 * Created on 16.02.2003
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package ru.myx.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import ru.myx.ae3.Engine;
import java.util.function.Function;
import ru.myx.ae3.help.Convert;
import ru.myx.file_watcher.FileWatcher;

/**
 * @author barachta
 *
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class IpGeography {

	private static final String[] COUNTRIES = {
			null, "**", "US", "RU", "CA", "LV", "SP", "IT", "UK", "DE", "IR", "ES", "SE", "LU", "BG", "UA", "GR", "BA", "NO", "NL", "HR", "PL", "HU", "AT", "TR", "CZ", "IL", "CH",
			"FR", "TM", "SL", "BE", "LI", "IE", "FI", "MT", "AE", "DK", "RO", "IS", "LT", "GE", "EU", "SK", "JO", "EG", "PT", "NG", "AM", "GA", "SI", "EE", "PS", "YU", "ML", "CY",
			"MD", "BY", "BH", "MK", "KZ", "AZ", "UZ", "KW", "SA", "DZ", "LB", "UG", "KE", "GH", "SN", "CI", "QA", "TN", "KG", "MC", "ET", "MA", "GI", "VA", "OM", "GM", "FO", "BF",
			"SD", "YE", "CM", "SM", "GL", "AD", "BJ", "TG", "AL", "LY", "JP", "CN", "IN", "ID", "BD", "AU", "PH", "TH", "SG", "NZ", "TW", "HK", "PK", "MP", "MY", "LK", "VN", "KR",
			"MM", "NP", "AP", "PW", "KI", "LA", "KH", "MN", "VU", "BN", "GU", "MO", "IO", "PG", "NC", "MV", "FJ", "CK", "MU", "TV", "SB", "BT", "PF", "WS", "NR", "TO", "BM", "AR",
			"PE", "CL", "GT", "GB", "AN", "BB", "ZA", "TT", "PR", "MX", "VE", "PA", "CO", "BR", "NA", "DO", "JM", "SY", "TZ", "CR", "AO", "ZW", "MZ", "SZ", "CU", "GD", "BW", "GN",
			"BO", "EC", "UY", "NE", "NI", "KP", "SV", "BS", "LS",
	};
	
	private static final Map<String, Integer> COUNTRY_TO_INDEX = new HashMap<>(255);
	
	private static final int MIN = 0;
	
	private static final int MAX = 1 << 24;
	
	static final FileFilter DB_FILTER = new FilterDB();
	
	private static final File FOLDER = new File(new File(Engine.PATH_PROTECTED, "settings"), "ip_geography");
	
	private static final boolean USE_MEMORY_MAPPING = Convert.MapEntry.toBoolean(System.getProperties(), "ae2.use_memory_mapping", true);
	
	private static ByteBuffer database = null;
	
	static {
		for (int i = IpGeography.COUNTRIES.length - 1; i >= 0; --i) {
			IpGeography.COUNTRY_TO_INDEX.put(IpGeography.COUNTRIES[i], new Integer(i));
		}
	}
	
	static {
		try {
			IpGeography.checkDB();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	static {
		try {
			FileWatcher.registerReader("ip_geography", new Function<FileReader, Object>() {
				
				@Override
				public Object apply(final FileReader o) {
					
					if (o == null) {
						throw new IllegalArgumentException("Null parameter!");
					}
					try {
						IpGeography.index(o);
						{
							final InputStream inputStream = this.getClass().getResourceAsStream("defaults.txt");
							try (final Reader reader = new InputStreamReader(inputStream)) {
								IpGeography.index(reader);
							}
						}
					} catch (final Throwable t) {
						throw new RuntimeException("While running " + this, t);
					}
					return null;
				}

				@Override
				public String toString() {
					
					return "IP-GEO (RE-)LOADER";
				}
			});
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
	
	private static void checkDB() throws IOException {

		if (!IpGeography.FOLDER.exists() && !IpGeography.FOLDER.mkdirs()) {
			throw new RuntimeException("IP -> GEO, cannot create: " + IpGeography.FOLDER.getAbsolutePath());
		}
		final File[] files = IpGeography.FOLDER.listFiles(IpGeography.DB_FILTER);
		File last = null;
		if (files != null) {
			for (int i = files.length - 1; i >= 0; --i) {
				if (last == null) {
					last = files[i];
					continue;
				}
				if (files[i].lastModified() > last.lastModified()) {
					last = files[i];
				}
			}
		}
		if (IpGeography.USE_MEMORY_MAPPING) {
			if (last == null) {
				final long ctm = System.currentTimeMillis();
				last = new File(IpGeography.FOLDER, ctm + ".24db");
				try (final RandomAccessFile file = new RandomAccessFile(last, "rw")) {
					IpGeography.database = file.getChannel().map(FileChannel.MapMode.READ_WRITE, IpGeography.MIN, IpGeography.MAX);
					IpGeography.database.limit(IpGeography.MAX);
				}
				
				{
					final File countries = new File(IpGeography.FOLDER, ctm + ".txt");
					try (final PrintWriter pw = new PrintWriter(new FileWriter(countries))) {
						for (int i = 0; i < IpGeography.COUNTRIES.length; ++i) {
							pw.println(i + " - " + IpGeography.COUNTRIES[i]);
						}
					}
				}
			} else {
				try (final RandomAccessFile file = new RandomAccessFile(last, "rw")) {
					IpGeography.database = file.getChannel().map(FileChannel.MapMode.READ_WRITE, IpGeography.MIN, IpGeography.MAX);
				}
			}
		} else {
			System.out.println("IP_GEORGAPHY: Warning, NOT using memory mapping, additional 16M of RAM will be used!");
			if (last == null) {
				final long ctm = System.currentTimeMillis();
				last = new File(IpGeography.FOLDER, ctm + ".24db");
				try (final RandomAccessFile file = new RandomAccessFile(last, "rw")) {
					file.setLength(IpGeography.MAX);
					final byte[] bytes = new byte[(int) file.length()];
					file.readFully(bytes);
					IpGeography.database = ByteBuffer.wrap(bytes);
				}
				
				{
					final File countries = new File(IpGeography.FOLDER, ctm + ".txt");
					try (final PrintWriter pw = new PrintWriter(new FileWriter(countries))) {
						for (int i = 0; i < IpGeography.COUNTRIES.length; ++i) {
							pw.println(i + " - " + IpGeography.COUNTRIES[i]);
						}
					}
				}
			} else {
				try (final RandomAccessFile file = new RandomAccessFile(last, "rw")) {
					final byte[] bytes = new byte[(int) file.length()];
					file.readFully(bytes);
					IpGeography.database = ByteBuffer.wrap(bytes);
				}
			}
		}
	}
	
	/**
	 * @param address
	 * @return string
	 */
	public static final String getCountryCode(final InetAddress address) {

		final byte[] bytes = address.getAddress();
		return IpGeography.COUNTRIES[IpGeography.database.get(((bytes[0] & 0xFF) << 16) + ((bytes[1] & 0xFF) << 8) + (bytes[2] & 0xFF)) & 0xFF];
	}
	
	/**
	 * @param address
	 * @return string
	 */
	public static final String getCountryCode(final long address) {

		return IpGeography.COUNTRIES[IpGeography.database.get((int) (address >> 8 & 0x00FFFFFF)) & 0xFF];
	}
	
	/**
	 * @param address
	 * @return string
	 * @throws UnknownHostException
	 */
	public static final String getCountryCode(final String address) throws UnknownHostException {

		return IpGeography.getCountryCode(InetAddress.getByName(address));
	}
	
	/**
	 * @param address
	 * @param def
	 * @return string
	 */
	public static final String getCountryCode(final String address, final String def) {

		try {
			final String result = IpGeography.getCountryCode(InetAddress.getByName(address));
			if (result == null) {
				return def;
			}
			return result;
		} catch (final UnknownHostException e) {
			return def;
		}
	}
	
	/**
	 * @param address
	 * @param def
	 * @param local
	 * @return string
	 */
	public static final String getCountryCode(final String address, final String def, final String local) {

		try {
			final String result = IpGeography.getCountryCode(InetAddress.getByName(address));
			if (result == null) {
				return def;
			}
			if (result == IpGeography.COUNTRIES[1]) {
				return local;
			}
			return result;
		} catch (final UnknownHostException e) {
			return def;
		}
	}
	
	static void index(final Reader file) throws IOException {

		System.out.println("IP_GEOGRAPHY: import launched.");
		final StringBuilder c_country = new StringBuilder();
		final StringBuilder c_proto = new StringBuilder();
		final StringBuilder c_address = new StringBuilder();
		final StringBuilder c_length = new StringBuilder();
		final StringBuilder[] buffs = {
				c_country, c_proto, c_address, c_length
		};
		try (final BufferedReader reader = new BufferedReader(file)) {
			for (;;) {
				final String line = reader.readLine();
				if (line == null) {
					break;
				}
				final String data = line.trim();
				if (data.length() == 0 || data.charAt(0) == '#' || data.startsWith("1|")) {
					continue;
				}
				final int length = data.length();
				
				int mode = -1;
				for (int i = 0; i < length; ++i) {
					final char c = data.charAt(i);
					if (c == '|') {
						if (mode == 1 && !c_proto.toString().equals("ipv4")) {
							c_country.setLength(0);
							c_proto.setLength(0);
							break;
						}
						if (mode == 3) {
							break;
						}
						mode++;
						continue;
					}
					if (mode >= 0) {
						buffs[mode].append(c);
					}
				}
				if (c_proto.length() == 0) {
					continue;
				}
				
				final String country = c_country.toString();
				final Integer index = IpGeography.COUNTRY_TO_INDEX.get(country);
				
				if (index == null) {
					System.out.println("IP_GEOGRAPHY: unknown country: " + country);
				} else {
					final String addr = c_address.toString();
					final InetAddress address = InetAddress.getByName(addr);
					final byte[] bytes = address.getAddress();
					final int idx = ((bytes[0] & 0xFF) << 16) + ((bytes[1] & 0xFF) << 8) + (bytes[2] & 0xFF);
					
					final int len = Integer.parseInt(c_length.toString()) >> 8;
					
					final byte countryIndex = addr.equals("127.0.0.0") || addr.equals("172.16.0.0") || addr.equals("10.0.0.0") || addr.equals("192.168.0.0")
						? 1
						: index.byteValue();
					for (int i = 0; i < len; ++i) {
						IpGeography.database.put(idx + i, countryIndex);
					}
				}
				
				c_country.setLength(0);
				c_proto.setLength(0);
				c_address.setLength(0);
				c_length.setLength(0);
			}
			if (!(IpGeography.database instanceof MappedByteBuffer)) {
				// WRITE HERE **** xxx
			}
		}
		System.out.println("IP_GEOGRAPHY: import finished.");
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {

		for (final String element : args) {
			IpGeography.index(new FileReader(new File(element)));
		}
		
		final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			System.out.print("Enter IP address or CRLF to exit: ");
			final String s = input.readLine();
			if (s == null || s.length() == 0) {
				break;
			}
			System.out.println("Country: " + IpGeography.getCountryCode(s));
		}
	}
	
}
