
package serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess3_1 extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess3_1(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter pw = new PrintWriter(
                    socket.getOutputStream(), true
            );

            // Envoyer le numéro d'ordre au client
            pw.println(
                    "Bienvenue, vous etes le client n" + numero
            );

            // Lire les messages envoyés par ce client
            String ligne = br.readLine();

            while (ligne != null && !ligne.equals("quit")) {

                System.out.println(
                        "[Client n" + numero + "] a envoye : " + ligne
                );

                // Renvoyer le message au client
                pw.println("Echo : " + ligne);

                ligne = br.readLine();
            }

            System.out.println(
                    "Client n" + numero + " deconnecte"
            );

        } catch (IOException e) {

            System.out.println(
                    "Erreur client n" + numero + " : " + e.getMessage()
            );

        } finally {

            try {
                // Chaque thread ferme sa propre socket
                socket.close();

            } catch (IOException e) {
                System.out.println(
                        "Erreur lors de la fermeture de la socket : "
                                + e.getMessage()
                );
            }
        }
    }
}
