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
				System.out.println("Envoie de la clé public du serveur au client (6 premiers caractères) : " + this.serverPublicKey.substring(0,6)+ "...");
				// Envoie la clé public du serveur
				dos.writeUTF(this.serverPublicKey);
				// Récupère la clé public du client
				System.out.println("Récupération de la clé public du client");
				this.clientPublicKey = dis.readUTF();
				System.out.println("Clé récupérée (6 premiers caractères) : " + this.clientPublicKey.substring(0,6)+ "...");
				while(true) {
					System.out.println("------------");
					// receive the string
					received = dis.readUTF();
					System.out.println("Message crypté récupéré (6 premiers caractères) : " + received.substring(0,6)+ "...");
					String message = SecurityManager.decrypt(received, this.serverPrivateKey );
					System.out.println("Message de "+this.nom+" décrypté : " + message);
					//écriture du message côté serveur
					this.broadCast(message);

					if (message.equals("logout")) {
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
					ClientHandler client = Server.getAr().get(i);
					System.out.println("Client "+client.nom+", clé : "+client.clientPublicKey.substring(0,6)+ "...");
					String messageEncrypt = SecurityManager.encrypt("Message de "+this.nom+" : "+ toSend,client.clientPublicKey);
					System.out.println("Cryptage et envoie du message pour le client "+client.nom+" (6 premiers caractères) : " + messageEncrypt.substring(0,6)+ "...");
					client.dos.writeUTF(messageEncrypt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

}