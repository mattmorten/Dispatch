package com.mm.dispatchprogram;

public class Apple {

	private String name;

	public Apple(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Apple [name=" + name + "]";
	}

	public String getName() {
		return name;
	}
}
