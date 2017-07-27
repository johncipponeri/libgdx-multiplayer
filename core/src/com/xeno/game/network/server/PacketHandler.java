package com.xeno.game.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;

public class PacketHandler extends Listener {

	private GameServer server;
	
	public PacketHandler(GameServer server) { 
		this.server = server;
	}
	
	@Override
	public void received(Connection c, Object o) {
		super.received(c, o);
		
		if (o instanceof Packets.GetClientId)
			handleGetClientId(c, (Packets.GetClientId) o);
		else if (o instanceof Packets.AddPlayer)
			handleAddPlayer(c, (Packets.AddPlayer) o);
		else if (o instanceof Packets.RemovePlayer)
			handleRemovePlayer(c, (Packets.RemovePlayer) o);
	}
	
	private void handleGetClientId(Connection c, Packets.GetClientId packet) {
		packet.id = c.getID();
		server.sendGetClientId(c, packet);
	}
	
	private void handleAddPlayer(Connection c, Packets.AddPlayer packet) {
		packet.id = c.getID();
		
		// Testing
		packet.x = 5;
		packet.y = 5;
		
		server.players.add(new Player(packet.x, packet.y, packet.id));
		server.sendAddPlayer(c, packet);
		
		System.out.println("Connection #" + packet.id + " connected!");
	}
	
	private void handleRemovePlayer(Connection c, Packets.RemovePlayer packet) {
		packet.id = c.getID();
		
		Player player = Player.getPlayerById(packet.id, server.players);
		
		server.players.remove(player);
		server.sendRemovePlayer(c, packet);
		
		System.out.println("Connection #" + packet.id + " disconnected!");
	}
	
	@Override
	public void disconnected(Connection c) {
		super.disconnected(c);
	}
}