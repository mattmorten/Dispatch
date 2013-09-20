package com.mm.concurrency.dispatch.rule;

public class RuleData<T> {
	private Rule rule;
	private T data;
	
	public RuleData(Rule rule) {
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
