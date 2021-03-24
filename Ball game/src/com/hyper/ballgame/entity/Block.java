package com.hyper.ballgame.entity;

import static com.hyper.ballgame.BallGame.GRID_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.hyper.ballgame.BallGame;

public class Block extends Collidable {
	private Color bgColor = Color.CYAN,
			outerColor = Color.BLUE;

	private Point2D pos;

	private int counter;

	public Block(Point2D position, int counter) {
		this.pos = position;
		this.counter = counter;
	}

	@Override
	public void draw(Graphics2D g) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(String.valueOf(counter), g);
		g.setColor(bgColor);
		g.fillRect((int)pos.getX(), (int)pos.getY(), GRID_SIZE, GRID_SIZE);
		g.setColor(outerColor);
		g.drawRect((int)pos.getX()+1, (int)pos.getY()+1, GRID_SIZE-3, GRID_SIZE-3);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(counter), (int)pos.getX() + GRID_SIZE/2-(int)rect.getWidth()/2, (int)pos.getY() + GRID_SIZE/2+(int)rect.getHeight()/4);
	}

	@Override
	public double getNormal(Point2D position) {
		if(position.getX() >= pos.getX() && position.getX() <= pos.getX() + GRID_SIZE)
			return Math.PI/2;

		if(position.getY() >= pos.getY() && position.getY() <= pos.getY() + GRID_SIZE)
			return 0;

		//Ne nearest point from the square is a corner
		Point2D nearestCorner = pos;
		if(nearestCorner.distanceSq(position) > position.distanceSq(pos.getX(), pos.getY() + GRID_SIZE))
			nearestCorner = new Point2D.Double(pos.getX(), pos.getY() + GRID_SIZE);

		if(nearestCorner.distanceSq(position) > position.distanceSq(pos.getX() + GRID_SIZE, pos.getY()))
			nearestCorner = new Point2D.Double(pos.getX() + GRID_SIZE, pos.getY());

		if(nearestCorner.distanceSq(position) > position.distanceSq(pos.getX() + GRID_SIZE, pos.getY() + GRID_SIZE))
			nearestCorner = new Point2D.Double(pos.getX() + GRID_SIZE, pos.getY() + GRID_SIZE);

		return Math.atan2(nearestCorner.getY()-position.getY(), nearestCorner.getX()-position.getX());
	}

	@Override
	public double distanceSqTo(Point2D position) {
		double dx = Math.max(Math.max(pos.getX() - position.getX(),  position.getX() - GRID_SIZE - pos.getX()), 0),
				dy = Math.max(Math.max(pos.getY() - position.getY(), position.getY() - GRID_SIZE - pos.getY()), 0);
		return dx*dx+dy*dy;
	}

	@Override
	public void onHit() {
		counter--;
		if(counter <= 0)
			this.die();
	}

	@Override
	public boolean onMoveDown() {
		this.pos.setLocation(pos.getX(), pos.getY() + GRID_SIZE);
		return pos.getY() >= BallGame.HEIGHT - GRID_SIZE;
	}
}
