package ru.myx.ae3.console.tty;

import java.nio.charset.StandardCharsets;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferTarget;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.console.AbstractCharacterConsole;
import ru.myx.ae3.console.ConsoleState;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.report.Report;
import ru.myx.util.QueueStackRecord;

/** @author myx */
public abstract class ConsoleTty extends AbstractCharacterConsole {
	
	private static final byte[] CRLF = "\r\n".getBytes(StandardCharsets.US_ASCII);
	
	private static final byte[] MSG_WELCOME;
	
	private static final byte[] NULL_BYTES = "null".getBytes(StandardCharsets.US_ASCII);

	private static final byte[] STATE_ATTENTION = new byte[]{
			(byte) 27, // ESCAPE
			(byte) '[', (byte) '0', // reset
			(byte) ';', (byte) '1', // bold
			(byte) ';', (byte) '3', // white foreground
			(byte) '7', (byte) 'm',
			//
	};
	
	private static final byte[] STATE_ERROR = new byte[]{
			(byte) 7, // BEEP
			(byte) 27, // ESCAPE
			(byte) '[', (byte) '0', // reset
			(byte) ';', (byte) '1', // bold
			(byte) ';', (byte) '3', // red
			(byte) '1', (byte) 'm',
			//
	};
	
	private static final byte[] STATE_NORMAL = new byte[]{
			(byte) 27, // ESCAPE
			(byte) '[', (byte) '0', // reset
			(byte) 'm',
			//
	};
	
	private static final byte[] TELNET_DETECTED = "# telnet detected\r\n".getBytes(StandardCharsets.US_ASCII);

	private static final byte[] TELNET_PING = new byte[]{
			//
			// DO SUPPRESS GO AHEAD
			(byte) 255, (byte) 253, (byte) 3,
	};
	
	private static final byte[] TELNET_SETUP = new byte[]{
			//
			// DO SUPPRESS GO AHEAD
			(byte) 255, (byte) 253, (byte) 3,
			// WILL SUPPRESS GO AHEAD
			(byte) 255, (byte) 251, (byte) 3,
			// WILL ECHO
			(byte) 255, (byte) 251, (byte) 1,
			// DON'T LINEMODE
			(byte) 255, (byte) 254, (byte) 34,
			// DO TEMINAL TYPE
			(byte) 255, (byte) 253, (byte) 24,
			// ASK TEMINAL TYPE
			(byte) 255, (byte) 250, (byte) 24, (byte) 1, (byte) 255, (byte) 240,
			// DO WINDOW SIZE
			(byte) 255, (byte) 253, (byte) 31,
			// ASK WINDOW SIZE
			(byte) 255, (byte) 250, (byte) 31, (byte) 1, (byte) 255, (byte) 240,
			/** <code>
			// DO ENCRYPT
			(byte) 255,
			(byte) 253,
			(byte) 38,
			</code> */
			// DON'T ENCRYPT
			(byte) 255, (byte) 254, (byte) 38,
			/** <code>
			// DO AUTHENTICATE
			(byte) 255,
			(byte) 253,
			(byte) 37,
			</code> */
			// DON'T AUTHENTICATE
			(byte) 255, (byte) 254, (byte) 37,
			// end
	};
	
	private static final byte[] TERMINAL_ANSI_DETECTED = "# ansi detected\r\n".getBytes(StandardCharsets.US_ASCII);

	private static final byte[] TERMINAL_PING = new byte[]{
			//
			// WHAT ARE YOU
			(byte) 27, (byte) '[', (byte) 'c',
	};
	
	private static final byte[] TERMINAL_SETUP = new byte[]{
			//
			/** <code>
			// RESET
			(byte) 27,
			(byte) 'c',
			// ERASE ENTIRE SCREEN
			(byte) 27,
			(byte) '[',
			(byte) '2',
			(byte) 'J',
			// STATUS REPORT
			(byte) 27,
			(byte) '[',
			(byte) '5',
			(byte) 'n',
			// WHAT ARE YOU
			(byte) 27,
			(byte) '[',
			(byte) 'c',
			</code> */
			// end
	};
	
