package com.xeno.game.network.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.xeno.game.network.common.PlayerInputState;

public class PlayerInput {
	
	public PlayerInput() {
		
	}
	
	public PlayerInputState Input() {	
		
		PlayerInputState _input = new PlayerInputState();
		
		_input.upPressed = Gdx.input.isKeyPressed(Keys.UP);
		_input.rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
		_input.downPressed = Gdx.input.isKeyPressed(Keys.DOWN);
		_input.leftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
		
		return _input;
	}
}
