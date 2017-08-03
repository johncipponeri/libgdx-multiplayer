package com.xeno.game.network.client;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import com.xeno.game.common.Network;
import com.xeno.game.network.common.Packets;
import com.xeno.game.network.common.PlayerInputState;
import com.xeno.game.network.common.PlayerState;
import com.xeno.game.screens.GameScreen;

public class GameClient {

	public Client client;
	
	private String ipAddress;
	private int tcpPort, udpPort;
	public GameScreen game;
	private int id;
	
	public GameClient(GameScreen gameScreen) {
		this(Network.SERVER_IP, Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT, gameScreen);
	}
	
	public GameClient(String ipAddress, int tcpPort, int udpPort, GameScreen gameScreen) {
		client = new Client();
		
		this.ipAddress = ipAddress;
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.game = gameScreen;
		
		register(client);
	}
	
	private void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		
		kryo.register(Packets.GetClientId.class);
		kryo.register(Packets.AddPlayer.class);
		kryo.register(Packets.SendInput.class);
		kryo.register(PlayerInputState.class);
		kryo.register(PlayerState.class);
		kryo.register(Packets.InputConfirmation.class);
		kryo.register(Packets.UpdatePlayer.class);
	}
	
	public void run() throws IOException {
		client.start();
		client.addListener(new PacketHandler(this));
		client.connect(5000, ipAddress, tcpPort, udpPort);
		sendGetClientId();
	}
	
	public void sendGetClientId() {
		client.sendTCP(new Packets.GetClientId());
	}
	
	public void sendAddPlayer() {
		client.sendTCP(new Packets.AddPlayer());
	}
	
	public void sendInput(PlayerInputState input) {
		Packets.SendInput packet = new Packets.SendInput();
		
		packet.input = input;
		client.sendTCP(packet);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
