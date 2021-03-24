package com.hyper.ballgame.entity;

import static com.hyper.ballgame.BallGame.GRID_SIZE;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.hyper.ballgame.BallGame;
import com.hyper.ballgame.logic.World;

public class BallAdder extends Entity {
	public static final int SIZE = 15;

	private World world;
	
	private Point2D position;

	public BallAdder(World world, Point2D position) {
		this.world = world;
		this.position = position;
	}

	@Override
	public void update() {
		for(Entity e : world.getEntities()) {
			if(e instanceof Ball) {
				Ball b = (Ball)e;
				if(b.getPosition().distanceSq(position) <= (SIZE+Ball.SIZE)*(SIZE+Ball.SIZE)) {
					world.getPlayer().addBall();
					this.die();
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(2));
		g.drawOval((int)position.getX()-SIZE, (int)position.getY()-SIZE, SIZE*2, SIZE*2);
		g.drawLine((int)position.getX(), (int)position.getY()-SIZE/3, (int)position.getX(), (int)position.getY()+SIZE/3);
		g.drawLine((int)position.getX()-SIZE/3, (int)position.getY(), (int)position.getX()+SIZE/3, (int)position.getY());
		g.setStroke(new BasicStroke(1));
	}
	
	@Override
	public boolean onMoveDown() {
		this.position.setLocation(position.getX(), position.getY() + GRID_SIZE);
		return position.getY() >= BallGame.HEIGHT - GRID_SIZE;
	}
}
