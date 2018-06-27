/**
 *
 */
package ru.myx.ae3.skinner;

import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.l2.skin.Skin;
import ru.myx.ae3.serve.ServeRequest;
import ru.myx.ae3.vfs.Entry;

final class SkinnerDefaultXML implements Skinner {

	// MultivariantString.getString(
	// "Default: Simple XML",
	// Collections.singletonMap( "ru", "Системный: Простой XML" )
	// );
	private static final BaseObject TITLE = Base.forString("Default: Simple XML");

	@Override
	public LayoutDefinition<TargetContext<?>> getLayoutDefinition(final String name) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		
		return "default-xml";
	}

	@Override
	public Entry getRoot() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Skin getSkinParent() {
		
		return null;
	}
	
	@Override
	public BaseObject getTitle() {
		
		return SkinnerDefaultXML.TITLE;
	}

	@Override
	public ReplyAnswer handleReply(final ReplyAnswer response) {
		
		return response;
	}

	@Override
	public ReplyAnswer handleReplyOnce(final ReplyAnswer response) {
		
		return response;
	}

	@Override
	public boolean isAbstract() {
		
		return false;
	}

	@Override
	public ReplyAnswer onQuery(final ServeRequest query) {
		
		return null;
	}
}
