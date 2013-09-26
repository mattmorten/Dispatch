package com.mm.concurrency.dispatch.key;

import java.util.Arrays;
import java.util.List;


public class Key {
	protected List<KeyComponent> components;
	
	public Key(List<KeyComponent> components) {
		this.components = components;
	}
	
	public Key(KeyComponent... components) {
		this.components = Arrays.asList(components);
	}
	
	public List<KeyComponent> getComponents() {
		return components;
	}
	
}
