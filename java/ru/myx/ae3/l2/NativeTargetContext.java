package ru.myx.ae3.l2;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseNativeArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.i3.TargetInterface;
import ru.myx.ae3.l2.geo.GeometryLayout;
import ru.myx.ae3.report.Report;

/** @author myx */
public class NativeTargetContext extends TargetContextAbstract<NativeTargetContext> {
	
	static final class JsonHandlerSequence implements NativeHandler {
		
		static final NativeHandler INSTANCE = new JsonHandlerSequence();

		private JsonHandlerSequence() {
			
			//
		}

		@Override
		public void insert(final NativeTargetContext target, final BaseObject value) {
			
			assert value != null : "NULL java value";
			assert value != BaseObject.UNDEFINED : "Undefined values should be skipped!";
			final NativeArray array = (NativeArray) target.current.baseGet("elements", BaseObject.UNDEFINED);
			array.add(value);
		}

		@Override
		public BaseObject onEnter(final NativeTargetContext target, final BaseObject layout) {
			
			return target.onEnter(target, layout);
		}

		@Override
		public void onLeave(final NativeTargetContext target) {
			
			target.onLeave(target);
		}

		@Override
		public BaseObject onNest(final NativeTargetContext target, final BaseObject layout) {
			
			return target.onNest(target, layout);
		}

		@Override
		public String toString() {
			
			return "SEQUENCE";
		}
	}
	
	/** @author myx */
	public static interface LayoutsProvider {
		
		/** @return */
		public default Map<String, LayoutDefinition<?>> getLayouts() {
			
			return null;
		}
	}
	
	static class NativeArray extends BaseNativeArray {
		//
	}
	
	static interface NativeHandler extends ContextHandler<NativeTargetContext, BaseObject> {
		
		void insert(final NativeTargetContext target, final BaseObject value);
	}

	static final class NativeHandlerContent implements NativeHandler {
		
		static final NativeHandler INSTANCE = new NativeHandlerContent();

		private NativeHandlerContent() {
			
			//
		}

		@Override
		public void insert(final NativeTargetContext target, final BaseObject value) {
			
			target.current.baseDefine("content", value, BaseProperty.ATTRS_MASK_WED);
		}

		@Override
		public BaseObject onEnter(final NativeTargetContext target, final BaseObject layout) {
			
			return target.onEnter(target, layout);
		}

		@Override
		public void onLeave(final NativeTargetContext target) {
			
			target.onLeave(target);
		}

		@Override
		public BaseObject onNest(final NativeTargetContext target, final BaseObject layout) {
			
			return target.onNest(target, layout);
		}

		@Override
		public String toString() {
			
			return "CONTENT";
		}
	}
	
	static final class NativeLayoutDefinitionImpl implements LayoutDefinition<NativeTargetContext> {

		static final LayoutDefinition<NativeTargetContext> INSTANCE = new NativeLayoutDefinitionImpl();
		
		private NativeLayoutDefinitionImpl() {

			//
		}
		
		@Override
		public BaseObject onExecute(final NativeTargetContext context, final BaseObject layout) {

			final String name = Base.getString(layout, "layout", "");
			assert context.dump(this.getClass().getName() + ", onExecute, layout=" + name);
			final GeometryLayout geo = GeometryLayout.parse(name);
			if (geo == GeometryLayout.STRING) {
				final BaseObject value = layout.baseGet("value", BaseObject.UNDEFINED);
				if (value.baseIsPrimitiveString()) {
					context.insert(value);
					return null;
				}
			}
			if ("image".equals(name)) {
				final BaseObject href = layout.baseGet("href", BaseObject.UNDEFINED);
				if (href != BaseObject.UNDEFINED) {
					return layout;
				}
			}
			{
				final BaseArray array = layout.baseGet("elements", BaseObject.UNDEFINED).baseArray();
				if (array != null) {
					final BaseObject replacement = BaseObject.createObject(layout);
					if (context.current != null) {
						replacement.baseDefine("..return", context.current, BaseProperty.ATTRS_MASK_NND);
					}
					replacement.baseDefine("elements", new NativeArray(), BaseProperty.ATTRS_MASK_NND);
					context.insert(replacement);
					context.current = replacement;
					return context.enterSequence(JsonHandlerSequence.INSTANCE, array);
				}
			}
			{
				final BaseObject content = layout.baseGet("content", BaseObject.UNDEFINED);
				if (content != BaseObject.UNDEFINED) {
					final BaseObject replacement = BaseObject.createObject(layout);
					if (context.current != null) {
						replacement.baseDefine("..return", context.current, BaseProperty.ATTRS_MASK_NND);
					}
					replacement.baseDefine("content", BaseObject.NULL, BaseProperty.ATTRS_MASK_WED);
					context.insert(replacement);
					context.current = replacement;
					return context.enterContent(NativeHandlerContent.INSTANCE, content);
				}
			}
			context.insert(layout);
			return null;
		}
		
	}

