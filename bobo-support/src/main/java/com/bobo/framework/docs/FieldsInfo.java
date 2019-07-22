package com.bobo.framework.docs;

import java.lang.reflect.Field;

public class FieldsInfo {
	Field field;

	String fieldNotes;

	public String getFieldNotes() {
		return fieldNotes;
	}

	public void setFieldNotes(String fieldNotes) {
		this.fieldNotes = fieldNotes;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
