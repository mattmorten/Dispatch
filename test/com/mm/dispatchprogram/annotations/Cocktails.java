package com.mm.dispatchprogram.annotations;

import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.boundField;
import static com.mm.concurrency.dispatch.key.KeyBuilder.allId;
import static com.mm.concurrency.dispatch.key.KeyBuilder.type;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder;
import com.mm.concurrency.dispatch.key.KeyBuilder;
import com.mm.concurrency.dispatch.receiver.Receiver;
import com.mm.concurrency.dispatch.receiver.ReceiverFactory;
import com.mm.concurrency.dispatch.rule.RuleSet;

public class Cocktails {

	public static void main(String[] args) throws InterruptedException {
		final Dispatcher d = new Dispatcher();
		
		d.registerGenerator(new LimeGenerator(d));
		d.registerGenerator(new AppleGenerator(d));
		
		// Ruleset for apply
		RuleSet ingregientsRules = new RuleSet("IngredientsRules",
			new AnnotationKeyBuilder()
				.with(boundField(Apple.class,"Name","X")).buildRule("AppleRule"),
			new AnnotationKeyBuilder()
				.with(boundField(Lime.class,"Name","X")).buildRule("LimeRule"));
		
		
		// Register the Shaker
		d.registerPool(ingregientsRules, 
				new ReceiverFactory() {
					public Receiver createReceiver() {
						return new Shaker(d);
					}
				}, false,null);
		
		// Register the Glasser
		RuleSet mixtureRule = new RuleSet("MixtureRuleSet",
			new AnnotationKeyBuilder()
				.with(AnnotationKeyBuilder.anyFieldValue(CocktailMixture.class))
				.buildRule("MixtureRule"));
		

		d.registerPool(mixtureRule, 
				new ReceiverFactory() {
					public Receiver createReceiver() {
						return new Glasser();
					}
				}, false, null);

		// Register the Tidyer
		RuleSet allIngredientsGoneRule = new RuleSet("AllIngredients",
				new AnnotationKeyBuilder()
					.with(AnnotationKeyBuilder.allType(Lime.class))
					.buildRule("AllLimeRule"),
				new KeyBuilder()
					.then(AnnotationKeyBuilder.allType(Apple.class))
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
