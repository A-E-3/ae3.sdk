// ******************************************************************************
// Gif89Frame.java
// ******************************************************************************
package ru.myx.sapi;

import java.awt.Point;
import java.io.IOException;
import java.io.OutputStream;

// ==============================================================================
/** First off, just to dispel any doubt, this class and its subclasses have nothing to do with GUI
 * "frames" such as java.awt.Frame. We merely use the term in its very common sense of a still
 * picture in an animation sequence. It's hoped that the restricted context will prevent any
 * confusion.
 * <p>
 * An instance of this class is used in conjunction with a Gif89Encoder object to represent and
 * encode a single static image and its associated "control" data. A Gif89Frame doesn't know or care
 * whether it is encoding one of the many animation frames in a GIF movie, or the single bitmap in a
 * "normal" GIF. (FYI, this design mirrors the encoded GIF structure.)
 * <p>
 * Since Gif89Frame is an abstract class we don't instantiate it directly, but instead create
 * instances of its concrete subclasses, IndexGif89Frame and DirectGif89Frame. From the API
 * standpoint, these subclasses differ only in the sort of data their instances are constructed
 * from. Most folks will probably work with DirectGif89Frame, since it can be constructed from a
 * java.awt.Image object, but the lower-level IndexGif89Frame class offers advantages in specialized
 * circumstances. (Of course, in routine situations you might not explicitly instantiate any frames
 * at all, instead letting Gif89Encoder's convenience methods do the honors.)
 * <p>
 * As far as the public API is concerned, objects in the Gif89Frame hierarchy interact with a
 * Gif89Encoder only via the latter's methods for adding and querying frames. (As a side note, you
 * should know that while Gif89Encoder objects are permanently modified by the addition of
 * Gif89Frames, the reverse is NOT true. That is, even though the ultimate encoding of a Gif89Frame
 * may be affected by the context its parent encoder object provides, it retains its original
 * condition and can be reused in a different context.)
 * <p>
 * The core pixel-encoding code in this class was essentially lifted from Jef Poskanzer's well-known
 * <cite>Acme GifEncoder </cite>, so please see the <a href="../readme.txt">readme </a> containing
 * his notice.
 *
 * @version 0.90 beta (15-Jul-2000)
 * @author J. M. G. Elliott (tep@jmge.net)
 * @see Gif89Encoder
 * @see Gif89FrameDirect
 * @see Gif89FrameIndex */
@SuppressWarnings("javadoc")
abstract class Gif89Frame {

	// // Public "Disposal Mode" constants ////

	/** The animated GIF renderer shall decide how to dispose of this Gif89Frame's display area.
	 *
	 * @see Gif89Frame#setDisposalMode */
	public static final int DM_UNDEFINED = 0;

	/** The animated GIF renderer shall take no display-disposal action.
	 *
	 * @see Gif89Frame#setDisposalMode */
	public static final int DM_LEAVE = 1;

	/** The animated GIF renderer shall replace this Gif89Frame's area with the background color.
	 *
	 * @see Gif89Frame#setDisposalMode */
	public static final int DM_BGCOLOR = 2;

	/** The animated GIF renderer shall replace this Gif89Frame's area with the previous frame's
	 * bitmap.
	 *
	 * @see Gif89Frame#setDisposalMode */
	public static final int DM_REVERT = 3;

	// // Bitmap variables set in package subclass constructors ////
	int theWidth = -1;

	int theHeight = -1;

	byte[] ciPixels;

	// // GIF graphic frame control options ////
	private Point thePosition = new Point(0, 0);

	private boolean isInterlaced;

	private int csecsDelay;

	private int disposalCode = Gif89Frame.DM_LEAVE;

	// ----------------------------------------------------------------------------
	Gif89Frame() {
		
		// empty
	} // package-visible default constructor

	// ----------------------------------------------------------------------------
	void encode(final OutputStream os, final boolean epluribus, final int colorDepth, final int transparentIndex) throws IOException {
		
		this.writeGraphicControlExtension(os, epluribus, transparentIndex);
		this.writeImageDescriptor(os);
		new GifPixelsEncoder(this.theWidth, this.theHeight, this.ciPixels, this.isInterlaced, colorDepth).encode(os);
	}

	// ----------------------------------------------------------------------------
	int getHeight() {
		
		return this.theHeight;
	}

	// ----------------------------------------------------------------------------
	byte[] getPixelSink() {
		
		return this.ciPixels;
	}

	// ----------------------------------------------------------------------------
	abstract Object getPixelSource();

	// ----------------------------------------------------------------------------
	int getWidth() {
		
		return this.theWidth;
	}

