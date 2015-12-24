package com.wxxr.nirvana.inspinia;

import java.util.ArrayList;
import java.util.List;

public class Row {
	private List<Column> columns = new ArrayList<Column>();
	
	
	public List<Column> getColumns() {
		return columns;
	}


	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}


	public void addColumn(Column c){
		if(c != null){
			columns.add(c);
		}
	}
	
}
