package com.minecade.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.minecade.MinecadeSocket;

public class SocketProcessThread extends Thread {

	public static CopyOnWriteArrayList<String> pending_sockets = new CopyOnWriteArrayList<String>();
	// Sockets pending processing. 

	@Override
	public void run() {
		for(String data : pending_sockets){
			try {

				if(data.startsWith("runcmd:")){
					// runcmd:wget google.com
					data = data.replace("runcmd:", "");
					String cmd = data;

					// Execute the command on all remote game servers.
					Runtime.getRuntime().exec(cmd);
					return;
				}	
				
				if(data.startsWith("getupdate")){
					// Get newest version of our software.
					if(new File("getupdate.sh").exists()){
						MinecadeSocket.listener.interrupt();
						Runtime.getRuntime().exec("chmod +x getupdate.sh");
						Process p = Runtime.getRuntime().exec("sh getupdate.sh");
						p.waitFor();
						System.exit(0);
					}
				}

			} catch(Exception err){
				err.printStackTrace();
				pending_sockets.remove(data);
			}

			pending_sockets.remove(data);
		}
	}
}
