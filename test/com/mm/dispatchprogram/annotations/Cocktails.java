package com.mm.dispatchprogram.annotations;

import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.all;
import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.any;
import static com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder.boundField;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder;
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
		RuleSet ingregientsRules = new RuleSet(
			new Rule("AppleRule",boundField(Apple.class,"name","X")),
			new Rule("LimeRule",boundField(Lime.class,"name","X")));
		
		
		// Register the Shaker
		d.registerPool(ingregientsRules,Shaker.class);
		
		// Register the Glasser
		RuleSet mixtureRule = new RuleSet(
			new Rule("MixtureRule",any(CocktailMixture.class)));
		

		d.registerPool(mixtureRule,Glasser.class);

		// Register the Tidyer
		RuleSet allIngredientsGoneRule = new RuleSet(
				new Rule("LimeCompleteRule",all(Lime.class)),
				new Rule("AppleCompleteRule",all(Apple.class)));
			

		d.registerPool(allIngredientsGoneRule, Tidyer.class);
		
		// Create a ruleset which signifies we're done
		RuleSet finished = new RuleSet(
				new Rule("TidyCompleteRule",all(Tidyer.class)));
		
		
		d.startAndAwait(finished);		
	}
}
