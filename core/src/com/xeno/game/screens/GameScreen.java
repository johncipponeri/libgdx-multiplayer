package com.xeno.game.screens;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.xeno.game.GameMap;
import com.xeno.game.MainGame;
import com.xeno.game.common.Direction;
import com.xeno.game.common.Player;
import com.xeno.game.network.client.GameClient;

public class GameScreen extends ScreenAdapter {

	private GameClient client;
	private MainGame game;
	
	public CopyOnWriteArrayList<Player> players;
	public Player player;
	
	public OrthographicCamera camera;
	
	private GameMap map;
	
	private SpriteBatch batch;
	
	public GameScreen(MainGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        
		batch = new SpriteBatch();
		
		map = new GameMap("map.tmx", batch);
		
		players = new CopyOnWriteArrayList<Player>();
		
		try {
			client = new GameClient(this);
			client.run();
		} catch (Exception e) {
		}
	}
	
	public void update(float delta) {
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			client.sendMove(Direction.WEST, 300 * delta);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			client.sendMove(Direction.EAST, 300 * delta);
		if (Gdx.input.isKeyPressed(Keys.UP))
			client.sendMove(Direction.NORTH, 300 * delta);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			client.sendMove(Direction.SOUTH, 300 * delta);
		
		camera.position.set(player.getX(), player.getY(), 0);
		
		// Calculate once
		float cameraHalfWidth = camera.viewportWidth / 2;
		float cameraHalfHeight = camera.viewportHeight / 2;
		
		camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, map.getWidth() - cameraHalfWidth);
		camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, map.getHeight() - cameraHalfHeight);
		
		camera.update();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		if (player == null) return;
		
		update(delta);
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		map.draw(camera);
		
		batch.begin();
		
		for (Player p : players)
			p.draw(batch);
		
		batch.end();
	}
	
	@Override
	public void dispose() {
		client.sendRemovePlayer();
		
		client = null;
		players.clear();
		players = null;
		camera = null;
		map.dispose();
		batch.dispose();
		
		super.dispose();
	}
}