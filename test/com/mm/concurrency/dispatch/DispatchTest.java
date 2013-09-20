package com.mm.concurrency.dispatch;

import static com.mm.concurrency.dispatch.key.KeyBuilder.anyId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.boundId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.id;
import static com.mm.concurrency.dispatch.key.KeyBuilder.type;

import org.junit.Test;

import com.mm.concurrency.dispatch.key.KeyBuilder;
import com.mm.concurrency.dispatch.receiver.Receiver;
import com.mm.concurrency.dispatch.receiver.ReceiverFactory;
import com.mm.concurrency.dispatch.rule.Rule;
import com.mm.concurrency.dispatch.rule.RuleSet;

public class DispatchTest {

	@Test
	public void testSingleProcess() throws Exception {
		
		Dispatcher d = new Dispatcher();
		
		// Some rules
		Rule rule1 = new KeyBuilder()
			.then(type(A.class), id("A1"))
			.then(type(B.class), anyId())
			.then(type(C.class), boundId("X"))
			.buildRule("AppleRule"); 
		
		Rule rule2 = new KeyBuilder()
			.then(type(A.class), id("A2"))
			.then(type(B.class), anyId())
			.then(type(C.class), boundId("X"))
			.buildRule("OrangeRule");
		
		RuleSet ruleSet = new RuleSet("ApplesAndOranges",rule1,rule2);
		
		d.registerPool(ruleSet, new ReceiverFactory(){
			public Receiver createReceiver() {
				return new TestReceiver();
			}
		}, false,null);
		
		d.dataReceived(new KeyBuilder()
							.then(type(A.class), id("A1"))
							.then(type(B.class), id("ABC"))
							.then(type(C.class), id("123"))
							.buildKey(), new Apple("Apple123"));
		
		d.dataReceived(new KeyBuilder()
							.then(type(A.class), id("A1"))
							.then(type(B.class), id("ABC"))
							.then(type(C.class), id("456"))
							.buildKey(), new Apple("Apple456"));
		
		d.dataReceived(new KeyBuilder()
							.then(type(A.class), id("A2"))
							.then(type(B.class), id("XYZ"))
							.then(type(C.class), id("123"))
							.buildKey(), new Orange("Orange123"));
		
		d.dataReceived(new KeyBuilder()
							.then(type(A.class), id("A2"))
							.then(type(B.class), id("XYZ"))
							.then(type(C.class), id("456"))
							.buildKey(), new Orange("Orange456"));
		
		Thread.sleep(300);
		
	}
}
