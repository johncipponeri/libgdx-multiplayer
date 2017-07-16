package com.xeno.game.common;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.xeno.game.MainGame;

public class Player {

	public Vector2 position;
	private Rectangle bounds;
	
	public Player(int x, int y) {
		this.position = new Vector2(x, y);
		
		bounds = new Rectangle(position.x, position.y, 32, 32);
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
}