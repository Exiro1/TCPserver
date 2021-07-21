package fr.exiro.TCPserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    static int PORT = 11000;

    static public Raspberry rasp;
    static ArrayList<Client> clients;

    static byte[] lastData;
    static int dataLen=0;

    public static void main(String args[]){
        clients = new ArrayList<>();
        if(args.length>0)
            PORT = Integer.parseInt(args[0]);
        startServer();
    }

    /**
     * écrit a tout les clients connectés
     * @param arr données à retransmettre
     * @param len taille des données
     */
    public static void writeToAllClient(byte[] arr, int len){
        for(Client c : clients){
            c.writeData(arr,len);
        }
    }

    public static void startServer(){

        //on crée le serveur TCP
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                //on attend la connexion d'un appareil
                Socket socket = serverSocket.accept();
                System.out.println("New client connected - " + socket.getRemoteSocketAddress());
                //on ajoute le client a la liste de client puis on lance son thread associé
                clients.add(new Client(socket));
                clients.get(clients.size()-1).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }



}
