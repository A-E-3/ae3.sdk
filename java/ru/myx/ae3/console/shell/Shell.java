package ru.myx.ae3.console.shell;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.console.Console;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecArguments;
import ru.myx.ae3.exec.ExecArgumentsListXWrapped;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.help.Text;
import ru.myx.ae3.l2.LayoutEngine;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;
import ru.myx.ae3.vfs.EntryContainer;
import ru.myx.ae3.vfs.EntryVfsRoot;
import ru.myx.ae3.vfs.Storage;
import ru.myx.sapi.FormatSAPI;

/** @author myx */
public class Shell extends AbstractShellCommand {

	private static enum CommandState {
		/**
		 *
		 */
		ESCAPED_NORM,
		/**
		 *
		 */
		ESCAPED_QUOTED,
		/**
		 *
		 */
		NORM,
		/**
		 *
		 */
		QUOTE,
		/**
		 *
		 */
		QUOTED,
	}

	private static final Map<String, ShellCommand> COMMANDS;

	static {
		COMMANDS = new TreeMap<>();
		Shell.registerCommand(new Shell());
	}

	/** @param argument
	 * @return */
	// TODO: use context pwd etc for argument expansion?
	public static final Set<String> calculateOptions(final String argument) {

		final Set<String> result = new TreeSet<>();
		for (final char c : argument.toCharArray()) {
			result.add(String.valueOf(c));
		}
		return result;
	}

	/** @param ctx
	 * @param args
	 * @return Anything but REPEAT */
	public static final ExecStateCode exec(final ExecProcess ctx, final BaseArray args) {

		if (args == null || args.length() == 0) {
			return ctx.vmSetCallResultFalse();
		}
		final String name = Base.getFirstString(args, null);
		final ShellCommand cmd = Shell.getCommand(name);
		if (cmd == null) {
			return ctx.vmRaise("unknown command: " + name);
		}
		final ExecArguments arguments = ExecArgumentsListXWrapped.createArguments(args);
		for (;;) {
			final ExecStateCode code = cmd.execCallPrepare(ctx, ctx, ResultHandler.FA_BNN_NXT, true, arguments);
			if (code != ExecStateCode.REPEAT) {
				return code;
			}
		}
	}

	/** @param ctx
	 * @param commandLine
	 * @return */
	public static final ExecStateCode exec(final ExecProcess ctx, final String commandLine) {

		final BaseList<String> argumentList = BaseObject.createArray(4);
		final ExecStateCode code = Shell.parseCommandLine(ctx, commandLine, argumentList);
		if (code != null) {
			return code;
		}
		if (argumentList.isEmpty()) {
			ctx.vmRaise("Command-line is empty!");
		}
		return Shell.exec(ctx, argumentList);
	}

	/** @param ctx
	 * @param args
	 * @return */
	public static final ExecStateCode exec(final ExecProcess ctx, final String[] args) {

		if (args == null || args.length == 0) {
			return ctx.vmSetCallResultFalse();
		}
		final String name = args[0];
		final ShellCommand cmd = Shell.getCommand(name);
		if (cmd == null) {
			return ctx.vmRaise("unknown command: " + name);
		}
		final ExecArguments arguments = ExecArgumentsListXWrapped.createArguments(Base.forArray(args));
		for (;;) {
			final ExecStateCode code = cmd.execCallPrepare(ctx, ctx, ResultHandler.FA_BNN_NXT, true, arguments);
			if (code != ExecStateCode.REPEAT) {
				return code;
			}
		}
	}

	/** @param ctx
	 * @param args
	 * @return
	 * @throws Exception */
	public static final BaseObject execNative(final ExecProcess ctx, final BaseArray args) throws Exception {

		final ExecStateCode code = Shell.exec(ctx, args);
		if (code == null || code == ExecStateCode.NEXT) {
			return ctx.vmGetResultDetachable();
		}
		if (code == ExecStateCode.ERROR) {
			final BaseObject result = ctx.vmGetResultDetachable();
			if (result instanceof Exception) {
				throw (Exception) result;
			}
			if (result instanceof Error) {
				throw (Error) result;
			}
			throw Exec.createThrown(result, ctx.contextGetDebug(), null);
		}
		throw new RuntimeException("Illegal state code: " + code);
	}

