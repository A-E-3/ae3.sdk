//******************************************************************************
// Gif89Encoder.java
//******************************************************************************
package ru.myx.sapi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//==============================================================================
/** This is the central class of a JDK 1.1 compatible GIF encoder that, AFAIK, supports more
 * features of the extended GIF spec than any other Java open source encoder. Some sections of the
 * source are lifted or adapted from Jef Poskanzer's <cite>Acme GifEncoder </cite> (so please see
 * the <a href="../readme.txt">readme </a> containing his notice), but much of it, including nearly
 * all of the present class, is original code. My main motivation for writing a new encoder was to
 * support animated GIFs, but the package also adds support for embedded textual comments.
 * <p>
 * There are still some limitations. For instance, animations are limited to a single global color
 * table. But that is usually what you want anyway, so as to avoid irregularities on some displays.
 * (So this is not really a limitation, but a "disciplinary feature" :) Another rather more serious
 * restriction is that the total number of RGB colors in a given input-batch mustn't exceed 256.
 * Obviously, there is an opening here for someone who would like to add a color-reducing
 * preprocessor.
 * <p>
 * The encoder, though very usable in its present form, is at bottom only a partial implementation
 * skewed toward my own particular needs. Hence a couple of caveats are in order. (1) During
 * development it was in the back of my mind that an encoder object should be reusable - i.e., you
 * should be able to make multiple calls to encode() on the same object, with or without intervening
 * frame additions or changes to options. But I haven't reviewed the code with such usage in mind,
 * much less tested it, so it's likely I overlooked something. (2) The encoder classes aren't thread
 * safe, so use caution in a context where access is shared by multiple threads. (Better yet, finish
 * the library and re-release it :)
 * <p>
 * There follow a couple of simple examples illustrating the most common way to use the encoder,
 * i.e., to encode AWT Image objects created elsewhere in the program. Use of some of the most
 * popular format options is also shown, though you will want to peruse the API for additional
 * features.
 *
 * <p>
 * <strong>Animated GIF Example </strong>
 *
 * <pre>
 *
 *
 *      import net.jmge.gif.Gif89Encoder;
 *      // ...
 *      void writeAnimatedGIF(Image[] still_images,
 *                            String annotation,
 *                            boolean looped,
 *                            double frames_per_second,
 *                            OutputStream out) throws IOException
 *      {
 *        Gif89Encoder gifenc = new Gif89Encoder();
 *        for (int i = 0; i &lt; still_images.length; ++i)
 *          gifenc.addFrame(still_images[i]);
 *        gifenc.setComments(annotation);
 *        gifenc.setLoopCount(looped ? 0 : 1);
 *        gifenc.setUniformDelay((int) Math.round(100 / frames_per_second));
 *        gifenc.encode(out);
 *      }
 *
 *
 * </pre>
 *
 * <strong>Static GIF Example </strong>
 *
 * <pre>
 *
 *
 *      import net.jmge.gif.Gif89Encoder;
 *      // ...
 *      void writeNormalGIF(Image img,
 *                          String annotation,
 *                          int transparent_index,  // pass -1 for none
 *                          boolean interlaced,
 *                          OutputStream out) throws IOException
 *      {
 *        Gif89Encoder gifenc = new Gif89Encoder(img);
 *        gifenc.setComments(annotation);
 *        gifenc.setTransparentIndex(transparent_index);
 *        gifenc.getFrameAt(0).setInterlaced(interlaced);
 *        gifenc.encode(out);
 *      }
 *
 *
 * </pre>
 *
 * @version 0.90 beta (15-Jul-2000)
 * @author J. M. G. Elliott (tep@jmge.net)
 * @see Gif89Frame
 * @see Gif89FrameDirect
 * @see Gif89FrameIndex */
@SuppressWarnings("javadoc")
public class Gif89Encoder {

	private Dimension dispDim = new Dimension(0, 0);

	private final GifColorTable colorTable;

	private int bgIndex = 0;

	private int loopCount = 1;

	private String theComments;

	private final List<Gif89Frame> vFrames = new ArrayList<>();

