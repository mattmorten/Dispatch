package com.mm.concurrency.dispatch.key;

import java.util.List;


public class Key {
	protected List<KeyComponent> components;
	private boolean poisonous;

	
	public Key(List<KeyComponent> components, boolean poisonous) {
		this.components = components;
		this.poisonous = poisonous;
	}
	
	public List<KeyComponent> getComponents() {
		return components;
	}
	
	public boolean isPoisonous() {
		return poisonous;
	}
}
