package com.mm.concurrency.dispatch.key;

import java.util.Map;

public interface KeyComponent {

	boolean matches(KeyComponent other, Map<String, Object> bindings);
}
