package com.hyper.ballgame.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.hyper.ballgame.BallGame;
import com.hyper.ballgame.entity.BallAdder;
import com.hyper.ballgame.entity.Block;
import com.hyper.ballgame.entity.Collidable;
import com.hyper.ballgame.entity.Entity;
import com.hyper.ballgame.entity.Player;
import com.hyper.ballgame.entity.Triangle;

public class World {
	public static final double BLOCK_SPAWN = 0.5,
			TRIANGLE_CHANCE = 0;

	private Player player;

	private boolean lost = false;

	private int score = 0;

	private ArrayList<Collidable> blocks = new ArrayList<>();

	private List<Entity> otherEntities = new ArrayList<>();

	public World() {}

	public void setPlayer(Player p) {
		this.player = p;
	}

	public void update() {
		player.update();
		List<Entity> entities = getEntities();
		for(int i = entities.size()-1; i >= 0; i--) {
			if(entities.get(i).isDead())
				if(!blocks.remove(entities.get(i)))
					otherEntities.remove(entities.get(i));
		}
		entities = getEntities();
		for(Entity e : entities)
			e.update();
	}

	public void advanceBlocks() {
		score++;

		for(Entity e : this.getEntities())
			if(e.onMoveDown()) lost = true;

		int spawnerPlace = (int) (Math.random()*BallGame.WIDTH/BallGame.GRID_SIZE);

		for(int x = 0; x <= BallGame.WIDTH-BallGame.GRID_SIZE; x += BallGame.GRID_SIZE) {
			if(spawnerPlace*BallGame.GRID_SIZE == x) {
				this.spawn(new BallAdder(this, new Point(x+BallGame.GRID_SIZE/2, BallGame.GRID_SIZE*3/2)));
			} else if(Math.random() < BLOCK_SPAWN) {
				if(Math.random() < TRIANGLE_CHANCE)
					this.spawn(new Triangle(Triangle.Corner.values()[(int) (Math.round(Math.random()*3))], new Point(x, BallGame.GRID_SIZE), this.score));
				else
					this.spawn(new Block(new Point(x, BallGame.GRID_SIZE), this.score));
			}
		}

		if(lost)System.out.println("You lost !");
	}

	public List<Collidable> getBlocks() {
		return new ArrayList<>(blocks);
	}

	public List<Entity> getEntities() {
		List<Entity> result = new ArrayList<>(blocks);
		result.addAll(otherEntities);
		return result;
	}

	public void spawn(Entity e) {
		this.otherEntities.add(e);
	}

	public void spawn(Collidable b) {
		this.blocks.add(b);
	}

	public void kill(Entity e) {
		e.die();
	}

	public Player getPlayer() {
		return this.player;
	}

	public int getScore() {
		return this.score;
	}
}
