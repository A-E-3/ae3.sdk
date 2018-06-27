package ru.myx.ae3.l2;

import java.util.StringTokenizer;

import ru.myx.ae3.answer.AbstractReplyException;
import ru.myx.ae3.answer.Reply;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseHostObject;
import ru.myx.ae3.base.BaseMapEditable;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.common.HolderSimple;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.i3.TargetInterface;
import ru.myx.ae3.l2.skin.Skin;
import ru.myx.ae3.reflect.ReflectionIgnore;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.skinner.Skinner;

/**
 *
 * @author myx
 * @param <T>
 *            target
 *
 */
@ReflectionIgnore
public abstract class TargetContextAbstract<T extends TargetContextAbstract<?>> extends BaseHostObject implements TargetContext<BaseObject>, ContextHandler<T, BaseObject> {

	/**
	 * @param cssClassOld
	 * @param cssClass
	 * @return
	 */
	private static String injectCssClass(final String cssClassOld, final String cssClass) {

		if (cssClassOld.length() == 0) {
			return cssClass;
		}
		for (final StringTokenizer st = new StringTokenizer(cssClassOld, " "); st.hasMoreTokens();) {
			final String cssClassCompare = st.nextToken();
			if (cssClassCompare.equals(cssClass)) {
				return cssClassOld;
			}
		}
		return cssClassOld + " " + cssClass;
	}

	static {
		LayoutEngine.getDocumentation();
	}

	ExecProcess context;

	private int countSteps = 0;

	BaseArray currentArray;

	ContextHandler<T, BaseObject> currentHandler;

	int currentIndex;

	BaseObject currentObject;

	Skin currentSkin;

	Skinner currentSkinner;

	ContextState currentState;

	private final long dateStarted;

	final TargetInterface iface;

	ContextData<T, BaseObject> stack;

	/**
	 * current zoom
	 */
	ZoomType zoom;

	/**
	 * @param iface
	 *
	 */
	protected TargetContextAbstract(final TargetInterface iface) {

		this.iface = iface;
		this.dateStarted = System.currentTimeMillis();
		this.context = Exec.createProcess(Exec.currentProcess(), "L2TGT: " + this.getClass().getName());
	}

	/**
	 *
	 */
	protected void doFinish() {

		// empty
	}

	/**
	 * @param skin
	 */
	public void doSetSkin(final Skin skin) {

		this.currentSkin = skin;
	}

	/**
	 * @param skinner
	 */
	public void doSetSkinner(final Skinner skinner) {

		this.currentSkinner = skinner;
	}

	/**
	 *
	 */
	protected void doStart() {

		//
	}

	@Override
	public boolean dump(final String s) {

		if (!Report.MODE_DEBUG && !Report.MODE_ASSERT) {
			return true;
		}
		int level = 0;
		for (ContextData<T, BaseObject> pointer = this.stack; pointer != null; level++, pointer = pointer.stack) {
			// empty
		}
		System.out.println(System.identityHashCode(this) + " > " + level + " > " + this.currentState + " > " + this.currentIndex + " > " + s);
		return true;
	}

	/**
	 * you should return from loop after call this method
	 *
	 * @param handler
	 *
	 * @param layout
	 * @return
	 */
	protected BaseObject enterContent(final ContextHandler<T, BaseObject> handler, final BaseObject layout) {

		this.dump("enter content, handler=" + handler + ", layout=" + Ecma.toEcmaSourceCompact(layout));
		this.currentObject = handler.onEnter(this.toTarget(), this.currentObject);
		assert this.currentObject == null : "nulls only yet";
		final ContextData<T, BaseObject> stack = new ContextData<>();
		stack.currentArray = this.currentArray;
		stack.currentHandler = this.currentHandler;
		stack.currentIndex = this.currentIndex;
		stack.currentObject = this.currentObject;
		stack.currentSkin = this.currentSkin;
		stack.currentState = this.currentState;
		stack.stack = this.stack;
		this.stack = stack;
		this.currentArray = null;
		this.currentHandler = handler;
		this.currentIndex = -1;
		this.currentObject = layout;
		this.currentState = ContextState.CONTENT;
		return null;
	}

