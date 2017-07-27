package com.xeno.game.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.MainGame;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;

public class PacketHandler extends Listener {

	private GameClient client;
	
	public PacketHandler(GameClient client) {
		this.client = client;
	}
	
	@Override
	public void received(Connection c, Object o) {
		super.received(c, o);
		
		if (o instanceof Packets.GetClientId)
			handleGetClientId((Packets.GetClientId) o);
		else if (o instanceof Packets.AddPlayer)
			handleAddPlayer((Packets.AddPlayer) o);
		else if (o instanceof Packets.RemovePlayer)
			handleRemovePlayer((Packets.RemovePlayer) o);
	}
	
	private void handleGetClientId(Packets.GetClientId packet) {
		client.setId(packet.id);
		client.sendAddPlayer();
		
		if (MainGame.DEBUGGING) {
			System.out.println(
				"Received Packet: GetClientId\n" +
				"id: " + packet.id
			);
		}	
	}
	
	private void handleAddPlayer(Packets.AddPlayer packet) {
		Player newPlayer = new Player(packet.x, packet.y, packet.id);
		client.game.players.add(newPlayer);
		
		if (packet.id == client.getId())
			client.game.player = newPlayer;
		
		if (MainGame.DEBUGGING) {
			System.out.println(
				"Received Packet: AddPlayer\n" +
				"x: " + packet.x + "\n" +
				"y: " + packet.y + "\n" +
				"id: " + packet.id + "\n"
			);
		}
	}
	
	private void handleRemovePlayer(Packets.RemovePlayer packet) {
		Player player = Player.getPlayerById(packet.id, client.game.players);
		
		client.game.players.remove(player);
		
		if (MainGame.DEBUGGING) {
			System.out.println(
				"Received Packet: RemovePlayer\n" +
				"id: " + packet.id + "\n"
			);
		}
	}
}