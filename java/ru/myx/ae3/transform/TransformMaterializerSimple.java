package ru.myx.ae3.transform;

/**
 * @author myx
 * @param <T>
 * 
 */
public abstract class TransformMaterializerSimple<T> extends TransformMaterializer<T> {
	final String[]		contentType;
	
	final Class<?>[]	resultClass;
	
	/**
	 * @param contentType
	 * @param resultClass
	 */
	protected TransformMaterializerSimple(final String contentType, final Class<? super T> resultClass) {
		if (resultClass == null) {
			throw new NullPointerException( "Materialization result class cannot be null!" );
		}
		this.contentType = new String[] { contentType };
		this.resultClass = new Class[] { resultClass };
	}
	
	/**
	 * @param contentType
	 * @param resultClass
	 */
	protected TransformMaterializerSimple(final String contentType, final Class<? super T>[] resultClass) {
		if (resultClass == null) {
			throw new NullPointerException( "Materialization result class cannot be null!" );
		}
		this.contentType = new String[] { contentType };
		this.resultClass = resultClass;
	}
	
	@Override
	public final Class<?>[] targets() {
		return this.resultClass;
	}
	
	@Override
	public final String[] variety() {
		return this.contentType;
	}
}