	/** @author myx */
	public static enum TargetMode implements LayoutsProvider {
		/**
		 *
		 */
		CLONE,
		/**
		 *
		 */
		EXTERNALIZED {

			private final Map<String, LayoutDefinition<?>> LAYOUTS;
			{
				this.LAYOUTS = new TreeMap<>();
				this.LAYOUTS.put("string", null);
				this.LAYOUTS.put("sequence", null);
			}
			
			@Override
			public Map<String, LayoutDefinition<?>> getLayouts() {

				return this.LAYOUTS;
			}
		},
		/**
		 *
		 */
		SERVER {

			private final Map<String, LayoutDefinition<?>> LAYOUTS;
			{
				this.LAYOUTS = new TreeMap<>();
				// this.LAYOUTS.put( "paragraph", JsonLayoutDefinitionImpl.INSTANCE
				// );
				this.LAYOUTS.put("document", NativeLayoutDefinitionImpl.INSTANCE);
				this.LAYOUTS.put("image", NativeLayoutDefinitionImpl.INSTANCE);
				this.LAYOUTS.put("link", NativeLayoutDefinitionImpl.INSTANCE);
				this.LAYOUTS.put("numbered", NativeLayoutDefinitionImpl.INSTANCE);
				this.LAYOUTS.put("padding", NativeLayoutDefinitionImpl.INSTANCE);
				this.LAYOUTS.put("window", NativeLayoutDefinitionImpl.INSTANCE);
			}
			
			@Override
			public Map<String, LayoutDefinition<?>> getLayouts() {

				return this.LAYOUTS;
			}
		},
		/**
		 *
		 */
		DUMB {

			private final Map<String, LayoutDefinition<?>> LAYOUTS;
			{
				this.LAYOUTS = new TreeMap<>();
			}
			
			@Override
			public Map<String, LayoutDefinition<?>> getLayouts() {

				return this.LAYOUTS;
			}
		};

		/** @return null is handles all layouts on onEntry */
		@Override
		public Map<String, LayoutDefinition<?>> getLayouts() {

			return null;
		}
	}
	
	/**
	 *
	 */
	protected final LayoutsProvider targetMode;
	
	/**
	 *
	 */
	protected BaseObject result;
	
	/**
	 *
	 */
	protected BaseObject current;
	
	/**
	 *
	 */
	protected TargetContext<?> replacement;
	
	/** @param replacement
	 * @param targetMode
	 */
	public NativeTargetContext(final TargetContext<?> replacement, final TargetMode targetMode) {
		
		super(
				replacement == null
					? null
					: replacement.getInterface());
		this.replacement = replacement;
		this.targetMode = targetMode == null
			? TargetMode.SERVER
			: targetMode;
	}
	
	/** @param iface
	 * @param targetMode
	 */
	public NativeTargetContext(final TargetInterface iface, final TargetMode targetMode) {
		
		super(iface);
		this.replacement = null;
		this.targetMode = targetMode == null
			? TargetMode.SERVER
			: targetMode;
	}
	
	@Override
	protected void doFinish() {

		// have nothing to do here
	}
	
	@Override
	protected void doStart() {

		this.result = null;
		this.current = null;
	}
	
	@Override
	public boolean dump(final String s) {

		if ((Report.MODE_DEBUG || Report.MODE_ASSERT) && this.current != null) {
			final NativeArray array;
			if (this.current.baseGetOwnProperty("comment") != null) {
				array = (NativeArray) this.current.baseGet("comment", BaseObject.UNDEFINED);
			} else {
				array = new NativeArray();
				final BaseObject comment = this.current.baseGet("comment", BaseObject.UNDEFINED);
				if (comment != BaseObject.UNDEFINED) {
					array.add(comment);
				}
				this.current.baseDefine("comment", array, BaseProperty.ATTRS_MASK_WED);
			}
			array.add(
					Base.forString(
							"DEBUG: " + (s.length() > 100
								? s.substring(0, 80) + "..."
								: s)));
		}
		return super.dump(s);
	}
	
	/** Adjust visibility! */
	@Override
	protected BaseObject enterContent(final ContextHandler<NativeTargetContext, BaseObject> handler, final BaseObject layout) {

		return super.enterContent(handler, layout);
	}
	
	/** Adjust visibility! */
	@Override
	protected BaseObject enterSequence(final ContextHandler<NativeTargetContext, BaseObject> handler, final BaseArray array) {

		return super.enterSequence(handler, array);
	}
	
	@Override
	protected LayoutDefinition<NativeTargetContext> getLayoutForContext(final String name) {

		final Map<String, LayoutDefinition<?>> layouts = this.targetMode.getLayouts();
		assert layouts != null : "CLONE mode is the only mode with NULL layouts and you shouldn't get there in CLONE mode!";
		return layouts.containsKey(name) || GeometryLayout.parse(name) != null
			? NativeLayoutDefinitionImpl.INSTANCE
			: null;
	}
	
	/** @return */
	@Override
	public final BaseObject getResultLayout() {

		return this.result;
	}
	
	void insert(final BaseObject object) {

		final ContextHandler<NativeTargetContext, BaseObject> handler = this.getContextHandler();
		if (handler instanceof NativeHandler) {
			((NativeHandler) handler).insert(this, object);
			return;
		}
		assert handler == this;
		assert this.result == null;
		this.result = object;
	}
	
	@Override
	public void onLeave(final NativeTargetContext target) {

		final BaseObject replacement = this.current.baseGet("..return", BaseObject.UNDEFINED);
		if (replacement == BaseObject.UNDEFINED) {
			assert this.result != null;
			this.current = this.result;
		} else {
			this.current.baseDelete("..return");
			this.current = replacement;
		}
		super.onLeave(target);
	}
	
	@Override
	public BaseObject onNest(final NativeTargetContext target, final BaseObject layout) {

		if (this.targetMode == TargetMode.CLONE) {
			this.result = layout;
			return null;
		}
		return super.onNest(target, layout);
	}
	
	@Override
	public URI registerBinary(final String relativeName, final TransferCopier binary) {

		if (this.replacement != null) {
			return this.replacement.registerBinary(relativeName, binary);
		}
		return super.registerBinary(relativeName, binary);
	}
}
