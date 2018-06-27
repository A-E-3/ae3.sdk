package ru.myx.sapi;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/*
 * Created on 01.06.2004
 */
/**
 * @author myx
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TextWriter extends Applet {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -4064998011458403568L;
	
	private static final int		DEVIATION			= 0x0A;
	
	private static final char		FIRST				= '-';
	
	private static final int[][]	_MINUS				= { // -
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0 },							};
	
	private static final int[][]	_DOT				= { // .
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },							};
	
	private static final int[][]	_SLASH				= { // /
			{ 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },							};
	
	private static final int[][]	_N0					= { // 0
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 1, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 1, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	_N1					= { // 1
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 1, 1, 1, 1, 1 },							};
	
	private static final int[][]	_N2					= { // 2
			{ 1, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 1 },							};
	
	private static final int[][]	_N3					= { // 3
			{ 1, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	_N4					= { // 4
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1 },
			{ 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 1 },							};
	
	private static final int[][]	_N5					= { // 5
			{ 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	_N6					= { // 6
			{ 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	_N7					= { // 7
			{ 1, 1, 1, 1, 1 },
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },							};
	
	private static final int[][]	_N8					= { // 8
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	_N9					= { // 9
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 1 },
			{ 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	_58					= { // :
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0 },							};
	
	private static final int[][]	_59					= { // ;
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 0, 0 },							};
	
	private static final int[][]	_LESS				= { // <
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 1, 0 },							};
	
	private static final int[][]	_EQU				= { // =
			{ 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 0 },							};
	
	private static final int[][]	_MORE				= { // >
			{ 0, 1, 0, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 0, 0 },							};
	
	private static final int[][]	_QMARK				= { // ?
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 0 },							};
	
	private static final int[][]	_AT					= { // @
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1 },
			{ 1, 0, 1, 1, 0 },
			{ 0, 1, 0, 0, 0 },							};
	
	private static final int[][]	A					= {
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	B					= {
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	C					= {
			{ 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 1 },							};
	
	private static final int[][]	D					= {
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	E					= {
			{ 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 1 },							};
	
	private static final int[][]	F					= {
			{ 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },							};
	
	private static final int[][]	G					= {
			{ 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 1, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 1 },							};
	
	private static final int[][]	H					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	I					= {
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	J					= {
			{ 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	K					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 1, 0 },
			{ 1, 1, 1, 0, 0 },
			{ 1, 0, 0, 1, 0 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	L					= {
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 1 },							};
	
	private static final int[][]	M					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 0, 1, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	N					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 0, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 1, 1 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	O					= {
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	P					= {
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 0 },
			{ 1, 0, 0, 0, 0 },							};
	
	private static final int[][]	Q					= {
			{ 0, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 1, 0 },
			{ 0, 1, 1, 0, 1 },							};
	
	private static final int[][]	R					= {
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	S					= {
			{ 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0 },
			{ 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0 },							};
	
	private static final int[][]	T					= {
			{ 1, 1, 1, 1, 1 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },							};
	
	private static final int[][]	U					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 1, 1, 0 },							};
	
	private static final int[][]	V					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },							};
	
	private static final int[][]	W					= {
			{ 1, 0, 0, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 1 },
			{ 0, 1, 0, 1, 0 },							};
	
	private static final int[][]	X					= {
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 1, 0 },
			{ 1, 0, 0, 0, 1 },							};
	
	private static final int[][]	Y					= {
			{ 1, 0, 0, 0, 1 },
			{ 0, 1, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 0, 1, 0, 0 },							};
	
	private static final int[][]	Z					= {
			{ 1, 1, 1, 1, 1 },
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 1, 0, 0 },
			{ 0, 1, 0, 0, 0 },
			{ 1, 1, 1, 1, 1 },							};
	
	private static final int[][][]	ALPHABET			= {
			TextWriter._MINUS,
			TextWriter._DOT,
			TextWriter._SLASH,
			TextWriter._N0,
			TextWriter._N1,
			TextWriter._N2,
			TextWriter._N3,
			TextWriter._N4,
			TextWriter._N5,
			TextWriter._N6,
			TextWriter._N7,
			TextWriter._N8,
			TextWriter._N9,
			TextWriter._58,
			TextWriter._59,
			TextWriter._LESS,
			TextWriter._EQU,
			TextWriter._MORE,
			TextWriter._QMARK,
			TextWriter._AT,
			TextWriter.A,
			TextWriter.B,
			TextWriter.C,
			TextWriter.D,
			TextWriter.E,
			TextWriter.F,
			TextWriter.G,
			TextWriter.H,
			TextWriter.I,
			TextWriter.J,
			TextWriter.K,
			TextWriter.L,
			TextWriter.M,
			TextWriter.N,
			TextWriter.O,
			TextWriter.P,
			TextWriter.Q,
			TextWriter.R,
			TextWriter.S,
			TextWriter.T,
			TextWriter.U,
			TextWriter.V,
			TextWriter.W,
			TextWriter.X,
			TextWriter.Y,
			TextWriter.Z								};
	
	/**
	 * @param textOriginal
	 * @param rgbBACK
	 * @param rgbTEXT
	 * @param image
	 */
	public static void drawImpl(
			final String textOriginal,
			final int rgbBACK,
			final int rgbTEXT,
			final BufferedImage image) {
		final String text = textOriginal.toUpperCase();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int pWidth = text.length() * 6 + 6 + 6 + 3;
		final int pHeight = 6 + 6 + 3;
		{
			for (int index = text.length() - 1; index >= 0; index--) {
				final char c = text.charAt( index );
				if (c >= TextWriter.FIRST && c <= 'z') {
					final int[][] letter = TextWriter.ALPHABET[c - TextWriter.FIRST];
					for (int yy = height, xidx = 0; yy > -pHeight; yy -= pHeight, xidx++) {
						final int xshift = -(xidx & 0x1) * (pWidth / 2) + index * 6;
						for (int xx = width; xx > -pWidth; xx -= pWidth) {
							for (int y = 4; y >= 0; y--) {
								for (int x = 4; x >= 0; x--) {
									if (letter[y][x] > 0) {
										final int xxx = xx + xshift + x;
										if (xxx < 0 || xxx >= width) {
											continue;
										}
										final int yyy = yy + y;
										if (yyy < 0 || yyy >= height) {
											continue;
										}
										if ((image.getRGB( xxx, yyy ) & 0xFFFFFF) == rgbBACK) {
											image.setRGB( xxx, yyy, rgbTEXT );
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * @param textOriginal
	 * @param image
	 */
	public static void drawImplMark(final String textOriginal, final BufferedImage image) {
		final int deviationBlack = TextWriter.DEVIATION;
		final int deviationWhite = 0xFF - TextWriter.DEVIATION;
		final int rgbBLACK = (deviationWhite << 16) + (deviationWhite << 8) + deviationWhite;
		final int rgbWHITE = (deviationBlack << 16) + (deviationBlack << 8) + deviationBlack;
		final String text = textOriginal.toUpperCase();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int pWidth = text.length() * 6 + 6 + 6 + 3;
		final int pHeight = 6 + 6 + 3;
		{
			for (int index = text.length() - 1; index >= 0; index--) {
				final char c = text.charAt( index );
				if (c >= TextWriter.FIRST && c <= 'z') {
					final int[][] letter = TextWriter.ALPHABET[c - TextWriter.FIRST];
					for (int yy = height, xidx = 0; yy > -pHeight; yy -= pHeight, xidx++) {
						final int xshift = -(xidx & 0x1) * (pWidth / 2) + index * 6;
						for (int xx = width; xx > -pWidth; xx -= pWidth) {
							for (int y = 4; y >= 0; y--) {
								for (int x = 4; x >= 0; x--) {
									if (letter[y][x] > 0) {
										final int xxx = xx + xshift + x;
										if (xxx < 0 || xxx >= width) {
											continue;
										}
										final int yyy = yy + y;
										if (yyy < 0 || yyy >= height) {
											continue;
										}
										final int rgb = image.getRGB( xxx, yyy ) & 0xFFFFFF;
										if (rgb == 0xFFFFFF) {
											image.setRGB( xxx, yyy, rgbBLACK );
											continue;
										}
										if (rgb == 0x000000) {
											image.setRGB( xxx, yyy, rgbWHITE );
											continue;
										}
										{
											final double deviation = TextWriter.DEVIATION / 255.0;
											final double r = ((rgb & 0xFF0000) >> 16) / 256.0;
											final double g = ((rgb & 0x00FF00) >> 8) / 256.0;
											final double b = (rgb & 0x0000FF) / 256.0;
											final int nr = (int) (256 * ((r > 0.5
													? r - deviation * 2 * (r - 0.5)
													: 0) + (r <= 0.5
													? r + deviation * 2 * (0.5 - r)
													: 0)));
											final int ng = (int) (256 * ((g > 0.5
													? g - deviation * 2 * (g - 0.5)
													: 0) + (g <= 0.5
													? g + deviation * 2 * (0.5 - g)
													: 0)));
											final int nb = (int) (256 * ((b > 0.5
													? b - deviation * 2 * (b - 0.5)
													: 0) + (b <= 0.5
													? b + deviation * 2 * (0.5 - b)
													: 0)));
											image.setRGB( xxx, yyy, (nr << 16) + (ng << 8) + nb );
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private final boolean			isStandalone	= false;
	
	private static BufferedImage	DSCN9760		= null;
	
	private static BufferedImage	DSCN9792		= null;
	
	/**
	 * @param key
	 * @param def
	 * @return string
	 */
	public String getParameter(final String key, final String def) {
		return this.isStandalone
				? System.getProperty( key, def )
				: this.getParameter( key ) != null
						? this.getParameter( key )
						: def;
	}
	
	@SuppressWarnings("unused")
	@Override
	public final void paint(final Graphics g) {
		if (g == null) {
			return;
		}
		
		if (true) {
			final BufferedImage image;
			if (TextWriter.DSCN9760 == null) {
				try {
					TextWriter.DSCN9760 = ImageIO.read( ImageIO.createImageInputStream( this.getClass()
							.getResourceAsStream( "DSCN9760-s.JPG" ) ) );
					TextWriter.DSCN9792 = ImageIO.read( ImageIO.createImageInputStream( this.getClass()
							.getResourceAsStream( "DSCN9792-s.JPG" ) ) );
				} catch (final IOException e) {
					throw new RuntimeException( e );
				}
			}
			image = new Random().nextBoolean()
					? TextWriter.DSCN9760
					: TextWriter.DSCN9792;
			TextWriter.drawImplMark( "http://music-cafe.ru", image );
			g.drawImage( image, 0, 0, null );
		} else {
			final String test = "http://music-cafe.ru 1234567890 =2+z-y";
			final BufferedImage image = new BufferedImage( this.getSize().width,
					this.getSize().height,
					BufferedImage.TYPE_INT_RGB );
			TextWriter.drawImpl( test, 0x000000, 0x444444, image );
			g.drawImage( image, 0, 0, null );
		}
	}
}
