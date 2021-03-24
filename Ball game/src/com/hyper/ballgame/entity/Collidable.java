package com.hyper.ballgame.entity;

import java.awt.geom.Point2D;

public abstract class Collidable extends Entity {
	@Override
	public void update() {}
	
	public abstract double getNormal(Point2D position);
	
	public abstract double distanceSqTo(Point2D position);
	
	public abstract void onHit();
}
