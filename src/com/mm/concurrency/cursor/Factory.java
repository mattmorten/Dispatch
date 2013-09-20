package com.mm.concurrency.cursor;

import java.util.Iterator;

public interface Factory<T> {

	Iterator<T> createItems(int position, int numberOfItems);
}
