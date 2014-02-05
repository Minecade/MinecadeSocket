package com.minecade.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.minecade.MinecadeSocket;
import com.minecade.threads.SocketProcessThread;

public class SocketListener extends Thread {
	InetAddress lAddress;

	MinecadeSocket plugin = null;

	public SocketListener(MinecadeSocket as) { 
		plugin = as;
	}
	
	public void run() {
		ServerSocket ss = null;;
		try {

			lAddress = InetAddress.getByName(MinecadeSocket.IP);
			ss = new ServerSocket(MinecadeSocket.listen_port, 1000, lAddress);
			System.out.println("[MinecadeSocket] LISTENING on port " + MinecadeSocket.listen_port + "...");

			while(true){
				Socket clientSocket = ss.accept();
				String ip = clientSocket.getInetAddress().getHostAddress();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					System.out.println("[MinecadeSocket] (" + ip + ") Got packet: " + inputLine + " on " + clientSocket.getLocalPort());
					SocketProcessThread.pending_sockets.add(inputLine);
				}
				
				if(in != null){
					in.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR: Could not listen on port: " + MinecadeSocket.listen_port);
			return;
			
		} finally {
			if(ss != null){
				try {ss.close();} catch (IOException e) {e.printStackTrace();}
			}
		}

	}

}
