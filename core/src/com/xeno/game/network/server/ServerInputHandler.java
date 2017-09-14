package com.xeno.game.network.server;

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
			System.out.println("/warp playername mapid");
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
		System.out.println("Warping " + name + " to map #" + mapID);
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
