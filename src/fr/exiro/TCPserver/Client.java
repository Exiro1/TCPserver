package fr.exiro.TCPserver;

import java.io.*;
import java.net.Socket;

public class Client  extends Thread {
    protected Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            in = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            out = socket.getOutputStream();
            printer = new PrintWriter(out,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    InputStream in;
    OutputStream out;
    PrintWriter printer;
    BufferedReader reader;

    /**
     * envoi des données au client
     * @param arr données a transmettre
     * @param len taille des données
     */
    public void writeData(byte[] arr,int len){
        try {
        out.write(arr,0,len);
        out.flush();
        }catch (IOException e){
            System.out.println("error when writing to client");
        }

    }

    /**
     * envoi de texte au client
     * @param str text à envoyer
     */
    public void writeString(String str){
            printer.println(str);
    }

    /**
     * Thread d'écoute
     * permet a la boucle principale de la fonction startServer() du Main
     * de continuer à accepter de nouveau client, car l'écoute des données reçu inclus une boucle quasi-infini
     * On aura donc un thread par client
     */
    public void run() {
        try {
            String text;
            text = reader.readLine();
            //on verifie si le client est un pc ou le raspberry
            if(text.contains("RASP")){
                //dans le cas ou ç'est un raspberry, on le retire de la liste des clients
                System.out.println("Raspberry connected");
                Main.clients.remove(this);
                if(Main.rasp != null){
                    //on stop le thread du dernier Rasp
                    Main.rasp.closeConn();
                }
                //puis on crée une instance de Raspberry, une classe fille de Client avec le socket du raspberry
                Main.rasp = new Raspberry(this.socket);
                //puis on lance sa boucle d'écoute
                Main.rasp.start();
                //on sort de la methode run(), arretant ainsi ce thread
                return;
            }else{
                System.out.println("recv from "+ socket.getRemoteSocketAddress().toString() +" : "+text);
            }
            if(Main.dataLen>0)
                writeData(Main.lastData,Main.dataLen);
            do {  // boucle quasi-infini d'écoute
                text = reader.readLine();
                if(Main.rasp != null){
                    Main.rasp.writeString(text);
                }
            } while (!text.equals("END"));
            System.out.println("Client disconnected");
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch(NullPointerException e){
            System.out.println("Server exception 2: " + e.getMessage());
        }
        Main.clients.remove(this);
    }
}
