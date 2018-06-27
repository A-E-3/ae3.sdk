package ru.myx.ae3.l2.skin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.LayoutEngine;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.pack.PackImpl;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;

/**
 * @author myx
 * 		
 */
public class SkinImpl extends PackImpl implements Skin {
	
	/**
	 * @param root
	 * @return
	 */
	public static final Entry getLayoutsFolder(final Entry root) {
		
		assert root != null : "Skin root is NULL!";
		assert root.isContainer() : "Skin root should be a container!";
		final Entry folder = root.relative("layouts", null);
		if (folder == null || !folder.isExist()) {
			return null;
		}
		if (!folder.isContainer()) {
			assert false : "layouts folder is not a container!";
		}
		return folder;
	}
	
	/**
	 * @param root
	 * @return
	 */
	public static final Map<String, LayoutDefinition<TargetContext<?>>> loadLayouts(final Entry root) {
		
		final Map<String, LayoutDefinition<TargetContext<?>>> result = new HashMap<>();
		if (root == null || !root.isExist()) {
			return result;
		}
		if (!root.isContainer()) {
			assert false : "layouts folder is not a container!";
		}
		final BaseList<Entry> contents = root.toContainer().getContentCollection(null).baseValue();
		for (final Entry current : contents) {
			final String key = current.getKey();
			if (current.isBinary()) {
				final String name;
				LayoutDefinition<TargetContext<?>> layout;
				try {
					if (key.endsWith(".jso")) {
						name = LayoutEngine.convertClassNameToLayoutName(key.substring(0, key.length() - 4));
						layout = Convert.Any.toAny(new LayoutJavaScriptObject(name, current));
					} else //
					if (key.endsWith(".jslt")) {
						name = LayoutEngine.convertClassNameToLayoutName(key.substring(0, key.length() - 5));
						layout = Convert.Any.toAny(new LayoutJavaScriptLayoutTransform(name, current));
					} else //
					if (key.endsWith(".jsld")) {
						name = LayoutEngine.convertClassNameToLayoutName(key.substring(0, key.length() - 5));
						layout = Convert.Any.toAny(new LayoutJavaScriptLayoutData(name, current));
					} else {
						continue;
					}
					if (result.containsKey(name)) {
						Report.warning("SKIN-LOADER", "Layout '" + name + "' is already defined locally, redefining with: " + key);
					}
					result.put(name, layout);
				} catch (final Throwable t) {
					Report.exception("SKIN-LOADER", "Error while trying to initialize layout: " + current, t);
					continue;
				}
			}
		}
		return result;
	}
	
	private final Entry root;
	
	private final Map<String, LayoutDefinition<TargetContext<?>>> layouts;
	
	/**
	 * TODO skin inheritance
	 */
	private final Skin parent;
	
	/**
	 * 
	 * @param parent
	 * @param folder
	 *            root folder
	 */
	public SkinImpl(final Skin parent, final Entry folder) {
		super(folder);
		this.root = super.getRoot();
		this.layouts = SkinImpl.loadLayouts(SkinImpl.getLayoutsFolder(this.root));
		this.parent = parent;
	}
	
	/**
	 * 
	 * @param parent
	 * @param folder
	 *            root folder
	 */
	public SkinImpl(final Skin parent, final File folder) {
		super(folder);
		this.root = super.getRoot();
		this.layouts = SkinImpl.loadLayouts(SkinImpl.getLayoutsFolder(this.root));
		this.parent = parent;
	}
	
	/**
	 * Name is relative to anchor. Just like path prefix.
	 * 
	 * @param parent
	 * @param name
	 * @param anchor
	 *            anchor point
	 */
	public SkinImpl(final Skin parent, final String name, final Class<?> anchor) {
		super(name, anchor);
		this.root = super.getRoot();
		this.layouts = SkinImpl.loadLayouts(SkinImpl.getLayoutsFolder(this.root));
		this.parent = parent;
	}
	
	/**
	 * 
	 * @param name
	 * @param parent
	 * @param folder
	 *            root folder
	 */
	public SkinImpl(final String name, final Skin parent, final Entry folder) {
		super(name, folder);
		this.root = super.getRoot();
		this.layouts = SkinImpl.loadLayouts(SkinImpl.getLayoutsFolder(this.root));
		this.parent = parent;
	}
	
	@Override
	public LayoutDefinition<TargetContext<?>> getLayoutDefinition(final String name) {
		
		return this.layouts == null
			? null
			: this.layouts.get(name);
	}
	
	@Override
	public Entry getRoot() {
		
		return this.root;
	}
	
	@Override
	public Skin getSkinParent() {
		
		return this.parent;
	}
	
	@Override
	public String toString() {
		
		return "[object ae3.l2.SkinImpl(name=" + this.getName() + ")]";
	}
}
