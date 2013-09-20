package com.mm.concurrency.dispatch.rule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mm.concurrency.dispatch.key.Key;


public class Partial {

	private Map<String, Object> bindings;
	
	private Map<String,Object> matchedRules;
	private Set<Rule> remainingRules;

	public Partial(Rule rule, Set<Rule> remainingRules, Object data, Map<String, Object> bindings){
		this.matchedRules = new HashMap<String,Object>();
		this.remainingRules = remainingRules;
		this.bindings = bindings;
		matchedRules.put(rule.getName(),data);
	}
	
	public boolean tryMatch(Key key, Object data){
		Iterator<Rule> iter = remainingRules.iterator();
		boolean found = false;
		while(iter.hasNext()) {
			Rule rule = iter.next();
			if (rule.isSatisfiedBy(key, bindings)){
				iter.remove();
				matchedRules.put(rule.getName(), data);
				found = true;
			}
		}
		
		return found;
	}
	
	public boolean isComplete(){
		return remainingRules.isEmpty();
	}
	
	
	public Object getData(String ruleName){
		return matchedRules.get(ruleName);
	}
}
