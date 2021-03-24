package com.hyper.ballgame;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

public class MouseHandler implements MouseMotionListener, MouseListener {
	private Point2D mousePos = new Point(0, 0);
	
	private boolean mousePressed = false;
	
	public MouseHandler(Component source) {
		source.addMouseListener(this);
		source.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos.setLocation(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public void update() {
		this.mousePressed = false;
	}
	
	public boolean isMousePressed() {
		return mousePressed;
	}

	public Point2D getMousePosition() {
		return this.mousePos;
	}
}
