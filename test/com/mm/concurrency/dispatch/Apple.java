package com.mm.concurrency.dispatch;

public class Apple {
	private String name;

	public Apple(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Apple [name=" + name + "]"; 
	}
}
