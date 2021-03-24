package com.hyper.ballgame;

public abstract class Main {
	public static void main(String[] args) {
		BallGame b = new BallGame();
		while(true) {
			b.update();
		}
	}
}
