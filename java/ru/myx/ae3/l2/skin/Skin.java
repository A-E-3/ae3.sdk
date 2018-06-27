package ru.myx.ae3.l2.skin;

import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.pack.Pack;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.Storage;

/**
 * @author myx
 * 		
 */
public interface Skin extends Pack {
	
	/**
	 * failover skin
	 */
	static final Skin SKIN_FAILOVER = new SkinImpl(
			null, //
			Storage.PUBLIC.relative("resources/skin/skin-failover", null));
			
	/**
	 * standard skin
	 */
	static final Skin SKIN_STANDARD = new SkinImpl(
			null, //
			Storage.PUBLIC.relative("resources/skin/skin-standard", null));
			
	/**
	 * @param name
	 * @return
	 */
	LayoutDefinition<TargetContext<?>> getLayoutDefinition(final String name);
	
	/**
	 * @return
	 */
	@Override
			String getName();
			
	/**
	 * @return
	 */
	@Override
			Entry getRoot();
			
	/**
	 * 
	 * @return NULL or instance of ancestor skin instance
	 */
	Skin getSkinParent();
}
