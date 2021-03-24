package com.hyper.ballgame.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hyper.ballgame.BallGame;
import com.hyper.ballgame.MouseHandler;
import com.hyper.ballgame.logic.World;

public class Player extends Entity {
	static {
		Image i = null;
		try {
			i = ImageIO.read(new File("res/player.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		PLAYER_IMAGE = i;
	}
	public static final Image PLAYER_IMAGE;
	public static final int PLAYER_WIDTH = 100, PLAYER_HEIGHT = 150;
	public static final int ARROW_LENGTH = 80;

	public static final int FRAMES_BETWEEN_SHOTS = BallGame.FPS/20;

	private MouseHandler handler;
	private World world;

	private int ballCount = 1, lastThrowBallCount = 1, ballsInHand = 1, ballsRecieved = 0,
			ballShootCounter = 0;

	private int position = BallGame.WIDTH/2, nextPosition = position;

	private double aim = Math.PI/2.0;

	private PlayerState state = PlayerState.AIMING;

	public Player(MouseHandler handler, World world) {
		this.handler = handler;
		this.world = world;
	}

	@Override
	public void update() {
		switch(state) {
		case AIMING:
			Point2D pos = handler.getMousePosition();
			this.position = nextPosition;
			this.ballsInHand = this.ballCount;
			this.aim = Math.atan2(pos.getY()-BallGame.HEIGHT, pos.getX()-this.position);
			if(aim > -Math.PI/6) aim = -Math.PI/6;
			if(aim < -Math.PI*5/6) aim = -Math.PI*5/6;
			if(handler.isMousePressed()) {
				this.state = PlayerState.SHOOTING;
				this.ballsRecieved = 0;
				this.lastThrowBallCount = this.ballCount;
			}
			break;
		case SHOOTING:
			if(--ballShootCounter <= 0) {
				shoot();
				if(ballsInHand == 0)
					state = PlayerState.WAITING;
			}
			break;
		case WAITING:
			if(ballsRecieved == lastThrowBallCount) {
				state = PlayerState.AIMING;
				world.advanceBlocks();
			}
			break;
		default:
		}
	}

	private void shoot() {
		ballShootCounter = FRAMES_BETWEEN_SHOTS;
		ballsInHand--;
		world.spawn(new Ball(world, new Point2D.Double(position, BallGame.HEIGHT), this.aim));
	}
	
	public void recieveBall(Ball b) {
		if(ballsRecieved == 0) nextPosition = (int) b.getPosition().getX();
		ballsRecieved++;
		world.kill(b);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.drawImage(PLAYER_IMAGE, nextPosition-PLAYER_WIDTH/2, BallGame.HEIGHT-PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT, null);
		g.drawString(ballsInHand + "/" + ballCount, nextPosition+20, BallGame.HEIGHT-50);
		if(state == PlayerState.AIMING) {
			g.setColor(Color.MAGENTA);
			Point2D tip = new Point(nextPosition + (int) (Math.cos(aim)*ARROW_LENGTH), BallGame.HEIGHT + (int) (Math.sin(aim)*ARROW_LENGTH));
			double angle = aim + 5*Math.PI/6.0,
					angle2 = aim - 5*Math.PI/6.0;
			g.drawLine(nextPosition, BallGame.HEIGHT, (int)tip.getX(), (int)tip.getY());
			g.drawLine((int)tip.getX(), (int)tip.getY(), (int)(tip.getX() + Math.cos(angle)*ARROW_LENGTH/4), (int)(tip.getY() + Math.sin(angle)*ARROW_LENGTH/4));
			g.drawLine((int)tip.getX(), (int)tip.getY(), (int)(tip.getX() + Math.cos(angle2)*ARROW_LENGTH/4), (int)(tip.getY() + Math.sin(angle2)*ARROW_LENGTH/4));
		}
	}

	public void addBall() {
		this.ballCount++;
	}
}
