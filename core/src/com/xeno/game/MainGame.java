package com.xeno.game;

import com.badlogic.gdx.Game;
import com.xeno.game.screens.GameScreen;

public class MainGame extends Game {
	
	public static final boolean DEBUGGING = true;
	
	@Override
	public void create() {
		setScreen(new GameScreen(this));
	}

	@Override
	public void dispose() {
		getScreen().dispose();
		
		super.dispose();
	}
}