	// ----------------------------------------------------------------------------
	/** Set the between-frame interval.
	 *
	 * @param interval
	 *            Centiseconds to wait before displaying the subsequent frame. (Default: 0) */
	public void setDelay(final int interval) {
		
		this.csecsDelay = interval;
	}

	// ----------------------------------------------------------------------------
	/** Setting this option determines (in a cooperative GIF-viewer) what will be done with this
	 * frame's display area before the subsequent frame is displayed. For instance, a setting of
	 * DM_BGCOLOR can be used for erasure when redrawing with displacement.
	 *
	 * @param code
	 *            One of the four int constants of the Gif89Frame.DM_* series. (Default:
	 *            DM_LEAVE) */
	public void setDisposalMode(final int code) {
		
		this.disposalCode = code;
	}

	// ----------------------------------------------------------------------------
	/** Set or clear the interlace flag.
	 *
	 * @param b
	 *            true if you want interlacing. (Default: false) */
	public void setInterlaced(final boolean b) {
		
		this.isInterlaced = b;
	}

	// ----------------------------------------------------------------------------
	/** Set the position of this frame within a larger animation display space.
	 *
	 * @param p
	 *            Coordinates of the frame's upper left corner in the display space. (Default: The
	 *            logical display's origin [0, 0])
	 * @see Gif89Encoder#setLogicalDisplay */
	public void setPosition(final Point p) {
		
		this.thePosition = new Point(p);
	}

	// ----------------------------------------------------------------------------
	private void writeGraphicControlExtension(final OutputStream os, final boolean epluribus, final int itransparent) throws IOException {
		
		final int transflag = itransparent == -1
			? 0
			: 1;
		if (transflag == 1 || epluribus) // using
		// transparency or
		// animating ?
		{
			os.write('!'); // GIF Extension Introducer
			os.write(0xf9); // Graphic Control Label
			os.write(4); // subsequent data block size
			os.write(this.disposalCode << 2 | transflag); // packed
			// fields
			// (1
			// byte)
			Gif89Util.leShort(this.csecsDelay, os); // delay
			// field (2
			// bytes)
			os.write(itransparent); // transparent index field
			os.write(0); // block terminator
		}
	}

	// ----------------------------------------------------------------------------
	private void writeImageDescriptor(final OutputStream os) throws IOException {
		
		os.write(','); // Image Separator
		Gif89Util.leShort(this.thePosition.x, os);
		Gif89Util.leShort(this.thePosition.y, os);
		Gif89Util.leShort(this.theWidth, os);
		Gif89Util.leShort(this.theHeight, os);
		os.write(
				this.isInterlaced
					? 0x40
					: 0); // packed fields
		// (1 byte)
	}
}

// ==============================================================================

class GifPixelsEncoder {

	private static final int EOF = -1;

	private static final int BITS = 12;

	private static final int HSIZE = 5003; // 80%
	
	// occupancy

	private static final int maxCode(final int n_bits) {
		
		return (1 << n_bits) - 1;
	}

	private final int imgW, imgH;

	private final byte[] pixAry;

	private final boolean wantInterlaced;

	private final int initCodeSize;

	// raster data navigators
	private int countDown;

	private int xCur, yCur;

	// ****************************************************************************
	// (J.E.) The logic of the next two methods is largely intact from
	// Jef Poskanzer. Some stylistic changes were made for consistency sake,
	// plus the second method accesses the pixel value from a prefiltered linear
	// array. That's about it.
	// ****************************************************************************

	private int curPass;

	private int n_bits; // number
	
	// of
	// bits/code

	// ****************************************************************************
	// (J.E.) I didn't touch Jef Poskanzer's code from this point on. (Well, OK,
	// I changed the name of the sole outside method it accesses.) I figure
	// if I have no idea how something works, I shouldn't play with it :)
	//
	// Despite its unencapsulated structure, this section is actually highly
	// self-contained. The calling code merely calls compress(), and the present
	// code calls nextPixel() in the caller. That's the sum total of their
	// communication. I could have dumped it in a separate class with a callback
	// via an interface, but it didn't seem worth messing with.
	// ****************************************************************************

	// GIFCOMPR.C - GIF Image compression routines
	//
	// Lempel-Ziv compression based on 'compress'. GIF modifications by
	// David Rowley (mgardi@watdcsu.waterloo.edu)

	// General DEFINEs

	private final int maxbits = GifPixelsEncoder.BITS; // user
	
	// settable
	// max
	// #

	private int maxcode; // maximum
	
	// code,
	// given

	// GIF Image compression - modified 'compress'
	//
	// Based on: compress.c - File compression ala IEEE Computer, June 1984.
	//
	// By Authors: Spencer W. Thomas (decvax!harpo!utah-cs!utah-gr!thomas)
	// Jim McKie (decvax!mcvax!jim)
	// Steve Davies (decvax!vax135!petsd!peora!srd)
	// Ken Turkowski (decvax!decwrl!turtlevax!ken)
	// James A. Woods (decvax!ihnp4!ames!jaw)
	// Joe Orost (decvax!vax135!petsd!joe)

