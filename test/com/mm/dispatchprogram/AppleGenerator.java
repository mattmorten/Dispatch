package com.mm.dispatchprogram;

import java.util.Arrays;
import java.util.List;


import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.generator.Generator;
import com.mm.concurrency.dispatch.key.KeyBuilder;
import static com.mm.concurrency.dispatch.key.KeyBuilder.*;

public class AppleGenerator implements Generator {

	private List<String> names = Arrays.asList(
			"D","G","B","A","C","J","E","I","F","H","K"
			);
	
	private Dispatcher dispatcher;
	
	public AppleGenerator(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void run() {
		
		for (String name : names) {
			Apple apple = new Apple(name);
			
			dispatcher.dataReceived(new KeyBuilder()
				.then(type(Apple.class), id(name))
				.buildKey(), apple);
			
			System.out.println("Built apple: " + apple);
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// All gone
		dispatcher.dataReceived(new KeyBuilder()
			.then(type(Apple.class), allId())
			.buildKey(), null);
	}

}
