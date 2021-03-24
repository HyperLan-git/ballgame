package com.hyper.ballgame.entity;

import java.awt.Graphics2D;

public abstract class Entity {
	private boolean dead = false;

	public abstract void update();
	
	public abstract void draw(Graphics2D g);

	public final void die() {
		dead = true;
	}
	
	public final boolean isDead() {
		return dead;
	}

	public boolean onMoveDown() {
		return false;
	}
}
