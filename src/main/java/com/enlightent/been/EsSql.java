package com.enlightent.been;

import com.enlightent.util.Assert;
import com.enlightent.util.EsUtils;

import java.util.*;
import java.util.Map.Entry;

public class EsSql {

	/**
	 * elasticsearch-sql在不指定limit时默认返回200条，最大数量为10000条记录，
	 * 在不分页的情况下，默认返回5000条记录
	 */
	public static final Integer FIVE_THOUSAND_SIZE = 5_000;
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	private List<String> fields;
	
	private String table;
	
	private Map<WhereField, String> where;
	
	private String group;
	
	private String order;
	
	private String limit;

	private Sort sort;

	public void setWhere(Map<WhereField, String> where) {
		this.where = where;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setFields(String... fields) {
		Assert.notNull(fields, "arguments 'fields' must not be null");
		this.fields = Arrays.asList(fields);
	}

	public void setOrder(String field, String order) {
		this.order = field + " " + order;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public void setDefaultLimit(Integer reqPage) {
		this.setLimit((reqPage - 1) * DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE);
	}
	
	public void setLimit(Integer from, Integer size) {
		this.limit = from + ", " + size;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSql() {
		return this.getSql(null);
	}
	
	public String getSql(String replacement) {
		StringBuilder sBuilder = new StringBuilder(150);
		sBuilder.append("select ");
		for (int i = 0, size = fields.size(); i < size; i++) {
			sBuilder.append(fields.get(i));
			if (i != (size - 1)) {
				sBuilder.append(", ");
			}
		}
		sBuilder.append(" from ").append(table);
		if (where != null) {
			StringBuilder temp = new StringBuilder();
			sBuilder.append(" where ");
			Set<Entry<WhereField,String>> entrySet = where.entrySet();
			for (Entry<WhereField, String> entry : entrySet) {
				WhereField whereField = entry.getKey();
				
				CompareSymbol symbol = whereField.getSymbol();
				if (symbol.equals(CompareSymbol.MULTI_KEY)) {
					String keyword = EsUtils.buildWhere(whereField.getFields(), whereField.getValues());
					temp.append(keyword);
				} else {
					temp.append(whereField.getField());
					switch (symbol) {
						case EQ:
							temp.append(" = ");
							break;
						case GT:
							temp.append(" > ");
							break;
						case LT:
							temp.append(" < ");
							break;
						case GE:
							temp.append(" >= ");
							break;
						case LE:
							temp.append(" <= ");
							break;
						case NOT_EQ:
							temp.append(" <> ");
							break;
						default:
							break;
					}
					temp.append("\"").append(whereField.getValue()).append("\"");
				}
				
				String value = entry.getValue();
				if (value != null) {
					temp.append(" ").append(value).append(" ");
				}
				
			}
			String tempStr = temp.toString();
			//如果where条件以and结尾，则去除
			String replaceLastAnd = tempStr.replaceAll("and\\s$", "");
			
			sBuilder.append(replaceLastAnd);
		}
		
		if (group != null) {
			sBuilder.append(" group by ").append(group).append(" ");
		}

		if (order != null) {
			sBuilder.append(" order by ").append(order).append(" ");
		} else if (sort != null) {
			sBuilder.append(" order by ").append(sort).append(" ");
		}
		
		if (limit != null) {
			sBuilder.append(" limit ").append(limit).append(" ");
		}
		String sql = sBuilder.toString();
		if (replacement != null) {
			sql = sql.replace("1 = \"1\"", replacement);
		}
		return sql;
	}
	
	
}
