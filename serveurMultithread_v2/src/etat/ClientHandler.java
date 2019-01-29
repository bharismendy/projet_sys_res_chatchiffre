package etat;
import Security.SecurityManager;

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
	String serverPublicKey;
	String serverPrivateKey;
	String clientPublicKey;

	public ClientHandler(Socket s, int id, DataInputStream dis, DataOutputStream dos, String publicKey, String privateKey) {
		this.dis=dis;
		this.dos=dos;
		this.id=id;
		this.s=s;
		this.isloggedin=true;
		this.nom = "client_"+id;
		this.serverPublicKey = publicKey;
		this.serverPrivateKey = privateKey;
	}

	@Override
	public void run() {
			String received;

			try {
				// Envoie la clé public du serveur
				dos.writeUTF(this.serverPublicKey);
				// Récupère la clé public du client
				this.clientPublicKey = dis.readUTF();
				while(true) {
					// receive the string
					received = SecurityManager.decrypt(dis.readUTF(), this.serverPrivateKey );
					//écriture du message côté serveur
					System.out.println("Message de "+this.nom+" : "+ received);
					this.broadCast(SecurityManager.encrypt("Message de "+this.nom+" : "+ received,this.clientPublicKey));

					if (received.equals("logout")) {
						this.isloggedin=false;
						this.dos.writeUTF(SecurityManager.encrypt("bye !",this.clientPublicKey));
						this.s.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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