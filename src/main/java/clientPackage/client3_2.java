package clientPackage;

import commonPackage.Operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class client3_2 {

    static final String IP_SERVEUR = "192.168.1.208";
    static final int PORT = 1234;

    public static void main(String[] args) {

        try (Socket socket = new Socket();
             Scanner clavier = new Scanner(System.in)) {

            System.out.println("Je suis un client pas encore connecté...");

            socket.connect(
                    new InetSocketAddress(InetAddress.getByName(IP_SERVEUR), PORT),
                    5000
            );

            System.out.println("Je suis un client connecté.");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Serveur : " + ois.readObject());

            while (true) {
                System.out.print("Opérande 1 (quit pour arrêter) : ");
                String s1 = clavier.nextLine().trim();

                if (s1.equalsIgnoreCase("quit")) {
                    oos.writeObject("quit");
                    oos.flush();
                    break;
                }

                System.out.print("Opérateur (+ - * /) : ");
                String operateur = clavier.nextLine().trim();

                System.out.print("Opérande 2 : ");
                String s2 = clavier.nextLine().trim();

                double a;
                double b;
                try {
                    a = Double.parseDouble(s1.replace(',', '.'));
                    b = Double.parseDouble(s2.replace(',', '.'));
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : saisissez des nombres valides.");
                    continue;
                }

                if (!operateur.matches("[+\\-*/]")) {
                    System.out.println("Erreur : opérateur invalide (utilisez + - * /).");
                    continue;
                }

                Operation operation = new Operation(a, operateur, b);

                oos.writeObject(operation);
                oos.flush();

                Operation reponse = (Operation) ois.readObject();

                if (reponse.getErreur() != null) {
                    System.out.println("Serveur : erreur - " + reponse.getErreur());
                } else {
                    System.out.println("Serveur : " + reponse + " = " + reponse.getResultat());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
