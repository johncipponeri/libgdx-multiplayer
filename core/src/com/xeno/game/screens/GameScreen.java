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
import com.xeno.game.common.Player;
import com.xeno.game.network.client.GameClient;
import com.xeno.game.network.client.PlayerInput;
import com.xeno.game.network.common.PlayerInputState;
import com.xeno.game.util.SystemTime;

public class GameScreen extends ScreenAdapter {

	private GameClient client;
	private MainGame game;
	
	public CopyOnWriteArrayList<Player> players;
	public Player player;
	
	public OrthographicCamera camera;
	
	private GameMap map;
	
	private SpriteBatch batch;
	
	private PlayerInput playerInput;
	
	private SystemTime time;
	
	public GameScreen(MainGame game) {
		this.game = game;
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        
		batch = new SpriteBatch();
		
		map = new GameMap("map.tmx", batch);
		
		players = new CopyOnWriteArrayList<Player>();
		playerInput = new PlayerInput();
		
		time = new SystemTime();
		
		try {
			client = new GameClient(this);
			client.run();
		} catch (Exception e) {
		}
	}
	
	float _updateTimer;
	
	public void update(float delta) {
		time.StartUpdate();
		
		PlayerInputState input = playerInput.Input();
		
		System.out.println(time.CurrentFrozenTimeMS());
		
		_updateTimer += delta;
        if ((_updateTimer % 1) * 1000 > 50)
        {
        	player.QueueInputPrediction(input);
    		player.UpdateState(input, SystemTime.CurrentFrozenTimeMS(), false, false);
    		client.sendInput(input);
    		
            _updateTimer = 0;
        }

		player.UpdateFrame(delta);
		
		camera.position.set(player.GetPosition().x, player.GetPosition().y, 0);
		
		// Calculate once
		float cameraHalfWidth = camera.viewportWidth / 2;
		float cameraHalfHeight = camera.viewportHeight / 2;
		
		camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, map.getWidth() - cameraHalfWidth);
		camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, map.getHeight() - cameraHalfHeight);
		
		camera.update();
		
		time.EndUpdate();
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
		client = null;
		players.clear();
		players = null;
		camera = null;
		map.dispose();
		batch.dispose();
		
		super.dispose();
	}
}