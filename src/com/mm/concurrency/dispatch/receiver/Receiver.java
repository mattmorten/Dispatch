package com.mm.concurrency.dispatch.receiver;

import com.mm.concurrency.dispatch.Dispatcher;


public interface Receiver extends Runnable {
	void setDispatcher(Dispatcher dispatcher);
}
