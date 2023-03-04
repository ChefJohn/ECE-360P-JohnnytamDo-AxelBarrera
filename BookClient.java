import com.sun.xml.internal.bind.v2.TODO;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class BookClient {
    public static void main(String[] args) {
        String hostAddress;
        int tcpPort;
        int udpPort;
        int clientId;
        try{
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddr = InetAddress.getByName("localhost");
        }catch (SocketException e){System.out.println(e);}
        catch (UnknownHostException e){System.out.println(e);}


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

        try {
            Scanner sc = new Scanner(new FileReader(commandFile));

            while (sc.hasNextLine()) {
                String cmd = sc.nextLine();
                String[] tokens = cmd.split(" ");

                if (tokens[0].equals("set-mode")) {
                    // TODO: set the mode of communication for sending commands to the server
                } else if (tokens[0].equals("begin-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                } else if (tokens[0].equals("end-loan")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                } else if (tokens[0].equals("get-loans")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                } else if (tokens[0].equals("get-inventory")) {
                    // TODO: send appropriate command to the server and display the
                    // appropriate responses form the server
                } else if (tokens[0].equals("exit")) {
                    // TODO: send appropriate command to the server
                } else {
                    System.out.println("ERROR: No such command");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
