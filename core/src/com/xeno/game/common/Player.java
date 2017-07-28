package com.xeno.game.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.xeno.game.MainGame;

public class Player {

	public Vector2 position;
	private Rectangle bounds;
	private int id;
	
	public Player(int x, int y, int id) {
		this.id = id;
		
		position = new Vector2(x, y);
		bounds = new Rectangle(position.x, position.y, 32, 32);
	}
	
	public void move(int direction, float distance) {
		switch (direction) {
		case Direction.NORTH:
			position.y += distance;
			break;
		case Direction.SOUTH:
			position.y -= distance;
			break;
		case Direction.EAST:
			position.x += distance;
			break;
		case Direction.WEST:
			position.x -= distance;
			break;
		}
	}
	
	public void update(float delta) {
		bounds.setPosition(position);
	}
	
	public void draw(SpriteBatch batch) {
		if (MainGame.DEBUGGING) {
			ShapeRenderer renderer = new ShapeRenderer();
			renderer.setProjectionMatrix(batch.getProjectionMatrix());
			renderer.setAutoShapeType(true);
			
			renderer.begin();
			renderer.rect(position.x, position.y, bounds.width, bounds.height);
			renderer.end();
		}
	}
	
	public static Player getPlayerById(int id, ArrayList<Player> players) {
		Iterator<Player> iter = players.iterator();

		while (iter.hasNext()) {
		    Player p = iter.next();

		    if (p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	public static Player getPlayerById(int id, CopyOnWriteArrayList<Player> players) {
		Iterator<Player> iter = players.iterator();

		while (iter.hasNext()) {
		    Player p = iter.next();

		    if (p.getId() == id)
				return p;
		}
		
		return null;
	}
	
	public void setPosition(int x, int y) {
		position.set(x, y);
	}

	public int getX() {
		return (int) position.x;
	}

	public void setX(int x) {
		this.position.x = x;
	}

	public int getY() {
		return (int) position.y;
	}

	public void setY(int y) {
		this.position.y = y;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}