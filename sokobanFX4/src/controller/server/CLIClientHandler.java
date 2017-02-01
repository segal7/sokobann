package controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import controller.commands.Command;

public class CLIClientHandler extends Observable implements ClientHandler {

	private BlockingQueue<String> msg_queue;
	private volatile boolean disconnect;
	
	public CLIClientHandler() {
		msg_queue = new ArrayBlockingQueue<String>(20);
	}
	
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		try{
	
		Thread rcvThread = recieveClientMsg(inFromClient); rcvThread.start();
		Thread sendThread = deliverMsgToClient(outToClient); sendThread.start();

		rcvThread.join(); sendThread.join();

		}
		catch(Exception e) { e.printStackTrace(); }
	}
	@Override
	public void msgToClient(String msg) throws InterruptedException{
		msg_queue.put(msg);
	}
	
	
	private Thread deliverMsgToClient(OutputStream outToClient){
		
		PrintWriter outputToClient = new PrintWriter(new OutputStreamWriter(outToClient));
		Thread t = new Thread(new Runnable(){
			public void run(){
				String msg = "Exit";
				do
				{
					try {
						msg = msg_queue.take();
						outputToClient.println(msg);
						System.out.println(msg);
						outputToClient.flush();
					} catch (InterruptedException e) { System.out.println(e.getMessage()); }
					
				}while(!msg.equals("Exit") && disconnect!=false);
				if(disconnect == true){
					System.out.println("disconnecting user");
					outputToClient.println("server going down. bye bye.");
					outputToClient.println("Exit");
					outputToClient.flush();
				}
				
			}
		});

		return t;
	}
	private Thread recieveClientMsg(InputStream inFromClient){
		
		BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(inFromClient));

		Thread t = new Thread(new Runnable(){
			public void run(){
				try{
					String msg = "";
					while(!msg.equals("Exit")){
						try {						
							msg = inputFromClient.readLine();
							System.out.println(msg);
							LinkedList<String> cmd = new LinkedList<String>();
							for(String o : msg.split(" "))
								cmd.add(o);
							setChanged();
							notifyObservers(cmd);
						} catch (IOException e) { e.printStackTrace(); }
				
					};
					
				}catch(Exception e) {System.out.println(e.getMessage()); }
			}
		});

		return t;
	}

	@Override
	public void disconnectUser() {
		this.disconnect = true;
	}

}
