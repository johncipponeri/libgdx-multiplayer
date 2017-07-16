package com.xeno.game.screens;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.xeno.game.MainGame;
import com.xeno.game.common.Player;
import com.xeno.game.network.client.GameClient;

public class GameScreen extends ScreenAdapter {

	private GameClient client;
	private MainGame game;
	
	public CopyOnWriteArrayList<Player> players;
	private Player player;
	
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
		
		players = new CopyOnWriteArrayList<Player>();
		player = new Player(5 * 32, 5 * 32);
		
		try {
			client = new GameClient(this);
			client.run();
		} catch (Exception e) {
		}
	}
	
	public void update(float delta) {
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			player.setX((int) (player.getX() - 300 * delta));
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			player.setX((int) (player.getX() + 300 * delta));
		if (Gdx.input.isKeyPressed(Keys.UP))
			player.setY((int) (player.getY() + 300 * delta));
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			player.setY((int) (player.getY() - 300 * delta));
		
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		super.render(delta);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		batch.begin();
		
		player.draw(batch);
		
		batch.end();
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