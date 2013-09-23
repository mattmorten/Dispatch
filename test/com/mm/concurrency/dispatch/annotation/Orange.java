package com.mm.concurrency.dispatch.annotation;

import com.mm.concurrency.dispatch.Attribute;

public class Orange {
	private String name;

	public Orange(String name) {
		this.name = name;
	}
	
	@Attribute("name")
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Orange [name=" + name + "]";
	}
	
}
