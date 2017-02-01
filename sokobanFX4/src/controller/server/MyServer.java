package controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class MyServer extends Observable implements Observer,Server {
	
	private int port;
	private ClientHandler ch;
	private volatile boolean stop = false;
	private ServerSocket server = null;
	//private boolean online;
	
	public MyServer(int port, ClientHandler ch){
		this.port = port;
		this.ch = ch;
		stop = false;
	}
	@Override
	public void runServer() throws IOException{
		System.out.println("running the server");
		//ServerSocket server = null;
		try{
		server = new ServerSocket(port);
		} catch(Exception e) {System.out.println(e.getMessage()); }
		
		if(server != null){
			System.out.println("listening on port number " + port);
			Thread t = new Thread(new Runnable(){
				public void run(){
	
					while(!stop){
						System.out.println("waiting for client");
						Socket aClient;
						try {
							if(server.isClosed() == false)
							{
								aClient = server.accept();
								System.out.println("a client connected to the server");	
								ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
		
								aClient.close();
								System.out.println("client disconnected");
							}
						} catch(Exception e) {System.out.println(e.getMessage());}
					}
				}
			});	
			t.start();
		}
	}
	@Override
	public void stop() {
		stop = true;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnectUser();
		System.out.println("closing the server"); 
	}
	@Override
	public void sendMsgToClient(String msg){
		try {
			ch.msgToClient(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);		
	}
	@Override
	public ClientHandler getHandler() {
		return ch;
	}
	@Override
	public void disconnectUser() {
		ch.disconnectUser();		
	}
}
