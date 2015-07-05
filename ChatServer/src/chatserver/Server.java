/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

/**
 *
 * @author rb
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    
    private static final int PORT = 5600;

    private static HashSet<String> names = new HashSet<String>();

    
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

   
    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

   
    private static class Handler extends Thread {
        private String name;
        private Socket socket;
       private DataInputStream in1;
       private BufferedReader in;
        private PrintWriter out;
         private OutputStream out2;
         private InputStream in2;
       
        public Handler(Socket socket) {
            this.socket = socket;
        }

      
        public void run() {
            try {
              
                in2=socket.getInputStream();
                out2=socket.getOutputStream();
                in = new BufferedReader(new InputStreamReader(in2));
                in1 = new DataInputStream((in2));
                out = new PrintWriter(out2, true);
        

               
                while (true) {
                    out.println("SUBMITNAME");
                    
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED");
                writers.add(out);

                while (true) {
                    
             
               
                   String input = in.readLine();
              /*     String input,extn=""; 
            input=in1.readUTF(); 
            System.out.println("\n"+input); 
            int flag=0,i; */
                    if (input == null) {
                        return;
                    }
     /*                for(i=0;i<input.length();i++) 
                { 
                     
                    if(input.charAt(i)=='.' || flag==1) 
                    { 
                    flag=1; 
                    extn+=input.charAt(i); 
                    } 
                } 
                         if(extn.equals(".jpg") || extn.equals(".png")) 
                  {             
                    File file = new File("RecievedImage"+input); 
                    FileOutputStream fout = new FileOutputStream(file); 
              
                    //receive and save image from client 
                    byte[] readData = new byte[1024]; 
                    while((i = in1.read(readData)) != -1) 
                    {
                        fout.write(readData, 0, i); 
                        if(flag==1) 
                        { 
                        out.println("Image Has Been Received"); 
                        flag=0; 
                        } 
                    } 
                fout.flush(); 
                fout.close();
                  }else{ */
                    for(PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                    
          //               }
                }
            } catch (IOException e) {
                System.out.println(e);
            }finally {
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
           
    }
    }
}