	// ----------------------------------------------------------------------------
	/** Use this default constructor if you'll be adding multiple frames constructed from RGB data
	 * (i.e., AWT Image objects or ARGB-pixel arrays). */
	public Gif89Encoder() {
		
		// empty color table puts us into "palette autodetect" mode
		this.colorTable = new GifColorTable();
	}

	// ----------------------------------------------------------------------------
	/** This constructor installs a user color table, overriding the detection of of a palette from
	 * ARBG pixels.
	 *
	 * Use of this constructor imposes a couple of restrictions: (1) Frame objects can't be of type
	 * DirectGif89Frame (2) Transparency, if desired, must be set explicitly.
	 *
	 * @param colors
	 *            Array of color values; no more than 256 colors will be read, since that's the
	 *            limit for a GIF. */
	public Gif89Encoder(final Color[] colors) {
		
		this.colorTable = new GifColorTable(colors);
	}

	// ----------------------------------------------------------------------------
	/** Convenience constructor for encoding a static GIF from index-model data. Adds a single frame
	 * as specified.
	 *
	 * @param colors
	 *            Array of color values; no more than 256 colors will be read, since that's the
	 *            limit for a GIF.
	 * @param width
	 *            Width of the GIF bitmap.
	 * @param height
	 *            Height of same.
	 * @param ci_pixels
	 *            Array of color-index pixels no less than width * height in length. */
	public Gif89Encoder(final Color[] colors, final int width, final int height, final byte ci_pixels[]) {
		
		this(colors);
		this.addFrame(width, height, ci_pixels);
	}

	// ----------------------------------------------------------------------------
	/** Like the default except that it also adds a single frame, for conveniently encoding a static
	 * GIF from an image.
	 *
	 * @param static_image
	 *            Any Image object that supports pixel-grabbing.
	 * @exception IOException
	 *                See the addFrame() methods. */
	public Gif89Encoder(final Image static_image) throws IOException {
		
		this();
		this.addFrame(static_image);
	}

	// ----------------------------------------------------------------------------
	private void accommodateFrame(final Gif89Frame gf) {
		
		this.dispDim.width = Math.max(this.dispDim.width, gf.getWidth());
		this.dispDim.height = Math.max(this.dispDim.height, gf.getHeight());
		this.colorTable.processPixels(gf);
	}

	// ----------------------------------------------------------------------------
	/** Add a Gif89Frame frame to the end of the internal sequence. Note that there are restrictions
	 * on the Gif89Frame type: if the encoder object was constructed with an explicit color table,
	 * an attempt to add a DirectGif89Frame will throw an exception.
	 *
	 * @param gf
	 *            An externally constructed Gif89Frame. */
	public void addFrame(final Gif89Frame gf) {
		
		this.accommodateFrame(gf);
		this.vFrames.add(gf);
	}

	// ----------------------------------------------------------------------------
	/** Convenience version of addFrame() that takes a Java Image, internally constructing the
	 * requisite DirectGif89Frame.
	 *
	 * @param image
	 *            Any Image object that supports pixel-grabbing.
	 * @exception IOException
	 *                If either (1) pixel-grabbing fails, (2) the aggregate cross-frame RGB color
	 *                count exceeds 256, or (3) this encoder object was constructed with an explicit
	 *                color table. */
	public void addFrame(final Image image) throws IOException {
		
		this.addFrame(new Gif89FrameDirect(image));
	}

	// ----------------------------------------------------------------------------
	/** The index-model convenience version of addFrame().
	 *
	 * @param width
	 *            Width of the GIF bitmap.
	 * @param height
	 *            Height of same.
	 * @param ci_pixels
	 *            Array of color-index pixels no less than width * height in length. */
	public void addFrame(final int width, final int height, final byte ci_pixels[]) {
		
		this.addFrame(new Gif89FrameIndex(width, height, ci_pixels));
	}