	private final int maxmaxcode = 1 << GifPixelsEncoder.BITS; // should
	
	private final int[] htab = new int[GifPixelsEncoder.HSIZE];

	// bits/code

	private final int[] codetab = new int[GifPixelsEncoder.HSIZE];

	// n_bits

	private final int hsize = GifPixelsEncoder.HSIZE; // for
	
	// dynamic
	// table
	// sizing

	// NEVER
	// generate
	// this code

	private int free_ent = 0; // first
	
	// unused
	// entry

	// block compression parameters -- after all codes are used up,
	// and compression rate changes, start over.
	private boolean clear_flg = false;

	private int gInitBits;

	private int ClearCode;

	private int EOFCode;

	private int cur_accum = 0;

	// Algorithm: use open addressing double hashing (no chaining) on the
	// prefix code / next character combination. We do a variant of Knuth's
	// algorithm D (vol. 3, sec. 6.4) along with G. Knott's relatively-prime
	// secondary probe. Here, the modular division first probe is gives way
	// to a faster exclusive-or manipulation. Also do block compression with
	// an adaptive reset, whereby the code table is cleared when the compression
	// ratio decreases, but after the table fills. The variable-length output
	// codes are re-sized at this point, and a special CLEAR code is generated
	// for the decompressor. Late addition: construct the table according to
	// file size for noticeable speed improvement on small files. Please direct
	// questions about this implementation to ames!jaw.

	private int cur_bits = 0;

	private final int masks[] = {
			0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF, 0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF
	};

	// Number of characters so far in this 'packet'
	private int a_count;

	// Define the storage for the packet accumulator
	private final byte[] accum = new byte[256];

	// output
	//
	// Output the given code.
	// Inputs:
	// code: A n_bits-bit integer. If == -1, then EOF. This assumes
	// that n_bits =< wordsize - 1.
	// Outputs:
	// Outputs code to the file.
	// Assumptions:
	// Chars are 8 bits long.
	// Algorithm:
	// Maintain a BITS character long buffer (so that 8 codes will
	// fit in it exactly). Use the VAX insv instruction to insert each
	// code in turn. When the buffer fills up empty it and start over.

	// ----------------------------------------------------------------------------
	GifPixelsEncoder(final int width, final int height, final byte[] pixels, final boolean interlaced, final int color_depth) {
		
		this.imgW = width;
		this.imgH = height;
		this.pixAry = pixels;
		this.wantInterlaced = interlaced;
		this.initCodeSize = Math.max(2, color_depth);
	}

	// ----------------------------------------------------------------------------
	// Bump the 'xCur' and 'yCur' to point to the next pixel.
	// ----------------------------------------------------------------------------
	private void bumpPosition() {
		
		// Bump the current X position
		++this.xCur;

		// If we are at the end of a scan line, set xCur back to the beginning
		// If we are interlaced, bump the yCur to the appropriate spot,
		// otherwise, just increment it.
		if (this.xCur == this.imgW) {
			this.xCur = 0;

			if (!this.wantInterlaced) {
				++this.yCur;
			} else {
				switch (this.curPass) {
					case 0 :
						this.yCur += 8;
						if (this.yCur >= this.imgH) {
							++this.curPass;
							this.yCur = 4;
						}
						break;
					case 1 :
						this.yCur += 8;
						if (this.yCur >= this.imgH) {
							++this.curPass;
							this.yCur = 2;
						}
						break;
					case 2 :
						this.yCur += 4;
						if (this.yCur >= this.imgH) {
							++this.curPass;
							this.yCur = 1;
						}
						break;
					case 3 :
						this.yCur += 2;
						break;
					default :
				}
			}
		}
	}

	// Set up the 'byte output' routine
	private void charInit() {
		
		this.a_count = 0;
	}

	// Add a character to the end of the current packet, and if it is 254
	// characters, flush the packet to disk.
	private void charOut(final byte c, final OutputStream outs) throws IOException {
		
		this.accum[this.a_count++] = c;
		if (this.a_count >= 254) {
			this.flush_char(outs);
		}
	}

	// Clear out the hash table

	// table clear for block compress
	private void cl_block(final OutputStream outs) throws IOException {
		
		this.cl_hash(this.hsize);
		this.free_ent = this.ClearCode + 2;
		this.clear_flg = true;

		this.output(this.ClearCode, outs);
	}

	// reset code table
	private void cl_hash(final int hsize) {
		
		for (int i = 0; i < hsize; ++i) {
			this.htab[i] = -1;
		}
	}

