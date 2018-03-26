package com.enlightent.been;

import java.util.List;

public class Paging<T> {
	private List<T> rows;
	private Long total;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Paging [rows=" + rows + ", total=" + total + "]";
	}

}