	/**
	 * you should return from loop after call this method
	 *
	 * @param handler
	 *
	 * @param array
	 * @return
	 */
	protected BaseObject enterSequence(final ContextHandler<T, BaseObject> handler, final BaseArray array) {

		this.dump("enter sequence, handler=" + handler + ", size=" + array.length());
		this.currentObject = handler.onEnter(this.toTarget(), this.currentObject);
		assert this.currentObject == null : "nulls only yet";
		final ContextData<T, BaseObject> stack = new ContextData<>();
		stack.currentArray = this.currentArray;
		stack.currentHandler = this.currentHandler;
		stack.currentIndex = this.currentIndex;
		stack.currentObject = this.currentObject;
		stack.currentSkin = this.currentSkin;
		stack.currentState = this.currentState;
		stack.stack = this.stack;
		this.stack = stack;
		this.currentArray = array;
		this.currentHandler = handler;
		this.currentIndex = -1;
		this.currentObject = null;
		this.currentState = ContextState.SEQUENCE;
		return null;
	}

	@Override
	public ExecProcess getContext() {

		return this.context;
	}

	/**
	 * @return current context handler
	 */
	protected ContextHandler<T, BaseObject> getContextHandler() {

		return this.currentHandler;
	}

	/**
	 * replace
	 *
	 * @return
	 */
	protected TargetContextAbstract<?> getContextLayoutSource() {

		return this;
	}

	/**
	 * @return number of steps taken to render this context up to date
	 */
	public int getCountSteps() {

		return this.countSteps;
	}

	/**
	 * @return date when context was created
	 */
	public long getDateStarted() {

		return this.dateStarted;
	}

	@Override
	public TargetInterface getInterface() {

		return this.iface;
	}

	/**
	 * @return
	 */
	public BaseObject getLayoutAbout() {

		try {
			return LayoutEngine.parseJSLD(this.getClass().getResourceAsStream("about.jsld"));
		} catch (final Throwable e) {
			return BaseObject.createObject()//
					.putAppend("code", 500)//
					.putAppend("layout", "message")//
					.putAppend("value", Format.Throwable.toText(e));
		}
	}

	/**
	 * @param name
	 * @return null or interface/context layout for given name
	 */
	protected abstract LayoutDefinition<T> getLayoutForContext(final String name);

	/**
	 * @return
	 */
	public BaseObject getResultLayout() {

		return this.currentObject;
	}

	@Override
	public Skin getSkin() {

		return this.currentSkin;
	}

	@Override
	public Skinner getSkinner() {

		return this.currentSkinner;
	}

	@Override
	public BaseObject onEnter(final T target, final BaseObject layout) {

		return null;
	}

	@Override
	public void onLeave(final T target) {

		if (Report.MODE_DEVEL && Report.MODE_ASSERT) {
			this.dump("default onLeave, ignored");
		}
	}

	@Override
	public BaseObject onNest(final T target, final BaseObject layout) {

		// ignore before-nest by default
		return layout;
	}

