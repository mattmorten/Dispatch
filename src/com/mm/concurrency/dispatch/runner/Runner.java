package com.mm.concurrency.dispatch.runner;

import com.mm.concurrency.dispatch.receiver.Receiver;

public interface Runner {
	void dataReady(Receiver receiver);
}
