package ru.myx.ae3.i3;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import ru.myx.ae3.answer.AbstractReplyException;
import ru.myx.ae3.answer.Reply;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.serve.ServeRequest;
import ru.myx.ae3.skinner.SkinScanner;
import ru.myx.ae3.skinner.Skinner;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.Storage;

/** @author myx */
public abstract class TargetInterfaceAbstract extends BaseHostEmpty implements TargetInterface {
	
	private final Entry root;
	
	private final Entry base;
	
	/** @param root
	 *            (same as base) */
	public TargetInterfaceAbstract(final Entry root) {
		
		this.root = root;
		this.base = root;
	}
	
	/** @param root
	 * @param base */
	public TargetInterfaceAbstract(final Entry root, final Entry base) {
		
		this.root = root;
		this.base = base;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		return Reflect.classToBasePrototype(TargetInterface.class);
	}
	
	@Override
	public Entry getBase() {
		
		return this.base;
	}
	
	@Override
	public Entry getRoot() {
		
		return this.root;
	}
	
	@Override
	public ReplyAnswer onQuery(final ServeRequest request) {
		
		final String resourceIdentifier = request.getResourceIdentifier();
		if (resourceIdentifier.length() <= 3 || //
				resourceIdentifier.charAt(0) != '/' || //
				resourceIdentifier.charAt(1) != '!' || //
				resourceIdentifier.charAt(2) != '/'//
		) {
			return null;
		}
		if (resourceIdentifier.regionMatches(false, 3, "skin/", 0, 5)) {
			final int queryPosition = resourceIdentifier.indexOf(
					'/', //
					/* "/!/skin/".length() */
					8);
			if (queryPosition != -1) {
				final String skin = resourceIdentifier.substring(8, queryPosition);
				{
					final Skinner skinner = SkinScanner.getSystemSkinner(skin);
					if (skinner != null) {
						/** FIXME: SkinNewAbstract contains similar code! */
						request.shiftRequested(queryPosition, true);
						// System.out.println(" >>> >>>>> 8: " + this + ", skinner: " + skinner + ",
						// query:" + request);
						try {
							final ReplyAnswer answer = skinner.onQuery(request);
							// System.out.println(" >>> >>>>> A: " + this + ", skinner: " + skinner
							// + ", answer:" + answer);
							if (answer != null && answer != request) {
								return skinner.handleReply(answer);
							}
						} catch (final AbstractReplyException e) {
							final ReplyAnswer answer = e.getReply();
							// System.out.println(" >>> >>>>> B: " + this + ", skinner: " + skinner
							// + ", answer:" + answer);
							if (answer != null && answer != request) {
								return skinner.handleReply(answer);
							}
						}
					}
				}
			}
			return null;
		}
		if (resourceIdentifier.regionMatches(false, 3, "class/", 0, 6)) {
			final String className = resourceIdentifier.substring( /* "/!/class/". length() */9);
			final InputStream resource = this.getClass().getClassLoader().getResourceAsStream(className);
			return resource == null
				? Reply.string("WSM-QD", request, className)//
						.setCode(Reply.CD_UNKNOWN)
				: Reply.binary("WSM-QD", request, Transfer.createBuffer(resource)) //
						.setTimeToLiveDays(1) //
						.setFinal() //
			;
		}
		final Entry entry;
		if (resourceIdentifier.regionMatches(false, 3, "public/", 0, 7)) {
			entry = Storage.PUBLIC
					.relative(resourceIdentifier.substring( /* "/!/public/".length() */10), null);
		} else //
		if (resourceIdentifier.regionMatches(false, 3, "base/", 0, 5)) {
			entry = this.base.relative(resourceIdentifier.substring( /* "/!/base/".length() */8), null);
		} else {
			return null;
		}
		return entry != null && entry.isExist()
			? entry.isBinary()
				? Reply.entry(
						"IFACE", //
						request,
						entry.toBinary(),
						entry.getKey())//
						// .setContentDisposition( "file" )
						.setContentDisposition("inline")
				: Reply.string(
						"IFACE", //
						request,
						"Directory listing is not allowed: " + resourceIdentifier)//
						.setCode(Reply.CD_UNKNOWN)
			: Reply.string(
					"IFACE", //
					request,
					"Not found: " + resourceIdentifier)//
					.setCode(Reply.CD_UNKNOWN);
	}
	
	@Override
	public final URI resolveEntry(final Entry entry) {
		
		try {
			for (Entry current = entry; current != null; current = current.getParent()) {
				if (current == Storage.PUBLIC) {
					final String base = Storage.PUBLIC.getLocation();
					final String full = entry.getLocation();
					assert full.startsWith(base);
					return new URI("/!/public" + full.substring(base.length()));
				}
				if (current == this.base) {
					final String base = this.base.getLocation();
					final String full = entry.getLocation();
					assert full.startsWith(base);
					return new URI("/!/base" + full.substring(base.length()));
				}
			}
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	@Override
	public final Entry resolveEntry(final String path) {
		
		if (path.startsWith("/!/public/")) {
			return Storage.PUBLIC.relative(path.substring("/!/public/".length()), null);
		}
		if (path.startsWith("/!/base/")) {
			return this.base.relative(path.substring("/!/base/".length()), null);
		}
		return null;
	}
	
	@Override
	public final Entry resolveEntry(final URI uri) {
		
		final String string = uri.getPath();
		return this.resolveEntry(string);
	}
}