	private final boolean step() {

		if (Report.MODE_DEBUG) {
			this.dump("step: " + (this.currentObject == null
				? null
				: Ecma.toEcmaSourceCompact(this.currentObject)));
		}

		/**
		 * check undefined
		 */
		if (this.currentObject == null || this.currentObject == BaseObject.UNDEFINED) {
			switch (this.currentState) {
				case SEQUENCE :
					this.currentIndex++;
					final int length = this.currentArray.length();
					if (this.currentIndex < length) {
						this.currentObject = this.currentArray.baseGet(this.currentIndex, BaseObject.UNDEFINED);
						return true;
					}
					//$FALL-THROUGH$
				case CONTENT :
					this.currentHandler.onLeave(this.toTarget());
					//$FALL-THROUGH$
				case ROOT :
					final ContextData<T, BaseObject> data = this.stack;
					if (data == null) {
						return false;
					}
					this.stack = data.stack;
					this.currentArray = data.currentArray;
					this.currentHandler = data.currentHandler;
					this.currentIndex = data.currentIndex;
					this.currentObject = data.currentObject;
					this.currentSkin = data.currentSkin;
					this.currentState = data.currentState;
					return true;
			}
		}
		/**
		 * Convert primitive object to layouts
		 */
		if (this.currentObject.baseIsPrimitiveBoolean()) {
			final BaseObject replacement = BaseObject.createObject();
			replacement.baseDefine("layout", "boolean");
			replacement.baseDefine("value", this.currentObject);
			this.currentObject = replacement;
		} else //
		if (this.currentObject.baseIsPrimitiveNumber()) {
			final BaseObject replacement = BaseObject.createObject();
			replacement.baseDefine("layout", "number");
			replacement.baseDefine("value", this.currentObject);
			this.currentObject = replacement;
		} else //
		if (this.currentObject.baseIsPrimitiveString()) {
			final BaseObject replacement = BaseObject.createObject();
			replacement.baseDefine("layout", "string");
			replacement.baseDefine("value", this.currentObject);
			this.currentObject = replacement;
		} else {
			final BaseArray array = this.currentObject.baseArray();
			if (array != null) {
				final BaseObject prototype = this.currentObject;
				final BaseObject replacement = BaseObject.createObject(prototype);
				replacement.baseDefine("layout", "sequence");
				replacement.baseDefine("elements", this.currentObject);
				this.currentObject = replacement;
			}
		}
		final String name = Base.getString(this.currentObject, "layout", "").trim();
		if (name.length() != 0) {
			final BaseObject previous = this.currentObject;
			/**
			 * Check nest handler
			 */
			{
				final BaseObject replacement = this.currentHandler.onNest(this.toTarget(), this.currentObject);
				if (this.currentObject != previous) {
					assert replacement == null : "Must be null";
					return true;
				}
				if (replacement != previous) {
					this.currentObject = replacement;
					return true;
				}
			}
			/**
			 * Check skin?
			 */
			{
				for (Skin skin = this.currentSkin; skin != null;) {
					{
						final LayoutDefinition<TargetContext<?>> definition = skin.getLayoutDefinition(name);
						if (definition != null) {
							if (Report.MODE_DEBUG) {
								System.out.println(">>>>>> layout: " + Format.Describe.toEcmaSource(this.currentObject, "") + ", def: " + definition);
							}
							this.currentObject = definition.onExecute(this, this.currentObject);
							if (this.currentObject != previous) {
								return true;
							}
						}
					}
					{
						/**
						 * TODO: this is actually not protecting from anything
						 * more than trivial
						 */
						final Skin next = skin.getSkinParent();
						assert skin != next : "Oops - same skin: " + next;
						skin = next;
					}
				}
			}
			/**
			 * Check context
			 */
			{
				@SuppressWarnings("unchecked")
				final TargetContextAbstract<T> contextReplacement = (TargetContextAbstract<T>) this.getContextLayoutSource();
				assert contextReplacement != null : "Return 'this' at least!";
				final LayoutDefinition<T> definition = contextReplacement.getLayoutForContext(name);
				if (definition != null) {
					final BaseObject replacement = definition.onExecute(contextReplacement.toTarget(), this.currentObject);
					if (this.currentObject != previous) {
						assert replacement == null : "Must be null";
						return true;
					}
					if (replacement != previous) {
						this.currentObject = replacement;
						return true;
					}
				}
			}
			/**
			 * Check defaults
			 */
			{
				final LayoutDefinition<TargetContext<?>> definition = Skin.SKIN_FAILOVER.getLayoutDefinition(name);
				if (definition != null) {
					this.currentObject = definition.onExecute(this, this.currentObject);
					if (this.currentObject != previous) {
						return true;
					}
				}
			}
		} else {
			if (this.currentObject instanceof ReplyAnswer) {
				return false;
			}
		}

		if ("string".equals(name)) {
			/**
			 * layout string is not found, however it is required for this loop
			 * to work
			 */
			this.currentObject = this.currentObject.baseGet("value", BaseString.EMPTY);
			return false;
		}

		/**
		 * Check super-defaults
		 */
		{
			{
				final BaseObject content = this.currentObject.baseGet("content", BaseObject.UNDEFINED);
				if (content != BaseObject.UNDEFINED) {
					final String cssClassOld = Base.getString(this.currentObject, "cssClass", "").trim();
					final String cssClassNew = TargetContextAbstract.injectCssClass(cssClassOld, "ui-" + name);

					if (content.baseIsPrimitive()) {
						/**
						 * Convert primitive object to layouts
						 */
						if (content.baseIsPrimitiveNumber()) {
							final BaseObject prototype = this.currentObject;
							final BaseObject replacement = BaseObject.createObject(prototype);
							replacement.baseDefine("layout", "number");
							replacement.baseDefine("value", content);
							replacement.baseDefine("cssClass", cssClassNew);
							this.currentObject = replacement;
							return true;
						}
						if (content.baseIsPrimitiveString()) {
							final BaseObject prototype = this.currentObject;
							final BaseObject replacement = BaseObject.createObject(prototype);
							replacement.baseDefine("layout", "string");
							replacement.baseDefine("value", content);
							replacement.baseDefine("cssClass", cssClassNew);
							this.currentObject = replacement;
							return true;
						}
						if (content.baseIsPrimitiveBoolean()) {
							final BaseObject prototype = this.currentObject;
							final BaseObject replacement = BaseObject.createObject(prototype);
							replacement.baseDefine("layout", "boolean");
							replacement.baseDefine("value", content);
							replacement.baseDefine("cssClass", cssClassNew);
							this.currentObject = replacement;
							return true;
						}

						assert false //
						: "content: " + content + ", currentObject: " + Format.Describe.toEcmaSource(this.currentObject, "");

						this.currentObject = content;
						return true;
					}
					{
						final BaseArray array = content.baseArray();
						if (array != null) {
							// final BaseObject replacement = new
							// BaseNativeObject(
							// this.currentObject );
							final BaseObject prototype = this.currentObject;
							final BaseObject replacement = BaseObject.createObject(prototype);
							replacement.baseDefine("layout", "sequence");
							replacement.baseDefine("elements", content);
							replacement.baseDefine("content", BaseObject.UNDEFINED);
							replacement.baseDefine("cssClass", cssClassNew);
							this.currentObject = replacement;
							return true;
						}
					}
					assert !"container".equals(name) : "container layout is mandatory to have (as geometry layout)";
					final BaseObject prototype = this.currentObject;
					final BaseObject replacement = BaseObject.createObject(prototype);
					replacement.baseDefine("layout", "container");
					// replacement.baseDefine( "content", content );
					replacement.baseDefine("cssClass", cssClassNew);
					this.currentObject = replacement;
					return true;
				}
			}
			{
				final BaseArray elements = this.currentObject.baseGet("elements", BaseObject.UNDEFINED).baseArray();
				if (elements != null && !"sequence".equals(name)) {
					final String cssClassOld = Base.getString(this.currentObject, "cssClass", "").trim();
					final String cssClassNew = TargetContextAbstract.injectCssClass(cssClassOld, "ui-" + name);
					final BaseObject prototype = this.currentObject;

					final BaseObject replacement = BaseObject.createObject(prototype);
					replacement.baseDefine("layout", "sequence");
					// Basedefine( replacement, "elements", elements );
					replacement.baseDefine("cssClass", cssClassNew);
					this.currentObject = replacement;
					return true;
				}
			}
		}
		if (!"message".equals(name)) {
			final BaseObject prototype = this.currentObject;
			final BaseObject replacement = BaseObject.createObject(prototype);
			replacement.baseDefine("layout", "message");
			replacement.baseDefine("code", Reply.CD_UNIMPLEMENTED);
			replacement.baseDefine("type", "error");
			replacement.baseDefine("content", "Layout '" + name + "' is not known");
			if (Report.MODE_ASSERT || Report.MODE_DEBUG) {
				replacement.baseDefine("detail", this.toString());
			}
			this.currentObject = replacement;
			return true;
		}

		this.currentObject = Base.forString(Report.MODE_ASSERT || Report.MODE_DEBUG
			? "[Layout '" + name + "' is not known, " + this.toString() + "]"
			: "[Layout '" + name + "' is not known!]");
		return true;
	}

