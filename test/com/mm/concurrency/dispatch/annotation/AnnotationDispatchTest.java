package com.mm.concurrency.dispatch.annotation;

import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.boundField;

import org.junit.Test;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.receiver.Receiver;
import com.mm.concurrency.dispatch.receiver.ReceiverFactory;
import com.mm.concurrency.dispatch.rule.Rule;
import com.mm.concurrency.dispatch.rule.RuleSet;

public class AnnotationDispatchTest {

	@Test
	public void testSingleProcess() throws Exception {
		
		Dispatcher d = new Dispatcher();
		
		// Some rules
		Rule rule1 = new AnnotationKeyBuilder()
			.with(AnnotationKeyBuilder.boundField(Apple.class, "name", "X"))
			.buildRule("AppleRule"); 
		
		Rule rule2 = new AnnotationKeyBuilder()
			.with(boundField(Orange.class, "name", "X"))
			.buildRule("OrangeRule");
		
		RuleSet ruleSet = new RuleSet(rule1,rule2);
		
		d.registerPool(ruleSet, TestReceiver.class);
		
		d.dataReceived(new Apple("123"));
		d.dataReceived(new Apple("456"));
		d.dataReceived(new Orange("123"));
		d.dataReceived(new Orange("678"));
		
		
		Thread.sleep(500);
		
	}
}
