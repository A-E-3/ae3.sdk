package ru.myx.ae3.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostLookup;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;

/**
 * Title: System DocumentLevel for every level of 6 level model Description:
 * Copyright: Copyright (c) 2001 Company: -= MyX =-
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
public final class ControlLookupStatic extends BaseHostLookup {
	
	
	private final List<String> options = new ArrayList<>();
	
	private final BaseMap optionsByKey = new BaseNativeObject();
	
	/**
	 * 
	 */
	public ControlLookupStatic() {
		
		// empty
	}
	
	/**
	 * @param list
	 * @param entryDelimeter
	 * @param fieldDelimeter
	 */
	public ControlLookupStatic(final String list, final String entryDelimeter, final String fieldDelimeter) {
		
		for (final StringTokenizer st = new StringTokenizer(list, entryDelimeter); st.hasMoreTokens();) {
			final String Current = st.nextToken().trim();
			if (Current.length() == 0) {
				continue;
			}
			final String key;
			final String title;
			final int Pos = Current.indexOf(fieldDelimeter);
			if (Pos == -1) {
				key = title = Current;
			} else {
				key = Current.substring(0, Pos);
				title = Current.substring(Pos + 1);
			}
			this.baseDefine(key, title);
		}
	}
	
	@Override
	public BaseObject baseGetLookupValue(final BaseObject key) {
		
		
		return this.optionsByKey.baseGet(key.baseToString(), BaseObject.UNDEFINED);
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return !this.options.isEmpty();
	}
	
	@Override
	public final boolean baseIsExtensible() {
		
		
		return true;
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return this.options.isEmpty()
			? BaseObject.ITERATOR_EMPTY
			: this.options.iterator();
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		
		return this.options.isEmpty()
			? BaseObject.ITERATOR_EMPTY
			: this.options.iterator();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return this.options.isEmpty()
			? BaseObject.ITERATOR_EMPTY_PRIMITIVE
			: Base.iteratorPrimitiveSafe(this.options.iterator());
	}
	
	@Override
	public String toString() {
		
		
		return "[LookupStatic]";
	}
	
	/**
	 * @param key
	 * @param title
	 * @return same lookup
	 */
	public final ControlLookupStatic putAppend(final String key, final BaseObject title) {
		
		
		this.options.add(key);
		this.optionsByKey.baseDefine(key, title);
		return this;
	}
	
	/**
	 * @param key
	 * @param title
	 * @return same lookup
	 */
	public final ControlLookupStatic putAppend(final String key, final String title) {
		
		
		this.options.add(key);
		this.optionsByKey.baseDefine(key, title);
		return this;
	}
	
}
