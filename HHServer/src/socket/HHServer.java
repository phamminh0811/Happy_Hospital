package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import algorithm.AStar;
import classes.Position;
import socket.Message;
import socket.Path;

public class HHServer {
	ServerSocket ss = null;
	public HHServer(int port) throws IOException {
		ss = new ServerSocket(port);
	}

	public void start() throws IOException {
		while (true) {
			System.out.println("Listening at 3100..");
			Socket socket = null;
			try {
				socket = ss.accept();
				System.err.println("Accept to " + socket.getRemoteSocketAddress());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Message msg = (Message) ois.readObject();
				ArrayList<Position> path = new AStar(msg.getWidth(), msg.getHeight(), msg.getStartPos(),
						msg.getEndPos(), msg.getGroundPos()).cal();
				System.out.println("path length: " + path.size());
				oos.writeObject(new Path(path));
				oos.close();
				ois.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			socket.close();
		}

	}

	public void close() throws IOException {
		ss.close();
	}
}