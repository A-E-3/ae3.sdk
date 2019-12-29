package ru.myx.ae3.exec.fn;

/** @author myx */
public enum FunctionExecutionType {
	/**
	 *
	 */
	ALWAYS {
		//
	},
	/**
	 *
	 */
	AUTO {
		//
	},
	/**
	 *
	 */
	BUFFERED {
		//
	},
	/**
	 *
	 */
	CACHE {
		//
	},
	/**
	 *
	 */
	DEFERRED {
		//
	},
	/**
	 *
	 */
	ONCE {
		//
	};

	/** @param string
	 * @param defaultValue
	 * @return execution type */
	public static final FunctionExecutionType getExecutionType(final String string, final FunctionExecutionType defaultValue) {

		if (string == null) {
			return defaultValue;
		}
		final int length = string.length();
		if (length == 0) {
			return defaultValue;
		}
		switch (string.charAt(0)) {
			case 'a' : {
				if (length == 6 && "always".equals(string)) {
					return ALWAYS;
				}
				if (length == 4 && "auto".equals(string)) {
					return AUTO;
				}
				break;
			}
			case 'b' : {
				if (length == 8 && "buffered".equals(string)) {
					return BUFFERED;
				}
				break;
			}
			case 'c' : {
				if (length == 5 && "cache".equals(string)) {
					return CACHE;
				}
				break;
			}
			case 'd' : {
				if (length == 8 && ("deferred".equals(string) || "deffered".equals(string))) {
					return DEFERRED;
				}
				break;
			}
			case 'o' : {
				if (length == 4 && "once".equals(string)) {
					return ONCE;
				}
				break;
			}
			default :
		}
		return defaultValue;
	}
}
