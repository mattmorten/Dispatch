package com.mm.dispatchprogram;

import static com.mm.concurrency.dispatch.key.KeyBuilder.*;
import static com.mm.concurrency.dispatch.key.KeyBuilder.boundId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.type;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.key.KeyBuilder;
import com.mm.concurrency.dispatch.receiver.Receiver;
import com.mm.concurrency.dispatch.receiver.ReceiverFactory;
import com.mm.concurrency.dispatch.rule.Rule;
import com.mm.concurrency.dispatch.rule.RuleSet;

public class Cocktails {

	public static void main(String[] args) throws InterruptedException {
		final Dispatcher d = new Dispatcher();
		
		d.registerGenerator(new LimeGenerator(d));
		d.registerGenerator(new AppleGenerator(d));
		
		// Ruleset for apply
		RuleSet ingregientsRules = new RuleSet("IngredientsRules",
			new KeyBuilder()
				.then(type(Apple.class), boundId("X")).buildRule("AppleRule"),
			new KeyBuilder()
				.then(type(Lime.class), boundId("X")).buildRule("LimeRule"));
		
		// Kill rules
//		RuleSet ingredientsComplete = new RuleSet("KillRules",
//			new KeyBuilder()
//				.then(type(Apple.class), allId()).buildRule("ApplesComplete"),
//			new KeyBuilder()
//				.then(type(Lime.class), allId()).buildRule("LimesComplete"));
			
		
		// Register the Shaker
		d.registerPool(ingregientsRules, 
				new ReceiverFactory() {
					public Receiver createReceiver() {
						return new Shaker(d);
					}
				}, false,null);
		
		// Register the Glasser
		RuleSet mixtureRule = new RuleSet("MixtureRuleSet",
			new KeyBuilder()
				.then(type(CocktailMixture.class), anyId())
				.buildRule("MixtureRule"));
		

		d.registerPool(mixtureRule, 
				new ReceiverFactory() {
					public Receiver createReceiver() {
						return new Glasser();
					}
				}, false, null);

		// Register the Tidyer
		RuleSet allIngredientsGoneRule = new RuleSet("AllIngredients",
				new KeyBuilder()
					.then(type(Lime.class), allId())
					.buildRule("AllLimeRule"),
				new KeyBuilder()
					.then(type(Apple.class), allId())
					.buildRule("AllAppleRule"));
			

		d.registerPool(allIngredientsGoneRule, 
				new ReceiverFactory() {
					public Receiver createReceiver() {
						return new Tidyer();
					}
				}, false, null);
		
		d.startAndAwait();
		
		
		Thread.sleep(10 * 1000);
		
	}
}
