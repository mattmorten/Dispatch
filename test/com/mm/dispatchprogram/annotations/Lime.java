package com.mm.dispatchprogram.annotations;

import com.mm.concurrency.dispatch.Attribute;

public class Lime {

	private String name;

	public Lime(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Lime [name=" + name + "]";
	}
	
	@Attribute("Name")
	public String getName() {
		return name;
	}
}
