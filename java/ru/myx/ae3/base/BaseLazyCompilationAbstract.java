package ru.myx.ae3.base;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.common.WaitTimeoutException;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/**
 * @author myx
 * @param <S>
 *            source class
 * 			
 */
@ReflectionManual
public abstract class BaseLazyCompilationAbstract<S> implements BaseObjectNoOwnProperties, Value<ProgramPart> {
	
	private final LanguageImpl language;
	
	private final String identity;
	
	private final S source;
	
	private ProgramPart program = null;
	
	/**
	 * 
	 * @param language
	 * @param identity
	 * @param source
	 */
	public BaseLazyCompilationAbstract(final LanguageImpl language, final String identity, final S source) {
		
		this.language = language;
		this.identity = identity;
		this.source = source;
	}
	
	@Override
	public final BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		return this.getSource();
	}
	
	@Override
	public final BasePrimitiveString baseToString() {
		
		return this.getSource();
	}
	
	@Override
	public final ProgramPart baseValue() throws WaitTimeoutException {
		
		{
			final ProgramPart program = this.program;
			if (program != null) {
				return program;
			}
		}
		synchronized (this) {
			if (this.program != null) {
				return this.program;
			}
			final String source = this.getSource().stringValue();
			return this.program = Evaluate.compileProgramSilent(
					this.language, //
					this.identity,
					source);
		}
	}
	
	/**
	 * Script source, as in XmlToMap. Eg: &lt;source class="script"
	 * type="ECMA-262">...&lt;/source>
	 * 
	 * @return
	 */
	@ReflectionExplicit
	public BasePrimitiveString getSource() {
		
		return this.getSourceAsString(this.source);
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	protected abstract String getSourceAsJavaString(final S source);
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	protected abstract BasePrimitiveString getSourceAsString(final S source);
	
	/**
	 * Script language name, as in XmlToMap. Eg: &lt;source class="script"
	 * type="ECMA-262">...&lt;/source>
	 * 
	 * @return
	 */
	@ReflectionExplicit
	public final String getType() {
		
		return this.language.getKey();
	}
	
	@Override
	public final String toString() {
		
		return this.getSource().stringValue();
	}
}
