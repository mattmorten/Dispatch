package com.mm.concurrency.dispatch.generator;

public abstract class GeneratorRunner implements Runnable {

	private Generator generator;
	
	public GeneratorRunner(Generator generator) {
		this.generator = generator;
	}
	public void run() {
		try {
			generator.run();
		} finally {
			postRun();
		}
		
	}
	
	public abstract void postRun();

}
