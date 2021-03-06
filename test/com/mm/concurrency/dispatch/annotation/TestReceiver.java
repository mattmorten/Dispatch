package com.mm.concurrency.dispatch.annotation;

import com.mm.concurrency.dispatch.Data;
import com.mm.concurrency.dispatch.Dispatcher;
import com.mm.concurrency.dispatch.receiver.Receiver;

public class TestReceiver implements Receiver {
	
	private Apple apple;
	private Orange orange;

	
	@Data("AppleRule")
	public void setApple(Apple apple) {
		this.apple = apple;
	}

	@Data("OrangeRule")
	public void setOrange(Orange orange) {
		this.orange = orange;
	}

	public void run() {
		System.out.println("A: " +apple);
		System.out.println("O: " +orange);
	}

	public void setDispatcher(Dispatcher dispatcher) {
	}
}
