/*
 * Created on 10.10.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sapi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.flow.Flow;

/**
 * @author myx
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 * 
 * @version 1.3, 6 May 2011
 * @since 400
 */
public class ImagingSAPI {
	private static final int	HEX_PRE_0	= '0';
	
	private static final int	HEX_PRE_A	= 'A' - 10;
	
	/**
	 * @param img
	 * @param width
	 * @param height
	 * @return image
	 */
	public static final BufferedImage bitmapResize(final BufferedImage img, final int width, final int height) {
		final int owidth = img.getWidth();
		final int oheight = img.getHeight();
		final int nwidth = width == -1
				? owidth * height / oheight
				: width;
		final int nheight = height == -1
				? oheight * width / owidth
				: height;
		final BufferedImage result = ImagingSAPI.createImage( nwidth, nheight, img.getColorModel() );
		final Graphics2D g = result.createGraphics();
		ImagingSAPI.bitmapResizeImpl( result, nwidth, nheight, g, img, owidth, oheight, nwidth, nheight );
		g.dispose();
		return result;
	}
	
	/**
	 * @param img
	 * @param color
	 * @param width
	 * @param height
	 * @return image
	 */
	public static final BufferedImage bitmapResizeCanvas(
			final BufferedImage img,
			final int color,
			final int width,
			final int height) {
		final BufferedImage result = ImagingSAPI.createImage( width, height, img.getColorModel() );
		final Graphics2D g = (Graphics2D) result.getGraphics();
		try {
			g.setBackground( new Color( color ) );
			g.clearRect( 0, 0, width, height );
			final int owidth = img.getWidth();
			final int oheight = img.getHeight();
			if (owidth <= width && oheight <= height) {
				return ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, owidth, oheight );
			}
			return 1.0 * owidth / width > 1.0 * oheight / height
					? ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, width, -1 )
					: ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, -1, height );
		} finally {
			g.dispose();
		}
	}
	
	/**
	 * @param img
	 * @param width
	 * @param height
	 * @return image
	 */
	public static final BufferedImage bitmapResizeCrop(final BufferedImage img, final int width, final int height) {
		final BufferedImage result = ImagingSAPI.createImage( width, height, img.getColorModel() );
		final Graphics2D g = (Graphics2D) result.getGraphics();
		try {
			final int owidth = img.getWidth();
			final int oheight = img.getHeight();
			if (owidth <= width && oheight <= height) {
				return ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, owidth, oheight );
			}
			if (1.0 * owidth / width > 1.0 * oheight / height) {
				return ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, -1, height );
			}
			return ImagingSAPI.bitmapResizeImpl( result, width, height, g, img, owidth, oheight, width, -1 );
		} finally {
			g.dispose();
		}
	}
	
	/**
	 * @param img
	 * @param width
	 * @param height
	 * @return image
	 */
	public static final BufferedImage bitmapResizeFit(final BufferedImage img, final int width, final int height) {
		final int owidth = img.getWidth();
		final int oheight = img.getHeight();
		if (owidth <= width && oheight <= height) {
			return img;
		}
		if (1.0 * owidth / width > 1.0 * oheight / height) {
			return ImagingSAPI.bitmapResize( img, width, -1 );
		}
		return ImagingSAPI.bitmapResize( img, -1, height );
	}
	
	private static final BufferedImage bitmapResizeImpl(
			final BufferedImage result,
			final int cwidth,
			final int cheight,
			final Graphics2D g,
			final BufferedImage img,
			final int owidth,
			final int oheight,
			final int rwidth,
			final int rheight) {
		final int nwidth = rwidth == -1
				? owidth * rheight / oheight
				: rwidth;
		final int nheight = rheight == -1
				? oheight * rwidth / owidth
				: rheight;
		final int shiftX = (cwidth - nwidth) / 2;
		final int shiftY = (cheight - nheight) / 2;
		if (owidth * oheight > 1600 * 1200 || nwidth * nheight > 1600 * 1200) {
			ImagingSAPI.internSetup2dRelaxed( g );
		} else {
			ImagingSAPI.internSetup2dQuality( g );
		}
		g.drawImage( img.getScaledInstance( nwidth, nheight, Image.SCALE_SMOOTH ), shiftX, shiftY, null );
		return result;
	}
	
	/**
	 * @param red
	 * @param green
	 * @param blue
	 * @return int
	 */
	public static final int colorForComponents(final int red, final int green, final int blue) {
		return (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
	}
	
	/**
	 * @param width
	 * @param height
	 * @return image
	 */
	public static final BufferedImage createImage(final int width, final int height) {
		return new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
	}
	
	/**
	 * @param width
	 * @param height
	 * @param model
	 * @return image
	 */
	public static final BufferedImage createImage(final int width, final int height, final ColorModel model) {
		return new BufferedImage( width, height, model.hasAlpha()
				? BufferedImage.TYPE_4BYTE_ABGR
				: BufferedImage.TYPE_3BYTE_BGR );
	}
	
	/**
	 * @param img
	 * @return message
	 * @throws IOException
	 */
	public static final BaseMessage encodeGif(final BufferedImage img) throws IOException {
		final TransferCopier copier = ImagingSAPI.encodeGifBinary( img );
		return Flow.binary( "IMAGING", //
				"ENCODED",
				new BaseNativeObject( "Content-Type", "image/gif" ),
				copier );
	}
	
	/**
	 * @param bitmaps
	 * @param delay
	 * @param loop
	 * @return message
	 * @throws IOException
	 */
	public static final BaseMessage encodeGif(final Collection<?> bitmaps, final int delay, final boolean loop)
			throws IOException {
		final TransferCopier copier = ImagingSAPI.encodeGifBinary( bitmaps, delay, loop );
		return Flow.binary( "IMAGING", //
				"ENCODED",
				new BaseNativeObject( "Content-Type", "image/gif" ),
				copier );
	}
	
	/**
	 * @param bitmaps
	 * @param delay
	 * @param loop
	 * @return copier
	 * @throws IOException
	 */
	public static final TransferCopier encodeGifBinary(final Collection<?> bitmaps, final int delay, final boolean loop)
			throws IOException {
		final Gif89Encoder gifenc = new Gif89Encoder();
		final TransferCollector collector = Transfer.createCollector();
		gifenc.setComments( "Built on the fly!" );
		gifenc.setTransparentIndex( -1 );
		gifenc.setUniformDelay( delay );
		gifenc.setLoopCount( loop
				? 0
				: 1 );
		for (final Object o : bitmaps) {
			if (o == null) {
				// ignore
			} else //
			if (o instanceof Image) {
				gifenc.addFrame( (Image) o );
			}
		}
		gifenc.encode( collector.getOutputStream() );
		return collector.toBinary();
	}
	
	/**
	 * @param img
	 * @return copier
	 * @throws IOException
	 */
	public static final TransferCopier encodeGifBinary(final Image img) throws IOException {
		final Gif89Encoder gifenc = new Gif89Encoder();
		final TransferCollector collector = Transfer.createCollector();
		gifenc.setComments( "Built on the fly!" );
		gifenc.setTransparentIndex( -1 );
		gifenc.addFrame( img );
		gifenc.getFrameAt( 0 ).setInterlaced( false );
		gifenc.encode( collector.getOutputStream() );
		return collector.toBinary();
	}
	
	/**
	 * @param img
	 * @return message
	 * @throws IOException
	 */
	public static final BaseMessage encodeJpeg(final BufferedImage img) throws IOException {
		final TransferCopier copier = ImagingSAPI.encodeJpegBinary( img );
		return Flow.binary( "IMAGING", //
				"ENCODED",
				new BaseNativeObject( "Content-Type", "image/jpeg" ),
				copier );
	}
	
	/**
	 * @param img
	 * @param progressive
	 * @param quality
	 * @return message
	 * @throws IOException
	 */
	public static final BaseMessage encodeJpeg(
			final BufferedImage img,
			final boolean progressive,
			final double quality) throws IOException {
		final TransferCopier copier = ImagingSAPI.encodeJpegBinary( img, progressive, quality );
		return Flow.binary( "IMAGING", //
				"ENCODED",
				new BaseNativeObject( "Content-Type", "image/jpeg" ),
				copier );
	}
	
	/**
	 * @param img
	 * @return copier
	 * @throws IOException
	 */
	public static final TransferCopier encodeJpegBinary(final BufferedImage img) throws IOException {
		{
			/**
			 * Java has a bug... it saves RGBA images to jpeg as it were CMYK
			 * images hence fucking the image... so we need to explicitly
			 * convert such images to a 3 channel RGB ones.
			 */
			final ColorModel model = img.getColorModel();
			if (model.hasAlpha() && model.getNumComponents() == 4 && model.getNumColorComponents() == 3) {
				final int width = img.getWidth();
				final int height = img.getHeight();
				final BufferedImage converted = new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
				final Graphics2D g = (Graphics2D) converted.getGraphics();
				try {
					g.drawImage( img, 0, 0, null );
				} finally {
					g.dispose();
				}
				return ImagingSAPI.encodeJpegBinary( converted );
			}
		}
		{
			final TransferCollector collector = Transfer.createCollector();
			ImageIO.write( img, "jpeg", collector.getOutputStream() );
			return collector.toBinary();
		}
	}
	
	/**
	 * @param img
	 * @param progressive
	 * @param quality
	 * @return copier
	 * @throws IOException
	 */
	public static final TransferCopier encodeJpegBinary(
			final BufferedImage img,
			final boolean progressive,
			final double quality) throws IOException {
		{
			/**
			 * Java has a bug... it saves RGBA images to jpeg as it were CMYK
			 * images hence fucking the image... so we need to explicitly
			 * convert such images to a 3 channel RGB ones.
			 */
			final ColorModel model = img.getColorModel();
			if (model.hasAlpha() && model.getNumComponents() == 4 && model.getNumColorComponents() == 3) {
				final int width = img.getWidth();
				final int height = img.getHeight();
				final BufferedImage converted = new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );
				final Graphics2D g = (Graphics2D) converted.getGraphics();
				try {
					g.drawImage( img, 0, 0, null );
				} finally {
					g.dispose();
				}
				return ImagingSAPI.encodeJpegBinary( converted, progressive, quality );
			}
		}
		final Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName( "jpeg" );
		if (!writers.hasNext()) {
			throw new IOException( "No image writer for 'jpeg' format found!" );
		}
		final ImageWriter writer = writers.next();
		final ImageWriteParam parameter = writer.getDefaultWriteParam();
		parameter.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
		parameter.setCompressionQuality( (float) quality );
		parameter.setProgressiveMode( progressive
				? ImageWriteParam.MODE_DEFAULT
				: ImageWriteParam.MODE_DISABLED );
		final TransferCollector collector = Transfer.createCollector();
		try (final ImageOutputStream output = new MemoryCacheImageOutputStream( collector.getOutputStream() )) {
			writer.setOutput( output );
			writer.write( null, new IIOImage( img, null, null ), parameter );
		}
		return collector.toBinary();
	}
	
	/**
	 * @param img
	 * @return message
	 * @throws IOException
	 */
	public static final BaseMessage encodePng(final BufferedImage img) throws IOException {
		final TransferCopier copier = ImagingSAPI.encodePngBinary( img );
		return Flow.binary( "IMAGING", //
				"ENCODED",
				new BaseNativeObject( "Content-Type", "image/png" ),
				copier );
	}
	
	/**
	 * @param img
	 * @return copier
	 * @throws IOException
	 */
	public static final TransferCopier encodePngBinary(final BufferedImage img) throws IOException {
		final TransferCollector collector = Transfer.createCollector();
		ImageIO.write( img, "png", collector.getOutputStream() );
		return collector.toBinary();
	}
	
	/**
	 * @param rgb
	 * @return string
	 */
	public static final String getColorCode(final int rgb) {
		final StringBuilder result = new StringBuilder();
		final int h1 = (rgb & 0x00000F) >> 0;
		final int h2 = (rgb & 0x0000F0) >> 4;
		final int h3 = (rgb & 0x000F00) >> 8;
		final int h4 = (rgb & 0x00F000) >> 12;
		final int h5 = (rgb & 0x0F0000) >> 16;
		final int h6 = (rgb & 0xF00000) >> 20;
		result.append( (char) (h6 < 10
				? ImagingSAPI.HEX_PRE_0 + h6
				: ImagingSAPI.HEX_PRE_A + h6) );
		result.append( (char) (h5 < 10
				? ImagingSAPI.HEX_PRE_0 + h5
				: ImagingSAPI.HEX_PRE_A + h5) );
		result.append( (char) (h4 < 10
				? ImagingSAPI.HEX_PRE_0 + h4
				: ImagingSAPI.HEX_PRE_A + h4) );
		result.append( (char) (h3 < 10
				? ImagingSAPI.HEX_PRE_0 + h3
				: ImagingSAPI.HEX_PRE_A + h3) );
		result.append( (char) (h2 < 10
				? ImagingSAPI.HEX_PRE_0 + h2
				: ImagingSAPI.HEX_PRE_A + h2) );
		result.append( (char) (h1 < 10
				? ImagingSAPI.HEX_PRE_0 + h1
				: ImagingSAPI.HEX_PRE_A + h1) );
		return result.toString();
	}
	
	/**
	 * @param object
	 * @return dimensions
	 */
	public static final ImageSize getImageDimensions(final Object object) {
		assert object != null;
		try {
			if (object instanceof BufferedImage) {
				final BufferedImage image = (BufferedImage) object;
				final int rw = image.getWidth();
				final int rh = image.getHeight();
				return new ru.myx.sapi.ImageSize( rw, rh );
			}
			if (object instanceof java.io.InputStream) {
				return ru.myx.sapi.ImageHelper.getImageSizes( (java.io.InputStream) object );
			}
			if (object instanceof byte[]) {
				return ru.myx.sapi.ImageHelper.getImageSizes( (byte[]) object );
			}
			if (object instanceof TransferBuffer) {
				final TransferBuffer buffer = (TransferBuffer) object;
				return ru.myx.sapi.ImageHelper.getImageSizes( buffer.toDirectArray() );
			}
			if (object instanceof TransferCopier) {
				final TransferCopier copier = (TransferCopier) object;
				return ru.myx.sapi.ImageHelper.getImageSizes( copier.nextDirectArray() );
			}
			if (object instanceof BaseMessage) {
				final TransferCopier copier = ((BaseMessage) object).toBinary().getBinary();
				return ru.myx.sapi.ImageHelper.getImageSizes( copier.nextDirectArray() );
			}
			if (object instanceof Value<?>) {
				final Object baseValue = ((Value<?>) object).baseValue();
				if (baseValue != null && baseValue != object) {
					return ImagingSAPI.getImageDimensions( baseValue );
				}
			}
			throw new Error( "Imaging: Unknown data type: " + object.getClass().getName() );
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * @param g
	 * @param image
	 * @param x
	 * @param y
	 */
	public static final void graphicsDrawImage(final Graphics g, final Image image, final int x, final int y) {
		final PixelGrabber pg = new PixelGrabber( image, 0, 0, -1, -1, true );
		try {
			if (!pg.grabPixels()) {
				throw new RuntimeException( "UNAVAILABLE" );
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException( "INTERRUPTED" );
		}
		g.drawImage( image, x, y, null );
	}
	
	/**
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public static final void graphicsDrawLine(final Graphics g, final int x1, final int y1, final int x2, final int y2) {
		g.drawLine( x1, y1, x2, y2 );
	}
	
	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static final void graphicsDrawOval(final Graphics g, final int x, final int y, final int w, final int h) {
		g.drawOval( x, y, w, h );
	}
	
	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static final void graphicsDrawRect(final Graphics g, final int x, final int y, final int w, final int h) {
		g.drawRect( x, y, w, h );
	}
	
	/**
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param face
	 * @param bold
	 * @param italic
	 * @param size
	 * @param horizontal
	 *            specifies horizontal alignment of text box (by width), use:
	 *            <ul>
	 *            <li><b>0</b> to have text aligned so its center point is in
	 *            the given <b>x</b> coordinate;</li>
	 *            <li>use <b>1</b> (actually any integer value greater than
	 *            zero) for left edge of the text box to be in the given
	 *            <b>x</b> coordinate;</li>
	 *            <li>use <b>-1</b> (actually any integer value less than zero)
	 *            for right edge of the text box to be in the given <b>x</b>
	 *            coordinate.</li>
	 *            </ul>
	 * 
	 * @param vertical
	 *            specifies vertical alignment of text box (by height), use:
	 *            <ul>
	 *            <li><b>0</b> to have text aligned so its center point is in
	 *            the given <b>y</b> coordinate;</li>
	 *            <li>use <b>1</b> (actually any integer value greater than
	 *            zero) for top edge of the text box to be in the given <b>y</b>
	 *            coordinate;</li>
	 *            <li>use <b>-1</b> (actually any integer value less than zero)
	 *            for bottom edge of the text box to be in the given <b>y</b>
	 *            coordinate.</li>
	 *            </ul>
	 */
	public static final void graphicsDrawText(
			final Graphics2D g,
			final String text,
			final int x,
			final int y,
			final String face,
			final boolean bold,
			final boolean italic,
			final int size,
			final int horizontal,
			final int vertical) {
		final Font font = new Font( face, bold
				? italic
						? Font.BOLD | Font.ITALIC
						: Font.BOLD
				: italic
						? Font.ITALIC
						: Font.PLAIN, size );
		g.setFont( font );
		final float xc = (float) (horizontal == 0
				? x - font.getStringBounds( text, g.getFontRenderContext() ).getWidth() / 2
				: horizontal < 0
						? x - font.getStringBounds( text, g.getFontRenderContext() ).getWidth()
						: x);
		final LineMetrics metrics = font.getLineMetrics( text, g.getFontRenderContext() );
		final float yc = vertical == 0
				? y + metrics.getHeight() / 2
				: vertical > 0
						? y + metrics.getAscent()
						: y - metrics.getDescent();
		g.drawString( text, xc, yc );
	}
	
	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static final void graphicsFillOval(final Graphics g, final int x, final int y, final int w, final int h) {
		g.fillOval( x, y, w, h );
	}
	
	/**
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public static final void graphicsFillRect(final Graphics g, final int x, final int y, final int w, final int h) {
		g.fillRect( x, y, w, h );
	}
	
	/**
	 * @param g
	 * @param color
	 */
	public static final void graphicsSetColor(final Graphics g, final int color) {
		g.setColor( new Color( color ) );
	}
	
	/**
	 * @param g
	 * @param color
	 */
	public static final void graphicsSetXorMode(final Graphics g, final int color) {
		g.setXORMode( new Color( color ) );
	}
	
	/**
	 * @param o
	 * @return image
	 * @throws Exception
	 */
	public static final Image imageForBinary(final Object o) throws Exception {
		if (o == null) {
			return null;
		}
		if (o instanceof TransferBuffer) {
			return ImagingSAPI.imageForBuffer( (TransferBuffer) o );
		}
		if (o instanceof TransferCopier) {
			return ImagingSAPI.imageForCopier( (TransferCopier) o );
		}
		if (o instanceof BaseMessage) {
			final BaseMessage message = (BaseMessage) o;
			if (message.isFile()) {
				return ImagingSAPI.imageForFile( message.getFile() );
			}
			/**
			 * TODO: add character for SVG? 8-)
			 */
			//
			return ImagingSAPI.imageForCopier( message.toBinary().getBinary() );
		}
		if (o instanceof byte[]) {
			return ImagingSAPI.imageForBytes( (byte[]) o );
		}
		if (o instanceof Value<?>) {
			final Object baseValue = ((Value<?>) o).baseValue();
			if (baseValue != null && baseValue != o) {
				return ImagingSAPI.imageForBinary( baseValue );
			}
		}
		return null;
	}
	
	private static final BufferedImage imageForBuffer(final TransferBuffer buffer) throws Exception {
		return ImageIO.read( ImageIO.createImageInputStream( buffer.toInputStream() ) );
	}
	
	private static final BufferedImage imageForBytes(final byte[] bytes) throws Exception {
		return ImageIO.read( ImageIO.createImageInputStream( bytes ) );
	}
	
	private static final BufferedImage imageForCopier(final TransferCopier copier) throws Exception {
		return ImageIO.read( ImageIO.createImageInputStream( copier.nextInputStream() ) );
	}
	
	private static final BufferedImage imageForFile(final File file) throws Exception {
		return ImageIO.read( ImageIO.createImageInputStream( file ) );
	}
	
	private static void internSetup2dQuality(final Graphics2D g) {
		g.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
		g.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
		g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
		g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		g.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
	}
	
	private static void internSetup2dRelaxed(final Graphics2D g) {
		g.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT );
		g.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE );
		g.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
		g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		g.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
		g.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
		g.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
	}
	
	/**
	 * @param image
	 * @param text
	 * @return
	 */
	public static final BufferedImage makeWatermarks(final BufferedImage image, final String text) {
		TextWriter.drawImplMark( text, image );
		return image;
	}
	
	/**
	 * @param image
	 * @param text
	 * @param background
	 * @param color
	 * @return
	 */
	public static final BufferedImage makeWatermarks(
			final BufferedImage image,
			final String text,
			final int background,
			final int color) {
		TextWriter.drawImpl( text, background, color, image );
		return image;
	}
}
