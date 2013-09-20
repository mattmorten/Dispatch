package com.mm.concurrency.dispatch.key;

import java.util.Map;

public class IdentityComponent implements KeyComponent {
	
	private final String id;

	public IdentityComponent(String id) {
		this.id = id;
	}

	public boolean matches(KeyComponent other, Map<String, Object> bindings) {
		return this.equals(other);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		IdentityComponent other = (IdentityComponent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class All extends IdentityComponent{

		public All() {
			super("+");
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			return other instanceof IdentityComponent.All;
		}
		
	}
	public static class Any extends IdentityComponent{

		public Any() {
			super("*");
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			return other instanceof KeyComponent;
		}
		
	}
	
	public static class Bounded extends IdentityComponent {

		private String bindingName;
		
		public Bounded(String bindingName) {
			super(null);
			this.bindingName = bindingName;
		}
		
		@Override
		public boolean matches(KeyComponent other, Map<String, Object> bindings) {
			if (!(other instanceof IdentityComponent)){
				return false;
			}
			
			IdentityComponent otherIdComponent = (IdentityComponent) other;

			// Get any bindings we might have made already
			String alreadyBounded = (String) bindings.get(bindingName);
			
			// Get the other's value
			String otherId = otherIdComponent.id;
			
			if (alreadyBounded == null){
				bindings.put(bindingName, otherId);
				return true;
			}
			else {
				return alreadyBounded.equals(otherId);
			}
		}

	}
	
}
