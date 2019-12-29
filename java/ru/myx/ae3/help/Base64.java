/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.help;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/** @author myx */
public final class Base64 {
	
	// base 64 byte decoding mapping
	private static final byte[] bytes = new byte[256];

	// base 64 character encoding mapping
	private static final char[] code = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	};
	static {
		// characters mapped to their byte value
		for (int i = Base64.code.length - 1; i >= 0; --i) {
			Base64.bytes[Base64.code[i]] = (byte) i;
		}
	}

	/** Convert BASE64 encoded string to a byte array.
	 *
	 * @param string
	 * @param lines
	 * @return bytes */
	public static final byte[] decode(final String string, final boolean lines) {
		
		try {
			return Base64.decodeIntern(string.toCharArray(), lines);
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException("Decoder error: " + e.getMessage(), e);
		}
	}

	/** Decode a base64 encoded char array.
	 *
	 * @param ch
	 *            Char array to decode
	 * @param lines
	 * @return A byte array of the decoded file
	 * @throws UnsupportedEncodingException
	 *             If the input file is not properly encoded */
	private static final byte[] decodeIntern(final char[] ch, final boolean lines) throws UnsupportedEncodingException {
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		// base64 is made up in 4 byte chunks, so get a buffer to
		// store each encoded part in
		final char[] buff = new char[4];
		int count = 0; // keep track of the file length
		if (lines) {
			while (count < ch.length) {
				for (int j = 0; j < buff.length; j++) {
					char chr;
					do {
						// if file is not dividable by four it is not
						// properly
						// encoded so throw an exception
						if (count >= ch.length) {
							throw new UnsupportedEncodingException("Invalid base64 encoding.");
						}
						chr = ch[count];
						count++;
					} while (chr == '\n' || chr == '\r' || chr == ' '); // loop
					// to
					// avoid
					// CRLF's
					buff[j] = chr;
				}
				Base64.doDecode(buff, out); // decode buffer to
				// outputstream
			}
		} else {
			while (count < ch.length) {
				for (int j = 0; j < buff.length; j++) {
					char chr;
					do {
						// if file is not dividable by four it is not
						// properly
						// encoded so throw an exception
						if (count >= ch.length) {
							throw new UnsupportedEncodingException("Invalid base64 encoding.");
						}
						chr = ch[count];
						count++;
					} while (chr == '\n' || chr == '\r'); // loop
					// to
					// avoid
					// CRLF's
					buff[j] = chr == ' '
						? '+'
						: chr;
				}
				Base64.doDecode(buff, out); // decode buffer to
				// outputstream
			}
		}
		return out.toByteArray();
	}

	private static final void doDecode(final char[] org, final ByteArrayOutputStream out) {
		
		// the first two characters are always present so get their
		// byte representation
		final byte b_1 = Base64.bytes[org[0] & 0xff];
		final byte b_2 = Base64.bytes[org[1] & 0xff];
		// encode the first byte
		out.write((byte) (b_1 << 2 & 0xfc | b_2 >>> 4 & 3));
		// character 3 and 4 can be '=' characters to mark the end of the
		// file,
		// but if they are not - encode them too
		if (org[2] != '=') {
			final byte b_3 = Base64.bytes[org[2] & 0xff];
			out.write((byte) (b_2 << 4 & 0xf0 | b_3 >>> 2 & 0xf));
			if (org[3] != '=') {
				final byte b_4 = Base64.bytes[org[3] & 0xff];
				out.write((byte) (b_3 << 6 & 0xc0 | b_4 & 0x3f));
			}
		}
	}

	private static final void doEncode(final byte[] org, final int len, final StringWriter sw) {
		
		// even if there is only one byte to encode, it will be
		// represented by two characters
		sw.write(Base64.code[org[0] >>> 2 & 0x3f]);
		sw.write(Base64.code[org[0] << 4 & 0x30 | org[1] >>> 4 & 0x0f]);
		// then switch through the number of characters to encode and
		// fill with '=' characters if we're at the end of the file
		switch (len) {
			case 3 :
				sw.write(Base64.code[org[1] << 2 & 0x3c | org[2] >>> 6 & 0x03]);
				sw.write(Base64.code[org[2] & 0x3f]);
				break;
			case 2 :
				sw.write(Base64.code[org[1] << 2 & 0x3c | org[2] >>> 6 & 0x03]);
				sw.write('=');
				break;
			case 1 :
				sw.write('=');
				sw.write('=');
				break;
			default :
		}
	}

	/** Convert byte array to a BASE64 encoded string.
	 *
	 * @param bytes
	 * @param lines
	 * @return string */
	public static final String encode(final byte[] bytes, final boolean lines) {
		
		if (bytes == null) {
			throw new java.lang.NullPointerException("base64 encoder - input bytes are null!");
		}
		return Base64.encodeIntern(bytes, lines);
	}

	// do the decoding
	/** Encode byte array to a base64 encoded String.
	 *
	 * @param b
	 *            Byte array to encode.
	 * @param lines
	 * @return A string representation of the encoded file. */
	private static final String encodeIntern(final byte[] b, final boolean lines) {
		
		final StringWriter sw = new StringWriter();
		int i = 0; // file length count
		int linecount = 0; // line width count
		// encoding is made by dividing 3 bytes into 4
		// 6 bits representations, so get a buffer
		final byte[] tmp = new byte[3];
		while (i < b.length) {
			// decide if we have reached the end of the file
			// and set "len" value to the appropriate length
			// and read bytes into buffer
			int len = 0;
			if (i + 2 < b.length) {
				for (int j = 0; j < 3; j++) {
					tmp[j] = b[i];
					++i;
				}
				len = 3;
			} else //
			if (i + 1 < b.length) {
				for (int j = 0; j < 2; j++) {
					tmp[j] = b[i];
					++i;
				}
				tmp[2] = 0x00;
				len = 2;
			} else {
				tmp[0] = b[i];
				tmp[1] = 0x00;
				tmp[2] = 0x00;
				len = 1;
				++i;
			}
			// line width in base64 should not extend 76 characters
			// so if it does...
			if (lines && linecount + 4 > 76) {
				sw.write("\r\n");
				linecount = 0;
			}
			// "len" is set to the number of bytes in the buffer to encode
			Base64.doEncode(tmp, len, sw);
			linecount += 4;
		}
		return sw.toString();
	}

	// do the encoding
	private Base64() {
		
		// empty
	}
}
