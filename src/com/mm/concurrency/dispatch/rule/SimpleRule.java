package com.mm.concurrency.dispatch.rule;

import java.util.List;
import java.util.Map;

import com.mm.concurrency.dispatch.key.Key;
import com.mm.concurrency.dispatch.key.KeyComponent;

public class SimpleRule extends Key implements Rule{

	private String name;

	public SimpleRule(String name, List<KeyComponent> components) {
		super(components, false);
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
	
}
