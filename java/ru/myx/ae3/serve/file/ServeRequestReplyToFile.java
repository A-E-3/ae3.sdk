package ru.myx.ae3.serve.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.function.Function;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.serve.AbstractServeRequestMutable;

/**
 * @author myx
 *
 */
public class ServeRequestReplyToFile extends AbstractServeRequestMutable<ServeRequestReplyToFile> implements Function<ReplyAnswer, Boolean> {
	
	/**
	 * Writes reply to a file
	 *
	 * @param reply
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeReplyToFile(final ReplyAnswer reply, final File file) throws FileNotFoundException, IOException {
		
		assert reply != null : "NULL reply!";
		if (reply.isFile()) {
			try (final RandomAccessFile source = new RandomAccessFile(reply.getFile(), "r")) {
				try (final RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
					for (long position = 0;;) {
						final long read = raf.getChannel().transferFrom(source.getChannel(), position, source.length() - position);
						if (read <= 0) {
							break;
						}
						position += read;
					}
				}
			}
			return;
		}
		if (reply.isBinary()) {
			final FileOutputStream output = new FileOutputStream(file);
			Transfer.toStream(reply.toBinary().getBinary(), output, true);
			return;
		}
		if (reply.isCharacter()) {
			try (final RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
				Transfer.toStream(Transfer.createBufferUtf8(reply.toCharacter().getText()), raf);
			}
			return;
		}
		{
			throw new IllegalArgumentException("Unsupported reply type: " + reply);
		}
	}
	
	/**
	 *
	 */
	protected final File file;
	
	ServeRequestReplyToFile(final File file) {
		super("FILE", "GET", null);
		this.file = file;
		this.setResourceIdentifier(file.getName());
		this.setResponseTarget(this);
	}
	
	@Override
	public Boolean apply(final ReplyAnswer reply) {
		
		try {
			ServeRequestReplyToFile.writeReplyToFile(reply, this.file);
		} catch (final IOException e) {
			throw new Transfer.TransferOperationException("writing: " + this.file.getAbsolutePath(), e);
		}
		return Boolean.TRUE;
	}
	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName() + ": file=" + this.file;
	}
	
}
