package ru.myx.ae3.console.tty;

import java.util.function.Function;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.console.Console;
import ru.myx.ae3.console.shell.Shell;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.help.Format;

final class TtySession implements Function<ExecProcess, Void> {
	
	static final TtySession INSTANCE = new TtySession();
	
	private TtySession() {
		
		//
	}
	
	@Override
	public Void apply(final ExecProcess ctx) {
		
		final Console console = ctx.getConsole();
		console.sendMessage("# tty session, console class: " + console.getClass().getName());
		console.flush();
		try {
			
			for (int left = 3;;) {
				final Value<String> login = console.readString("login", null);
				final Value<String> password = console.readPassword("password");
				
				final String loginString = login.baseValue();
				if (loginString == null || loginString.length() == 0) {
					continue;
				}
				
				final String passwordString = password.baseValue();
				if (passwordString == null) {
					console.sendMessage("can't read your password!");
					console.close();
					return null;
				}
				
				final BaseList<Object> constants = BaseObject.createArray(4);
				constants.add(Base.forString("ru.acmcms.internal/util/AuthAcmAnyControllerJail"));
				constants.add(Base.forString(loginString));
				constants.add(Base.forString(passwordString));
				constants.add(Base.forString("admin"));
				
				final boolean isAdmin = Evaluate.evaluateBoolean(
						"(function check(){" //
								+ "var A, a, u;" //
								+ "try{" //
								+ "	A = require( @0 );" //
								+ "	a = new A();"//
								+ "}catch(e){" //
								+ "	console.log('>>>>> console.tty: TtySession auth failed check skipped, error: %s', e.message || e);" //
								+ "	return false;" //
								+ "}" //
								+ "u = a.checkLoginPassword( @1, @2 ); " //
								+ "return a.checkMembership( u, @3 ); //"
								// + "return a.checkAdmin( @1, @2 );"
								+ "})()",
						ctx,
						constants);
				if (isAdmin) {
					console.sendMessage("# hi: " + loginString);
					break;
				}
				console.sendMessage("user is unknown or permission is denied.");
				console.flush();
				if (--left <= 0) {
					console.sendMessage("too many attempts, bye!");
					console.close();
					return null;
				}
			}
			ctx.fldVfsFocus = ctx.fldVfsShare = ctx.fldVfsRoot.toContainer();
			
			try {
				Shell.execNative(ctx, Base.forArray(new String[]{
						"shell"
				}));
			} catch (final Throwable t) {
				console.setStateError();
				console.sendMessage("FATAL", Format.Throwable.toText("unhandled tty session exception", t));
				console.setStateNormal();
				console.sendMessage("bye.");
			}
		} catch (final Throwable t) {
			console.setStateError();
			console.sendMessage("FATAL", Format.Throwable.toText("unhandled tty session exception", t));
			console.setStateNormal();
			console.sendMessage("bye.");
		}
		console.close();
		return null;
	}
	
	@Override
	public String toString() {
		
		return "TTY_SESSION";
	}
}
