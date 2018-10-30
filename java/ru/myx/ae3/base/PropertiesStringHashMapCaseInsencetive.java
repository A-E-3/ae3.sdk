package ru.myx.ae3.base;

final class PropertiesStringHashMapCaseInsencetive extends PropertiesStringHashMap {

	/**
	 *
	 */
	private static final long serialVersionUID = 5006732750317050717L;
	
	@Override
	public boolean containsKey(final Object key) {
		
		final Object keyActual = key instanceof String
			? ((String) key).toLowerCase()
			: key;
		return super.containsKey(keyActual);
	}

	@Override
	public BasePropertyData<String> get(final Object key) {
		
		final Object keyActual = key instanceof String
			? ((String) key).toLowerCase()
			: key;
		return super.get(keyActual);
	}

	@Override
	public BasePropertyData<String> put(final String key, final BasePropertyData<String> value) {
		
		final String keyActual = key == null
			? key
			: key.toLowerCase();
		return super.put(keyActual, value);
	}
	
	@Override
	public BasePropertyData<String> remove(final Object key) {
		
		final Object keyActual = key instanceof String
			? ((String) key).toLowerCase()
			: key;
		return super.remove(keyActual);
	}
	
	@Override
	public boolean remove(final Object key, final Object value) {
		
		final Object keyActual = key instanceof String
			? ((String) key).toLowerCase()
			: key;
		return super.remove(keyActual, value);
	}
	
}
