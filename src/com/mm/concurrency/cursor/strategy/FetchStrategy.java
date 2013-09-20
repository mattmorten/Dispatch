package com.mm.concurrency.cursor.strategy;

import java.util.Iterator;

import com.mm.concurrency.cursor.event.Listener;

public interface FetchStrategy<T> extends Listener {
	boolean shouldFetch();
	Iterator<T> fetch();
}