	// ----------------------------------------------------------------------------
	/** After adding your frame(s) and setting your options, simply call this method to write the
	 * GIF to the passed stream. Multiple calls are permissible if for some reason that is useful to
	 * your application. (The method simply encodes the current state of the object with no thought
	 * to previous calls.)
	 *
	 * @param out
	 *            The stream you want the GIF written to.
	 * @exception IOException
	 *                If a write error is encountered. */
	public void encode(final OutputStream out) throws IOException {
		
		final int nframes = this.getFrameCount();
		final boolean is_sequence = nframes > 1;

		// N.B. must be called before writing screen descriptor
		this.colorTable.closePixelProcessing();

		// write GIF HEADER
		Gif89Util.ascii("GIF89a", out);

		// write global blocks
		this.writeLogicalScreenDescriptor(out);
		this.colorTable.encode(out);
		if (is_sequence && this.loopCount != 1) {
			this.writeNetscapeExtension(out);
		}
		if (this.theComments != null && this.theComments.length() > 0) {
			this.writeCommentExtension(out);
		}

		// write out the control and rendering data for each frame
		for (int i = 0; i < nframes; ++i) {
			this.vFrames.get(i).encode(out, is_sequence, this.colorTable.getDepth(), this.colorTable.getTransparent());
		}

		// write GIF TRAILER
		out.write(';');

		out.flush();
	}

	// ----------------------------------------------------------------------------
	/** Get a reference back to a Gif89Frame object by position.
	 *
	 * @param index
	 *            Zero-based index of the frame in the sequence.
	 * @return Gif89Frame object at the specified position (or null if no such frame). */
	public Gif89Frame getFrameAt(final int index) {
		
		return this.isOk(index)
			? (Gif89Frame) this.vFrames.get(index)
			: null;
	}

	// ----------------------------------------------------------------------------
	/** Get the number of frames that have been added so far.
	 *
	 * @return Number of frame items. */
	public int getFrameCount() {
		
		return this.vFrames.size();
	}

	// ----------------------------------------------------------------------------
	/** Like addFrame() except that the frame is inserted at a specific point in the sequence rather
	 * than appended.
	 *
	 * @param index
	 *            Zero-based index at which to insert frame.
	 * @param gf
	 *            An externally constructed Gif89Frame. */
	public void insertFrame(final int index, final Gif89Frame gf) {
		
		this.accommodateFrame(gf);
		this.vFrames.add(index, gf);
	}

	// ----------------------------------------------------------------------------
	private boolean isOk(final int frameIndex) {
		
		return frameIndex >= 0 && frameIndex < this.vFrames.size();
	}

	// ----------------------------------------------------------------------------
	/** Specify some textual comments to be embedded in GIF.
	 *
	 * @param comments
	 *            String containing ASCII comments. */
	public void setComments(final String comments) {
		
		this.theComments = comments;
	}

	// ----------------------------------------------------------------------------
	/** Sets attributes of the multi-image display area, if applicable.
	 *
	 * @param dim
	 *            Width/height of display. (Default: largest detected frame size)
	 * @param background
	 *            Color table index of background color. (Default: 0)
	 * @see Gif89Frame#setPosition */
	public void setLogicalDisplay(final Dimension dim, final int background) {
		
		this.dispDim = new Dimension(dim);
		this.bgIndex = background;
	}

	// ----------------------------------------------------------------------------
	/** Set animation looping parameter, if applicable.
	 *
	 * @param count
	 *            Number of times to play sequence. Special value of 0 specifies indefinite looping.
	 *            (Default: 1) */
	public void setLoopCount(final int count) {
		
		this.loopCount = count;
	}

	// ----------------------------------------------------------------------------
	/** Set the color table index for the transparent color, if any.
	 *
	 * @param index
	 *            Index of the color that should be rendered as transparent, if any. A value of -1
	 *            turns off transparency. (Default: -1) */
	public void setTransparentIndex(final int index) {
		
		this.colorTable.setTransparent(index);
	}

	// ----------------------------------------------------------------------------
	/** A convenience method for setting the "animation speed". It simply sets the delay parameter
	 * for each frame in the sequence to the supplied value. Since this is actually frame-level
	 * rather than animation-level data, take care to add your frames before calling this method.
	 *
	 * @param interval
	 *            Interframe interval in centiseconds. */
	public void setUniformDelay(final int interval) {
		
		for (int i = 0; i < this.vFrames.size(); ++i) {
			this.vFrames.get(i).setDelay(interval);
		}
	}

