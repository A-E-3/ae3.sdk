/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.fieldset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.control.AbstractBasic;
import ru.myx.ae3.control.field.ControlField;
import ru.myx.ae3.control.field.ControlFieldFactory;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/**
 * FIXME ERROR will use base object's properties for listings but actually uses
 * own list and map for storage!
 *
 * @author myx
 * @param <T>
 *
 */
public abstract class AbstractFieldset<T extends AbstractFieldset<?>> extends AbstractBasic<T> implements ControlFieldset<T> {
	
	
	private static ControlField baseToField(final BaseObject value) {
		
		
		if (value instanceof ControlField) {
			return (ControlField) value;
		}
		if (value.baseValue() instanceof ControlField) {
			return (ControlField) value.baseValue();
		}
		return ControlFieldFactory.createField(Base.getString(value, "class", "type", "object"), value);
	}
	
	private Map<String, Number> fieldsMap = null;
	
	private Set<String> innerFields = null;
	
	private String[] fieldNames = null;
	
	private final List<ControlField> fieldsList = new ArrayList<>();
	
	/**
	 *
	 */
	protected AbstractFieldset() {
		
		//
	}
	
	@Override
	public int nameCount() {
		
		
		return this.size();
	}
	
	@Override
	public int nameIndex(final String name) {
		
		
		if (this.fieldsMap == null) {
			synchronized (this) {
				if (this.fieldsMap == null) {
					this.fieldsMap = new TreeMap<>();
					for (int i = this.fieldsList.size() - 1; i >= 0; --i) {
						final ControlField field = (ControlField) this.baseGet(i, null);
						this.fieldsMap.put(field.getKey(), Base.forInteger(i));
					}
				}
			}
		}
		final Number n = this.fieldsMap.get(name);
		return n == null
			? -1
			: n.intValue();
	}
	
	@Override
	public int nameIndex(final int nameIndex) {
		
		
		return nameIndex;
	}
	
	@Override
	public String[] names() {
		
		
		{
			final String[] names = this.fieldNames;
			if (names != null) {
				return names;
			}
		}
		synchronized (this) {
			{
				final String[] names = this.fieldNames;
				if (names != null) {
					return names;
				}
			}
			{
				final String[] result = new String[this.fieldsList.size()];
				for (int i = result.length - 1; i >= 0; --i) {
					result[i] = this.fieldsList.get(i).getKey();
				}
				return this.fieldNames = result;
			}
		}
	}
	
	AbstractFieldset(final ControlFieldset<?> example, final List<ControlField> list) {
		
		this.baseDefineImportAllEnumerable(example);
		if (example.hasAttributes()) {
			this.setAttributes(example.getAttributes());
		}
		this.fieldsList.addAll(list);
	}
	
	@Override
	public boolean add(final ControlField value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldNames = null;
		return this.fieldsList.add(value);
	}
	
