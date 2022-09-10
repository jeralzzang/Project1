package db;

import lombok.*;


public class SqlCondition {
	private String cond;
    private String value;
 	
	public String getCond() {
		return cond;
	}

	public String getValue() {
		return value;
	}

	public SqlCondition(String cond, String value){
		this.cond = cond;
		this.value = value;
	}
}
