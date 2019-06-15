package ru.myx.ae3.vfs.roots;

import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.EntryBinary;
import ru.myx.ae3.vfs.EntryCharacter;
import ru.myx.ae3.vfs.EntryContainer;
import ru.myx.ae3.vfs.EntryMount;
import ru.myx.ae3.vfs.EntryPrimitive;
import ru.myx.ae3.vfs.TreeLinkType;
import ru.myx.ae3.vfs.TreeReadType;

class EntryRoots implements EntryBinary, EntryContainer, EntryCharacter, EntryPrimitive {
	
	@Override
	public boolean canWrite() {
		
		return false;
	}

	@Override
	public Entry clone(final Entry parent, final String key, final TreeLinkType mode) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doMoveRename(final Entry newEntry) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doRename(final String newKey) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetBinary(final TransferCopier binary) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetContainer() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetLastModified(final long lastModified) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetPrimitive(final Object primitive) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetText(final CharSequence text) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doSetValue(final Object value) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<Boolean> doUnlink() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransferCopier getBinary() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<TransferCopier> getBinaryContent() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getBinaryContentLength() {
		
		// TODO Auto-generated method stub
		return 0;
	}

	//
	
	@Override
	public long getCharacterContentLength() {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Value<? extends TransferCopier> getContentAsBinary(final String key) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<? extends CharSequence> getContentAsText(final String key, final CharSequence defaultValue) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<? extends BaseList<Entry>> getContentCollection(final TreeReadType type) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<? extends Entry> getContentElement(final String key, final TreeLinkType defaultMode) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseObject getContentPrimitive(final String key, final BaseObject defaultValue) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<? extends BaseList<Entry>> getContentRange(final String keyStart, final String keyStop, final int limit, final boolean backwards, final TreeReadType type) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseObject getContentValue(final String key) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKey() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastModified() {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLocation() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeLinkType getMode() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry getParent() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Guid getPrimitiveGuid() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrimitiveValue() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseObject getPrimitiveValue(final ExecProcess ctx) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharSequence getText() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<CharSequence> getTextContent() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBinary() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCharacter() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainer() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainerEmpty() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExist() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFile() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHidden() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMount() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrimitive() {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Value<?> setContentBinary(final String key, final TreeLinkType mode, final TransferCopier binary) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentCachedHardlink(final String key, final Entry entry) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentCachedPrimitive(final String key, final Object primitive) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentContainer(final String key, final TreeLinkType mode) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentHardlink(final String key, final TreeLinkType mode, final Entry entry) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentPrimitive(final String key, final TreeLinkType mode, final Object primitive) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentPublicTreeBinary(final String key, final TransferCopier binary) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentPublicTreeHardlink(final String key, final Entry entry) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentPublicTreePrimitive(final String key, final Object primitive) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentPublicTreeValue(final String key, final Object value) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentText(final String key, final TreeLinkType mode, final CharSequence text) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value<?> setContentUndefined(final String key) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryBinary toBinary() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryCharacter toCharacter() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryContainer toContainer() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryMount toMount() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryPrimitive toPrimitive() {
		
		// TODO Auto-generated method stub
		return null;
	}
}
