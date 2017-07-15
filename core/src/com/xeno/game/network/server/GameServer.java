package com.xeno.game.network.server;

import java.io.IOException;

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
import com.xeno.game.network.common.Packets;

public class GameServer extends ApplicationAdapter {

	private Server server;
	
	public GameServer() throws IOException {
		this(Network.SERVER_TCP_PORT, Network.SERVER_UDP_PORT);
	}
	
	public GameServer(int tcpPort, int udpPort) throws IOException {
		server = new Server();
		
		register(server);
		
		server.bind(tcpPort, udpPort);
		
		server.addListener(new PacketHandler(this));
	}
	
	private void register(EndPoint endPoint) {
		Kryo kyro = endPoint.getKryo();
		
		kyro.register(Packets.GetId.class);
	}
	
	public void sendGetId(Connection c, Packets.GetId packet) {
		server.sendToTCP(c.getID(), packet);
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