package com.hyper.ballgame.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.hyper.ballgame.BallGame;
import com.hyper.ballgame.logic.World;

public class Ball extends Entity {
	public static final double SPEED = 500, RANDOM_BOUNCE = 0.1, BOUNCE_CHANCE = 0.001;
	public static final int SIZE = 15;
	private Color c = Color.ORANGE;

	private Point2D pos = new Point2D.Double(), momentum = new Point2D.Double();

	private World world;

	public Ball(World world, Point2D position, double angle) {
		this.world = world;
		this.pos.setLocation(position);
		this.momentum.setLocation(Math.cos(angle)*SPEED/BallGame.FPS, Math.sin(angle)*SPEED/BallGame.FPS);
	}

	@Override
	public void update() {
		for(int i = 0; i < 4; i++)
			if(quarterStep()) {
				world.getPlayer().recieveBall(this);
				break;
			}
	}

	private boolean quarterStep() {
		double length = 0.25;
		pos.setLocation(momentum.getX()/4 + pos.getX(), momentum.getY()/4 + pos.getY());
		if(pos.getX() < 10) {
			bounce(0);
			pos.setLocation(10, pos.getY());
		} else if(pos.getX() > BallGame.WIDTH-10) {
			bounce(Math.PI);
			pos.setLocation(BallGame.WIDTH-10, pos.getY());
		}

		if(pos.getY() < 10) {
			bounce(-Math.PI/2.0);
			pos.setLocation(pos.getX(), 10);
		} else if(pos.getY() > BallGame.HEIGHT)
			return true;

		for(Collidable c : world.getBlocks())
			if(c.distanceSqTo(pos) < SIZE*SIZE) {
				bounce(c.getNormal(pos));
				while(c.distanceSqTo(pos) < SIZE*SIZE) {
					pos.setLocation(momentum.getX()/16 + pos.getX(), momentum.getY()/16 + pos.getY());
					length += 1.0/4;
					if(length >= 1) {
						break;
					}
				}
				c.onHit();
				break;
			}
		pos.setLocation(momentum.getX()*(1.0-length)/4 + pos.getX(), momentum.getY()*(1.0-length)/4 + pos.getY());
		return false;
	}

	private void bounce(double normalAngle) {
		bounceRandom(normalAngle, BOUNCE_CHANCE);
	}

	private void bounceRandom(double normalAngle, double randomChance) {
		if(Math.random() < randomChance)
			momentum = BallGame.mirror(momentum, normalAngle + Math.random()*RANDOM_BOUNCE);
		else
			momentum = BallGame.mirror(momentum, normalAngle);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(c);
		g.fillOval((int)pos.getX()-SIZE/2, (int)pos.getY()-SIZE/2, SIZE, SIZE);
	}

	public Point2D getPosition() {
		return this.pos;
	}

}
