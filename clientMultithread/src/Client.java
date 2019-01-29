import Security.KeyManager;
import Security.SecurityManager;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	final static int ServerPort=8888;

	public Client() {}

	public static void main(String args[]) throws UnknownHostException, IOException {
		final Scanner scn=new Scanner(System.in);

		// getting localhost ip
		System.out.print("\nRENTRER L'IP DU SERVEUR (localhost est accepté) : ");
		String ipUser = scn.nextLine();
		InetAddress ip=InetAddress.getByName(ipUser);

		// establish the connection
		Socket s=new Socket(ip, ServerPort);

		// obtaining input and zdout streams
		final DataInputStream dis=new DataInputStream(s.getInputStream());
		final DataOutputStream dos=new DataOutputStream(s.getOutputStream());

		final KeyManager keys = new KeyManager();
		System.out.println("public : "+keys.getPublicKey());
		System.out.println("private : "+keys.getPrivateKey());
		// Récupération de la clé publique du serveur
		System.out.println("Récupération de la clé public du serveur");
		final String serverPublicKey = dis.readUTF();
		System.out.println("Clé récupérée (6 premiers caractères) : " + serverPublicKey.substring(0,6)+ "...");
		// Envoie de la clé publique
		System.out.println("Envoie de ma clé public au serveur : " + keys.getPublicKey().substring(0,6)+ "...");
		dos.writeUTF(keys.getPublicKey());

		// sendMessage thread
		Thread sendMessage=new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// Encrypt the message to deliver.
					String message = scn.nextLine();
					System.out.println("Cryptage de : " + message);
					String messageEncrypt= SecurityManager.encrypt(message, serverPublicKey);
					System.out.println("Envoie du message crypté au serveur (6 premiers caractères) : " + messageEncrypt.substring(0,6)+ "...");

					try {
						// write on the output stream
						dos.writeUTF(messageEncrypt);
					} catch (IOException e) {
						System.err.println("connection lost while sending !");
						e.printStackTrace();
						break;
					}
				}
			}
		});

		// readMessage thread
		Thread readMessage=new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("------------");
						// read the message sent to this client
						String received = dis.readUTF();
						System.out.println("Message crypté récupéré (6 premiers caractères) : " + received.substring(0,6)+ "...");
						String msg=SecurityManager.decrypt(received, keys.getPrivateKey());
						System.out.println("Message décrypté : " + msg);
					} catch (IOException e) {
						System.err.println("connection lost while reading !");
						e.printStackTrace();
						break;
					}
				}
			}
		});

		try {
			sendMessage.start();
			readMessage.start();
			readMessage.join();
			s.close();
			System.err.println("send interrupt");
			sendMessage.interrupt();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("interrupt exception");
			e.printStackTrace();
		} finally {
			System.err.println("socket");
			s.close();
		}
		System.err.println("fini");
		System.exit(0);
	}
}