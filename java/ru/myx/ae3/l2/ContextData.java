package ru.myx.ae3.l2;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.l2.skin.Skin;

/**
 * @author myx
 * @param <T>
 *            target
 * @param <L>
 *            layout
 * 
 */
class ContextData<T, L> {
	BaseArray			currentArray;
	
	ContextHandler<T, L>	currentHandler;
	
	int						currentIndex;
	
	BaseObject			currentObject;
	
	Skin					currentSkin;
	
	ContextState			currentState;
	
	ContextData<T, L>		stack;
}
