package com.mm.dispatchprogram.annotations;

import com.mm.concurrency.dispatch.Data;
import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.receiver.Receiver;

public class Glasser implements Receiver {

	private CocktailMixture mixture;
	
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Ready to drink: " + mixture);
		
	}

	@Data("MixtureRule")
	public void setMixture(CocktailMixture mixture) {
		this.mixture = mixture;
	}
	
	public void setDispatcher(Dispatcher dispatcher) {
	
	}

}
