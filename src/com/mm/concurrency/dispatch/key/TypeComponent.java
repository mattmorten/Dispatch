package com.mm.concurrency.dispatch.key;

import java.util.Map;

public class TypeComponent<T> implements KeyComponent {

	private final Class<T> type;

	public TypeComponent(Class<T> type) {
		this.type = type;
	}
	
	public boolean matches(KeyComponent other, Map<String, Object> bindings) {
		if (!(other instanceof TypeComponent)){
			return false;
		}
		
		@SuppressWarnings("unchecked")
		TypeComponent<T> otherTypeComponent = (TypeComponent<T>) other;
		
		return type.isAssignableFrom(otherTypeComponent.type);
	}
	
	public static class All extends TypeComponent<Object>{

		public All() {
			super(Object.class);
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			return other instanceof TypeComponent.All;
		}
		
	}	
	public static class Any extends TypeComponent<Object>{

		public Any() {
			super(Object.class);
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			return other instanceof TypeComponent;
		}
		
	}	
	public static class Bounded extends TypeComponent<Object>{

		private String bindingName;
		
		public Bounded(String bindingName) {
			super(Object.class);
			this.bindingName = bindingName;
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			if (!(other instanceof TypeComponent)){
				return false;
			}
			
			TypeComponent<?> otherTypeComponent = (TypeComponent<?>) other;
			
			// Get any bindings we might have made already
			Class<?> alreadyBounded = (Class<?>) bindings.get(bindingName);
			
			// Get the other's value
			Class<?> otherType = otherTypeComponent.type;
			
			if (alreadyBounded == null){
				bindings.put(bindingName, otherType);
				return true;
			}
			else {
				return alreadyBounded.equals(otherType);
			}
		}

		
	}
}
