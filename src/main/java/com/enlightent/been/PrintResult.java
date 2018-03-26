package com.enlightent.been;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrintResult {

	private boolean print = false;
	
	private String sql;

	private List<Map> result = new ArrayList<>();
	
	private Set<String> excepts = new HashSet<>();
	
	private boolean printSql = true;
	
	private boolean printParam;
	
	private boolean printValue;
	
	private boolean printDetailUrl = true;
	
	private int total;
	
	private int previous = 0;

	public int getPrevious() {
		return previous;
	}

	public void setPrevious(int previous) {
		this.previous = previous;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isPrintDetailUrl() {
		return printDetailUrl;
	}

	public void setPrintDetailUrl(boolean printDetailUrl) {
		this.printDetailUrl = printDetailUrl;
	}

	public Set<String> getExcepts() {
		return excepts;
	}

	public void setExcepts(Set<String> excepts) {
		this.excepts = excepts;
	}

	public boolean isPrintSql() {
		return printSql;
	}

	public void setPrintSql(boolean printSql) {
		this.printSql = printSql;
	}

	public boolean isPrintParam() {
		return printParam;
	}

	public void setPrintParam(boolean printParam) {
		this.printParam = printParam;
	}

	public boolean isPrintValue() {
		return printValue;
	}

	public void setPrintValue(boolean printValue) {
		this.printValue = printValue;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public List<Map> getResult() {
		return result;
	}

	public void setResult(List<Map> result) {
		this.result = result;
	}
	
}
