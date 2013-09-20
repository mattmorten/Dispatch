package com.mm.concurrency.cursor.event;

public interface Listener {
	void readStart();
	void readEnd();
	void delay(long millis);
}
