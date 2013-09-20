package com.mm.dispatchprogram;

import static com.mm.concurrency.dispatch.key.KeyBuilder.id;
import static com.mm.concurrency.dispatch.key.KeyBuilder.type;

import com.mm.concurrency.dispatch.Data;
import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.key.KeyBuilder;
import com.mm.concurrency.dispatch.receiver.Receiver;

public class Shaker implements Receiver {

	private Dispatcher dispatcher;
	private Apple apple;
	private Lime lime;
	
	public Shaker(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void run() {
		System.out.println("Shaking a: " + apple.getName() + ", l: " + lime.getName());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		CocktailMixture mixture = new CocktailMixture(apple, lime);
		
		dispatcher.dataReceived(new KeyBuilder()
			.then(type(CocktailMixture.class), id("" + System.nanoTime()))
			.buildKey(), mixture);
		
	}

	@Data("LimeRule")
	public void setLime(Lime lime) {
		this.lime = lime;
	}
	
	@Data("AppleRule")
	public void setApple(Apple apple) {
		this.apple = apple;
	}
}
