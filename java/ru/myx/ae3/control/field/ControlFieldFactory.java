package ru.myx.ae3.control.field;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.produce.Produce;
import ru.myx.ae3.report.Report;

/**
 * @author myx
 * 
 */
public final class ControlFieldFactory {
	
	private static final Map<String, ControlField>	creatableFields	= Create.treeMap();
	
	/**
	 * @param type
	 * @param attributes
	 * @return field
	 */
	public static final ControlField createField(final String type, final BaseObject attributes) {
		Object result = Produce.object( ControlField.class, type != null && type.length() > 0
				? type
				: "object", attributes, null );
		if (result == null) {
			Report.warning( "AE1/FIELD_MANAGER", "Cannot find appropriate field class, '"
					+ type
					+ "' specified, repacing with 'string'" );
			result = Produce.object( ControlField.class, "string", attributes, null );
		}
		return (ControlField) result;
	}
	
	/**
	 * @param id
	 * @param title
	 * @param lengthLimit
	 * @return field
	 */
	public static final ControlField createFieldBinary(
			final String id,
			final Object title,
			final int lengthLimit) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "max", lengthLimit );
		return ControlFieldFactory.createField( "binary", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldBoolean(
			final String id,
			final Object title,
			final boolean defaultValue) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", defaultValue );
		return ControlFieldFactory.createField( "boolean", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldDate(
			final String id,
			final Object title,
			final Date defaultValue) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", Base.forDate( defaultValue ) );
		return ControlFieldFactory.createField( "date", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldDate(
			final String id,
			final Object title,
			final long defaultValue) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", Base.forDateMillis( defaultValue ) );
		return ControlFieldFactory.createField( "date", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldFloating(
			final String id,
			final Object title,
			final double defaultValue) {
		return ControlFieldFactory.createFieldFloating( id,
				title,
				defaultValue,
				Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @param min
	 * @param max
	 * @return field
	 */
	public static final ControlField createFieldFloating(
			final String id,
			final Object title,
			final double defaultValue,
			final double min,
			final double max) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", defaultValue )//
				.putAppend( "min", min )//
				.putAppend( "max", max );
		return ControlFieldFactory.createField( "floating", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @return field
	 */
	public static final ControlField createFieldGuid(final String id, final Object title) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "type", "string" );
		return ControlFieldFactory.createField( "guid", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldInteger(
			final String id,
			final Object title,
			final int defaultValue) {
		return ControlFieldFactory.createFieldInteger( id, title, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @param min
	 * @param max
	 * @return field
	 */
	public static final ControlField createFieldInteger(
			final String id,
			final Object title,
			final int defaultValue,
			final int min,
			final int max) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", defaultValue )//
				.putAppend( "min", min )//
				.putAppend( "max", max );
		return ControlFieldFactory.createField( "integer", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldLong(
			final String id,
			final Object title,
			final long defaultValue) {
		return ControlFieldFactory.createFieldLong( id, title, defaultValue, Long.MIN_VALUE, Long.MAX_VALUE );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @param min
	 * @param max
	 * @return field
	 */
	public static final ControlField createFieldLong(
			final String id,
			final Object title,
			final long defaultValue,
			final long min,
			final long max) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", defaultValue )//
				.putAppend( "min", min )//
				.putAppend( "max", max );
		return ControlFieldFactory.createField( "long", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldMap(
			final String id,
			final Object title,
			final BaseObject defaultValue) {
		final BaseNativeObject attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) );
		if (defaultValue != null && defaultValue != BaseObject.UNDEFINED) {
			attributes.putAppend( "default", defaultValue );
		}
		return ControlFieldFactory.createField( "map", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldObject(
			final String id,
			final Object title,
			final Object defaultValue) {
		final BaseNativeObject attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) );
		if (defaultValue != null && defaultValue != BaseObject.UNDEFINED) {
			attributes.putAppend( "default", Base.forUnknown( defaultValue ) );
		}
		return ControlFieldFactory.createField( "object", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @return field
	 */
	public static final ControlField createFieldOwner(final String id, final Object title) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "constant", BaseObject.TRUE )//
				.putAppend( "type", "string" );
		return ControlFieldFactory.createField( "owner", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldSet(
			final String id,
			final Object title,
			final Set<?> defaultValue) {
		final BaseNativeObject attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) );
		if (defaultValue != null) {
			attributes.putAppend( "default", Base.forUnknown( defaultValue ) );
		}
		return ControlFieldFactory.createField( "set", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldString(
			final String id,
			final Object title,
			final Object defaultValue) {
		return ControlFieldFactory.createFieldString( id, title, defaultValue, 0, 65536 );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @param min
	 * @param max
	 * @return field
	 */
	public static final ControlField createFieldString(
			final String id,
			final Object title,
			final Object defaultValue,
			final int min,
			final int max) {
		final BaseNativeObject attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "min", min )//
				.putAppend( "max", max );
		if (defaultValue != null && defaultValue != BaseObject.UNDEFINED) {
			attributes.putAppend( "default", Base.forUnknown( defaultValue ) );
		}
		return ControlFieldFactory.createField( "string", attributes );
	}
	
	/**
	 * @param id
	 * @param title
	 * @param defaultValue
	 * @return field
	 */
	public static final ControlField createFieldTemplate(
			final String id,
			final Object title,
			final String defaultValue) {
		final BaseMap attributes = new BaseNativeObject()//
				.putAppend( "id", id )//
				.putAppend( "title", Base.forUnknown( title ) )//
				.putAppend( "default", defaultValue );
		return ControlFieldFactory.createField( "template", attributes );
	}
	
	/**
	 * @return map
	 */
	public static final Map<String, ControlField> registeredFields() {
		return Create.tempMap( ControlFieldFactory.creatableFields );
	}
	
	/**
	 * Produce factory will be automatically registered for given field.
	 * 
	 * @param factoryIdentity
	 * @param field
	 */
	public static final void registerFieldClass(final String factoryIdentity, final ControlField field) {
		Produce.registerFactory( new FactoryForField( factoryIdentity, field ) );
		if (factoryIdentity != null && factoryIdentity.equals( field.getFieldClass() )) {
			ControlFieldFactory.creatableFields.put( factoryIdentity, field );
		}
	}
	
	/**
	 * Produce factory will be automatically registered for given field.
	 * 
	 * @param factoryIdentity
	 * @param field
	 * @param fields
	 */
	public static final void registerFieldClass(
			final String factoryIdentity,
			final ControlField field,
			final Map<String, ControlField> fields) {
		Produce.registerFactory( new FactoryForField( factoryIdentity, field ) );
		if (factoryIdentity != null && factoryIdentity.equals( field.getFieldClass() )) {
			fields.put( factoryIdentity, field );
		}
	}
	
	private ControlFieldFactory() {
		// ignore
	}
}
