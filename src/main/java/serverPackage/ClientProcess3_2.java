package serverPackage;

import commonPackage.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProcess3_2 extends Thread {

    private static int compteurGlobal = 0;

    private Socket socket;
    private int numero;

    public ClientProcess3_2(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    private static synchronized int incrementerCompteur(int numeroClient) {
        compteurGlobal++;
        System.out.println(
                "[Compteur global] Opérations traitées : " + compteurGlobal
                        + " (dernière demandée par le client n°" + numeroClient + ")"
        );
        return compteurGlobal;
    }

    private void calculer(Operation op) {
        double a = op.getOperande1();
        double b = op.getOperande2();

        switch (op.getOperateur()) {
            case "+":
                op.setResultat(a + b);
                break;
            case "-":
                op.setResultat(a - b);
                break;
            case "*":
                op.setResultat(a * b);
                break;
            case "/":
                if (b == 0) {
                    op.setErreur("Division par zéro impossible");
                } else {
                    op.setResultat(a / b);
                }
                break;
            default:
                op.setErreur("Opérateur invalide : " + op.getOperateur());
        }
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();

            while (true) {
                Object recu = ois.readObject();

                if (recu instanceof String && recu.equals("quit")) {
                    break;
                }

                if (recu instanceof Operation) {
                    Operation op = (Operation) recu;

                    System.out.println(
                            "[Client n°" + numero + "] demande : " + op
                    );

                    calculer(op);

                    incrementerCompteur(numero);

                    oos.writeObject(op);
                    oos.flush();
                }
            }

            System.out.println("Client n°" + numero + " déconnecté");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Client n°" + numero + " : connexion terminée (" + e.getMessage() + ")"
            );

        } finally {

            try {
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