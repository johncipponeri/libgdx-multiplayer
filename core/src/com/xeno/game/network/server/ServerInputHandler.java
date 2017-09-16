package com.xeno.game.network.server;

import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;

public class ServerInputHandler {
	
	private GameServer server;
	
	public ServerInputHandler(GameServer server)
	{
		this.server = server;
	}
	
	public void handleInput(String input) {
		String[] command = input.split(" ");
		
		if (command[0].startsWith("/"))
			command[0] = command[0].substring(1);
		else
			System.out.println("Please enter a valid command. \nType '/commands' to view available commands.");
		
		switch(command[0]) {
			case "warp":
				handleWarp(command);
				break;
			default: 
				System.out.println("Command " + command[0] + " does not exist.");
		}
	}
	
	public void handleWarp(String[] input) {
		
		if (input.length == 1) {
			System.out.println("Usage: /warp playername mapid");
			return;
		}
		if (input.length != 3 && input.length != 1) {
			System.out.println("Invalid amount of arguments.\nArguments Expected: " + "3\n" + "Arguments given: " + input.length);
			return;
		}
		
		if(isParsable(input[2]))
			warp(input[1], Integer.parseInt(input[2]));
		else
			System.out.println(input[2] + " is not a Map ID.");
	}
	
	public void warp(String name, int mapID) {
		Player p = Player.getPlayerById2(Integer.parseInt(name), server.maps);
		
		int oldMap = p.getMapID();
		
		Packets.RemovePlayer packet = new Packets.RemovePlayer();
		packet.id = p.getId();
		
		server.maps.get(oldMap).removePlayer(p);
		server.maps.get(mapID).addPlayer(p);
		p.setMapID(mapID);
		
		for (Player r : server.maps.get(oldMap).getPlayerArrayList()) {
			server.sendRemovePlayer(r.getId(), mapID, packet);
		}
		
		Packets.AddPlayer pack = new Packets.AddPlayer();
		pack.id = p.getId();
		pack.x = p.getX();
		pack.y = p.getY();
		
		for (Player pl : server.maps.get(mapID).getPlayerArrayList()) {
			server.sendAddPlayer(pack.id, mapID, pack);
		}
		
		System.out.println("Warped " + name + " to map #" + mapID);
	}
	
	public boolean isParsable(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
}
