import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

//IMPORTANT NOTE: UNCOMMENT OUT THE ARGS SHIT WHEN DONE TESTING

public class BookServer {
    int loanID;
    ReentrantLock loanLock = new ReentrantLock();

    public BookServer() {
        this.loanID = 1;
    }

    public static void main(String[] args) throws Exception {
        int tcpPort;
        int udpPort;
        // if (args.length != 1) {
        //     System.out.println("ERROR: Provide 1 argument: input file containing initial inventory");
        //     System.exit(-1);
        // }
        //String fileName = args[0];
        tcpPort = 7000;
        udpPort = 8000;
        /**
         * Initialize HashMaps
         */
        HashMap<String, Integer> bookCountMap = new HashMap<>();

        /**
         * Initialize Server Sockets
         */
        DatagramSocket udpss = new DatagramSocket(udpPort);
        ServerSocket tcpss = new ServerSocket(tcpPort);
        BookServer server = new BookServer();

        // parse the inventory file (also change this to the filename variable when done testing)
        File file = new File("/Users/johnnydo/Documents/School/Concurrent/Project3/ECE-360P-JohnnytamDo-AxelBarrera/input_file.txt");

        try {
            Scanner sc = new Scanner(file);

            //for each line, map the number of books to corresponding book name
            while(sc.hasNextLine()){
                String[] line = sc.nextLine().split("\" ");
                bookCountMap.put(line[0] + "\"", Integer.parseInt(line[1]));
            }

            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found!");
        }

        //TESTING PARSING
        for (Map.Entry<String,Integer> mapElement : bookCountMap.entrySet()) {
            System.out.println(mapElement.getKey());
            System.out.println(mapElement.getValue());
        }

        // TODO: handle request from clients
        UDPClientHandler o = new UDPClientHandler(bookCountMap,udpss,server,tcpss);
        Thread t = new Thread(o);
        t.start();
        t.join();
    }

    public int getNewLoan(){
        loanLock.lock();
        int answer = loanID;
        loanID+=1;
        loanLock.unlock();
        return answer;
    }
}
