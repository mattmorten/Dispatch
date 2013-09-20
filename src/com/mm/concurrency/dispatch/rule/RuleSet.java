package com.mm.concurrency.dispatch.rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mm.concurrency.dispatch.key.Key;


public class RuleSet{

	private Set<Rule> rules;
	private String name;
	
	public RuleSet(Set<Rule> rules, String name) {
		this.rules = rules;
		this.name = name;
	}
	
	public RuleSet(String name, Rule... rules) {
		this.rules = new HashSet<Rule>(Arrays.asList(rules));
	}

	public Partial match(Key key, Object data) {
		Iterator<Rule> iter = rules.iterator();
		while(iter.hasNext()){
			Rule rule = iter.next();
			Map<String, Object> bindings = new HashMap<String, Object>();
			if (rule.isSatisfiedBy(key, bindings)){
				Set<Rule> rules = new HashSet<Rule>(this.rules);
				rules.remove(rule);
				return new Partial(rule,rules, data, bindings);
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
}
