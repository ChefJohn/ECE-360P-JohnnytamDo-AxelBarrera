import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.net.*;
import java.util.HashMap;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;


public class UDPClientHandler implements Runnable {
    HashMap<String,Integer> bookCountMap;
    HashMap<String,LibUser> userClassMap;
    HashMap<Integer,LibUser> loanClassMap;
    DatagramSocket socket;
    ServerSocket tcpsocket;
    BookServer server;

    public UDPClientHandler(HashMap<String, Integer> bookCountMap,
                            DatagramSocket socket,
                            BookServer server,
                            ServerSocket tcpsocket) {
        this.bookCountMap = bookCountMap;
        this.userClassMap = new HashMap<>();
        this.loanClassMap = new HashMap<>();
        this.socket = socket;
        this.server = server;
        this.tcpsocket = tcpsocket;
    }


    @Override
    public void run() {
        //TODO: Handles UDP Messages and executes necessary commands
        while(true){
            try {
                DatagramPacket pack = receiveUDP(socket);
                String message = extractMessage(pack);
                String[] tokens = tokenizeMessage(message);
                String response = executeCommand(tokens);
                sendUDP(response,pack,socket);
            }catch (Exception e){System.out.println(e);}
        }
    }

    private String executeCommand(String[] token)throws Exception{
        //TODO: Does the bulk of the work, handling all the commands
        String result = "";
        LibUser user;
        switch (token[0]){
            case "0":
                //set mode
                if(token[1].equals("t")){
                    sendClientToTCP();
                    result = "The communication mode is set to TCP\n";
                }else if(token[1].equals("u")){
                    result = "The communication mode is set to UDP\n";
                }else{
                    result = "NONE";
                }
                break;
            case "1":
                //begin loan
                if(!userClassMap.containsKey(token[1])){
                    user = new LibUser(token[1],bookCountMap);
                    userClassMap.put(token[1],user);
                }else{
                    user = userClassMap.get(token[1]);
                }
                //checking to see if requested book exists
                if (bookCountMap.get(token[2]) == null){
                    result = "Request Failed - We do not have this book\n";
                    return result;
                }
                //checking to see if requested book has avilability
                if (bookCountMap.get(token[2]) == 0){
                    result = "Request Failed - Book not available\n";
                    return result;
                }
                int loanID = server.getNewLoan();
                result = user.userBeginLoan(loanID,token[2]);
                loanClassMap.put(loanID,user);
                break;
            case "2":
                // end loan
                int endLoanID = Integer.parseInt(token[1]);
                user = loanClassMap.get(endLoanID);

                if (user == null){
                    user = new LibUser(token[1], bookCountMap);
                }

                result = user.userEndLoan(endLoanID);
                break;
            case "3":
                // get loans
                String username = token[1];
                user = userClassMap.get(username);

                if (user == null){
                    user = new LibUser(token[1], bookCountMap);
                }

                result = user.userGetLoan();
                break;
            case "4":
                // get inventory
                for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
                    result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
                }
                break;
            case "5":
                // exit
                printInventory();
                result="NONE";
                break;
        }
        return result;
    }

    private String[] tokenizeMessage(String message){
        //TODO: Splits the message, 0th indx = command,
        // 1st indx = 1st parameter, 2nd indx = 2nd parameter
        String[] answer = message.split("\\|");
        return answer;
    }

    private void printInventory()throws Exception{
        String result = "";
        for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
            result +=  mapElement.getKey() + " " + mapElement.getValue() + "\n";
        }
        FileWriter fileWriter = new FileWriter("inventory.txt");
        BufferedWriter file = new BufferedWriter(fileWriter);
        file.write(result);
        file.close();
    }

    private void sendClientToTCP() throws Exception{
        Socket c = tcpsocket.accept();
        TCPClientHandler o = new TCPClientHandler(
                bookCountMap,userClassMap,loanClassMap,c,server);
        Thread t = new Thread(o);
        t.start();
    }

    private void sendUDP(String message,
                                DatagramPacket receivedPack,
                                DatagramSocket socket)throws Exception {
        InetAddress address = receivedPack.getAddress();
        int port = receivedPack.getPort();
        byte[] data = message.getBytes();
        DatagramPacket pack = new DatagramPacket(data,data.length,address,port);
        socket.send(pack);
    }

    private String extractMessage(DatagramPacket pack){
        String message = new String(pack.getData(),0, pack.getLength());
        return message;
    }

    private DatagramPacket receiveUDP(DatagramSocket socket) throws IOException{
        byte[] data = new byte[1024];
        DatagramPacket pack = new DatagramPacket(data,data.length);
        socket.receive(pack);
        return pack;
    }
}
