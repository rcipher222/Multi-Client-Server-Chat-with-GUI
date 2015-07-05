/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author rb
 */
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class client {
      private DataOutputStream out1;
      private DataInputStream in1;
       private BufferedReader in;
        private PrintWriter out;
         private OutputStream out2;
         private InputStream in2;
       
    JFrame Filepicker= new JFrame("file picker");;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(8, 40);
    JButton openButton = new JButton("Open");
    
    private JFrame mainFrame;
//   private JLabel headerLabel;
//   private JLabel statusLabel;
//  private JPanel controlPanel;
    
    public client()throws IOException {

        
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");        
        frame.getContentPane().add(openButton,"East");
        frame.pack();

        
        textField.addActionListener(new ActionListener() {
            
            
            public void actionPerformed(ActionEvent e) {                
                out.println(textField.getText());
                textField.setText("");
                
            }
        });
        openButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                   
               //     prepareGUI();
                mainFrame = new JFrame("pic up the file to transfer");
      mainFrame.setSize(400,400);
          //        mainFrame.setVisible(true); 
      final JFileChooser  fileDialog = new JFileChooser();
  //    JButton showFileDialogButton = new JButton("Open File");
//    showFileDialogButton.addActionListener(new ActionListener() {
   //      @Override
  //      public void actionPerformed(ActionEvent e) {
            int returnVal = fileDialog.showOpenDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
               java.io.File file = fileDialog.getSelectedFile();
               out.println("File Selected :" + file.getAbsolutePath());
                      
      //         if(file.toString().split(".")[1].equalsIgnoreCase("jpg")){
                   try {
                       DisplayImage(file.getAbsolutePath());
                             } catch (IOException ex) {
                       Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
                   }
                       /*     try{
                       
                       FileInputStream fin = new FileInputStream(file);
                       out1.writeUTF(file.getName());
                       out.println("Sending image...");
                       byte[] readData = new byte[1024];
                       int i;
                       
                       while((i = fin.read(readData)) != -1)
                       {
                       out1.write(readData, 0, i);
                       }
                       System.out.println("Image sent");
                       messageArea.append("\nImage Has Been Sent");
                       fin.close();
                       }catch(IOException ex)
                       {System.out.println("Image ::"+ex);}
                   */           

      //         }
               
               textField.setText(""); 
            }
            else{
               out.println(textField.getText());
                textField.setText("");      
            }      
    //     }
  //   });
  //    controlPanel.add(showFileDialogButton);
      mainFrame.setVisible(false);  
              
                 }
        });
    }
    public void DisplayImage(String s) throws IOException
    {
        BufferedImage img=ImageIO.read(new File(s));
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,300);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    /* 
 private void prepareGUI(){
      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(400,400);
     mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
        
      mainFrame.setVisible(true);  
   }*/

   
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

    
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    
    private void run() throws IOException {

       
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 5600);
  //      if(socket.getInputStream().)
            
                in2=socket.getInputStream();
                out2=socket.getOutputStream();
                in = new BufferedReader(new InputStreamReader(in2));
                out = new PrintWriter(out2, true);
       
        
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    
    public static void main(String[] args) throws Exception {
        client client = new client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
