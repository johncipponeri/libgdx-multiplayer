package com.xeno.game.network.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.mockito.Mockito;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import com.xeno.game.common.Map;
import com.xeno.game.common.Network;
import com.xeno.game.common.Player;
import com.xeno.game.network.common.Packets;
import com.xeno.game.network.common.PlayerInputState;
import com.xeno.game.network.common.PlayerState;

public class GameServer extends ApplicationAdapter {

	private Server server;
	
	public ArrayList<Map> maps;
	// public ArrayList<Player> players;
	
	public GameServer() throws IOException {
		this(Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT);
	}
	
	public GameServer(int tcpPort, int udpPort) throws IOException {
		server = new Server();
		
		maps = new ArrayList<Map>();
		// players = new ArrayList<Player>();
		
		register(server);
		
		server.bind(tcpPort, udpPort);
		
		server.addListener(new PacketHandler(this));
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
		kryo.register(Packets.RemovePlayer.class);
	}
	
	public void sendGetClientId(Connection c, Packets.GetClientId packet) {
		server.sendToTCP(c.getID(), packet);
	}
	
	public void sendAddPlayer(Connection c, int mapID, Packets.AddPlayer packet) {
		sendAddPlayer(c.getID(), mapID, packet);
	}
	
	public void sendAddPlayer(int cID, int mapID, Packets.AddPlayer packet) {
		// verify mapID
		// Packet to contain existing player info
		Packets.AddPlayer packetExisting = new Packets.AddPlayer();
		
		// Send information to everyone
		for (Player p : maps.get(mapID).getPlayerArrayList()) {
			if (p.getId() != cID) {
				// Send new player to existing players
				server.sendToTCP(p.getId(), packet);
				
				// Send this existing player to new player
				packetExisting.x = p.getX();
				packetExisting.y = p.getY();
				packetExisting.id = p.getId();

				server.sendToTCP(cID, packetExisting);
			} else {
				// Send new player to themselves
				server.sendToTCP(cID, packet);
			}
		}
	}
	
	public void SendInputConfirmation(Connection c, long stateTime, Player player)
    {
        Packets.InputConfirmation packet = new Packets.InputConfirmation();
       
        packet.stateTime = stateTime;
        packet.state = player.GetLatestState();

        server.sendToTCP(c.getID(), packet);
    }
	
	public void sendUpdatePlayerToAll(Player player, int mapID) {
		for (Player p : maps.get(mapID).getPlayerArrayList())
		{
		  	sendUpdatePlayer(p.getId(), player);
		}
	}
	
	public void sendUpdatePlayer(int id, Player player) {
		if (!player.Ready())
            return;

        if (player.getId() == id)
            return;

        Packets.UpdatePlayer packet = new Packets.UpdatePlayer();
        
        packet.id = player.getId();
        packet.state = player.GetLatestState();

        server.sendToTCP(id, packet);
	
	}
	
	public void sendRemovePlayer(Connection c, Packets.RemovePlayer packet) {
		server.sendToAllExceptTCP(c.getID(), packet);
	}
	
	public void sendRemovePlayer(int id, int mapID, Packets.RemovePlayer packet) {
		for (Player p : maps.get(mapID).getPlayerArrayList()) {
			server.sendToTCP(id, packet);
		}
	}
	
	private void loadMaps()
	{
		maps.add(new Map("Test Map!", "map.tmx"));
		System.out.println("Added 'Test Map!'");
		maps.add(new Map("Test Map 2!", "map.tmx"));
		System.out.println("Added 'Test Map!'");
	}
	
	public void run() {
		System.out.println("Server starting ...");
		
		server.start();
		
		System.out.println("Loading Maps ...");
		
		loadMaps();
		
		System.out.println("Maps Loaded");
		
		// start server loop
		System.out.println("Server started!");
		
		Scanner consoleInput = new Scanner(System.in);
		ServerInputHandler commandHandler = new ServerInputHandler(this);
		
		while(true) {
			String input = consoleInput.nextLine();
	        
			if (!input.isEmpty()) {
	            commandHandler.handleInput(input);
	        }
		}
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