package com.xeno.game.network.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.xeno.game.MainGame;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;
import com.xeno.game.network.common.PlayerState;
import com.xeno.game.util.SystemTime;

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
		else if (o instanceof Packets.InputConfirmation)
			handleInputConfirmation((Packets.InputConfirmation) o);
		else if (o instanceof Packets.UpdatePlayer)
			handleUpdatePlayer((Packets.UpdatePlayer) o);
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
		newPlayer.SimulationDelay = 200;
		client.game.players.add(newPlayer);
		
		if (packet.id == client.getId()) {
			client.game.player = newPlayer;
			client.game.player.SimulationDelay = 50;
		}
		
		if (MainGame.DEBUGGING) {
			System.out.println(
				"Received Packet: AddPlayer\n" +
				"x: " + packet.x + "\n" +
				"y: " + packet.y + "\n" +
				"id: " + packet.id + "\n"
			);
		}
	}
	
	private void handleInputConfirmation(Packets.InputConfirmation packet) {
		long time = packet.stateTime;

		PlayerState state = new PlayerState();
        state = packet.state;

        client.game.player.SetState(time, state, true);

        client.game.player.ValidateInput(time);
	}
	
	private void handleUpdatePlayer(Packets.UpdatePlayer packet)
	{
		int id = packet.id;

        Player player = Player.getPlayerById(id, client.game.players);

        // TODO: Send request for creation?
        if (player == null)
            return;

        PlayerState state = new PlayerState();
        state = packet.state;
        
        player.SetState(SystemTime.CurrentFrozenTimeMS(), state, true);
        System.out.println("I tried");
	}
}