	// ----------------------------------------------------------------------------
	private void writeCommentExtension(final OutputStream os) throws IOException {
		
		os.write('!'); // GIF Extension Introducer
		os.write(0xfe); // Comment Extension Label

		final int remainder = this.theComments.length() % 255;
		final int nsubblocks_full = this.theComments.length() / 255;
		final int nsubblocks = nsubblocks_full + (remainder > 0
			? 1
			: 0);
		int ibyte = 0;
		for (int isb = 0; isb < nsubblocks; ++isb) {
			final int size = isb < nsubblocks_full
				? 255
				: remainder;

			os.write(size);
			Gif89Util.ascii(this.theComments.substring(ibyte, ibyte + size), os);
			ibyte += size;
		}

		os.write(0); // block terminator
	}

	// ----------------------------------------------------------------------------
	private void writeLogicalScreenDescriptor(final OutputStream os) throws IOException {
		
		Gif89Util.leShort(this.dispDim.width, os);
		Gif89Util.leShort(this.dispDim.height, os);

		// write 4 fields, packed into a byte (bitfieldsize:value)
		// global color map present? (1:1)
		// bits per primary color less 1 (3:7)
		// sorted color table? (1:0)
		// bits per pixel less 1 (3:varies)
		os.write(0xf0 | this.colorTable.getDepth() - 1);

		// write background color index
		os.write(this.bgIndex);

		// Jef Poskanzer's notes on the next field, for our possible
		// edification:
		// Pixel aspect ratio - 1:1.
		// Putbyte( (byte) 49, outs );
		// Java's GIF reader currently has a bug, if the aspect ratio byte is
		// not zero it throws an ImageFormatException. It doesn't know that
		// 49 means a 1:1 aspect ratio. Well, whatever, zero works with all
		// the other decoders I've tried so it probably doesn't hurt.

		// OK, if it's good enough for Jef, it's definitely good enough for us:
		os.write(0);
	}

	// ----------------------------------------------------------------------------
	private void writeNetscapeExtension(final OutputStream os) throws IOException {
		// n.b. most software seems to interpret the count as a repeat count
		// (i.e., interations beyond 1) rather than as an iteration count
		// (thus, to avoid repeating we have to omit the whole extension)

		os.write('!'); // GIF Extension Introducer
		os.write(0xff); // Application Extension Label

		os.write(11); // application ID block size
		Gif89Util.ascii("NETSCAPE2.0", os); // application ID
		// data

		os.write(3); // data sub-block size
		os.write(1); // a looping flag? dunno

		// we finally write the relevent data
		Gif89Util.leShort(
				this.loopCount > 1
					? this.loopCount - 1
					: 0,
				os);

		os.write(0); // block terminator
	}
}

// ==============================================================================

class GifColorTable {

	// ----------------------------------------------------------------------------
	private static int computeColorDepth(final int colorcount) {
		
		// color depth = log-base-2 of maximum number of simultaneous colors,
		// i.e.
		// bits per color-index pixel
		if (colorcount <= 2) {
			return 1;
		}
		if (colorcount <= 4) {
			return 2;
		}
		if (colorcount <= 16) {
			return 4;
		}
		return 8;
	}

	// the palette of ARGB colors, packed as returned by Color.getRGB()
	private int[] theColors = new int[256];

	// other basic attributes
	private int colorDepth;

	private int transparentIndex = -1;

	// color

	// indices

	// these fields track color-index info across frames
	private int ciCount = 0; // count of distinct
	
	// ----------------------------------------------------------------------------
	GifColorTable() {
		
		// empty
	}

	// ----------------------------------------------------------------------------
	GifColorTable(final Color[] colors) {
		
		final int n2copy = Math.min(this.theColors.length, colors.length);
		for (int i = 0; i < n2copy; ++i) {
			this.theColors[i] = colors[i].getRGB();
		}
	}

