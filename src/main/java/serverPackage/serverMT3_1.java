package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverMT3_1 {

    static final int PORT = 1234;

    public static void main(String[] args) {

        int numero = 0; // Numéro d'ordre de connexion

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            System.out.println("Le serveur attend la connexion d'un client...");

            while (true) {
                // Attendre la connexion d'un client
                Socket socket = serverSocket.accept();

                numero++;

                // Afficher l'adresse IP et le numéro du client
                System.out.println(
                        "Client n°" + numero
                                + " connecté, adresse : "
                                + socket.getRemoteSocketAddress()
                );

                // Créer un thread dédié à ce client
                new ClientProcess3_1(socket, numero).start();
            }

        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }
}
