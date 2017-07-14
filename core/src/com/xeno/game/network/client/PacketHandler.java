package com.xeno.game.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.network.common.Packets;

public class PacketHandler extends Listener {

	private GameClient client;
	
	public PacketHandler(GameClient client) {
		this.client = client;
	}
	
	@Override
	public void received(Connection c, Object o) {
		super.received(c, o);
		
		if (o instanceof Packets.GetId)
			handleGetId((Packets.GetId) o);
	}
	
	private void handleGetId(Packets.GetId packet) {
		client.setId(packet.id);
		
		System.out.println(
			"Received Packet: GetId\n" +
			"id: " + packet.id
		);
	}
}