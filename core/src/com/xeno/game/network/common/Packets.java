package com.xeno.game.network.common;

import com.esotericsoftware.kryo.Kryo;

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
	
	public static void register(Kryo kryo) {
		kryo.register(Packets.GetClientId.class);
		kryo.register(Packets.AddPlayer.class);
		kryo.register(Packets.RemovePlayer.class);
	}
}