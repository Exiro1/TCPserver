package fr.exiro.TCPserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Raspberry extends Client{


    public Raspberry(Socket socket) {
        super(socket);
    }

    public void closeConn(){
        stop = true;
    }
    volatile boolean stop = false;

    public void writeToFile(byte[] data, int len){

        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(Main.filename, true));
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<len;i++){
                int v = data[i] < 0 ? data[i] + 256 : data[i];
                sb.append(v+",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append('\n');
            out.write(sb.toString());
            out.close();
        }catch(IOException e){

        }

    }


    @Override
    public void run() {
        try {
            byte[] data = new byte[255];
            int len=0;
            while(!stop){
                //on attend de recevoir des données et on les stock dans data
                len = in.read(data);
                if(len>0){
                    //System.out.println("new data : "+ Arrays.toString(data));
                    //on envoie les données reçu a tout les clients connectés
                    Main.writeToAllClient(data,len);
                    Main.lastData = data;
                    Main.dataLen = len;
                    writeToFile(data, len);

                    //si les données contiennent "END"alors on ferme la connexion
                    if(len==3) {
                      if(data[0] == 'E' && data[1]=='N' && data[2] == 'D'){
                          System.out.println("Raspberry disconnected");
                          Main.rasp = null;
                          break;
                      }
                    }
                }else if(len==-1){
                    System.out.println("Raspberry disconnected");
                    Main.rasp = null;
                    break;
                }
            }
            socket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
            System.out.println("Raspberry disconnected");
            Main.rasp = null;
        }
    }
}
