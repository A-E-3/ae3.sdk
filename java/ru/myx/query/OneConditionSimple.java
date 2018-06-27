package ru.myx.query;

/**
 * @author myx
 * 
 */
public final class OneConditionSimple implements OneCondition {
	private final boolean	exact;
	
	private final String	field;
	
	private final String	operand;
	
	private final String	value;
	
	private final String	comment;
	
	/**
	 * @param exact
	 * @param field
	 * @param operand
	 * @param value
	 */
	public OneConditionSimple(final boolean exact, final String field, final String operand, final String value) {
		this.exact = exact;
		this.field = field;
		this.operand = operand;
		this.value = value;
		this.comment = null;
	}
	
	/**
	 * @param exact
	 * @param field
	 * @param operand
	 * @param value
	 * @param comment
	 */
	public OneConditionSimple(final boolean exact,
			final String field,
			final String operand,
			final String value,
			final String comment) {
		this.exact = exact;
		this.field = field;
		this.operand = operand;
		this.value = value;
		this.comment = comment;
	}
	
	@Override
	public int compareTo(final OneCondition that) {
		final String s1 = (this.isExact()
				? '+'
				: '-') + this.getField() + this.getOperator() + this.getValue();
		final String s2 = (that.isExact()
				? '+'
				: '-') + that.getField() + that.getOperator() + that.getValue();
		return s1.compareTo( s2 );
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof OneCondition)) {
			return false;
		}
		final OneCondition cond = (OneCondition) obj;
		return this.isExact() == cond.isExact()
				&& this.getOperator().equals( cond.getOperator() )
				&& this.getField().equals( cond.getField() )
				&& this.getValue().equals( cond.getValue() );
	}
	
	@Override
	public String getField() {
		return this.field;
	}
	
	@Override
	public String getOperator() {
		return this.operand;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		return (this.isExact()
				? 1
				: 0) ^ this.getField().hashCode() ^ this.getOperator().hashCode() ^ this.getValue().hashCode();
	}
	
	@Override
	public boolean isExact() {
		return this.exact;
	}
	
	@Override
	public String toString() {
		return (this.isExact()
				? '+'
				: '-') + this.getField() + this.getOperator() + this.getValue() + (this.comment == null
				? ""
				: " // " + this.comment);
	}
}
