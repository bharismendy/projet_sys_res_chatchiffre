import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	final static int ServerPort=8888;

	public Client() {}

	public static void main(String args[]) throws UnknownHostException, IOException {
		final Scanner scn=new Scanner(System.in);

		// getting localhost ip
		InetAddress ip=InetAddress.getByName("localhost");

		// establish the connection
		Socket s=new Socket(ip, ServerPort);

		// obtaining input and zdout streams
		final DataInputStream dis=new DataInputStream(s.getInputStream());
		final DataOutputStream dos=new DataOutputStream(s.getOutputStream());

		// sendMessage thread
		Thread sendMessage=new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// read the message to deliver.
					String msg=scn.nextLine();

					try {
						// write on the output stream
						dos.writeUTF(msg);
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
						// read the message sent to this client
						String msg=dis.readUTF();
						System.out.println(msg);
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
			System.err.println("read join");
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