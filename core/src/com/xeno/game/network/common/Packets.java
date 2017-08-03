package com.xeno.game.network.common;

public class Packets {

	public static class GetClientId {
		public int id;
	}
	
	public static class AddPlayer {
		public int x, y, id;
	}
	
	public static class SendInput {
		public PlayerInputState input;
	}
	
	public static class InputConfirmation {
		public long stateTime;
		public PlayerState state;
	}
	
	public static class UpdatePlayer {
		public int id;
		public PlayerState state;
	}
}