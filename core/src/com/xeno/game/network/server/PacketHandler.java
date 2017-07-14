package com.xeno.game.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.network.common.Packets;

public class PacketHandler extends Listener {

	private GameServer server;
	
	public PacketHandler(GameServer server) { 
		this.server = server;
	}
	
	@Override
	public void received(Connection c, Object o) {
		super.received(c, o);
		
		if (o instanceof Packets.GetId)
			handleGetId(c, (Packets.GetId) o);
	}
	
	private void handleGetId(Connection c, Packets.GetId packet) {
		packet.id = c.getID();
		server.sendGetId(c, packet);
	}
	
	@Override
	public void disconnected(Connection c) {
		super.disconnected(c);
	}
}