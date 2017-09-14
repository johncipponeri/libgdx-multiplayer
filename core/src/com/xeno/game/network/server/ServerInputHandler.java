package com.xeno.game.network.server;

public class ServerInputHandler {
	
	private GameServer server;
	
	public ServerInputHandler(GameServer server)
	{
		this.server = server;
	}
	
	public void handleInput(String input) {
		String[] command = input.split(" ");
		
		switch(command[0]) {
			case "/warp":
				warp(command[1], Integer.parseInt(command[2]));
				break;
			default: 
				System.out.println("Command " + command[0] + " does not exist.");
		}
	}
	
	public void warp(String name, int mapID) {
		System.out.println("Warping " + name + " to map #" + mapID);
	}
}
