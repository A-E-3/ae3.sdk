package ru.myx.sapi;

import java.io.IOException;
import java.io.InputStream;

import ru.myx.ae3.binary.Transfer;

/**
 * Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
class ImageHelper {
	private static final String	SIGNATURE_PNG	= Character.toString( (char) 0x89 ) + "PNG\r\n";
	
	private static final String	SIGNATURE_GIF	= "GIF8";
	
	static final boolean compareToString(final byte[] bytes, final int pos, final String string) {
		if (bytes == null) {
			return false;
		}
		for (int i = 0; i < string.length(); ++i) {
			if (bytes[i + pos] != (byte) string.charAt( i )) {
				return false;
			}
		}
		return true;
	}
	
	static final int getByteValue(final byte Byte) {
		return Byte < 0
				? 256 + Byte
				: (int) Byte;
	}
	
	/**
	 * Extracts info 'bout image dimensions from PNG, GIF and JPEG encoded
	 * images. Width of an image is stored in lower word of result. Height of an
	 * image is stored in higher word of result.
	 * 
	 * @param image
	 * @return dimensions
	 */
	static final ImageSize getImageSizes(final byte[] image) {
		if (image == null || image.length == 0) {
			return null;
		}
		int width = 0;
		int height = 0;
		if (ImageHelper.compareToString( image, 0, ImageHelper.SIGNATURE_GIF )) {
			width = ImageHelper.getByteValue( image[7] ) * 256 + ImageHelper.getByteValue( image[6] );
			height = ImageHelper.getByteValue( image[9] ) * 256 + ImageHelper.getByteValue( image[8] );
		} else //
		if (ImageHelper.compareToString( image, 0, ImageHelper.SIGNATURE_PNG )) {
			width = ImageHelper.getByteValue( image[8 + 4 + 4 + 0] )
					* 256
					* 256
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 1] )
					* 256
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 2] )
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 3] );
			height = ImageHelper.getByteValue( image[8 + 4 + 4 + 4] )
					* 256
					* 256
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 5] )
					* 256
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 6] )
					* 256
					+ ImageHelper.getByteValue( image[8 + 4 + 4 + 7] );
		} else //
		if (ImageHelper.getByteValue( image[0] ) == 255 && ImageHelper.getByteValue( image[1] ) == 216) {
			/* JPEG */
			int current = 2;
			while (true) {
				// cycle through segments
				final int segmentHeader = ImageHelper.getByteValue( image[current] );
				final int segmentType = ImageHelper.getByteValue( image[current + 1] );
				if (segmentHeader != 255) {
					break;
				}
				if (segmentType == 192 || segmentType == 194) {
					// Image info segment
					width = ImageHelper.getByteValue( image[current + 7] )
							* 256
							+ ImageHelper.getByteValue( image[current + 8] );
					height = ImageHelper.getByteValue( image[current + 5] )
							* 256
							+ ImageHelper.getByteValue( image[current + 6] );
					break;
				}
				final int currentSegmentSize = ImageHelper.getByteValue( image[current + 2] )
						* 256
						+ ImageHelper.getByteValue( image[current + 3] );
				current += currentSegmentSize + 2;
			}
		}
		return width == 0
				? null
				: new ImageSize( width, height );
	}
	
	/**
	 * Extracts info 'bout image dimensions from PNG, GIF and JPEG encoded
	 * images. Width of an image is stored in lower word of result. Height of an
	 * image is stored in higher word of result.
	 * 
	 * @param image
	 * @return dimensions
	 * @throws IOException
	 */
	static final ImageSize getImageSizes(final InputStream image) throws IOException {
		return ImageHelper.getImageSizes( Transfer.createBuffer( image ).toDirectArray() );
	}
	
	private ImageHelper() {
		// empty
	}
}
