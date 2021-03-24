package com.hyper.ballgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import com.hyper.ballgame.entity.Entity;
import com.hyper.ballgame.entity.Player;
import com.hyper.ballgame.logic.World;

public class BallGame extends JFrame {
	public static final int FPS = 60;
	public static final int HEIGHT = 1000;
	public static final int WIDTH = 800;
	public static final int GRID_SIZE = 80;
	public static final Dimension MINIMUM_SIZE = new Dimension(WIDTH+1, HEIGHT+20);

	private long nextFrameTime = 0;

	private Dimension offDimension = new Dimension();
	private Image offImage;
	private Graphics offGraphics;

	private MouseHandler handler;
	private Player thePlayer;
	private World theWorld;

	public BallGame() {
		this.setMinimumSize(MINIMUM_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setUndecorated(true);

		handler = new MouseHandler(this);
		theWorld = new World();
		thePlayer = new Player(handler, theWorld);

		theWorld.setPlayer(thePlayer);

		theWorld.advanceBlocks();

		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		if(offImage != null)
			g.drawImage(offImage, 0, 0, this);

		Dimension d = this.getSize();
		if (d.width > 0 && (offGraphics == null || d.width != offDimension.width || d.height != offDimension.height)) {
			offDimension = d;
			offImage = createImage(d.width, d.height);
			offGraphics = offImage.getGraphics();
		}

		Graphics2D g2 = (Graphics2D)offGraphics;
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2.setColor(Color.GRAY);
		for(int x = 0; x <= WIDTH-GRID_SIZE; x += GRID_SIZE) for(int y = 0; y <= HEIGHT-GRID_SIZE; y += GRID_SIZE) {
			g2.drawLine(x, y, x + GRID_SIZE, y);
			g2.drawLine(x, y, x, y + GRID_SIZE);
			g2.drawLine(x, y + GRID_SIZE, x + GRID_SIZE, y + GRID_SIZE);
			g2.drawLine(x + GRID_SIZE, y, x + GRID_SIZE, y + GRID_SIZE);
		}

		thePlayer.draw(g2);
		for(Entity e : theWorld.getEntities())
			if(!e.isDead())
				e.draw(g2);
		g.drawImage(offImage, 0, 0, this);
	}

	public void update() {
		if(this.nextFrameTime < System.currentTimeMillis()) {
			theWorld.update();
			this.handler.update();
			this.nextFrameTime = System.currentTimeMillis() + 1000/FPS;
		}
		this.repaint();
	}

	public static double dotProduct(Point2D v, Point2D v2) {
		return v.getX()*v2.getX() + v.getY()*v2.getY();
	}

	public static double lengthSq(Point2D v) {
		return dotProduct(v, v);
	}

	public static Point2D mirror(Point2D v, double normal) {
		Point2D n = new Point2D.Double(Math.cos(normal), Math.sin(normal)),
				result = new Point2D.Double(v.getX()-n.getX()*2*dotProduct(v, n),
						v.getY()-n.getY()*2*dotProduct(v, n));
		return result;
	}
}
