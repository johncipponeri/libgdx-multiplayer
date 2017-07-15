package com.xeno.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.xeno.game.MainGame;
import com.xeno.game.network.client.GameClient;

public class GameScreen extends ScreenAdapter {

	private GameClient client;
	private MainGame game;
	
	public OrthographicCamera camera;
	
	private TiledMap map;
	private TiledMapRenderer mapRenderer;
	
	private SpriteBatch batch;
	
	public GameScreen(MainGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        
		batch = new SpriteBatch();
		
		map = new TmxMapLoader().load("map.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, batch);
		
		try {
			client = new GameClient(this);
			client.run();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	@Override
	public void dispose() {
		client = null;
		camera = null;
		map.dispose();
		mapRenderer = null;
		batch.dispose();
		
		super.dispose();
	}
}