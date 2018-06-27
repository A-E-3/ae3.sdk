package ru.myx.ae3.control.fieldset;

import java.io.Serializable;
import java.util.List;

import ru.myx.ae3.control.field.ControlField;

/*
 * Created on 26.04.2004
 */

final class FieldsetDefault extends AbstractFieldset<FieldsetDefault> implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8645405570273130423L;
	
	/**
	 * 
	 */
	FieldsetDefault() {
		// empty
	}
	
	FieldsetDefault(final ControlFieldset<?> example, final List<ControlField> list) {
		super( example, list );
	}
	
	/**
	 * @param id
	 */
	FieldsetDefault(final String id) {
		this.setAttributeIntern( "id", id );
		this.recalculate();
	}
}
