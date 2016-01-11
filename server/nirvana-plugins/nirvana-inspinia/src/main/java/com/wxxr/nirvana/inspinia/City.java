package com.wxxr.nirvana.inspinia;

public class City {
	private String name;
	private String password;
	private String key;
	
	
	public City(String name, String password, String key) {
		super();
		this.name = name;
		this.password = password;
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