	/** @param ctx
	 * @param commandLine
	 * @return
	 * @throws Exception */
	public static final BaseObject execParse(final ExecProcess ctx, final String commandLine) throws Exception {

		final ExecStateCode code = Shell.exec(ctx, commandLine);
		if (code == null || code == ExecStateCode.NEXT) {
			return ctx.vmGetResultDetachable();
		}
		if (code == ExecStateCode.ERROR) {
			final BaseObject result = ctx.vmGetResultDetachable();
			if (result instanceof Exception) {
				throw (Exception) result;
			}
			if (result instanceof Error) {
				throw (Error) result;
			}
			throw Exec.createThrown(result, ctx.contextGetDebug(), null);
		}
		throw new RuntimeException("Illegal state code: " + code);
	}

	/** @param ctx
	 * @param console
	 * @param commandLine
	 * @return
	 * @throws Exception */
	public static final BaseObject executeNativeCommandWithConsole(final ExecProcess ctx, final Console console, final String commandLine) throws Exception {

		final Console prev = ctx.getConsole();
		try {
			ctx.setConsole(console);
			return Shell.execParse(ctx, commandLine);
		} finally {
			ctx.setConsole(prev);
		}
	}

	/** @param ctx
	 * @param pathCurrent
	 * @param argumentList
	 * @param string
	 * @return false to stop */
	private static boolean
			expandPatternComponent(final ExecProcess ctx, final Entry focus, final StringBuilder pathCurrent, final BaseList<String> argumentList, final String string) {

		final String component;
		final String componentsLeft;
		{
			final StringBuilder currentPathComponent = new StringBuilder();
			boolean prevPathEscaped = false;
			boolean left = true;
			int position = 0;
			components : for (final StringTokenizer paths = new StringTokenizer(string, "\\/", true);;) {
				final String pathComponent;
				if (paths.hasMoreTokens()) {
					pathComponent = paths.nextToken();
				} else {
					if (prevPathEscaped) {
						return false;
					}
					pathComponent = "/";
					left = false;
				}
				final int length = pathComponent.length();
				position += length;
				if (length == 1) {
					if ('\\' == pathComponent.charAt(0)) {
						/** keep all characters */
						currentPathComponent.append('\\');
						if (prevPathEscaped) {
							prevPathEscaped = false;
							continue components;
						}
						prevPathEscaped = true;
						continue components;
					}
					if ('/' == pathComponent.charAt(0)) {
						if (prevPathEscaped) {
							currentPathComponent.append('/');
							prevPathEscaped = false;
							continue components;
						}
						{
							component = currentPathComponent.toString();
							if (component.length() == 0) {
								/** no empty components */
								return false;
							}
							if (!left) {
								componentsLeft = null;
							} else {
								componentsLeft = string.substring(position);
							}
							break components;
						}
					}
				}
				{
					prevPathEscaped = false;
					currentPathComponent.append(pathComponent);
					continue components;
				}
			}
		}
		if (componentsLeft != null && !focus.isContainer()) {
			/** non-container (or doesn't exist) - no children */
			return false;
		}
		{
			final int basePathLength = pathCurrent.length();
			if (basePathLength != 0 && (basePathLength != 1 || pathCurrent.charAt(0) != '/')) {
				pathCurrent.append('/');
			}
		}
		final EntryContainer folder = focus.toContainer();
		{
			if (component.indexOf('*') != -1) {
				boolean patternFound = false;
				final StringBuilder currentPattern = new StringBuilder();
				boolean prevPatternEscaped = false;
				patterns : for (final StringTokenizer st = new StringTokenizer(string, "\\*", true); st.hasMoreTokens();) {
					final String patternComponent = st.nextToken();
					if (patternComponent.length() == 1) {
						if ('\\' == patternComponent.charAt(0)) {
							if (prevPatternEscaped) {
								currentPattern.append('\\');
								prevPatternEscaped = false;
								continue patterns;
							}
							prevPatternEscaped = true;
							continue patterns;
						}
						if ('*' == patternComponent.charAt(0)) {
							if (prevPatternEscaped) {
								currentPattern.append('*');
								prevPatternEscaped = false;
								continue patterns;
							}
							patternFound = true;
						}
					}
					prevPatternEscaped = false;
				}
				if (patternFound) {
					final BaseList<Entry> entries = folder.getContentCollection(null).baseValue();
					if (entries == null || entries.length() == 0) {
						return false;
					}
					final String current = pathCurrent.toString();
					if (componentsLeft == null) {
						boolean found = false;
						for (final Entry entry : entries) {
							argumentList.add(current + entry.getKey());
							found = true;
						}
						return found;
					}
					{
						final int save = pathCurrent.length();
						boolean found = false;
						for (final Entry entry : entries) {
							pathCurrent.append(entry.getKey());
							if (Shell.expandPatternComponent(ctx, entry, pathCurrent, argumentList, componentsLeft)) {
								found = true;
							}
							pathCurrent.setLength(save);
						}
						return found;
					}
				}
			}
			/** no actual pattern */
			{
				final Entry entry = folder.relative(component, null);
				if (entry == null || !entry.isExist()) {
					/** Stop - this one does not exist */
					return false;
				}
				pathCurrent.append(component);
				if (componentsLeft == null) {
					argumentList.add(pathCurrent.toString());
					return true;
				}
				{
					return Shell.expandPatternComponent(ctx, entry, pathCurrent, argumentList, componentsLeft);
				}
			}
		}
	}

