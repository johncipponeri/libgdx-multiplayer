package com.xeno.game.network.server;

import java.io.IOException;
import java.util.ArrayList;

import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.xeno.game.common.Network;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;

public class GameServer extends ApplicationAdapter {

	private Server server;
	
	public ArrayList<Player> players;
	
	public GameServer() throws IOException {
		this(Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT);
	}
	
	public GameServer(int tcpPort, int udpPort) throws IOException {
		server = new Server();
		
		players = new ArrayList<Player>();
		
		register(server);
		
		server.bind(tcpPort, udpPort);
		
		server.addListener(new PacketHandler(this));
	}
	
	private void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		
		Packets.register(kryo);
	}
	
	public void sendGetClientId(Connection c, Packets.GetClientId packet) {
		server.sendToTCP(c.getID(), packet);
	}
	
	public void sendAddPlayer(Connection c, Packets.AddPlayer packet) {
		// Packet to contain existing player info
		Packets.AddPlayer packetExisting = new Packets.AddPlayer();
		
		// Send information to everyone
		for (Player p : players) {
			if (p.getId() != c.getID()) {
				// Send new player to existing players
				server.sendToTCP(p.getId(), packet);
				
				// Send this existing player to new player
				packetExisting.x = p.getX();
				packetExisting.y = p.getY();
				packetExisting.id = p.getId();

				server.sendToTCP(c.getID(), packetExisting);
			} else {
				// Send new player to themselves
				server.sendToTCP(c.getID(), packet);
			}
		}
	}
	
	public void sendRemovePlayer(Connection c, Packets.RemovePlayer packet) {
		server.sendToAllExceptTCP(packet.id, packet);
	}
	
	public void run() {
		System.out.println("Server starting ...");
		
		server.start();
		
		System.out.println("Server started!");
	}
	
	public static void printIntro() {
		String asciiLogo = String.join("\n"
				, "\\ \\/ /___ _ __   ___   / _\\ ___ _ ____   _____ _ __"
				, " \\  // _ \\ '_ \\ / _ \\  \\ \\ / _ \\ '__\\ \\ / / _ \\ '__|"
				, " /  \\  __/ | | | (_) | _\\ \\  __/ |   \\ V /  __/ |"
				, "/_/\\_\\___|_| |_|\\___/  \\__/\\___|_|    \\_/ \\___|_|"
        );
		
		System.out.println(asciiLogo + "\n");
	}
	
	public static void main(String[] args) {
		printIntro();
		
		try {
			GameServer server = new GameServer();
			
			Gdx.gl = Mockito.mock(GL20.class);
			
			new HeadlessApplication(server);
			
			server.run();
		} catch (IOException e) {
			System.out.println("Error: port already in use!");
		}
	}
}