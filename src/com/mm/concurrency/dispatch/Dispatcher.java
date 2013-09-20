package com.mm.concurrency.dispatch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mm.concurrency.dispatch.generator.Generator;
import com.mm.concurrency.dispatch.generator.GeneratorRunner;
import com.mm.concurrency.dispatch.key.Key;
import com.mm.concurrency.dispatch.receiver.Receiver;
import com.mm.concurrency.dispatch.receiver.ReceiverFactory;
import com.mm.concurrency.dispatch.rule.Partial;
import com.mm.concurrency.dispatch.rule.RuleSet;

public class Dispatcher {
	
	private Map<RuleSet,ExecutorService> pools = new HashMap<RuleSet,ExecutorService>();
	
	/* Partials */
	private Multimap<RuleSet,Partial> partials = ArrayListMultimap.create();
	
	/* Simple rules */
	private Map<RuleSet,ReceiverFactory> rules = new HashMap<RuleSet,ReceiverFactory>();
	
	/* Generators */
	private List<Generator> generators = new ArrayList<Generator>();
	private ExecutorService generatorPool;
	
	private int threadsToWaitFor;
	
	private CountDownLatch latch;
	
	public void registerPool(RuleSet ruleSet, ReceiverFactory factory, boolean waitUntilFinished,
			RuleSet killRule){
		rules.put(ruleSet, factory);
		pools.put(ruleSet, Executors.newFixedThreadPool(5));
		
		if (waitUntilFinished)
			threadsToWaitFor++;
	}
	
	public void registerGenerator(Generator generator){
		generators.add(generator);
	}
	
	public void startAndAwait(){
		threadsToWaitFor += generators.size();
		generatorPool = Executors.newFixedThreadPool(generators.size());

		latch = new CountDownLatch(threadsToWaitFor);
		for (Generator gen : generators) {
			generatorPool.execute(new GeneratorRunner(gen){
				@Override
				public void postRun() {
					latch.countDown();
				}
			});
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {}
	}
	
	public synchronized void dataReceived(Key key, Object data){
		
		// Rules that we don't want to go to
		Set<RuleSet> ignore = new HashSet<RuleSet>();
		
		
		// First go through the partials. They get first dibs because
		// they are closer to completion
		Multimap<RuleSet,Partial> toRemove = ArrayListMultimap.create();
		for (RuleSet ruleSet : partials.keySet()) {
			
			List<Partial> entities = new ArrayList<Partial>(partials.get(ruleSet));
			for (Partial partial : entities) {
				
				boolean matchFound = partial.tryMatch(key, data);
				if (matchFound){
					ignore.add(ruleSet);
				}
				
				boolean complete = partial.isComplete();
				if (complete){
					// We're ready!!
					Receiver receiver = createReceiver(ruleSet, partial);
					ExecutorService pool = pools.get(ruleSet);
			        pool.submit(receiver);
			        toRemove.put(ruleSet, partial);
				}
			}
			
		}
		
		for (Entry<RuleSet, Partial> removeEntry : toRemove.entries()){
			partials.remove(removeEntry.getKey(), removeEntry.getValue());
		}
		
		// Now go through the normal rules
		for (RuleSet ruleSet : rules.keySet()){
			Partial partial = ruleSet.match(key, data);
			
			if (ignore.contains(ruleSet)){
				continue;
			}
			
			if (partial != null){
				
				if (partial.isComplete()){
					// We're ready!!
					Receiver receiver = createReceiver(ruleSet, partial);
					ExecutorService pool = pools.get(ruleSet);
			        pool.submit(receiver);
				}
				else {
					partials.put(ruleSet, partial);
				}
			}
		}
		
	}

	private Receiver createReceiver(RuleSet ruleSet, Partial partial) {
		ReceiverFactory factory = rules.get(ruleSet);
		Receiver receiver = factory.createReceiver();
		
		Method[] methods = receiver.getClass().getMethods();

		for (Method method : methods) {
		    Data annos = method.getAnnotation(Data.class);
		    if (annos != null) {
		    	String ruleName = annos.value();
		    	Object dataItem = partial.getData(ruleName);
		        try {
		            method.invoke(receiver,dataItem);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
		return receiver;
	}

}