	/** FIXME: \* should not be processed! need 3 stages.
	 *
	 * @param ctx
	 * @param argumentList
	 * @param string */
	private static void expandPatterns(final ExecProcess ctx, final BaseList<String> argumentList, final String string) {

		/** TODO <code>
		if(string.length() == 1){
			if('~' == string.charAt( 0 )){
				// argumentList.add(ctx.getHome());
				return;
			}
		}
		</code> */
		if (string.length() == 0) {
			argumentList.add(string);
			return;
		}

		if (string.indexOf('*') == -1) {
			if (string.charAt(0) == '$') {
				argumentList.add(Base.getString(ctx, string.substring(1), ""));
				return;
			}
			argumentList.add(string);
			return;
		}
		final int size = argumentList.size();
		final EntryContainer root;
		final String relative;
		final StringBuilder pathCurrent = new StringBuilder();
		if (string.charAt(0) == '/') {
			root = ctx.fldVfsShare.toContainer();
			relative = string.substring(1);
			pathCurrent.append('/');
		} else {
			root = ctx.fldVfsFocus.toContainer();
			relative = string;
		}
		if (Shell.expandPatternComponent(ctx, root, pathCurrent, argumentList, relative)) {
			/** cleanup? is it needed? */
			while (size > argumentList.size()) {
				argumentList.remove(size);
			}
		} else {
			/** add original string is nothing expanded */
			argumentList.add(string);
		}
		return;
	}

	/** @param name
	 * @return */
	public static final ShellCommand getCommand(final String name) {

		final ShellCommand command = Shell.COMMANDS.get(name);
		if (command != null) {
			return command.clone();
		}
		/** check for extension commands. */
		{
			final Entry e = Storage.UNION.relative("settings/shell/" + name + ".json", null);
			if (e != null /* && e.isExist() */) {
				final String source = e.toBinary().getBinaryContent().baseValue().toString(StandardCharsets.UTF_8);
				final BaseObject o = LayoutEngine.parseJSLD(source);
				final String type = Base.getString(o, "type", "");
				if (!"ae3/Executable".equals(type)) {
					Report.exception("SHELL", "invalid command", new Error("invalid command type: " + name + ", type=" + type));
					return null;
				}
				final String reference = Base.getString(o, "reference", "");
				if (reference.length() == 0) {
					Report.exception("SHELL", "invalid command", new Error("command reference is empty: " + name));
					return null;
				}
				return new ShellJsCommand(name, reference);
			}
		}

		return null;
	}

	static final Map<String, ShellCommand> getCommands() {

		final Entry e = Storage.UNION.relative("settings/shell/", null);
		if (e == null || !e.isExist()) {
			return Shell.COMMANDS;
		}

		final BaseList<Entry> children = e.toContainer().getContentCollection(null).baseValue();
		if (children == null || children.length() == 0) {
			return Shell.COMMANDS;
		}

		final Map<String, ShellCommand> result = Create.tempMap(Shell.COMMANDS);
		for (final Entry child : children) {
			final String key = child.getKey();
			if (key.endsWith(".json")) {
				final String name = key.substring(0, key.length() - 5);
				result.put(name, Shell.getCommand(name));
			}
		}
		return result;
	}

