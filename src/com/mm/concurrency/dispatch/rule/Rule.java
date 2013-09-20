package com.mm.concurrency.dispatch.rule;

import java.util.Map;

import com.mm.concurrency.dispatch.key.Key;

public interface Rule {

	boolean isSatisfiedBy(Key key, Map<String, Object> bindings);
	String getName();
}
