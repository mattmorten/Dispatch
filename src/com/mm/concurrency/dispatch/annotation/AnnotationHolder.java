package com.mm.concurrency.dispatch.annotation;

import java.util.Map;

import com.mm.concurrency.dispatch.key.KeyComponent;

public class AnnotationHolder implements KeyComponent{

	private Object value;

	public AnnotationHolder(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public boolean matches(KeyComponent other, Map<String, Object> bindings) {
		return false;
	}

}
