import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class UDPClientHandler implements Runnable {
    HashMap<String,Integer> bookCountMap;
    HashMap<String,LibUser> userClassMap;
    HashMap<Integer,LibUser> loanClassMap;
    DatagramSocket socket;
    BookServer server;

    public UDPClientHandler(HashMap<String, Integer> bookCountMap,
                            HashMap<String, LibUser> userClassMap,
                            HashMap<Integer, LibUser> loanClassMap,
                            DatagramSocket socket,
                            BookServer server) {
        this.bookCountMap = bookCountMap;
        this.userClassMap = userClassMap;
        this.loanClassMap = loanClassMap;
        this.socket = socket;
        this.server = server;
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

    private String executeCommand(String[] token){
        //TODO: Does the bulk of the work, handling all the commands
        String result = "";
        LibUser user;
        switch (token[0]){
            case "0":
                //set mode
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
                    result = "Request Failed - We do not have this book";
                    return result;
                }
                //checking to see if requested book has avilability
                if (bookCountMap.get(token[2]) == 0){
                    result = "Request Failed - Book not available";
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
                    user = new LibUser(null, bookCountMap);
                }

                result = user.userEndLoan(endLoanID);
                break;
            case "3":
                // get loans
                String username = token[1];
                user = userClassMap.get(username);

                if (user == null){
                    user = new LibUser(null, bookCountMap);
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

    private void sendClientToTCP(){
        //TODO: When set-mode t command comes this should execute the neccesary server changes
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
