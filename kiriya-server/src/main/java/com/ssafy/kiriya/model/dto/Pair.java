package com.ssafy.kiriya.model.dto;

public class Pair {
	int code;
	String name;
	
	public Pair() {}
	
	public Pair(int code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Pair [code=" + code + ", name=" + name + "]";
	}
	
}
