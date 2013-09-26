Dispatch
========

A concurrency library for Java based on data dependencies, rather than process dependencies.

###Overview
Dispatch helps users develop concurrent software which is defined by data dependencies, which helps construct pipelined architecture. For example, "we can start processing C when A and B are available". 

Here are the core concepts:

* __Rules__. These define the dependencies upon which a process can start
* __Dispatcher__. The central coordinator. Data is passed to the Dispatcher when it becomes available. The Dispatcher evaluates the rules against this data item, and hands it over to a Receiver if possible.
* __Receiver__. A process which is begins running when data is available. 
 
### Motivation
Threading in Java is complex, and so many people don't bother trying to do it, or do it incorrectly. If we abandon concurrency all together, then we lose out on important potential gains:

* Smaller memory footprint
* Faster runtime

High level design should produce dependencies between data. It is much clearer to express dependencies explicitly using rules, rather than use low-level Java concurrency features.

Once we've defined the data dependencies in the application, couldn't the application itself perform runtime empirical analysis to determin optimal parameters such as threadpool size, batching size, memory size etc?

Someone with a strong understanding of concurrency probably wouldn't need a library like this. But for everyone else, this could be an easy way to get started.


###Rules / Dependencies
Conceptually, the simplest rule might look like this:

```
A[*]
```

This means that when any data of A.class becomes available, start a thread to process it.

More complex:

```
A[*]
B[*]
C
```

In this case, a prcoess will begin when we receive any object of type A, another object of type B, and __all__ data of type C. In real life, type C might be some reference data or a lookup table, and the program might need all of it available.

We can inspect the data we've received, using either Annotations or JavaBean properties:

```
A[name="ABC"]
```

The above rule causes a process to be spawned when a.getName().equals("ABC").

Finally, we can bind variables:

```
A[name=T]
B[name=T]
```

A process will be spawned when we receive an A and a B object with the same name.

###Features
This project is just a toy for now. It has:

* A fluent-builder interface for describing rules
* A Dispatcher object
* Data annotations for injecting objects into Receivers
* Rules can include (Any, All, Field-values, Field variable-binding). We can also have multiple rules.
* Receivers are started in thread pools.

###Wishlist
What I'd like to do:

* A DSL rule language
* More complex rule options
* Dispatchers that aren't locked down to one-thread-at-a-time for processing data
* Different concurrency structures
* Shutdown logic (signalling to other processors when we're completely done)
* Empirical runtime analysis, to vary thread-pool sizes, batch size etc.

###Current State
Again, this project is a toy. Don't use it for anything important.

### Example Code

Here is how we define rules at the moment (this needs to be cleaned up):

```java
/**
 * Create some rules. The following rules evaluate to true when we receive 
 * a CustomerDetails object and a AddressObject with the same "userId" value.
 */
RuleSet nameAndAddressRules = new RuleSet("NameAndAddressRules",
			new Rule("NameRule",boundField(CustomerDetails.class,"userId","X")),
			new Rule("AddressRule",boundField(Address.class,"userId","X")));
		
```

Next, we tie up the rules to a receiver. I.e. when a rule is satisfied, the `ReceiverFactory` creates a new `DetailsCombiner` object. 

```java
/** 
 * Register a receiver, which will be created and run in a Thread Pool whenever we
 * get a hit on the above rule.
 */
Dispatcher dispatcher = new Dispatcher();

dispatcher.registerPool(nameAndAddressRules, DetailsCombiner.class);
				
```
A DetailsCombiner object will be passed the CustomerDetails and the Address objects which it needs to do its work:

```java
public class DetailsCombiner implements Receiver {

	private CustomerDetails customerDetails;
	private Address address;
	
	public void run() {
		System.out.println("I know that: " + customerDetails.getName() + 
			" lives at " + address.toString());
	}

	@Data("NameRule")
	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}
	
	@Data("AddressRule")
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public void setDispatcher(Dispatcher dispatcher){
		// Not required here
	}
}
```

Any process can create data. When it does, we hand it to the Dispatcher:

```java
dispatcher.dataReceived(customerDetails);
```

The Dispatcher will know what to do.
