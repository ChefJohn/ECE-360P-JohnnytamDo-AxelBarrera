

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class BookClient {
    public static void main(String[] args) throws Exception{
        boolean mode = true;         //true == udp, false == tcp
        String hostAddress;
        int tcpPort;
        int udpPort;
        int clientId;
        int loanID;             //keeps track of the amount of loans there are
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddr = InetAddress.getByName("localhost");

        Socket tcpSocket = null;           //socket for tcp


        if (args.length != 2) {
            System.out.println("ERROR: Provide 2 arguments: command-file, clientId");
            System.out.println("\t(1) command-file: file with commands to the server");
            System.out.println("\t(2) clientId: an integer between 1..9");
            System.exit(-1);
        }

        String commandFile = args[0];
        clientId = Integer.parseInt(args[1]);
        hostAddress = "localhost";
        tcpPort = 7000;// hardcoded -- must match the server's tcp port
        udpPort = 8000;// hardcoded -- must match the server's udp port
        String fileName = new String("out_" + Integer.toString(clientId));
        FileWriter fileWriter = new FileWriter(fileName+".txt");
        BufferedWriter file = new BufferedWriter(fileWriter);

        try {
            Scanner sc = new Scanner(new FileReader(commandFile+".txt"));

            while (sc.hasNextLine()) {
                String cmd = sc.nextLine();
                String[] tokens = cmd.split(" ");

                String command = "";
                int commandID;
                String arg1 = "";
                String arg2 = "";

                boolean switchModeFlag = false;         //this flags determines if we are switching modes or not

                if (tokens[0].equals("set-mode")) {
                    // TODO: set the mode of communication for sending commands to the server
                    arg1 = tokens[1];
                    command = interpretCommand(0, tokens);
                    if ((mode && tokens[1].equals("t")) || (!mode && tokens[1].equals("u"))) switchModeFlag = true;

                } else if (tokens[0].equals("begin-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    arg2 = tokens[2];
                    command = interpretCommand(1, tokens);
                } else if (tokens[0].equals("end-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    command = interpretCommand(2, tokens);
                } else if (tokens[0].equals("get-loans")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    command = interpretCommand(3, tokens);
                } else if (tokens[0].equals("get-inventory")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    command = interpretCommand(4, tokens);
                } else if (tokens[0].equals("exit")) {
                    // TODO: send appropriate command to the server
                    command = interpretCommand(5,tokens);
                    file.close();
                } else {
                    System.out.println("ERROR: No such command");
                }

                String serverResponse = "";
                if (mode){
                    sendUDP(command, udpPort, serverAddr, socket);
                
                    if (switchModeFlag){
                        tcpSocket = new Socket("localhost", tcpPort);
                        mode = false;
                        switchModeFlag = false;
                    }

                    serverResponse = receiveUDP(socket);
                    
                } else {
                    sendTCP(command, tcpPort, tcpSocket);
                    serverResponse = receiveTCP(tcpSocket);

                    if (switchModeFlag){
                        tcpSocket.close();
                        mode = true;
                        switchModeFlag = false;
                    }
                }
                writeToOut(serverResponse, file);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String interpretCommand(int commandID, String[] token){
        String message = "";

        //interpretting commands that have 0 args
        if (commandID == 4 || commandID == 5){
            message += commandID;
        
        //interpretting commands that have 1 arg
        } else if (commandID == 0 || commandID == 2 || commandID == 3){                   
            message = commandID + "|" + token[1];
        
        //interpretting commands that have 2 args (so just begin-loan command)
        } else {
            message = commandID + "|" + token[1] + "|";
            for(int i =2;i< token.length-1;i++){
                message+=token[i] +" ";
            }
            message += token[token.length-1];
        }

        return message;
    }

    //function that sends a message to the server thru the UDP connection
    private static void sendUDP(String message, int port,
                                  InetAddress address,
                                  DatagramSocket socket)throws IOException{
        byte[] data = message.getBytes();
        DatagramPacket pack = new DatagramPacket(data,data.length, address, port);
        socket.send(pack);
    }

    //function that receives a message from the server thru the UDP connection
    private static String receiveUDP(DatagramSocket socket) throws IOException{
        byte[] data = new byte[1024];
        DatagramPacket pack = new DatagramPacket(data,data.length);
        socket.receive(pack);
        String message = new String(pack.getData(),0,pack.getLength());
        return message;
    }

    //function that sends a message to the server thru the TCP connection
    private static void sendTCP(String message, int port, Socket socket)throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);

        out.writeUTF(message);
    }

    //function that receives a message from the server thru the TCP connection
    private static String receiveTCP(Socket socket) throws IOException{
        InputStream inputStream = socket.getInputStream();
        DataInputStream in = new DataInputStream(inputStream);
        
        String message = in.readUTF();
        return message;
    }

    private static void writeToOut(String output, BufferedWriter f)throws Exception {
        if(!output.equals("NONE")){
            f.write(output);
        }
    }

}