/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control.command;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseJoined;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.reflect.Reflect;

/**
 * @author myx
 * @param <T>
 *
 */
public interface ControlCommand<T extends ControlCommand<?>> extends BaseFunction, ControlBasic<T> {

	/**
	 *
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(//
			new BaseJoined(
					BaseFunction.PROTOTYPE, //
					Reflect.classToBasePrototype(ControlCommand.class)//
			)//
	);

	@Override
	default BaseObject basePrototype() {

		return ControlCommand.PROTOTYPE;
	}

	/**
	 * May return null when no description is available.
	 *
	 * @return string
	 */
	default String commandDescription() {

		return Base.getString(this.getAttributes(), "description", "");
	}

	/**
	 * Returns <b>true </b> by default.
	 *
	 * @return
	 */
	default boolean commandEnabled() {

		return true;
	}
	/**
	 * @return string
	 */
	default String commandPermission() {

		return Base.getString(this.getAttributes(), "permission", "");
	}

	/**
	 * MUST NOT return null, return commandName() at least.
	 *
	 * @return string
	 */
	@Override
	public String getTitle();

	/**
	 * @param icon
	 * @return command
	 */
	@SuppressWarnings("unchecked")
	default T setCommandIcon(final String icon) {
		
		this.setAttribute("icon", icon);
		return (T) this;
	}

	/**
	 * @param permission
	 * @return command
	 */
	@SuppressWarnings("unchecked")
	default T setCommandPermission(final String permission) {

		this.setAttribute("permission", permission);
		return (T) this;
	}

	/**
	 * @param global
	 * @return command
	 */
	@SuppressWarnings("unchecked")
	default T setGlobal(final boolean global) {

		this.setAttribute("global", global);
		return (T) this;
	}
}
