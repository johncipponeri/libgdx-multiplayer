package com.xeno.game.common;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.xeno.game.GameMap;
import com.xeno.game.common.entities.Entity;

public class Map {

	public String name;
	private int width;
	private int height;
	public TiledMap tiledMap;
	
	public ArrayList<Player> players;
	
	public Map() {
		this("null", "map.tmx");
	}
	
	public Map(String name, String filepath) {
		this.tiledMap = new TmxMapLoader().load("assets/" + filepath);
		this.name = name;
		width = tiledMap.getProperties().get("width", Integer.class);
	    height = tiledMap.getProperties().get("height", Integer.class);
	}
	
	
}
