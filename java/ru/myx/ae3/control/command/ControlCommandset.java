/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control.command;

import java.util.List;
import java.util.Map;

/** @author myx */
public interface ControlCommandset extends List<ControlCommand<?>> {

	/** @param key
	 * @return command */
	default ControlCommand<?> getByKey(final String key) {

		if (key == null || key.trim().length() == 0) {
			return null;
		}
		for (int i = this.size() - 1; i >= 0; --i) {
			final ControlCommand<?> command = this.get(i);
			if (key.equals(command.getKey())) {
				return command;
			}
		}
		return null;
	}

	/** @return */
	default Map<String, Object> getData() {

		return null;
	}

}
