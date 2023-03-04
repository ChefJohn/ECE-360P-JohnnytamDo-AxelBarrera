import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class UDPClientHandler implements Runnable {
    HashMap<String,Integer> bookCountMap;
    HashMap<String,LibUser> userClassMap;
    HashMap<String,LibUser> loanClassMap;
    DatagramSocket socket;

    public UDPClientHandler(HashMap<String, Integer> bookCountMap,
                            HashMap<String, LibUser> userClassMap,
                            HashMap<String, LibUser> loanClassMap,
                            DatagramSocket socket) {
        this.bookCountMap = bookCountMap;
        this.userClassMap = userClassMap;
        this.loanClassMap = loanClassMap;
        this.socket = socket;
    }


    @Override
    public void run() {
        //TODO: Handles UDP Messages and executes necessary commands
    }

    private void executeCommand(String[] token){
        //TODO: Does the bulk of the work, handling all the commands
    }

    private String[] tokenizeMessage(){
        //TODO: Splits the message, 0th indx = command,
        // 1st indx = 1st parameter, 2nd indx = 2nd parameter
        String[] answer = new String[3];

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