	// ----------------------------------------------------------------------------
	void closePixelProcessing() // must be called before
	// encode()
	{
		
		this.colorDepth = GifColorTable.computeColorDepth(this.ciCount);
	}

	// ----------------------------------------------------------------------------
	void encode(final OutputStream os) throws IOException {
		
		// size of palette written is the smallest power of 2 that can accomdate
		// the number of RGB colors detected (or largest color index, in case of
		// index pixels)
		final int palette_size = 1 << this.colorDepth;
		for (int i = 0; i < palette_size; ++i) {
			if (i < this.ciCount) {
				os.write(this.theColors[i] >> 16 & 0xff);
				os.write(this.theColors[i] >> 8 & 0xff);
				os.write(this.theColors[i] & 0xff);
			} else {
				os.write(0);
				os.write(0);
				os.write(0);
			}
		}
	}

	// ----------------------------------------------------------------------------
	// This method accomplishes three things:
	// (1) converts the passed rgb pixels to indexes into our rgb lookup table
	// (2) fills the rgb table as new colors are encountered
	// (3) looks for transparent pixels so as to set the transparent index
	// The information is cumulative across multiple calls.
	//
	// (Note: some of the logic is borrowed from Jef Poskanzer's code.)
	// ----------------------------------------------------------------------------
	private void filterPixels(final Gif89FrameDirect dgf) {
		
		final int[] argb_pixels = (int[]) dgf.getPixelSource();
		this.theColors = Quantize.quantizeImage(argb_pixels, dgf.getWidth(), dgf.getHeight(), 255);
		this.ciCount = this.theColors.length;
		if (this.transparentIndex == -1) {
			this.transparentIndex = this.ciCount;
		}
		final byte[] ci_pixels = dgf.getPixelSink();
		for (int i = argb_pixels.length - 1; i >= 0; --i) {
			ci_pixels[i] = (byte) argb_pixels[i];
		}

		/* if (ciLookup == null) throw new IOException("RGB frames require palette autodetection");
		 * int[] argb_pixels = (int[]) dgf.getPixelSource(); byte[] ci_pixels = dgf.getPixelSink();
		 * int npixels = argb_pixels.length; for (int i = 0; i < npixels; ++i) { int argb =
		 * argb_pixels[i]; // handle transparency if ((argb >>> 24) < 0x80) // transparent pixel? if
		 * (transparentIndex == -1) // first transparent color encountered? transparentIndex =
		 * ciCount; // record its index else if (argb != theColors[transparentIndex]) // different
		 * pixel value? { // collapse all transparent pixels into one color index ci_pixels[i] =
		 * (byte) transparentIndex; continue; // CONTINUE - index already in table } // try to look
		 * up the index in our "reverse" color table int color_index = ciLookup.getPaletteIndex(argb
		 * & 0xffffff); if (color_index == -1) // if it isn't in there yet { if (ciCount == 256)
		 * throw new IOException("can't encode as GIF (> 256 colors)"); // store color in our
		 * accumulating palette theColors[ciCount] = argb; // store index in reverse color table
		 * ciLookup.put(argb & 0xffffff, ciCount); // send color index to our output array
		 * ci_pixels[i] = (byte) ciCount; // increment count of distinct color indices ++ciCount; }
		 * else // we've already snagged color into our palette ci_pixels[i] = (byte) color_index;
		 * // just send filtered pixel } */
	}

	// ----------------------------------------------------------------------------
	int getDepth() {
		
		return this.colorDepth;
	}

	// ----------------------------------------------------------------------------
	int getTransparent() {
		
		return this.transparentIndex;
	}

	// ----------------------------------------------------------------------------
	void processPixels(final Gif89Frame gf) {
		
		if (gf instanceof Gif89FrameDirect) {
			this.filterPixels((Gif89FrameDirect) gf);
		} else {
			this.trackPixelUsage((Gif89FrameIndex) gf);
		}
	}

	// ----------------------------------------------------------------------------
	// default: -1 (no transparency)
	void setTransparent(final int colorIndex) {
		
		this.transparentIndex = colorIndex;
	}