	// GIF Specific routines

	private void compress(final int init_bits, final OutputStream outs) throws IOException {
		
		int fcode;
		int i /* = 0 */;
		int c;
		int ent;
		int disp;
		int hsize_reg;
		int hshift;

		// Set up the globals: g_init_bits - initial number of bits
		this.gInitBits = init_bits;

		// Set up the necessary values
		this.clear_flg = false;
		this.n_bits = this.gInitBits;
		this.maxcode = GifPixelsEncoder.maxCode(this.n_bits);

		this.ClearCode = 1 << init_bits - 1;
		this.EOFCode = this.ClearCode + 1;
		this.free_ent = this.ClearCode + 2;

		this.charInit();

		ent = this.nextPixel();

		hshift = 0;
		for (fcode = this.hsize; fcode < 65536; fcode *= 2) {
			++hshift;
		}
		hshift = 8 - hshift; // set hash code range bound

		hsize_reg = this.hsize;
		this.cl_hash(hsize_reg); // clear hash table

		this.output(this.ClearCode, outs);

		outer_loop : while ((c = this.nextPixel()) != GifPixelsEncoder.EOF) {
			fcode = (c << this.maxbits) + ent;
			i = c << hshift ^ ent; // xor hashing

			if (this.htab[i] == fcode) {
				ent = this.codetab[i];
				continue;
			}
			if (this.htab[i] >= 0) // non-empty slot

			{
				disp = hsize_reg - i; // secondary hash (after G.
				// Knott)
				if (i == 0) {
					disp = 1;
				}
				do {
					if ((i -= disp) < 0) {
						i += hsize_reg;
					}

					if (this.htab[i] == fcode) {
						ent = this.codetab[i];
						continue outer_loop;
					}
				} while (this.htab[i] >= 0);
			}
			this.output(ent, outs);
			ent = c;
			if (this.free_ent < this.maxmaxcode) {
				this.codetab[i] = this.free_ent++; // code
				// ->
				// hashtable
				this.htab[i] = fcode;
			} else {
				this.cl_block(outs);
			}
		}
		// Put out the final code.
		this.output(ent, outs);
		this.output(this.EOFCode, outs);
	}

	// ----------------------------------------------------------------------------
	void encode(final OutputStream os) throws IOException {
		
		os.write(this.initCodeSize); // write "initial code
		// size"
		// byte

		this.countDown = this.imgW * this.imgH; // reset
		// navigation
		// variables
		this.xCur = this.yCur = this.curPass = 0;

		this.compress(this.initCodeSize + 1, os); // compress
		// and
		// write the pixel
		// data

		os.write(0); // write block terminator
	}

	// Flush the packet to disk, and reset the accumulator
	private void flush_char(final OutputStream outs) throws IOException {
		
		if (this.a_count > 0) {
			outs.write(this.a_count);
			outs.write(this.accum, 0, this.a_count);
			this.a_count = 0;
		}
	}

	// ----------------------------------------------------------------------------
	// Return the next pixel from the image
	// ----------------------------------------------------------------------------
	private int nextPixel() {
		
		if (this.countDown == 0) {
			return GifPixelsEncoder.EOF;
		}

		--this.countDown;

		final byte pix = this.pixAry[this.yCur * this.imgW + this.xCur];

		this.bumpPosition();

		return pix & 0xff;
	}

	private void output(final int code, final OutputStream outs) throws IOException {
		
		this.cur_accum &= this.masks[this.cur_bits];

		if (this.cur_bits > 0) {
			this.cur_accum |= code << this.cur_bits;
		} else {
			this.cur_accum = code;
		}

		this.cur_bits += this.n_bits;

		while (this.cur_bits >= 8) {
			this.charOut((byte) (this.cur_accum & 0xff), outs);
			this.cur_accum >>= 8;
			this.cur_bits -= 8;
		}

		// If the next entry is going to be too big for the code size,
		// then increase it, if possible.
		if (this.free_ent > this.maxcode || this.clear_flg) {
			if (this.clear_flg) {
				this.maxcode = GifPixelsEncoder.maxCode(this.n_bits = this.gInitBits);
				this.clear_flg = false;
			} else {
				++this.n_bits;
				if (this.n_bits == this.maxbits) {
					this.maxcode = this.maxmaxcode;
				} else {
					this.maxcode = GifPixelsEncoder.maxCode(this.n_bits);
				}
			}
		}

		if (code == this.EOFCode) {
			// At EOF, write the rest of the buffer.
			while (this.cur_bits > 0) {
				this.charOut((byte) (this.cur_accum & 0xff), outs);
				this.cur_accum >>= 8;
				this.cur_bits -= 8;
			}

			this.flush_char(outs);
		}
	}
}
