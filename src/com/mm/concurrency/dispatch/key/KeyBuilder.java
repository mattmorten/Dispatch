package com.mm.concurrency.dispatch.key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mm.concurrency.dispatch.rule.Rule;

public class KeyBuilder {
	private List<KeyComponent> components = new ArrayList<KeyComponent>();
	
	public KeyBuilder then(KeyComponent component){
		components.add(component);
		return this;
	}
	
	public KeyBuilder then(KeyComponent... components){
		this.components.add(new CompositeComponent(Arrays.asList(components)));
		return this;
	}
	
	public static <T> KeyComponent type(Class<T> clazz){
		return new TypeComponent<T>(clazz);
	}
	
	public static <T> KeyComponent anyType(){
		return new TypeComponent.Any();
	}
	
	public static <T> KeyComponent allType(){
		return new TypeComponent.All();
	}
	
	public static KeyComponent boundType(String binding){
		return new TypeComponent.Bounded(binding);
	}
	
	public static <T> KeyComponent id(String id){
		return new IdentityComponent(id);
	}
	
	public static <T> KeyComponent anyId(){
		return new IdentityComponent.Any();
	}
	
	public static <T> KeyComponent allId(){
		return new IdentityComponent.All();
	}
	
	public static KeyComponent boundId(String bindingName){
		return new IdentityComponent.Bounded(bindingName);
	}
	
	public Key buildKey(){
		return new Key(components);
	}
	
	public Rule buildRule(String name){
		return new Rule(name, components);
	}
}
