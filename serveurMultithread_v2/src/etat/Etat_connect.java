package etat;

public class Etat_connect implements Etat {
	Server serveur;
	
	public Etat_connect(Server serveur){
		this.serveur = serveur;
	}

	public void connect() {
		System.out.println("Vous êtes déjà connecté");
		
	}

	public void disconnect() {
		serveur.getEtatdisconnect();
	}
}