	static {
		{
			final TransferCollector collector = Transfer.createCollector();
			/** HEAD */
			collector.getTarget().absorb('#');
			/** TELNET: WILL SUPPRESS GO AHEAD */
			collector.getTarget().absorb(255);
			collector.getTarget().absorb(251);
			collector.getTarget().absorb(3);
			/** TELNET: WILL ECHO */
			collector.getTarget().absorb(255);
			collector.getTarget().absorb(251);
			collector.getTarget().absorb(1);
			/** TERMINAL: WHAT ARE YOU */
			collector.getTarget().absorb(27);
			collector.getTarget().absorb('[');
			collector.getTarget().absorb('c');
			/** Human friendly message */
			collector.getTarget()
					.absorbBuffer(Transfer.wrapBuffer((" Welcome to " + Engine.HOST_NAME + " running " + Engine.VERSION_STRING + "\r\n").getBytes(StandardCharsets.US_ASCII)));

			MSG_WELCOME = collector.toBinary().nextDirectArray();
		}
	}
	
	private AnsiState ansiState;
	
	private boolean ansiDetected;
	
	private final StringBuilder builder = new StringBuilder();
	
	private final TransferCollector collector = Transfer.createCollector();
	
	private int consoleHeight;
	
	private ConsoleTtyState consoleState;
	
	private int consoleWidth;
	
	private ConsoleState stateCurrent;
	
	private ConsoleState stateRequested;
	
	private ConsoleTtyTask<?> task;
	
	private TelnetState telnetState;
	
	private boolean telnetDetected;
	
	private final QueueStackRecord<ConsoleTtyTask<?>> todo = new QueueStackRecord<>();
	
	/**
	 *
	 */
	protected ConsoleTty() {

		this.consoleDestroy();
	}
	
	/**
	 *
	 */
	private void ansiHideCursor() {
		
		if (this.ansiDetected) {
			/** HIDE CURSOR */
			final TransferTarget target = this.collector.getTarget();
			target.absorb(27);
			target.absorb('[');
			target.absorb('?');
			target.absorb('2');
			target.absorb('5');
			target.absorb('l');
		}
	}
	
	/**
	 *
	 */
	private void ansiShowCursor() {
		
		if (this.ansiDetected) {
			/** SHOW CURSOR */
			final TransferTarget target = this.collector.getTarget();
			target.absorb(27);
			target.absorb('[');
			target.absorb('?');
			target.absorb('2');
			target.absorb('5');
			target.absorb('h');
		}
	}
	
	@Override
	public void checkUpdateClient() {
		
		/** DO NOT use checkState from Tasks! */
		synchronized (this) {
			if (this.ansiDetected) {
				/** TERMINAL: WHAT ARE YOU */
				final TransferTarget target = this.collector.getTarget();
				target.absorb(27);
				target.absorb('[');
				target.absorb('c');
			}
		}
		/** no flush intentionally */
		// this.flush();
	}
	
	/** Use this to release resources associated current tty console session */
	protected void consoleDestroy() {
		
		this.consoleState = ConsoleTtyState.INITIAL;
		this.ansiState = AnsiState.UNDETECTED;
		this.ansiDetected = false;
		this.telnetState = TelnetState.UNDETECTED;
		this.telnetDetected = false;
		this.stateCurrent = ConsoleState.NORMAL;
		this.stateRequested = ConsoleState.NORMAL;
		this.consoleWidth = 79;
		this.consoleHeight = 24;
		this.builder.setLength(0);
		this.todo.clear();
		this.collector.reset();
		if (this.task != null) {
			this.task.setResult(null);
			this.task = null;
		}
	}
	
	/** @return */
	protected abstract String consolePeerIdentity();
	
