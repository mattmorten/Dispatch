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
	
	public RuleSet(Set<Rule> rules) {
		this.rules = rules;
	}
	
	public RuleSet(Rule... rules) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
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
		RuleSet other = (RuleSet) obj;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		return true;
	}
}
