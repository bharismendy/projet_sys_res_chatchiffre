package etat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.util.*;


class ClientHandler implements Runnable {
	Scanner scn=new Scanner(System.in);
	private int id;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isloggedin;
	String nom = "";

	public ClientHandler(Socket s, int id, DataInputStream dis, DataOutputStream dos) {
		this.dis=dis;
		this.dos=dos;
		this.id=id;
		this.s=s;
		this.isloggedin=true;
		this.nom = "client_"+id;
	}

	@Override
	public void run() {
			String received;

			while(true) {
				try {
					// receive the string
					received = dis.readUTF();
					//écriture du message côté serveur
					System.out.println("Message de "+this.nom+" : "+ received);
					this.broadCast("Message de "+this.nom+" : "+ received);

					if (received == "logout") {
						this.isloggedin=false;
						this.dos.writeUTF("bye !");
						this.s.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
			}
}
			try {
				// closing resources
				this.dis.close();
				this.dos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}

			Server.client_disconnect(id);

		}

	    public void anycast(String toSend, int idToSend) {
	    	for (int i = 0; i < Server.getAr().size(); i++) {
	    			try {
	    				Server.getAr().get(idToSend).dos.writeUTF(toSend);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
			}
	    public void broadCast(String toSend) {
	    	for (int i = 0; i < Server.getAr().size(); i++) {
				try {
					Server.getAr().get(i).dos.writeUTF(toSend);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

}