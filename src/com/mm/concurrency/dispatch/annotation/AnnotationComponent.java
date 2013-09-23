package com.mm.concurrency.dispatch.annotation;

import java.lang.reflect.Method;
import java.util.Map;

import com.mm.concurrency.dispatch.Attribute;
import com.mm.concurrency.dispatch.Data;
import com.mm.concurrency.dispatch.key.KeyComponent;
import com.mm.concurrency.dispatch.key.TypeComponent;

public class AnnotationComponent implements KeyComponent {
	
	protected final Class<?> type;
	protected final String field;
	protected final Object value;
	protected final Method method;

	public AnnotationComponent(Class<?> type, String field, Object value) {
		this.type = type;
		this.field = field;
		this.value = value;
		
		// Ensure the type has the component
		method = (field == null) ? null : getMethod(type, field);
	}

	public boolean matches(KeyComponent otherComponent, Map<String, Object> bindings) {
		
		if (!(otherComponent instanceof AnnotationHolder)){
			return false;
		}
		
		AnnotationHolder otherHolder = (AnnotationHolder) otherComponent;
		Object other = otherHolder.getValue();
		
		if (!other.getClass().equals(type)){
			return false;
		}
		
		Object value = getAnnotationValue(other, method);
		
		return (this.value == null && value == null) || this.value.equals(value);
	}
	
	protected Method getMethod(Class<?> type, String field){
		Method[] methods = type.getMethods();
		for (Method method : methods) {
		    Attribute annos = method.getAnnotation(Attribute.class);
		    if (annos != null) {
		    	String fieldName = annos.value();
		    	
		    	// This is the field we are looking for
		    	if (fieldName.equals(field)){
			        return method;
		    	}
		 
		    }
		}
		
		throw new IllegalArgumentException("No field [" + field + "] on target class [" + type + "]");
	}
	
	protected Object getAnnotationValue(Object other, Method method){
		
        try {
            return method.invoke(other);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		AnnotationComponent other = (AnnotationComponent) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}



	public static class All extends AnnotationComponent{

		public All(Class<?> type) {
			super(type, null, "+");
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			return other instanceof AnnotationComponent.All;
		}
				
	}
	public static class Any extends AnnotationComponent{

		public Any(Class<?> type) {
			super(type, null, "*");
		}
		
		@Override
		public boolean matches(KeyComponent otherComponent, Map<String, Object> bindings) {
			
			if (!(otherComponent instanceof AnnotationHolder)){
				return false;
			}
			
			AnnotationHolder otherHolder = (AnnotationHolder) otherComponent;
			Object other = otherHolder.getValue();
			
			if (!other.getClass().equals(type)){
				return false;
			}
			
			return true;
		}
		
	}
	
	public static class Bounded extends AnnotationComponent {
		
		private String bindingName;
		
		public Bounded(Class<?> type, String field,String bindingName) {
			super(type, field, "*");
			this.bindingName = bindingName;
		}
		
		@Override
		public boolean matches(KeyComponent otherComponent, Map<String, Object> bindings) {
			
			if (!(otherComponent instanceof AnnotationHolder)){
				return false;
			}
			
			AnnotationHolder otherHolder = (AnnotationHolder) otherComponent;
			Object other = otherHolder.getValue();
			
			
			if (!other.getClass().equals(type)){
				return false;
			}
			
			Object otherValue = getAnnotationValue(other, method);
			
			// Get any bindings we might have made already
			String alreadyBounded = (String) bindings.get(bindingName);
			
			if (alreadyBounded == null){
				bindings.put(bindingName, otherValue);
				return true;
			}
			else {
				return alreadyBounded.equals(otherValue);
			}
		}
	}
}
