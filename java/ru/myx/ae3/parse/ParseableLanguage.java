package ru.myx.ae3.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ExecProcess;

public interface ParseableLanguage {
	
	InputParser createParser();
	
	BaseObject evaluate(ExecProcess ctx);
	
	BaseObject tokenize(ExecProcess ctx);
	
	BaseObject optimize(ExecProcess ctx);
	
	BaseObject compile(ExecProcess ctx);
}
