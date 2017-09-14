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
	
	private ArrayList<Player> players;
	
	public Map() {
		this("null", "map.tmx");
	}
	
	public Map(String name, String filepath) {
		this.tiledMap = new TmxMapLoader().load("assets/" + filepath);
		this.name = name;
		width = tiledMap.getProperties().get("width", Integer.class);
	    height = tiledMap.getProperties().get("height", Integer.class);
	    
	    players = new ArrayList<Player>();
	}
	
	public ArrayList<Player> getPlayerArrayList() {
		return players;
		
	}
	
	public boolean hasPlayer(Player p)
	{
		if (players.contains(p))
			return true;
		else
			return false;
	}
	
	public boolean hasPlayer(int id)
	{
		for (Player p : players)
		{
			if (p.getId() == id)
				return true;
		}
		
		return false;
	}
	
	public Player getPlayer(int id)
	{
		for (Player p : players)
		{
			if (p.getId() == id)
				return p;
		}
		
		System.out.println("Player ID "+ id + " does not exist");
		
		return null;
	}
	
	public void addPlayer(Player p)
	{
		players.add(p);
	}
	
	public void removePlayer(Player p)
	{
		if (players.contains(p))
			players.remove(p);
		else
			System.out.println("No Player Exists on " + name);
	}
	
}
