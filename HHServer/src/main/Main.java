package main;
import java.io.IOException;

import socket.HHServer;

public class Main {
	public static void main(String[] args) throws IOException {
		HHServer server = new HHServer(3100);
		server.start();
		server.close();
	}
}