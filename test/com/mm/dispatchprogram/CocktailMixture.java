package com.mm.dispatchprogram;

public class CocktailMixture {
	private Apple apple;
	private Lime lime;
	public CocktailMixture(Apple apple, Lime lime) {
		this.apple = apple;
		this.lime = lime;
	}
	@Override
	public String toString() {
		return "CocktailMixture [apple=" + apple + ", lime=" + lime + "]";
	}
	
}