	/** Use this to start a new tty console session */
	protected void consoleStart() {
		
		final String peerIdentity = this.consolePeerIdentity();
		this.consoleState = ConsoleTtyState.HANDSHAKE;
		final TransferTarget target = this.collector.getTarget();
		target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.MSG_WELCOME));
		target.absorbBuffer(Transfer.wrapBuffer(("# your address is: " + peerIdentity + "\r\n").getBytes(StandardCharsets.US_ASCII)));
		this.consumeFromClientWanted(true);
		this.flush();
		final ExecProcess ctx = Exec.createProcess(null, "TTY Session: " + peerIdentity);
		ctx.setConsole(this);
		Act.later(ctx, TtySession.INSTANCE, ctx, 1000L);
	}
	
	/** @param b
	 * @return */
	protected boolean consumeFromClient(final int b) {
		
		return this.nextTelnet(b);
	}
	
	/** @param flag
	 * @return */
	protected abstract boolean consumeFromClientWanted(final boolean flag);
	
	/** @param buffer
	 * @return */
	protected abstract boolean consumeFromServer(final TransferBuffer buffer);
	
	private final boolean consumeNextPayload(final int b) {
		
		final ConsoleTtyTask<?> task;
		flush : {
			sync : synchronized (this) {
				if (this.task == null) {
					if ((this.task = this.todo.next()) == null) {
						task = null;
						break sync;
					}
					task = this.task;
					task.onTaskInit(this);
					break sync;
				}
				task = this.task;
				break flush;
			}
			if (task == null) {
				this.consumeFromClientWanted(false);
				this.ansiHideCursor();
				this.flush();
				return true;
			}
			this.flush();
		}
		
		final ConsoleTtyTask<?> replacement = task.consumeNext(this, b);
		
		if (task != replacement) {
			hide : {
				sync : synchronized (this) {
					if (replacement == null) {
						if ((this.task = this.todo.next()) == null) {
							break sync;
						}
						this.task.onTaskInit(this);
						break hide;
					}
					this.task = replacement;
					break hide;
				}
				this.consumeFromClientWanted(false);
				this.ansiHideCursor();
				this.flush();
				return true;
			}
		}
		this.flush();
		return true;
	}
	
	@Override
	public void flush() {
		
		final TransferBuffer buffer;
		synchronized (this) {
			/** collector is reset ready to collect new messages after that */
			buffer = this.collector.toBinary().nextCopy();
		}
		assert buffer != null : "NULL buffer";
		if (buffer.hasRemaining()) {
			if (!this.consumeFromServer(buffer)) {
				throw new RuntimeException("Connection reset");
			}
		}
	}
	
	/** @return */
	private TransferTarget internCheckPrepareMessage() {
		
		final TransferTarget target = this.collector.getTarget();
		if (this.task != null) {
			if (this.telnetDetected) {
				target.absorb(255);
				target.absorb(248);
			}
			if (this.ansiDetected) {
				/** CLEAR LINE */
				target.absorb(27);
				target.absorb('[');
				target.absorb('2');
				target.absorb('K');
				/** POSITION 0 */
				target.absorb(27);
				target.absorb('[');
				target.absorb('0');
				target.absorb('G');
			}
		}
		this.internCheckStatus();
		return target;
	}
	
	private void internCheckStatus() {
		
		if (this.stateCurrent != this.stateRequested) {
			if (this.ansiDetected) {
				if (this.stateRequested == ConsoleState.NORMAL) {
					this.collector.getTarget().absorbBuffer(Transfer.wrapBuffer(ConsoleTty.STATE_NORMAL));
				} else //
				if (this.stateRequested == ConsoleState.ATTENTION) {
					this.collector.getTarget().absorbBuffer(Transfer.wrapBuffer(ConsoleTty.STATE_ATTENTION));
				} else //
				if (this.stateRequested == ConsoleState.ERROR) {
					this.collector.getTarget().absorbBuffer(Transfer.wrapBuffer(ConsoleTty.STATE_ERROR));
				}
			}
			this.stateCurrent = this.stateRequested;
		}
	}
	
	boolean internEcho(final int b) {
		
		if (this.telnetDetected) {
			this.internCheckStatus();
			final TransferTarget target = this.collector.getTarget();
			if (this.ansiDetected && b == 8) {
				target.absorbBuffer(Transfer.wrapBuffer(new byte[]{
						(byte) 22, (byte) (b & 0x7F)
				}));
				this.flush();
				return true;
			}
			/** if ((this.ansiState != AnsiState.UNDETECTED) && ((b & 0x80) != 0)) {
			 * this.consumeFromServer( Transfer.wrapBuffer( new byte[] { (byte) 22, (byte) (b &
			 * 0x7F) } ) ); return; } */
			target.absorbBuffer(Transfer.singletonBuffer((byte) b));
			this.flush();
		}
		return true;
	}
	
	private final void internSendMessageIdent(final String message, final int index) {

		boolean ident = true;
		final TransferTarget target = this.collector.getTarget();
		for (final char c : message.toCharArray()) {
			if (c == '\r') {
				continue;
			}
			if (c == '\n') {
				target.absorbArray(ConsoleTty.CRLF, 0, 2);
				ident = true;
				continue;
			}
			if (ident) {
				ident = false;
				for (int i = index; i > 0; --i) {
					target.absorb('\t');
				}
			}
			target.absorbBuffer(Transfer.wrapBuffer(Character.toString(c).getBytes(StandardCharsets.UTF_8)));
		}
		target.absorb('\r');
		target.absorb('\n');
		this.flush();
	}
	
	/** @return */
	public boolean isAnsiDetected() {
		
		return this.ansiDetected;
	}
	
	/** @return */
	public boolean isTelnetDetected() {
		
		return this.telnetDetected;
	}
	
	private final boolean nextTelnet(final int i) {
		
		switch (this.telnetState) {
			case UNDETECTED : {
				if (i != 255 || this.consoleState != ConsoleTtyState.HANDSHAKE) {
					if (i == 10 && this.consoleState == ConsoleTtyState.HANDSHAKE) {
						this.consoleState = ConsoleTtyState.IDLE;
					}
					return this.nextTerminal(i);
				}
				
				final TransferTarget target = this.collector.getTarget();
				target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TELNET_DETECTED));
				target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TELNET_SETUP));
				this.telnetState = TelnetState.DETECTED;
				this.telnetDetected = true;
				this.onDetectTelnet();
			}
			//$FALL-THROUGH$
			case DETECTED :
				if (i == 255) {
					this.telnetState = TelnetState.ATTENTION;
					return true;
				}
				return this.nextTerminal(i);
			case ATTENTION :
				switch (i) {
					case 240 :
						this.telnetState = TelnetState.DETECTED;
						return true;
					case 250 :
						this.telnetState = TelnetState.TELNET_SB;
						return true;
					case 251 :
						this.telnetState = TelnetState.TELNET_WILL;
						return true;
					case 252 :
						this.telnetState = TelnetState.TELNET_WONT;
						return true;
					case 253 :
						this.telnetState = TelnetState.TELNET_DO;
						return true;
					case 254 :
						this.telnetState = TelnetState.TELNET_DONT;
						return true;
					default :
						System.out.println(">>>>> TELNET_ATTENTION, byte=" + i);
						this.telnetState = TelnetState.DETECTED;
						return true;
				}
			case TELNET_SB :
				switch (i) {
					case 24 :
						this.builder.setLength(0);
						this.telnetState = TelnetState.TELNET_SB_TERM_TYPE;
						return true;
					case 31 :
						this.telnetState = TelnetState.TELNET_SB_DIMENSIONS_W1;
						return true;
					default :
				}
				System.out.println(">>>>> TELNET_SB, byte=" + i);
				this.telnetState = TelnetState.DETECTED;
				return true;
			case TELNET_SB_DIMENSIONS_W1 :
				this.consoleWidth = i * 256;
				this.telnetState = TelnetState.TELNET_SB_DIMENSIONS_W2;
				return true;
			case TELNET_SB_DIMENSIONS_W2 :
				this.consoleWidth += i;
				this.telnetState = TelnetState.TELNET_SB_DIMENSIONS_H1;
				return true;
			case TELNET_SB_DIMENSIONS_H1 :
				this.consoleHeight = i * 256;
				this.telnetState = TelnetState.TELNET_SB_DIMENSIONS_H2;
				return true;
			case TELNET_SB_DIMENSIONS_H2 :
				this.consoleHeight += i;
				this.sendMessage("# got dimensions: " + this.consoleWidth + "x" + this.consoleHeight);
				this.telnetState = TelnetState.DETECTED;
				return true;
			case TELNET_SB_TERM_TYPE :
				if (i == 255) {
					this.sendMessage("# got term type: " + this.builder);
					this.builder.setLength(0);
					this.telnetState = TelnetState.ATTENTION;
					return true;
				}
				this.builder.append((char) i);
				return true;
			case TELNET_WILL :
				this.telnetState = TelnetState.DETECTED;
				switch (i) {
					case 3 :
						// will suppress go ahead
						return true;
					case 24 :
						// will report terminal type changes
						return true;
					case 31 :
						// will report dimensions
						return true;
					case 37 :
						// will authenticate
						this.sendMessage("# will authenticate");
						return true;
					case 38 :
						// will encrypt
						this.sendMessage("# will encrypt");
						return true;
					default :
				}
				Report.event("TELNET", "unknown will", "TELNET_WILL, byte=" + i);
				return true;
			case TELNET_WONT :
				this.telnetState = TelnetState.DETECTED;
				Report.event("TELNET", "unknown won't", "TELNET_WONT, byte=" + i);
				return true;
			case TELNET_DO :
				this.telnetState = TelnetState.DETECTED;
				switch (i) {
					case 1 :
						// do echo
						return true;
					case 3 :
						// do suppress go ahead
						return true;
					default :
				}
				Report.event("TELNET", "unknown do", "TELNET_DO, byte=" + i);
				return true;
			case TELNET_DONT :
				this.telnetState = TelnetState.DETECTED;
				Report.event("TELNET", "unknown don't", "TELNET_DONT, byte=" + i);
				return true;
			default :
		}
		return false;
	}
	
	private final boolean nextTerminal(final int i) {
		
		switch (this.ansiState) {
			case UNDETECTED : {
				if (i != 27 || this.consoleState != ConsoleTtyState.HANDSHAKE) {
					if (this.consoleState == ConsoleTtyState.HANDSHAKE && i == 10) {
						this.consoleState = ConsoleTtyState.IDLE;
					}
					return this.consumeNextPayload(i);
				}
				final TransferTarget target = this.collector.getTarget();
				target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TERMINAL_ANSI_DETECTED));
				target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TERMINAL_SETUP));
				this.ansiState = AnsiState.DETECTED;
				this.ansiDetected = true;
				this.onDetectAnsi();
			}
			//$FALL-THROUGH$
			case DETECTED :
				switch (i) {
					case 3 :
						return false;
					case 22 :
						this.ansiState = AnsiState.HIGH_BIT;
						return true;
					case 27 :
						this.ansiState = AnsiState.ESCAPE;
						return true;
					default :
				}
				return this.consumeNextPayload(i);
			case HIGH_BIT :
				this.ansiState = AnsiState.DETECTED;
				System.out.println(">>>>> HIGH, byte=" + i);
				return this.nextTerminal(0x80 | i);
			case ESCAPE :
				switch (i) {
					case 27 :
						this.ansiState = AnsiState.DETECTED;
						return this.consumeNextPayload(i);
					case 59 :
						this.ansiState = AnsiState.DT1;
						return true;
					case '[' : // 91
						this.ansiState = AnsiState.DT2;
						return true;
					default :
				}
				System.out.println(">>>>> ATT, byte=" + i);
				Report.event("TELNET", "unknown attention", "TERMINAL_ATTENTION, byte=" + i);
				this.ansiState = AnsiState.DETECTED;
				return true;
			case DT1 :
				System.out.println(">>>>> EXT1, byte=" + i);
				Report.event("TELNET", "unknown ext1", "TERMINAL_TERMINAL_EXT1_KEY, byte=" + i);
				this.ansiState = AnsiState.DETECTED;
				return true;
			case DT2 :
				this.builder.append((char) i);
				if (i >= 64 && i <= 126) {
					/** vt100 - ^[[?1;0c ^[[?1;1c or ^[[?1;2c
					 *
					 * http://docstore.mik.ua/orelly/unix/upt/ch05_05.htm */
					final String string = this.builder.toString();
					if (string.equals("?1;0c") || string.equals("?1;1c") || string.equals("?1;2c")) {
						// vt100
					} else {
						System.out.println(">>>>> EXT2, buf=" + this.builder);
						Report.event("TELNET", "unknown ext2", "TERMINAL_TERMINAL_EXT2_KEY, buf=" + this.builder);
					}
					this.ansiState = AnsiState.DETECTED;
					this.builder.setLength(0);
				}
				return true;
			case DT3 :
				System.out.println(">>>>> EXT3, byte=" + i);
				Report.event("TELNET", "unknown ext3", "TERMINAL_TERMINAL_EXT2_KEY, byte=" + i);
				this.ansiState = AnsiState.DETECTED;
				return true;
			default :
		}
		return false;
	}
	
	/** Override this method with a proper trigger.
	 *
	 * Called not more than once per session. */
	protected void onDetectAnsi() {
		
		// empty
	}
	
	/** Override this method with a proper trigger.
	 *
	 * Called not more than once per session. */
	protected void onDetectTelnet() {
		
		// empty
		
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title) {
		
		final ConsoleTtyTask<Boolean> task = this.telnetDetected
			? new TaskReadBooleanCharacter(title, null)
			: new TaskReadBooleanString(title, null);
		this.todo(task);
		return task;
	}
	
	@Override
	public Value<Boolean> readBoolean(final String title, final boolean defaultValue) {

		final ConsoleTtyTask<Boolean> task = this.telnetDetected
			? new TaskReadBooleanCharacter(
					title,
					defaultValue
						? Boolean.TRUE
						: Boolean.FALSE)
			: new TaskReadBooleanString(
					title,
					defaultValue
						? Boolean.TRUE
						: Boolean.FALSE);
		this.todo(task);
		return task;
	}
	
	@Override
	public Value<?> readContinue(final String title) {
		
		final ConsoleTtyTask<?> task = new TaskReadContinue(title);
		this.todo(task);
		return task;
	}
	
	@Override
	public Value<Number> readInteger(final String title) {
		
		final ConsoleTtyTask<Number> task = new TaskReadNumber(title, null);
		this.todo(task);
		return task;
	}
	
	@Override
	public Value<Number> readInteger(final String title, final int defaultValue) {

		final ConsoleTtyTask<Number> task = new TaskReadNumber(title, Integer.valueOf(defaultValue));
		this.todo(task);
		return task;
	}
	
	@Override
	protected Value<String> readPasswordImpl(final String title) {
		
		final ConsoleTtyTask<String> task = new TaskReadPassword(title);
		this.todo(task);
		return task;
	}
	
	@Override
	protected Value<String> readStringImpl(final String title, final String defaultValue) {

		final ConsoleTtyTask<String> task = new TaskReadString(title, defaultValue);
		this.todo(task);
		return task;
	}
	
	@Override
	protected boolean sendMessageImpl(final String message) {
		
		final TransferTarget target = this.internCheckPrepareMessage();
		final byte[] bytes = message == null
			? ConsoleTty.NULL_BYTES
			: message.getBytes(StandardCharsets.UTF_8);
		target.absorbArray(bytes, 0, bytes.length);
		target.absorb('\r');
		target.absorb('\n');
		/** DO NOT use sendMessage from Tasks! */
		if (this.task != null) {
			this.task.onTaskInit(this);
		}
		return true;
	}
	
	@Override
	protected boolean sendMessageImpl(final String title, final String body) {
		
		final TransferTarget target = this.internCheckPrepareMessage();
		target.absorbBuffer(Transfer.wrapBuffer(title.getBytes(StandardCharsets.UTF_8)));
		target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.CRLF));
		this.internSendMessageIdent(body, 1);
		/** DO NOT use sendMessage from Tasks! */
		if (this.task != null) {
			this.task.onTaskInit(this);
		}
		return true;
	}
	
	void sendStatus(final String message) {
		
		this.internCheckStatus();
		this.collector.getTarget().absorbBuffer(Transfer.wrapBuffer(message.getBytes(StandardCharsets.UTF_8)));
	}
	
	@Override
	public void setState(final ConsoleState state) {
		
		this.stateRequested = state;
	}
	
	private void todo(final ConsoleTtyTask<?> task) {
		
		boolean flush = false;
		synchronized (this) {
			if (this.task == null) {
				this.task = task;
				this.task.onTaskInit(this);
				flush = true;
			} else {
				this.todo.enqueue(task);
			}
		}
		if (flush) {
			this.consumeFromClientWanted(true);
			this.ansiShowCursor();
			this.flush();
		}
	}
	
	/** @return */
	protected boolean tryPingClient() {
		
		if (this.telnetDetected) {
			final TransferTarget target = this.collector.getTarget();
			target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TELNET_PING));
			this.flush();
			return true;
		}
		if (this.ansiDetected) {
			final TransferTarget target = this.collector.getTarget();
			target.absorbBuffer(Transfer.wrapBuffer(ConsoleTty.TERMINAL_PING));
			this.flush();
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean write(final int c) {
		
		return this.collector.getTarget().absorb(c);
	}
	
	@Override
	protected boolean write(final String text) {
		
		return this.collector.getTarget().absorbBuffer(Transfer.wrapBuffer(text.getBytes(StandardCharsets.UTF_8)));
	}
}
