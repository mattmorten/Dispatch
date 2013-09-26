package com.mm.concurrency.dispatch.key;

import static com.mm.concurrency.dispatch.key.KeyBuilder.anyId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.boundId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.id;
import static com.mm.concurrency.dispatch.key.KeyBuilder.type;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.mm.concurrency.dispatch.A;
import com.mm.concurrency.dispatch.B;
import com.mm.concurrency.dispatch.C;
import com.mm.concurrency.dispatch.rule.Partial;
import com.mm.concurrency.dispatch.rule.Rule;
import com.mm.concurrency.dispatch.rule.RuleSet;
import com.mm.concurrency.dispatch.rule.Rule;

public class RuleTest {

	@Test
	public void testSimpleRuleMatching() throws Exception {
		
		Rule rule = new KeyBuilder()
			.then(type(A.class), anyId())
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), anyId())
			.buildRule("TestRule");
		
		Key key = new KeyBuilder()
			.then(type(A.class), id("Apple"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("ABC123"))
			.buildKey();
		
		assertTrue(rule.isSatisfiedBy(key, new HashMap<String,Object>()));
		
		key = new KeyBuilder()
			.then(type(A.class), id("Apple"))
			.then(type(B.class), id("Bricks"))
			.then(type(C.class), id("ABC123"))
			.buildKey();
		
		assertFalse(rule.isSatisfiedBy(key, new HashMap<String,Object>()));
		
		key = new KeyBuilder()
			.then(type(A.class), id("Apple"))
			.then(type(B.class), id("Bricks"))
			.then(type(C.class))
			.buildKey();
		
		assertFalse(rule.isSatisfiedBy(key, new HashMap<String,Object>()));
	}
	
	@Test
	public void testSimpleBinding() throws Exception {
		
		Rule rule = new KeyBuilder()
			.then(type(A.class), anyId())
			.then(type(B.class), boundId("X"))
			.then(type(C.class), boundId("X"))
			.buildRule("TestRule");
		
		Key key = new KeyBuilder()
			.then(type(A.class), id("Apple"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("Balloons"))
			.buildKey();
		
		assertTrue(rule.isSatisfiedBy(key, new HashMap<String,Object>()));
		
		key = new KeyBuilder()
			.then(type(A.class), id("Apple"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("ABC123"))
			.buildKey();
	
		assertFalse(rule.isSatisfiedBy(key, new HashMap<String,Object>()));

	}
	
	@Test
	public void testMultipleRules() throws Exception {
		
		Rule rule1 = new KeyBuilder()
			.then(type(A.class), id("A1"))
			.then(type(B.class), anyId())
			.then(type(C.class), boundId("X"))
			.buildRule("TestRule1");
		
		Rule rule2 = new KeyBuilder()
			.then(type(A.class), id("A2"))
			.then(type(B.class), anyId())
			.then(type(C.class), boundId("X"))
			.buildRule("TestRule2");
		
		RuleSet ruleSet = new RuleSet(rule1,rule2);
		
		Key key = new KeyBuilder()
			.then(type(A.class), id("A1"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("ABC123"))
			.buildKey();
		
		Partial partial = ruleSet.match(key,"SomeData");
		assertFalse(partial.isComplete());
		
		key = new KeyBuilder()
			.then(type(A.class), id("A2"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("ABC234"))
			.buildKey();
	
		assertFalse(partial.tryMatch(key, "SomeMoreData"));
		
		key = new KeyBuilder()
			.then(type(A.class), id("A2"))
			.then(type(B.class), id("Balloons"))
			.then(type(C.class), id("ABC123"))
			.buildKey();
		
		assertTrue(partial.tryMatch(key, "SomeFurtherData"));
		assertTrue(partial.isComplete());
		
		// Now try using the rule again, to prove that is is reusable
		key = new KeyBuilder()
		.then(type(A.class), id("A1"))
		.then(type(B.class), id("Balloons"))
		.then(type(C.class), id("XYZYX"))
		.buildKey();
		
		Key key2 = new KeyBuilder()
		.then(type(A.class), id("A2"))
		.then(type(B.class), id("Bellyflops"))
		.then(type(C.class), id("XYZYX"))
		.buildKey();
		
		partial = ruleSet.match(key2,"SomeData2");
		assertFalse(partial.isComplete());
		assertTrue(partial.tryMatch(key, "SomeFurtherData2"));
		assertTrue(partial.isComplete());
	}
	
}