	/** @param ctx
	 * @param commandLine
	 * @param argumentList
	 * @return */
	private static ExecStateCode parseCommandLine(final ExecProcess ctx, final String commandLine, final BaseList<String> argumentList) {

		{
			CommandState state = CommandState.NORM;
			final StringBuilder buffer = new StringBuilder();
			main : for (final char c : commandLine.toCharArray()) {
				switch (state) {
					case NORM : {
						if (c == '#' && buffer.length() == 0) {
							break main;
						}
						if (c == '\\') {
							state = CommandState.ESCAPED_NORM;
							continue main;
						}
						if (c == '"') {
							if (buffer.length() == 0) {
								state = CommandState.QUOTED;
							} else {
								buffer.append(c);
							}
							continue main;
						}
						if (Character.isWhitespace(c)) {
							if (buffer.length() > 0) {
								Shell.expandPatterns(ctx, argumentList, buffer.toString());
								buffer.setLength(0);
							}
							continue main;
						}
						buffer.append(c);
						continue main;
					}
					case QUOTED : {
						if (c == '\\') {
							state = CommandState.ESCAPED_QUOTED;
							continue main;
						}
						if (c == '"') {
							state = CommandState.QUOTE;
							continue main;
						} //
						{
							buffer.append(c);
							continue main;
						}
					}
					case QUOTE : {
						if (Character.isWhitespace(c)) {
							argumentList.add(buffer.toString());
							buffer.setLength(0);
							state = CommandState.NORM;
							continue main;
						}
						buffer.append(c);
						state = CommandState.QUOTED;
						continue main;
					}
					case ESCAPED_NORM :
						state = CommandState.NORM;
						buffer.append(c);
						continue main;
					case ESCAPED_QUOTED :
						state = CommandState.QUOTED;
						buffer.append(c);
						continue main;
					default :
				}
			}
			if (state == CommandState.NORM || state == CommandState.QUOTE) {
				if (buffer.length() > 0) {
					if (state == CommandState.QUOTE) {
						argumentList.add(buffer.toString());
					} else {
						Shell.expandPatterns(ctx, argumentList, buffer.toString());
					}
					buffer.setLength(0);
				}
			}
			if (state == CommandState.QUOTED) {
				return ctx.vmRaise("Unterminated quote!");
			}
		}
		return null;
	}

	/** @param command */
	public static final void registerCommand(final AbstractShellCommand command) {

		Shell.COMMANDS.put(command.getName(), command);
	}

	/** default constructor */
	public Shell() {

		//
	}

	@Override
	public Shell clone() {

		return new Shell();
	}

