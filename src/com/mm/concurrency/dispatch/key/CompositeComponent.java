package com.mm.concurrency.dispatch.key;

import java.util.List;
import java.util.Map;

public class CompositeComponent implements KeyComponent {

	private List<KeyComponent> components;
	
	public CompositeComponent(List<KeyComponent> components) {
		this.components = components;
	}

	public boolean matches(KeyComponent other, Map<String, Object> bindings) {
		
		List<KeyComponent> keyComponents = ((CompositeComponent)other).components;
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
	

}