	// ----------------------------------------------------------------------------
	private void trackPixelUsage(final Gif89FrameIndex igf) {
		
		final byte[] ci_pixels = (byte[]) igf.getPixelSource();
		final int npixels = ci_pixels.length;
		for (int i = 0; i < npixels; ++i) {
			if (ci_pixels[i] >= this.ciCount) {
				this.ciCount = ci_pixels[i] + 1;
			}
		}
	}
}

// ==============================================================================
// We're doing a very simple linear hashing thing here, which seems sufficient
// for our needs. I make no claims for this approach other than that it seems
// an improvement over doing a brute linear search for each pixel on the one
// hand, and creating a Java object for each pixel (if we were to use a Java
// Map) on the other. Doubtless my little hash could be improved by
// tuning the capacity (at the very least). Suggestions are welcome.
// ==============================================================================

class ReverseColorMap {

	// private static class ColorRecord {
	// int rgb;
	// int ipalette;
	// ColorRecord(int rgb, int ipalette)
	// {
	// this.rgb = rgb;
	// this.ipalette = ipalette;
	// }
	// }
	//
	// // I wouldn't really know what a good hashing capacity is, having missed
	// out
	// // on data structures and algorithms class :) Alls I know is, we've got a
	// lot
	// // more space than we have time. So let's try a sparse table with a
	// maximum
	// // load of about 1/8 capacity.
	// private static final int HCAPACITY = 2053; // a nice prime number
	//
	// // our hash table proper
	// private ColorRecord[] hTable = new ColorRecord[HCAPACITY];
	//
	// //----------------------------------------------------------------------------
	// // Assert: rgb is not negative (which is the same as saying, be sure the
	// // alpha transparency byte - i.e., the high byte - has been masked out).
	// //----------------------------------------------------------------------------
	// int getPaletteIndex(int rgb)
	// {
	// ColorRecord rec;
	//
	// for ( int itable = rgb % hTable.length;
	// (rec = hTable[itable]) != null && rec.rgb != rgb;
	// itable = ++itable % hTable.length
	// )
	// ;
	//
	// if (rec != null)
	// return rec.ipalette;
	//
	// return -1;
	// }
	//
	// //----------------------------------------------------------------------------
	// // Assert: (1) same as above; (2) rgb key not already present
	// //----------------------------------------------------------------------------
	// void put(int rgb, int ipalette)
	// {
	// int itable;
	//
	// for ( itable = rgb % hTable.length;
	// hTable[itable] != null;
	// itable = ++itable % hTable.length
	// )
	// ;
	//
	// hTable[itable] = new ColorRecord(rgb, ipalette);
	// }
	//

	// ----------------------------------------------------------------------------
	/** A simple driver to test the installation and to demo usage. Put the JAR on your classpath
	 * and run ala <blockquote>java net.jmge.gif.Gif89Encoder {filename} </blockquote> The filename
	 * must be either (1) a JPEG file with extension 'jpg', for conversion to a static GIF, or (2) a
	 * file containing a list of GIFs and/or JPEGs, one per line, to be combined into an animated
	 * GIF. The output will appear in the current directory as 'gif89out.gif'.
	 * <p>
	 * (N.B. This test program will abort if the input file(s) exceed(s) 256 total RGB colors, so in
	 * its present form it has no value as a generic JPEG to GIF converter. Also, when multiple
	 * files are input, you need to be wary of the total color count, regardless of file type.)
	 *
	 * @param args
	 *            Command-line arguments, only the first of which is used, as mentioned above. */
	public static void main(final String[] args) {
		
		try {

			final Toolkit tk = Toolkit.getDefaultToolkit();
			try (final OutputStream out = new BufferedOutputStream(new FileOutputStream("gif89out.gif"))) {

				if (args[0].toUpperCase().endsWith(".JPG")) {
					new Gif89Encoder(tk.getImage(args[0])).encode(out);
				} else {
					final Gif89Encoder ge = new Gif89Encoder();

					try (final BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
						String line;
						while ((line = in.readLine()) != null) {
							ge.addFrame(tk.getImage(line.trim()));
						}
						ge.setLoopCount(0); // let's loop indefinitely
						ge.encode(out);
					}
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
