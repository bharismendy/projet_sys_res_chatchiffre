package etat;

import Security.KeyManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
	// HasMap to store active clients by pseudo_user
    private static HashMap<Integer, ClientHandler> ar=new HashMap<Integer, ClientHandler>();
    // counter for clients
    private static int i=0;

	Etat etat_connect;
	Etat etat_disconnect;
	Etat etat = etat_disconnect;

	public Server(){
		etat_connect = new Etat_connect(this);
		etat_disconnect = new Etat_disconnect(this);
	}

    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(8888, 0, InetAddress.getByName("localhost"));
        System.out.println("running");
		KeyManager keyManager = new KeyManager();
		System.out.println("public : "+keyManager.getPublicKey());
		System.out.println("private : "+keyManager.getPrivateKey());
        Socket s;

        // running infinite loop for getting
        // client request
        while(true) {
            // Accept the incoming request
            s=ss.accept();
            System.out.println("New client request received : "+s);

            // obtain input and output streams
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dos=new DataOutputStream(s.getOutputStream());

            // Create a new handler object for handling this request.
            System.out.println("Creating a new handler for this client...");
            ClientHandler mtch=new ClientHandler(s, i, dis, dos, keyManager.getPublicKey(), keyManager.getPrivateKey());

            // Create a new Thread with this object.
            Thread t=new Thread(mtch);

            // add this client to active clients list
            System.out.println("Adding this client to active client list");
            ar.put(i, mtch);
            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
        }
    }

    public static void client_disconnect(int id) {
    	ar.remove(id);
    	Server.i--;
    }

	public void connect(){
		etat.connect();
	}
	public void disconnect(){
		etat.disconnect();
	}

	public void setEtat(Etat etat){
		this.etat = etat;
	}

	public void getEtatconnect(){
		setEtat(etat_connect);
	}

	public void getEtatdisconnect(){
		setEtat(etat_disconnect);
	}
	public static HashMap<Integer, ClientHandler> getAr() {
		return ar;
	}
}