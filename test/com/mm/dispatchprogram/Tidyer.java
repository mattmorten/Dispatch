package com.mm.dispatchprogram;

import com.mm.concurrency.dispatch.receiver.Receiver;

public class Tidyer implements Receiver {

	public void run() {
		System.out.println("Finished all Ingredients. Tidying :-)");
	}

}
