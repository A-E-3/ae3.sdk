package ru.myx.ae3.skinner;

import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.i3.RequestHandler;
import ru.myx.ae3.l2.skin.Skin;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.serve.ServeRequest;

/** @author myx */
public interface Skinner extends RequestHandler, Skin {
	
	/**
	 *
	 */
	static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(Skinner.class));

	/**
	 *
	 */
	static final Skinner NUL_SKINNER = new SkinnerDefaultXML();
	
	/** @return settings */
	default BaseObject getSkinSettings() {

		return BaseObject.UNDEFINED;
	}
	
	/** @return */
	default ControlFieldset<?> getSkinSettingsFieldset() {
		
		return null;
	}
	
	/** @return title */
	BaseObject getTitle();
	
	/** @param response
	 * @return answer */
	ReplyAnswer handleReply(final ReplyAnswer response);
	
	/** @param response
	 * @return answer */
	ReplyAnswer handleReplyOnce(final ReplyAnswer response);
	
	/** @return */
	boolean isAbstract();
	
	/** @param query
	 * @return answer */
	@Override
	ReplyAnswer onQuery(final ServeRequest query);
	
	/** Returns false by default. Will enforce HIGH authorization for secure interface if overridden
	 * and returns true.
	 *
	 * @return */
	default boolean requireAuth() {
		
		return false;
	}
	
	/** Returns false by default. Will enforce check for secure interface if overridden and returns
	 * true.
	 *
	 * @return */
	default boolean requireSecure() {

		return false;
	}
	
	/** return false to replace skinner with new one. return true to keep using this skinner.
	 *
	 * @return */
	default boolean scan() {
		
		// do nothing
		return true;
	}
}
