package etat;

public class Etat_disconnect implements Etat{
	Server serveur;

	public Etat_disconnect(Server serveur) {
		this.serveur = serveur; 
	}

	public void connect() {
		serveur.getEtatconnect();
		
	}

	public void disconnect() {
		System.out.println("Already disconnected");
		
	}

	public void join_game() {
		System.out.println("You've got to sign-in before");
		
	}

	public void leave_game() {
		System.out.println("You're not in a game yet");
		
	}
}