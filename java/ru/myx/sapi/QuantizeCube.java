/**
 * 
 */
package ru.myx.sapi;

final class QuantizeCube {
	private final int			pixels[];
	
	private final int			width;
	
	private final int			height;
	
	private final int			maxColors;
	
	int							colormap[];
	
	private final QuantizeNode	root;
	
	int							depth;
	
	// counter for the number of colors in the cube. this gets
	// recalculated often.
	int							colors;
	
	// counter for the number of nodes in the tree
	int							nodes;
	
	QuantizeCube(final int pixels[], final int width, final int height, final int maxColors) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.maxColors = maxColors;
		
		int i = maxColors;
		for (this.depth = 1; i != 0; this.depth++) {
			i /= 4;
		}
		if (this.depth > 1) {
			--this.depth;
		}
		if (this.depth > Quantize.MAX_TREE_DEPTH) {
			this.depth = Quantize.MAX_TREE_DEPTH;
		} else //
		if (this.depth < 2) {
			this.depth = 2;
		}
		
		this.root = new QuantizeNode( this );
	}
	
	/*
	 * Procedure assignment generates the output image from the pruned tree. The
	 * output image consists of two parts: (1) A color map, which is an array of
	 * color descriptions (RGB triples) for each color present in the output
	 * image; (2) A pixel array, which represents each pixel as an index into
	 * the color map array. First, the assignment phase makes one pass over the
	 * pruned color description tree to establish the image's color map. For
	 * each node with n2 > 0, it divides Sr, Sg, and Sb by n2. This produces the
	 * mean color of all pixels that classify no lower than this node. Each of
	 * these colors becomes an entry in the color map. Finally, the assignment
	 * phase reclassifies each pixel in the pruned tree to identify the deepest
	 * node containing the pixel's color. The pixel's value in the pixel array
	 * becomes the index of this node's mean color in the color map.
	 */
	void assignment() {
		this.colormap = new int[this.colors];
		
		this.colors = 0;
		this.root.colormap();
		
		final int pixels[] = this.pixels;
		
		final int width = this.width;
		final int height = this.height;
		
		final QuantizeSearch search = new QuantizeSearch();
		
		// convert to indexed color
		for (int x = width; x-- > 0;) {
			for (int y = height; y-- > 0;) {
				final int pixel = pixels[x + y * this.width];
				final int red = pixel >> 16 & 0xFF;
				final int green = pixel >> 8 & 0xFF;
				final int blue = pixel >> 0 & 0xFF;
				
				// walk the tree to find the cube containing that color
				QuantizeNode node = this.root;
				for (;;) {
					final int id = (red > node.mid_red
							? 1
							: 0) << 0 | (green > node.mid_green
							? 1
							: 0) << 1 | (blue > node.mid_blue
							? 1
							: 0) << 2;
					if (node.child[id] == null) {
						break;
					}
					node = node.child[id];
				}
				
				// Find the closest color.
				search.distance = Integer.MAX_VALUE;
				node.parent.closestColor( red, green, blue, search );
				pixels[x + y * this.width] = search.color_number;
			}
		}
	}
	
	/*
	 * Procedure Classification begins by initializing a color description tree
	 * of sufficient depth to represent each possible input color in a leaf.
	 * However, it is impractical to generate a fully-formed color description
	 * tree in the classification phase for realistic values of cmax. If colors
	 * components in the input image are quantized to k-bit precision, so that
	 * cmax= 2k-1, the tree would need k levels below the root node to allow
	 * representing each possible input color in a leaf. This becomes
	 * prohibitive because the tree's total number of nodes is 1 +
	 * sum(i=1,k,8k). A complete tree would require 19,173,961 nodes for k = 8,
	 * cmax = 255. Therefore, to avoid building a fully populated tree,
	 * QUANTIZE: (1) Initializes data structures for nodes only as they are
	 * needed; (2) Chooses a maximum depth for the tree as a function of the
	 * desired number of colors in the output image (currently log2(colormap
	 * size)). For each pixel in the input image, classification scans downward
	 * from the root of the color description tree. At each level of the tree it
	 * identifies the single node which represents a cube in RGB space
	 * containing It updates the following data for each such node:
	 * number_pixels : Number of pixels whose color is contained in the RGB cube
	 * which this node represents; unique : Number of pixels whose color is not
	 * represented in a node at lower depth in the tree; initially, n2 = 0 for
	 * all nodes except leaves of the tree. total_red/green/blue : Sums of the
	 * red, green, and blue component values for all pixels not classified at a
	 * lower depth. The combination of these sums and n2 will ultimately
	 * characterize the mean color of a set of pixels represented by this node.
	 */
	void classification() {
		final int pixels[] = this.pixels;
		
		final int width = this.width;
		final int height = this.height;
		
		// convert to indexed color
		for (int x = width; x-- > 0;) {
			for (int y = height; y-- > 0;) {
				final int pixel = pixels[x + y * this.width];
				final int red = pixel >> 16 & 0xFF;
				final int green = pixel >> 8 & 0xFF;
				final int blue = pixel >> 0 & 0xFF;
				
				// a hard limit on the number of nodes in the tree
				if (this.nodes > Quantize.MAX_NODES) {
					System.out.println( "pruning" );
					this.root.pruneLevel();
					--this.depth;
				}
				
				// walk the tree to depth, increasing the
				// number_pixels count for each node
				QuantizeNode node = this.root;
				for (int level = 1; level <= this.depth; ++level) {
					final int id = (red > node.mid_red
							? 1
							: 0) << 0 | (green > node.mid_green
							? 1
							: 0) << 1 | (blue > node.mid_blue
							? 1
							: 0) << 2;
					if (node.child[id] == null) {
						new QuantizeNode( node, id, level ).getClass();
					}
					node = node.child[id];
					node.number_pixels += Quantize.SHIFT[level];
				}
				
				++node.unique;
				node.total_red += red;
				node.total_green += green;
				node.total_blue += blue;
			}
		}
	}
	
	/*
	 * reduction repeatedly prunes the tree until the number of nodes with
	 * unique > 0 is less than or equal to the maximum number of colors allowed
	 * in the output image. When a node to be pruned has offspring, the pruning
	 * procedure invokes itself recursively in order to prune the tree from the
	 * leaves upward. The statistics of the node being pruned are always added
	 * to the corresponding data in that node's parent. This retains the pruned
	 * node's color characteristics for later averaging.
	 */
	void reduction() {
		int threshold = 1;
		while (this.colors > this.maxColors) {
			this.colors = 0;
			threshold = this.root.reduce( threshold, Integer.MAX_VALUE );
		}
	}
}
