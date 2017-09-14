package com.xeno.game.network.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;
import com.xeno.game.network.common.PlayerInputState;

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
		else if (o instanceof Packets.SendInput)
			handleSendInput(c, (Packets.SendInput) o);
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
		
		Player p = new Player(packet.x, packet.y, packet.id);
		
		server.maps.get(p.getMapID()).addPlayer(p);
		server.sendAddPlayer(c, p.getMapID(), packet);
		
		System.out.println("Connection #" + packet.id + " connected!");
	}
	
	private void handleSendInput(Connection c, Packets.SendInput packet) {
		
		Player player = Player.getPlayerById2(c.getID(), server.maps);
		if (player == null)
			return;
		
		PlayerInputState input = new PlayerInputState();
		input = packet.input;
		
		player.UpdateState(input);
		
		server.SendInputConfirmation(c, input.Identifier, player);
				
		server.sendUpdatePlayerToAll(player, player.getMapID());
	}
	
	private void handleRemovePlayer(Connection c, Packets.RemovePlayer packet) {
		
		packet.id = c.getID();
		
		Player r = Player.getPlayerById2(packet.id, server.maps);
		// server.players.remove(r);
		server.maps.get(r.getMapID()).removePlayer(r);
		
		server.sendRemovePlayer(c, packet);
	}
	
	@Override
	public void disconnected(Connection c) {
		super.disconnected(c);
	}
}