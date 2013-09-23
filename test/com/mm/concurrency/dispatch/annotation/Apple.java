package com.mm.concurrency.dispatch.annotation;

import com.mm.concurrency.dispatch.Attribute;

public class Apple {
	private String name;

	public Apple(String name) {
		this.name = name;
	}
	
	@Attribute("Name")
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Apple [name=" + name + "]"; 
	}
}
