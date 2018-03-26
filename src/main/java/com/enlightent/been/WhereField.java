package com.enlightent.been;

import java.util.List;

public class WhereField {

	private String field;
	
	private String[] fields;
	
	private CompareSymbol symbol;
	
	private String value;
	
	private List<String> values;

	public WhereField(String field, CompareSymbol symbol, String value) {
		super();
		this.field = field;
		this.symbol = symbol;
		this.value = value;
	}
	
	public WhereField(String[] fields, List<String> values) {
		super();
		this.fields = fields;
		this.symbol = CompareSymbol.MULTI_KEY;
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public CompareSymbol getSymbol() {
		return symbol;
	}

	public void setSymbol(CompareSymbol symbol) {
		this.symbol = symbol;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
