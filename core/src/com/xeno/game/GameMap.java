package com.xeno.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	private int width, height;
	
	public GameMap(String name, SpriteBatch batch) {
		map = new TmxMapLoader().load(name);
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		
		width = map.getProperties().get("width", Integer.class) * 32;
		height = map.getProperties().get("height", Integer.class) * 32;
	}
	
	public void draw(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
	}
	
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