	@Override
	public void add(final int index, final ControlField element) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldNames = null;
		this.fieldsList.add(index, element);
	}
	
	@Override
	public boolean addAll(final Collection<? extends ControlField> c) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.addAll(c);
	}
	
	@Override
	public boolean addAll(final int index, final Collection<? extends ControlField> c) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldNames = null;
		return this.fieldsList.addAll(index, c);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T addField(final ControlField field) {
		
		
		if (field != null) {
			final String name = field.getKey();
			if (name == null) {
				throw new IllegalArgumentException("Fieldset.addField(): 'id' is unknown!");
			}
			if (this.getField(name) != null) {
				throw new IllegalArgumentException("Fieldset.addField(): '" + name + "' field is already registered for this definition!");
			}
			this.fieldsList.add(field);
			this.fieldsMap = null;
			this.innerFields = null;
			this.fieldNames = null;
		}
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T addFields(final ControlField[] fields) {
		
		
		if (fields != null) {
			for (final ControlField field : fields) {
				if (field == null) {
					continue;
				}
				final String name = field.getKey();
				if (name == null) {
					throw new IllegalArgumentException("Fieldset.addFields(): 'id' is unknown!");
				}
				if (this.getField(name) != null) {
					throw new IllegalArgumentException("Fieldset.addFields(): '" + name + "' field is already registered for this definition!");
				}
				this.fieldsList.add(field);
			}
			this.fieldsMap = null;
			this.innerFields = null;
			this.fieldNames = null;
		}
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T addFields(final ControlFieldset<?> fieldset) {
		
		
		final int length = fieldset.size();
		for (int i = 0; i < length; ++i) {
			this.addField(fieldset.get(i));
		}
		return (T) this;
	}
	
	@Override
	public void baseClear() {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.clear();
		super.baseClear();
	}
	
	@Override
	public boolean baseContains(final BaseObject value) {
		
		
		if (value instanceof ControlField) {
			return value == this.getField(((ControlField) value).getKey());
		}
		return false;
	}
	
	@Override
	public BaseObject baseDefaultPop() {
		
		
		final int length = this.fieldsList.size();
		if (length == 0) {
			return BaseObject.UNDEFINED;
		}
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.remove(length - 1);
	}
	
	@Override
	public int baseDefaultPush(final BaseObject value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.add(AbstractFieldset.baseToField(value));
		return this.length();
	}
	
	@Override
	public int baseInsert(final int index, final BaseObject value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.add(index, AbstractFieldset.baseToField(value));
		return this.length();
	}
	@Override
	public AbstractFieldset<T> baseDefaultReverse() {
		
		
		Collections.reverse(this.fieldsList);
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this;
	}
	
	@Override
	public BaseObject baseDefaultShift() {
		
		
		final int length = this.fieldsList.size();
		if (length == 0) {
			return BaseObject.UNDEFINED;
		}
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.remove(0);
	}
	
	@Override
	public int baseDefaultUnshift(final BaseObject value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.add(0, AbstractFieldset.baseToField(value));
		return this.length();
	}
	
	@Override
	public int baseDefaultUnshift(final BaseObject[] values) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		int index = 0;
		for (final BaseObject value : values) {
			this.fieldsList.add(index++, AbstractFieldset.baseToField(value));
		}
		return this.length();
	}
	
	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {
		
		
		final ControlField result = this.fieldsList.get(index);
		return result == null
			? defaultValue
			: result;
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
	
	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		
		final int length = this.fieldsList.size();
		if (length == 0) {
			return defaultValue;
		}
		final ControlField result = this.fieldsList.get(0);
		return result == null
			? defaultValue
			: result;
	}
	
	@Override
	public BaseObject baseGetLast(final BaseObject defaultValue) {
		
		
		final int length = this.fieldsList.size();
		if (length == 0) {
			return defaultValue;
		}
		final ControlField result = this.fieldsList.get(length - 1);
		return result == null
			? defaultValue
			: result;
	}
	
	@Override
	public Iterator<? extends BaseObject> baseIterator() {
		
		
		return this.iterator();
	}
	
	@Override
	public BaseObject baseRemove(final int index) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.remove(index);
	}
	
	@Override
	public boolean baseSet(final int index, final BaseObject value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.set(index, AbstractFieldset.baseToField(value));
		return true;
	}
	
	@Override
	public ControlFieldset<?> baseArraySlice(final int start, final int end) {
		
		
		final int length = this.fieldsList.size();
		final int fromIndex = BaseArray.genericDefaultSliceGetStartIndex(start, length);
		final int toIndex = BaseArray.genericDefaultSliceGetEndIndex(end, length);
		final ControlFieldset<?> fieldset = new FieldsetDefault();
		for (int i = fromIndex; i < toIndex; ++i) {
			fieldset.baseDefaultPush(this.fieldsList.get(i));
		}
		return fieldset;
	}
	
	@Override
	public void clear() {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		this.fieldsList.clear();
	}
	
	@Override
	public boolean contains(final Object o) {
		
		
		return this.fieldsList.contains(o);
	}
	
	@Override
	public boolean containsAll(final Collection<?> c) {
		
		
		return this.fieldsList.containsAll(c);
	}
	
	@Override
	public void dataRetrieve(final BaseObject source, final BaseObject target) {
		
		
		final int length = this.size();
		for (int i = 0; i < length; ++i) {
			final ControlField field = (ControlField) this.baseGet(i, null);
			final String key = field.getKey();
			final BaseObject argument = source == null
				? BaseObject.UNDEFINED
				: source.baseGet(key, BaseObject.UNDEFINED);
			assert argument != null : "NULL java value, " + (source == null
				? "UNDEFINED is java null, totally fucked!"
				: "class=" + source.getClass().getName() + ", key=" + key);
			final BaseObject retrieved = field.dataRetrieve(argument, source);
			assert retrieved != null : "NULL java value, fieldClass=" + field.getClass() + ", field=" + field;
			target.baseDefine(key, retrieved);
		}
	}
	
	@Override
	public void dataStore(final BaseObject source, final BaseObject target) {
		
		
		final int length = this.size();
		for (int i = 0; i < length; ++i) {
			final ControlField field = (ControlField) this.baseGet(i, null);
			if (field != null && !field.isConstant()) {
				final String key = field.getKey();
				target.baseDefine(key, field.dataStore(source == null
					? BaseObject.UNDEFINED
					: source.baseGet(key, BaseObject.UNDEFINED), source) //
				);
			}
		}
	}
	
	@Override
	public Map<String, String> dataValidate(final BaseObject source) {
		
		
		Map<String, String> result = null;
		for (final ControlField current : this.fieldsList) {
			final String error = current.dataValidate(source);
			if (error != null) {
				if (result == null) {
					result = new TreeMap<>();
				}
				result.put(current.getKey(), error);
			}
		}
		return result;
	}
	
	@Override
	public ControlField get(final int index) {
		
		
		return this.fieldsList.get(index);
	}
	
	@Override
	public BaseObject getData() {
		
		
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public ControlField getField(final String name) {
		
		
		if (this.fieldsMap == null) {
			synchronized (this) {
				if (this.fieldsMap == null) {
					this.fieldsMap = new TreeMap<>();
					for (int i = this.fieldsList.size() - 1; i >= 0; --i) {
						final ControlField field = (ControlField) this.baseGet(i, null);
						this.fieldsMap.put(field.getKey(), Base.forInteger(i));
					}
				}
			}
		}
		final Number n = this.fieldsMap.get(name);
		return n == null
			? null
			: this.fieldsList.get(n.intValue());
	}
	
	@Override
	public int indexOf(final Object o) {
		
		
		return this.fieldsList.indexOf(o);
	}
	
	@Override
	public Set<String> innerFields() {
		
		
		final Set<String> check1 = this.innerFields;
		if (check1 != null) {
			return check1;
		}
		synchronized (this) {
			final Set<String> check2 = this.innerFields;
			if (check2 != null) {
				return check2;
			}
			final Set<String> result = new TreeSet<>();
			for (int i = this.fieldsList.size() - 1; i >= 0; --i) {
				((ControlField) this.baseGet(i, null)).fillFields(result);
			}
			return this.innerFields = result;
		}
	}
	
	@Override
	public boolean isEmpty() {
		
		
		return this.fieldsList.isEmpty();
	}
	
	@Override
	public Iterator<ControlField> iterator() {
		
		
		// FIXME: wrap iterator - can be used for 'remove' - should discard
		// cached
		return this.fieldsList.iterator();
	}
	
	@Override
	public int lastIndexOf(final Object o) {
		
		
		return this.fieldsList.lastIndexOf(o);
	}
	
	@Override
	public int length() {
		
		
		return this.fieldsList.size();
	}
	
	@Override
	public ListIterator<ControlField> listIterator() {
		
		
		// FIXME: wrap iterator - can be used for 'remove' - should discard
		// cached
		return this.fieldsList.listIterator();
	}
	
	@Override
	public ListIterator<ControlField> listIterator(final int index) {
		
		
		// FIXME: wrap iterator - can be used for 'remove' - should discard
		// cached
		return this.fieldsList.listIterator(index);
	}
	
	/**
	 * @throws IllegalArgumentException
	 */
	@Override
	protected void recalculate() throws IllegalArgumentException {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		super.recalculate();
	}
	
	@Override
	public ControlField remove(final int index) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.remove(index);
	}
	
	@Override
	public boolean remove(final Object o) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.remove(o);
	}
	
	@Override
	public boolean removeAll(final Collection<?> c) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.removeAll(c);
	}
	
	@Override
	public boolean retainAll(final Collection<?> c) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.retainAll(c);
	}
	
	@Override
	public ControlField set(final int index, final ControlField value) {
		
		
		this.fieldsMap = null;
		this.innerFields = null;
		this.fieldNames = null;
		return this.fieldsList.set(index, value);
	}
	
	@Override
	public int size() {
		
		
		return this.fieldsList.size();
	}
	
	@Override
	public List<ControlField> subList(final int fromIndex, final int toIndex) {
		
		
		return new FieldsetDefault(this, this.fieldsList.subList(fromIndex, toIndex));
	}
	
	@Override
	public Object[] toArray() {
		
		
		return this.fieldsList.toArray();
	}
	
	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(final T[] a) {
		
		
		return this.fieldsList.toArray(a);
	}
}
