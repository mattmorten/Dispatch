package com.mm.dispatchprogram.annotations;

import static com.mm.concurrency.dispatch.key.KeyBuilder.allId;

import java.util.Arrays;
import java.util.List;

import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder;
import com.mm.concurrency.dispatch.generator.Generator;

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
			
			dispatcher.dataReceived(apple);
			
			System.out.println("Built apple: " + apple);
			
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// All gone
		dispatcher.dataReceived(new AnnotationKeyBuilder()
			.with(AnnotationKeyBuilder.allType(Apple.class))
			.buildKey(), null);
	}

}
