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
	
	private void handleSendInput(Connection c, Packets.SendInput packet) {
		
		Player player = Player.getPlayerById(c.getID(), server.players);
		if (player == null)
			return;
		
		PlayerInputState input = new PlayerInputState();
		input = packet.input;
		
		player.UpdateState(input);
		
		server.SendInputConfirmation(c, input.Identifier, player);
				
		server.sendUpdatePlayerToAll(player);
	}
	
	@Override
	public void disconnected(Connection c) {
		super.disconnected(c);
	}
}