	@Override
	public String toString() {

		final BaseObject layout = this.currentObject;
		return "[object " //
				+ this.getClass().getSimpleName() + "(iface=" + this.iface + (layout == null
					? ""
					: " layout=" + Format.Describe.toEcmaSource(layout, "\t"))
				+ ")]";
	}

	/**
	 * @return this object casted to target type
	 */
	@SuppressWarnings("unchecked")
	protected T toTarget() {

		return (T) this;
	}

	@Override
	public final BaseObject transformLayout(final BaseObject layout) {

		try {
			this.transform(layout).baseValue();
			return this.getResultLayout();
		} catch (final AbstractReplyException e) {
			return Base.forUnknown(e.getReply().getObject());
		}
	}

	/**
	 * @param layout
	 * @return
	 */
	@Override
	public final Value<Void> transform(final BaseObject layout) {

		if (layout == null || layout.baseIsPrimitive()) {
			/**
			 * TODO: move to some general place, idea is to wrap single text
			 * string layout into a document.
			 */
			final BaseMapEditable currentObject = BaseObject.createObject();
			this.currentObject = currentObject;
			currentObject.baseDefine("layout", "document");
			if (layout == null) {
				currentObject.baseDefine("title", "no content");
				currentObject.baseDefine("content", "no content: java null");
			} else //
			if (layout == BaseObject.UNDEFINED) {
				currentObject.baseDefine("title", "no content");
				currentObject.baseDefine("content", "no content");
			} else {
				currentObject.baseDefine("content", layout);
			}
			/**
			 *
			 */
		} else {
			this.currentObject = layout;
		}
		this.currentHandler = this;
		this.currentState = ContextState.ROOT;
		this.doStart();
		int count = 0;
		for (; this.step();) {
			count++;
			this.countSteps++;
		}
		this.dump("transform finished, " + count + (this.countSteps == count
			? " step(s)"
			: " step(s), " + this.countSteps + " step(s) total") + ", " + Format.Compact.toPeriod(System.currentTimeMillis() - this.dateStarted));
		this.doFinish();
		return new HolderSimple<>(null);
	}
}
