package ru.myx.sapi;

/**
 * A single Node in the tree.
 */
final class QuantizeNode {
	/**
	 * Figure out the distance between this node and som color.
	 * 
	 * @param color
	 * @param r
	 * @param g
	 * @param b
	 * @return int
	 */
	private static final int distance(final int color, final int r, final int g, final int b) {
		return Quantize.SQUARES[(color >> 16 & 0xFF) - r + Quantize.MAX_RGB]
				+ Quantize.SQUARES[(color >> 8 & 0xFF) - g + Quantize.MAX_RGB]
				+ Quantize.SQUARES[(color >> 0 & 0xFF) - b + Quantize.MAX_RGB];
	}
	
	private QuantizeCube	cube;
	
	// parent node
	QuantizeNode			parent;
	
	// child nodes
	QuantizeNode			child[];
	
	private int				nchild;
	
	// our index within our parent
	private final int		id;
	
	// our level within the tree
	private final int		level;
	
	// our color midpoint
	int						mid_red;
	
	int						mid_green;
	
	int						mid_blue;
	
	// the pixel count for this node and all children
	int						number_pixels;
	
	// the pixel count for this node
	int						unique;
	
	// the sum of all pixels contained in this node
	int						total_red;
	
	int						total_green;
	
	int						total_blue;
	
	// used to build the colormap
	int						color_number;
	
	QuantizeNode(final QuantizeCube cube) {
		this.cube = cube;
		this.parent = this;
		this.child = new QuantizeNode[8];
		this.id = 0;
		this.level = 0;
		
		this.number_pixels = Integer.MAX_VALUE;
		
		this.mid_red = Quantize.MAX_RGB + 1 >> 1;
		this.mid_green = Quantize.MAX_RGB + 1 >> 1;
		this.mid_blue = Quantize.MAX_RGB + 1 >> 1;
	}
	
	QuantizeNode(final QuantizeNode parent, final int id, final int level) {
		this.cube = parent.cube;
		this.parent = parent;
		this.child = new QuantizeNode[8];
		this.id = id;
		this.level = level;
		
		// add to the cube
		++this.cube.nodes;
		if (level == this.cube.depth) {
			++this.cube.colors;
		}
		
		// add to the parent
		++parent.nchild;
		parent.child[id] = this;
		
		// figure out our midpoint
		final int bi = 1 << Quantize.MAX_TREE_DEPTH - level >> 1;
		this.mid_red = parent.mid_red + ((id & 1) > 0
				? bi
				: -bi);
		this.mid_green = parent.mid_green + ((id & 2) > 0
				? bi
				: -bi);
		this.mid_blue = parent.mid_blue + ((id & 4) > 0
				? bi
				: -bi);
	}
	
	/*
	 * ClosestColor traverses the color cube tree at a particular node and
	 * determines which colormap entry best represents the input color.
	 */
	void closestColor(final int red, final int green, final int blue, final QuantizeSearch search) {
		if (this.nchild != 0) {
			for (int id = 0; id < 8; id++) {
				if (this.child[id] != null) {
					this.child[id].closestColor( red, green, blue, search );
				}
			}
		}
		
		if (this.unique != 0) {
			final int color = this.cube.colormap[this.color_number];
			final int distance = QuantizeNode.distance( color, red, green, blue );
			if (distance < search.distance) {
				search.distance = distance;
				search.color_number = this.color_number;
			}
		}
	}
	
	/*
	 * colormap traverses the color cube tree and notes each colormap entry. A
	 * colormap entry is any node in the color cube tree where the number of
	 * unique colors is not zero.
	 */
	void colormap() {
		if (this.nchild != 0) {
			for (int id = 0; id < 8; id++) {
				if (this.child[id] != null) {
					this.child[id].colormap();
				}
			}
		}
		if (this.unique != 0) {
			final int r = (this.total_red + (this.unique >> 1)) / this.unique;
			final int g = (this.total_green + (this.unique >> 1)) / this.unique;
			final int b = (this.total_blue + (this.unique >> 1)) / this.unique;
			this.cube.colormap[this.cube.colors] = 0xFF << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
			this.color_number = this.cube.colors++;
		}
	}
	
	/**
	 * Remove this child node, and make sure our parent absorbs our pixel
	 * statistics.
	 */
	private void pruneChild() {
		--this.parent.nchild;
		this.parent.unique += this.unique;
		this.parent.total_red += this.total_red;
		this.parent.total_green += this.total_green;
		this.parent.total_blue += this.total_blue;
		this.parent.child[this.id] = null;
		--this.cube.nodes;
		this.cube = null;
		this.parent = null;
	}
	
	/**
	 * Prune the lowest layer of the tree.
	 */
	void pruneLevel() {
		if (this.nchild != 0) {
			for (int id = 0; id < 8; id++) {
				if (this.child[id] != null) {
					this.child[id].pruneLevel();
				}
			}
		}
		if (this.level == this.cube.depth) {
			this.pruneChild();
		}
	}
	
	/**
	 * Remove any nodes that have fewer than threshold pixels. Also, as long as
	 * we're walking the tree: - figure out the color with the fewest pixels -
	 * recalculate the total number of colors in the tree
	 * 
	 * @param threshold
	 * @param next_threshold
	 * @return int
	 */
	int reduce(final int threshold, int next_threshold) {
		if (this.nchild != 0) {
			for (int id = 0; id < 8; id++) {
				if (this.child[id] != null) {
					next_threshold = this.child[id].reduce( threshold, next_threshold );
				}
			}
		}
		if (this.number_pixels <= threshold) {
			this.pruneChild();
		} else {
			if (this.unique != 0) {
				this.cube.colors++;
			}
			if (this.number_pixels < next_threshold) {
				next_threshold = this.number_pixels;
			}
		}
		return next_threshold;
	}
	
	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		if (this.parent == this) {
			buf.append( "root" );
		} else {
			buf.append( "node" );
		}
		buf.append( ' ' );
		buf.append( this.level );
		buf.append( " [" );
		buf.append( this.mid_red );
		buf.append( ',' );
		buf.append( this.mid_green );
		buf.append( ',' );
		buf.append( this.mid_blue );
		buf.append( ']' );
		return new String( buf );
	}
}
