Dispatch
========

Some Java concurrency tools

```java
/**
 * Create some rules. The following rules evaluate to true when we receive 
 * a CustomerDetails object and a AddressObject with the same "userId" value.
 */
RuleSet nameAndAddressRules = new RuleSet("NameAndAddressRules",
			new AnnotationKeyBuilder()
				.with(boundField(CustomerDetails.class,"userId","X")).buildRule("NameRule"),
			new AnnotationKeyBuilder()
				.with(boundField(Address.class,"userId","X")).buildRule("AddressRule"));
		
```
```java
/** 
 * Register a receiver, which will be created and run in a Thread Pool whenever we
 * get a hit on the above rule.
 */
Dispatcher dispatcher = new Dispatcher();

dispatcher.registerPool(nameAndAddressRules, 
  new ReceiverFactory() {
	  public Receiver createReceiver() {
		  return new DetailsCombiner(d);
		}
	}, false,null);
				
```
A DetailsCombiner object will be passed the CustomerDetails and the Address objects which it needs to do its work:

```java
public class DetailsCombiner implements Receiver {

	private CustomerDetails customerDetails;
	private Address address;
	
	public void run() {
		System.out.println("I know that: " + customerDetails.getName() + " lives at " + address.toString());
	}

	@Data("NameRule")
	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}
	
	@Data("AddressRule")
	public void setAddress(Address address) {
		this.address = address;
	}
}
```
