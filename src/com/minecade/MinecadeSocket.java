package com.minecade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.minecade.listeners.SocketListener;
import com.minecade.threads.SocketProcessThread;

public class MinecadeSocket {
	public static final int listen_port = 9001;
	public static final String IP = getIp();
	
	public static MinecadeSocket as = null;
	public static SocketListener listener = null;
	public static SocketProcessThread spt = null;
	
	public MinecadeSocket(){
		as = this;
	}
	
	public static void main(String[] args){
		// try {loadConfig();} catch (IOException e) {e.printStackTrace();}
		listener = new SocketListener(as);
		listener.start();
		spt = new SocketProcessThread();
		spt.start();
	}
	
	public static String getIp() {
		try{
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(
						whatismyip.openStream()));
				String ip = in.readLine();
				return ip;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(Exception err){
			err.printStackTrace();
		}

		return null;
	}
}
