
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class BookClient {
    public static void main(String[] args) throws Exception{
        String hostAddress;
        int tcpPort;
        int udpPort;
        int clientId;
        int loanID;             //keeps track of the amount of loans there are
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddr = InetAddress.getByName("localhost");


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
            Scanner sc = new Scanner(new FileReader(commandFile));

            while (sc.hasNextLine()) {
                String cmd = sc.nextLine();
                String[] tokens = cmd.split(" ");

                int commandID;
                String arg1 = "";
                String arg2 = "";

                if (tokens[0].equals("set-mode")) {
                    // TODO: set the mode of communication for sending commands to the server
                    arg1 = tokens[1];
                    interpretCommand(0, arg1, arg2);
                } else if (tokens[0].equals("begin-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    arg2 = tokens[2];
                    interpretCommand(1, arg1, arg2);
                } else if (tokens[0].equals("end-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    interpretCommand(2, arg1, arg2);
                } else if (tokens[0].equals("get-loans")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    arg1 = tokens[1];
                    interpretCommand(3, arg1, arg2);
                } else if (tokens[0].equals("get-inventory")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                    interpretCommand(4, arg1, arg2);
                } else if (tokens[0].equals("exit")) {
                    // TODO: send appropriate command to the server
                    interpretCommand(5, arg1, arg2);
                } else {
                    System.out.println("ERROR: No such command");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String interpretCommand(int commandID, String arg1, String arg2){
        String message = "";

        //interpretting commands that have 0 args
        if (commandID == 4 || commandID == 5){
            message += commandID;
        
        //interpretting commands that have 1 arg
        } else if (commandID == 0 || commandID == 2 || commandID == 3){                   
            message = commandID + "|" + arg1;
        
        //interpretting commands that have 2 args (so just begin-loan command)
        } else {
            message = commandID + "|" + arg1 + "|" + arg2;
        }

        return message;
    }

    private static void sendUDP(String message, int port,
                                  InetAddress address,
                                  DatagramSocket socket)throws IOException{
        byte[] data = message.getBytes();
        DatagramPacket pack = new DatagramPacket(data,data.length, address, port);
        socket.send(pack);
    }

    private static String receiveUDP(DatagramSocket socket) throws IOException{
        byte[] data = new byte[1024];
        DatagramPacket pack = new DatagramPacket(data,data.length);
        socket.receive(pack);
        String message = new String(pack.getData(),0,pack.getLength());
        return message;
    }

}
