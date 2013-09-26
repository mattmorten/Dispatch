package com.mm.concurrency.dispatch.rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mm.concurrency.dispatch.key.Key;
import com.mm.concurrency.dispatch.key.KeyComponent;

public class Rule extends Key{

	private String name;

	public Rule(String name, List<KeyComponent> components) {
		super(components);
		this.name = name;
	}
	
	public Rule(String name, KeyComponent... components) {
		super(Arrays.asList(components));
		this.name = name;
	}
	
	public boolean isSatisfiedBy(Key key, Map<String, Object> bindings){
		List<KeyComponent> keyComponents = key.getComponents();
		List<KeyComponent> ruleComponents = this.components;
		
		if (keyComponents.size() != ruleComponents.size()){
			return false;
		}
		
		for (int i = 0; i < keyComponents.size(); i++){
			if (!ruleComponents.get(i).matches(keyComponents.get(i), bindings)){
				return false;
			}
		}
				
		return true;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
