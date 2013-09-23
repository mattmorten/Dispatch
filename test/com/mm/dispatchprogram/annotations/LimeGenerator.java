package com.mm.dispatchprogram.annotations;

import java.util.Arrays;
import java.util.List;


import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.annotation.AnnotationKeyBuilder;
import com.mm.concurrency.dispatch.generator.Generator;
import com.mm.concurrency.dispatch.key.KeyBuilder;
import static com.mm.concurrency.dispatch.key.KeyBuilder.*;

public class LimeGenerator implements Generator {

	private List<String> names = Arrays.asList(
		"A","B","C","D","E","F","G","H","I","J","K"
			);
	
	private Dispatcher dispatcher;
	
	public LimeGenerator(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void run() {
		
		for (String name : names) {
			Lime lime = new Lime(name);
			
			dispatcher.dataReceived(lime);
			
			System.out.println("Build Lime: " + lime);
			
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// All gone
		dispatcher.dataReceived(new AnnotationKeyBuilder()
			.with(AnnotationKeyBuilder.allType(Lime.class))
			.buildKey(), null);
	}

}
