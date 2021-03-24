package com.hyper.ballgame.entity;

import static com.hyper.ballgame.BallGame.GRID_SIZE;
import static com.hyper.ballgame.BallGame.HEIGHT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import com.hyper.ballgame.BallGame;

public class Triangle extends Collidable {
	//TODO
	private final Corner rightAngleCorner;

	private Point2D pos;

	private Polygon actualShape;

	private int counter;

	public Triangle(Corner c, Point2D position, int counter) {
		this.counter = counter;
		c = Corner.DOWN_LEFT;
		this.pos = position;
		this.rightAngleCorner = c;
		generateShape(position);
	}

	private void generateShape(Point2D position) {
		int[] xPoints = new int[3], yPoints = new int[3];
		for(int i = 0; i < 3; i++) {
			xPoints[i] = (int) position.getX();
			yPoints[i] = (int) position.getY();
		}
		switch(rightAngleCorner) {
		case UP_LEFT:
			xPoints[1] += GRID_SIZE;
			yPoints[2] += GRID_SIZE;
			break;
		case UP_RIGHT:
			xPoints[0] += GRID_SIZE;
			xPoints[1] += GRID_SIZE;
			yPoints[0] += GRID_SIZE;
			yPoints[2] += GRID_SIZE;
			break;
		case DOWN_RIGHT:
			xPoints[0] += GRID_SIZE;
			xPoints[1] += GRID_SIZE;
			yPoints[1] += GRID_SIZE;
			break;
		case DOWN_LEFT:
			xPoints[1] += GRID_SIZE;
			yPoints[2] += GRID_SIZE;
			break;
		}
		actualShape = new Polygon(xPoints, yPoints, 3);
	}

	private Point2D getCorner() {
		return new Point(actualShape.xpoints[0], actualShape.ypoints[0]);
	}

	public static final Line2D[] getSides(Polygon p) {
		Line2D[] result = new Line2D[p.npoints];
		for(int i = 0; i < result.length; i++) {
			if(i == result.length-1)
				result[i] = new Line2D.Double(p.xpoints[i], p.ypoints[i], p.xpoints[0], p.ypoints[0]);
			else
				result[i] = new Line2D.Double(p.xpoints[i], p.ypoints[i], p.xpoints[i+1], p.ypoints[i+1]);
		}
		return result;
	}

	@Override
	public double getNormal(Point2D position) {
		Point2D toCorner = new Point2D.Double(position.getX()-actualShape.xpoints[0], position.getY()-actualShape.ypoints[0]);
		switch(rightAngleCorner) {
		case UP_LEFT:
			if(toCorner.getX() <= 0 && toCorner.getY() <= GRID_SIZE && toCorner.getY() >= 0)
				//Left side
				return 0;
			if(toCorner.getX() >= toCorner.getY() && -toCorner.getX() <= toCorner.getY() &&
					GRID_SIZE*GRID_SIZE-toCorner.getX() >= toCorner.getY())
				//Hypotenuse
				return -Math.PI/4;
			if(toCorner.getY() >= GRID_SIZE && toCorner.getX() >= 0 && toCorner.getX() <= GRID_SIZE)
				//Upper side
				return Math.PI/2;
			break;
		default:
			//TODO
		}

		return 0;
	}

	@Override
	public double distanceSqTo(Point2D position) {
		double x, y;
		switch(rightAngleCorner) {
		case DOWN_LEFT:
			x = position.getX()-this.pos.getX();
			y = position.getX()-this.pos.getY();
			if(x > 0 && x+y > GRID_SIZE && y > 0) {
				Point2D toCorner = new Point2D.Double(x, y-GRID_SIZE),
						vec = new Point2D.Double(this.actualShape.xpoints[1]-this.actualShape.xpoints[2], this.actualShape.ypoints[1]-this.actualShape.ypoints[2]);
				double scalar = BallGame.dotProduct(toCorner, vec);
				Point2D proj = new Point2D.Double(this.actualShape.xpoints[2]+ (this.actualShape.xpoints[1]-this.actualShape.xpoints[2])*scalar/BallGame.lengthSq(vec),
						this.actualShape.ypoints[2]+ (this.actualShape.ypoints[1]-this.actualShape.ypoints[2])*scalar/BallGame.lengthSq(vec));
				return proj.distanceSq(position);
			}
		default:
		}
		Point2D pos = new Point2D.Double(this.actualShape.xpoints[0], this.actualShape.ypoints[0]);
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
		pos = new Point2D.Double((int)pos.getX(), (int)pos.getY() + GRID_SIZE);
		generateShape(pos);
		return getCorner().getY() >= HEIGHT - GRID_SIZE;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fill(actualShape);
		g.setColor(Color.WHITE);
		int xAvg = 0, yAvg = 0;
		for(int i = 0; i < 3; i++) {
			xAvg += actualShape.xpoints[i];
			yAvg += actualShape.ypoints[i];
		}
		g.drawString(String.valueOf(counter), xAvg/3, yAvg/3);
	}

	public static enum Corner {
		UP_LEFT,
		UP_RIGHT,
		DOWN_RIGHT,
		DOWN_LEFT;
	}
}
