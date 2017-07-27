package com.xeno.game.network.common;

public class Packets {

	public static class GetClientId {
		public int id;
	}
	
	public static class AddPlayer {
		public int x, y, id;
	}
	
	public static class RemovePlayer {
		public int id;
	}
}