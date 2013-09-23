package com.mm.concurrency.dispatch.annotation;

import java.util.Collections;

import com.mm.concurrency.dispatch.key.Key;
import com.mm.concurrency.dispatch.key.KeyComponent;
import com.mm.concurrency.dispatch.key.TypeComponent;
import com.mm.concurrency.dispatch.rule.SimpleRule;

public class AnnotationKeyBuilder {
	private KeyComponent component;
	
	public AnnotationKeyBuilder with(KeyComponent component){
		this.component = component;
		return this;
	}
	
	public SimpleRule buildRule(String name){
		return new SimpleRule(name, Collections.singletonList(component));
	}
	
	public Key buildKey(){
		return new Key(Collections.singletonList(component), false);
	}
	
	public static <T> KeyComponent field(Class<?> type, String field, Object value){
		return new AnnotationComponent(type, field, value);
	}
	
	public static <T> KeyComponent anyFieldValue(Class<?> type){
		return new AnnotationComponent.Any(type);
	}
	
	public static <T> KeyComponent allType(Class<?> type){
		return new AnnotationComponent.All(type);
	}
		
	public static KeyComponent boundField(Class<?> type, String field, String bindingName){
		return new AnnotationComponent.Bounded(type, field, bindingName);
	}
	
}
