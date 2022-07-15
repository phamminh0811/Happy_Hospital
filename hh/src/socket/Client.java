package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Path recvMsg = null;
	Socket socket = null;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	public Client(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket("localhost", 3100);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	public void sendMessage(Message msg) throws IOException {
		oos.writeObject(msg);		
	}	
	public Path recvMessage() throws ClassNotFoundException, IOException {
		return (Path)ois.readObject();
	}
	public void close() throws IOException {
		this.ois.close();
		this.oos.close();
		this.socket.close();
	}
}