	@Override
	public ExecStateCode execCallImpl(final ExecProcess ctx) {

		final EntryVfsRoot saveRoot = ctx.fldVfsRoot;
		final Entry saveShare = ctx.fldVfsShare;
		final Entry saveFocus = ctx.fldVfsFocus;

		try {
			final Console console = ctx.getConsole();
			ctx.vmScopeDeriveContextFromFV();
			final int shellLevel = Base.getInt(ctx, "$SUBSHELL", -1) + 1;
			boolean x_trace = false;
			boolean verbose = false;
			boolean command = false;
			// ObjectSource<Value<String>> altSource = null;
			Iterator<String> altSource = null;
			{
				final BaseArray arguments = ctx.contextGetArguments();
				final int length = arguments.length();
				arguments : for (int argumentIndex = 1;;) {
					if (argumentIndex >= length) {
						/** no arguments left */
						if (command) {
							console.sendMessage(this.getManual());
							return ExecStateCode.ERROR;
						}
						break arguments;
					}
					if (command) {
						ctx.contextCreateMutableBinding("$VERBOSE", Base.forBoolean(verbose), true);
						ctx.contextCreateMutableBinding("$COMMAND", Text.join(arguments, " "), true);
						ctx.contextCreateMutableBinding("$SUBSHELL", shellLevel, true);
						return Shell.exec(ctx, arguments.baseArraySlice(argumentIndex, length));
					}

					final String first = Base.getString(arguments, argumentIndex, "");

					if (first.startsWith("-")) {
						if (Shell.calculateOptions(first).contains("v")) {
							verbose = true;
						}
						if (Shell.calculateOptions(first).contains("c")) {
							command = true;
						}
						if (Shell.calculateOptions(first).contains("x")) {
							x_trace = true;
						}
						++argumentIndex;
						continue arguments;
					}
					{
						final Entry entry = Storage.getRelative(ctx, first, null);
						if (entry == null || !entry.isExist()) {
							return ctx.vmRaise("Not found: " + first);
						}
						if (entry.isContainer()) {
							return ctx.vmRaise("Is container: " + first);
						}
						altSource = Arrays.asList(entry.getTextContent().baseValue().toString().split("\n")).iterator();
						break arguments;
					}
				}
			}
			main : for (;;) {

				final String commandLine;
				if (altSource != null) {
					if (!altSource.hasNext()) {
						/** end of input, leave shell */
						return ExecStateCode.NEXT;
					}
					commandLine = altSource.next();
				} else {
					final String locationShare = ctx.fldVfsShare.getLocation();
					final String locationFocus = ctx.fldVfsFocus.getLocation();
					final String prompt = "[" + Engine.HOST_NAME + ' ' + (ctx.fldVfsShare == ctx.fldVfsFocus
						? "/"
						: locationFocus.substring(
								locationShare.length() == 1
									? 0
									: locationShare.length()))
							+ "]";
					console.checkUpdateClient();
					console.setStateNormal();
					final Value<String> commandRef = console.readString(prompt, "");
					if (commandRef == null) {
						/** end of input, leave shell */
						return ExecStateCode.NEXT;
					}

					commandLine = commandRef.baseValue();
					if (commandLine == null) {
						/** end of input, leave shell */
						return ExecStateCode.NEXT;
					}
					if (commandLine.length() == 0) {
						/** shell does nothing, repeat */
						// return ExecStateCode.REPEAT;
						continue main;
					}
				}

				try {
					ctx.contextCreateMutableBinding("$VERBOSE", Base.forBoolean(verbose), true);
					ctx.contextCreateMutableBinding("$COMMAND", commandLine, true);
					ctx.contextCreateMutableBinding("$SUBSHELL", shellLevel, true);
					final BaseList<String> argumentList = BaseObject.createArray(8);

					ExecStateCode code;
					code : {
						code = Shell.parseCommandLine(ctx, commandLine, argumentList);
						if (code != null && code != ExecStateCode.NEXT) {
							break code;
						}
						if (argumentList.isEmpty()) {
							/** no actual arguments, repeat */
							continue main;
						}
						if (x_trace) {
							console.log("+ " + FormatSAPI.jsObject(argumentList));
						}

						code = Shell.exec(ctx, argumentList);
					}

					if (ExecStateCode.RETURN == code) {
						return ExecStateCode.NEXT;
					}

					if (ExecStateCode.ERROR == code) {
						final BaseObject result = ctx.vmGetResultImmediate();
						if (result != null && result != BaseObject.UNDEFINED) {
							console.sendError(
									result instanceof Throwable
										? verbose
											? Format.Throwable.toText((Throwable) result)
											: ((Throwable) result).getMessage()
										: String.valueOf(result));
							ctx.ra0RB = BaseObject.UNDEFINED;
						}

						/** repeat */
						continue main;
					}

					/** repeat */
					continue main;

				} catch (final Throwable t) {

					console.sendError("ERROR: " + t.getMessage());
					ctx.ra0RB = BaseObject.UNDEFINED;
					Report.exception("SHELL", "While running: " + commandLine, t);

					/** repeat */
					continue main;

				}
			}
		} finally {
			ctx.fldVfsFocus = saveFocus;
			ctx.fldVfsShare = saveShare;
			ctx.fldVfsRoot = saveRoot;
		}
	}

	@Override
	public String getDescription() {

		return "shell";
	}

	@Override
	public String getManual() {

		return "" + "Usage: shell [-vc] [scriptFile]\r\n" + "       shell -c cmdArg0 [... cmdArgX]\r\n" + "Use -v option for verbose logging.\r\n"
				+ "Use -x option for x-trace logging.\r\n" + "Use -c option to execute the remaining arguments as a command.\r\n" + this.getDescription();
	}

	@Override
	public String getName() {

		return "shell";
	}
}
